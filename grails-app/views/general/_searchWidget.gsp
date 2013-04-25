<div id="search-widget" class="pillbox">
  <div class="controls-container row">
    <div class="span5">
      <select class="sparte-filter empty">
        <option value="default" selected>
          <g:message code="apd.filter_sector" />
        </option>
        <g:each in="${filters.sparte}">
          <g:if test="${ it instanceof List}">
            <g:each in="${it}" var="subfilter">
              <option value="${subfilter}">- ${subfilter}</option>
            </g:each>
          </g:if>
          <g:else>
            <option value="${it}">${it}</option>
          </g:else>
        </g:each>
      </select>
    </div>
    <div class="span5">
      <select class="bundesland-filter empty">
        <option value="default" selected>
          <g:message code="apd.filter_region" />
        </option>
        <g:each in="${filters.bundesland}">
          <g:if test="${ it instanceof List}">
            <g:each in="${it}" var="subfilter">
              <option value="${subfilter}">- ${subfilter}</option>
            </g:each>
          </g:if>
          <g:else>
            <option value="${it}">${it}</option>
          </g:else>
        </g:each>
      </select>
    </div>
    <div class="span1">
      <select class="az-filter empty">
        <option value="default" selected>
          A-Z
        </option>
        <g:each in="${filters.alphabet}">
          <option value="${it}">${it}</option>
        </g:each>
      </select>
    </div>
    <div class="span1">
      <g:form id="search-widget-form"
        url="[controller: 'structureview', action:' ']" method="GET">
        <button type="submit" class="btn">
          <i class="icon-search"></i>
        </button>
      </g:form>
    </div>
  </div>
  <ul>
  </ul>
</div>