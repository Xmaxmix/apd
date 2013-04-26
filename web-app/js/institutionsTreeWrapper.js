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

$(function() {
  
  InstitutionsTreeWrapper = function(){
    this.init();
  }

  $.extend(InstitutionsTreeWrapper.prototype, {

    init: function() {
    },

    buildInitialTree: function(treeDiv, detailView) {
      
      _canLog = false;
      
      // Initialize tree
      var $self = this;
      $(treeDiv).dynatree({
        onClick: function(node, event) {
          console.log(node);
          $self.showNodeDetails(node.data.key, detailView);
        },
        onExpand: function(expand, node) {
          $self.openTreeNode(node.data.key);
        },

      });
      
      
      // Open root node
      var rootNodeTreeElement = $(treeDiv).dynatree("getTree").getNodeByKey("_2");
      rootNodeTreeElement.expand(true);
       
    },
    
//    registerClickHandlers: function(rootElement, detailView) {
//      var links = $(rootElement + " a");
//      for(var i=0; i<links.size(); i++) {
//        var linkElement = links.get(i);
//        console.log("####################2 registerClickHandlers "+linkElement.id);
//        var institutionId = linkElement.id;
//        $(linkElement).click(function(event) {
//          console.log(event);
//          var institutionsTreeWrapper = new InstitutionsTreeWrapper();
//          institutionsTreeWrapper.clickOnInstitution(event.target.id, detailView);
//        });
//        
//      }
//    },
    
    clickOnInstitution: function(institutionId, detailView) {
      this.showNodeDetails(institutionId, detailView);
      this.openTreeNode(institutionId, detailView);
    },
    
    openTreeNode: function(institutionId) {
      var institutionsApiWrapper = new InstitutionsApiWrapper();
      institutionsApiWrapper.getObjectTreeNodeChildren(institutionId, function(data) {
        console.log(data);
      });
    },

    showNodeDetails: function(institutionId, detailView) {
      console.log("####################2 showNodeDetails "+institutionId);
      var institutionsApiWrapper = new InstitutionsApiWrapper();
      
      var query = "gutenberg";
      var offset = 0;
      var pagesize = 20;
      
      institutionsApiWrapper.getObjectTreeNodeDetails(institutionId, query, offset, pagesize, function(data) {
        console.log("####################2 showNodeDetails append to "+detailView);
        $(detailView).empty();
        $(detailView).append(data);
      });
    },
  
  });
  
});