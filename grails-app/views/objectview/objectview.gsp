<html>
  <head>
    <title><g:message code="apd.List"/> - <g:message code="apd.ArchivportalD"/></title>

    <meta name="page" content="objectview" />
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
              <%-- 
              <ul>
                <li>
                  <span> ${searchResult.count } <g:message code="apd.Object"/></span>
                  <ul>
                    <g:each in="${ searchResult.institutions }">
                      <li id="${ it.id }" class="" >
                        <a id="a-${ it.id }" class="" href="#">
                          ${ it?.name } 
                          <span>(${ it?.count } <g:message code="apd.Object"/>)</span>
                        </a>
                        <ul>
                          <li></li>
                        </ul>
                      </li>
                    </g:each>
                  </ul>
                </li>
              </ul>
              --%>
            </div>
          </div>
        </div>
      </div>
      <div class="span8">
        <g:render template="pagination" />
        <div class="list-container">
          <g:if test="${results}">
            <g:itemResultsRender results="${results.results["docs"]}"></g:itemResultsRender>
          </g:if>
        </div>
      </div>
    </div>
  </body>
</html>