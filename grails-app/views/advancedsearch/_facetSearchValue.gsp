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
  <g:if test="${group.equals(0)}">
    <input class="value" type="text" id="value-${group}-${row}" name="value-${group}-${row}" 
      title="" data-content="<g:message code="apd.Contextual_Help_${group}_${row}"/>" data-original-title="some text"/>
  </g:if>
  <g:else>
    <input class="value" type="text" id="value-${group}-${row}" name="value-${group}-${row}" 
      title="" data-content="<g:message code="apd.Contextual_Help"/>" data-original-title="some text"/>
  </g:else>
</div><!-- /end of .span5 -->
