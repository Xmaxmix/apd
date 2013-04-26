package de.ddb.apd

import groovy.json.JsonBuilder;
import groovyx.net.http.ContentType;

class InstitutionsController {

    def institutionService
    def configurationService

    def index() {
        render (contentType: ContentType.TEXT.toString()) { "" }
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

    //    def getAjaxListArchiveFull() {
    //        def hash = params.hashId
    //        def allInstitutions = institutionService.findAll()
    //
    //        def allArchives = []
    //        allInstitutions.each {
    //            // sec_01 == archives
    //            if(checkIfArchive(it)){
    //                allArchives.add(it)
    //            }
    //        }
    //
    //        response.setHeader("Cache-Control", "no-cache")
    //        render (contentType: ContentType.JSON.toString()) { allArchives }
    //    }

    //    private boolean checkIfArchive(Map institution){
    //        // if it has archive-sector -> return true
    //        if(institution.sector == "sec_01"){
    //            return true
    //            // if it does not have archive sector but has childs -> check childs
    //            // if any child is archive -> return true, otherwise return false
    //        } else if(institution.children) {
    //            for(int i=0; i<institution.children.size(); i++){
    //                if(checkIfArchive(institution.children[i])) {
    //                    return true
    //                }
    //            }
    //            return false
    //            // Is no archive and has no childs -> return true
    //        } else {
    //            return false
    //        }
    //    }

    //    def getAjaxSearch() {
    //        //http://backend-p1.deutsche-digitale-bibliothek.de:9998/search?query=gutenberg&facet=sector_fct&facet=provider_fct&sector_fct=sec_01
    //        def query = params.query
    //        def backendUrl = configurationService.getBackendUrl()
    //        def parameters = [:]
    //        parameters["query"] = query
    //        parameters["facet"] = ["sector_fct", "provider_fct"]
    //        parameters["sector_fct"] = "sec_01"
    //        def searchWrapper = ApiConsumer.getJson(backendUrl, "/search", parameters)
    //
    //        if(!searchWrapper.isOk()){
    //            log.error "#################### 1 not ok"
    //        }
    //
    //
    //        //Getting result institutions for search
    //        def searchResponse = searchWrapper.getResponse()
    //        def responseFacets = searchResponse.facets
    //        def foundProviders = []
    //        for(int i=0; i<responseFacets.size(); i++) {
    //            println "#################### 2 "+responseFacets.get(i).field
    //            if(responseFacets.get(i).field == "provider_fct"){
    //                println "#################### 3 found"
    //                foundProviders = responseFacets.get(i).facetValues
    //                break
    //            }
    //        }
    //
    //
    //        def allInstitutions = institutionService.findAll()
    //
    //        for(int i=0; i<foundProviders.size(); i++){
    //            for(int j=0; j<allInstitutions.size(); j++){
    //                if(allInstitutions[j].name == foundProviders[i].value){
    //                    println "#################### 5 match: "+allInstitutions[j].name +"=="+ foundProviders[i].value+" -> "+allInstitutions[j].id
    //                    foundProviders[i]["id"] = allInstitutions[j].id
    //                    break
    //                }
    //            }
    //        }
    //
    //        // Getting ID for institutions
    //        println "#################### 6 "+foundProviders
    //        for(int i=0; i<foundProviders.size(); i++){
    //            println "#################### 7 "+foundProviders[i]
    //
    //        }
    //
    //        def resultList = []
    //        foundProviders.each {
    //            resultList.add(["id": it.id, "name": it.value, "count": it.count])
    //        }
    //
    //        render (contentType: ContentType.JSON.toString()) { resultList }
    //
    //    }


}
