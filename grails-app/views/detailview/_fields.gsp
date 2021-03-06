<g:if test="${binaryList?.size() > 0}">
  <g:set var="spanSizeRow" value="${"span8"}" />
  <g:set var="spanSizeValue" value="${"span4"}" />
</g:if>
<g:else>
  <g:set var="spanSizeRow" value="${"span12"}" />
  <g:set var="spanSizeValue" value="${"span10"}" />
</g:else>

<div class="${spanSizeRow} fields">
  <div class="fields">
    <div class="row">
      <div class="span2"><strong><g:message code="apd.indexmeta_0002"/>: </strong></div>
      <div class="value ${spanSizeValue}">${title}</div>
    </div>
    <g:each in="${fields}">
      <div class="row">
        <div class="span2"><strong>${it.name}: </strong></div>
        <div class="value ${spanSizeValue}">${it.value}</div>
      </div>
    </g:each>
    
    <div class="row">
      <a class="page-link span8" href="${itemUri}" title="<g:message code="apd.Item_LinkToThisPage" />">
        <span><g:message code="apd.Item_LinkToThisPage" /></span>
      </a>  
    </div>
    <div class="row">
      <a class="page-link span8" href="${ddbUri}" title="<g:message code="apd.Item_LinkToDDBPage" />">
        <span><g:message code="apd.Item_LinkToDDBPage" /></span>
      </a>  
    </div>
  </div>
</div>