<div class="row search-group">
  <div class="span12">
    <div class="row">
      <div class="span11 operator group-operator">
        <label for="operator-group-${group}"><g:message code="apd.AdvancedSearch_Operator_MatchLabel"/></label>
        <select id="operator-group-${group}" name="operator-group-${group}">
          <option value="OR"><g:message code="apd.AdvancedSearchGroupOperator_AnyTerms"/></option>
          <option value="AND"><g:message code="apd.AdvancedSearchGroupOperator_AllTerms"/></option>
        </select>
      </div>
      <div class="span1 button-group">
        <button type="button" class="remove-group-button fr" style="display:none" title="<g:message code="apd.AdvancedSearch_RemoveGroupButton_Title"/>"></button>
      </div>
    </div>
    <div class="row bt">
      <div class="span12 search-field-group">
        <g:set var="row" value="${0}"/>
        <g:while test="${row < searchFieldCount}">
          <div class="row search-field-row">
            <g:render template="advancedsearchrow" />
          </div>
          <%row++%>
        </g:while>
      </div>
    </div>
  </div>
</div>
