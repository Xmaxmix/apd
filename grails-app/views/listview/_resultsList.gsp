<ul class="results-list unstyled  <g:if test="${viewType == 'grid'}">grid</g:if>">
  <g:set var="pageHitCounter" value="${0}"/>
  <g:each in="${results}">
    <g:set var="pageHitCounter" value="${pageHitCounter + 1}" />
    <g:set var="hitNumber" value="${offset + pageHitCounter}"/>
    <g:set var="controller" value="item" />
    <g:set var="action" value="findById" />
    <g:if test="${it.category == 'Institution'}">
        <g:set var="controller" value="institution" />
        <g:set var="action" value="showInstitutionsTreeByItemId" />
    </g:if>
    <li class="item bt">
      <div class="summary <g:if test="${viewType != 'grid'}">row</g:if>">
        <g:if test="${viewType == 'grid'}">
          <g:render template="thumbnailWrapper" model="${[viewType: viewType, item: it, confBinary: confBinary, hitNumber: hitNumber, action: action, controller: controller]}" />
          <g:render template="summaryMainWrapper" model="${[viewType: viewType, item: it, urlParams: urlParams, hitNumber: hitNumber, action: action, controller: controller]}" />
        </g:if>
        <g:else>
          <g:render template="summaryMainWrapper" model="${[viewType: viewType, item: it, urlParams: urlParams, hitNumber: hitNumber, action: action, controller: controller]}" />
          <g:render template="thumbnailWrapper" model="${[viewType: viewType, item: it, confBinary: confBinary, hitNumber: hitNumber, action: action, controller: controller]}" />
        </g:else>
      </div>
    </li>
  </g:each>
</ul>