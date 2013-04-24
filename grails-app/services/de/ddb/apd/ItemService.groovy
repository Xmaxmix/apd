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
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.codehaus.groovy.grails.web.util.WebUtils;

import de.ddb.apd.ApiConsumer;
import de.ddb.apd.exception.BackendErrorException;
import de.ddb.apd.exception.ItemNotFoundException;

class ItemService {
    private static final log = LogFactory.getLog(this)

    private static final SOURCE_PLACEHOLDER = '{0}'
    private static final def THUMBNAIL = 'mvth'
    private static final def PREVIEW= 'mvpr'
    private static final def FULL = 'full'
    private static final def ORIG= 'orig'
    private static final def IMAGE= 'image/jpeg'
    private static final def AUDIO = 'audio/mp3'
    private static final def VIDEOMP4 = 'video/mp4'
    private static final def VIDEOFLV = 'video/flv'

    private static final def MAX_LENGTH_FOR_ITEM_WITH_BINARY = 270
    private static final def MAX_LENGTH_FOR_ITEM_WITH_NO_BINARY = 350

    def transactional = false
    def configurationService

    LinkGenerator grailsLinkGenerator

    def findItemById(id) {
        final def componentsPath = "/access/" + id + "/components/"
        final def path = componentsPath + "view"

        def apiResponse = ApiConsumer.getXml(configurationService.getBackendUrl(), path)
        if(!apiResponse.isOk()){
            log.error "findItemById(): Server returned no results -> " + id
            apiResponse.throwException(WebUtils.retrieveGrailsWebRequest().getCurrentRequest())
        }
        def response = apiResponse.getResponse()



        def institution= response.institution
        def item = response.item
        def title = shortenTitle(id, item)
        def fields = response.item.fields.field.findAll()
        def viewerUri = buildViewerUri(item, componentsPath)
        def pageLabel = response.pagelabel

        return [
            'uri': '',
            'viewerUri': viewerUri,
            'institution': institution,
            'item': item,
            'title': title,
            'fields': fields,
            'pageLabel': pageLabel
        ]
    }

    private getItemTitle(id) {
        final def path = "/access/" + id + "/components/title"

        def apiResponse = ApiConsumer.getText(configurationService.getBackendUrl(), path, [:], [:], true)
        if(!apiResponse.isOk()){
            log.error "getItemTitle(): Server returned no results -> " + id
        }
        return apiResponse.getResponse()
    }

    private shortenTitle(id, item) {

        def title = item.title.text()

        def hasBinary = !fetchBinaryList(id).isEmpty()

        if(title.size() <= MAX_LENGTH_FOR_ITEM_WITH_NO_BINARY) {
            return title
        }

        if(hasBinary && title.size() > MAX_LENGTH_FOR_ITEM_WITH_BINARY) {
            return apendDotDot(title.substring(0, MAX_LENGTH_FOR_ITEM_WITH_BINARY))
        } else if(title.size() > MAX_LENGTH_FOR_ITEM_WITH_NO_BINARY) {
            return apendDotDot(title.substring(0, MAX_LENGTH_FOR_ITEM_WITH_NO_BINARY))
        }

        return title
    }

    def apendDotDot(String shortenedTitle){
        def lastSpaceIndex = shortenedTitle.lastIndexOf(' ')
        def shortenedTitleUntilLastSpace  = shortenedTitle.substring(0, lastSpaceIndex)
        shortenedTitleUntilLastSpace + '...'
    }


    private def buildViewerUri(item, componentsPath) {
        if(item.viewers.viewer == null || item.viewers.viewer.isEmpty()) {
            return ''
        }

        def BINARY_SERVER_URI = grailsLinkGenerator.getContextPath()
        def viewerPrefix = item.viewers.viewer.uri.toString()

        if(viewerPrefix.contains(SOURCE_PLACEHOLDER)) {
            def withoutPlaceholder = viewerPrefix.toString() - SOURCE_PLACEHOLDER
            def sourceUri = BINARY_SERVER_URI + componentsPath + 'source'
            def encodedSourceUri= URLEncoder.encode sourceUri, 'UTF-8'
            return withoutPlaceholder + encodedSourceUri
        }
    }

    def findBinariesById(id) {
        def prev = parse(fetchBinaryList(id))
        return prev
    }

