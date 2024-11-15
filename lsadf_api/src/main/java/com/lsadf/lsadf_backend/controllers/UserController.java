package com.lsadf.lsadf_backend.controllers;

import com.lsadf.core.constants.ControllerConstants;
import com.lsadf.core.constants.ResponseMessages;
import com.lsadf.core.exceptions.http.UnauthorizedException;
import com.lsadf.core.models.UserInfo;
import com.lsadf.core.responses.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.lsadf.core.configurations.SwaggerConfiguration.BEARER_AUTHENTICATION;
import static com.lsadf.core.configurations.SwaggerConfiguration.OAUTH2_AUTHENTICATION;

/**
 * Controller for user operations
 */
@RequestMapping(value = ControllerConstants.USER)
@Tag(name = ControllerConstants.Swagger.USER_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface UserController {


    /**
     * Gets the logged UserInfo user info
     * @param jwt the jwt
     * @return the user info
     * @throws UnauthorizedException
     */
    @GetMapping(value = ControllerConstants.User.ME)
    @Operation(summary = "Gets the logged UserInfo user info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    ResponseEntity<GenericResponse<UserInfo>> getUserInfo(@AuthenticationPrincipal Jwt jwt);

}
