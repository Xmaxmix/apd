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


class ApdLinkTagLib {

    /**
     * Custom link taglib based on g:link but with some additional features.
     * 
     * @attr controller REQUIRED the controller
     * @attr action the action
     * @attr params REQUIRED the params object of the view
     * @attr addOrUpdate a map of new url parameters to add or to update
     * @attr remove a list of url parameters to remove from the url
     */
    def apdLink = { attrs, body ->

        attrs = handleTagAttributes(attrs)

        def applicationTagLib = grailsApplication.mainContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib')
        applicationTagLib.link.call(attrs, body)
    }

    /**
     * Custom link taglib based on g:createLink but with some additional features.
     * 
     * @attr controller REQUIRED the controller
     * @attr action the action
     * @attr params REQUIRED the params object of the view
     * @attr addOrUpdate a map of new url parameters to add or to update
     * @attr remove a list of url parameters to remove from the url
     */
    def createApdLink = { attrs, body ->

        def controller = attrs.controller
        def action = attrs.action

        attrs = handleTagAttributes(attrs)

        out << createLink("controller": controller, "action": action, "params":attrs.params)
    }

    /**
     * Custom link taglib based on g:form but with some additional features.
     * 
     * @attr controller REQUIRED the controller
     * @attr action the action
     * @attr params REQUIRED the params object of the view
     * @attr addOrUpdate a map of new url parameters to add or to update
     * @attr remove a list of url parameters to remove from the url
     */
    def apdForm = { attrs, body ->

        attrs = handleTagAttributes(attrs)

        def applicationTagLib = grailsApplication.mainContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib')
        applicationTagLib.form.call(attrs, body)
    }

    private def handleTagAttributes(attrs) {

        def controller = attrs.controller
        def action = attrs.action

        // If no controller explicitly set -> take the controller of the current request (aka view)
        if(!controller){
            controller = attrs.params["controller"]
        }

        // Clone the params so the current request will not be influenced
        def params = attrs.params.clone()

        // Remove the controller/action from the params, they are not needed there
        params.remove("controller")
        params.remove("action")

        def addOrUpdate = attrs.addOrUpdate
        def remove = attrs.remove

        // Remove the addOrUpdate/remove values from the attrs list, they are needed to manipulate the params values
        attrs.remove("addOrUpdate")
        attrs.remove("remove")

        // If attribute "remove" is set, remove all the parameters in the list. If only "*" is set -> remove all
        if(remove){
            if(remove.size() == 1 && remove.get(0) == "*") {
                params = [:]
            }
            for(int i=0; i<remove.size(); i++){
                params.remove(remove.get(i))
            }
        }

        // If attribute "addOrUpdate" is set, add or update all the parameters in the map
        if(addOrUpdate){
            params << addOrUpdate
        }

        // update the manipulated params-map in the attributes
        attrs["params"] = params

        return attrs
    }
}
