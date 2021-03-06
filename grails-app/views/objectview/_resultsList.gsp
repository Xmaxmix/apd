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
<%--
We use this template to build the response for JavaScript Client. We do _not_
use it to render HTML in the server.
 --%>
<ul class="results-list unstyled">
  <g:set var="pageHitCounter" value="${0}"/>
  <g:each in="${results}">
    <g:set var="pageHitCounter" value="${pageHitCounter + 1}" />
    <g:set var="pagesize" value="${params.pagesize.toInteger()}" />
    <g:set var="hitNumber" value="${(offset.toInteger() * pagesize) + pageHitCounter}"/>
    <g:set var="controller" value="item" />
    <g:set var="action" value="${it.id}" />
    <g:if test="${it.category == 'Institution'}">
        <g:set var="controller" value="institution" />
        <g:set var="action" value="showInstitutionsTreeByItemId" />
    </g:if>
    <li class="item bt">
      <div class="summary row">
          <g:render template="summaryMainWrapper" model="${[item: it, action: action, controller: controller]}" />
          <g:render template="thumbnailWrapper" model="${[item: it, confBinary: confBinary, action: action, controller: controller]}" />
      </div>
    </li>
  </g:each>
</ul>
