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

$(function(){
  TreeManager = function(treeElement, detailViewElement, isStructure){
    this.isStructure = isStructure;
    this.buildTree(treeElement, detailViewElement);
  }
  
  $.extend(TreeManager.prototype, {
    
    objectContentManager: null,
    institutionsApiWrapper: new InstitutionsApiWrapper(),
    initialized: false,
    loadInitialTreeNodesStack: 0,
    loadPathToNodeStack: 0,
    isStructure: false,
    
    buildTree: function(treeElement, detailViewElement){
      
      _canLog = false;
      
      // Initialize tree
      var $self = this;
      if(!this.isStructure){
        this.objectContentManager = new ContentManager();
      }
      treeElement.dynatree({
        onClick: function(node, event) {
          if(!node.bExpanded){
            //We are setting empty parameters to avoid recursion for the auto-initialization of
            //the tree starting from an itemId in the URL
            var openCalls = {'open':0};
            var emptyFunction = function(){};
            $self.openTreeNode(node.data.key, treeElement, 2, emptyFunction, openCalls, emptyFunction);
          }
          if($self.isStructure){
            $self.showStructureNodeDetails(node.data.key, treeElement, detailViewElement, node.data.institution);
          }else{
            $self.objectContentManager.showNodeDetails(node.data.key, detailViewElement, node.data.numberOfItems);
          }
        },
        onExpand: function(expand, node) {
        },
        onPostInit: function(isReloading, isError){
          if(!isReloading){
            $self.loadInitialTreeNodes(treeElement, function(){
              var nodeId = getUrlParam("nodeId");
              if(nodeId){
                $self.openPathToNode(nodeId, treeElement, detailViewElement);
              }
            });
          }
          $self.initialized = true;
        }
      });
      //TODO Remember to remove it and check the duplicated calls on the openPathToNode in the Objecttree after a search
//      this.loadInitialTreeNodes(treeElement, function(){
//        var nodeId = getUrlParam("nodeId");
//        if(nodeId){
//          $self.openPathToNode(nodeId, treeElement, detailViewElement);
//        }
//      } );
    },
    
    openTreeNode: function(institutionId, treeElement, recursionDepth, initialTreeNodesLoadedCallback, openCalls, isOpenedCallback){
      if(this.isStructure){
        this.openStructureTreeNode(institutionId, treeElement, recursionDepth, initialTreeNodesLoadedCallback, openCalls, isOpenedCallback);
      }else{
        this.openObjectTreeNode(institutionId, treeElement, recursionDepth, initialTreeNodesLoadedCallback, openCalls, isOpenedCallback);
      }
    },
    
    openStructureTreeNode: function(institutionId, treeElement, recursionDepth, initialTreeNodesLoadedCallback, openCalls, isOpenedCallback){
      
      var $self = this;
      recursionDepth = recursionDepth - 1;
      $self.loadInitialTreeNodesStack ++;
      openCalls.open = openCalls.open + 1;
      
      if(institutionId != "rootnode"){

        this.institutionsApiWrapper.getStructureTreeNodeChildren(institutionId, function(data) {
          var node = treeElement.dynatree("getTree").getNodeByKey(institutionId);
          if(data){
            var childNodes = [];
            for(var i=0; i<data.length; i++) {
              
              var nodeTitle = "<div class='dynatree-apd-title' id='" + data[i].id + "'>" + data[i].label + "</div>";
              
              $self.createAndPushNode(nodeTitle, childNodes, data[i].id, true, false, data[i].institution);
            }
            
            if(childNodes.length == 0){
              node.data.isFolder = false;
            }
            
            node.removeChildren();
            node.addChild(childNodes);
            
            if(recursionDepth > 0){
              for(var i=0; i<data.length; i++) {
                $self.openTreeNode(data[i].id, treeElement, recursionDepth, initialTreeNodesLoadedCallback, openCalls, isOpenedCallback);
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
    
    openObjectTreeNode: function(institutionId, treeElement, recursionDepth, initialTreeNodesLoadedCallback, openCalls, isOpenedCallback){
      
      var $self = this;
      recursionDepth = recursionDepth - 1;
      $self.loadInitialTreeNodesStack ++;
      openCalls.open = openCalls.open + 1;

      var query = getUrlParam('query');
      if (query === '') {
        query = '*';
      }
     
      if(institutionId != "rootnode"){
        this.institutionsApiWrapper.getObjectTreeNodeChildren(institutionId, function(data) {

          if(data){
            var childNodes = [];
            for(var i=0; i<data.length; i++) {
              var nodeTitle = "<div class='dynatree-apd-title' id='" + data[i].id + "'>" + data[i].label + " (?)</div>";
              $self.createAndPushNode(nodeTitle, childNodes, data[i].id, true, false, data[i].institution);
            }
            
            if(childNodes.length == 0){
              treeElement.dynatree("getTree").getNodeByKey(institutionId).data.isFolder = false;
            }
            
            treeElement.dynatree("getTree").getNodeByKey(institutionId).removeChildren();
            treeElement.dynatree("getTree").getNodeByKey(institutionId).addChild(childNodes);
            
            for(var i=0; i<data.length; i++) {
              $self.institutionsApiWrapper.getObjectTreeNodeObjectCount(data[i].id, data[i].label, query, function(response){
                var node = treeElement.dynatree("getTree").getNodeByKey(response.id);
                node.setTitle(node.data.title.replace("(?)", "("+response.count+")"));
              });

              if(recursionDepth > 0){
                $self.openTreeNode(data[i].id, treeElement, recursionDepth, initialTreeNodesLoadedCallback, openCalls, isOpenedCallback);
              }
            }

          }else{
            // No response data from backend
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

    showStructureNodeDetails: function(institutionId, treeElement, detailViewElement, isInstitution){

      var query = getUrlParam("query");
      var isInstitution = treeElement.dynatree("getTree").getNodeByKey(institutionId).data.isInstitution;

      if(institutionId != "rootnode"){
        var History = window.History;
        var urlParameters = "?query=" + query + 
                            "&nodeId=" + institutionId + 
                            "&isInstitution=" + isInstitution;
        History.pushState("", document.title, decodeURI(urlParameters));
      }
      
      var id = getUrlParam("nodeId");
      if(id != "" && id != "rootnode"){
        institutionId = id;
      }

      this.institutionsApiWrapper.getStructureTreeNodeDetails(institutionId, query, isInstitution, function(data) {
        detailViewElement.empty();
        if(data){
          detailViewElement.append(data);
          mapSetup();
        }else{
          detailViewElement.append("Bitte wählen Sie ein Archiv");
        }
        
//        var History = window.History;
//        var urlParameters = "?query=" + query + 
//                            "&nodeId=" + institutionId + 
//                            "&isInstitution=" + isInstitution;
//        History.pushState("", document.title, decodeURI(urlParameters));

      });
    },

    loadInitialTreeNodes: function(treeElement, initialTreeNodesLoadedCallback) {
      if(this.isStructure){
        this.loadStructureInitialTreeNodes(treeElement, initialTreeNodesLoadedCallback);
      }else{
        this.loadObjectInitialTreeNodes(treeElement, initialTreeNodesLoadedCallback);
      }
    },
    
    loadStructureInitialTreeNodes: function(treeElement, initialTreeNodesLoadedCallback){
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
            $self.createAndPushNode(nodeTitle, rootNodes, data.institutions[i].id, true, false, data.institutions[i].institution);
          }
          for(var i=0; i<data.institutions.length; i++) {
            if(data.institutions[i].id){
              $self.openTreeNode(data.institutions[i].id, treeElement, 1, initialTreeNodesLoadedCallback, {'open':0}, function(openCalls){});
            }
          }          
          treeElement.dynatree("getRoot").addChild(rootNodes);
        }else{
          var rootNodes = [];
          treeElement.dynatree("getRoot").addChild(rootNodes);
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
    
    loadObjectInitialTreeNodes: function(treeElement, initialTreeNodesLoadedCallback){
      var $self = this;
      var query = getUrlParam("query");
      if(query === ""){
        query = "*"
      }
      
      $self.loadInitialTreeNodesStack = 0;
      $self.loadInitialTreeNodesStack ++;

      this.institutionsApiWrapper.getObjectTreeRootNodes(query, function(data){
        
        if(data){
          var childNodes = [];
          for(var i=0; i<data.institutions.length; i++) {
            var tmpNodeTitle = "<div class='dynatree-apd-title' id='" + data.institutions[i].id + "'>" + data.institutions[i].name+" ("+data.institutions[i].count+")" + "</div>";
            $self.createAndPushNode(tmpNodeTitle, childNodes, data.institutions[i].id, true, false, data.institutions[i].institution, data.institutions[i].count);
          }
          
          for(var i=0; i<data.institutions.length; i++) {
            if(data.institutions[i].id){
              $self.openTreeNode(data.institutions[i].id, treeElement, 1, initialTreeNodesLoadedCallback, {'open':0}, function(openCalls){});
            }
          }

          var rootNodeTitle = "<div class='dynatree-apd-title'>" + data.count+" Objekte" + "</div>";
          var root = [{ title: rootNodeTitle, 
                        key: "rootnode",
                        numberOfItems: data.count,
                        isFolder: true,
                        isLazy: true,
                        children: childNodes,
                        isInstitution: false}
                      ];
          
          treeElement.dynatree("getRoot").addChild(root);
          
          // Open root node
          var rootNodeTreeElement = treeElement.dynatree("getTree").getNodeByKey("rootnode");
          rootNodeTreeElement.expand(true);

          // Initialize the list pagination and navigation
          $self.objectContentManager.initializePagination(rootNodeTreeElement);

        }else{
          //No data from backend
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
    
    openPathToNode: function(nodeId, treeElement, detailViewElement) {
      var $self = this;
      this.institutionsApiWrapper.getStructureTreeNodeParents(nodeId, function(data){
        if(data){
          $self.openNode(nodeId, data, treeElement, detailViewElement);
        }
      });
      
    },
    
    openNode: function(nodeId, nodeList, treeElement, detailViewElement){
      var $self = this;
      if(nodeList.length > 0){
        var currentNode = nodeList[nodeList.length-1];
        nodeList.splice(nodeList.length - 1, nodeList.length);
        
        $self.openTreeNode(currentNode.id, treeElement, 1, function(){}, {'open':0}, function(openCalls){
          if(openCalls['open'] == 0){
            var node = treeElement.dynatree("getTree").getNodeByKey(currentNode.id);
            if(node){
              node.expand(true);
            }
            $self.openNode(nodeId, nodeList, treeElement, detailViewElement);
          }
        });
      }else{ // Last node
        $self.highlightNode(nodeId, treeElement);
        
        var node = treeElement.dynatree("getTree").getNodeByKey(nodeId);
        if($self.isStructure){
          $self.showStructureNodeDetails(node.data.key, treeElement, detailViewElement, node.data.institution);
        }else{
          $self.objectContentManager.showNodeDetails(node.data.key, detailViewElement, node.data.numberOfItems);
        }
        
        if($self.isStructure){
          $self.scrollToNode(nodeId);
        }
      }
    },
    
    createAndPushNode: function(nodeTitle, nodesArray, key, isFolder, isLazy, isInstitution, numberOfItems){ 
      nodesArray.push(
          {title: nodeTitle, 
            key: key, 
            isFolder: isFolder, 
            isLazy: isLazy,
            children: [{title:"<div class='dynatree-apd-title'>Loading...</div>", key: "empty"}],
            isInstitution: isInstitution,
            numberOfItems: numberOfItems}
      );
    },
    
    highlightNode: function(nodeId, treeElement) {
      
      // Deselect all
      treeElement.dynatree("getRoot").visit(function(node){
        node.select(false);
      });
      
      // Select single node
      var node = treeElement.dynatree("getTree").getNodeByKey(nodeId);
      if(node){
        node.select(true);
        treeElement.dynatree("getTree").activateKey(nodeId);
      }
      
    },
    
    scrollToNode: function(nodeId){
      var nodeAnchor = $("#"+nodeId)[0];
      nodeAnchor.scrollIntoView();
    }
    
  });
});
