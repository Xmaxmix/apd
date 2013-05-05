<!--[if lt IE 9]>
  <div id="header">
<![endif]-->
<header>
  <h1 class="invisible-but-readable"><g:message code="apd.Heading_Header"/></h1>
  <div class="container">
    <div class="row">
      <div class="span4">
        <g:link controller="index">
          <g:img dir="images" file="archivportal.png" width="423" height="123" />
        </g:link>
      </div>
      <div class="span8">
        <div class="navbar">
          <div class="navbar-inner">
            <div class="container">

              <!-- .btn-navbar is used as the toggle for collapsed navbar content -->
              <a class="btn btn-navbar" data-toggle="collapse"
                data-target=".nav-collapse"> <span class="icon-bar"></span>
                <span class="icon-bar"></span> <span class="icon-bar"></span>
              </a>
              <div class="nav-collapse collapse navbar-responsive-collapse">
                <ul class="nav">
                  <li class="root dropdown<g:isMappingActive context="${params}" testif="${[[controller: "index"], [controller: "advancedsearch"], [controller: "structureview"]]}"> active-default</g:isMappingActive>">
                    <g:link controller="index" class="dropdown-toggle"><g:message code="apd.Home"/></g:link>
                    <ul class="dropdown-menu inline">
                      <li class="<g:isMappingActive context="${params}" testif="${[[controller: "advancedsearch"]]}">active-default</g:isMappingActive>">
                        <g:link controller="advancedsearch"><g:message code="apd.Advanced_Search"/></g:link>
                      </li>
                      <li class="<g:isMappingActive context="${params}" testif="${[[controller: "structureview"]]}">active-default</g:isMappingActive>">
                        <g:link controller="structureview"><g:message code="apd.Tree_View"/></g:link>
                      </li>
                    </ul>
                  <li>
                  <li class="root dropdown<g:isMappingActive context="${params}" testif="${[[controller: "info", page:"ueber"],[controller: "info", page:"neues"],[controller: "info", page:"ddb"]]}"> active-default</g:isMappingActive>">
                    <g:link controller="info" action="ueber" class="dropdown-toggle"><g:message code="apd.About_Us"/></g:link>
                    <ul class="dropdown-menu inline">
                      <li class="<g:isMappingActive context="${params}" testif="${[[controller: "info", page:"neues" ]]}">active-default</g:isMappingActive>">
                        <g:link controller="info" action="neues"><g:message code="apd.News"/></g:link>
                      </li>
                      <li class="<g:isMappingActive context="${params}" testif="${[[controller: "info", page:"ddb" ]]}">active-default</g:isMappingActive>">
                        <g:link controller="info" action="ddb"><g:message code="apd.DDB"/></g:link>
                      </li>
                    </ul>
                  <li>
                  <li class="root dropdown<g:isMappingActive context="${params}" testif="${[[controller: "info", page: "hilfe"],[controller: "info", page:"glossar"],[controller: "info", page:"faq"]]}"> active-default</g:isMappingActive>">
                    <g:link controller="info" action="hilfe" class="dropdown-toggle"><g:message code="apd.Help"/></g:link>
                    <ul class="dropdown-menu inline">
                      <li class="<g:isMappingActive context="${params}" testif="${[[controller: "info", page:"glossar" ]]}">active-default</g:isMappingActive>">
                        <g:link controller="info" action="glossar"><g:message code="apd.Glossary"/></g:link>
                      </li>
                      <li class="<g:isMappingActive context="${params}" testif="${[[controller: "info", page:"faq" ]]}">active-default</g:isMappingActive>">
                        <g:link controller="info" action="faq"><g:message code="apd.FAQ"/></g:link>
                      </li>
                    </ul>
                  <li>
                </ul>
                <g:if test="${!isStart}">
                  <g:form url="[controller:'liste', action:'']" class="navbar-search pull-right" method="GET">
                    <input id="query" name="query" type="text" class="search-query span2" placeholder="<g:message code="apd.Search_Placeholder"/>">
                  </g:form>
                </g:if>
              </div>
              <%--  
              <div class="language-wrapper">
                Current: <g:currentLanguage />, 
                Select:
                  <g:languageLink params="${params}" locale="de" islocaleclass="nopointer"><g:message code="apd.language_de"/></g:languageLink>
                  <g:languageLink params="${params}" locale="en" islocaleclass="nopointer"><g:message code="apd.language_en"/></g:languageLink>
              </div>
              --%>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</header>
<!--[if lt IE 9]>
  </div>
<![endif]-->
