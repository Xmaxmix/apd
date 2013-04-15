<html>
  <head>
    <title><g:message code="apd.Struktur"/> - <g:message code="apd.ArchivportalD"/></title>
    
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
            <a href="struktur">
              <div class="selector border"><g:message code="apd.Struktur"/></div>
            </a>
            <a href="liste">
              <div class="selector border"><g:message code="apd.Objecte"/></div>
            </a>
          </div>
        </div>
        <div class="row">
          <div class="span4 tree-container fuelux">

            <div id="institution-tree" class="tree">
              <g:each in="${ all }">
                <g:if test="${ it?.children}">
                  <div class="tree-folder">
                    <div class="tree-folder-header">
                      <i class="icon-folder icon-folder-open"></i>
                      <div class="tree-folder-name">
                        <a href="${it.uri}"> 
                          ${ it?.name } <span>(<g:message code="${ it?.sectorLabelKey }" />)</span>
                        </a>
                      </div>
                    </div>
                    <div class="tree-folder-content">
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
        <div class="details-container">details</div>
      </div>
    </div>
  </body>
</html>