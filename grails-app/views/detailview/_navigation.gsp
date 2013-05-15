<div class="sibling-navigation">
  <div class="nav-text">
    <g:message code="apd.Sibling_Navigation"/>    
  </div>

  <div class="sibling-previous">
    <g:if test="${siblingInformation.previous}">
      <%-- 
      <g:link url="${siblingInformation.previous.id}"><div class="nav-button left-active" ></div></g:link>
      --%>
      <g:link controller="detailview" action="index" params="[id: siblingInformation.previous.id, 'query': params.query, 'offset': navData.newOffset, 'pagesize': navData.pagesize, 'sort': params.sort, 'nodeId': params.nodeId, 'hitNumber': navData.hitNumber]">
        <div class="nav-button left-active" ></div>
      </g:link>
    </g:if>
    <g:else>
      <div class="nav-button left-inactive" ></div>
    </g:else>
  </div>

  <div class="sibling-current">
    <form >
      <p>
      <select name="id" size="1" id="siblings-dropdown">
        <g:each in="${siblingInformation.siblings}">
          <g:if test="${itemId == it.id}">
            <option value="${createLink(controller: "detailview", action: "index", params: ['id': siblingInformation.previous.id, 'query': params.query, 'offset': navData.newOffset, 'pagesize': navData.pagesize, 'sort': params.sort, 'nodeId': params.nodeId, 'hitNumber': navData.hitNumber])}" selected="selected">${it.label}</option>
          </g:if>
          <g:else>
            <option value="${createLink(controller: "detailview", action: "index", params: ['id': siblingInformation.previous.id, 'query': params.query, 'offset': navData.newOffset, 'pagesize': navData.pagesize, 'sort': params.sort, 'nodeId': params.nodeId, 'hitNumber': navData.hitNumber])}">${it.label}</option>
          </g:else>
        </g:each>
      </select>
      <p>
    </form>
  </div>

  <div class="sibling-next">
    <g:if test="${siblingInformation.next}">
      <%-- 
      <g:link url="${siblingInformation.next.id}"><div class="nav-button right-active" ></div></g:link>
      --%>
      <g:link controller="detailview" action="index" params="[id: siblingInformation.next.id, 'query': params.query, 'offset': navData.newOffset, 'pagesize': navData.pagesize, 'sort': params.sort, 'nodeId': params.nodeId, 'hitNumber': navData.hitNumber]">
        <div class="nav-button right-active" ></div>
      </g:link>
    </g:if>
    <g:else>
      <div class="nav-button right-inactive" ></div>
    </g:else>
  </div>

</div>

