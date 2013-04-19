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
              <div class="selector active"><g:message code="apd.Struktur"/></div>
            </a>
            <a href="liste">
              <div class="selector"><g:message code="apd.Objecte"/></div>
            </a>
          </div>
        </div>
        <div class="row">
          <div class="span4">
            <div id="institution-tree">
              <ul>
                <g:each in="${ all }">
                  <g:if test="${ it?.children}">
                    <li class="jstree-closed" data-sector="${ it?.sector }" data-institution-id="${ it.id }">
                      <a href="${it.uri}" class="">
                        ${ it?.name } <span>(<g:message code="${ it?.sectorLabelKey }" />)</span>
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
