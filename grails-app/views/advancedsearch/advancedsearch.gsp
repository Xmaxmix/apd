<html>
  <head>
    <title><%-- <g:message code="ddbnext.Homepage"/> - --%><g:message code="apd.ArchivportalD"/></title>
    
    <meta name="page" content="advancedsearch" />
    <meta name="layout" content="main" />
    
  </head>
  <body>
    <!-- TODO: localize string -->
    <div id="advanced-search" class="row">
      <form action="" class="form-inline">
        <fieldset>
          <label><g:message code="apd.fieldSearch" />: </label>
          <select>
            <option value="Title">Signatur</option>
            <option value="Title">Titel</option>
            <option value="Title">Enthaelt</option>
            <option value="Title">Vorprovenienz</option>
            <option value="Title">Altsignatur</option>
            <option value="Title">Laufzeit</option>
            <option value="Title">Archivalienart</option>
          </select>
          <input type="search" placeholder="Suchbegriff eingeben">
          <i class="icon-search contextual-help" data-content="Geben Sie Ihre 
          Suchbegriffe in die Suchfelder ein. Sie können mehrere Suchfelder 
          und Suchgruppen kombinieren. &lt;a href=&quot;/content/help/search
          -advanced&quot;&gt; Hilfe zur erweiterten Suche &lt;/a&gt;">
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
