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
  
  StructureTreeWrapper = function(){
    this.init();
  }

  $.extend(StructureTreeWrapper.prototype, {

    init: function() {
    },

    buildInitialTree: function(treeDiv, detailView) {
      
      _canLog = false;
      
      // Initialize tree
      var $self = this;
      $(treeDiv).dynatree({
        onClick: function(node, event) {
          console.log(node);
          $self.clickOnInstitution(node.data.key, treeDiv, detailView);
        },
        onExpand: function(expand, node) {
          $self.openTreeNode(node.data.key);
        },

      });
      
    },
    
    clickOnInstitution: function(institutionId, treeDiv, detailView) {
      console.log("####################4 clickOnInstitution: "+institutionId);
      this.showNodeDetails(institutionId, detailView);
      this.openTreeNode(institutionId, treeDiv, detailView);
    },
    
    openTreeNode: function(institutionId, treeDiv, detailView) {
      console.log("####################4 openTreeNode "+institutionId);
      if(institutionId != "rootnode"){
        var institutionsApiWrapper = new InstitutionsApiWrapper();
        institutionsApiWrapper.getStructureTreeNodeChildren(institutionId, function(data) {
          console.log("####################4 child: "+data);
          var childNodes = [];
          for(var i=0; i<data.length; i++) {
            childNodes.push(
              {title: data[i].label, 
                key: data[i].id, 
                isFolder: true, 
                isLazy: true, 
                children: null }
              );
          }
          console.log("####################4 showNodeDetails "+ $(treeDiv).dynatree("getTree").getNodeByKey(institutionId));
          $(treeDiv).dynatree("getTree").getNodeByKey(institutionId).addChild(childNodes);
          
        });
      }
    },

    showNodeDetails: function(institutionId, detailView) {
      console.log("####################4 showNodeDetails "+institutionId);
      var institutionsApiWrapper = new InstitutionsApiWrapper();
      
      var query = this.getUrlParam("search");
      
      console.log("####################4 showNodeDetails "+query);
      institutionsApiWrapper.getStructureTreeNodeDetails(institutionId, query, function(data) {
        console.log("####################4 showNodeDetails append to "+detailView);
        console.log(data);
        $(detailView).empty();
        $(detailView).append(data);
        mapSetup();
      });
    },
    
    loadInitialTreeNodes: function(treeDiv) {
      console.log("####################4 loadInitialTreeNodes "+treeDiv);
      
      var query = this.getUrlParam("search");

      var institutionsApiWrapper = new InstitutionsApiWrapper();
      institutionsApiWrapper.getStructureTreeRootNodes(query, function(data){
        
        var rootNodes = [];
        for(var i=0; i<data.institutions.length; i++) {
          rootNodes.push(
            {title: data.institutions[i].name, 
              key: data.institutions[i].id, 
              isFolder: true, 
              isLazy: true, 
              children: [{title:"", key: "empty"}] }
            );
        }
        
        
        $(treeDiv).dynatree("getRoot").addChild(rootNodes);
        
        // Open root node
        //var rootNodeTreeElement = $(treeDiv).dynatree("getTree").getNodeByKey("rootnode");
        //rootNodeTreeElement.expand(true);
        
      });
    },
    
    getUrlParam: function(name){
      var results = new RegExp('[\\?&amp;]' + name + '=([^&amp;#]*)').exec(window.location.href);
      if(!results){
        return ""
      }
      return results[1] || 0;
    },
  
  });
  
});