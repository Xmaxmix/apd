<div class="span5">
  <g:each var="facetSearchfield" in="${facetSearchfields}">
    <g:if test="${facetSearchfield.searchType?.equals(enumSearchType)}">
      <select class="facet-values"
        id="${facetSearchfield.name}${facetNameSuffix}-${group}-${row}"
        name="${facetSearchfield.name}${facetNameSuffix}-${group}-${row}"
        style="display: none">
        <option value="">
          <g:message code="apd.AdvancedSearch_PleaseSelect" />
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
</div><!-- /end of .span5 -->
