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

class ListviewController {

    def institutionService
    def searchService
    def configurationService

    def results() {

        def urlQuery = searchService.convertQueryParametersToSearchParameters(params)

        def urlPath = request.getContextPath()+'/apis/search'
        def apiResponse = ApiConsumer.getJson(configurationService.getApisUrl() ,urlPath, urlQuery)
        if(!apiResponse.isOk()){
          log.error "Json: Json file was not found"
          throw apiResponse.getException()
        }
        def resultsItems = apiResponse.getResponse()

        def queryString = request.getQueryString()

        if(!queryString?.contains("sort=random") && urlQuery["randomSeed"])
            queryString = queryString+"&sort="+urlQuery["randomSeed"]

        def allInstitution = institutionService.findAll()
        def institutionByFirstLetter = allInstitution.data

        // TODO: make this more idiomatic Groovy
        def all = []
        institutionByFirstLetter?.each { all.addAll(it.value) }

        // no institutions
        institutionByFirstLetter.each { k,v ->
            if(institutionByFirstLetter[k]?.size() == 0) {
                institutionByFirstLetter[k] = true
            } else {
                institutionByFirstLetter[k] = false
            }
        }

        // TODO: move to service
        def index = []
        institutionByFirstLetter.each {
            index.add(it)
        }

        render(view: "listview", model: [results: resultsItems,offset: params["offset"], index: index, all: all, total: allInstitution?.total])

    }

}