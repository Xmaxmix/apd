<html>
  <head>
    <title><g:message code="apd.List"/> - <g:message code="apd.ArchivportalD"/></title>
    
    <meta name="page" content="structureview" />
    <meta name="layout" content="main" />
    
  </head>
  <body>
    <div class="row">
      <div class="span12 search-widget-container border">search-widget</div>
    </div>
    <div class="row">
      <div class="span4">
        <div class="row">
          <div class="span4">
            <g:link controller="struktur">
              <div class="selector"><g:message code="apd.Structure"/></div>
            </g:link>
            <g:link controller="liste">
              <div class="selector active"><g:message code="apd.Object"/></div>
            </g:link>
          </div>
        </div>
        <div class="row">
          <div class="span4">
            <div id="institution-tree">
              <ul>
                <li rel="root" class="jstree-closed">
                  <i class="icon-root"></i>
                  <span>  ${total } <g:message code="apd.Object"/></span>
                  <ul>
                    <g:each in="${ all }">
                      <g:if test="${ it?.children}">
                        <li class="jstree-close" data-sector="${ it?.sector }" data-institution-id="${ it.id }">
                          <a href="${it.uri}" class="">
                            ${ it?.name } 
                            <span>(<g:message code="${ it?.sectorLabelKey }" />)</span>
                            <span>(${ it?.children.size() } <g:message code="apd.Object"/>)</span>
                          </a>
                          <ul>
                            <g:render template="listItem" model="['children': it?.children]" />
                          </ul>
                        </li>
                      </g:if>
                      <g:else>
                        <li class="jstree-leaf" data-sector="${ it?.sector }" data-institution-id="${ it.id }">
                          <a href="${it.uri}" class="">
                            ${ it?.name } <span>(<g:message code="${ it?.sectorLabelKey }" />)</span>
                          </a>
                        </li>
                      </g:else>
                    </g:each>
                  </ul>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      <div class="span8">
        <div class="list-container">
          <g:if test="${results}">
            <g:itemResultsRender results="${results.results["docs"]}"></g:itemResultsRender>
          </g:if>
        </div>
      </div>
    </div>
  </body>
</html>