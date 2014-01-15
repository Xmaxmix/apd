<html>
<head>
<title>
  <%-- <g:message code="apd.Homepage"/> - --%> <g:message
    code="apd.ArchivportalD" />
</title>

<meta name="page" content="index" />
<meta name="layout" content="main" />
<%-- 
    <r:require module="startpage"/>
    --%>
</head>
<body>
  <div id="home-search-container">
    <g:apdForm id="home-search-form" controller="liste" params="${params}" class="well" method="GET">
      <div id="search-widget" class="row-fluid">
        <div class="span8 offset2 input-append">
          <input type="text" id="query" name="query"
            class="input-xlarge search-query"
            placeholder="<g:message code="apd.Search_Placeholder"/>">
          <button type="submit" class="btn btn-primary">
             Suchen (Replace)
          </button>
      </div>
      <hr>
      <h2>Archive finden</h2>
    </g:apdForm>
    <div>
      <g:searchWidgetRender></g:searchWidgetRender>
    </div>
  </div>
  <div id="carousel-container"></div>
</body>
</html>
