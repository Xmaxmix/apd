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
 * Get facetted searchfields and values for facet from Backend.
 *
 * @author mih
 *
 */
public class FacetsService {
    //Backend URL
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
        def json = ApiConsumer.getTextAsJson(url ,'/search/facets/' + facetName, null)
        json.facetValues.each{
            if(filtersForFacetName.isEmpty() || !filtersForFacetName.contains(it.value)){
                res[i] = it.value
                i++
            }
        }
        return res
    }

    /**
     * Get List of Searchfields(Facets) for advanced search.
     *
     * @return List of Arrays.<br>
     *     Array contains name, searchType(TEXT or ENUM), sortType(ALPHA_ID, ALPHA_LABEL).<br>
     *     name: name of facet(Searchfield).<br>
     *     searchType TEXT: display textfield for searchstring.<br>
     *     searchType ENUM: display selectbox with possible values for searchstring.<br>
     *     sortType (for searchType = ENUM): how to sort possible values.
     *     ALPHA_ID: sort by id.<br>
     *     ALPHA_LABEL: sort by value.<br>
     */
    public List getExtendedFacets() throws IOException {
           /*
         * facetSearchFields =  [
         *   {name=search_all, searchType=TEXT, sortType=null},
         *   {name=title, searchType=TEXT, sortType=null},
         *   {name=description, searchType=TEXT, sortType=null},
         *   {name=time, searchType=ENUM, sortType=ALPHA_ID},
         *   {name=place, searchType=TEXT, sortType=null},
         *   {name=affiliate, searchType=TEXT, sortType=null},
         *   {name=keywords, searchType=TEXT, sortType=null},
         *   {name=language, searchType=ENUM, sortType=ALPHA_LABEL},
         *   {name=type, searchType=ENUM, sortType=ALPHA_LABEL},
         *   {name=sector, searchType=ENUM, sortType=ALPHA_LABEL},
         *   {name=provider, searchType=TEXT, sortType=null}
         * ]
         * */
        /*
         *
         *  JSON = [
              {
                "name": "search_all",
                "paths": [],
                "searchType": "TEXT",
                "sortType": null,
                "displayType": "EXTENDED",
                "position": 1
              },
              {
                "name": "title",
                "paths": [],
                "searchType": "TEXT",
                "sortType": null,
                "displayType": "EXTENDED",
                "position": 2
              },
              {
                "name": "description",
                "paths": [],
                "searchType": "TEXT",
                "sortType": null,
                "displayType": "EXTENDED",
                "position": 3
              },
              {
                "name": "time",
                "paths": [],
                "searchType": "ENUM",
                "sortType": "ALPHA_ID",
                "displayType": "EXTENDED",
                "position": 4
              },
              {
                "name": "place",
                "paths": [],
                "searchType": "TEXT",
                "sortType": null,
                "displayType": "EXTENDED",
                "position": 5
              },
              {
                "name": "affiliate",
                "paths": [],
                "searchType": "TEXT",
                "sortType": null,
                "displayType": "EXTENDED",
                "position": 6
              },
              {
                "name": "keywords",
                "paths": [],
                "searchType": "TEXT",
                "sortType": null,
                "displayType": "EXTENDED",
                "position": 7
              },
              {
                "name": "language",
                "paths": [],
                "searchType": "ENUM",
                "sortType": "ALPHA_LABEL",
                "displayType": "EXTENDED",
                "position": 8
              },
              {
                "name": "type",
                "paths": [],
                "searchType": "ENUM",
                "sortType": "ALPHA_LABEL",
                "displayType": "EXTENDED",
                "position": 9
              },
              {
                "name": "sector",
                "paths": [],
                "searchType": "ENUM",
                "sortType": "ALPHA_LABEL",
                "displayType": "EXTENDED",
                "position": 10
              },
              {
                "name": "provider",
                "paths": [],
                "searchType": "TEXT",
                "sortType": null,
                "displayType": "EXTENDED",
                "position": 11
              }
            ]
         */
        def facetSearchFields = []
        def json = ApiConsumer.getTextAsJson(url ,'/search/facets/', [type:'EXTENDED'])
        json.each{
            def part = [:]
            part["name"] = it.name
            part["searchType"] = it.searchType
            part["sortType"] = it.sortType
            facetSearchFields[it.position - 1] = part
        }

         extend(facetSearchFields)
    }

    /*
     * Because the back-end hasn't support other facets yet, we manually extend them. We need to replace it once
     * the back-end support them.
     *
     * TODO: date has `text` as type, shouldn't it be date? What are the possible ENUM facet values?
     */
    def extend(facetSearchFields) {
        def MISSING_FACET_NAMES = ['signature', 'archievetype', 'date']
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
