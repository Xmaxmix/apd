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
    <g:form id="home-search-form" url="[controller:'liste', action:'']"
      class="form-search" method="GET">
      <div class="input-append">
        <input type="text" id="query" name="query"
          class="input-xlarge search-query"
          placeholder="<g:message code="apd.Search_Placeholder"/>">
        <button type="submit" class="btn">
          <i class="icon-search"></i>
        </button>
      </div>
    </g:form>
    <div>
      <g:searchWidgetRender></g:searchWidgetRender>
    </div>
  </div>
  <div id="carousel-container"></div>
</body>
</html>
