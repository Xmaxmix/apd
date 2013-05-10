<div class="sibling-navigation">
  <div>
    <g:message code="apd.Sibling_Navigation"/>    
  </div>

  <div class="sibling-previous">
    <g:if test="${siblingInformation.previous}">
      <g:link url="${siblingInformation.previous.id}"><g:img dir="images/icons" file="nav-left-active.png"/></g:link>
    </g:if>
    <g:else>
      <g:img dir="images/icons" file="nav-left-inactive.png"/>
    </g:else>
  </div>

  <div class="sibling-current">
    <form >
      <p>
      <select name="id" size="1" id="siblings-dropdown">
        <g:each in="${siblingInformation.siblings}">
          <g:if test="${itemId == it.id}">
            <option value="${it.id}" selected="selected">${it.label}</option>
          </g:if>
          <g:else>
            <option value="${it.id}">${it.label}</option>
          </g:else>
        </g:each>
      </select>
      <p>
    </form>
  </div>

  <div class="sibling-next">
    <g:if test="${siblingInformation.next}">
      <g:link url="${siblingInformation.next.id}"><g:img dir="images/icons" file="nav-right-active.png"/></g:link>
    </g:if>
    <g:else>
      <g:img dir="images/icons" file="nav-right-inactive.png"/>
    </g:else>
  </div>

</div>

