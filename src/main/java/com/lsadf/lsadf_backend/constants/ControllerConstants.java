package com.lsadf.lsadf_backend.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Controller constants class
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ControllerConstants {

    public static final String GAME_SAVE = "/api/v1/game_save";
    public static final String AUTH = "/api/v1/auth";
    public static final String USER = "/api/v1/user";
    public static final String ADMIN = "/api/admin";
    public static final String CURRENCY = "/api/v1/currency";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Swagger {
        public static final String GAME_SAVE_CONTROLLER = "Game Save Controller";
        public static final String USER_CONTROLLER = "User Controller";
        public static final String AUTH_CONTROLLER = "Auth Controller";
        public static final String ADMIN_CONTROLLER = "Admin Controller";
        public static final String CURRENCY_CONTROLLER = "Currency Controller";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Currency {
        public static final String GAME_SAVE_ID = "/{game_save_id}";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Admin {
        public static final String GLOBAL_INFO = "/global_info";

        public static final String USERS = "/users";
        public static final String USER_ID = USERS + "/{user_id}";
        public static final String USER_GAME_SAVES = USERS + "/{user_id}/game_saves";
        public static final String USER_EMAIL = USERS + "/email/{user_email}";
        public static final String CREATE_USER = USERS + "/new";
        public static final String SEARCH_USERS = USERS + "/search";

        public static final String GAME_SAVES = "/game_saves";
        public static final String CREATE_GAME_SAVE = GAME_SAVES + "/new";
        public static final String SEARCH_GAME_SAVES = GAME_SAVES + "/search";
        public static final String GAME_SAVE_ID = GAME_SAVES + "/{game_save_id}";

        public static final String CACHE = "/cache";
        public static final String TOGGLE_CACHE = CACHE + "/toggle";
        public static final String CACHE_ENABLED = CACHE + "/enabled";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class GameSave {
        public static final String GENERATE = "/generate";
        public static final String UPDATE_NICKNAME = "/{game_save_id}/nickname";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Auth {
        public static final String LOGIN = "/login";
        public static final String REFRESH_LOGIN = "/refresh_login";
        public static final String LOGOUT = "/logout";
        public static final String REGISTER = "/register";
        public static final String VALIDATE_TOKEN = "/validate";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class User {
        public static final String USER_ME = "/me";
        public static final String USER_ME_GAME_SAVES = "/me/game_saves";
    }
}
