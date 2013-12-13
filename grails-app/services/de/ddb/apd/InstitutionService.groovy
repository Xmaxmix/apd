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


import grails.util.CosineSimilarity
import net.sf.json.JSONObject

import org.junit.Test

import de.ddb.apd.institutions.InstitutionsCache


class InstitutionService {

    private static final def LETTERS='A'..'Z'

    private static final def NUMBERS = 0..9

    private static final def NUMBER_KEY = '0-9'

    def transactional = false
    def configurationService

    static InstitutionsCache institutionsCache = new InstitutionsCache()

    def grailsLinkGenerator
    def itemService


    /**
     * Creates an index map which holds all institutions without children. 
     * The key of the index is a letter/number and the value a list of institutes that belongs to the key. 
     *  
     * @return an index map of all institutions in alphabetical order
     */
    def findAllAlphabetical(){
        def institutionList = findAll()

        def totalInstitution = 0
        def allInstitutions = [data: [:], total: totalInstitution]
        def institutionByFirstChar = buildIndex()

        institutionList.each { it ->
            def firstChar = it?.name[0]?.toUpperCase()
            it.firstChar = firstChar

            if (LETTERS.contains(firstChar) && institutionByFirstChar.get(firstChar)?.size() == 0) {
                it.isFirst = true
            }

            it.sectorLabelKey = 'apd.' + it.sector
            buildChildren(it)
            institutionByFirstChar = putToIndex(institutionByFirstChar, addUri(it), firstChar)
        }

        allInstitutions.data = institutionByFirstChar
        allInstitutions.total = getTotal(institutionList)

        return allInstitutions
    }

    /**
     * Retrieves all institutions from the backend
     * 
     * @return all institutions stored in the backend
     */
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


    /**
     * Search for institutions that have items that match the search query. The search is implemented in two steps:
     * <ul>
     *  <li>A backend search with the given query. Add request parameters for sector (archive) and provider facets. 
     *  <li>Adjust the institution found in the search with the complete institution list
     * </ul>
     * 
     * @param query the query to search teh backend
     * 
     * @return A list of institutions that have items that match the search query
     */
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

    /**
     * Searches for archives in the complete institutions list
     * 
     * @param query The query to search for archives
     * @return A list of archives
     */
    def searchArchivesForStructure() {
        def resultList = []

        def allInstitutes = findAll()

        for(int i=0; i<allInstitutes.size(); i++){
            def currentInstitute = allInstitutes.get(i)
            if(isInstitutionOrChildAnArchive(currentInstitute)) {
                resultList.add(["id": currentInstitute.id, "name": currentInstitute.name, "count": 0, "institution": true])
            }
        }

        def resultObject = [:]
        resultObject["count"] = resultList.size()
        resultObject["institutions"] = resultList

        return resultObject

    }

