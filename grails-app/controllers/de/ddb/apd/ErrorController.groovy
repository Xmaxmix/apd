/*
 * Copyright (C) 2013 FIZ Karlsruhe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.ddb.apd

import grails.util.Environment

/**
 * Central controller for handling error situations (404, 500, etc).
 */
class ErrorController {

    def configurationService

    def uncaughtException() {
        log.error "uncaughtException(): An uncaught exception occured in the frontend."
        serverError();
    }

    /**
     * Handler method for error 500 situations
     * @return Either the 500_development view or the 500_production view, dependent on the environment
     */
    def serverError() {


        //Lot of logging to make bugfixing easier
        log.error "serverError(): The user will be redirected to the 500 page."
        if(request?.exception){
            try{
                log.error "serverError(): Source of the error is: '"+request.exception.getMessage()+"'"
                def stackElements = request.exception.getStackTrace();
                for(stackElement in stackElements){
                    log.error "Stack: "+stackElement.getClassName()+"."+stackElement.getMethodName()+" ["+stackElement.getLineNumber()+"]"
                }

            }catch(Throwable ex){
                log.error "serverError(): Grails did not provide a valid error object", ex
            }
        }else{
            log.error "serverError(): Grails did not provide an error object. No stacktrace available. Most probably an earlier try-catch already consumed it. Check your log."
        }

        // Return response code 500
        response.status = 500

        // The content type and encoding of the error page (should be explicitly set, otherwise the mime
        // could be text/json if an API was called and the layout would be messed up
        def contentTypeFromConfig = configurationService.getMimeTypeHtml()
        def encodingFromConfig = configurationService.getEncoding()

        // Return the view dependent on the configured environment (PROD vs DEV)
        if ( Environment.PRODUCTION == Environment.getCurrent() ) {

            // Production: show a nice error message
            log.error "serverError(): Return view '505_production'"
            return render(view:'500_production', contentType: contentTypeFromConfig, encoding: encodingFromConfig)
        } else {
            // Not it production? show an ugly, developer-focused error message
            log.error "serverError(): Return view '505_development'"
            return render(view:'500_development', contentType: contentTypeFromConfig, encoding: encodingFromConfig)
        }
    }

    /**
     * Handler method for error 400 situations
     * @return The notfound view
     */
    def notFound() {

        // Here we have the possibility to add further logging to identify if some 404 urls were called

        // Return response code 400
        response.status = 404

        // The content type and encoding of the error page (should be explicitly set, otherwise the mime
        // could be text/json if an API was called and the layout would be messed up
        def contentTypeFromConfig = configurationService.getMimeTypeHtml()
        def encodingFromConfig = configurationService.getEncoding()

        // Return the 404 view
        log.error "notFound(): Return view '404'"
        return render(view:'404', contentType: contentTypeFromConfig, encoding: encodingFromConfig)
    }
}
