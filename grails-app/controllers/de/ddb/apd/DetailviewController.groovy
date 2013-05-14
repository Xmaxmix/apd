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

        def siblingInformation = getSiblingInformation(id, item.title)

        // TODO: handle 404 and failure separately. HTTP Status Code 404, should
        // to `not found` page _and_ Internal Error should go to `internal server
        // error` page. We should send also the HTTP Status Code 404 or 500 to the
        // Client.
        if(item == '404' || item?.failure) {
            redirect(controller: 'error')
        } else {
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
                        'siblingInformation': siblingInformation]
                    )
        }
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

    def getSiblingInformation(id, name) {
        def siblingInformation = [:]

        def siblings = []
        def parentList = itemService.getParent(id)
        if(parentList.size() > 1){
            def parentId = parentList.get(0).parent
            def childList = itemService.getChildren(parentId)
            siblings.addAll(childList)
        }else if(parentList.size() == 1){
            siblings.add(parentList.get(0))
        }else{
            siblings.add(['id': id, 'label': name])
        }
        siblingInformation["siblings"] = siblings

        for(int i=0; i<siblings.size(); i++){
            if(siblings[i].id == id){
                if(i==0 && i==siblings.size()-1){
                    siblingInformation["previous"] = null
                    siblingInformation["next"] = null
                }else if(i==0){
                    siblingInformation["previous"] = siblings[siblings.size()-1]
                    siblingInformation["next"] = siblings[i+1]
                }else if(i==siblings.size()-1){
                    siblingInformation["previous"] = siblings[i-1]
                    siblingInformation["next"] = siblings[0]
                }else{
                    siblingInformation["previous"] = siblings[i-1]
                    siblingInformation["next"] = siblings[i+1]
                }
            }
        }

        return siblingInformation
    }

}
