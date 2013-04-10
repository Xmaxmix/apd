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

import java.util.regex.Pattern

import static groovyx.net.http.ContentType.*
import grails.util.Holders
import groovy.json.*
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

import java.util.regex.Pattern

import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.web.context.ServletContextHolder


class ApiConsumer {
    private static final log = LogFactory.getLog(this)
    private static Pattern nonProxyHostsPattern

    static def postText(String baseUrl, String path, query, method = Method.POST) {
        try {
            path = checkContext(baseUrl, path)
            def http = new HTTPBuilder(baseUrl)
            setProxy(http, baseUrl)

            def timestampStart = System.currentTimeMillis();
            http.request(method, ContentType.TEXT) {
                uri.path = path
                uri.query = query
                response.success = { resp, reader ->
                    log.info "postText(): Current request uri: 200, "+(System.currentTimeMillis()-timestampStart)+"ms, "+uri
                    log.debug "response status: ${resp.statusLine}"
                    log.debug 'Headers: -----------'
                    resp.headers.each { h ->
                        log.debug " ${h.name} : ${h.value}"
                    }
                    return reader.getText()
                }
                response.'404' = {
                    log.error "postText(): Current request uri: 404, "+(System.currentTimeMillis()-timestampStart)+"ms, "+uri
                    return 'Not found'
                }
                response.failure = { resp ->
                    log.error "postText(): Current request uri: 500, "+(System.currentTimeMillis()-timestampStart)+"ms, "+uri+", Unexpected error: ${resp.statusLine} : ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
                    return null
                }
            }
        } catch (groovyx.net.http.HttpResponseException ex) {
            log.error "postText(): A HttpResponseException occured", ex
            return null
        } catch (java.net.ConnectException ex) {
            log.error "postText(): A ConnectException occured", ex
            return null
        } catch (java.lang.Exception ex) {
            log.error "postText(): An unexpected exception occured", ex
            return null
        }
    }

    static def getText(String baseUrl, String path, query) {
        return postText(baseUrl, path, query, Method.GET)
    }

    static def getTextAsJson(String baseUrl, String path, query, method = Method.GET) {
        try {
            path = checkContext(baseUrl, path)
            def http = new HTTPBuilder(baseUrl)
            setProxy(http, baseUrl)

            def timestampStart = System.currentTimeMillis();
            http.request(method, JSON) {
                uri.path = path
                uri.query = query
                response.success = { resp, json ->
                    log.info "getTextAsJson(): Current request uri: 200, "+(System.currentTimeMillis()-timestampStart)+"ms, "+uri
                    log.debug "response status: ${resp.statusLine}"
                    log.debug 'Headers: -----------'
                    resp.headers.each { h -> log.debug " ${h.name} : ${h.value}" }
                    return json
                }
                response.failure = { resp ->
                    log.error "getTextAsJson(): Current request uri: 500, "+(System.currentTimeMillis()-timestampStart)+"ms, "+uri+", Unexpected error: ${resp.statusLine} : ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
                    //                    response.
                    log.debug 'Headers: -----------'

                    resp.headers.each { h -> log.debug " ${h.name} : ${h.value}" }
                    return null
                }
                response.'404' = {resp, reader ->
                    log.error "getTextAsJson(): Current request uri: 404, "+(System.currentTimeMillis()-timestampStart)+"ms, "+uri
                    log.debug 'Headers: -----------'
                    resp.headers.each { h -> log.debug " ${h.name} : ${h.value}" }
                    return 'Not found'
                }
            }
        } catch (groovyx.net.http.HttpResponseException ex) {
            log.error "getTextAsJson(): A HttpResponseException occured", ex
            return null
        } catch (java.net.ConnectException ex) {
            log.error "getTextAsJson(): A ConnectException occured", ex
            return null
        } catch (java.lang.Exception ex) {
            log.error "getTextAsJson(): An unexpected exception occured", ex
            return null
        }
    }

    static def getTextAsXml(String baseUrl, String path, query, method = Method.GET) {
        try {
            path = checkContext(baseUrl, path)
            def http = new HTTPBuilder(baseUrl)
            setProxy(http, baseUrl)

            def timestampStart = System.currentTimeMillis();
            http.request(method, XML) {
                uri.path = path
                uri.query = query
                headers.Accept = 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8'
                response.success = { resp, xml ->
                    log.info "getTextAsXml(): Current request uri: 200, "+(System.currentTimeMillis()-timestampStart)+"ms, "+uri
                    log.debug "response status: ${resp.statusLine}"
                    log.debug 'Headers: -----------'
                    resp.headers.each { h -> log.debug " ${h.name} : ${h.value}" }
                    return xml
                }
                response.'404' = {resp, reader ->
                    log.error "getTextAsXml(): Current request uri: 404, "+(System.currentTimeMillis()-timestampStart)+"ms, "+uri
                    log.debug 'Headers: -----------'
                    resp.headers.each { h -> log.debug " ${h.name} : ${h.value}" }
                    return 'Not found'
                }
                response.failure = { resp ->
                    log.error "getTextAsXml(): Current request uri: 500, "+(System.currentTimeMillis()-timestampStart)+"ms, "+uri+", Unexpected error: ${resp.statusLine} : ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
                    log.debug 'Headers: -----------'

                    resp.headers.each { h -> log.debug " ${h.name} : ${h.value}" }
                    return null
                }
            }
        } catch (groovyx.net.http.HttpResponseException ex) {
            log.error "getTextAsXml(): A HttpResponseException occured", ex
            return null
        } catch (java.net.ConnectException ex) {
            log.error "getTextAsXml(): A ConnectException occured", ex
            return null
        } catch (java.lang.Exception ex) {
            log.error "getTextAsXml(): An unexpected exception occured", ex
            return null
        }
    }