    private def fetchBinaryList(id) {
        final def path = "/access/" + id + "/components/binaries"

        def apiResponse = ApiConsumer.getXml(configurationService.getBackendUrl(), path)
        if(!apiResponse.isOk()){
            log.error "fetchBinaryList(): Server returned no results -> " + id
            apiResponse.throwException(WebUtils.retrieveGrailsWebRequest().getCurrentRequest())
        }
        def response = apiResponse.getResponse()

        return response.binary.list()
    }

    private def parse(binaries) {
        def BINARY_SERVER_URI = grailsLinkGenerator.getContextPath()
        def binaryList = []
        def bidimensionalList = []
        String position
        String path
        String type
        //creation of a bi-dimensional list containing the binaries separated for position
        binaries.each { x ->
            if(x.'@position'.toString() != position){
                def subList = []
                bidimensionalList[x.'@position'.toInteger()-1] = subList
                position = x.'@position'.toString()
            }
            bidimensionalList[x.'@position'.toInteger()-1].add(x)
        }
        //creation of a list of binary maps from the bi-dimensional list
        bidimensionalList.each { y ->
            def binaryMap = ['orig' : ['title':'', 'uri': ['image':'','audio':'','video':'']],
                'preview' : ['title':'', 'uri':''],
                'thumbnail' : ['title':'', 'uri':''],
                'full' : ['title':'', 'uri':''],
                'checkValue' : "",
            ]
            y.each { z ->
                path = z.'@path'
                type = z.'@mimetype'
                if(path.contains(ORIG)){
                    if(type.contains(IMAGE)){
                        binaryMap.'orig'.'uri'.'image' = BINARY_SERVER_URI + z.'@path'
                        if(!binaryMap.'orig'.'title') binaryMap.'orig'.'title' = z.'@name'
                    }
                    else if(type.contains(AUDIO)){
                        binaryMap.'orig'.'uri'.'audio' = BINARY_SERVER_URI + z.'@path'
                        binaryMap.'orig'.'title' = z.'@name'
                    }
                    else if(type.contains(VIDEOMP4)||type.contains(VIDEOFLV)){
                        binaryMap.'orig'.'uri'.'video' = BINARY_SERVER_URI + z.'@path'
                        binaryMap.'orig'.'title' = z.'@name'
                    }
                    binaryMap.'checkValue' = "1";
                }
                else if(path.contains(PREVIEW)) {
                    binaryMap.'preview'.'title' = z.'@name'
                    binaryMap.'preview'.'uri' = BINARY_SERVER_URI + z.'@path'
                    binaryMap.'checkValue' = "1";
                } else if (path.contains(THUMBNAIL)) {
                    binaryMap.'thumbnail'.'title' = z.'@name'
                    binaryMap.'thumbnail'.'uri' = BINARY_SERVER_URI + z.'@path'
                    binaryMap.'checkValue' = "1";
                } else if (path.contains(FULL)) {
                    binaryMap.'full'.'title' = z.'@name'
                    binaryMap.'full'.'uri' = BINARY_SERVER_URI + z.'@path'
                    binaryMap.'checkValue' = "1";
                }
            }
            if(binaryMap.'checkValue'){
                binaryList.add(binaryMap)
            }
        }
        return binaryList
    }

    def binariesCounter(binaries){
        def images = 0
        def audios = 0
        def videos = 0
        binaries.each {
            if(it.'orig'.'uri'.'audio' || it.'orig'.'uri'.'video'){
                if(it.'orig'.'uri'.'audio'){
                    audios++
                }
                if(it.'orig'.'uri'.'video'){
                    videos++
                }
            } else if (it.'orig'.'uri'.'image'){
                images++
            }
        }
        return (['images':images,'audios':audios,'videos':videos])
    }


    def getParent(itemId){
        final def parentsPath = "/hierarchy/" + itemId + "/parent/"
        def apiResponse = ApiConsumer.getJson(configurationService.getBackendUrl(), parentsPath)
        if(!apiResponse.isOk()){
            log.error "getParent(): Server returned no parents -> " + itemId
            apiResponse.throwException(WebUtils.retrieveGrailsWebRequest().getCurrentRequest())
        }
        return apiResponse.getResponse()
    }

    def getChildren(itemId){
        final def childrenPath = "/hierarchy/" + itemId + "/children/"
        def apiResponse = ApiConsumer.getJson(configurationService.getBackendUrl(), childrenPath)
        if(!apiResponse.isOk()){
            log.error "getChildren(): Server returned no children -> " + itemId
            apiResponse.throwException(WebUtils.retrieveGrailsWebRequest().getCurrentRequest())
        }
        return apiResponse.getResponse()
    }

}
