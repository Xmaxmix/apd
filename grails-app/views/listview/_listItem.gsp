<g:each var="child" in="${ children }">
  <g:if test="${ child?.children}">
    <li class="jstree-close" data-sector="${ child?.sector }" data-institution-id="${ child.id }">
      <a href="${child.uri}" class=""> 
        ${ child?.name }
        <span>(<g:message code="${ child?.sectorLabelKey }" />)</span>
        <span>(${ child?.children.size() } <g:message code="apd.Object"/>)</span>
      </a>
      <ul>
        <g:render template="listItem" model="['children': child?.children]" />
      </ul>
    </li>
  </g:if>
  <g:else>
    <li class="jstree-leaf" data-sector="${ child?.sector }" data-institution-id="${ child.id }">
      <a href="${child.uri}" class="">
        ${ child?.name } <span>(<g:message code="${ child?.sectorLabelKey }" />)</span>
      </a>
    </li>
  </g:else>
</g:each>