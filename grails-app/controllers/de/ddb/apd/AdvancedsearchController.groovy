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

class AdvancedsearchController {

    private static final String enumSearchType = "ENUM"
    private static final String textSearchType = "TEXT"
    // TODO: copy the message properties from DDB NEXT.
    private static final String languageTagPrefix = "apd.facet_"
    private static final String facetNameSuffix = "_fct"
    private static final String labelSortType = "ALPHA_LABEL"
    private static final String MESSAGE_PROPERTY_PREFIX = "apd."

    static defaultAction = "fillValues"

    def messageSource

    def index() {
        /* TODO: These don't work, if we use external properties file. When we
         * use external properties the values are Strings *not* Integer.
         */
        def searchGroupCount = grailsApplication.config.apd.advancedSearch.searchGroupCount.toInteger()
        def searchFieldCount = grailsApplication.config.apd.advancedSearch.searchFieldCount.toInteger()

        String url = grailsApplication.config.apd.backend.url
        /* We use facetSearchFiels to fill the facet selection.
         * Example of facetSearchFiels = [ 'search_all', 'title', 'place' ]
         * */
        List facetSearchfields = new FacetsService(url:url).getExtendedFacets()

        Map facetValuesMap = getFacetValues(facetSearchfields)

        render(view: "advancedsearch",
        model: [
            searchGroupCount: searchGroupCount,
            searchFieldCount: searchFieldCount,
            facetSearchfields: facetSearchfields,
            facetValuesMap : facetValuesMap,
            textSearchType : textSearchType,
            languageTagPrefix : languageTagPrefix,
            facetNameSuffix : facetNameSuffix,
            labelSortType : labelSortType,
            enumSearchType : enumSearchType
        ])
    }

    /**
     * Take form-parameters from advanced-search-form, generate query and call search with query.
     *
     * @throws IOException
     */
    def executeSearch() throws IOException {
        // TODO: research how to reduce the code duplication.
        def searchGroupCount = grailsApplication.config.apd.advancedSearch.searchGroupCount.toInteger()
        def searchFieldCount = grailsApplication.config.apd.advancedSearch.searchFieldCount.toInteger()
        def offset = grailsApplication.config.apd.advancedSearch.defaultOffset.toInteger()
        def rows = grailsApplication.config.apd.advancedSearch.defaultRows.toInteger()
        def url = grailsApplication.config.apd.backend.url

        def facetSearchfields = new FacetsService(url:url).getExtendedFacets()

        AdvancedSearchFormToQueryConverter converter =
                new AdvancedSearchFormToQueryConverter(params, searchGroupCount, searchFieldCount, facetSearchfields)
        log.info 'params: ' + params
        String query = converter.convertFormParameters()
        log.info 'query: ' + query
        redirect(uri: "/liste?query=" + query + "&offset=" + offset + "&rows=" + rows)
    }

    /**
     * request facet-values (for select-box) for all facets that are searchable.
     * fill results in global variable facetValuesMap (key: name of facet, value: map with value, display-value, sorted)
     *
     */
    private Map getFacetValues(facetSearchfields) {
        def facetValuesMap = [:]
        def url = grailsApplication.config.apd.backend.url

        // TODO: ask @mih for the value
        // [{facetName=language_fct, filter=term:unknown}, {facetName=language_fct, filter=term:termunknown}]
        // def allFacetFilters = grailsApplication.config.apd.backend.facets.filter
        def allFacetFilters =  [
            [
                'facetName': 'language_fct',
                'filter': 'term:unknown'
            ],
            [
                'facetName': 'language_fct',
                'filter': 'term:termunknown'
            ]
        ]
        def facetsRequester = new FacetsService(url:url)
        for ( facetSearchfield in facetSearchfields ) {
            if (facetSearchfield.searchType.equals(enumSearchType)) {
                // this is okay
                def facetValues = facetsRequester.getFacet(facetSearchfield.name + facetNameSuffix, allFacetFilters)
                def facetDisplayValuesMap = new TreeMap()
                for (facetValue in facetValues) {
                    //translate because of sorting
                    facetDisplayValuesMap[facetValue] =
                            getMessage(MESSAGE_PROPERTY_PREFIX + facetSearchfield.name +
                            facetNameSuffix + "_" + facetValue)
                }
                if (facetSearchfield.sortType != null && facetSearchfield.sortType.equals(labelSortType)) {
                    facetDisplayValuesMap = facetDisplayValuesMap.sort {it.value}
                }
                else {
                    //workaround for time_fct, sort desc by id
                    if (facetSearchfield.name == "time") {
                        facetDisplayValuesMap = facetDisplayValuesMap.sort {a, b -> b.key <=> a.key}
                    }
                    else {
                        facetDisplayValuesMap = facetDisplayValuesMap.sort {it.key}
                    }
                }

                facetValuesMap[facetSearchfield.name + facetNameSuffix] = facetDisplayValuesMap
            }
        }
        return facetValuesMap
    }

    /**
     * get display-value language-dependent.
     *
     * @param name fieldname
     * @return String translated display-value
     */
    private String getMessage(name) {
        return messageSource.getMessage(name,null, request.getLocale())
    }
}
