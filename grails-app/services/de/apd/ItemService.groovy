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
package de.apd

import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.web.mapping.LinkGenerator

import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

import groovyx.net.http.HTTPBuilder


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
    def grailsApplication
    LinkGenerator grailsLinkGenerator

    def findItemById(id) {
        def http = new HTTPBuilder(grailsApplication.config.apd.backend.url.toString())
        ApiConsumer.setProxy(http, grailsApplication.config.apd.backend.url.toString())

        /* TODO remove this hack, once the server deliver the right content
         type*/
        http.parser.'application/json' = http.parser.'application/xml'

        final def componentsPath = "/access/" + id + "/components/"
        final def viewPath = componentsPath + "view"

        def institution, item, title, fields, viewerUri, pageLabel
        http.request( GET) { req ->
            uri.path = viewPath

            response.success = { resp, xml ->
                institution= xml.institution
                item = xml.item

                title = shortenTitle(id, item)

                fields = xml.item.fields.field.findAll()
                viewerUri = buildViewerUri(item, componentsPath)

                return ['uri': '', 'viewerUri': viewerUri, 'institution': institution, 'item': item, 'title': title,
                    'fields': fields, pageLabel: xml.pagelabel]
            }

            response.'404' = { return '404' }

            //TODO: handle other failure such as '500'
            response.failure = { resp ->
                log.warn """
                Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}
                """
                return response
            }
        }
    }

    private getItemTitle(id) {
        def http = new HTTPBuilder(grailsApplication.config.apd.backend.url.toString())
        ApiConsumer.setProxy(http, grailsApplication.config.apd.backend.url.toString())

        /* TODO remove this hack, once the server deliver the right content
         type*/
        http.parser.'application/json' = http.parser.'text/html'

        final def componentsPath = "/access/" + id + "/components/"
        final def titlePath = componentsPath + "title"

        http.request( GET) { req ->
            uri.path = titlePath

            response.success = { resp, html -> return html }

            response.'404' = { return '404' }

            //TODO: handle other failure such as '500'
            response.failure = { resp -> log.warn """
                Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}
                """ }
        }
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

        def http = new HTTPBuilder(grailsApplication.config.apd.backend.url.toString())
        ApiConsumer.setProxy(http, grailsApplication.config.apd.backend.url.toString())
        http.parser.'application/json' = http.parser.'application/xml'
        final def binariesPath= "/access/" + id + "/components/binaries"

        http.request( GET) { req ->
            uri.path = binariesPath

            response.success = { resp, xml ->
                def binaries = xml
                return binaries.binary.list()
            }

            response.'404' = { return '404' }

            //TODO: handle other failure such as '500'
            response.failure = { resp -> log.warn """
                Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}
                """ }
        }
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
        return ApiConsumer.getTextAsJson(grailsApplication.config.apd.backend.url.toString(), parentsPath, [:]);
    }

    def getChildren(itemId){
        final def childrenPath = "/hierarchy/" + itemId + "/children/"
        return ApiConsumer.getTextAsJson(grailsApplication.config.apd.backend.url.toString(), childrenPath, [:]);
    }

    private def log(list) {
        list.each { it ->
            log.debug "---"
            log.debug "name: ${it.'@name'}"
            log.debug "mime: ${it.'@mimetype'}"
            log.debug "path: ${it.'@path'}"
            log.debug "pos: ${it.'@position'}"
            log.debug "is primary?: ${it.'@primary'}"
        }
    }

    private def log(resp, xml) {
        // print response
        log.debug "response status: ${resp.statusLine}"
        log.debug 'Headers: -----------'

        resp.headers.each { h -> log.debug " ${h.name} : ${h.value}" }

        log.debug 'Response data: -----'
        log.debug xml
        log.debug '\n--------------------'

        // parse
        assert xml instanceof groovy.util.slurpersupport.GPathResult
    }
}
