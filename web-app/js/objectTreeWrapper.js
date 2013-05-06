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

      var $self = this;
      $(treeDiv).dynatree({
        onClick: function(node, event) {
          $self.showNodeDetails(node.data.key, detailView, node.data.numberOfItems);
        },
        onExpand: function(expand, node) {
          $self.openTreeNode(node.data.key);
        }
      });

    },

    // TODO: who calls this function?
    clickOnInstitution: function(institutionId, detailView) {
      this.showNodeDetails(institutionId, detailView);
      this.openTreeNode(institutionId, detailView);
    },

    openTreeNode: function(institutionId) {
      if (institutionId !== 'rootnode') {
        var institutionsApiWrapper = new InstitutionsApiWrapper();
        institutionsApiWrapper.getObjectTreeNodeChildren(institutionId, function(data) {
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
      History.pushState('', encodeURI(document.title), '?query=' + encodeURI(query) +
                                                       '&offset=' + offset +
                                                       '&pagesize=' + pagesize +
                                                       '&sort=' + sortBy +
                                                       '&id=' + institutionId);

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
      var query = this.getUrlParam('search');

      var institutionsApiWrapper = new InstitutionsApiWrapper();
      institutionsApiWrapper.getObjectTreeRootNodes(query, function(data) {

        var childNodes = [];
        for (var i = 0; i < data.institutions.length; i++) {
          childNodes.push({
            title: data.institutions[i].name + ' (' + data.institutions[i].count + ')',
            numberOfItems: data.institutions[i].count,
            key: data.institutions[i].id,
            isFolder: true,
            isLazy: true,
            children: [{title: '', key: 'empty'}]
          });
        }

        var root = [{ title: data.count + ' Objekte',
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

    getUrlParam: function(name) {
      var results = new RegExp('[\\?&amp;]' + name + '=([^&amp;#]*)').exec(window.location.href);
      if (!results) {
        return '';
      }
      return results[1] || 0;
    }

  });

});
