<html>
<head>
  <title><%-- <g:message code="apd.Homepage"/> - --%><g:message code="apd.ArchivportalD"/></title>

  <meta name="page" content="advancedsearch" />
  <meta name="layout" content="main" />

</head>
<body>
  <div id="advanced-search" class="row">
    <div class="span12">
      <div class="row heading bb">
        <div class="span6">
          <div class="fl">
            <h1><g:message code="apd.AdvancedSearch"/></h1>
          </div>
          <span class="contextual-help fl hidden-phone hidden-tablet"
            title="<g:message code="apd.AdvancedSearch_Hint" default="apd.AdvancedSearch_Hint"/>">
          </span>
          <div class="tooltip off"></div>
        </div>
      </div>
      <div class="row">
        <div class="span12">
          <g:form method="post" id="advanced-search-form" url="[controller:'advancedsearch', action:'executeSearch']" >
          <div class="row">
            <div class="span12">
              <div class="row operator global-operator">
                <div class="span12">
                  <label for="operator" ><g:message code="apd.AdvancedSearch_AllGroupsOperator_MatchLabel"/></label>
                  <select id="operator" name="operator">
                    <option value="OR"><g:message code="apd.AdvancedSearchGlobalOperator_AnyGroups"/></option>
                    <option value="AND"><g:message code="apd.AdvancedSearchGlobalOperator_AllGroups"/></option>
                  </select>
                </div><!-- /end of .span12 -->
              </div>
              <g:set var="group" value="${0}"/>
              <g:while test="${group < searchGroupCount}">
                <g:render template="/search/advancedsearchgroup" /><%group++%>
              </g:while>
            <div class="row bb">
              <div class="span12 button-group">
                <button type="button" class="add-group-button fr" style="display: none" title="<g:message code="apd.AdvancedSearch_AddGroupButton_Title"/>">
                  <g:message code="apd.AdvancedSearch_AddGroupButton_Title"/>
                </button>
              </div>
            </div><!-- end of .bb -->
            <div class="row">
              <div class="span6 button-group fr">
                <button class="reset" type="reset"><span><g:message code="apd.Reset"/></span></button>
                <button class="submit" type="submit"><span><g:message code="apd.Search"/></span></button>
              </div>
            </div><!-- /end of .row -->
            </div>
          </div>
          </g:form>
        </div>
      </div>
    </div><!-- /end of span12 -->
  </div><!-- /end of #advanced-search -->
</body>
</html>
