<html>
  <head>
    <title><%-- <g:message code="ddbnext.Homepage"/> - --%><g:message code="apd.ArchivportalD"/></title>
    
    <meta name="page" content="advancedsearch" />
    <meta name="layout" content="main" />
    
  </head>
  <body>
    <!-- TODO: localize string -->
    <div id="advanced-search" class="row">
      <form class="form-inline">
        <fieldset>
          <label><g:message code="apd.fieldSearch" />: </label>
          <select>
            <option><g:message code="apd.signature" /></option>
            <option><g:message code="apd.title" /></option>
            <option><g:message code="apd.contain" /></option>
            <!-- TODO: translate vorprovenienz in message properties -->
            <option><g:message code="apd.vorprovenienz" /></option>
            <option><g:message code="apd.oldSignature" /></option>
            <option><g:message code="apd.runtime" /></option>
            <option><g:message code="apd.archievesType" /></option>
          </select>
          <input type="search" placeholder="<g:message code="apd.searchPlaceholder" />">
          <i class="icon-search contextual-help" data-content="<g:message code="apd.archievesType" />">
            contextual help
          </i>
        </fieldset>
        <fieldset>
          <label for="search-area">Suchbereich: </label>
          <input type="search" id="search-area">
        </fieldset>
        <fieldset>
          <!-- TODO: rethink about the IDs -->
          <label for="structure">Ergebnisse in Strukturansicht anzeigen
            <input type="checkbox" id="structure">
          </label>
          <label for="list">Ergebnisse in Strukturansicht anzeigen
            <input type="checkbox" id="list">
          <label>
        </fieldset>
      </form>
    </div>
  </body>
</html>
