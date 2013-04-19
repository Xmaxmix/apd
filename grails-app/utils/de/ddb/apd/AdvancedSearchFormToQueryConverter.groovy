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

/**
 * Convert GET-Parameters from
 * advancedsearch-request to search-query.
 *
 * @author mih
 *
 */
public class AdvancedSearchFormToQueryConverter {
    //CharacterEncoding of query-String
    private String characterEncoding = "UTF-8"

    //Operator (AND/OR) to concatenate the groups
    private static final String GROUP_OPERATOR_NAME = "operator"

    //Operator (AND/OR) to concatenate the rows of one group
    private static final String ROW_OPERATOR_NAME = "operator-group-"

    //Name of form-field that holds the facetname to search in(searchfield)
    private String facetName = "facet-"

    //Name of form-field that holds the searchstring
    private String valueName = "value-"

    //Name of form-field that holds the match (ALL/ANY/EXACT)
    private String matchName = "match-"
    private String exactName = "EXACT"
    private String andName = "ALL"
    private String orName = "ANY"

    //Replacement-Pattern for Lucene search-query
    //mask certain signs according to Lucene-Rules
    //lucene requires to escape certain signs in searchstring:
    //+-&|!(){}[]^"~:\ has to get escaped with backslash
    private String luceneReplacementPattern = "([\\+\\-\\&\\|\\!\\(\\)\\{\\}\\[\\]\\^\\\"\\~\\:\\\\])"

    //Type in facet-result from ddb-backend that indicates that a facet
    //is searchable by an enumeration
    //(select-box instead of textfield for searchstring in searchform)
    def enumSearchType = "ENUM"

    //suffix for facetname(searchfield name) to get values for facet.
    def facetNameSuffix = "_fct"

    //GET-Parameters from request
    def parameters

    //Number of search-groups in advanced search form
    def searchGroupCount

    //Number of search-rows per group in advanced search form
    def searchFieldCount

    //names of facet-searchfields. used to get name of selectbox containing facetted values.
    def facetSearchfields

    /**
     * Constructor with number of Groups and rows per group in searchform.
     *
     * @param parameters GET-Parameters
     * @param searchGroupCount
     * @param searchFieldCount
     * @param searchfields searchfields with facet-names
     */
    public AdvancedSearchFormToQueryConverter(parameters, searchGroupCount, searchFieldCount, searchfields) {
        this.parameters = parameters
        this.searchGroupCount = searchGroupCount
        this.searchFieldCount = searchFieldCount
        this.facetSearchfields = [:]
        for ( searchfield in searchfields ) {
            if (searchfield.searchType.equals(enumSearchType)) {
                facetSearchfields[searchfield.name] = searchfield.name + facetNameSuffix
            }
        }
    }

    /**
     * Convert parameters to QueryString
     *
     * @return String QueryString
     */
    public String convertFormParameters() {
        def queryString = new StringBuilder()

        String groupOperator = parameters.get(GROUP_OPERATOR_NAME)

        // check if the parameter contains: `operator`, exit the method if empty
        if (groupOperator == null || groupOperator.isEmpty()) {
            return queryString.toString()
        }

        List<String> groupParts = []

        /*
         * Search group count is default to 3.
         * 0 <= groupId <= searchGroupCount - 1
         * TODO: what happens if the user add or remove the group?
         */
        // for each group, we create a query string.
        for (int groupId = 0; groupId < searchGroupCount; groupId++) {
            StringBuilder groupQueryString = convertGroup(groupId)
            if (groupQueryString) {
                groupParts << groupQueryString
            }
        }

        if (groupParts) {
            for (String groupPart : groupParts) {
                if (queryString) {
                    queryString.append(" ").append(groupOperator).append(" ")
                }
                // When the user selects more than one group.
                if (groupParts.size() > 1) {
                    queryString.append("(").append(groupPart).append(")")
                }
                else {
                    queryString.append(groupPart)
                }
            }
        }

        if(parameters.get('isMediaOnly')) {
            def allMediaQueryString='(type:(mediatype_001) OR type:(mediatype_002) OR type:(mediatype_3) OR type:(mediatype_5))'
            queryString << ' AND ' << allMediaQueryString
        }

        return URLEncoder.encode(queryString.toString(), characterEncoding)
    }

