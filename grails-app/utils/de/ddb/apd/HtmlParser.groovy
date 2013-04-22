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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import groovy.xml.StreamingMarkupBuilder;

import org.ccil.cowan.tagsoup.Parser;

/**
 * Utility class for parsing HTML pages and retrieving specific information from them 
 * 
 * See http://groovy.codehaus.org/Reading+XML+using+Groovy%27s+XmlSlurper
 *
 * @author hla
 */
class HtmlParser {

    def htmlParser

    /**
     * Constructor taking the source code of the page as input param
     * @param htmlPageSource The page source code
     */
    def HtmlParser(String htmlPageSource){
        def tagsoupParser = new Parser()
        tagsoupParser.setFeature(tagsoupParser.namespacesFeature, false)
        def slurper = new XmlSlurper(tagsoupParser)
        htmlParser = slurper.parseText(htmlPageSource)
    }

    /**
     * Returns the content of the first found tag with the given name as html
     * @param tagName The tag to search for
     * @return The content of the tag as html
     */
    def getContentOfFirstTagAsHtml(String tagName){
        def tagContents = htmlParser."**".findAll{it.name() == tagName}
        if(tagContents.size() > 0){
            return (new StreamingMarkupBuilder().bindNode(tagContents[0])).toString()
        }else{
            return ""
        }
    }

    /**
     * Returns the content of the first found tag with the given name as text without any tags
     * @param tagName The tag to search for
     * @return The content of the tag as text 
     */
    def getContentOfFirstTagAsText(String tagName){
        def tagContents = htmlParser."**".findAll{it.name() == tagName}
        if(tagContents.size() > 0){
            return tagContents[0].text()
        }else{
            return ""
        }
    }

    /**
     * Returns the value of the content attribute of the meta-tag with the given name attribute.
     * @param metaTagName The name attribute of the meta-tag
     * @return The value of the content attribute of the metag-tag
     */
    def getContentOfMetaTag(String metaTagName){
        def metaTagContent = htmlParser."**".find{it.name() == 'meta' && it.@name == metaTagName}.@content
        return metaTagContent
    }
}
