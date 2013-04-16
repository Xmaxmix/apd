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

import groovy.json.JsonBuilder;
import groovyx.net.http.ContentType;

import javax.servlet.http.HttpServletResponse;

class StructureviewController {

    def institutionService

    def index() {
        //def allInstitution = institutionService.findAll()
        def institutionsListHash = institutionService.institutionsCache.getHash()
        render (
                view: 'structureview',
                model: [
                    //'all': allInstitution,
                    'institutionsListHash' : institutionsListHash
                ])
    }

    def getAjaxList() {
        def hash = params.hashId
        def allInstitutions = institutionService.findAll()

        response.setHeader("Cache-Control", "public, max-age=31536000")
        // Explicitly use "text/plain" as contenttype because some browsers disable caching for JSON
        render (contentType: ContentType.TEXT.toString(), text: allInstitutions.toString())
    }
}
