<div class="row search-group">
  <div class="span12">
    <div class="row">
      <g:render template="groupOperator" />
      <g:render template="removeGroup" />
    </div>
    <div class="row bt">
      <div class="span12 search-field-group">
        <!-- TODO refactor the use of while, set and row++. Replace them with GSP tag -->
        <!-- 
        We show a number of rows by default. We read it from the application config. It defaults to 5
         -->
        <g:set var="row" value="${ 0 }"/>
        <g:while test="${row < searchFieldCount}">
          <div class="row search-field-row">
            <g:render template="advancedSearchRow" />
          </div>
          <% row++ %>
        </g:while>
      </div>
    </div>
  </div>
</div>
