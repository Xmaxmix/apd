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
      History.pushState('', encodeURI(document.title), newUri);
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

      $('.results-overall-index').text('1 - ' + pageSize);
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
    });


    // TODO: add click listener to a.page-nav-result
    $('a.page-nav-result').click(function(event) {
      event.preventDefault();

      var queryString = $.parseQuery($(this).attr('href'));

      var fullUrl = jsContextPath + '/liste/detail/' + queryString.id;
      fullUrl += '?query=' + queryString.query;
      fullUrl += '&offset=' + queryString.offset;
      fullUrl += '&pagesize=' + queryString.pagesize;
      fullUrl += '&sort=' + queryString.sort;
      var isAlready404 = false;

      $.ajax({
        type: 'GET',
        dataType: 'html',
        async: true,
        cache: false,
        url: fullUrl,
        complete: function(data) {
          if (isAlready404) { // prevent redirects after 404 answer
            ///callback(null);
            return;
          }
          $('.list-container').empty();
          console.log(data.responseText);
//          $('.list-container').append(data.responseText);
        },
        error: function(xhr, ajaxOptions, thrownError) {
          isAlready404 = true;
//          callback(null);
        }
      });
    });
    // TODO: onSuccess replace the result list with the new HTML
    // TODO: update the navigations
  }
});
