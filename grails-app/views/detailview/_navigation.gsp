<div class="sibling-navigation">
  <div class="nav-text">
    <g:message code="apd.Sibling_Navigation"/>    
  </div>

  <div class="sibling-previous">
    <g:if test="${siblingInformation.previous}">
      <g:apdLink controller="detailview" params="${params}" addOrUpdate="${[id: siblingInformation.previous.id] }">
        <div class="nav-button left-active" ></div>
      </g:apdLink>
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
          <option value="<g:createApdLink controller="detailview" params="${params}" addOrUpdate="${['id': it.id]}"/>" 
              <g:if test="${itemId == it.id}">selected="selected"</g:if>>
                ${it.label}
          </option>
        </g:each>
      </select>
      <p>
    </form>
  </div>

  <div class="sibling-next">
    <g:if test="${siblingInformation.next}">
      <g:apdLink controller="detailview" params="${params}" addOrUpdate="${[id: siblingInformation.next.id] }">
        <div class="nav-button right-active" ></div>
      </g:apdLink>
    </g:if>
    <g:else>
      <div class="nav-button right-inactive" ></div>
    </g:else>
  </div>

</div>

