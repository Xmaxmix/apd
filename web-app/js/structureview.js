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
    
    var treeElement = $("#institution-tree");
    var detailViewElement = $(".institution-item-details");
    
    if ($('.search-widget-container').length > 0) {
      var searchWidgetContainer = $('#search-widget');
      searchWidget = new SearchWidget($('#search-widget-form'),searchWidgetContainer, searchWidgetContainer.find('.controls-container'));
    }
    
    var structureTreeManager = new TreeManager(treeElement, detailViewElement, true);
    console.log(structureTreeManager)
      //structureTreeWrapper.showNodeDetails('rootnode', '#institution-tree', '.institution-item-details');
    
    $(window).bind('statechange', function(e) {
        var nodeId = getUrlParam("nodeId");
        if(nodeId){
          structureTreeManager.openPathToNode(nodeId, treeElement, detailViewElement);
        }else{
          //TODO Create a method for this
          detailViewElement.empty();
          //TODO Localize text
          detailViewElement.html("<h1>Bitte wählen Sie ein Archiv</h1>");
        }
    });
  }
});
