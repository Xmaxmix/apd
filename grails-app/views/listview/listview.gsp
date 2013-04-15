<html>
  <head>
    <title><g:message code="apd.Trefferliste"/> - <g:message code="apd.ArchivportalD"/></title>
    
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
            <g:link controller="structureview">
              <div class="selector border"><g:message code="apd.Struktur"/></div>
            </g:link>
            <g:link controller="listview">
              <div class="selector border"><g:message code="apd.Objecte"/></div>
            </g:link>
          </div>
        </div>
        <div class="row">
          <div class="span4 tree-container fuelux">

            <div id="institution-tree" class="tree">
              <g:each in="${ all }">
                <g:if test="${ it?.children}">
                  <div class="tree-folder">
                    <div class="tree-folder-header">
                      <i class="icon-folder icon-folder-close"></i>
                      <div class="tree-folder-name">
                        <a href="${it.uri}"> 
                          ${ it?.name } <span>(<g:message code="${ it?.sectorLabelKey }" />)</span>
                        </a>
                      </div>
                    </div>
                    <div class="tree-folder-content off">
                      <g:render template="listItem" model="['children': it?.children]" />
                    </div>
                    <div class="tree-loader off">
                      <div>Loading...</div>
                    </div>
                  </div>
                </g:if>
                <g:else>
                  <div class="tree-item" data-sector="${ it?.sector }" data-institution-id="${ it.id }">
                    <i class="tree-dot"></i>
                    <div class="tree-item-name">
                      <a href="${it.uri}">
                        ${ it?.name } <span>(<g:message code="${ it?.sectorLabelKey }" />)</span>
                      </a>
                    </div>
                  </div>
                </g:else>
              </g:each>
            </div>

          </div>
        </div>
      </div>
      <div class="span8">
        <div class="details-container">
          <g:if test="${results}">
            <g:itemResultsRender results="${results.results["docs"]}"></g:itemResultsRender>
          </g:if>
          <g:else>
          list
          </g:else>
        </div>
      </div>
    </div>
  </body>
</html>
