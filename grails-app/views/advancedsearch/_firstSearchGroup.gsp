<div class="row search-group">
  <div class="span12">
    <div class="row">
      <g:render template="groupOperator" model="['group':0]" />
      <g:render template="removeGroup" />
    </div>
    <div class="row bt">
      <div class="span12 search-field-group">
      
        <!-- TODO: how can we inject the selected option to the template? -->
        <!-- for the first row, we select the value `title` as default. -->
        <div class="row search-field-row">
          <div class="span11">
            <div class="row">
              <div class="span3">
              
                <!-- The JS version -->
                <select class="facet facet-js" id="facet-js-0-0" name="facet-0-0" style="display: none" disabled="disabled">
                  <g:each in="${facetSearchfields}">
                    <g:if test="${it.searchType?.equals(enumSearchType)}">
                      <option value="${it.name}"
                        data-inputid="${it.name}${facetNameSuffix}-0-0">
                        <g:message code="${languageTagPrefix}${it.name}" />
                      </option>
                    </g:if>
                    <g:else>
                      <option value="${it.name}" data-inputid="value-0-0">
                        <g:message code="${languageTagPrefix}${it.name}" />
                      </option>
                    </g:else>
                  </g:each>
                </select>
                
                <!-- The non-JS version -->
                <select class="facet facet-simple" name="facet-0-0">
                  <g:each in="${facetSearchfields}">
                    <!-- We set the default value here. -->
                    <g:if test="${ it.name == 'title' && it.searchType == textSearchType }">
                      <option value="${it.name}" selected>
                        <g:message code="${languageTagPrefix}${it.name}" />
                      </option>
                    </g:if>
                    <g:elseif test="${it.searchType == textSearchType}">
                      <option value="${it.name}">
                        <g:message code="${languageTagPrefix}${it.name}" />
                      </option>
                    </g:elseif>
                  </g:each>
                </select>
              
              </div><!-- /end of .span3 -->
              <g:render template="facetSearchValue" model="['group':0,'row':0]" />
              <g:render template="matchesSelection" model="['group':0,'row':0]" />
            </div><!-- /end of .row -->
          </div><!-- /end of .span11 -->
          
          <g:render template="addAndRemoveSearchRow" />
          
          <div class="clearfix"></div>
        </div><!-- /end of .search-field-row -->

        <!-- for the second row, we select the value `signature` as default. -->
        <div class="row search-field-row">
          <div class="span11">
            <div class="row">
              <div class="span3">
              
                <!-- The JS version -->
                <select class="facet facet-js" id="facet-js-0-1" name="facet-0-1" style="display: none" disabled="disabled">
                  <g:each in="${facetSearchfields}">
                    <g:if test="${it.searchType?.equals(enumSearchType)}">
                      <option value="${it.name}"
                        data-inputid="${it.name}${facetNameSuffix}-0-1">
                        <g:message code="${languageTagPrefix}${it.name}" />
                      </option>
                    </g:if>
                    <g:else>
                      <option value="${it.name}" data-inputid="value-0-1">
                        <g:message code="${languageTagPrefix}${it.name}" />
                      </option>
                    </g:else>
                  </g:each>
                </select>
                
                <!-- The non-JS version -->
                <select class="facet facet-simple" name="facet-0-1">
                  <g:each in="${facetSearchfields}">
                    <!-- We set the default value here. -->
                    <!-- TODO: We don't have a facet with value signature, yet. -->
                    <g:if test="${ it.name == 'signature' && it.searchType == textSearchType }">
                      <option value="${it.name}" selected>
                        <g:message code="${languageTagPrefix}${it.name}" />
                      </option>
                    </g:if>
                    <g:if test="${it.searchType == textSearchType}">
                      <option value="${it.name}">
                        <g:message code="${languageTagPrefix}${it.name}" />
                      </option>
                    </g:if>
                  </g:each>
                </select>
              
              </div><!-- /end of .span3 -->
              <g:render template="facetSearchValue" model="['group':0,'row':1]" />
              <g:render template="matchesSelection" model="['group':0,'row':1]" />
            </div><!-- /end of .row -->
          </div><!-- /end of .span11 -->
          
          <g:render template="addAndRemoveSearchRow" />
          
          <div class="clearfix"></div>
        </div><!-- /end of .search-field-row -->

        <!-- For the search field `Enthaelt` -->
        <div class="row search-field-row">
          <div class="span11">
            <div class="row">
              <div class="span3">
              
                <!-- The JS version -->
                <select class="facet facet-js" id="facet-js-0-2" name="facet-0-2" style="display: none" disabled="disabled">
                  <g:each in="${facetSearchfields}">
                    <g:if test="${it.searchType?.equals(enumSearchType)}">
                      <option value="${it.name}"
                        data-inputid="${it.name}${facetNameSuffix}-0-2">
                        <g:message code="${languageTagPrefix}${it.name}" />
                      </option>
                    </g:if>
                    <g:else>
                      <option value="${it.name}" data-inputid="value-0-2">
                        <g:message code="${languageTagPrefix}${it.name}" />
                      </option>
                    </g:else>
                  </g:each>
                </select>
                
                <!-- The non-JS version -->
                <select class="facet facet-simple" name="facet-0-2">
                  <g:each in="${facetSearchfields}">
                    <!-- 
                    For the `enthaelt` search field, we look in `description`,i.e. 
                    `enthaelt` is equals `description`
                     -->
                    <g:if test="${ it.name == 'description' && it.searchType == textSearchType }">
                      <option value="${ it.name }" selected>
                        <g:message code="${ languageTagPrefix }${ it.name }" />
                      </option>
                    </g:if>
                    <g:elseif test="${it.searchType?.equals(textSearchType)}">
                      <option value="${it.name}">
                        <g:message code="${languageTagPrefix}${it.name}" />
                      </option>
                    </g:elseif>
                  </g:each>
                </select>
              
              </div><!-- /end of .span3 -->
              <g:render template="facetSearchValue" model="['group':0,'row':2]" />
              <g:render template="matchesSelection" model="['group':0,'row':2]" />
            </div><!-- /end of .row -->
          </div><!-- /end of .span11 -->
          
          <g:render template="addAndRemoveSearchRow" />
          
          <div class="clearfix"></div>
        </div><!-- /end of .search-field-row -->
        
        <!-- For the search field `Arhivalientyp` -->
        <div class="row search-field-row">
          <div class="span11">
            <div class="row">
              <div class="span3">
              
                <!-- The JS version -->
                <select class="facet facet-js" id="facet-js-0-3" name="facet-0-3" style="display: none" disabled="disabled">
                  <g:each in="${facetSearchfields}">
                    <g:if test="${it.searchType?.equals(enumSearchType)}">
                      <option value="${it.name}"
                        data-inputid="${it.name}${facetNameSuffix}-0-3">
                        <g:message code="${languageTagPrefix}${it.name}" />
                      </option>
                    </g:if>
                    <g:else>
                      <option value="${it.name}" data-inputid="value-0-3">
                        <g:message code="${languageTagPrefix}${it.name}" />
                      </option>
                    </g:else>
                  </g:each>
                </select>
                
                <!-- The non-JS version -->
                <select class="facet facet-simple" name="facet-0-3">
                  <g:each in="${facetSearchfields}">
                  <div>${ it.name }</div>
                    <!-- We set the default value here. -->
                    <g:if test="${ it.name == 'archievetype' && it.searchType == textSearchType }">
                      <option value="${it.name}" selected>
                        <g:message code="${languageTagPrefix}${it.name}" />
                      </option>
                    </g:if>
                    <g:if test="${it.searchType == textSearchType}">
                      <option value="${it.name}">
                        <g:message code="${languageTagPrefix}${it.name}" />
                      </option>
                    </g:if>
                  </g:each>
                </select>
              
              </div><!-- /end of .span3 -->
              <g:render template="facetSearchValue" model="['group':0,'row':3]" />
              <g:render template="matchesSelection" model="['group':0,'row':3]" />
            </div><!-- /end of .row -->
          </div><!-- /end of .span11 -->
          
          <g:render template="addAndRemoveSearchRow" />
          
          <div class="clearfix"></div>
        </div><!-- /end of .search-field-row -->

        <!-- For the search field `Laufzeit` -->
        <div class="row search-field-row">
          <div class="span11">
            <div class="row">
              <div class="span3">
              
                <!-- The JS version -->
                <select class="facet facet-js" id="facet-js-0-4" name="facet-0-4" style="display: none" disabled="disabled">
                  <g:each in="${facetSearchfields}">
                    <g:if test="${it.searchType?.equals(enumSearchType)}">
                      <option value="${it.name}"
                        data-inputid="${it.name}${facetNameSuffix}-0-4">
                        <g:message code="${languageTagPrefix}${it.name}" />
                      </option>
                    </g:if>
                    <g:else>
                      <option value="${it.name}" data-inputid="value-0-4">
                        <g:message code="${languageTagPrefix}${it.name}" />
                      </option>
                    </g:else>
                  </g:each>
                </select>
                
                <!-- The non-JS version -->
                <select class="facet facet-simple" name="facet-0-4">
                  <g:each in="${facetSearchfields}">
                    <!-- We set the default value here. -->
                    <g:if test="${ it.name == 'date' && it.searchType == textSearchType }">
                      <option value="${it.name}" selected>
                        <g:message code="${languageTagPrefix}${it.name}" />
                      </option>
                    </g:if>
                    <g:if test="${it.searchType?.equals(textSearchType)}">
                      <option value="${it.name}">
                        <g:message code="${languageTagPrefix}${it.name}" />
                      </option>
                    </g:if>
                  </g:each>
                </select>
              
              </div><!-- /end of .span3 -->
                <g:render template="dateFacetSearchValue" model="['group':0,'row':4]" />
              <g:render template="matchesSelection" model="['group':0,'row':4]" />
            </div><!-- /end of .row -->
          </div><!-- /end of .span11 -->
          
          <g:render template="addAndRemoveSearchRow" />
          
          <div class="clearfix"></div>
        </div><!-- /end of .search-field-row -->

      </div>
    </div>
  </div>
</div>
