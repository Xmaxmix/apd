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
        <g:apdLink id="home-search-form" controller="liste" params="${params}" class="form-search" method="GET">
          <div class="input-append">
            <input type="text" id="query" name="query"
              class="input-xlarge search-query"
              placeholder="<g:message code="apd.Search_Placeholder"/>" value="${query}">
            <button type="submit" class="btn">
              <i class="icon-search"></i>
            </button>
          </div>
        </g:apdLink>
        <div>
          <g:searchWidgetRender></g:searchWidgetRender>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="span4">
        <div class="row">
          <div class="span4">
            <g:apdLink controller="structureview" params="${params}">
              <div class="selector active"><g:message code="apd.Structure"/></div>
            </g:apdLink>
            <g:apdLink controller="objectview" params="${params}">
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
