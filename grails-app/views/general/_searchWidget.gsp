<div id="search-widget" class="pillbox">
  <div class="controls-container row">
    <div class="span5">
      <select class="sparte-filter empty">
        <option value="default" selected>
          <g:message code="apd.filter_sector" />
        </option>
        <g:each in="${filters.sector}">
          <g:if test="${ it instanceof List}">
            <g:each in="${it}" var="subfilter">
              <option value="sector:${subfilter}">- ${subfilter}</option>
            </g:each>
          </g:if>
          <g:else>
            <option value="sector:${it}">${it}</option>
          </g:else>
        </g:each>
      </select>
    </div>
    <div class="span5">
      <select class="bundesland-filter empty">
        <option value="default" selected>
          <g:message code="apd.filter_region" />
        </option>
        <g:each in="${filters.state}">
          <g:if test="${ it instanceof List}">
            <g:each in="${it}" var="subfilter">
              <option value="state:${subfilter}">- ${subfilter}</option>
            </g:each>
          </g:if>
          <g:else>
            <option value="state:${it}">${it}</option>
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
          <option value="alphabet:${it}">${it}</option>
        </g:each>
      </select>
    </div>
    <div class="span1">
      <g:apdForm class="search-widget-form" id="search-widget-form" controller="structureview" params="${params}" method="GET">
        <button type="submit" class="btn">
          <i class="icon-search"></i>
        </button>
      </g:apdForm>
    </div>
  </div>
  <ul>
  </ul>
</div>