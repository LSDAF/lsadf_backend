package com.lsadf.core.configurations.interceptors;

import com.lsadf.core.constants.LogColor;
import com.lsadf.core.models.RequestLog;
import com.lsadf.core.properties.HttpLogProperties;
import com.lsadf.core.services.ClockService;
import com.lsadf.core.utils.ColorUtils;
import com.lsadf.core.utils.DateUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lsadf.core.constants.LogColor.*;

@Slf4j(topic = "[HTTP]")
public class RequestLoggerInterceptor implements HandlerInterceptor {

    private static final String ANONYMOUS_USER = "anonymousUser";

    private static final String FILE = "FILE";
    private static final Marker FILE_MARKER = MarkerFactory.getMarker(FILE);
    private static final String CONSOLE = "CONSOLE";
    private static final Marker CONSOLE_MARKER = MarkerFactory.getMarker(CONSOLE);

    private final Set<String> loggedMethods;
    private final boolean colorEnabled;
    private final ClockService clockService;


    public RequestLoggerInterceptor(HttpLogProperties httpLogProperties,
                                    ClockService clockService) {
        this.loggedMethods = httpLogProperties.getLoggedMethods().stream().map(HttpMethod::toString).collect(Collectors.toSet());
        this.colorEnabled = httpLogProperties.isColorEnabled();
        this.clockService = clockService;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (loggedMethods.contains(request.getMethod())) {
            Date now = clockService.nowDate();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            RequestLog requestLog = buildRequestLog(request,
                    response,
                    now,
                    authentication);
            logToConsole(requestLog, colorEnabled);
            logToFile(requestLog);
        }
    }

    /**
     * Logs request to console
     *
     * @param requestLog   RequestLog object to log
     * @param colorEnabled true if color is enabled, false otherwise
     */
    private static void logToConsole(RequestLog requestLog, boolean colorEnabled) {
        String usernameLog = requestLog.getUsername() != null ? "[user:" + requestLog.getUsername() + "]" : "";
        int status = requestLog.getStatus();
        String statusString;
        if (colorEnabled) {
            LogColor color;
            if (status >= 200 && status < 300) {
                color = GREEN;
            } else if (status >= 400 && status < 500) {
                color = YELLOW;
            } else {
                color = RED;
            }
            statusString = ColorUtils.colorizeString(String.valueOf(status), color);
        } else {
            statusString = String.valueOf(status);
        }
        log.info(CONSOLE_MARKER, "[{}][{}][{}][{}]{}",
                statusString,
                requestLog.getMethod(),
                requestLog.getRequestUri(),
                requestLog.getNow(),
                usernameLog);
    }

    /**
     * Logs request to file
     *
     * @param requestLog RequestLog object to log
     */
    private static void logToFile(RequestLog requestLog) {
        String usernameLog = requestLog.getUsername() != null ? "[user:" + requestLog.getUsername() + "]" : "";
        log.info(FILE_MARKER, "[{}][{}][{}][{}]{}",
                requestLog.getStatus(),
                requestLog.getMethod(),
                requestLog.getRequestUri(),
                requestLog.getNow(),
                usernameLog);
    }

    /**
     * Builds RequestLog object from request, response, now, and authentication
     *
     * @param request        HttpServletRequest object
     * @param response       HttpServletResponse object
     * @param now            Date object representing current time
     * @param authentication Authentication object
     * @return RequestLog object
     */
    private static RequestLog buildRequestLog(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Date now,
                                              Authentication authentication) {
        Object principal = authentication.getPrincipal();
        String username = null;
        if (principal instanceof String principalString && !principal.equals(ANONYMOUS_USER)) {
            username = principalString;
        }
        String nowString = DateUtils.dateToString(now);
        RequestLog.RequestLogBuilder requestLogBuilder = RequestLog.builder()
                .status(response.getStatus())
                .now(nowString)
                .method(request.getMethod())
                .requestUri(request.getRequestURI())
                .username(username);
        return requestLogBuilder.build();
    }
}
