<g:set var="hierarchyItem" value="${hierarchyItem.getFirstChild() }" />
<g:if test="${hierarchyItem}">
  <ul>
    <g:if test="${hierarchyItem.isMainItem}" >
      <li>
        <g:if test="${!hierarchyItem.aggregationEntity}">
          <g:link url="${hierarchyItem.id}"><strong>${hierarchyItem.label}</strong></g:link>
        </g:if>
        <g:else>
          <strong>${hierarchyItem.label}</strong>
        </g:else>
      </li>
    </g:if>
    <g:else>
      <li>
        <g:if test="${!hierarchyItem.aggregationEntity}">
          <g:link url="${hierarchyItem.id}">${hierarchyItem.label}</g:link>
        </g:if>
        <g:else>
          ${hierarchyItem.label}
        </g:else>
      </li>
    </g:else>
    <g:render template="hierarchy" />
  </ul>
</g:if>
