
if ( typeof InstitutionItemMapModel == 'undefined' ) {

    InstitutionItemMapModel = (function () {

        var _initialized = false;

        var _allMapData; // constant, a flattened tree

        var _sectorSelection;   // as last published on InstitutionsSectors
        var _sectorsMapData; // the data to display depending on selected sectors
        var _allSectors; // count and name of sectors selected

        var _mapWidget;
        var _detailedViewSpec;

        var f_isInitialized = function() {
            return _initialized;
        }

        var f_initialize = function (mapDivId, language, position, startupOptions) {

            GeoTemConfig.determineAndSetUrlPrefix("InstitutionItemMapModel.js");

            if (typeof console != 'undefined') {
             // console.log("f_initialize mapDivId=" + mapDivId + " language=" + language);
             // console.dir(position);
             // console.dir(startupOptions);
             // console.log("GeoTemConfig.urlPrefix=" + GeoTemConfig.urlPrefix);
            }

            GeoTemConfig.language = language;
            GeoTemConfig.allowFilter = false;

            var mapDiv = document.getElementById(mapDivId);
            _detailedViewSpec = {
                        longitude: position.longitude,
                        latitude: position.latitude,
                        zoomLevel: 15,
                        marker: 'ddb_marker_final.png',
                        markerWidth: 38,
                        markerHeight: 45
                    };

            var map = new WidgetWrapper();

            _mapWidget = new MapWidget(map, mapDiv, $.extend( {
                mapTitle: GeoTemConfig.getString('institutes'),
                popups: false,
		        mapHeight : false, // '240px', // false or desired height css definition for the map
		        showDescriptions : false,
		        olNavigation : false, // show/hide OpenLayers navigation panel
                mapSelectionTools : false, // show/hide map selector tools
                labelGrid: false, // enables, disables hove
                countrySelect: false,
                resetMap: true,
                mapSelection: false, // disable background map selection
                boundaries: false,
                maxPlaceLabels: 0, // unlimited
                geoLocation: false,
                detailedViewSpec: _detailedViewSpec
            },startupOptions) );

            if (typeof console != 'undefined') {
                console.log("MapWidget displayed");
            }

            _initialized = true;

            $('.absoluteToolbar').css("display","none");

            f_displayPosition();
            return map;
        };

        var f_displayPosition = function() {
            _mapWidget.centerAndMarkPosition();
        }

        var f_getUrlPrefix = function() {
            return GeoTemConfig.urlPrefix;
        }

        return {
            initialize: f_initialize,
            isInitialized: f_isInitialized,
            displayPosition: f_displayPosition,
            getUrlPrefix: f_getUrlPrefix
        };
    })();
}

