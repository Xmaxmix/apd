<g:set var="hierarchyItem" value="${hierarchyItem.getFirstChild() }" />
<g:if test="${hierarchyItem}">
  <ul>
    <g:if test="${hierarchyItem.isMainItem}" >
      <li>
        <strong>${hierarchyItem.label}</strong>
      </li>
    </g:if>
    <g:else>
      <li>
        <%-- 
        <g:if test="${!hierarchyItem.aggregationEntity}">
          <g:link url="${hierarchyItem.id}">${hierarchyItem.label}</g:link>
        </g:if>
        <g:else>
          ${hierarchyItem.label}
        </g:else>
        --%>
        <g:link controller="structureview" action="index" params="[nodeId: hierarchyItem.id, 'query': params.query, 'offset': navData.newOffset, 'pagesize': navData.pagesize, 'sort': params.sort, 'hitNumber': navData.hitNumber]">
          ${hierarchyItem.label}
        </g:link>
        
      </li>
    </g:else>
    <g:render template="hierarchy" />
  </ul>
</g:if>
