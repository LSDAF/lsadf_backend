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

import Keycloak, {KeycloakConfig} from "keycloak-js";


const keycloakConfig: KeycloakConfig = {
    url: 'http://localhost:8081',
    realm: 'LSADF',
    clientId: 'lsadf-admin-ui',
}

const keycloak: Keycloak = new Keycloak(keycloakConfig);

export async function initKeycloak() {
    try {
        console.log("Initializing Keycloak...");
        keycloak.init({
            onLoad: 'login-required',
            pkceMethod: 'S256',
            redirectUri: window.location.origin + '/',
            checkLoginIframe: false,
        }).then((authenticated) => {
            if (!authenticated) {
                console.log("Unable to authenticate user. Logging out...");
            } else {
                console.log("Authenticated");
                console.log({keycloak});
                setInterval(() => {
                    keycloak
                        .updateToken(30)
                        .then((refreshed) => {
                            if (refreshed) {
                                console.log("Token refreshed");
                            }
                        })
                        .catch(() => {
                            console.error("Failed to refresh token. Logging out...");
                            keycloak.logout();
                        });
                }, 30000);
            }
        });
    } catch (err) {
        console.log("Keycloak init error", err);
    }
}

export default keycloak;