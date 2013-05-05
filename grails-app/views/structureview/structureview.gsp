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
      <div class="span12 search-widget-container border">search-widget</div>
    </div>
    <div class="row">
      <div class="span4">
        <div class="row">
          <div class="span4">
            <g:link controller="structureview">
              <div class="selector active"><g:message code="apd.Structure"/></div>
            </g:link>
            <g:link controller="objectview">
              <div class="selector"><g:message code="apd.Object"/></div>
            </g:link>
          </div>
        </div>
        <div class="row">
          <div class="span4">

            <div id="institution-tree">
              <%-- 
              <ul>
                <g:each in="${ all }">
                  <g:if test="${ it?.children}">
                    <li class="jstree-open" data-sector="${ it?.sector }" data-institution-id="${ it.id }">
                      <g:remoteLink action="ajaxDetails" update="details-container" params="[id:it.id]"
                        onComplete="mapSetup();">
                        ${ it?.name } <span>(<g:message code="${ it?.sectorLabelKey }" />)</span>
                      </g:remoteLink>
                      <ul>
                        <g:render template="listItem" model="['children': it?.children]" />
                      </ul>
                    </li>
                  </g:if>
                  <g:else>
                    <li class="jstree-leaf" data-sector="${ it?.sector }" data-institution-id="${ it.id }">
                      <g:remoteLink action="ajaxDetails" update="details-container" params="[id:it.id]"
                        onComplete="mapSetup();">
                        ${ it?.name } <span>(<g:message code="${ it?.sectorLabelKey }" />)</span>
                      </g:remoteLink>
                    </li>
                  </g:else>
                </g:each>
              </ul>
              --%>
            </div>

          </div>
        </div>
      </div>
      <div class="span8">

        <div class="institution-item-details">
        <%-- 
          <div id="details-container">
            <g:if test="${selectedItemId}">
              <g:render template="ajaxDetails" />  
            </g:if>
            <g:else>
              <h1>Bitte wählen Sie ein Archiv</h1>
            </g:else>
          </div>
          --%>
          <h1>Bitte wählen Sie ein Archiv</h1>
        </div>
      </div>
    </div>
  </body>
</html>