    /**
     * Convert group in search form to (sub)query
     *
     * @param groupId
     * @return (sub)query
     */
    private StringBuilder convertGroup(groupId) {
        def groupQueryString = new StringBuilder()
        String rowOperator = parameters.get(ROW_OPERATOR_NAME + groupId)

        if (rowOperator == null || rowOperator.isEmpty()) {
            return groupQueryString
        }

        // The number of search field is read from the properties file, default to 5?
        for (int rowId = 0; rowId < searchFieldCount; rowId++) {
            StringBuilder rowQueryString = convertRow(groupId, rowId)
            if (rowQueryString) {
                if (groupQueryString) {
                    groupQueryString.append(" ").append(rowOperator).append(" ")
                }
                groupQueryString.append(rowQueryString)
            }
        }
        return groupQueryString
    }

    /**
     * Convert row in search form to (sub)query.
     * Decide where to get the searched value from (textfield or selectbox)
     *
     * @param groupId
     * @param rowId
     * @return (sub)query
     */
    private StringBuilder convertRow(groupId, rowId) {
        def rowQuery = new StringBuilder()
        if (parameters.get(facetName + groupId + "-" + rowId) != null
        && !parameters.get(facetName + groupId + "-" + rowId).isEmpty()) {
            String searchValue = null
            //check if facet-searchfield-name is in facetSearchfields
            if (facetSearchfields.get(parameters.get(facetName + groupId + "-" + rowId)) == null
            || facetSearchfields.get(parameters.get(facetName + groupId + "-" + rowId)).isEmpty()) {
                //no facet search field, so use value-group-row as input field name
                if (parameters.get(valueName + groupId + "-" + rowId) != null
                && !parameters.get(valueName + groupId + "-" + rowId).isEmpty()) {
                    searchValue = parameters.get(valueName + groupId + "-" + rowId)
                }
            }
            else {
                //facet search field, get value from select-box
                def value = parameters.get(facetName + groupId + "-" + rowId)

                String selectboxName = facetSearchfields.get(value) + "-" + groupId + "-" + rowId
                if (parameters.get(selectboxName) != null
                && !parameters.get(selectboxName).isEmpty()) {
                    searchValue = parameters.get(selectboxName)
                }
            }
            if (searchValue != null) {
                rowQuery = getRowQuery(parameters.get(facetName + groupId + "-" + rowId), searchValue, parameters.get(matchName + groupId + "-" + rowId))
            }
        }
        return rowQuery
    }

    /**
     * Convert row in searchform to (sub)query.
     *
     * @param facet searchfield
     * @param value searchvalue
     * @param match ALL/ANY/EXACT
     * @return (sub)query
     */
    private StringBuilder getRowQuery(String facet, String value, String match) {
        StringBuilder rowQuery = new StringBuilder()
        if (facet == null || facet.isEmpty() || value == null || value.isEmpty() || match == null || match.isEmpty()) {
            return rowQuery
        }
        if (match.equalsIgnoreCase(exactName)) {
            rowQuery.append(facet).append(":(\"").append(value).append("\")")
        }
        else {
            String[] parts = value.trim().split("\\s")
            if (parts != null) {
                rowQuery.append(facet).append(":(")
                for (int i = 0; i < parts.length; i++) {
                    if (parts[i] != null && parts[i].length() > 0) {
                        parts[i] = parts[i]
                                .replaceAll(luceneReplacementPattern, '\\\\$1')
                        if (i > 0) {
                            if (match.equalsIgnoreCase(orName)) {
                                rowQuery.append(" OR ")
                            }
                            else if (match.equalsIgnoreCase(andName)) {
                                rowQuery.append(" AND ")
                            }
                            else {
                                return new StringBuilder()
                            }
                        }
                        rowQuery.append(parts[i])
                    }
                }
                rowQuery.append(")")
            }
        }
        return rowQuery
    }

}
