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

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET

/**
 * Get faceted search fields and values for facet from the Backend.
 *
 * @author mih
 * @author chh
 */
public class FacetsService {

    private static final def UNNEEDED_FACETS = ['time', 'sector', 'provider']
    //The Backend URL
    String url

    /**
     * Get values for a facet.
     *
     * @param facetName The name of the facet
     * @param allFacetFilters List of all available facet filter mappings
     * @return List of Facet-Values
     */
    public List getFacet(facetName, allFacetFilters) throws IOException {
        def filtersForFacetName = getFiltersForFacetName(facetName, allFacetFilters)
        def res = []
        int i = 0
        log.info "Facet name: ${facetName}"

        def responseWrapper = ApiConsumer.getJson(url ,'/search/facets/' + facetName)
        if(responseWrapper.isOk()) {
            def json = responseWrapper.getResponse()
            json.facetValues.each{
                if(filtersForFacetName.isEmpty() || !filtersForFacetName.contains(it.value)){
                    res[i] = it.value
                    i++
                }
            }
            return res
        }
    }

    /**
     * Get the list of Facets for advanced search. We will use them for the advanced's search fields.
     *
     * @return List of Arrays.<br>
     *     Array contains name, searchType(TEXT or ENUM), sortType(ALPHA_ID, ALPHA_LABEL).<br>
     *     name: name of facet(Searchfield).<br>
     *     searchType TEXT: display textfield for searchstring.<br>
     *     searchType ENUM: display selectbox with possible values for searchstring.<br>
     *     sortType (for searchType = ENUM): how to sort possible values.
     *     ALPHA_ID: sort by id.<br>
     *     ALPHA_LABEL: sort by value.<br>
     *
     *     The JSON has following format:
     *     JSON = [
     *         {
     *           "name": "search_all",
     *           "paths": [],
     *           "searchType": "TEXT",
     *           "sortType": null,
     *           "displayType": "EXTENDED",
     *           "position": 1
     *         },
     *         {
     *           "name": "title",
     *           "paths": [],
     *           "searchType": "TEXT",
     *           "sortType": null,
     *           "displayType": "EXTENDED",
     *           "position": 2
     *         }
     *        ]
     */
    public List getExtendedFacets() throws IOException {
        def facetSearchFields = []
            def responseWrapper = ApiConsumer.getJson(url ,'/search/facets/', [type:'EXTENDED'])

            if(!responseWrapper.isOk()){
                log.error "findAll(): Server returned no results "
                throw responseWrapper.getException()
            }

        def json  = responseWrapper.response

        /* we don't want to include all facets in APD. We don't need following facets:
         * - time,
         * - sector,
         * - provider
         * */
        json.each{
            // TODO: extract to constant
            if(UNNEEDED_FACETS.contains(it.name)) {
                log.debug('ignore facet: ' + it.name)
            } else {
                def part = [:]
                part["name"] = it.name
                part["searchType"] = it.searchType
                part["sortType"] = it.sortType

                // TODO: do we really need the order?
                // facetSearchFields[it.position - 1] = part
                facetSearchFields << part
            }
        }

         extend(facetSearchFields)
    }

    /*
     * Because the back-end hasn't support other facets yet, we manually extend them. We need to replace it once
     * the back-end support them.
     *
     */
    def extend(facetSearchFields) {
        def MISSING_FACET_NAMES = ['signature', 'archieve_type', 'material', 'itemization_grade']
        def extendedFacets = MISSING_FACET_NAMES.inject(facetSearchFields) { initialList, facetName ->
           initialList + [name: facetName, searchType: 'TEXT', sortType: null]
        }
        extendedFacets
    }


    /**
     * Takes a list of configured facet filter mapping and returns only the filter values for the matching facet name.
     * E.g.: facetName=facet1, allFacetsFilters=[{facetName:facet1, filter:filter1}, {facetName:facet2, filter:filter2}]
     * The returned list would be [filter1]
     * @param facetName The name of the facet
     * @param allFacetFilters List of mappings containing all available facet filter mappings
     * @return A list of filters for the matching facet name
     */
    private List getFiltersForFacetName(facetName, allFacetFilters){
        def filtersForFacetName = []
        for(filter in allFacetFilters) {
            if(filter.facetName != null && filter.facetName.equals(facetName)){
                filtersForFacetName.add(filter.filter)
            }
        }
        return filtersForFacetName
    }
}
