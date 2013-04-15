<div class="row">
  <div class="span12">
    <g:form method="post" id="advanced-search-form" url="[controller:'advancedsearch', action:'executeSearch']" >
    <div class="row">
      <div class="span12">
        <g:render template="globalOperator" />

        <!-- TODO this is the first search group with preselected facets -->
        <g:render template="firstSearchGroup" />
        
        <g:each var="group" in="${ (1..<searchGroupCount) }">
          <g:render template="advancedSearchGroup" />
        </g:each>
        
      <g:render template="groupButton" />
      <g:render template="buttons" />
      </div>
    </div>
    </g:form>
  </div>
</div>

