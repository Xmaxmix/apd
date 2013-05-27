<html>
  <head>
    <title><g:message code="apd.Structure"/> - <g:message code="apd.ArchivportalD"/></title>
    
    <meta name="page" content="structureview" />
    <meta name="layout" content="main" />
    
    <r:require module="structureview"/>
    <g:javascript library="jquery" />
    
  </head>
  <body>
    <div class="row">
      <div class="span12 search-widget-container">
        <g:form id="home-search-form" url="[controller:'liste', action:'']"
          class="form-search" method="GET">
          <div class="input-append">
            <input type="text" id="query" name="query"
              class="input-xlarge search-query"
              placeholder="<g:message code="apd.Search_Placeholder"/>" value="${query}">
            <button type="submit" class="btn">
              <i class="icon-search"></i>
            </button>
          </div>
        </g:form>
        <div>
          <g:searchWidgetRender></g:searchWidgetRender>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="span4">
        <div class="row">
          <div class="span4">
            <%-- 
            <g:link controller="structureview">
              <div class="selector active"><g:message code="apd.Structure"/></div>
            </g:link>
            --%>
            <g:apdLink controller="structureview" action="index" params="${params}">
              <div class="selector active"><g:message code="apd.Structure"/></div>
            </g:apdLink>
            <%-- 
            <g:link controller="objectview">
              <div class="selector"><g:message code="apd.Object"/></div>
            </g:link>
            --%>
            <g:apdLink controller="objectview" action="index" params="${params}">
              <div class="selector"><g:message code="apd.Object"/></div>
            </g:apdLink>
          </div>
        </div>
        <div class="row">
          <div class="span4">

            <div id="institution-tree">
            </div>

          </div>
        </div>
      </div>
      <div class="span8">
        <div class="institution-item-details">
          <h1><g:message code="apd.Please_select_an_archive"/></h1>
        </div>
      </div>
    </div>
  </body>
</html>
