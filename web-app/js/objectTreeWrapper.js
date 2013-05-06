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

    init: function() {
    },

    buildInitialTree: function(treeDiv, detailView) {
      _canLog = false;

      // Initialize tree
      var $self = this;
      $(treeDiv).dynatree({
        onClick: function(node, event) {
          if (!node.bExpanded) {
            $self.openTreeNode(node.data.key, treeDiv);
          }
          $self.showNodeDetails(node.data.key, detailView, node.data.numberOfItems);
        },
        onExpand: function(expand, node) {
        }
      });

    },

    openTreeNode: function(institutionId, treeDiv) {
      var $self = this;

      var query = this.getUrlParam('query');
      if (query === '') {
        query = '*';
      }

      if (institutionId != 'rootnode') {
        var institutionsApiWrapper = new InstitutionsApiWrapper();
        institutionsApiWrapper.getObjectTreeNodeChildren(institutionId, function(data) {

          var childNodes = [];
          for (var i = 0; i < data.length; i++) {

            var nodeTitle = "<div class='dynatree-apd-title'>" + data[i].label + ' (?)</div>';
            //var nodeTitle = data[i].label;

            childNodes.push(
              {title: nodeTitle,
                key: data[i].id,
                isFolder: true,
                isLazy: true,
                children: [{title: "<div class='dynatree-apd-title'>Loading...</div>", key: 'empty'}]}
              );
          }
          $(treeDiv).dynatree('getTree').getNodeByKey(institutionId).removeChildren();
          $(treeDiv).dynatree('getTree').getNodeByKey(institutionId).addChild(childNodes);

          for (var i = 0; i < data.length; i++) {
            institutionsApiWrapper.getObjectTreeNodeObjectCount(data[i].id, data[i].label, query, function(response) {
              var node = $(treeDiv).dynatree('getTree').getNodeByKey(response.id);
              node.setTitle(node.data.title.replace('(?)', '(' + response.count + ')'));
          });

            //$self.openTreeNode(data[i].id, treeDiv);
          }

        });
      }
    },

    buildNextPageUri: function(pagesize) {
      var query = $.getQuery(window.location.href);
      query.offset = parseInt(query.offset, 10) + parseInt(pagesize, 10);
      return location.host + location.pathname + '?' + $.param(query);
    },

    buildLastPageUri: function(totalResult, pagesize) {
      var pageSize = parseInt(pagesize, 10);
      // #page = Math.ceil(#results / pagesize)
      var totalPage = Math.ceil(totalResult / pageSize);

      var query = $.getQuery(window.location.href);

      // offset = (#page - 1) * pagesize
      query.offset = (totalPage - 1) * pageSize;
      return location.host + location.pathname + '?' + $.param(query);
    },

    showNodeDetails: function(institutionId, detailView, numberOfItems) {
      var institutionsApiWrapper = new InstitutionsApiWrapper();

      var query = this.getUrlParam('query');
      if (query === '') {
        query = '*';
      }
      var offset = this.getUrlParam('offset');
      if (offset === '') {
        offset = '0';
      }
      var pagesize = this.getUrlParam('pagesize');
      if (pagesize === '') {
        pagesize = '20';
      }

      var sortBy = this.getUrlParam('sort');
      if (sortBy === '') {
        sortBy = 'RELEVANCE';
      }

      var History = window.History;
      var urlParameters = '?query=' + query + '&offset=' + offset + '&pagesize=' + pagesize + '&sort=' + sortBy + '&id=' + institutionId;
      History.pushState('', document.title, decodeURI(urlParameters));

      var that = this;
      // TODO: too many parameters, refactor to use hash
      institutionsApiWrapper.getObjectTreeNodeDetails(institutionId, query, offset, pagesize, sortBy, function(data) {
        $(detailView).empty();
        $(detailView).append(data);

        // We change the total number of result to itemSize
        $('#results-total').text(numberOfItems);
        $('.results-overall-index').text('1 - ' + pagesize);

        // we change the hrefs in the folllowing links: first, prev, next and last
        var nextPageUri = that.buildNextPageUri(pagesize);
        var lastPageUri = that.buildLastPageUri(numberOfItems, pagesize);

        $('li.next-page').find('a.page-nav-result').attr('href', nextPageUri);
        $('li.last-page').find('a.page-nav-result').attr('href', lastPageUri);
      });
    },


    loadInitialTreeNodes: function(treeDiv) {

      var query = this.getUrlParam('query');
      if (query === '') {
        query = '*';
      }

      var institutionsApiWrapper = new InstitutionsApiWrapper();
      institutionsApiWrapper.getObjectTreeRootNodes(query, function(data) {

        var childNodes = [];
        for (var i = 0; i < data.institutions.length; i++) {

          var nodeTitle = "<div class='dynatree-apd-title'>" + data.institutions[i].name + ' (' + data.institutions[i].count + ')' + '</div>';
          //var nodeTitle = data.institutions[i].name+" ("+data.institutions[i].count+")";

          childNodes.push(
            {title: nodeTitle,
              key: data.institutions[i].id,
              isFolder: true,
              isLazy: true,
              children: [{title: "<div class='dynatree-apd-title'>Loading...</div>", key: 'empty'}] }
            );
        }

        var nodeTitle = "<div class='dynatree-apd-title'>" + data.count + ' Objekte' + '</div>';
        var root = [{ title: nodeTitle,
                      key: 'rootnode',
                      isFolder: true,
                      isLazy: true,
                      children: childNodes}
                    ];

        $(treeDiv).dynatree('getRoot').addChild(root);

        // Open root node
        var rootNodeTreeElement = $(treeDiv).dynatree('getTree').getNodeByKey('rootnode');
        rootNodeTreeElement.expand(true);

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
