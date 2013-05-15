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

    institutionsApiWrapper: new InstitutionsApiWrapper(),
    
    init: function() {
    },

    buildInitialTree: function(treeDiv, detailView) {
      
      _canLog = false;
      
      // Initialize tree
      var $self = this;
      $(treeDiv).dynatree({
        onClick: function(node, event) {
          if(!node.bExpanded){
            $self.openTreeNode(node.data.key, treeDiv, 2);
          }
          $self.showNodeDetails(node.data.key, node.data.institution, treeDiv, detailView);
        },
        onExpand: function(expand, node) {
        },

      });
      
      var nodeId = this.getUrlParam("nodeId");
      if(nodeId){
        this.openPathToNode(nodeId);
      }
      
    },
    
    openTreeNode: function(institutionId, treeDiv, recursionDepth) {
      var $self = this;
      recursionDepth = recursionDepth - 1;
      
      if(institutionId != "rootnode"){

        this.institutionsApiWrapper.getStructureTreeNodeChildren(institutionId, function(data) {
          if(data){
            var childNodes = [];
            for(var i=0; i<data.length; i++) {
              
              var nodeTitle = "<div class='dynatree-apd-title'>" + data[i].label + "</div>";
              
              childNodes.push(
                {title: nodeTitle, 
                  key: data[i].id, 
                  isFolder: true, 
                  isLazy: true,
                  children: [{title:"<div class='dynatree-apd-title'>Loading...</div>", key: "empty"}],
                  isInstitution: data[i].institution}
                );
            }
            
            if(childNodes.length == 0){
              $(treeDiv).dynatree("getTree").getNodeByKey(institutionId).data.isFolder = false;
            }
            
            $(treeDiv).dynatree("getTree").getNodeByKey(institutionId).removeChildren();
            $(treeDiv).dynatree("getTree").getNodeByKey(institutionId).addChild(childNodes);
            
            if(recursionDepth > 0){
              for(var i=0; i<data.length; i++) {
                $self.openTreeNode(data[i].id, treeDiv, recursionDepth);
              }
            }
            
          }else{
            $(treeDiv).dynatree("getTree").getNodeByKey(institutionId).removeChildren();
          }
          
        });
      }
    },

    showNodeDetails: function(institutionId, isInstitution, treeDiv, detailView) {
      var query = this.getUrlParam("query");
      var isInstitution = $(treeDiv).dynatree("getTree").getNodeByKey(institutionId).data.isInstitution;

      if(institutionId != "rootnode"){
        var History = window.History;
        var urlParameters = "?query=" + query + 
                            "&nodeId=" + institutionId + 
                            "&isInstitution=" + isInstitution;
        History.pushState("", document.title, decodeURI(urlParameters));
      }
      
      var id = this.getUrlParam("nodeId");
      if(id != "" && id != "rootnode"){
        institutionId = id;
      }

      this.institutionsApiWrapper.getStructureTreeNodeDetails(institutionId, query, isInstitution, function(data) {
        $(detailView).empty();
        if(data){
          $(detailView).append(data);
          mapSetup();
        }else{
          $(detailView).append("Bitte wählen Sie ein Archiv");
        }
        
        var History = window.History;
        var urlParameters = "?query=" + query + 
                            "&nodeId=" + institutionId + 
                            "&isInstitution=" + isInstitution;
        History.pushState("", document.title, decodeURI(urlParameters));

      });
    },
    
    loadInitialTreeNodes: function(treeDiv) {
      var $self = this;
      
      var query = "*"

      this.institutionsApiWrapper.getStructureTreeRootNodes(query, function(data){
        
        if(data){
          var rootNodes = [];
          for(var i=0; i<data.institutions.length; i++) {
            
            var nodeTitle = "<div class='dynatree-apd-title'>" + data.institutions[i].name + "</div>";

            rootNodes.push(
              {title: nodeTitle, 
                key: data.institutions[i].id, 
                isFolder: true, 
                isLazy: true,
                children: [{title:"<div class='dynatree-apd-title'>Loading...</div>", key: "empty"}],
                isInstitution: data.institutions[i].institution}
              );
          }
          
          for(var i=0; i<data.institutions.length; i++) {
            if(data.institutions[i].id){
              $self.openTreeNode(data.institutions[i].id, treeDiv, 1);
            }
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
    
    openPathToNode: function(nodeId) {
      this.institutionsApiWrapper.getStructureTreeNodeParents(nodeId, function(data){
        if(data){
          for(var i=0; i<data.length; i++){
            console.log("############### "+data[i]);
          }            
        }
      });
    },
    
    getUrlParam: function(name){
      name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
      var regexS = "[\\?&]" + name + "=([^&#]*)";
      var regex = new RegExp(regexS);
      var results = regex.exec(window.location.search);
      if(results == null) {
        return "";
      }else{
        return decodeURIComponent(results[1].replace(/\+/g, " "));
      }
    },
  
  });
  
});
