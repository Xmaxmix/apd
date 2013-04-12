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
        def title = fetchTitle(content)
        def author = fetchAuthor(content)
        def keywords = fetchKeywords(content)
        def robot = fetchRobots(content)
        def body = fetchBody(content)
        return [title:title, author:author, keywords:keywords, robot:robot, content:body]
    }

    private def fetchBody(content) {
        def bodyMatch = content =~ /(?s)<body\b[^>]*>(.*?)<\/body>/
        return bodyMatch[0][1]
    }

    private def fetchAuthor(content) {
        def authorMatch = content =~ /(?s)<meta (.*?)name="author" (.*?)content="(.*?)"(.*?)\/>/
        if (authorMatch)
            return authorMatch[0][3]
    }

    private def fetchTitle(content) {
        def titleMatch = content =~ /(?s)<title\b[^>]*>(.*?)<\/title>/
        if (titleMatch)
            return titleMatch[0][1]
    }

    private def fetchKeywords(content) {
        def keywordMatch = content =~ /(?s)<meta (.*?)name="keywords" (.*?)content="(.*?)"(.*?)\/>/
        if (keywordMatch)
            return keywordMatch[0][3]
    }

    private def fetchRobots(content) {
        def robotMatch = content =~ /(?s)<meta (.*?)name="robots" (.*?)content="(.*?)"(.*?)\/>/
        if (robotMatch)
            return robotMatch[0][3]
    }
}
