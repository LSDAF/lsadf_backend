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

import {ConnectClient, ConnectClientOptions} from "@vaadin/hilla-frontend";
import {LoggingMiddleware} from "Frontend/middlewares/logging-middleware";
import {keycloakAuthMiddleware} from "Frontend/middlewares/keycloak-auth-middleware";


const connectClientConfiguration: ConnectClientOptions = {
    prefix: "/connect",
    middlewares: [LoggingMiddleware, keycloakAuthMiddleware]
};

const connectClient: ConnectClient = new ConnectClient(connectClientConfiguration);

export default connectClient;