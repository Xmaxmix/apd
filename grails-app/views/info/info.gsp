<html>
  <head>
    <title>${title} - <g:message code="apd.ArchivportalD"/></title>
    
    <meta name="page" content="info" />
    <meta name="layout" content="main" />

    <g:if test="${author}">
      <meta name="author" content="${author}" />
    </g:if>
    <g:if test="${keywords}">
      <meta name="keywords" content="${keywords}" />
    </g:if>
    <g:if test="${robots}">
      <meta name="robots" content="${robots}" />
    </g:if>
    
  </head>
  <body>
    ${content}
  </body>
</html>
