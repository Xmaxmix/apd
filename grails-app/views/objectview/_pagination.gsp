<div class="results-paginator-options">
  <div class="page-filter">
    <label>
      <g:message code="apd.SearchResultsPagination_Display" />
      <select id="result-per-page" class="select input-mini">
       <!-- TODO: get the option values from the server. -->
       <option selected>20</option>
       <option>40</option>
       <option>60</option>
       <option>80</option>
       <option>100</option>
      </select>
    </label>
  </div>
  <div class="sort-results-switch">
    <label><g:message code="apd.SearchResultsPagination_Sort_By" />
      <select class="select input-medium">
        <!-- TODO: get the selected options from the server. -->
        <option value="RELEVANCE"><g:message code="apd.Sort_RELEVANCE" /></option>
        <option value="ALPHA_ASC"><g:message code="apd.Sort_ALPHA_ASC" /></option>
        <option value="ALPHA_DESC"><g:message code="apd.Sort_ALPHA_DESC" /></option>
      </select>
    </label>
  </div>
</div>