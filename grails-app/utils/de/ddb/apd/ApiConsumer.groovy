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

import java.util.regex.Pattern

import grails.util.Holders
import groovy.json.*
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import groovyx.net.http.HttpResponseDecorator.HeadersDecorator;

import org.apache.commons.logging.LogFactory

import de.ddb.apd.exception.BackendErrorException;
import de.ddb.apd.exception.ItemNotFoundException;


/**
 * The main wrapper class for all backend calls.
 * 
 * @author hla
 */
class ApiConsumer {

    /**
     * This String will be appended as a parameter to all backend calls. For example: http://aaa.bbb.ccc/ddd?client=APD
     */
    public static final String APD_CLIENT_NAME = "APD"

    private static final log = LogFactory.getLog(this)
    private static Pattern nonProxyHostsPattern


    /**
     * Requests a TEXT ressource from the backend by calling GET
     * @param baseUrl The base REST-server url
     * @param path The path to the requested resource
     * @param optionalHeaders Optional request headers to add to the request
     * @param fixWrongContentTypeHeader Workaround for a bug in the backend. On some responses that contain only application/text, 
     *      the backend sets a response-type of application/json, causing the parser to crash. So if fixWrongContentTypeHeader is set 
     *      to true, the json-parser is explicitly overwritten with the text-parser.  
     * @return An ApiResponse object containing the server response
     */
    static def getText(String baseUrl, String path, optionalHeaders = [:], fixWrongContentTypeHeader = false) {
        if(fixWrongContentTypeHeader){
            return requestServer(baseUrl, path, [ client: APD_CLIENT_NAME ], Method.GET, ContentType.JSON, optionalHeaders, true)
        }else{
            return requestServer(baseUrl, path, [ client: APD_CLIENT_NAME ], Method.GET, ContentType.TEXT, optionalHeaders)
        }
    }

    /**
     * Requests a JSON ressource from the backend by calling GET
     * @param baseUrl The base REST-server url
     * @param path The path to the requested resource
     * @param optionalHeaders Optional request headers to add to the request
     * @return An ApiResponse object containing the server response
     */
    static def getJson(String baseUrl, String path, optionalHeaders = [:]) {
        return requestServer(baseUrl, path, [ client: APD_CLIENT_NAME ], Method.GET, ContentType.JSON, optionalHeaders)
    }

    /**
     * Requests a XML ressource from the backend by calling GET
     * @param baseUrl The base REST-server url
     * @param path The path to the requested resource
     * @param optionalHeaders Optional request headers to add to the request
     * @return An ApiResponse object containing the server response
     */
    static def getXml(String baseUrl, String path, optionalHeaders = [:]) {
        return requestServer(baseUrl, path, [ client: APD_CLIENT_NAME ], Method.GET, ContentType.XML, optionalHeaders)
    }

    /**
     * Requests a BINARY ressource from the backend by calling GET
     * @param baseUrl The base REST-server url
     * @param path The path to the requested resource
     * @param optionalHeaders Optional request headers to add to the request
     * @return An ApiResponse object containing the server response
     */
    static def getBinary(String baseUrl, String path, optionalHeaders = [:]) {
        return requestServer(baseUrl, path, [ client: APD_CLIENT_NAME ], Method.GET, ContentType.BINARY, optionalHeaders)
    }

    /**
     * Central method to call the backend, all other methods just delegate.
     * @param baseUrl The base REST-server url (e.g. "http://backend.escidoc.org")
     * @param path The path to the requested resource (e.g. "/item/ASDGAHASDFASDFDFDGDSVFFDGSDG/view")
     * @param query The query parameters to append to the request (e.g. "[client: 'APD']")
     * @param method The request method (Method.GET, Method.POST)
     * @param content The expected response content (ContentType.TEXT, ContentType.JSON, ContentType.XML, ContentType.BINARY)
     * @param optionalHeaders Optional request headers to add to the request
     * @param fixWrongContentTypeHeader Workaround for a bug in the backend. On some responses that contain only application/text, 
     *      the backend sets a response-type of application/json, causing the parser to crash. So if fixWrongContentTypeHeader is set 
     *      to true, the json-parser is explicitly overwritten with the text-parser.  
     * @return An ApiResponse object containing the server response
     */
    private static def requestServer(baseUrl, path, query, method, content, optionalHeaders, fixWrongContentTypeHeader = false) {
        def timestampStart = System.currentTimeMillis();

        try {
            def http = new HTTPBuilder(baseUrl)
            setProxy(http, baseUrl)

            http.request(method, content) {
                uri.path = path
                uri.query = query

                optionalHeaders.each { key, value ->
                    headers.put(key, value)
                }

                if(content == ContentType.XML){
                    headers.Accept = 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8'
                }
                if(fixWrongContentTypeHeader){
                    http.parser.'application/json' = http.parser.'text/html'
                }

                response.success = { resp, output ->
                    switch(content) {
                        case ContentType.TEXT:
                            return build200Response(timestampStart, uri.toString(), method.toString(), content.toString(), resp.headers, output.getText())
                        case ContentType.JSON:
                            return build200Response(timestampStart, uri.toString(), method.toString(), content.toString(), resp.headers, output)
                        case ContentType.XML:
                            return build200Response(timestampStart, uri.toString(), method.toString(), content.toString(), resp.headers, output)
                        case ContentType.BINARY:
                            return build200Response(timestampStart, uri.toString(), method.toString(), content.toString(), resp.headers, [bytes: output.getBytes(), "Content-Type": resp.headers.'Content-Type', "Content-Length": resp.headers.'Content-Length'])
                        default:
                            return build200Response(timestampStart, uri.toString(), method.toString(), content.toString(), resp.headers, output)
                    }
                    //                    if(content == ContentType.TEXT){
                    //                        return build200Response(timestampStart, uri.toString(), method.toString(), content.toString(), output.getText())
                    //                    }else if(content == ContentType.JSON){
                    //                        return build200Response(timestampStart, uri.toString(), method.toString(), content.toString(), output)
                    //                    }else if(content == ContentType.XML){
                    //                        return build200Response(timestampStart, uri.toString(), method.toString(), content.toString(), output)
                    //                    }else if(content == ContentType.BINARY){
                    //                        return build200Response(timestampStart, uri.toString(), method.toString(), content.toString(), [bytes: output.getBytes(), "Content-Type": resp.headers.'Content-Type', "Content-Length": resp.headers.'Content-Length'])
                    //                    }else{
                    //                        return build200Response(timestampStart, uri.toString(), method.toString(), content.toString(), output)
                    //                    }
                }
                response.'404' = { resp ->
                    return build404Response(timestampStart, uri.toString(), method.toString(), content.toString(), resp.headers, "Server answered 404 -> " + uri.toString())
                }
                response.failure = { resp ->
                    return build500Response(timestampStart, uri.toString(), method.toString(), content.toString(), resp.headers, "Server answered 500 -> " + uri.toString() + " / " + resp.statusLine + "/"+resp.statusLine.statusCode +"/"+resp.statusLine.reasonPhrase)
                }
            }
        } catch (groovyx.net.http.HttpResponseException ex) {
            log.error "requestText(): A HttpResponseException occured", ex
            return build500ResponseWithException(timestampStart, baseUrl + path + query, method.toString(), content.toString(), ex)
        } catch (java.net.ConnectException ex) {
            log.error "requestText(): A ConnectException occured", ex
            return build500ResponseWithException(timestampStart, baseUrl + path + query, method.toString(), content.toString(), ex)
        } catch (java.lang.Exception ex) {
            log.error "requestText(): An Exception occured", ex
            return build500ResponseWithException(timestampStart, baseUrl + path + query, method.toString(), content.toString(), ex)
        }
    }

