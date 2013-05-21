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

        def siblingInformation = getSiblingInformation(id, item.title)

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
                    'siblingInformation': siblingInformation,
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
        //def parentList = itemService.getParent(id)
        def parentList = institutionService.getInstitutionParent(id)


        def startItem = new Item(['id': id, 'label': label])

        def rootItem = Item.buildHierarchy(startItem, parentList)

        // Get the mainItem
        Item mainItem = rootItem.getItemFromHierarchy(id)
        mainItem.setMainItem(true)

        //Build an empty node to function as the hidden root-node of the tree
        Item emptyStartItem = new Item()
        emptyStartItem.children.add(rootItem)

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

    def buildNavigationData(def params) {
        def navData = [:]

        def hitNumber = 1
        if(params.hitNumber){
            hitNumber = params.hitNumber.toInteger()
        }
        def currentId = params.id
        if(params.searchId){
            currentId = params.searchId
        }
        def pagesize = 1
        if(params.pagesize) {
            pagesize = params.pagesize.toInteger()
        }
        def searchOffset = 0
        if(hitNumber > 1) {
            searchOffset = hitNumber - 2
        }
        def sort = "RELEVANCE"
        if(params.sort){
            sort = params.sort
        }
        def nodeId = "rootnode"
        if(params.nodeId) {
            nodeId = params.nodeId
        }

        //def resultsItems = institutionService.searchArchive(params.query, params.id, params.offset, params.pagesize, params.sort)
        def firstResultItem = institutionService.searchArchive(params.query, nodeId, 0, 1, sort)
        def resultCount = firstResultItem["numberOfResults"]
        def lastResultItem = institutionService.searchArchive(params.query, nodeId, resultCount-1, 1, sort)
        def resultsItems = institutionService.searchArchive(params.query, nodeId, searchOffset, 3, sort)


        def firstHitId = firstResultItem.results[0]["docs"]?.get(0)?.id
        def lastHitId = lastResultItem.results[0]["docs"]?.get(0)?.id
        def previousHitId = "none"
        if(hitNumber > 1){
            previousHitId = resultsItems.results[0]["docs"]?.get(0)?.id
        }
        def nextHitId = "none"
        if(hitNumber < resultCount){
            def currentIdIndex = Integer.MAX_VALUE;
            for(int i=0; i<resultsItems.results[0]["docs"].size(); i++){
                if(resultsItems.results[0]["docs"].getAt(i).id == currentId){
                    currentIdIndex = i
                    break;
                }
            }
            if(currentIdIndex < resultsItems.results[0]["docs"].size() - 1) {
                nextHitId = resultsItems.results[0]["docs"]?.get(currentIdIndex + 1)?.id
            }
        }

        def newOffset = (int)((hitNumber - 1) / pagesize)

        navData["resultCount"] = resultCount
        navData["hitNumber"] = hitNumber
        navData["pagesize"] = pagesize
        navData["previousHit"] = previousHitId
        navData["nextHit"] = nextHitId
        navData["firstHit"] = firstHitId
        navData["lastHit"] = lastHitId
        navData["newOffset"] = newOffset

        return navData
    }

}
