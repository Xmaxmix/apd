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

import de.ddb.apd.hierarchy.Item

class DetailviewController {

    def itemService
    def configurationService
    def institutionService


    def index() {
        //Check if Item-Detail was called from search-result and fill parameters
        //def searchResultParameters = handleSearchResultParameters(params, request)
        def id = params.id

        def item = itemService.findItemById(id)

        def friendlyTitle = StringManipulation.getFriendlyUrlString(item.title)

        def hierarchyRootItem = buildItemHierarchy(id, item.title, item.institution)

        def binaryList = itemService.findBinariesById(id)
        def binariesCounter = itemService.binariesCounter(binaryList)

        def binaryInformation = [:]
        binaryInformation.all = [binaryList.size]
        binaryInformation.images = [binariesCounter.images]
        binaryInformation.audios = [binariesCounter.audios]
        binaryInformation.videos = [binariesCounter.videos]

        if (item.pageLabel?.isEmpty()) {
            item.pageLabel= itemService.getItemTitle(id)
        }

        def navData = buildNavigationData(params)

        def itemUri = request.forwardURI
        def ddbUri = configurationService.getDDBUrl() + "/item/" + id
        def fields = translate(item.fields)

        render(
                view: "detailview",
                model: ['itemUri': itemUri,
                    'ddbUri': ddbUri,
                    'viewerUri': item.viewerUri,
                    'title': item.title,
                    'friendlyTitle': friendlyTitle,
                    'item': item.item,
                    'itemId': id,
                    'institution': item.institution,
                    'fields': fields,
                    'binaryList': binaryList,
                    'pageLabel': item.pageLabel,
                    'hierarchyRoot': hierarchyRootItem,
                    //'firstHit': searchResultParameters["searchParametersMap"]["firstHit"],
                    //'lastHit': searchResultParameters["searchParametersMap"]["lastHit"],
                    //'hitNumber': params["hitNumber"],
                    //'results': searchResultParameters["resultsItems"],
                    //'searchResultUri': searchResultParameters["searchResultUri"],
                    'binaryInformation': binaryInformation,
                    'navData': navData]
                )
    }

    def translate(fields) {
        fields.each {
            def messageKey = 'apd.' + it.'@id'
            def translated = message(code: messageKey)
            if(translated != messageKey) {
                it.name = translated
            } else {
                it.name = it.name.toString().capitalize()
                log.warn 'can not find message property: ' + messageKey + ' use ' + it.name + ' instead.'
            }
        }
    }

    def buildItemHierarchy(id, label, institution) {

        // Build the hierarchy from the item to the root element. The root element is kept.
        def parentList = itemService.getParent(id)

        def startItem = new Item(['id': id, 'label': label])

        def rootItem = Item.buildHierarchy(startItem, parentList)

        // Get the mainItem
        Item mainItem = rootItem.getItemFromHierarchy(id)
        mainItem.setMainItem(true)

        Item institutionRootItem = new Item(['id': institution.id, 'label': institution.name, 'aggregationEntity': true])

        Item emptyStartItem = new Item()
        emptyStartItem.children.add(institutionRootItem)
        institutionRootItem.children.add(rootItem)

        return emptyStartItem
    }

    def buildNavigationData(def params) {
        def navData = [:]

        def resultsItems = institutionService.searchArchive(params.query, params.id, params.offset, params.pagesize, params.sort)

        def hitNumber = 1
        for(int i=0; i<resultsItems.results[0]["docs"].size(); i++){
            if(resultsItems.results[0]["docs"].get(i).id == params.id){
                hitNumber = i + 1;
            }
        }

        navData["results"] = resultsItems
        navData["hitNumber"] = hitNumber
        navData["firstHit"] = resultsItems.results[0]["docs"].get(0).id
        navData["lastHit"] = resultsItems.results[0]["docs"].get(resultsItems.results[0]["docs"].size()-1).id

        return navData
    }
}
