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

package de.ddb.apd

import org.h2.command.ddl.CreateLinkedTable;

class ApdLinkTagLib {

    /**
     * Custom link taglib based on g:link but with some additional features.
     * 
     * @attr controller REQUIRED the controller
     * @attr action REQUIRED the action
     * @attr params REQUIRED the params object of the view
     * @attr addOrUpdate a map of new url parameters to add or to update
     * @attr remove a list of url parameters to remove from the url
     */
    def apdLink = { attrs, body ->

        def controller = attrs.controller
        def action = attrs.action
        if(!controller){
            controller = attrs.params["controller"]
        }
        if(!action){
            action = attrs.params["action"]
        }

        def params = attrs.params.clone()
        params.remove("controller")
        params.remove("action")

        def addOrUpdate = attrs.addOrUpdate
        def remove = attrs.remove
        attrs.remove("addOrUpdate")
        attrs.remove("remove")

        if(remove){
            if(remove.size() == 1 && remove.get(0) == "*") {
                params = [:]
            }
            for(int i=0; i<remove.size(); i++){
                params.remove(remove.get(i))
            }
        }

        if(addOrUpdate){
            params << addOrUpdate
        }

        attrs["params"] = params

        def applicationTagLib = grailsApplication.mainContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib')
        applicationTagLib.link.call(attrs, body)
    }

    /**
     * Custom link taglib based on g:createLink but with some additional features.
     * 
     * @attr controller REQUIRED the controller
     * @attr action REQUIRED the action
     * @attr params REQUIRED the params object of the view
     * @attr addOrUpdate a map of new url parameters to add or to update
     * @attr remove a list of url parameters to remove from the url
     */
    def createApdLink = { attrs, body ->
        def controller = attrs.controller
        def action = attrs.action
        if(!controller){
            controller = attrs.params["controller"]
        }
        if(!action){
            action = attrs.params["action"]
        }

        def params = attrs.params.clone()
        params.remove("controller")
        params.remove("action")

        def addOrUpdate = attrs.addOrUpdate
        def remove = attrs.remove
        attrs.remove("addOrUpdate")
        attrs.remove("remove")

        if(addOrUpdate){
            params << addOrUpdate
        }

        if(remove){
            for(int i=0; i<remove.size(); i++){
                params.remove(remove.get(i))
            }
        }

        attrs["params"] = params

        out << createLink("controller": controller, "action": action, "params":params)
    }
}
