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

import groovyx.net.http.ContentType

import javax.servlet.http.HttpServletResponse

class StructureviewController {

    def institutionService
    def configurationService

    def index() {

        def query = params.query

        render (view: 'structureview',  model: [:])
    }

    def getTreeRootItems() {
        def query = params.query
        def searchResult = institutionService.searchArchivesForStructure(query)

        render (contentType: ContentType.JSON.toString()) { searchResult}
    }

    def getTreeNodeDetails() {
        def id = params.id
        def query = params.query
        def isInstitution = params.isInstitution

        //def searchResults = institutionService.searchArchive(query, id, offset, pagesize)

        //def foundInstitution = institutionService.findInstitutionForId(id)

        def itemId = id
        def vApiInstitution = new ApiInstitution()
        log.debug("read insitution by item id: ${id}")
        def selectedOrgXML = vApiInstitution.getInstitutionViewByItemId(id, configurationService.getBackendUrl())

        if (selectedOrgXML) {
            def jsonOrgParentHierarchy = vApiInstitution.getParentsOfInstitutionByItemId(id, configurationService.getBackendUrl())
            log.debug("jsonOrgParentHierarchy: ${jsonOrgParentHierarchy}")
            if (jsonOrgParentHierarchy.size() == 1) {
                if (jsonOrgParentHierarchy[0].id != id) {
                    log.error("ERROR: id:${id} != OrgParent.id:${jsonOrgParentHierarchy[0].id}")
                    forward controller: 'error', action: "ERROR: id:${id} != OrgParent.id:${jsonOrgParentHierarchy[0].id}"
                }
            }
            else if (jsonOrgParentHierarchy.size() > 1) {
                itemId = jsonOrgParentHierarchy[jsonOrgParentHierarchy.size() - 1].id
            }
            log.debug("root itemId = ${itemId}")
            def jsonOrgSubHierarchy = vApiInstitution.getChildrenOfInstitutionByItemId(itemId, configurationService.getBackendUrl())
            log.debug("jsonOrgSubHierarchy: ${jsonOrgSubHierarchy}")
            def jsonFacets = vApiInstitution.getFacetValues(selectedOrgXML.name.text(), configurationService.getBackendUrl())
            int countObjectsForProv = 0
            if ((jsonFacets != null)&&(jsonFacets.facetValues != null)&&(jsonFacets.facetValues.count != null)&&(jsonFacets.facetValues.count[0] != null)) {
                try {
                    countObjectsForProv = jsonFacets.facetValues.count[0].intValue()
                }
                catch (NumberFormatException ex) {
                    countObjectsForProv = -1
                }
            }
            render(
                    template: "detailView",
                    model: [
                        itemId: itemId,
                        selectedItemId: id,
                        selectedOrgXML: selectedOrgXML,
                        subOrg: jsonOrgSubHierarchy,
                        parentOrg: jsonOrgParentHierarchy,
                        countObjcs: countObjectsForProv,
                        vApiInst: vApiInstitution
                    ])
        } else {
            forward controller: 'error', action: "notfound"
        }
    }

    def getTreeNodeChildren() {
        def id = params.id
        def children = institutionService.getTechtonicFirstLvlHierarchyChildren(params.id).children

        render (contentType: ContentType.JSON.toString()) { children }
    }

    def getTreeNodeParents() {
        def id = params.id
        def parents = institutionService.getInstitutionParent(id)

        render (contentType: ContentType.JSON.toString()) { parents }
    }

    def ajaxDetails() {

        def id = params.id
        def itemId = id
        def vApiInstitution = new ApiInstitution()
        def selectedOrgXML = vApiInstitution.getInstitutionViewByItemId(id, configurationService.getBackendUrl())
        if (selectedOrgXML) {
            def jsonOrgParentHierarchy = vApiInstitution.getParentsOfInstitutionByItemId(id, configurationService.getBackendUrl())
            if (jsonOrgParentHierarchy.size() == 1) {
                if (jsonOrgParentHierarchy[0].id != id) {
                    forward controller: 'error', action: "ERROR: id:${id} != OrgParent.id:${jsonOrgParentHierarchy[0].id}"
                }
            }
            else if (jsonOrgParentHierarchy.size() > 1) {
                itemId = jsonOrgParentHierarchy[jsonOrgParentHierarchy.size() - 1].id
            }
            def jsonOrgSubHierarchy = vApiInstitution.getChildrenOfInstitutionByItemId(itemId, configurationService.getBackendUrl())
            def jsonFacets = vApiInstitution.getFacetValues(selectedOrgXML.name.text(), configurationService.getBackendUrl())
            int countObjectsForProv = 0
            if ((jsonFacets != null)&&(jsonFacets.facetValues != null)&&(jsonFacets.facetValues.count != null)&&(jsonFacets.facetValues.count[0] != null)) {
                try {
                    countObjectsForProv = jsonFacets.facetValues.count[0].intValue()
                }
                catch (NumberFormatException ex) {
                    countObjectsForProv = -1
                }
            }
            render(template: "ajaxDetails", model: [itemId: itemId, selectedItemId: id, selectedOrgXML: selectedOrgXML, subOrg: jsonOrgSubHierarchy, parentOrg: jsonOrgParentHierarchy, countObjcs: countObjectsForProv, vApiInst: vApiInstitution])
        }
        else {
            forward controller: 'error', action: "notfound"
        }
    }
}
