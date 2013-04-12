<html>
  <head>
    <title><%-- <g:message code="apd.Homepage"/> - --%><g:message code="apd.ArchivportalD"/></title>
    
    <meta name="page" content="500" />
    <meta name="layout" content="main" />
    
  </head>
  <body>
    500
    <div class="error">
      <g:if test="${exception}">
        <g:renderException exception="${exception}" />
      </g:if>
      <g:else>
        <b>DEV-Message:</b> No stacktrace available. Most likely, it was already consumed and logged to your console.
      </g:else>  
    </div>
    
  </body>
</html>
