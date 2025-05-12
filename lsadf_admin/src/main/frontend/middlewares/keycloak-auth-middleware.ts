///
/// Copyright © 2024-2025 LSDAF
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import {Middleware} from "@vaadin/hilla-frontend";
import keycloak from "Frontend/keycloak";


export const keycloakAuthMiddleware: Middleware = async function (context, next) {
    const request: Request = context.request;

    if (keycloak.token) {
        // add headers + authorization header to request
        request.headers.set('Authorization', `Bearer ${keycloak.token}`);
        console.log("Added Authorization header to request");
    }

    return next(context);
};