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

import de.ddb.apd.exception.ConfigurationException;

/**
 * Service for accessing the configuration.
 *
 * @author hla
 */
class ConfigurationService {

    def grailsApplication

    def getBinaryBackendUrl(){
        def url = grailsApplication.config.apd?.binary?.backend?.url
        if(!url){
            throw new ConfigurationException("getBinaryBackendUrl(): Configuration entry does not exist -> apd.binary.backend.url")
        }
        if(!(url instanceof String)){
            throw new ConfigurationException("getBinaryBackendUrl(): apd.binary.backend.url is not a String")
        }
        return url
    }

    def getStaticUrl(){
        def url = grailsApplication.config.apd?.static?.url
        if(!url){
            throw new ConfigurationException("getStaticUrl(): Configuration entry does not exist -> apd.static.url")
        }
        if(!(url instanceof String)){
            throw new ConfigurationException("getStaticUrl(): apd.static.url is not a String")
        }
        return url
    }

    def getApisUrl(){
        def url = grailsApplication.config.apd?.apis?.url
        if(!url){
            throw new ConfigurationException("getApisUrl(): Configuration entry does not exist -> apd.apis.url")
        }
        if(!(url instanceof String)){
            throw new ConfigurationException("getApisUrl(): apd.apis.url is not a String")
        }
        return url
    }

    def getBackendUrl(){
        def url = grailsApplication.config.apd?.backend?.url
        if(!url){
            throw new ConfigurationException("getBackendUrl(): Configuration entry does not exist -> apd.backend.url")
        }
        if(!(url instanceof String)){
            throw new ConfigurationException("getBackendUrl(): apd.backend.url is not a String")
        }
        return url
    }

    def getEncoding(){
        def encoding = grailsApplication.config.grails?.views?.gsp?.encoding
        if(!encoding){
            throw new ConfigurationException("getEncoding(): Configuration entry does not exist -> grails.views.gsp.encoding")
        }
        if(!(encoding instanceof String)){
            throw new ConfigurationException("getEncoding(): grails.views.gsp.encoding is not a String")
        }
        return encoding
    }

    def getMimeTypeHtml(){
        def mimeTypeHtml = grailsApplication.config.grails?.mime?.types["html"][0]
        if(!mimeTypeHtml){
            throw new ConfigurationException("getMimeTypeHtml(): Configuration entry does not exist -> grails.mime.types['html'][0]")
        }
        if(!(mimeTypeHtml instanceof String)){
            throw new ConfigurationException("getMimeTypeHtml(): grails.mime.types['html'][0] is not a String")
        }
        return mimeTypeHtml
    }

    def getDDBUrl(){
        def mimeTypeHtml = grailsApplication.config.apd?.ddb?.url
        if(!mimeTypeHtml){
            throw new ConfigurationException("getDDBUrl(): Configuration entry does not exist -> apd.ddb.url")
        }
        if(!(mimeTypeHtml instanceof String)){
            throw new ConfigurationException("getDDBUrl(): apd.ddb.url is not a String")
        }
        return mimeTypeHtml
    }

/*
apd.advancedSearch.defaultOffset=0
apd.advancedSearch.defaultRows=20
*/

    def getSearchGroupCount() {
        def searchGroupCount = grailsApplication.config.apd?.advancedSearch?.searchGroupCount
        if(!searchGroupCount ){
            throw new ConfigurationException("""getSearchGroupCount(): Configuration entry does not exist ->
                apd.advancedSearch.searchGroupCount""")
        }
        if(!(searchGroupCount instanceof String)){
            throw new ConfigurationException("""getSearchGroupCount(): apd.advancedSearch.searchGroupCount is not
                a String""")
        }
        searchGroupCount
    }

    def getSearchFieldCount() {
        def searchFieldCount= grailsApplication.config.apd.advancedSearch?.searchFieldCount
        if(!searchFieldCount){
            throw new ConfigurationException("""getSearchFieldCount(): Configuration entry does not exist ->
                apd.advancedSearch.searchFieldCount""")
        }
        if(!(searchFieldCount instanceof String)){
            throw new ConfigurationException("""getSearchFieldCount(): apd.advancedSearch.searchFieldCount is not
                a String""")
        }
        searchFieldCount
    }

    def getSearchOffset() {
        def offset = grailsApplication.config.apd.advancedSearch?.defaultOffset
        if(!offset){
            throw new ConfigurationException("""getSearchOffset(): Configuration entry does not exist ->
                apd.advancedSearch.defaultOffset""")
        }
        if(!(offset instanceof String)){
            throw new ConfigurationException("""getSearchOffset(): apd.advancedSearch.defaultOffset is not
                a String""")
        }
        offset
    }

    def getSearchRows() {
        def rows = grailsApplication.config.apd?.advancedSearch?.defaultRows
        if(!rows){
            throw new ConfigurationException("""getSearchRows(): Configuration entry does not exist ->
                apd.advancedSearch.defaultRows""")
        }
        if(!(rows instanceof String)){
            throw new ConfigurationException("""getSearchRows(): apd.advancedSearch.defaultRows is not
                a String""")
        }
        rows
    }

}
