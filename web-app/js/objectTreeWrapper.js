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

  ObjectTreeWrapper = function() {
    this.init();
  };

  $.extend(ObjectTreeWrapper.prototype, {

    objectDetailWrapper: new ObjectDetailWrapper(),
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
            $self.objectDetailWrapper.showNodeDetails(node.data.key, detailView, node.data.numberOfItems);
          }
        },
        onExpand: function(expand, node) {
        }
      });

    },

    openTreeNode: function(institutionId, treeDiv, recursionDepth) {

      var $self = this;
      recursionDepth = recursionDepth - 1;

      var query = this.getUrlParam('query');
      if (query === '') {
        query = '*';
      }
      
      if(institutionId != "rootnode"){
        this.institutionsApiWrapper.getObjectTreeNodeChildren(institutionId, function(data) {

          if(data){
            var childNodes = [];
            for(var i=0; i<data.length; i++) {
  
              var nodeTitle = "<div class='dynatree-apd-title'>" + data[i].label + " (?)</div>";

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
            
            for(var i=0; i<data.length; i++) {
              $self.institutionsApiWrapper.getObjectTreeNodeObjectCount(data[i].id, data[i].label, query, function(response){
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

    loadInitialTreeNodes: function(treeDiv) {

      var $self = this;
      var query = this.getUrlParam("query");
      if(query === ""){
        query = "*"
      }

      this.institutionsApiWrapper.getObjectTreeRootNodes(query, function(data){
        
        if(data){
          var childNodes = [];
          for(var i=0; i<data.institutions.length; i++) {
            
            var nodeTitle = "<div class='dynatree-apd-title'>" + data.institutions[i].name+" ("+data.institutions[i].count+")" + "</div>";
            //var nodeTitle = data.institutions[i].name+" ("+data.institutions[i].count+")";
            
            childNodes.push(
              {title: nodeTitle, 
                key: data.institutions[i].id, 
                numberOfItems: data.institutions[i].count,
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

          var nodeTitle = "<div class='dynatree-apd-title'>" + data.count+" Objekte" + "</div>";
          var root = [{ title: nodeTitle, 
                        key: "rootnode", 
                        numberOfItems: data.count,
                        isFolder: true, 
                        isLazy: true, 
                        children: childNodes,
                        isInstitution: false}
                      ];

          $(treeDiv).dynatree("getRoot").addChild(root);

          // Open root node
          var rootNodeTreeElement = $(treeDiv).dynatree("getTree").getNodeByKey("rootnode");
          rootNodeTreeElement.expand(true);

          // Initialize the list pagination and navigation
          var objectDetailWrapper = new ObjectDetailWrapper();
          objectDetailWrapper.initializePagination(rootNodeTreeElement);

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
    }

  });

});
