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

  ObjectDetailWrapper = function() {
    this.init();
  };

  $.extend(ObjectDetailWrapper.prototype, {

    institutionsApiWrapper: new InstitutionsApiWrapper(),

    init: function() {
    },
    
    initializePagination: function(institutionId, treeDiv, detailView) {
//        var rootNodeTreeElement = $(treeDiv).dynatree("getTree").getNodeByKey("rootnode");
//        this.showNodeDetails('rootnode', '#institution-tree', '.list-container', rootNodeTreeElement.data.numberOfItems);
    },

    showNodeDetails: function(institutionId, treeDiv, detailView, numberOfItems) {
      var $self = this;

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
      
      var isInstitution = false;
//      if($(treeDiv).dynatree("getTree").getNodeByKey(institutionId)){
//        isInstitution = $(treeDiv).dynatree("getTree").getNodeByKey(institutionId).data.isInstitution;
//      }
      
      if(institutionId != "rootnode"){
        var History = window.History;
        var urlParameters = '?query=' + query +
                            '&offset=' + offset +
                            '&pagesize=' + pagesize +
                            '&sort=' + sortBy +
                            '&id=' + institutionId + 
                            '&isInstitution=' + isInstitution;
        History.pushState('', document.title, decodeURI(urlParameters));
      }

      var id = this.getUrlParam("id");
      if(id != "" && id != "rootnode"){
        institutionId = id;
      }

      this.institutionsApiWrapper.getObjectTreeNodeDetails(institutionId, query, offset, pagesize, sortBy, function(data) {
        $(detailView).empty();
        $(detailView).append(data);

        // We change the total number of result to itemSize
        $('#results-total').text(numberOfItems);
        $('.results-overall-index').text('1 - ' + pagesize);

        var totalPage = Math.ceil(numberOfItems / pagesize);
        if(totalPage) {
          $('.total-pages').text(totalPage);
        }

        // we change the hrefs in the following links: first, prev, next and last
        var nextPageUri = $self.buildNextPageUri(pagesize);
        var lastPageUri = $self.buildLastPageUri(numberOfItems, pagesize);

        $('li.next-page').find('a.page-nav-result').attr('href', nextPageUri);
        $('li.last-page').find('a.page-nav-result').attr('href', lastPageUri);

        var History = window.History;
        var urlParameters = '?query=' + query +
                    '&offset=' + offset +
                    '&pagesize=' + pagesize +
                    '&sort=' + sortBy +
                    '&id=' + institutionId + 
                    '&isInstitution=' + isInstitution;
        History.pushState('', document.title, decodeURI(urlParameters));

      });
    },

    buildNextPageUri: function(pagesize) {
      var query = $.getQuery(window.location.href);
      query.offset = parseInt(query.offset, 10) + parseInt(pagesize, 10);
      return location.host + location.pathname + '?' + $.param(query);
    },

    buildLastPageUri: function(totalResult, pagesize) {
      var pageSize = parseInt(pagesize, 10);
      // 	#page = Math.ceil(#results / pagesize)
      var totalPage = Math.ceil(totalResult / pageSize);

      var query = $.getQuery(window.location.href);

      // offset = (#page - 1) * pagesize
      query.offset = (totalPage - 1) * pageSize;
      return location.host + location.pathname + '?' + $.param(query);
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