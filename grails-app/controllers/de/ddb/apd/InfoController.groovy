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

import de.ddb.apd.ApiConsumer;
import de.ddb.apd.SupportedLocales;
import de.ddb.apd.exception.BackendErrorException;
import de.ddb.apd.exception.ItemNotFoundException;
import org.springframework.web.servlet.support.RequestContextUtils;

class InfoController {

    def configurationService

    def index() {

        def page = "neues"
        if(params.page){
            page = params.page
        }

        def url = configurationService.getStaticUrl()
        def lang = getShortLocale()
        def path = "/static-apd/"+lang+"/"+page+".html"

        def apiResponse = ApiConsumer.getText(url, path, false)
        if(!apiResponse.isOk()){
            log.error "index(): Server returned no results -> " + page
            throw apiResponse.getException()
        }

        def response = apiResponse.getResponse()

        def map = retrieveArguments(response)
        render(view: "info", model: map)
        //        } catch(ItemNotFoundException infe){
        //            log.error "index(): Request for nonexisting item with page: '" + params?.page + "'. Going 404..."
        //            forward controller: "error", action: "notFound"
        //        }
    }


    private def getShortLocale() {
        def locale = RequestContextUtils.getLocale(request)
        return SupportedLocales.getBestMatchingLocale(locale).getLanguage()
    }

    private def retrieveArguments(def content){
        HtmlParser parser = new HtmlParser(content)
        def body = parser.getContentOfFirstTag("body")
        def title = parser.getContentOfFirstTag("title")
        def author = parser.getContentOfMetaTag("author")
        def keywords = parser.getContentOfMetaTag("keywords")
        def robot = parser.getContentOfMetaTag("robots")
        return [title:title, author:author, keywords:keywords, robot:robot, content:body]
    }
}
