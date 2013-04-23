<html>
  <head>
    <title><g:message code="apd.500.title"/> - <g:message code="apd.ArchivportalD"/></title>
    
    <meta name="page" content="500" />
    <meta name="layout" content="main" />
    
  </head>
  <body>
    <div class="errorpage">
      <h1>
        <g:message code="apd.500.title"/>
      </h1>
      <p>
        <g:message code="apd.500.body"/>
      </p>
      <hr />
      <g:if test="${exception}">
        <g:renderException exception="${exception}" />
      </g:if>
      <g:else>
        <b>DEV-Message:</b> No stacktrace available. Most likely, it was already consumed and logged to your console.
      </g:else>  
    </div>
    
  </body>
</html>
