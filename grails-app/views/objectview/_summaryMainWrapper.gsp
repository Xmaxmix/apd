<div class="summary-main-wrapper span6">
  <div class="summary-main">
    <h2 class="title">
      <g:link class="persist" controller="detailview" action="index" 
        params="['id': item.id, 'name': item.preview.urlFriendlyTitle, 'query': params.query, 'offset': params.offset, 'pagesize': params.pagesize, 'sort': params.sort, 'nodeId': params.id]" 
        title="${truncateHovercardTitle(title: item.label, length: 350)}">
      <g:truncateItemTitle title="${ item.preview.title }" length="${ 100 }"></g:truncateItemTitle>
      </g:link>
    </h2>
    <div class="subtitle">${item.preview.subtitle}</div>
    <ul class="matches unstyled">
      <li class="matching-item">
        <span>
          <g:each var="match" in="${item.view}">
            ...${match.replaceAll('match', 'strong')}...
          </g:each>
        </span>
      </li>
    </ul>
  </div>
  <div class="extra">
    <ul class="types unstyled inline">
    <g:each var="mediaType" in="${item.preview.media}">
      <g:set var="mediaTitle"><g:message code="apd.mediatype_007" /></g:set>
      <g:if test="${mediaType == 'audio'}">
        <g:set var="mediaTitle"><g:message code="apd.mediatype_001" /></g:set>
      </g:if>
      <g:if test="${mediaType == 'image'}">
        <g:set var="mediaTitle"><g:message code="apd.mediatype_002" /></g:set>
      </g:if>
      <g:if test="${mediaType == 'text'}">
        <g:set var="mediaTitle"><g:message code="apd.mediatype_003" /></g:set>
      </g:if>
      <g:if test="${mediaType == 'fullText'}">
        <g:set var="mediaTitle"><g:message code="apd.mediatype_004" /></g:set>
      </g:if>
      <g:if test="${mediaType == 'video'}">
        <g:set var="mediaTitle"><g:message code="apd.mediatype_005" /></g:set>
      </g:if>
      <g:if test="${mediaType == 'other'}">
        <g:set var="mediaTitle"><g:message code="apd.mediatype_006" /></g:set>
      </g:if>
      <li class="${mediaType}" classname="${mediaType}" title="${mediaTitle}">${mediaTitle}</li>
    </g:each>
    </ul>
  </div>
</div>
