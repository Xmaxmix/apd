<html>
  <head>
    <title>${title} - <g:message code="apd.ArchivportalD"/></title>

    <meta name="page" content="detailview" />
    <meta name="layout" content="main" />

  </head>
  <body>
    <div class="container detailview">
      <div class="row toprow" >
        <div class="span12 navigation-search">
          <g:render template="navigationSearch" />
        </div>
        <div class="span6 hierarchy">
          <div class="hierarchy-container">
            <g:set var="hierarchyItem" value="${hierarchyRoot}" />
            <g:render template="hierarchy" />
          </div>
        </div>
        <div class="span6">
          <div class="logo">
            <g:render template="logo" />
          </div>
          <div class="navigation">
            <g:render template="navigation" />
          </div>
        </div>
      </div>
      <div class="row">
          <g:render template="fields" />
          <g:render template="binaries" />
      </div>
    </div>
  </body>
</html>
