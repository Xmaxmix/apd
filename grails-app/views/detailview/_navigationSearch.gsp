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
    <g:link class="back-to-list" controller="objectview" action="index" params="['query': params.query, 'offset': params.offset, 'pagesize': params.pagesize, 'sort': params.sort, 'nodeId': params.nodeId]"><g:message code="apd.Back_ResultList" /></g:link>
  </div>
  <div class="search-nav-item">
    <a class="page-link page-link-popup-anchor" href="${createLink(controller: "detailview", action: "index", params:['id': itemId, 'name': friendlyTitle,'query': params.query, 'offset': params.offset, 'pagesize': params.pagesize, 'sort': params.sort, 'nodeId': params.nodeId])}" title="<g:message code="apd.Page_Link" />">
      <span><g:message code="apd.Page_Link" /></span>
    </a>
  </div>
</div>

<div class="item-nav span6">

  <g:set var="prevId" value="${navData.results.results[0]["docs"][navData.hitNumber - 2].id}" />
  <g:set var="nextId" value="nodisplay" />
  
  <g:if test="${navData.hitNumber == 1}">
    <g:set var="displayLeftPagination" value="off" />
  </g:if>
  <g:if test="${navData.hitNumber == navData.results["numberOfResults"] || navData.results["numberOfResults"] == 1}">
    <g:set var="displayRightPagination" value="off" />
  </g:if>
  
  <g:if test="${navData.hitNumber == navData.results["numberOfResults"]}">
    <g:set var="nextId" value="${navData.results.results[0]["docs"][0].id}" />
  </g:if>
  <g:else>
    <g:set var="nextId" value="${navData.results.results[0]["docs"][navData.hitNumber].id}" />
  </g:else>
  
  <ul class="inline">
    <li class="first-item ${displayLeftPagination}">
      <g:link controller="detailview" action="index" params="[id: navData.firstHit, 'query': params.query, 'offset': params.offset, 'pagesize': params.pagesize, 'sort': params.sort, 'nodeId': params.nodeId]"><g:message code="apd.First_Label" /></g:link>
    </li>
    <li class="prev-item br-white ${displayLeftPagination}">
      <g:link controller="detailview" action="index" params="[id: prevId, 'query': params.query, 'offset': params.offset, 'pagesize': params.pagesize, 'sort': params.sort, 'nodeId': params.nodeId]"><g:message code="apd.Previous_Label" /></g:link>
    </li>
    <li>
      <span class="result-label"><g:message code="apd.Hit" /> </span><span class="hit-number"><g:localizeNumber>${navData.hitNumber}</g:localizeNumber></span><span> <g:message code="apd.Of" /> </span><span class="hit-count"><g:localizeNumber>${navData.results["numberOfResults"]}</g:localizeNumber></span>
    </li>
    <li class="next-item bl-white ${displayRightPagination}">
      <g:link controller="detailview" action="index" params="[id: nextId, 'query': params.query, 'offset': params.offset, 'pagesize': params.pagesize, 'sort': params.sort, 'nodeId': params.nodeId]"><g:message code="apd.Next_Label" /></g:link>
    </li>
    <li class="last-item ${displayRightPagination}">
      <g:link controller="detailview" action="index" params="[id: navData.lastHit, 'query': params.query, 'offset': params.offset, 'pagesize': params.pagesize, 'sort': params.sort, 'nodeId': params.nodeId]"><g:message code="apd.Last_Label" /></g:link>
    </li>
  </ul>

</div>
