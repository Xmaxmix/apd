<div class="row search-group">
  <div class="span12">
    <div class="row">
      <g:render template="groupOperator" />
      <g:render template="removeGroup" />
    </div>
    <div class="row bt">
      <div class="span12 search-field-group">
        <!-- 
        We show a number of rows by default. We read it from the application config. It defaults to 5
         -->
        <g:each var="group" in="${ (0..<searchFieldCount) }">
          <div class="row search-field-row">
            <g:render template="advancedSearchRow" />
          </div>
        </g:each>
      </div>
    </div>
  </div>
</div>
