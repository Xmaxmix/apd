<%--
Copyright (C) 2013 FIZ Karlsruhe
 
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>


<div class="span6 search-nav">
  <div class="search-nav-item">
    <g:apdLink class="back-to-list" controller="objectview" action="index" params="${params}" remove="${['id','searchId','hitNumber','name']}"><g:message code="apd.Back_ResultList" /></g:apdLink>
  </div>
  <div class="search-nav-item">
    <a class="page-link page-link-popup-anchor" href="<g:createApdLink controller="detailview" action="index" params="${params}" addOrUpdate="${['id': itemId, 'name': friendlyTitle, 'hitNumber': navData.hitNumber, 'searchId': itemId]}" />" title="<g:message code="apd.Page_Link" />">
      <span><g:message code="apd.Page_Link" /></span>
    </a>
  </div>
</div>

<div class="item-nav span6">

  <g:set var="prevId" value="none" />
  <g:set var="nextId" value="none" />

  <g:if test="${navData.hitNumber == 1}">
    <g:set var="displayLeftPagination" value="off" />
  </g:if>
  <g:if test="${navData.hitNumber == navData.resultCount || navData.resultCount == 1}">
    <g:set var="displayRightPagination" value="off" />
  </g:if>
  

  <ul class="inline">
    <li class="first-item ${displayLeftPagination}">
      <g:apdLink controller="detailview" action="index" params="${params}" addOrUpdate="${[id: navData.firstHit, 'offset': navData.firstOffset, 'hitNumber': 1, 'searchId': navData.firstHit]}"><g:message code="apd.First_Label" /></g:apdLink>
    </li>
    <li class="prev-item br-white ${displayLeftPagination}">
      <g:apdLink controller="detailview" action="index" params="${params}" addOrUpdate="${[id: navData.previousHit, 'offset': navData.previousOffset, 'hitNumber': (navData.hitNumber-1), 'searchId': navData.previousHit]}"><g:message code="apd.Previous_Label" /></g:apdLink>
    </li>
    <li>
      <span class="result-label"><g:message code="apd.Hit" /> </span><span class="hit-number"><g:localizeNumber>${navData.hitNumber}</g:localizeNumber></span><span> <g:message code="apd.Of" /> </span><span class="hit-count"><g:localizeNumber>${navData.resultCount}</g:localizeNumber></span>
    </li>
    <li class="next-item bl-white ${displayRightPagination}">
      <g:apdLink controller="detailview" action="index" params="${params}" addOrUpdate="${[id: navData.nextHit, 'offset': navData.nextOffset, 'hitNumber': (navData.hitNumber+1), 'searchId': navData.nextHit]}"><g:message code="apd.Next_Label" /></g:apdLink>
    </li>
    <li class="last-item ${displayRightPagination}">
      <g:apdLink controller="detailview" action="index" params="${params}" addOrUpdate="${[id: navData.lastHit, 'offset': navData.lastOffset, 'hitNumber': navData.resultCount, 'searchId': navData.lastHit]}"><g:message code="apd.Last_Label" /></g:apdLink>
    </li>
  </ul>

</div>
