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
  if (jsPageName == 'objectview') {

    if ($('.search-widget-container').length > 0) {
      var searchWidgetContainer = $('#search-widget');
      searchWidget = new SearchWidget($('#search-widget-form'), searchWidgetContainer,
        searchWidgetContainer.find('.controls-container'));
    }
    
    /* Core functionalities for the right content in the object view*/
    ContentManager = function() {
        this.init();
      };

      $.extend(ContentManager.prototype, {

        institutionsApiWrapper: new InstitutionsApiWrapper(),

        init: function() {
        },
        
        initializePagination: function(rootNode) {
          this.showNodeDetails('rootnode', $('.list-container'), rootNode.data.numberOfItems);
        },

        showNodeDetails: function(institutionId, detailViewElement, numberOfItems) {

          var $self = this;

          var query = getUrlParam('query');
          if (query === '') {
            query = '*';
          }

          var offset = getUrlParam('offset');
          if (offset === '') {
            offset = '0';
          }
          var pagesize = getUrlParam('pagesize');
          if (pagesize === '') {
            pagesize = '20';
          }

          var sortBy = getUrlParam('sort');
          if (sortBy === '') {
            sortBy = 'RELEVANCE';
          }

          var isInstitution = false;

          $('.result-count').text(numberOfItems);

          var History = window.History;
          var urlParameters = '?query=' + query +
                              '&offset=' + offset +
                              '&pagesize=' + pagesize +
                              '&sort=' + sortBy +
                              '&id=' + institutionId + 
                              '&isInstitution=' + isInstitution;
          History.pushState('', document.title, decodeURI(urlParameters));

          var id = getUrlParam("id");
          if(id != "" && id != "rootnode"){
            institutionId = id;
          }

          this.institutionsApiWrapper.getObjectTreeNodeDetails(institutionId, query, offset, pagesize, sortBy, function(data) {
            detailViewElement.empty();
            detailViewElement.append(data);

            // pagination numbers
            var fromPag = Math.ceil(pagesize*(offset / pagesize)+1);
            var toPag = Math.ceil(parseInt(offset) + parseInt(pagesize));
            var maxNumber = parseInt($('.result-count').text(), 10);
            if (toPag>=maxNumber) {
              toPag = maxNumber;
            }
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
              $('li.next-page a.page-nav-result').removeClass("off");
              $('li.last-page a.page-nav-result').removeClass("off");
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
          if (query.offset<=0){
            query.offset = 0;
          }
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
        }

      });
      
      var treeElement = $("#institution-tree");
      var detailViewElement = $(".list-container");

      var objectTreeManager = new TreeManager(treeElement, detailViewElement, false);
  }
});
