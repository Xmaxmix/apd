<g:each var="child" in="${ children }">
  <g:if test="${ child?.children }">
    <div class="tree-folder" style="display: block;">
      <div class="tree-folder-header">
        <i class="icon-folder-open"></i>
        <div class="tree-folder-name">
          <a href="${child.uri}"> 
            ${ child?.name } <span>(<g:message code="${ child?.sectorLabelKey }" />)</span>
          </a>
        </div>
      </div>
      <div class="tree-folder-content">
        <g:render template="listItem" model="['children': child?.children]" />
      </div>
      <div class="tree-loader" style="display: none">
        <div>Loading...</div>
      </div>
    </div>
  </g:if>
  <g:else>
    <div class="tree-item" data-sector="${ child?.sector }" data-institution-id="${ child.id }" style="display: block;">
      <i class="tree-dot"></i>
      <div class="tree-item-name">
        <a href="${child.uri}">
          ${ child?.name } <span>(<g:message code="${ child?.sectorLabelKey }" />)</span>
        </a>
      </div>
    </div>
  </g:else>
</g:each>