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

import java.text.SimpleDateFormat;

import net.sf.json.JSONNull;
import de.ddb.apd.ApiConsumer
import de.ddb.apd.exception.ItemNotFoundException

import grails.converters.JSON

class ApisController {

    def configurationService
    def apisService

    def search(){

        def resultList = [:]
        def facets = []
        def highlightedTerms = []
        def docs = []
        def query = apisService.getQueryParameters(params)
        def apiResponse = ApiConsumer.getJson(configurationService.getBackendUrl(),'/search', query)
        if(!apiResponse.isOk()){
            log.error "Json: Json file was not found"
            throw apiResponse.getException()
        }
        def jsonResp = apiResponse.getResponse()

        jsonResp.results['docs'][0].each {

            def tmpResult = [:]
            def title
            def subtitle
            def thumbnail
            def media = []

            def titleMatch = it.preview.toString() =~ /(?m)<div (.*?)class="title"(.*?)>(.*?)<\/div>$/
            if (titleMatch)
                title= titleMatch[0][3]

            def subtitleMatch = it.preview.toString() =~ /(?m)<div (.*?)class="subtitle"(.*?)>(.*?)<\/div>$/
            subtitle= (subtitleMatch)?subtitleMatch[0][3]:""

            def thumbnailMatch = it.preview.toString() =~ /(?m)<img (.*?)src="(.*?)"(.*?)\/>$/
            if (thumbnailMatch){
                thumbnail= thumbnailMatch[0][2]
            }
            def mediaMatch = it.preview.toString() =~ /(?m)<div (.*?)data-media="(.*?)"/
            if (mediaMatch){
                mediaMatch[0][2].split (",").each{ media.add(it) }
            }
            tmpResult["id"] = it.id
            tmpResult["view"] = (it.view instanceof JSONNull)?"":it.view
            tmpResult["label"] = (it.label instanceof JSONNull)?"":it.label
            tmpResult["latitude"] = (it.latitude instanceof JSONNull)?"":it.latitude
            tmpResult["longitude"] = (it.longitude instanceof JSONNull)?"":it.longitude
            tmpResult["category"] = (it.category instanceof JSONNull)?"":it.category

            def properties = [:]

            tmpResult["preview"] = [title:title, subtitle: subtitle, media: media, thumbnail: thumbnail]
            tmpResult["properties"] = properties
            docs.add(tmpResult)
        }
        if(jsonResp.results["docs"].get(0).size()>0){
            apisService.fetchItemsProperties(jsonResp.results["docs"].get(0)).eachWithIndex() { obj, i ->
                docs[i].properties = obj
            }
        }
        resultList["facets"] = jsonResp.facets
        resultList["highlightedTerms"] = jsonResp.highlightedTerms
        resultList["results"] = [name:jsonResp.results.name,docs:docs,numberOfDocs:jsonResp.results.numberOfDocs]
        resultList["numberOfResults"] = jsonResp.numberOfResults
        resultList["randomSeed"] = jsonResp.randomSeed
        render (contentType:"text/json"){resultList}
    }
    /**
     * Wrapper to support streaming of files from the backend
     * @return OutPutStream
     */
    def index(){
        if(!params.filename){
            log.warn "binary(): A binary content was requested, but no filename was given in the url"
            throw new ItemNotFoundException("binary(): A binary content was requested, but no filename was given in the url");
        }

        def apiResponse = ApiConsumer.getBinaryStreaming(configurationService.getBinaryBackendUrl(), params.filename, response.outputStream)

        if(!apiResponse.isOk()){
            log.error "binary(): binary content was not found"
            apiResponse.throwException(request)
        }

        def responseObject = apiResponse.getResponse()

        def cacheExpiryInDays = 1
        response.setHeader("Cache-Control", "max-age="+cacheExpiryInDays * 24 * 60 *60)
        response.setHeader("Expires", formatDateForExpiresHeader(cacheExpiryInDays).toString())
        response.setHeader("Content-Disposition", "inline; filename=" + params.filename.tokenize('/')[-1])
        response.setContentType(responseObject.get("Content-Type"))
        response.setContentLength(responseObject.get("Content-Length").toInteger())
    }

    /**
     * Format RFC 2822 date
     * @parameters daysfromtoday, how many days from today do you want the date to be shifted
     * @return date
     */
    private def formatDateForExpiresHeader(daysfromtoday=4){
        def tomorrow= new Date()+daysfromtoday
        String pattern = "EEE, dd MMM yyyy HH:mm:ss Z";
        SimpleDateFormat format = new SimpleDateFormat(pattern, SupportedLocales.EN.getLocale());
        String tomorrowString = String.format(SupportedLocales.EN.getLocale(), '%ta, %<te %<tb %<tY %<tT CET', tomorrow)
        Date date = format.parse(tomorrowString);
        return date
    }
}
