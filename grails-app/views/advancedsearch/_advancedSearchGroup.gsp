<div class="row search-group">
  <div class="span12">
    <div class="row">
      <g:render template="groupOperator" />
      <g:render template="removeGroup" />
    </div>
    <div class="row bt">
      <div class="span12 search-field-group">

         <%--
        <g:each var="row" in="${ (0..<searchFieldCount) }">
          <div class="row search-field-row">
            <g:render template="advancedSearchRow" model="['group':group, 'row':row]" />
          </div>
        </g:each>
         --%>

        <!-- TODO: consider refactor using this code. We should remove
          Groovy code if possible. -->
        <g:set var="row" value="${0}"/>
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
