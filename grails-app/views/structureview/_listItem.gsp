<g:if test="${ item.isFirst }">
  <a id="${ item.firstChar }"
    href="${item.uri}">
    ${ item?.name } <span>(<g:message code="${ item?.sectorLabelKey }" />)
    </span>
  </a>
</g:if>
<g:else>
  <a href="${item.uri}">
    ${ item?.name } <span>(<g:message code="${ item?.sectorLabelKey }" />)</span>
  </a>
</g:else>