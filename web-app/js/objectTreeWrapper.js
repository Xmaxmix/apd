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
          console.log(node);
          $self.showNodeDetails(node.data.key, detailView);
        },
        onExpand: function(expand, node) {
          $self.openTreeNode(node.data.key);
        },

      });
      
    },
    
    clickOnInstitution: function(institutionId, detailView) {
      console.log("####################2 clickOnInstitution: "+institutionId);
      this.showNodeDetails(institutionId, detailView);
      this.openTreeNode(institutionId, detailView);
    },
    
    openTreeNode: function(institutionId) {
      console.log("####################2 openTreeNode "+institutionId);
      if(institutionId != "rootnode"){
        var institutionsApiWrapper = new InstitutionsApiWrapper();
        institutionsApiWrapper.getObjectTreeNodeChildren(institutionId, function(data) {
          console.log("####################2 child: "+data);
        });
      }
    },

    showNodeDetails: function(institutionId, detailView) {

      console.log("####################2 showNodeDetails '"+institutionId+"'");
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
      
      var History = window.History;
      History.pushState("", encodeURI(document.title), "?query="+encodeURI(query)+"&offset="+offset+"&pagesize="+pagesize+"&id="+institutionId);
      
      institutionsApiWrapper.getObjectTreeNodeDetails(institutionId, query, offset, pagesize, function(data) {
        $(detailView).empty();
        $(detailView).append(data);
      });
    },
    
    loadInitialTreeNodes: function(treeDiv) {
      console.log("####################2 loadInitialTreeNodes "+treeDiv);
      
      var query = this.getUrlParam("search");

      var institutionsApiWrapper = new InstitutionsApiWrapper();
      institutionsApiWrapper.getObjectTreeRootNodes(query, function(data){
        
        var childNodes = [];
        for(var i=0; i<data.institutions.length; i++) {
          childNodes.push(
            {title: data.institutions[i].name+" ("+data.institutions[i].count+")", 
              key: data.institutions[i].id, 
              isFolder: true, 
              isLazy: true, 
              children: [{title:"", key: "empty"}] }
            );
        }
        
        var root = [{ title: data.count+" Objekte", 
                      key: "rootnode", 
                      isFolder: true, 
                      isLazy: true, 
                      children: childNodes}
                    ];
        
        $(treeDiv).dynatree("getRoot").addChild(root);
        
        // Open root node
        var rootNodeTreeElement = $(treeDiv).dynatree("getTree").getNodeByKey("rootnode");
        rootNodeTreeElement.expand(true);
        
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