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
        <g:apdLink controller="structureview" action="index" params="${params}" addOrUpdate="${[nodeId: hierarchyItem.id]}" remove="${['id','searchId','hitNumber','name']}">
          ${hierarchyItem.label}
        </g:apdLink>
      </li>
    </g:else>
    <g:render template="hierarchy" />
  </ul>
</g:if>
