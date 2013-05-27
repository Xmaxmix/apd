<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>

<div class="row">
  
  <div class="span8 institution">
    <div class="row">
      <div class="span6">
        <div>
          <g:message code="apd.${parentInstitution.sector}"/>
        </div>
         <div id="institution-name">
           <h2>${parentInstitution.name}
             <g:if test="${(countObjcs > 0)}">
               <g:set var="facetvalue" value="provider_fct=${parentInstitution.name}"/>
               <%-- 
               <g:link class="count" controller="objectview" action="index" title="${message(code: 'apd.InstitutionItem_IngestedObjectCountTitleText')}">
                 <g:set var="flashArgs" value='["${String.format(RequestContextUtils.getLocale(request),'%,d', countObjcs)}"]' />
                 <g:if test="${(countObjcs == 1)}">
                   <g:message args="${flashArgs}" code="apd.InstitutionItem_IngestedObjectCountFormat" />
                 </g:if>
                 <g:if test="${(countObjcs > 1)}">
                   <g:message args="${flashArgs}" code="apd.InstitutionItem_IngestedObjectCountFormat_Plural" />
                 </g:if>
               </g:link>
               --%>
               <g:apdLink class="count" controller="objectview" action="index" params="${params}"
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
           <a href="${parentInstitution.uri }/">${String.valueOf(parentInstitution.uri).trim() }</a>
         </div>
       </div>
       <div class="span2">
        <img class="logo" alt="${parentInstitution.name}" src="${parentInstitution.logo}">
      </div>
    </div>
  </div>
  
</div>

 <div class="row">
   <div class="span8 locations">
    <div id="divOSM"></div>
    <div class="location-container">
      <div class="location" data-lat="${parentInstitution.locations.location.geocode.latitude }" data-lon="${parentInstitution.locations.location.geocode.longitude }">
        <p class="address">
          <b>${parentInstitution.name}</b><br>
          <span class="space">${parentInstitution.locations.location.address.street }</span>${parentInstitution.locations.location.address.houseIdentifier }<br>
          <g:if test="${(parentInstitution.locations.location.address.addressSupplement)&&(parentInstitution.locations.location.address.addressSupplement.text().length() > 0)}">
            ${parentInstitution.locations.location.address.addressSupplement}<br>
          </g:if>
          <span class="space">${parentInstitution.locations.location.address.postalCode }</span>${parentInstitution.locations.location.address.city }
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

<div class="row">
  <div class="span8 fields">
    <g:if test="${item}">
      <div class="row">
        <div class="span2"><strong><g:message code="apd.indexmeta_0002"/>: </strong></div>
        <div class="value span6">${item.title}</div>
      </div>
      <g:each in="${item.fields}">
        <div class="row">
          <div class="span2"><strong>${it.name}: </strong></div>
          <div class="value span6">${it.value}</div>
        </div>
      </g:each>
      </g:if>
    <g:else>
      <div class="row">
        <div class="value span8">
          <g:message code="apd.No_InstitutionItem_Data"/>
        </div>
      </div>
    </g:else>
      
  </div>
</div>
  
<g:if test="${binaries}">
  <div class="row">
    <div class="span8 binaries">    
      <g:render template="/detailview/binaries" />
    </div>
  </div>
</g:if>

  