<g:each var="child" in="${ children }">
  <g:if test="${ child?.children}">
    <li class="jstree-closed" data-sector="${ child?.sector }" data-institution-id="${ child.id }">
      <g:remoteLink action="ajaxDetails" update="details-container" params="[id:child.id]">
        ${ child?.name } <span>(<g:message code="${ child?.sectorLabelKey }" />)</span>
      </g:remoteLink>
      <ul>
        <g:render template="listItem" model="['children': child?.children]" />
      </ul>
    </li>
  </g:if>
  <g:else>
    <li class="jstree-leaf" data-sector="${ child?.sector }" data-institution-id="${ child.id }">
      <g:remoteLink action="ajaxDetails" update="details-container" params="[id:child.id]">
        ${ child?.name } <span>(<g:message code="${ child?.sectorLabelKey }" />)</span>
      </g:remoteLink>
    </li>
  </g:else>
</g:each>