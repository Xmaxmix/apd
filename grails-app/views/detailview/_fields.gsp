<div class="fields">
  <div class="row">
    <div class="span2"><strong><g:message code="apd.indexmeta_0002"/>: </strong></div>
    <div class="value <g:if test="${binaryList}">span4</g:if><g:else>span10</g:else>">${title}</div>
  </div>
  <g:each in="${fields}">
    <div class="row">
      <div class="span2"><strong>${it.name}: </strong></div>
      <div class="value <g:if test="${binaryList}">span4</g:if><g:else>span10</g:else>">${it.value}</div>
    </div>
  </g:each>
</div>
