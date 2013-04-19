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
              <div class="selector"><g:message code="apd.Struktur"/></div>
            </g:link>
            <g:link controller="listview">
              <div class="selector active"><g:message code="apd.Objecte"/></div>
            </g:link>
          </div>
        </div>
        <div class="row">
          <div class="span4">

            <div id="institution-tree">
              <ul>
                <g:each in="${ all }">
                  <g:if test="${ it?.children}">
                    <li class="jstree-open" data-sector="${ it?.sector }" data-institution-id="${ it.id }">
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
