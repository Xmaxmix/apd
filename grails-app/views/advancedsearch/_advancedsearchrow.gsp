<div class="span11">
  <div class="row">
    <div class="span3">
      <select class="facet facet-js" id="facet-js-${group}-${row}" name="facet-${group}-${row}" style="display: none" disabled="disabled">
        <g:each in="${facetSearchfields}">
          <g:if test="${it.searchType?.equals(enumSearchType)}">
            <option value="${it.name}"
              data-inputid="${it.name}${facetNameSuffix}-${group}-${row}">
              <g:message code="${languageTagPrefix}${it.name}" />
            </option>
          </g:if>
          <g:else>
            <option value="${it.name}" data-inputid="value-${group}-${row}">
              <g:message code="${languageTagPrefix}${it.name}" />
            </option>
          </g:else>
        </g:each>
      </select> <select class="facet facet-simple" name="facet-${group}-${row}">
        <g:each in="${facetSearchfields}">
          <g:if test="${it.searchType?.equals(textSearchType)}">
            <option value="${it.name}">
              <g:message code="${languageTagPrefix}${it.name}" />
            </option>
          </g:if>
        </g:each>
      </select>
    </div>

    <div class="span5">
      <g:each var="facetSearchfield" in="${facetSearchfields}">
        <g:if test="${facetSearchfield.searchType?.equals(enumSearchType)}">
          <select class="facet-values"
            id="${facetSearchfield.name}${facetNameSuffix}-${group}-${row}"
            name="${facetSearchfield.name}${facetNameSuffix}-${group}-${row}"
            style="display: none">
            <option value="">
              <g:message code="ddbnext.AdvancedSearch_PleaseSelect" />
            </option>
            <g:set var="key" value="${facetSearchfield.name}${facetNameSuffix}" />
            <g:each var="facetValue" in="${facetValuesMap[key]}">
              <option value="${facetValue.key}">
                ${facetValue.value}
              </option>
            </g:each>
          </select>
        </g:if>
      </g:each>

      <input class="value" type="text" id="value-${group}-${row}"
        name="value-${group}-${row}" />
    </div>

    <div class="span3">
      <select class="match" name="match-${group}-${row}">
        <option value="ANY">
          <g:message code="ddbnext.AdvancedSearch_Any" />
        </option>
        <option value="ALL">
          <g:message code="ddbnext.AdvancedSearch_All" />
        </option>
        <option value="EXACT">
          <g:message code="ddbnext.AdvancedSearch_Exact" />
        </option>
      </select>
    </div>
  </div>
</div>
<div class="span1 row-button-group">
  <button type="button" class="remove-button" style="display: none" title="<g:message code="ddbnext.AdvancedSearch_RemoveRowButton_Title"/>"></button>
  <button type="button" class="add-button" style="display: none" title="<g:message code="ddbnext.AdvancedSearch_AddRowButton_Title"/>"></button>
</div>
<div class="clearfix"></div>
