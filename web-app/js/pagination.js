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
(function() {

  var paginationModule = (function() {

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
      } else{
        return decodeURIComponent(results[1].replace(/\+/g, ' '));
      }
    };

    // TODO: duplicate method from InstitutionApiWrapper.
    var getObjectTreeNodeDetails = function getObjectTreeNodeDetails(itemId, query, offset, pagesize, callback) {
      var fullUrl = jsContextPath + '/liste/detail/' + itemId;
      fullUrl += '?query=' + query;
      fullUrl += '&offset=' + offset;
      fullUrl += '&pagesize=' + pagesize;
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
      showResults: function(pageSize) {
        getObjectTreeNodeDetails(institutionId, query, offset, pageSize, function(data) {
          $detailView.empty();
          $detailView.append(data);
        });
      },
      updateHistory: function(pageSize) {
        var History = window.History;
        var newUri = '?query=' + encodeURI(query) + '&offset=' + offset + '&pagesize=' +
          pageSize + '&id=' + institutionId;
        History.pushState('', encodeURI(document.title), newUri);
      }
    };
  }());

  $('#result-per-page').change(function(event) {
    event.preventDefault();
    var pageSize = $('#result-per-page')
      .find(':selected')
      .text();

    paginationModule.showResults(pageSize);
    paginationModule.updateHistory(pageSize);
  });

}());
