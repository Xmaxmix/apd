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
var paginationModule = (function(History) {
  'use strict';

  //TODO: refactor this function to a utility module.
  var getParameterByName = function getParameterByName(name) {
    var copy = name;
    // TODO: linter says bad escaping. Fix it.
    var temp = copy.replace(/[\[]/, '\\\[').replace(/[\]]/, '\\\]');
    var regexS = '[\\?&]' + temp + '=([^&#]*)';
    var regex = new RegExp(regexS);
    var results = regex.exec(window.location.search);
    if (results === null) {
      return '';
    } else {
      return decodeURIComponent(results[1].replace(/\+/g, ' '));
    }
  };

  // TODO: duplicate method from InstitutionApiWrapper.
  var getObjectTreeNodeDetails = function getObjectTreeNodeDetails(itemId, query, offset, pagesize, sortBy, callback) {
    var fullUrl = jsContextPath + '/liste/detail/' + itemId;
    fullUrl += '?query=' + query;
    fullUrl += '&offset=' + offset;
    fullUrl += '&pagesize=' + pagesize;
    fullUrl += '&sort=' + sortBy;
    var isAlready404 = false;
    $.ajax({
      type: 'GET',
      dataType: 'html',
      async: true,
      cache: false,
      url: fullUrl,
      complete: function(data) {
        if (isAlready404) { // prevent redirects after 404 answer
          callback(null);
          return;
        }
        callback(data.responseText);
      },
      error: function(xhr, ajaxOptions, thrownError) {
        isAlready404 = true;
        callback(null);
      }
    });
  };

  var institutionId,
    query,
    offset,
    pagesize,
    $detailView;

  query = getParameterByName('query');
  $detailView = $('.list-container');

  return {
    showResults: function(id, pageSize, sortBy, offset) {
      getObjectTreeNodeDetails(id, query, offset, pageSize, sortBy, function(data) {
        $detailView.empty();
        $detailView.append(data);
      });
    },
    updateHistory: function(id, pageSize, sortBy, offset) {
      if (History) {
      var newUri = '?query=' + encodeURI(query) + '&offset=' + offset + '&pagesize=' +
        pageSize + '&id=' + id + '&sort=' + sortBy;
      History.pushState('', document.title, newUri);
      } else {
        // TODO: use History.js, when the History API is not available.
      }
    },
    getParameterByName: getParameterByName
  };
}(History));

$(function() {
  if (jsPageName == 'objectview') {
    var $resultPerPage = $('#result-per-page'),
        $resultSortBy = $('#result-sort-by'),
        id = paginationModule.getParameterByName('id') || 'rootnode',
        pageSize = paginationModule.getParameterByName('pagesize') || 20,
        sortBy = paginationModule.getParameterByName('sort') || 'RELEVANCE',
        offset = paginationModule.getParameterByName('offset') || 0;

    $resultPerPage.val(pageSize);
    $resultSortBy.val(sortBy);

    /* When the user change the result per page, we execute a new search using
     * the new parameter. On success we replace the content on the right side
     * using the response.
     */
    $resultPerPage.change(function(event) {
      event.preventDefault();
      pageSize = $(this).val();

      id = paginationModule.getParameterByName('id') || 'rootnode',
      sortBy = paginationModule.getParameterByName('sort') || 'RELEVANCE',
      offset = paginationModule.getParameterByName('offset') || 0;

      paginationModule.showResults(id, pageSize, sortBy, offset);
      paginationModule.updateHistory(id, pageSize, sortBy, offset);

      // pagination numbers
      var fromPag = Math.ceil(pageSize*(offset / pageSize)+1);
      var toPag = Math.ceil(parseInt(offset) + parseInt(pageSize));
      var currentPagination = fromPag+"-"+toPag;
      $('.results-overall-index').text(currentPagination);

      var numberOfItems = parseInt($('.result-count').text(), 10);

      // current page number for the navigation
      var currentPage = Math.ceil((offset / pageSize)+1);
      $('.current-page').text(currentPage);

      var totalPage = Math.ceil(numberOfItems / pageSize);
      if(totalPage) {
        $('.total-pages').text(totalPage);
      }
      var contentManager = new ContentManager();
      var firstPageUri = contentManager.buildFirstPageUri(pageSize);
      var prevPageUri = contentManager.buildPrevPageUri(pageSize);
      var nextPageUri = contentManager.buildNextPageUri(pageSize);
      var lastPageUri = contentManager.buildLastPageUri(numberOfItems, pageSize);

      $('li.first-page a.page-nav-result').attr('href', firstPageUri);
      $('li.prev-page a.page-nav-result').attr('href', prevPageUri);
      $('li.next-page a.page-nav-result').attr('href', nextPageUri);
      $('li.last-page a.page-nav-result').attr('href', lastPageUri);
    });

    $resultSortBy.change(function(event) {
      event.preventDefault();
      sortBy = $(this).val();

      id = paginationModule.getParameterByName('id') || 'rootnode',
      pageSize = paginationModule.getParameterByName('pagesize') || 20,
      offset = paginationModule.getParameterByName('offset') || 0;
      console.log('sort-by', sortBy);

      // TODO: don't repeat your self.
      paginationModule.showResults(id, pageSize, sortBy, offset);
      paginationModule.updateHistory(id, pageSize, sortBy, offset);

      var queryFirst = $.getQuery($('li.first-page a.page-nav-result').attr('href'));
      var queryPrev = $.getQuery($('li.prev-page a.page-nav-result').attr('href'));
      var queryNext = $.getQuery($('li.next-page a.page-nav-result').attr('href'));
      var queryLast = $.getQuery($('li.last-page a.page-nav-result').attr('href'));
      query.sort = sortBy;
      firstPageUri = location.pathname + '?' + $.param(queryFirst);
      prevPageUri = location.pathname + '?' + $.param(queryPrev);
      nextPageUri = location.pathname + '?' + $.param(queryNext);
      lastPageUri = location.pathname + '?' + $.param(queryLast);
      $('li.first-page a.page-nav-result').attr('href', firstPageUri);
      $('li.prev-page a.page-nav-result').attr('href', prevPageUri);
      $('li.next-page a.page-nav-result').attr('href', nextPageUri);
      $('li.last-page a.page-nav-result').attr('href', lastPageUri);
    });

    //click listener for the elements with class page-nav-result
    $('.page-nav-result').click(function(){

      var contentManager = new ContentManager();
      var queryString = $.parseQuery($(this).attr('href'));
      var uri = $(this).attr('href');
      var query = getUrlParam('query', uri);
      var History = window.History;
      var urlParameters = '?query=' + query +
                  '&offset=' + queryString.offset +
                  '&pagesize=' + queryString.pagesize +
                  '&sort=' + queryString.sort +
                  '&nodeId=' + queryString.nodeId + 
                  '&isInstitution=' + queryString.isInstitution;

      History.pushState('', document.title, decodeURI(urlParameters));

      contentManager.showNodeDetails(queryString.nodeId, $('.list-container'));
      return false;
    });

    function getUrlParam (name, uri){
        name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
        var regexS = "[\\?&]" + name + "=([^&#]*)";
        var regex = new RegExp(regexS);
        var results = regex.exec(uri);
        if(results == null) {
          return "";
        }else{
          return decodeURIComponent(results[1].replace(/\+/g, " "));
        }
    };
  }
});