    /**
     * Checks for the facet value sec_01 in the given institutions and it's child institutions
     * @param currentInstitution the institution to ckeck for archives
     * @return <code>true</code> if the institution is or includes an archive 
     */
    private def isInstitutionOrChildAnArchive(def currentInstitution){
        if(currentInstitution.sector == "sec_01"){
            return true
        }else{
            if(currentInstitution.children){
                for(int i=0; i<currentInstitution.children.size(); i++){
                    if(isInstitutionOrChildAnArchive(currentInstitution.children.get(i))){
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * Search for specific items for a given institute
     * 
     * @param query the query to search for
     * @param institutionId search the items of this institute
     * @param offset the search offset
     * @param pagesize the pagination size
     * @param sort the sort order
     * 
     * @return the items which maps the given search criterias
     */
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

        //Get the institution by its id
        //TODO maybe it would be nice to hold an hashmap of the institutions with its id as a key. So we don not need to iterate over the whole list!
        def institutionName = ""
        for(int i=0; i<allInstitutions.size(); i++){
            if(allInstitutions[i].id == institutionId) {
                institutionName = allInstitutions[i].name
                break
            }
        }

        //Search for items related to the given institution
        def backendUrl = configurationService.getBackendUrl()
        def parameters = [:]
        parameters["query"] = query
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

        return searchWrapper.getResponse()
    }

    /**
     * Get the total number of institutions in the given list
     * 
     * @param rootList the list on which to calculate the total number of institutions
     * @return the total number of institutions in a given list
     */
    def getTotal(rootList) {
        def total = rootList.size()

        for (root in rootList) {
            if (root.children?.size() > 0) {
                total = total + root.children.size()
                total = total + countDescendants(root.children)
            }
        }

        return total
    }

    /**
     * Get the total number of descendants in the given children list
     *
     * @param rootList the list on which to calculate the total number of descendants
     * @return the total number of descendants in a given children list
     */
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

    /**
     * Puts an institute on the given index.
     * Implements some special logic for german umlauts.
     * 
     * @param institutionByFirstLetter the index where to put the institution
     * @param institutionWithUri the institute represented by an uri
     * @param firstLetter the first letter oft the institute
     * 
     * @return an updated version of the index
     */
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

    /**
     * Recursive method which appends data to the children of a given institute
     * 
     * @param institution the parent institute
     * @param counter TODO it seems that this parameter is never used in the method
     */
    private buildChildren(institution) {
        if(institution.children?.size() > 0 ) {
            institution.children.each { child ->
                child.uri = buildUri(child.id)
                child.sectorLabelKey = 'apd.' + child.sector
                child.parentId = institution.id
                child.firstChar = child?.name[0]?.toUpperCase()
                buildChildren(child)
            }
        }
    }

    /**
     * Prepares an index map that holds as key the letter a-z and a special NUMBER_KEY.
     * Each key gets an empty list assigned that will store institutes.
     *  
     * @return an index map for institutes
     */
    private def buildIndex() {
        // create a map with empty arrays as initial values.
        def institutionByFirstLetter = [:].withDefault{ []}

        // use A..Z as keys
        LETTERS.each {
            institutionByFirstLetter[it] = []
        }

        // add the '0-9' as the key for institutions start with a number.
        institutionByFirstLetter[NUMBER_KEY] = []

        return institutionByFirstLetter
    }

    /**
     * Adds an uri to the detail view of an institution to the given json
     * @param json the json where to add the uri
     * @return an uri to the detail view of an institution to the given json
     */
    private def addUri(json) {
        json.uri = buildUri(json.id)
        return json
    }

    /**
     * Builds an uri to show the details for the given institution id
     * @param id the institution id
     * @return an uri to show the details for the given institution id
     */
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
    def getTechtonicFirstLvlHierarchyChildren(id){
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

        if(id == "XYMQPA4OHAYDDFYWHV6Q4RFUIISTLQJV"){ // Special case for Landesarchiv BW with its 7 tectonics
            def tectonics = [
                [id:"A7DCNBBFPDAZHS7ESXK3Q76ZAGJ2W6H7", parent:"<<null>>", label:"Landesarchiv Baden-Württemberg Abt. Hauptstaatsarchiv Stuttgart", type:"<<null>>", position:-1, leaf:false, aggregationEntity:true, institution:false],
                [id:"HOEPZL253TB56GXLCPJ6QVNY4X5QCOJF", parent:"<<null>>", label:"Landesarchiv Baden-Württemberg Abt. Staatsarchiv Ludwigsburg", type:"<<null>>", position:-1, leaf:false, aggregationEntity:true, institution:false],
                [id:"SCVLIRRGBA66IBT4YXOZ2GJHERYX7Q2U", parent:"<<null>>", label:"Landesarchiv Baden-Württemberg Abt. Staatsarchiv Freiburg", type:"<<null>>", position:-1, leaf:false, aggregationEntity:true, institution:false],
                [id:"GVOQN7F2CZCHWRKJE2RKRHBIERX23TGA", parent:"<<null>>", label:"Landesarchiv Baden-Württemberg Abt. Hohenlohe-Zentralarchiv Neuenstein", type:"<<null>>", position:-1, leaf:false, aggregationEntity:true, institution:false],
                [id:"36BF267YNTQVVLZDC6IKZ66KMIRJT4YZ", parent:"<<null>>", label:"Landesarchiv Baden-Württemberg Abt. Generallandesarchiv Karlsruhe", type:"<<null>>", position:-1, leaf:false, aggregationEntity:true, institution:false],
                [id:"YSUXYV6ZN7577VESVIMJQNOZFMCODOGJ", parent:"<<null>>", label:"Landesarchiv Baden-Württemberg Abt. Staatsarchiv Wertheim", type:"<<null>>", position:-1, leaf:false, aggregationEntity:true, institution:false],
                [id:"3NNKFXSDLL3BVHKI5R62PVAYUJTTJODG", parent:"<<null>>", label:"Landesarchiv Baden-Württemberg Abt. Staatsarchiv Sigmaringen", type:"<<null>>", position:-1, leaf:false, aggregationEntity:true, institution:false]
            ]
            hierarchy.children.remove(0) // remove old tectonic
            hierarchy.children.addAll(0,tectonics)
        }

        return hierarchy
    }

    /**
     * Used in the getTechtonicFirstLvlHierarchyChildren
     * @return JSONObject
     */
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
    /**
     * This method is also temporary until proper support is provided by backend software
     * @param id
     * @return
     */
    def getInstitutionParent(id){

        def parents = itemService.getParent(id)
        if (parents.size() == 0){
            def item = itemService.findItemById(id)
            parents = itemService.getParent(item.institution.id)

            def replacementItem = [:]
            replacementItem["id"] = id
            replacementItem["parent"] = item.institution.id.toString()
            replacementItem["label"] = item.item.title.toString()
            replacementItem["type"] = ""
            replacementItem["position"] = -1
            replacementItem["leaf"] = true
            replacementItem["aggregationEntity"] = true

            parents.add(0,replacementItem)
        }
        parents = getParentBasedOnSimilarity(parents)
        return parents;
    }

    private getParentBasedOnSimilarity(parent) {
        def institutionsList = findAll()
        def candidates = []
        institutionsList.each { candidates << it.name }
        def cosines = CosineSimilarity.mostSimilar(parent.last().label, candidates);
        institutionsList.each {
            if (it.name== cosines.first().toString()){
                //Fix the different attributes for objects and institutions
                parent.last().parent = it.id

                def itCopy = [:]
                itCopy["id"] = it.id
                itCopy["label"] = it.name
                itCopy["parent"] = ""
                itCopy["leaf"] = false
                itCopy["type"] = ""
                itCopy["aggregationEntity"] = true
                itCopy["children"] = []

                parent << itCopy
            }
        }
        //Fix null types (causing JSON problems)
        parent.each {
            if(it.type){
                it.type = ""
            }
        }
        //Remove entries that contain themselfs as parents
        for(int i=parent.size()-1; i>=0; i--){
            if(parent.get(i).id == parent.get(i).parent){
                parent.remove(i);
            }
        }
        return parent
    }

    /**
     * Requests the backend for an institution item.
     * 
     * @param id the id of the institution item
     * @return view details of an institution item 
     */
    def findInstitutionViewById(id) {
        final def componentsPath = "/access/" + id + "/components/"
        final def path = componentsPath + "view"

        def apiResponse = ApiConsumer.getXml(configurationService.getBackendUrl(), path)
        if(!apiResponse.isOk()){
            log.error "findItemById(): Server returned no results -> " + id
            apiResponse.throwException(WebUtils.retrieveGrailsWebRequest().getCurrentRequest())
        }

        def response = apiResponse.getResponse()

        def uri = response.uri.text()
        def name = response.name.text()
        def fields = response.fields.field.findAll()

        return [
            'uri': uri,
            'name': name,
            'fields': fields
        ]
    }


    /**
     * Return the state for a given institution id 
     * @param id the id of the institution item
     * 
     * @return the state for the given institution id
     */
    @Test public String getInstitutionState(id) {
        def institution = findInstitutionViewById(id)
        def state = null

        institution?.fields.each {
            if (it.name =="state") {
                state = it.value
            }
        }

        return state
    }
}
