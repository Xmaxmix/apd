<div class="row">
  <div class="span12">
    <g:form method="post" id="advanced-search-form" url="[controller:'advancedsearch', action:'executeSearch']" >
    <div class="row">
      <div class="span12">
        <g:render template="globalOperator" />

        <!-- TODO this is the first search group with preselected facets -->
        <%-- TODO: clever tricks do not work.
        <g:each var="group" in="${ (0..<searchGroupCount) }">
          <g:render template="advancedSearchGroup" model="['group':group]" />
        </g:each>
        --%>
        <g:set var="group" value="${0}"/>
        <g:while test="${group < searchGroupCount}">
          <g:render template="advancedSearchGroup" />
          <%group++%>
        </g:while>
      <g:render template="groupButton" />
      <g:render template="buttons" />
      </div>
    </div>
    </g:form>
  </div>
</div>
