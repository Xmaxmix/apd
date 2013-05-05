<div class="results-paginator-options">
  <div class="page-filter">
    <label>
      <g:message code="apd.SearchResultsPagination_Display" />
      <select id="result-per-page" class="select input-mini">

       <!-- TODO: get the option values from the server. -->
       <option value="20" selected>20</option>
       <option value="40">40</option>
       <option value="60">60</option>
       <option value="80">80</option>
       <option value="100">100</option>
      </select>
    </label>
  </div>
  <div class="sort-results-switch">
    <label><g:message code="apd.SearchResultsPagination_Sort_By" />
      <select id="result-sort-by" class="select input-medium">

        <!-- TODO: get the selected options from the server. -->
        <option value="RELEVANCE"><g:message code="apd.Sort_RELEVANCE" /></option>
        <option value="ALPHA_ASC"><g:message code="apd.Sort_ALPHA_ASC" /></option>
        <option value="ALPHA_DESC"><g:message code="apd.Sort_ALPHA_DESC" /></option>
      </select>
    </label>
  </div>
</div>