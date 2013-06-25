
if ( typeof InstitutionItemMapController == 'undefined' ) {

    InstitutionItemMapController = (function () {

        var drawMap = function ( itemDiv, language, lon, lat, mapOptions ) {
        //  console.log("xxx mapOptions:");
        //  console.dir(mapOptions);

            var position =  { longitude: lon, latitude: lat };
            InstitutionItemMapModel.initialize(itemDiv, language, position, mapOptions);
        }

        return {
            drawMap: drawMap
        };

    })();
}
