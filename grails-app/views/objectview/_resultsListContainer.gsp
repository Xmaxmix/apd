<%--
We use this template to build the response for JavaScript Client. The JavaScript
client injects the HTML into the DOM to show the result list.
 --%>
<g:if test="${results}">
  <g:itemResultsRender results="${results}"></g:itemResultsRender>
</g:if>
