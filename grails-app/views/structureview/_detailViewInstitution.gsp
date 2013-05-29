<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>

<div class="row">
  <div class="span8 institution">
    <div class="row">
      <div class="span6">
        <div>
          <g:message code="apd.${selectedOrgXML.sector}"/>
        </div>
         <div id="institution-name" data-id="${selectedItemId}">
           <h2>${selectedOrgXML.name}
             <g:if test="${(countObjcs > 0)}">
               <g:set var="facetvalue" value="provider_fct=${selectedOrgXML.name}"/>
               <g:apdLink class="count" controller="objectview" params="${params}" 
                  title="${message(code: 'apd.InstitutionItem_IngestedObjectCountTitleText')}">
                 <g:set var="flashArgs" value='["${String.format(RequestContextUtils.getLocale(request),'%,d', countObjcs)}"]' />
                 <g:if test="${(countObjcs == 1)}">
                   <g:message args="${flashArgs}" code="apd.InstitutionItem_IngestedObjectCountFormat" />
                 </g:if>
                 <g:if test="${(countObjcs > 1)}">
                   <g:message args="${flashArgs}" code="apd.InstitutionItem_IngestedObjectCountFormat_Plural" />
                 </g:if>
               </g:apdLink>
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
        <div class="hierarchy span8">
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
        