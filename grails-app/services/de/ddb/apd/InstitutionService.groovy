package de.ddb.apd
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


import de.ddb.apd.institutions.InstitutionsCache;
import groovyx.net.http.HTTPBuilder


class InstitutionService {

    private static final def LETTERS='A'..'Z'

    private static final def NUMBERS = 0..9

    private static final def NUMBER_KEY = '0-9'

    def transactional = false

    def configurationService

    static InstitutionsCache institutionsCache = new InstitutionsCache()

    def grailsLinkGenerator


    def findAllAlphabetical(){
        def institutionList = findAll()

        def totalInstitution = 0
        def allInstitutions = [data: [:], total: totalInstitution]
        def institutionByFirstChar = buildIndex()

        institutionList.each { it ->

            totalInstitution++

            def firstChar = it?.name[0]?.toUpperCase()
            it.firstChar = firstChar

            //                def firstChar = it?.name[0]?.toUpperCase()
            //                it.firstChar = firstChar
            //
            //                /*
            //                 * mark an institution as the first one that start with the
            //                 * character. We will use it for assigning the id in the HTML.
            //                 * See: views/institutions/_listItem.gsp
            //                 * */
            //                if (LETTERS.contains(firstChar) && institutionByFirstChar.get(firstChar)?.size() == 0) {
            //                    it.isFirst = true
            //                }
            //
            //                it.sectorLabelKey = 'apd.' + it.sector
            //                buildChildren(it, totalInstitution)
            //                institutionByFirstChar = putToIndex(institutionByFirstChar, addUri(it), firstChar)
            /*
             * mark an institution as the first one that start with the
             * character. We will use it for assigning the id in the HTML.
             * See: views/institutions/_listItem.gsp
             * */
            if (LETTERS.contains(firstChar) && institutionByFirstChar.get(firstChar)?.size() == 0) {
                it.isFirst = true
            }

            it.sectorLabelKey = 'apd.' + it.sector
            buildChildren(it, totalInstitution)
            institutionByFirstChar = putToIndex(institutionByFirstChar, addUri(it), firstChar)
        }

        allInstitutions.data = institutionByFirstChar
        allInstitutions.total = getTotal(institutionList)

        return allInstitutions
    }

    def findAll() {
        // Get the last known Etag (empty on first request)
        def pendingEtag = institutionsCache.etag

        // Call backend with the last known Etag
        ApiResponse responseWrapper = ApiConsumer.getJson(configurationService.getBackendUrl(), "/institutions", [:], ["If-None-Match": pendingEtag])
        if(!responseWrapper.isOk()){
            log.error "findAll(): Server returned no results "
            throw responseWrapper.getException()
        }

        // Get responses
        def resultJson = responseWrapper.getResponse()
        def resultEtag = responseWrapper.getHeaders()["ETag"]
        if(!resultEtag){
            resultEtag = ""
        }

        // If the Etags of request and response do not match -> update the cache
        if(pendingEtag != resultEtag) {
            institutionsCache.updateCache(resultJson, resultEtag)
        }

        return institutionsCache.cache
    }


    def searchArchives(String query) {
        //http://backend-p1.deutsche-digitale-bibliothek.de:9998/search?query=gutenberg&facet=sector_fct&facet=provider_fct&sector_fct=sec_01
        def backendUrl = configurationService.getBackendUrl()
        def parameters = [:]
        parameters["query"] = query
        parameters["facet"] = ["sector_fct", "provider_fct"]
        parameters["sector_fct"] = "sec_01"
        def searchWrapper = ApiConsumer.getJson(backendUrl, "/search", parameters)

        if(!searchWrapper.isOk()){
            log.error "#################### 1 not ok"
        }


        //Getting result institutions for search
        def searchResponse = searchWrapper.getResponse()
        def responseFacets = searchResponse.facets
        def foundProviders = []
        for(int i=0; i<responseFacets.size(); i++) {
            println "#################### 2 "+responseFacets.get(i).field
            if(responseFacets.get(i).field == "provider_fct"){
                println "#################### 3 found"
                foundProviders = responseFacets.get(i).facetValues
                break
            }
        }


        def allInstitutions = findAll()

        for(int i=0; i<foundProviders.size(); i++){
            for(int j=0; j<allInstitutions.size(); j++){
                if(allInstitutions[j].name == foundProviders[i].value){
                    println "#################### 5 match: "+allInstitutions[j].name +"=="+ foundProviders[i].value+" -> "+allInstitutions[j].id
                    foundProviders[i]["id"] = allInstitutions[j].id
                    break
                }
            }
        }

        // Getting ID for institutions
        println "#################### 6 "+foundProviders
        for(int i=0; i<foundProviders.size(); i++){
            println "#################### 7 "+foundProviders[i]

        }

        def resultList = []
        foundProviders.each {
            resultList.add(["id": it.id, "name": it.value, "count": it.count])
        }

        def resultObject = [:]
        resultObject["count"] = searchResponse.numberOfResults
        resultObject["institutions"] = resultList
        println "#################### 7 "+searchResponse.numberOfResults


        return resultObject

    }


    private getTotal(rootList) {
        def total = rootList.size()

        for (root in rootList) {
            if (root.children?.size() > 0) {
                total = total + root.children.size()
                total = total + countDescendants(root.children)
            }
        }

        return total
    }

    private countDescendants(children) {
        def totalDescendants = 0

        for (institution in children) {
            if(institution.children) {
                totalDescendants = totalDescendants + institution.children.size()
                totalDescendants = totalDescendants + countDescendants(institution.children)
            }
        }
        return totalDescendants
    }

    private putToIndex(institutionByFirstLetter, institutionWithUri, firstLetter) {
        switch(firstLetter) {
            case 'Ä':
                institutionByFirstLetter['A'].add(institutionWithUri)
                break
            case 'Ö':
                institutionByFirstLetter['O'].add(institutionWithUri)
                break
            case 'Ü':
                institutionByFirstLetter['U'].add(institutionWithUri)
                break
            default:
                institutionByFirstLetter[firstLetter].add(institutionWithUri)
        }
        return institutionByFirstLetter
    }

    private buildChildren(institution, counter) {
        if(institution.children?.size() > 0 ) {
            institution.children.each { child ->
                child.uri = buildUri(child.id)
                child.sectorLabelKey = 'apd.' + child.sector
                child.parentId = institution.id
                child.firstChar = child?.name[0]?.toUpperCase()
                buildChildren(child, counter)
            }
        }
    }

    private def buildIndex() {
        // create a map with empty arrays as initial values.
        def institutionByFirstLetter = [:].withDefault{ []}

        // use A..Z as keys
        LETTERS.each {
            institutionByFirstLetter[it] = []
        }

        // add the '0-9' as the last key for institutions start with a number.
        institutionByFirstLetter[NUMBER_KEY] = []

        return institutionByFirstLetter
    }

    private def addUri(json) {
        json.uri = buildUri(json.id)
        return json
    }

    private def buildUri(id) {
        grailsLinkGenerator.link(url: [controller: 'structureview', action: 'show', id: id ])
    }
}
