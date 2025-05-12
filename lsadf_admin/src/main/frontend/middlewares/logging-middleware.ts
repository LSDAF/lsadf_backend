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

import { Middleware, MiddlewareContext, MiddlewareNext } from '@vaadin/hilla-frontend';

// A middleware is an async function, that receives the `context` and `next`
export const LoggingMiddleware: Middleware = async function(
    context: MiddlewareContext,
    next: MiddlewareNext
) {
    // The context object contains the call arguments. See the `call` method
    // of the `ConnectClient` class for their descriptions.
    const {endpoint, method, params} = context;
    console.log(
        `Sending request to endpoint: ${endpoint} ` +
        `method: ${method} ` +
        `parameters: ${JSON.stringify(params)} `
    );

    // Also, the context contains the `request`, which is a Fetch API `Request`
    // instance to be sent over the network.
    const request: Request = context.request;
    console.log(`${request.method} ${request.url}`);

    // Call the `next` async function to send the request and get the response.
    const response: Response = await next(context);

    // The response is a Fetch API `Response` object.
    console.log(`Received response: ${response.status} ${response.statusText}`);

    // Clone the response if reading the body is necessary
    const responseClone = response.clone();
    console.log(await responseClone.text());

    // A middleware returns a response.
    return response;
}