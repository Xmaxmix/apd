<ul class="results-list">
  <%-- 
  <g:set var="pageHitCounter" value="${0}"/>
  <g:each in="${results}">
    <g:set var="pageHitCounter" value="${pageHitCounter + 1}" />
    <g:set var="hitNumber" value="${offset + pageHitCounter}"/>
    <g:set var="controller" value="detailview" />
    <li class="item bt">
      <div class="summary row">
        <g:render template="summaryMainWrapper" model="${[item: it, urlParams: urlParams, hitNumber: hitNumber, action: action, controller: controller]}" />
        <g:render template="thumbnailWrapper" model="${[item: it, confBinary: confBinary, hitNumber: hitNumber, action: action, controller: controller]}" />
      </div>
    </li>
  </g:each>
  --%>

  <g:set var="pageHitCounter" value="${0}"/>
  <g:each in="${results}">
    <g:set var="pageHitCounter" value="${pageHitCounter + 1}" />
    <g:set var="hitNumber" value="${offset + pageHitCounter}"/>

    <li class="item bt">
      <div class="summary row">
        ${hitNumber}
        <br />
        ${it.label}
        <br />
        ${it.category}
      </div>
    </li>
  </g:each>
</ul>