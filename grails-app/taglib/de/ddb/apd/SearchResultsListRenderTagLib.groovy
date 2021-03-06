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

class SearchResultsListRenderTagLib {

    /**
     * Renders the item results.
     *
     * @attr results REQUIRED organizations list
     */
    def searchService

//    def itemResultsRender = { attrs, body ->
//        out << render(template:"/objectview/resultsList", model:[results: attrs.results,
//                                                                 confBinary: request.getContextPath()])
//    }

    def truncateItemTitle = { attrs, body ->
        out << searchService.trimTitle(attrs.title.toString(), attrs.length)
    }

    def truncateHovercardTitle = { attrs, body ->
        out << searchService
                .trimString(attrs.title.toString().replaceAll("<match>", "")
                .replaceAll("</match>", ""), attrs.length)
    }
}
