<%--
<g:pageInfoNavRender navData="${[

    resultsOverallIndex: resultsOverallIndex,
    numberOfResults: numberOfResultsFormatted,
    page: page, 
    totalPages: totalPages,
    paginationURL: paginationURL
    
  ]}"></g:pageInfoNavRender>
 --%>
<div class="page-info-nav">

  <div class="page-info">
    <span class="results-overall-index">1 - 20</span>
    <span><g:message code="apd.Of" /></span>
    <span><b><span class="results-total" id="results-total">0</span></b></span>
    <g:if test="true">
      <span id="results-label"><g:message code="apd.Results_lowercase" /></span>
    </g:if>
    <g:else>
      <span id="results-label"><g:message code="apd.Result_lowercase" /></span>
    </g:else>
  </div><!-- /end of .page-info -->
  
  <div class="page-nav">
    <ul class="inline">
      <li class="first-page" >
        <a class="off page-nav-result" href="paginationUrlFirstPage"><g:message code="apd.First_Label" /></a>  
      </li>
      <li class="prev-page br">
        <a class="off page-nav-result" href="paginationUrlPrevPage"><g:message code="apd.Previous_Label" /></a> 
      </li>
      <li class="pages-overall-index">
        <span>
            <g:message code="apd.Page" />
            <input type="text" class="input-mini page-input off" maxlength="10" value="1" />
            <%--
            <span class="page-nonjs">1</span>
             --%>
            <g:message code="apd.Of" />
            <span class="total-pages">
            <%--
              <g:localizeNumber>totalPages</g:localizeNumber>
             --%>
              500
            </span>
        </span>
      </li>
      <li class="next-page bl">
        <a class="page-nav-result" href="paginationUrlNextPage"><g:message code="apd.Next_Label" /></a> 
      </li>
      <li class="last-page">
        <a class="page-nav-result" href="paginationUrlLastPage"><g:message code="apd.Last_Label" /></a> 
      </li>
    </ul>
  </div><!-- /end of .page-nav -->
</div>
