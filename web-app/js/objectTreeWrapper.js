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
  
  ObjectTreeWrapper = function(){
    this.init();
  }

  $.extend(ObjectTreeWrapper.prototype, {

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
          $self.showNodeDetails(node.data.key, treeDiv, detailView);
        },
        onExpand: function(expand, node) {
        },

      });
      
    },
    
    openTreeNode: function(institutionId, treeDiv, recursionDepth) {
      var $self = this;
      recursionDepth = recursionDepth - 1;

      var query = this.getUrlParam("query");
      if(query == ""){
        query = "*"
      }
      
      if(institutionId != "rootnode"){
        var institutionsApiWrapper = new InstitutionsApiWrapper();
        institutionsApiWrapper.getObjectTreeNodeChildren(institutionId, function(data) {

          if(data){
            var childNodes = [];
            for(var i=0; i<data.length; i++) {
  
              var nodeTitle = "<div class='dynatree-apd-title'>" + data[i].label + " (?)</div>";
              //var nodeTitle = data[i].label;
              
              childNodes.push(
                {title: nodeTitle, 
                  key: data[i].id, 
                  isFolder: true, 
                  isLazy: true,
                  children: [{title:"<div class='dynatree-apd-title'>Loading...</div>", key: "empty"}]}
                );
            }
            
            if(childNodes.length == 0){
              $(treeDiv).dynatree("getTree").getNodeByKey(institutionId).data.isFolder = false;
            }
            
            $(treeDiv).dynatree("getTree").getNodeByKey(institutionId).removeChildren();
            $(treeDiv).dynatree("getTree").getNodeByKey(institutionId).addChild(childNodes);
            
            for(var i=0; i<data.length; i++) {
              institutionsApiWrapper.getObjectTreeNodeObjectCount(data[i].id, data[i].label, query, function(response){
                var node = $(treeDiv).dynatree("getTree").getNodeByKey(response.id);
                node.setTitle(node.data.title.replace("(?)", "("+response.count+")"));
              });

              if(recursionDepth > 0){
                $self.openTreeNode(data[i].id, treeDiv, recursionDepth);
              }
            }
            
            
          }else{
            // No response data from backend
          }
        });
      }
    },

    showNodeDetails: function(institutionId, treeDiv, detailView) {

      var institutionsApiWrapper = new InstitutionsApiWrapper();
      
      var query = this.getUrlParam("query");
      if(query == ""){
        query = "*"
      }
      var offset = this.getUrlParam("offset");
      if(offset == ""){
        offset = "0"
      }
      var pagesize = this.getUrlParam("pagesize");
      if(pagesize == ""){
        pagesize = "20"
      }
      
      if(institutionId != "rootnode"){
        var History = window.History;
        var urlParameters = "?query="+query+"&offset="+offset+"&pagesize="+pagesize+"&id="+institutionId;
        History.pushState("", document.title, decodeURI(urlParameters));
      }
      
      var id = this.getUrlParam("id");
      if(id != "" && id != "rootnode"){
        institutionId = id;
      }
      
      
      institutionsApiWrapper.getObjectTreeNodeDetails(institutionId, query, offset, pagesize, function(data) {
        $(detailView).empty();
        $(detailView).append(data);

        var History = window.History;
        var urlParameters = "?query="+query+"&offset="+offset+"&pagesize="+pagesize+"&id="+institutionId;
        History.pushState("", document.title, decodeURI(urlParameters));
        
      });
    },
    
    loadInitialTreeNodes: function(treeDiv) {
      var $self = this;
      
      var query = this.getUrlParam("query");
      if(query == ""){
        query = "*"
      }

      var institutionsApiWrapper = new InstitutionsApiWrapper();
      institutionsApiWrapper.getObjectTreeRootNodes(query, function(data){
        
        if(data){
          var childNodes = [];
          for(var i=0; i<data.institutions.length; i++) {
            
            var nodeTitle = "<div class='dynatree-apd-title'>" + data.institutions[i].name+" ("+data.institutions[i].count+")" + "</div>";
            //var nodeTitle = data.institutions[i].name+" ("+data.institutions[i].count+")";
            
            childNodes.push(
              {title: nodeTitle, 
                key: data.institutions[i].id, 
                isFolder: true, 
                isLazy: true, 
                children: [{title:"<div class='dynatree-apd-title'>Loading...</div>", key: "empty"}] }
              );
          }
          
          for(var i=0; i<data.institutions.length; i++) {
            if(data.institutions[i].id){
              $self.openTreeNode(data.institutions[i].id, treeDiv, 1);
            }
          }          
          
          var nodeTitle = "<div class='dynatree-apd-title'>" + data.count+" Objekte" + "</div>";
          var root = [{ title: nodeTitle, 
                        key: "rootnode", 
                        isFolder: true, 
                        isLazy: true, 
                        children: childNodes}
                      ];
          
          $(treeDiv).dynatree("getRoot").addChild(root);
          
          // Open root node
          var rootNodeTreeElement = $(treeDiv).dynatree("getTree").getNodeByKey("rootnode");
          rootNodeTreeElement.expand(true);
        }else{
          //No data from backend
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