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

import de.ddb.apd.ApiConsumer;
import de.ddb.apd.exception.ItemNotFoundException;

class ApisController {

    def configurationService

    def index() {
    }

    /**
     * Wrapper to support streaming of files from the backend
     * @return OutPutStream
     */
    def binary(){
        if(!params.filename){
            log.warn "binary(): A binary content was requested, but no filename was given in the url"
            throw new ItemNotFoundException();
        }

        def apiResponse = ApiConsumer.getBinary(getBinaryServerUrl(), params.filename)
        if(!apiResponse.isOk()){
            log.error "binary(): binary content was not found"
            throw apiResponse.getException()
        }
        def responseObject = apiResponse.getResponse()

        byte[] bytes = responseObject.get("bytes");
        response.setContentType(responseObject.get("Content-Type"))
        response.setContentLength(responseObject.get("Content-Length").toInteger())
        response.setHeader("Content-Disposition", "inline; filename=" + params.filename.tokenize('/')[-1])
        response.outputStream << bytes
    }

    private def getBinaryServerUrl(){
        def url = configurationService.getBinaryBackendUrl()
        assert url instanceof String, "This is not a string"
        return url;
    }
}
