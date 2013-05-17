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

    showNodeDetails: function(institutionId, detailView, numberOfItems) {
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

      $('.result-count').text(numberOfItems);

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

        // pagination numbers
        var fromPag = Math.ceil(pagesize*(offset / pagesize)+1);
        var toPag = Math.ceil(parseInt(offset) + parseInt(pagesize));
        var currentPagination = fromPag+"-"+toPag;
        $('.results-overall-index').text(currentPagination);

        // total number of result for the pagination
        $('#results-total').text(numberOfItems);

        // current page number for the navigation
        var currentPage = Math.ceil((offset / pagesize)+1);
        $('.current-page').text(currentPage);

        // total number of pages for the navigation
        var totalPage = Math.ceil(numberOfItems / pagesize);
        if(totalPage) {
          $('.total-pages').text(totalPage);
        }

        // activation of pagination and navigation controllers

        // first page
        if (currentPage == 1) {
          $('li.first-page a.page-nav-result').addClass("off");
          $('li.prev-page a.page-nav-result').addClass("off");
          $('li.next-page a.page-nav-result').removeClass("off");
          $('li.last-page a.page-nav-result').removeClass("off");
        }
        else {
          $('li.first-page a.page-nav-result').removeClass("off");
          $('li.prev-page a.page-nav-result').removeClass("off");
        }

        // last page
        if (currentPage == parseInt($('.total-pages').text(), 10)) {
          $('li.first-page a.page-nav-result').removeClass("off");
          $('li.prev-page a.page-nav-result').removeClass("off");
          $('li.next-page a.page-nav-result').addClass("off");
          $('li.last-page a.page-nav-result').addClass("off");
        }

        // updating of the hrefs in the following links: first, prev, next and last
        var firstPageUri = $self.buildFirstPageUri(pagesize);
        var prevPageUri = $self.buildPrevPageUri(pagesize);
        var nextPageUri = $self.buildNextPageUri(pagesize);
        if (!numberOfItems){
          numberOfItems = parseInt($('.result-count').text(), 10);
        }
        var lastPageUri = $self.buildLastPageUri(numberOfItems, pagesize);

        $('li.first-page a.page-nav-result').attr('href', firstPageUri);
        $('li.prev-page a.page-nav-result').attr('href', prevPageUri);
        $('li.next-page a.page-nav-result').attr('href', nextPageUri);
        $('li.last-page a.page-nav-result').attr('href', lastPageUri);

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

    buildFirstPageUri: function(pagesize) {
      var query = $.getQuery(window.location.href);
      query.offset = 0;
      return location.pathname + '?' + $.param(query);
    },

    buildPrevPageUri: function(pagesize) {
      var query = $.getQuery(window.location.href);
      query.offset = parseInt(query.offset, 10) - parseInt(pagesize, 10);
      return location.pathname + '?' + $.param(query);
    },

    buildNextPageUri: function(pagesize) {
      var query = $.getQuery(window.location.href);
      query.offset = parseInt(query.offset, 10) + parseInt(pagesize, 10);
      return location.pathname + '?' + $.param(query);
    },

    buildLastPageUri: function(totalResult, pagesize) {
      var pageSize = parseInt(pagesize, 10);
      var totalPage = Math.ceil(totalResult / pageSize);
      var query = $.getQuery(window.location.href);

      query.offset = (totalPage - 1) * pageSize;
      return location.pathname + '?' + $.param(query);
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