<html>
  <head>
    <title><%-- <g:message code="ddbnext.Homepage"/> - --%><g:message code="apd.ArchivportalD"/></title>

    <meta name="page" content="advancedsearch" />
    <meta name="layout" content="main" />

  </head>
  <body>
    <div id="advanced-search" class="row">
      <form>
        <fieldset>
          <label><g:message code="apd.fieldSearch"/>: </label>
          <!-- first search row -->
          <g:render template="simpleSearchRow"/>
          <!-- second search row -->
          <g:render template="searchRow"/>
          <!-- third search row -->
          <g:render template="searchRow"/>
        </fieldset>
        <fieldset>
          <label for="search-area">Suchbereich: </label>
          <input type="search" id="search-area">
        </fieldset>
        <fieldset>
          <!-- TODO: rethink about the IDs -->
          <label for="structure"><g:message code="apd.structureView"/>
            <input type="checkbox" id="structure">
          </label>
          <label for="list"><g:message code="apd.listView"/>
            <input type="checkbox" id="list">
          <label>
        </fieldset>
      </form>
    </div>
  </body>
</html>
