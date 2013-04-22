<div class="span3">

  <!-- The JS version -->
  <!-- TODO: remove inline style -->
  <select class="facet facet-js" id="facet-js-${group}-${row}" name="facet-${group}-${row}"
    style="display: none" disabled="disabled">
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
  </select>

  <!-- TODO: We don't need the non-JS version, remove it -->
  <!-- The non-JS version -->
  <select class="facet facet-simple" name="facet-${group}-${row}">
    <g:each in="${facetSearchfields}">
      <g:if test="${it.searchType?.equals(textSearchType)}">
        <option value="${it.name}">
          <g:message code="${languageTagPrefix}${it.name}" />
        </option>
      </g:if>
    </g:each>
  </select>

</div><!-- /end of .span3 -->