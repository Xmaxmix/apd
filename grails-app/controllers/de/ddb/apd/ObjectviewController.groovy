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

import net.sf.json.JSONNull;
import groovyx.net.http.ContentType


class ObjectviewController {

    def institutionService
    def searchService
    def configurationService

    def index() {
        render(view: "objectview", model: [:])
    }

    /**
     * TODO: what does this method do? Who call it? JavaScript client?
     * */
    def getTreeRootItems() {
        def query = params.query
        log.info "Query: ${query}"

        def searchResult = institutionService.searchArchives(query)

        render (contentType: ContentType.JSON.toString()) { searchResult}
    }

    /**
     * When the user clicks a node in the object tree, then the JavaScipt
     * client send a HTTP GET Request via AJAX to get the tree node details.
     *
     * Tree Node Details is the view on the right side of the application.
     * It lists items that matched the query.
     *
     * TODO: why we name it treeNodeDetails? Suggestion: findItemsById
     */
    def getTreeNodeDetails() {
        def id = params.id
        def query = params.query
        def offset = params.offset
        def pagesize = params.pagesize
        def sort = params.sort

        //        log.info """Get tree node details for item with \n
        //                the ID: ${id},\n
        //                query: ${query}, \n
        //                page size: ${pagesize}, \n
        //                offset: ${offset}
        //                sort: ${sort}"""

        def resultsItems = institutionService.searchArchive(query, id, offset, pagesize, sort)?.results[0]?.docs
        resultsItems.each {
            def title
            def subtitle
            def thumbnail
            def media = []


            def titleMatch = it.preview.toString() =~ /(?m)<div (.*?)class="title"(.*?)>(.*?)<\/div>$/
            if (titleMatch) {
                title= titleMatch[0][3]
            }

            def subtitleMatch = it.preview.toString() =~ /(?m)<div (.*?)class="subtitle"(.*?)>(.*?)<\/div>$/
            subtitle = (subtitleMatch)?subtitleMatch[0][3]:""

            def thumbnailMatch = it.preview.toString() =~ /(?m)<img (.*?)src="(.*?)"(.*?)\/>$/
            if (thumbnailMatch) {
                thumbnail= thumbnailMatch[0][2]
            }

            def mediaMatch = it.preview.toString() =~ /(?m)<div (.*?)data-media="(.*?)"/
            if (mediaMatch) {
                mediaMatch[0][2].split (",").each{ media.add(it) }
            }

            HtmlParser htmlParser = new HtmlParser(title)
            def urlFriendlyTitle = StringManipulation.getFriendlyUrlString(htmlParser.getTextWithoutTags())

            it["preview"] = ['title':title,
                'subtitle': subtitle,
                'media': media,
                'thumbnail': thumbnail,
                'urlFriendlyTitle': urlFriendlyTitle]
        }

        render(
                template: "resultsListContainer",
                model: [
                    "results": resultsItems,
                    "offset": offset
                ])

    }

    def getTreeNodeChildren() {
        def id = params.id
        def children = institutionService.getTechtonicFirstLvlHierarchyChildren(params.id).children

        render (contentType: ContentType.JSON.toString()) { children }
    }

    def getTreeNodeObjectCount() {
        def id = params.id
        def name = params.name
        def query = params.query

        render (contentType: ContentType.JSON.toString()) { ["id": id, "count": "-"] }
    }
}
