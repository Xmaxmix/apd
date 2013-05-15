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
    <g:link class="back-to-list" controller="objectview" action="index" params="['query': params.query, 'offset': navData.newOffset, 'pagesize': navData.pagesize, 'sort': params.sort, 'nodeId': params.nodeId, 'hitNumber': navData.hitNumber]"><g:message code="apd.Back_ResultList" /></g:link>
  </div>
  <div class="search-nav-item">
    <a class="page-link page-link-popup-anchor" href="${createLink(controller: "detailview", action: "index", params:['id': itemId, 'name': friendlyTitle,'query': params.query, 'offset': navData.newOffset, 'pagesize': navData.pagesize, 'sort': params.sort, 'nodeId': params.nodeId, 'hitNumber': navData.hitNumber])}" title="<g:message code="apd.Page_Link" />">
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
      <g:link controller="detailview" action="index" params="[id: navData.firstHit, 'query': params.query, 'offset': navData.newOffset, 'pagesize': navData.pagesize, 'sort': params.sort, 'nodeId': params.nodeId, 'hitNumber': 1]"><g:message code="apd.First_Label" /></g:link>
    </li>
    <li class="prev-item br-white ${displayLeftPagination}">
      <g:link controller="detailview" action="index" params="[id: navData.previousHit, 'query': params.query, 'offset': navData.newOffset, 'pagesize': navData.pagesize, 'sort': params.sort, 'nodeId': params.nodeId, 'hitNumber': (navData.hitNumber-1)]"><g:message code="apd.Previous_Label" /></g:link>
    </li>
    <li>
      <span class="result-label"><g:message code="apd.Hit" /> </span><span class="hit-number"><g:localizeNumber>${navData.hitNumber}</g:localizeNumber></span><span> <g:message code="apd.Of" /> </span><span class="hit-count"><g:localizeNumber>${navData.resultCount}</g:localizeNumber></span>
    </li>
    <li class="next-item bl-white ${displayRightPagination}">
      <g:link controller="detailview" action="index" params="[id: navData.nextHit, 'query': params.query, 'offset': navData.newOffset, 'pagesize': navData.pagesize, 'sort': params.sort, 'nodeId': params.nodeId, 'hitNumber': (navData.hitNumber+1)]"><g:message code="apd.Next_Label" /></g:link>
    </li>
    <li class="last-item ${displayRightPagination}">
      <g:link controller="detailview" action="index" params="[id: navData.lastHit, 'query': params.query, 'offset': navData.newOffset, 'pagesize': navData.pagesize, 'sort': params.sort, 'nodeId': params.nodeId, 'hitNumber': navData.resultCount]"><g:message code="apd.Last_Label" /></g:link>
    </li>
  </ul>

</div>
