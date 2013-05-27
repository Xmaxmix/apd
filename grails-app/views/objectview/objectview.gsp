<html>
  <head>
    <title><g:message code="apd.Object"/> - <g:message code="apd.ArchivportalD"/></title>

    <meta name="page" content="objectview" />
    <meta name="layout" content="main" />

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
            <g:link controller="struktur">
              <div class="selector"><g:message code="apd.Structure"/></div>
            </g:link>
            --%>
            <g:apdLink controller="struktur" action="index" params="${params}">
              <div class="selector"><g:message code="apd.Structure"/></div>
            </g:apdLink>
            <%-- 
            <g:link controller="liste">
              <div class="selector active"><g:message code="apd.Object"/></div>
            </g:link>
            --%>
            <g:apdLink controller="liste" action="index" params="${params}">
              <div class="selector active"><g:message code="apd.Object"/></div>
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
      <div class="span8 search-noresults-content off">
        <g:render template="noResults" />
      </div>
      <div class="span8 search-results-content">
        <div class="off result-count"></div>
        <g:render template="pagination" />
        <g:render template="navigation" />
        <div class="list-container">
        </div>
      </div>
    </div>
  </body>
</html>