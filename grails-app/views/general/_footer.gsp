<!--[if lt IE 9]>
  <div id="footer" class="container">
<![endif]-->
  <footer class="container">
    <div class="row">
      <h1 class="invisible-but-readable"><g:message code="apd.Heading_Footer"/></h1>
      <div class="span12 legal">
        <div class="inner">
          <small><g:message code="apd.Copyright_Archive_Portal"/></small>
          <ul class="inline">
            <li><g:apdLink controller="info" action="nutzungsbedingungen" params="${params}" remove="${["page"]}"><g:message code="apd.Terms_of_Use"/></g:apdLink></li>
            <li><g:apdLink controller="info" action="impressum" params="${params}" remove="${["page"]}"><g:message code="apd.Publisher"/></g:apdLink></li>
            <li><g:apdLink controller="info" action="sitemap" params="${params}" remove="${["page"]}"><g:message code="apd.Sitemap"/></g:apdLink></li>
            <li><g:apdLink controller="info" action="kontakt" params="${params}" remove="${["page"]}"><g:message code="apd.Contact"/></g:apdLink></li>
          </ul>
          <div class="build"><g:meta name="app.version"/> / <g:backendVersion/></div>
        </div>
      </div>
  </div>
  </footer>
<!--[if lt IE 9]>
  </div>
<![endif]-->