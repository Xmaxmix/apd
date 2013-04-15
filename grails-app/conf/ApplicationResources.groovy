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
        resource url:'/css/binaries-viewer.css', bundle: 'screen'
        resource url:'/css/apd.css', bundle: 'screen'
        resource url:'/css/detailview.css', bundle: 'screen'
        resource url:'/css/structureview.css', bundle: 'screen'
        resource url:'/css/listview.css', bundle: 'screen'
        resource url:'/css/errors.css', bundle: 'screen'
    }

    cssprint {
        //resource url:'/css/apd.css', attrs:[media:'print'], bundle: 'print'
    }

    javascript {
        resource url:'/js/vendor/jquery-1.9.1.js'
        resource url:'/js/vendor/bootstrap.js'
        resource url:'/js/vendor/jwplayer/jwplayer.js'
        resource url:'/js/jwplayer-key.js'
        resource url:'/js/global-variables.js'
        resource url:'/js/binaries-viewer.js'
        resource url:'/js/navbar-manager.js'
    }
}

