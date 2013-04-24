<html>
  <head>
    <title><g:message code="apd.Structure"/> - <g:message code="apd.ArchivportalD"/></title>
    
    <meta name="page" content="structureview" />
    <meta name="layout" content="main" />
    
    <r:require module="structureview"/>
    
  </head>
  <body>
    <div class="row">
      <div class="span12 search-widget-container border">search-widget</div>
    </div>
    <div class="row">
      <div class="span4">
        <div class="row">
          <div class="span4">
            <g:link controller="struktur">
              <div class="selector active"><g:message code="apd.Structure"/></div>
            </g:link>
            <g:link controller="liste">
              <div class="selector"><g:message code="apd.Object"/></div>
            </g:link>
          </div>
        </div>
        <div class="row">
          <div class="span4">

            <div id="institution-tree">
              <ul>
                <g:each in="${ all }">
                  <g:if test="${ it?.children}">
                    <li class="jstree-open" data-sector="${ it?.sector }" data-institution-id="${ it.id }">
                      <a href="${it.uri}" class="">
                        ${ it?.name } <span>(<g:message code="${ it?.sectorLabelKey }" />)</span>
                      </a>
                      <ul>
                        <g:render template="listItem" model="['children': it?.children]" />
                      </ul>
                    </li>
                  </g:if>
                  <g:else>
                    <li class="jstree-leaf" data-sector="${ it?.sector }" data-institution-id="${ it.id }">
                      <a href="${it.uri}" class="">
                        ${ it?.name } <span>(<g:message code="${ it?.sectorLabelKey }" />)</span>
                      </a>
                    </li>
                  </g:else>
                </g:each>
              </ul>
            </div>

          </div>
        </div>
      </div>
      <div class="span8">
        <g:if test="${!selectedOrgXML}">
          <div class="details-container">details</div>
        </g:if>
        <g:else>
          <div class="institution-item-details">
            <div class="row">
               <div class="span8 institution">
                 <div class="row">
                   <div class="span6">
                     <div>
                       <g:message code="apd.${selectedOrgXML.sector}"/>
                     </div>
                     <div>
                       <h2>${selectedOrgXML.name}
                         <g:if test="${(countObjcs > 0)}">
                           <g:set var="facetvalue" value="provider_fct=${selectedOrgXML.name}"/>
                           <g:link class="count" controller="search" action="results" params="[query: '', offset: '0',
                             rows: '20', 'facetValues[]': facetvalue]" title="${message(code: 'apd.InstitutionItem_IngestedObjectCountTitleText')}">
                             <g:set var="flashArgs" value='["${String.format(RequestContextUtils.getLocale(request),'%,d', countObjcs)}"]' />
                             <g:if test="${(countObjcs == 1)}">
                               <g:message args="${flashArgs}" code="apd.InstitutionItem_IngestedObjectCountFormat" />
                             </g:if>
                             <g:if test="${(countObjcs > 1)}">
                               <g:message args="${flashArgs}" code="apd.InstitutionItem_IngestedObjectCountFormat_Plural" />
                             </g:if>
                           </g:link>
                         </g:if>
                       </h2>
                     </div>
                     <div>
                       <a href="${selectedOrgXML.uri }/">${String.valueOf(selectedOrgXML.uri).trim() }</a>
                     </div>
                   </div>
                   <div class="span2">
                     <img class="logo" alt="${selectedOrgXML.name}" src="${selectedOrgXML.logo}">
                   </div>
                 </div>
               </div>
             </div>
  
             <div class="row">
               <div class="span8 locations">
                <div id="divOSM"></div>
                <div class="location-container">
                  <div class="location" data-lat="${selectedOrgXML.locations.location.geocode.latitude }" data-lon="${selectedOrgXML.locations.location.geocode.longitude }">
                    <p class="address">
                      <b>${selectedOrgXML.name}</b><br>
                      <span class="space">${selectedOrgXML.locations.location.address.street }</span>${selectedOrgXML.locations.location.address.houseIdentifier }<br>
                      <g:if test="${(selectedOrgXML.locations.location.address.addressSupplement)&&(selectedOrgXML.locations.location.address.addressSupplement.text().length() > 0)}">
                        ${selectedOrgXML.locations.location.address.addressSupplement}<br>
                      </g:if>
                      <span class="space">${selectedOrgXML.locations.location.address.postalCode }</span>${selectedOrgXML.locations.location.address.city }
                    </p>
                  </div>
                  <g:if test="${((subOrg)&&(subOrg.size() > 0)&&(!(parentOrg[parentOrg.size() - 1].aggregationEntity)))}">
                    <div class="hierarchy">
                      <span class="title"><g:message code="apd.InstitutionItem_OtherLocations" /></span>
                      <ol class="institution-list">
                        <li class="institution-listitem">
                          <g:if test="${(selectedItemId == itemId)}">
                            <i class="icon-institution"></i>
                            <b>${parentOrg[parentOrg.size() - 1].label}</b>
                          </g:if>
                          <g:else>
                            <i class="icon-child-institution"></i>
                            <a href="${request.contextPath}/about-us/institutions/item/${parentOrg[parentOrg.size() - 1].id}">${parentOrg[parentOrg.size() - 1].label}</a>
                          </g:else>
                          <g:render template="subinstitutions" />
                        </li>
                      </ol>
                    </div>
                  </g:if>
                </div>
              </div>
            </div>
          </div>
        </g:else>
      </div>
    </div>
  </body>
</html>