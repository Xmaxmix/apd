<g:set var="hierarchyItem" value="${hierarchyItem.getFirstChild() }" />
<g:if test="${hierarchyItem}">
  <ul>
    <g:if test="${hierarchyItem.isMainItem}" >
      <li>
        <g:link url="${hierarchyItem.id}"><strong>${hierarchyItem.label}</strong></g:link>
      </li>
    </g:if>
    <g:else>
      <li>
        <g:link url="${hierarchyItem.id}">${hierarchyItem.label}</g:link>
      </li>
    </g:else>
    <g:render template="hierarchy" />
  </ul>
</g:if>
