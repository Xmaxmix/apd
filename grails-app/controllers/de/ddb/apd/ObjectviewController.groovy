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

import groovyx.net.http.ContentType;


class ObjectviewController {

    def institutionService
    def searchService
    def configurationService

    def index() {

        def query = params.search
        println "##################### index: "+query

        def searchResult = institutionService.searchArchives(query)

        render(view: "objectview", model: ["searchResult":searchResult])
    }

    def getTreeNodeDetails() {
        def id = params.id
        def query = params.query
        def offset = params.offset
        def pagesize = params.pagesize
        println "##################### getTreeNodeDetails: "+id+","+query

        def searchResults = institutionService.searchArchive(query, id, offset, pagesize)

        render(template: "resultsList", model: ["results":searchResults, "offset": offset])
    }

    def getTreeNodeChildren() {
        def id = params.id
        println "##################### getTreeNodeChildren: "+id

        render (contentType: ContentType.JSON.toString()) { [:]}
    }
}