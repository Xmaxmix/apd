<g:set var="isStart" value="${isStartValue?1:0}"></g:set>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    
    <title><g:layoutTitle default="ArchivportalD" /></title>

    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no" />
    <%-- 
    <meta name="description" content="Deutsche Digitale Bibliothek" />
    <link rel="search" title="Deutsche Digitale Bibliothek" href=${resource(dir: '/', file: 'opensearch.osdx')} type="application/opensearchdescription+xml" />
    --%>
    <r:require module="apd" />
    <g:layoutHead />
    <r:layoutResources />
  </head>
  <body>
    <g:render template="/general/header" isStart="${isStart}"/>
    <div id="main-container" class="container" role="main">
      <g:layoutBody/>
    </div>
    <g:render template="/general/footer" />
    <g:render template="/general/jsVariables" />
    <jawr:script src="/i18n/messages.js"/>
    <r:layoutResources />
  </body>
</html>
