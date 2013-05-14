<html>
  <head>
    <title>${title} - <g:message code="apd.ArchivportalD"/></title>

    <meta name="page" content="detailview" />
    <meta name="layout" content="main" />

  </head>
  <body>
    <div class="container detailview">
      <div class="row toprow" >
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
        <div class="span8 fields">
          <g:render template="fields" />
        </div>
        <div class="span4 binaries">
          <g:render template="binaries" />
        </div>
      </div>
    </div>
  </body>
</html>
