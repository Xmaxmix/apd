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

class ListviewController {

    def institutionService
    def searchService
    def configurationService

    def index() {

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

        //def allInstitution = institutionService.findAll()
        //        def institutionsListHash = institutionService.institutionsCache.getHash()
        //        render (
        //                view: 'structureview',
        //                model: [
        //                    //'all': allInstitution,
        //                    'institutionsListHash' : institutionsListHash
        //                ])

        def allInstitution = institutionService.findAllAlphabetical()
        def institutionByFirstLetter = allInstitution.data

        //institution list
        def all = []
        institutionByFirstLetter?.each { all.addAll(it.value) }

        def index = []
        institutionByFirstLetter.each {
            index.add(it)
        }

        render(view: "listview", model: [results: resultsItems,offset: params["offset"], index: index, all: all, total: allInstitution?.total])

    }

    def getAjaxListFull() {
        def hash = params.hashId
        def allInstitutions = institutionService.findAll()

        response.setHeader("Cache-Control", "public, max-age=31536000")
        render (contentType: ContentType.TEXT.toString(), text: allInstitutions.toString())
    }

    def isAjaxListFullOutdated() {
        def hash = params.hashId
        def hasChanged = institutionService.institutionsCache.hasChanged(hash)

        def builder = new JsonBuilder()
        def root = builder {
            isOutdated hasChanged
            hashId institutionService.institutionsCache.getHash()
        }

        response.setHeader("Cache-Control", "no-cache")
        render (contentType: ContentType.JSON.toString()) { builder }
    }
    //The method can be used in ajax requests to retrieve elements on second level
    def institutionhierarchy(){
        assert params.id!=null, "this method should not be called without an ID"
        render institutionService.getTechtonicFirstLvlHierarchyChildren(params.id);
    }

}