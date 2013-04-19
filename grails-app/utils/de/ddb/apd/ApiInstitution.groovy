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

import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.web.json.JSONArray;

import groovy.json.JsonLexer;
import groovy.json.JsonSlurper;
import groovy.json.JsonToken;
import groovyx.net.http.ContentType;
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

class ApiInstitution {
   
   private static final log = LogFactory.getLog(this)
   
    def getInstitutionViewByItemId(String id, String url) {
        log.debug("get insitution view by item id: ${id}")
        def uriPath = "/access/" + id + "/components/view"
        def apiResponse = ApiConsumer.getXml(url, uriPath)
        if(!apiResponse.isOk()){
          log.error "Xml: Xml file was not found"
          throw apiResponse.getException()
        }
        def xmlResult = apiResponse.getResponse()
        return xmlResult;
    }
    
    def getChildrenOfInstitutionByItemId(String id, String url) {
        log.debug("get chlildren of institution by item id: ${id}")
        def jsonResult;
        def uriPath = "/hierarchy/" + id + "/children"
        def apiResponse = ApiConsumer.getJson(url, uriPath)
        if(!apiResponse.isOk()){
          log.error "Json: Json file was not found"
          throw apiResponse.getException()
        }
        jsonResult = apiResponse.getResponse()
        return jsonResult;
    }
    
    def getParentsOfInstitutionByItemId(String id, String url) {
        log.debug("get parent of institution by item id: ${id}")
        def jsonResult;
        def uriPath = "/hierarchy/" + id + "/parent"
        def apiResponse = ApiConsumer.getJson(url, uriPath)
        if(!apiResponse.isOk()){
          log.error "Json: Json file was not found"
          throw apiResponse.getException()
        }
        jsonResult = apiResponse.getResponse()
        return jsonResult;
    }
    
    def getFacetValues(String provName, String url) {
        log.debug("get facets values for: ${provName}")
        def jsonResult;
        int shortLength = 50;
        String shortQuery = (provName.length() > shortLength ? provName.substring(0, shortLength) : provName);
        def uriPath = "/search/facets/provider_fct"
        def query = ['query':"${shortQuery}" ]
        def apiResponse = ApiConsumer.getJson(url, uriPath, query)
        if(!apiResponse.isOk()){
          log.error "Json: Json file was not found"
          throw apiResponse.getException()
        }
        jsonResult = apiResponse.getResponse()
        return jsonResult;
    }
}
