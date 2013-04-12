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
            <div class="selector border"><g:message code="apd.Struktur"/></div>
            <div class="selector border"><g:message code="apd.Objecte"/></div>
          </div>
        </div>
        <div class="row">
          <div class="span4 tree-container border">
            <ol id="institution-list">
              <g:each in="${ all }">
                <li class="institution-listitem" data-sector="${ it?.sector }" data-institution-id="${ it?.id }">
                  <i class="icon-institution"></i>
                  <g:render template="listItem" model="['item': it]"/>
                  <g:render template="children" model="['children': it?.children]"/>
                </li>
              </g:each>
            </ol>
          </div>
        </div>
      </div>
      <div class="span8">
        <div class="details-container">details</div>
      </div>
    </div>
  </body>
</html>