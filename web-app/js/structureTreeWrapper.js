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
    initialized: false,
    loadInitialTreeNodesStack: 0,
    loadPathToNodeStack: 0,
    
    init: function() {
    },

    buildInitialTree: function(treeDiv, detailView, initializedCallback) {
      
      _canLog = false;
      
      // Initialize tree
      var $self = this;
      $(treeDiv).dynatree({
        onClick: function(node, event) {
          if(!node.bExpanded){
            $self.openTreeNode(node.data.key, treeDiv, 2, function(){}, {'open':0}, function(openCalls){});
          }
          $self.showNodeDetails(node.data.key, node.data.institution, treeDiv, detailView);
        },
        onExpand: function(expand, node) {
        },
        onPostInit: function(isReloading, isError){
          if(!isReloading){
            $self.initialized = true;
            initializedCallback();
          }
        },

      });
      
    },
    
    openTreeNode: function(institutionId, treeDiv, recursionDepth, initialTreeNodesLoadedCallback, openCalls, isOpenedCallback) {
      var $self = this;
      recursionDepth = recursionDepth - 1;
      $self.loadInitialTreeNodesStack ++;
      openCalls.open = openCalls.open + 1;
      
      if(institutionId != "rootnode"){

        this.institutionsApiWrapper.getStructureTreeNodeChildren(institutionId, function(data) {
          var node = $(treeDiv).dynatree("getTree").getNodeByKey(institutionId);
          if(data){
            var childNodes = [];
            for(var i=0; i<data.length; i++) {
              
              var nodeTitle = "<div class='dynatree-apd-title' id='" + data[i].id + "'>" + data[i].label + "</div>";
              
              childNodes.push(
                {title: nodeTitle, 
                  key: data[i].id, 
                  isFolder: true, 
                  isLazy: false,
                  children: [{title:"<div class='dynatree-apd-title'>Loading...</div>", key: "empty"}],
                  isInstitution: data[i].institution}
                );
            }
            
            if(childNodes.length == 0){
              node.data.isFolder = false;
            }
            
            node.removeChildren();
            node.addChild(childNodes);
            
            if(recursionDepth > 0){
              for(var i=0; i<data.length; i++) {
                $self.openTreeNode(data[i].id, treeDiv, recursionDepth, initialTreeNodesLoadedCallback, openCalls, isOpenedCallback);
              }
            }
            
          }else{
            node.removeChildren();
          }
          
          $self.loadInitialTreeNodesStack --;
          if($self.loadInitialTreeNodesStack == 0){
            initialTreeNodesLoadedCallback();
          }
          
          openCalls.open = openCalls.open - 1;
          
          isOpenedCallback(openCalls);

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
    
    loadInitialTreeNodes: function(treeDiv, initialTreeNodesLoadedCallback) {
      var $self = this;
      $self.loadInitialTreeNodesStack = 0;
      $self.loadInitialTreeNodesStack ++;
      
      var query = "*"

      $self.loadInitialTreeNodesStack ++;

      this.institutionsApiWrapper.getStructureTreeRootNodes(query, function(data){
        
        if(data){
          var rootNodes = [];
          for(var i=0; i<data.institutions.length; i++) {
            
            var nodeTitle = "<div class='dynatree-apd-title' id='" + data.institutions[i].id + "'>" + data.institutions[i].name + "</div>";

            rootNodes.push(
              {title: nodeTitle, 
                key: data.institutions[i].id, 
                isFolder: true, 
                isLazy: false,
                children: [{title:"<div class='dynatree-apd-title'>Loading...</div>", key: "empty"}],
                isInstitution: data.institutions[i].institution}
              );
          }
          
          for(var i=0; i<data.institutions.length; i++) {
            if(data.institutions[i].id){
              $self.openTreeNode(data.institutions[i].id, treeDiv, 1, initialTreeNodesLoadedCallback, {'open':0}, function(openCalls){});
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
        
        $self.loadInitialTreeNodesStack --;
        if($self.loadInitialTreeNodesStack == 0){
          initialTreeNodesLoadedCallback();
        }
      });
      
      $self.loadInitialTreeNodesStack --;
      if($self.loadInitialTreeNodesStack == 0){
        initialTreeNodesLoadedCallback();
      }
    },
    
    openPathToNode: function(nodeId, treeDiv, detailView) {
      var $self = this;
      
      this.institutionsApiWrapper.getStructureTreeNodeParents(nodeId, function(data){
        if(data){
          
          function openNode(nodeList, treeDiv){
            if(nodeList.length > 0){
              var currentNode = nodeList[nodeList.length-1];
              nodeList.splice(nodeList.length - 1, nodeList.length);
              
              $self.openTreeNode(currentNode.id, treeDiv, 1, function(){}, {'open':0}, function(openCalls){
                if(openCalls['open'] == 0){

                  var node = $(treeDiv).dynatree("getTree").getNodeByKey(currentNode.id);
                  if(node){
                    node.expand(true);
                  }
                
                  
                  openNode(nodeList, treeDiv);
                
                }

              });
              
            }else{ // Last node
              var node = $(treeDiv).dynatree("getTree").getNodeByKey(nodeId);
              node.select(true);
              
              $self.showNodeDetails(node.data.key, node.data.institution, treeDiv, detailView);
              
              var nodeAnchor = document.getElementById(nodeId);
              nodeAnchor.scrollIntoView();
            }
          }
          
          openNode(data, treeDiv);            
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
