<div class="row">
  <div class="span12">
    <g:apdForm controller="advancedsearch" action="executeSearch" params="${params}" id="advanced-search-form" method="POST">
      <div class="row">
        <div class="span12">
          <g:render template="globalOperator" />
  
          <!-- This is the first search group with preselected facets -->
          <g:render template="firstSearchGroup" />
  
          <%--
          TODO: clever trick does not work.
          <g:each var="group" in="${ (0..<searchGroupCount) }">
            <g:render template="advancedSearchGroup" model="['group':group]" />
          </g:each>
          --%>
  
          <g:set var="group" value="${1}"/>
          <g:while test="${group < searchGroupCount}">
            <g:render template="advancedSearchGroup" />
            <% group++ %>
          </g:while>
        <g:render template="groupButton" />
        <label class="checkbox">
          <input type="checkbox" name="isMediaOnly" value="true"><g:message code="apd.only_with_media" />
        </label>
        <g:render template="buttons" />
        </div>
      </div>
    </g:apdForm>
  </div>
</div>
