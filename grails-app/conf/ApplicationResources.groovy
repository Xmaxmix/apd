/*
 * Copyright (C) 2013 FIZ Karlsruhe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

modules = {
    apd {
        defaultBundle 'apd'
        dependsOn "images, cssscreen, cssprint, javascript"
    }

    images {  resource url:'/images/favicon.ico' }

    cssscreen {
        resource url:'/css/vendor/bootstrap/css/bootstrap.css', bundle: 'screen'
        resource url:'/css/apd.css', bundle: 'screen'
        //resource url:'/css/vendor/fuelux/fuelux.css', bundle: 'screen'
        //resource url:'/css/vendor/fuelux/fuelux-responsive.css', bundle: 'screen'
        resource url:'/css/apd.css', bundle: 'screen'
        resource url:'/css/index.css', bundle: 'screen'
        resource url:'/css/binaries-viewer.css', bundle: 'screen'
        resource url:'/css/detailview.css', bundle: 'screen'
        resource url:'/css/structureview.css', bundle: 'screen'
        resource url:'/css/objectview.css', bundle: 'screen'
        resource url:'/css/dynatree-apd.css', bundle: 'screen'
        resource url:'/css/vendor/dynatree/skin/ui.dynatree.css', bundle: 'screen'
        resource url:'/css/errors.css', bundle: 'screen'
        resource url:'/css/advancedsearch.css', bundle: 'screen'
        resource url:'/css/searchwidget.css', bundle: 'screen'
        resource url:'/third-party/map/css/style.css', bundle: 'screen'
        resource url:'/css/result.css', bundle: 'screen'
        resource url:'/css/modalDialog.css', bundle: 'screen'
    }

    cssprint {
        //resource url:'/css/apd.css', attrs:[media:'print'], bundle: 'print'
    }

    javascript {
        resource url:'/js/vendor/jquery.js'
        resource url:'/js/vendor/jquery.ui.js'
        resource url:'/js/vendor/bootstrap.js'
        resource url:'/js/vendor/fuelux/require.js'
        resource url:'/js/vendor/fuelux/loader.min.js'
        resource url:'/js/vendor/fuelux/pillbox.js'
        resource url:'/js/vendor/jwplayer/jwplayer.js'
        resource url:'/js/vendor/jquery.cookies.2.2.0.min.js'
        resource url:'/js/vendor/large-cookie.js'
        resource url:'/js/vendor/dynatree.js'
        resource url:'/js/vendor/history.js/scripts/bundled/html4+html5/jquery.history.js'
        resource url:'/js/vendor/jquery-queryParser.min.js'
        resource url:'/js/jwplayer-key.js'
        resource url:'/js/global-variables.js'
        resource url:'/js/binaries-viewer.js'
        resource url:'/js/navbar-manager.js'
        resource url:'/js/persistent-links-modal-dialog.js'
        resource url:'/js/institutionsApiWrapper.js'
        resource url:'/js/pagination.js'
        resource url:'/js/objectDetailWrapper.js'
        resource url:'/js/objectTreeWrapper.js'
        resource url:'/js/structureTreeWrapper.js'
        resource url:'/js/searchwidget.js'
        resource url:'/js/index.js'
        resource url:'/js/structureview.js'
        resource url:'/js/objectview.js'
        resource url:'/js/tooltip.js'
        resource url:'/js/advanced-search.js'
    }

    structureview {
        resource url:'/js/InstitutionsMapAdapter.js'
        resource url:'/third-party/map/geotemco_InstitutionItemMap.js'
    }
}
