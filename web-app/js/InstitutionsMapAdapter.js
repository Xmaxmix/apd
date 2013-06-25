/**
 * built according to "Revealing Module Pattern (Public & Private)"
 * http://enterprisejquery.com/2010/10/how-good-c-habits-can-encourage-bad-javascript-habits-part-1/
 */
//URI to institutions-data
var INSTITUTIONS_MAP_REF = '/apis/institutionsmap';

//Directory where map-application is located
var MAP_DIR = '/third-party/map/';

//name of page where map for 1 institution is written in
var INSTITUTION_PAGE_NAME = 'structureview';

//div where map for 1 institution is written in
var INSTITUTION_DIV = 'divOSM';

//only initialize map once, then remember in this variable
var mapInitialized = false;

var InstitutionsMapAdapter = (function ( $, undefined ) {

    //var osmTileServer = "openstreetmap.org";
    //var osmTileServer = "opencyclemap.org/cycle";
    //var osmTileServer = "maps.deutsche-digitale-bibliothek.de";

    var osmTileServer = "openstreetmap.org";
    var osmTileset = [ "http://a.tile." + osmTileServer + "/${z}/${x}/${y}.png",
                       "http://b.tile." + osmTileServer + "/${z}/${x}/${y}.png",
                       "http://c.tile." + osmTileServer + "/${z}/${x}/${y}.png" ];

    
    var institutionsMapOptions = {
        resetMap: true,
        mapHeight: false,
        mapWidth: false,
        osmTileset: osmTileset
    };

    var institutionMapOptions = {
        osmTileset: osmTileset
    };

    var Public = {}; // for public properties. avoid the reserved keyword "public"

    Public.drawInstitution = function ( mapDiv, lang, lon, lat) {
        if (typeof console != "undefined") {
            console.log("startup");
        }

        InstitutionItemMapController.drawMap( mapDiv, lang, lon, lat, institutionMapOptions );
    };

    Public.selectSectors = function () {
        var sectors = _getSectorSelection();
        InstitutionsMapController.selectSectors(sectors);
    };

    var _getSectorSelection = function () {
        var sectors = {};
        sectors["selected"] = [];
        sectors["deselected"] = [];
        $('.sector-facet').each(function () {
            var sectorData = {};
            sectorData["sector"] = $(this).find('input').data('sector');
            sectorData["name"] = $.trim($(this).children('label').text());
            if ($(this).find('input').is(":checked")) {
                sectors["selected"].push(sectorData);
            } else {
                sectors["deselected"].push(sectorData);
            }
        });
        return sectors;
    }


    Public.fetchAllInstitutions = function ( successFn ) {
        _fetchDataAjax(INSTITUTIONS_MAP_REF + '?clusterid=-1', successFn);
    };

    var _fetchDataAjax = function (a_url,a_successFn) {
        $.ajax({
            type : 'GET',
            dataType : 'json',
            async : true,
            url : a_url,
            success : a_successFn
        });
    };
    
    var _enableListView = function() {
        $('#institution-map').addClass('off');
        $('#institution-list').removeClass('off');

        $('.view-type-switch').removeClass('off');
        $('#first-letter-index').removeClass('off');

        $('#view-institution-list').addClass('selected');
        $('#view-institution-map').removeClass('selected');

        $('#main-container').removeClass('map');
        $('#main-container').addClass('list');
    }

    var _enableMapView = function() {
        $('#institution-list').addClass('off');
        $('#institution-map').removeClass('off');

        $('.view-type-switch').removeClass('off');
        $('#first-letter-index').addClass('off');
        
        $('#view-institution-map').addClass('selected');
        $('#view-institution-list').removeClass('selected');

        $('#main-container').addClass('map');
        $('#main-container').removeClass('list');
        if (!mapInitialized) {
            InstitutionsMapController.startup(INSTITUTIONLIST_DIV, jsLanguage, institutionsMapOptions);
            mapInitialized = true;
        }
    }

    Public.setupDom4MapDisplay = function () {

        var hash = window.location.hash.substring(1);
        if (typeof console != "undefined") {
            console.log("Anchor-tag: '"+hash+"'");
        }
    	if(hash === "list"){
    		_enableListView();
    	}else{
    		_enableMapView();
    	}    	

        $('#view-institution-list').click(function () {
    		_enableListView();
        });

        $('#view-institution-map').click(function () {
    		_enableMapView();
        });

        $('input:checkbox').click(function () {
            Public.selectSectors();
        });
    };

    return Public;

})(jQuery);

function mapSetup() {
    INSTITUTIONS_MAP_REF = jsContextPath + INSTITUTIONS_MAP_REF;
    GeoTemCoMinifier_urlPrefix = window.document.location.protocol + "//" + window.document.location.host + jsContextPath + MAP_DIR;
    jsLongitude = $("div.location").attr('data-lon');
    jsLatitude = $("div.location").attr('data-lat');
    if ($("#divOSM").length > 0){
        InstitutionsMapAdapter.drawInstitution(INSTITUTION_DIV, jsLanguage, jsLongitude, jsLatitude);
        //TODO move the pushing state directly on the tree's elements on click event
//        History.pushState({state:1}, $('#institution-name').attr('data-id'), "/apd/struktur/item/"+$('#institution-name').attr('data-id'));
    }
    return;
};

$(document).ready(function() {
    mapSetup();
});
