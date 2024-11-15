package com.lsadf.lsadf_backend.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lsadf.core.constants.ClaimsConstants.*;

@UtilityClass
public class TokenUtils {
    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES = "roles";

    /**
     * Get roles from jwt object
     * @param jwt the jwt
     * @return the roles
     */
    public static List<GrantedAuthority> getRolesFromJwt(Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();
        List<String> roles = new ArrayList<>();
        if (claims.containsKey(REALM_ACCESS)) {
            Map<String, Object> realmAccess = (Map<String, Object>) claims.get(REALM_ACCESS);
            roles = ((List<String>) realmAccess.get(ROLES));
        }
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());
    }

    /**
     * Get username from jwt
     *
     * @param jwt the jwt
     * @return the username email
     */
    public static String getUsernameFromJwt(Jwt jwt) {
        return jwt.getClaimAsString(PREFERRED_USERNAME);
    }

    /**
     * Get name from jwt
     *
     * @param jwt the jwt
     * @return the name
     */
    public static String getNameFromJwt(Jwt jwt) {
        return jwt.getClaimAsString(NAME);
    }

    /**
     * Get email verified from jwt
     *
     * @param jwt the jwt
     * @return the email verified
     */
    public static boolean getEmailVerifiedFromJwt(Jwt jwt) {
        return jwt.getClaimAsBoolean(EMAIL_VERIFIED);
    }
}
