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

$(document).ready(function () {
  if(jsPageName == "structureview") {
		
	$('#institution-tree').jstree({ 
		"themes" : {
			"theme" : "classic",
			"dots" : true,
			"icons" : true
		},
		"plugins" : [ "themes", "html_data" ]
	});	  
	  
    var apiWrapper = new InstitutionsApiWrapper();
	  

    // Build the tree
    function printInstitutionsList(json){
      // TODO build actual tree
      $("#institution-tree").empty();
      $.each(json, function(i, field){
        $("#institution-tree").append(i + " / " + field.name + " <br />");
      });
    }

    // When page loads -> load the data and build tree
//    apiWrapper.getFullInstitutionsList(function(json){
//      printInstitutionsList(json);
//    });		
  
  }
});