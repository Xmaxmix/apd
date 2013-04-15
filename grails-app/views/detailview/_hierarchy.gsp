<g:set var="hierarchyItem" value="${hierarchyItem.getFirstChild() }" />
<g:if test="${hierarchyItem}">
  <ul>
    <g:if test="${hierarchyItem.isMainItem}" >
      <li><strong>${hierarchyItem.label}</strong></li>
    </g:if>
    <g:else>
      <li>${hierarchyItem.label}</li>
    </g:else>
    <g:render template="hierarchy" />
  </ul>
</g:if>
