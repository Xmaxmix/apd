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
    $.ajax({
      type: 'GET',
      dataType: 'html',
      async: true,
      cache: false,
      url: fullUrl,
      complete: function(data) {
        callback(data.responseText);
      }
    });
  };

  var institutionId,
    query,
    offset,
    pagesize,
    $detailView;

  institutionId = getParameterByName('id');
  query = getParameterByName('query');
  offset = getParameterByName('offset');
  $detailView = $('.list-container');

  return {
    showResults: function(pageSize, sortBy) {
      getObjectTreeNodeDetails(institutionId, query, offset, pageSize, sortBy, function(data) {
        $detailView.empty();
        $detailView.append(data);
      });
    },
    updateHistory: function(pageSize, sortBy) {
      if (History) {
      var newUri = '?query=' + encodeURI(query) + '&offset=' + offset + '&pagesize=' +
        pageSize + '&id=' + institutionId + '&sort=' + sortBy;
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
        pageSize = paginationModule.getParameterByName('pagesize') || 20,
        sortBy = paginationModule.getParameterByName('sort') || 'RELEVANCE';

    $resultPerPage.val(pageSize);
    $resultSortBy.val(sortBy);

    /* When the user change the result per page, we execute a new search using
     * the new parameter. On success we replace the content on the right side
     * using the response.
     */
    $resultPerPage.change(function(event) {
      event.preventDefault();
      pageSize = $(this).val();

      paginationModule.showResults(pageSize, sortBy);
      paginationModule.updateHistory(pageSize, sortBy);
    });

    $resultSortBy.change(function(event) {
      event.preventDefault();
      sortBy = $(this).val();

      // TODO: don't repeat your self.
      paginationModule.showResults(pageSize, sortBy);
      paginationModule.updateHistory(pageSize, sortBy);
    });
  }
});
