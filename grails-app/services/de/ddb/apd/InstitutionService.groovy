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


import de.ddb.apd.institutions.InstitutionsCache
import grails.util.CosineSimilarity;
import groovyx.net.http.HTTPBuilder
import net.sf.json.JSONObject


class InstitutionService {

    private static final def LETTERS='A'..'Z'

    private static final def NUMBERS = 0..9

    private static final def NUMBER_KEY = '0-9'

    def transactional = false
    def configurationService

    static InstitutionsCache institutionsCache = new InstitutionsCache()

    def grailsLinkGenerator
    def itemService


    def findAllAlphabetical(){
        def institutionList = findAll()

        def totalInstitution = 0
        def allInstitutions = [data: [:], total: totalInstitution]
        def institutionByFirstChar = buildIndex()

        institutionList.each { it ->

            totalInstitution++

            def firstChar = it?.name[0]?.toUpperCase()
            it.firstChar = firstChar

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
            log.error "searchArchives(): search returned an error"
        }


        //Getting result institutions for search
        def searchResponse = searchWrapper.getResponse()
        def responseFacets = searchResponse.facets
        def foundProviders = []
        for(int i=0; i<responseFacets.size(); i++) {
            if(responseFacets.get(i).field == "provider_fct"){
                foundProviders = responseFacets.get(i).facetValues
                break
            }
        }


        def allInstitutions = findAll()

        for(int i=0; i<foundProviders.size(); i++){
            for(int j=0; j<allInstitutions.size(); j++){
                if(allInstitutions[j].name == foundProviders[i].value){
                    foundProviders[i]["id"] = allInstitutions[j].id
                    break
                }
            }
        }

        // Getting ID for institutions

        def resultList = []
        foundProviders.each {
            resultList.add(["id": it.id, "name": it.value, "count": it.count, "institution": true])
        }

        def resultObject = [:]
        resultObject["count"] = searchResponse.numberOfResults
        resultObject["institutions"] = resultList

        return resultObject
    }

    def searchArchivesForStructure(String query) {
        //http://backend-p1.deutsche-digitale-bibliothek.de:9998/search?query=gutenberg&facet=sector_fct&facet=provider_fct&sector_fct=sec_01
        def backendUrl = configurationService.getBackendUrl()
        def parameters = [:]
        parameters["query"] = query
        parameters["sector"] = "sec_01"
        def searchWrapper = ApiConsumer.getJson(backendUrl, "/institutions", parameters)

        if(!searchWrapper.isOk()){
            log.error "searchArchives(): search returned an error"
        }


        //Getting result institutions for search
        def searchResponse = searchWrapper.getResponse()

        // Getting ID for institutions
        //println searchResponse.get(0)
        def resultList = []
        searchResponse.each {
            resultList.add(["id": it.id, "name": it.name, "count": 0, "institution": true])
        }

        def resultObject = [:]
        resultObject["count"] = resultList.size()
        resultObject["institutions"] = resultList

        return resultObject

    }

    def searchArchive(query, institutionId, offset, pagesize, sort) {
        if(!offset) {
            offset = "0"
        }

        if(!pagesize) {
            pagesize = "20"
        }

        if(!sort) {
            sort = 'RELEVANCE'
        }

        def allInstitutions = findAll()

        def institutionName = ""
        for(int i=0; i<allInstitutions.size(); i++){
            if(allInstitutions[i].id == institutionId) {
                institutionName = allInstitutions[i].name
                break
            }
        }

        def backendUrl = configurationService.getBackendUrl()
        def parameters = [:]
        parameters["query"] = query
        //parameters["facet"] = ["sector_fct", "provider_fct"]
        parameters["facet"] = ["sector_fct"]
        if(institutionName.length() > 0){
            parameters["facet"].add("provider_fct")
        }
        parameters["sector_fct"] = "sec_01"
        if(institutionName.length() > 0){
            parameters["provider_fct"] = institutionName
        }
        parameters["offset"] = offset
        parameters["rows"] = pagesize
        parameters["sort"] = sort
        def searchWrapper = ApiConsumer.getJson(backendUrl, "/search", parameters)

        if(!searchWrapper.isOk()){
            log.error "searchArchive(): search returned an error"
        }

        return searchWrapper.getResponse()?.results[0]?.docs
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

    /**
     * Return one level information below this institution
     * 1. Get some item-object from an institution
     * 2. Find the Root Tectonic for this item / we assume that there is only one tectonic per institute
     * 3. Get all children for our institution and return a hierarchy if succesfull
     * @return JSONObject
     */
    private def getTechtonicFirstLvlHierarchyChildren(id){
        def JSONObject hierarchy = [:]
        def children=getInstitutionChildren(id)
        //TODO throw exception if response if not JSON
        assert children instanceof JSONObject
        def objectResults = children.results.docs[0]
        if(!hierarchy.children){
            hierarchy.children = []
        }

        if (objectResults.size()>0){
            def parentList = itemService.getParent(objectResults[0].id)
            if(parentList && parentList.size() > 0){
                def parent = itemService.getParent(objectResults[0].id).last()
                if(!parent?.parent || parent?.parent == "null"){
                    parent.parent = "<<null>>"
                }
                if(!parent?.type || parent?.type == "null"){
                    parent.type = "<<null>>"
                }
                if(!parent?.institution || parent?.institution == "null"){
                    parent.institution = false
                }
                if(!hierarchy.children){
                    hierarchy.children = []
                }
                hierarchy.children.addAll(parent);
            }
        }
        hierarchy.id = id
        hierarchy.children.addAll(getChildren(id).getAt("children"))

        return hierarchy
    }


    private def getChildren(id){
        def children = itemService.getChildren(id)
        HashMap jsonMap = new HashMap()
        jsonMap.children = children.collect {child ->
            return ["id": child.id, label: child.label, parent:child.parent, leaf: child.leaf, aggregationEntity:child.aggregationEntity,institution:false]

        }
        return jsonMap
    }
    /**
     * Used in the getTechtonicFirstLvlHierarchyChildren
     * @return JSONObject
     */
    private def getInstitutionChildren(id){
        def query =[query:"*",facet:"provider_id",provider_id:id]
        def searchPath = "/search"
        def apiResponse = ApiConsumer.getJson(configurationService.getBackendUrl(), searchPath,query)
        if(!apiResponse.isOk()){
            log.error "institutionService.getInstitutionChildren(): Server returned no parents -> " + id
        }
        return apiResponse.getResponse()
    }
    
    private def getInstitutionParent(id){
        def parent = itemService.getParent(id)
        parent = getParentBasedOnSimilarity(parent)
        return parent;
    }

	private getParentBasedOnSimilarity(parent) {
		def institutionsList = findAll()
		def candidates = []
		institutionsList.each {
			candidates << it.name
		}
		def cosines = CosineSimilarity.mostSimilar(parent.last().label, candidates);
		institutionsList.each {
			if (it.name== cosines.first().toString()){
				it.label = it.name;
				parent << it
			}
		}
		return parent
	}
    
}
