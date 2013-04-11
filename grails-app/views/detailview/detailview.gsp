<html>
  <head>
    <title>${title} - <g:message code="apd.ArchivportalD"/></title>
    
    <meta name="page" content="detailview" />
    <meta name="layout" content="main" />
    
  </head>
  <body>
    <div class="detailview">
      <div>
        <div class="hierarchy">
          Hierarchy
          <g:render template="hierarchy" />
        </div>
        <div class="navigation">
          Navigation
          <g:render template="navigation" />
        </div>
        <div class="logo">
          Logo
          <g:render template="logo" />
        </div>
      </div>
      <div>
        <div class="fields">
          Item Details
          <g:render template="fields" />
        </div>
        <div class="binaries">
          Binary Viewer
          <g:render template="binaries" />
        </div>
      </div>    
    </div>
  </body>
</html>