    /**
     * Utility method to build the ApiResponse object for successfull requests
     * @param timestampStart The timestamp when the request was send
     * @param calledUrl The complete URL that was called
     * @param method The request method (Method.GET, Method.POST)
     * @param content The expected response content (ContentType.TEXT, ContentType.JSON, ContentType.XML, ContentType.BINARY)
     * @param responseHeader The headers of the response
     * @param responseObject The response from the server
     * @return An ApiResponse object containing the server response
     */
    private static def build200Response(timestampStart, calledUrl, method, content, responseHeader, responseObject){
        def duration = System.currentTimeMillis()-timestampStart
        def response = new ApiResponse(calledUrl, method.toString(), content, responseObject, duration, null, ApiResponse.HttpStatus.HTTP_200, responseHeader)
        log.info response.toString()
        return response
    }

    /**
     * Utility method to build the ApiResponse object for 404 responses
     * @param timestampStart The timestamp when the request was send
     * @param calledUrl The complete URL that was called
     * @param method The request method (Method.GET, Method.POST)
     * @param content The expected response content (ContentType.TEXT, ContentType.JSON, ContentType.XML, ContentType.BINARY)
     * @param responseHeader The headers of the response
     * @param exceptionDescription The text for the ItemNotFoundException that will be attached but not thrown
     * @return An ApiResponse object containing the response information
     */
    private static def build404Response(timestampStart, calledUrl, method, content, responseHeader, exceptionDescription){
        def duration = System.currentTimeMillis()-timestampStart
        def exception = new ItemNotFoundException(exceptionDescription)
        def response = new ApiResponse(calledUrl, method.toString(), content, "", duration, exception, ApiResponse.HttpStatus.HTTP_404, responseHeader)
        log.info response.toString()
        return response
    }

    /**
     * Utility method to build the ApiResponse object for 500 responses
     * @param timestampStart The timestamp when the request was send
     * @param calledUrl The complete URL that was called
     * @param method The request method (Method.GET, Method.POST)
     * @param content The expected response content (ContentType.TEXT, ContentType.JSON, ContentType.XML, ContentType.BINARY)
     * @param responseHeader The headers of the response
     * @param exceptionDescription The text for the BackendErrorException that will be attached but not thrown
     * @return An ApiResponse object containing the response information
     */
    private static def build500Response(timestampStart, calledUrl, method, content, responseHeader, exceptionDescription){
        def duration = System.currentTimeMillis()-timestampStart
        def exception = new BackendErrorException(exceptionDescription)
        def response = new ApiResponse(calledUrl, method.toString(), content, "", duration, exception, ApiResponse.HttpStatus.HTTP_500, responseHeader)
        log.info response.toString()
        return response
    }

    /**
     * Utility method to build the ApiResponse object for 500 responses
     * @param timestampStart The timestamp when the request was send
     * @param calledUrl The complete URL that was called
     * @param method The request method (Method.GET, Method.POST)
     * @param content The expected response content (ContentType.TEXT, ContentType.JSON, ContentType.XML, ContentType.BINARY)
     * @param exceptionDescription The actual exception that occured
     * @return An ApiResponse object containing the response information
     */
    private static def build500ResponseWithException(timestampStart, calledUrl, method, content, exception){
        def duration = System.currentTimeMillis()-timestampStart
        def response = new ApiResponse(calledUrl, method.toString(), content, "", duration, exception, ApiResponse.HttpStatus.HTTP_500, [:])
        log.info response.toString()
        return response
    }

    /**
     * Sets the proxy information for each request
     * @param http The HttpBuilder object
     * @param baseUrl The base request URL
     * @return void
     */
    private static def setProxy(http, String baseUrl) {
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
}
