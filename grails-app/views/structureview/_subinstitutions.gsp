<g:set var="jsonHierarchyChildrenOfOrg" value="${vApiInst.getChildrenOfInstitutionByItemId(itemId, grailsApplication.config.apd.backend.url.toString())}" />
<g:if test="${((jsonHierarchyChildrenOfOrg)&&(jsonHierarchyChildrenOfOrg.size() > 0))}">
  <ol class="institution-list">
    <g:each in="${jsonHierarchyChildrenOfOrg}" >
      <li class="institution-listitem" data-sector="${ it?.sector }" data-institution-id="${ it?.id }" >
        <g:if test="${(selectedItemId == it.id)}">
          <i class="icon-institution"></i>
          <b>${it.label}</b>
        </g:if>
        <g:else>
          <i class="icon-child-institution"></i>
          <%-- 
          <g:link controller="institution" action="showInstitutionsTreeByItemId" params="[id: it.id]">${it.label}</g:link>
          --%>
          <g:apdLink controller="institution" action="showInstitutionsTreeByItemId" params="${params}" addOrUpdate="${[id: it.id]}">${it.label}</g:apdLink>
        </g:else>
        <g:if test="${!(it.aggregationEntity)}">
          <g:set var="itemId" value="${it.id}" />
          <g:render template="subinstitutions" />
        </g:if>
      </li>
    </g:each>
  </ol>
</g:if>
