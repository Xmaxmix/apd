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
          if(!node.bExpanded){
            $self.openTreeNode(node.data.key, treeDiv);
          }
          $self.showNodeDetails(node.data.key, detailView);
        },
        onExpand: function(expand, node) {
        },

      });
      
    },
    
    openTreeNode: function(institutionId, treeDiv) {
      var $self = this;
      
      if(institutionId != "rootnode"){

        var institutionsApiWrapper = new InstitutionsApiWrapper();
        institutionsApiWrapper.getStructureTreeNodeChildren(institutionId, function(data) {
          if(data){
            var childNodes = [];
            for(var i=0; i<data.length; i++) {
              
              var nodeTitle = "<div class='dynatree-apd-title'>" + data[i].label + "</div>";
              
              childNodes.push(
                {title: nodeTitle, 
                  key: data[i].id, 
                  isFolder: true, 
                  isLazy: true,
                  children: [{title:"<div class='dynatree-apd-title'>Loading...</div>", key: "empty"}]}
                );
            }
            $(treeDiv).dynatree("getTree").getNodeByKey(institutionId).removeChildren();
            $(treeDiv).dynatree("getTree").getNodeByKey(institutionId).addChild(childNodes);
            
          }else{
            $(treeDiv).dynatree("getTree").getNodeByKey(institutionId).removeChildren();
          }
        });
      }
    },

    showNodeDetails: function(institutionId, detailView) {
      var institutionsApiWrapper = new InstitutionsApiWrapper();
      
      var query = this.getUrlParam("search");

      var History = window.History;
      var urlParameters = "?query="+query+"&id="+institutionId;
      History.pushState("", document.title, decodeURI(urlParameters));

      institutionsApiWrapper.getStructureTreeNodeDetails(institutionId, query, function(data) {
        if(data){
          $(detailView).empty();
          $(detailView).append(data);
          mapSetup();
        }else{
          $(detailView).empty();
          $(detailView).append("No details found");
        }
      });
    },
    
    loadInitialTreeNodes: function(treeDiv) {
      
      var query = "*"

      var institutionsApiWrapper = new InstitutionsApiWrapper();
      institutionsApiWrapper.getStructureTreeRootNodes(query, function(data){
        
        if(data){
          var rootNodes = [];
          for(var i=0; i<data.institutions.length; i++) {
            
            var nodeTitle = "<div class='dynatree-apd-title'>" + data.institutions[i].name + "</div>";
            
            rootNodes.push(
              {title: nodeTitle, 
                key: data.institutions[i].id, 
                isFolder: true, 
                isLazy: true,
                children: [{title:"<div class='dynatree-apd-title'>Loading...</div>", key: "empty"}]}
              );
          }
          
          
          $(treeDiv).dynatree("getRoot").addChild(rootNodes);
          
          // Open root node
          //var rootNodeTreeElement = $(treeDiv).dynatree("getTree").getNodeByKey("rootnode");
          //rootNodeTreeElement.expand(true);
        }else{
          var rootNodes = [];
          $(treeDiv).dynatree("getRoot").addChild(rootNodes);
        }
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