    static def getAnyText(String baseUrl, String path, query, method = Method.GET) {
        try {
            path = checkContext(baseUrl, path)
            def http = new HTTPBuilder(baseUrl)
            setProxy(http, baseUrl)

            def timestampStart = System.currentTimeMillis();
            http.request(method, ContentType.ANY) {
                uri.path = path
                uri.query = query
                response.success = { resp, reader ->
                    log.info "getAnyText(): Current request uri: 200, "+(System.currentTimeMillis()-timestampStart)+"ms, "+uri
                    log.debug "response status: ${resp.statusLine}"
                    log.debug 'Headers: -----------'
                    resp.headers.each { h -> log.debug " ${h.name} : ${h.value}" }
                    log.debug reader
                    return reader
                }
                response.'404' = {resp, reader ->
                    log.error "getAnyText(): Current request uri: 404, "+(System.currentTimeMillis()-timestampStart)+"ms, "+uri
                    log.debug 'Headers: -----------'
                    resp.headers.each { h -> log.debug " ${h.name} : ${h.value}" }
                    return 'Not found'
                }
                response.failure = { resp ->
                    log.error "getAnyText(): Current request uri: 500, "+(System.currentTimeMillis()-timestampStart)+"ms, "+uri+", Unexpected error: ${resp.statusLine} : ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
                    log.debug 'Headers: -----------'
                    resp.headers.each { h -> log.debug " ${h.name} : ${h.value}" }
                    return null
                }
            }
        } catch (groovyx.net.http.HttpResponseException ex) {
            log.error "getAnyText(): A HttpResponseException occured", ex
            return null
        } catch (java.net.ConnectException ex) {
            log.error "getAnyText(): A ConnectException occured", ex
            return null
        } catch (java.lang.Exception ex) {
            log.error "getAnyText(): An unexpected exception occured", ex
            return null
        }
    }

    static def getBinaryContent(String baseUrl, String path, query, method = Method.GET) {
        try {
            path = checkContext(baseUrl, path)
            def http = new HTTPBuilder(baseUrl)
            setProxy(http, baseUrl)

            def timestampStart = System.currentTimeMillis();
            http.request(method, ContentType.BINARY) {
                uri.path = path
                uri.query = query
                response.success = { resp, inputstream ->
                    log.debug "Success getBinaryContent(): Current request uri: 200, "+(System.currentTimeMillis()-timestampStart)+"ms, "+uri
                    log.debug "response status: ${resp.statusLine}"
                    log.debug 'Headers: -----------'

                    resp.headers.each { h -> log.debug " ${h.name} : ${h.value}" }
                    return [bytes:inputstream.getBytes(),"Content-Type":resp.headers.'Content-Type',"Content-Length":resp.headers.'Content-Length']
                }
                response.'404' = {resp, reader ->
                    log.error "404 getBinaryContent(): Current request uri: 404, "+(System.currentTimeMillis()-timestampStart)+"ms, "+uri
                    log.debug 'Headers: -----------'
                    resp.headers.each { h -> log.debug " ${h.name} : ${h.value}" }
                    return 'Not found'
                }
                response.failure = { resp ->
                    log.error "Failure getBinaryContent(): Current request uri: 500, "+(System.currentTimeMillis()-timestampStart)+"ms, "+uri+", Unexpected error: ${resp.statusLine} : ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
                    log.debug 'Headers: -----------'
                    resp.headers.each { h -> log.debug " ${h.name} : ${h.value}" }
                    return null
                }
            }
        } catch (groovyx.net.http.HttpResponseException ex) {
            log.error "getBinaryContent(): A HttpResponseException occured", ex
            return null
        } catch (java.net.ConnectException ex) {
            log.error "getBinaryContent(): A ConnectException occured", ex
            return null
        } catch (java.lang.Exception ex) {
            log.error "getBinaryContent(): An unexpected exception occured", ex
            return null
        }
    }

    static def setProxy(http, String baseUrl) {
        def proxyHost = System.getProperty("http.proxyHost")
        def proxyPort = System.getProperty("http.proxyPort")
        def nonProxyHosts = System.getProperty("http.nonProxyHosts")

        if (proxyHost) {
            if (nonProxyHosts) {
                if (!nonProxyHostsPattern) {
                    nonProxyHosts = nonProxyHosts.replaceAll("\\.", "\\\\.")
                    nonProxyHosts = nonProxyHosts.replaceAll("\\*", "")
                    nonProxyHosts = nonProxyHosts.replaceAll("\\?", "\\\\?")
                    nonProxyHostsPattern = Pattern.compile(nonProxyHosts)
                }
                if (nonProxyHostsPattern.matcher(baseUrl).find()) {
                    return
                }
            }
            if (!proxyPort) {
                proxyPort = "80"
            }
            http.setProxy(proxyHost, new Integer(proxyPort), 'http')
        }
    }

    static def checkContext(String baseUrl, String path) {
        def grailsApplication = Holders.getGrailsApplication()
        if (grailsApplication.config.ddb.apis.url == baseUrl) {
            if (path == null) {
                path = ""
            }
            path = ServletContextHolder.servletContext.contextPath + path
        }
        return path
    }

}
