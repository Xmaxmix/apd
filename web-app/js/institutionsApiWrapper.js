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

  InstitutionsApiWrapper = function() {
    this.init();
  };

  $.extend(InstitutionsApiWrapper.prototype, {

    init: function() {
    },

    getFullInstitutionsList: function(callback) {
      $.ajax({
        type: 'GET',
        dataType: 'json',
        async: true,
        cache: false, // always no-cache this request!
        url: jsContextPath + '/institutions/outdated/' + jsInstitutionsListHash, // it is important to always add the hash!
        complete: function(data) {
          var jsonResponse = jQuery.parseJSON(data.responseText);
          if (jsonResponse.content.isOutdated) {
            jsInstitutionsListHash = jsonResponse.content.hashId; //Refresh hash url if dataset changed
          }

          $.ajax({
            type: 'GET',
            dataType: 'text', // Explicitly use "text/plain" as contenttype because some browsers disable caching for JSON
            async: true,
            cache: true, // always cache this request!
            url: jsContextPath + '/institutions/full/' + jsInstitutionsListHash, // it is important to always add the hash!
            complete: function(data) {
              var jsonResponse = jQuery.parseJSON(data.responseText);
              callback(jsonResponse);
            }
          });
        }
      });
    },

//    getArchiveList: function(query, facets, callback) {
//      var fullUrl = jsContextPath + "/institutions/archives";
//      fullUrl += "?searchQuery=" + query;
//      fullUrl += "?facets=" + facets;
//      $.ajax({
//        type: 'GET',
//        dataType: 'json',
//        async: true,
//        cache: false,
//        url: fullUrl,
//        complete: function(data){
//          var jsonResponse = jQuery.parseJSON(data.responseText);
//          callback(jsonResponse);
//        }
//      });
//    },


    getObjectTreeRootNodes: function(query, callback) {
      var fullUrl = jsContextPath + '/liste/root';
      fullUrl += '?query=' + query;
      $.ajax({
        type: 'GET',
        dataType: 'json',
        async: true,
        cache: false,
        url: fullUrl,
        complete: function(data) {
          var jsonResponse = jQuery.parseJSON(data.responseText);
          callback(jsonResponse);
        },
        error: function(xhr, ajaxOptions, thrownError) {
          callback(null);
        }
      });
    },

    getObjectTreeNodeDetails: function(itemId, query, offset, pagesize, sortBy, callback) {
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
    },

    getObjectTreeNodeObjectCount: function(itemId, itemName, query, callback) {
      var fullUrl = jsContextPath + '/liste/objectcount/' + itemId;
      fullUrl += '?query=' + query;
      fullUrl += '&itemName=' + itemName;
      $.ajax({
        type: 'GET',
        dataType: 'json',
        async: true,
        cache: false,
        url: fullUrl,
        complete: function(data) {
          var jsonResponse = jQuery.parseJSON(data.responseText);
          callback(jsonResponse);
        },
        error: function(xhr, ajaxOptions, thrownError) {
          callback(null);
        }
      });
    },

    getObjectTreeNodeChildren: function(itemId, callback) {
      var fullUrl = jsContextPath + '/liste/children/' + itemId;
      $.ajax({
        type: 'GET',
        dataType: 'json',
        async: true,
        cache: false,
        url: fullUrl,
        complete: function(data) {
          var jsonResponse = jQuery.parseJSON(data.responseText);
          callback(jsonResponse);
        },
        error: function(xhr, ajaxOptions, thrownError) {
          callback(null);
        }
      });
    },

    getStructureTreeRootNodes: function(query, callback) {
      var fullUrl = jsContextPath + '/struktur/root';
      fullUrl += '?query=' + query;
      $.ajax({
        type: 'GET',
        dataType: 'json',
        async: true,
        cache: false,
        url: fullUrl,
        complete: function(data) {
          var jsonResponse = jQuery.parseJSON(data.responseText);
          callback(jsonResponse);
        },
        error: function(xhr, ajaxOptions, thrownError) {
        }
      });
    },

    getStructureTreeNodeDetails: function(itemId, query, isInstitution, callback) {
      var fullUrl = jsContextPath + "/struktur/detail/"+itemId;
      fullUrl = fullUrl + "?query="+query+"&isInstitution="+isInstitution;
      var isAlready404 = false;
      $.ajax({
        type: 'GET',
        dataType: 'html',
        async: true,
        cache: false,
        url: fullUrl,
        complete: function(data){
          if(isAlready404){ // prevent redirects after 404 answer
            callback(null);
            return;
          }
          callback(data.responseText);
        },
        error:function (xhr, ajaxOptions, thrownError){
          isAlready404 = true;
          callback(null);
        }
      });
    },

    getStructureTreeNodeChildren: function(itemId, callback) {
      var fullUrl = jsContextPath + '/struktur/children/' + itemId;
      $.ajax({
        type: 'GET',
        dataType: 'json',
        async: true,
        cache: false,
        url: fullUrl,
        complete: function(data) {
          var jsonResponse = jQuery.parseJSON(data.responseText);
          callback(jsonResponse);
        },
        error: function(xhr, ajaxOptions, thrownError) {
          callback(null);
        }
      });
    },
    
    getStructureTreeNodeParents: function(itemId, callback) {
      var fullUrl = jsContextPath + '/struktur/parents/' + itemId;
      $.ajax({
        type: 'GET',
        dataType: 'json',
        async: true,
        cache: false,
        url: fullUrl,
        complete: function(data) {
          var jsonResponse = jQuery.parseJSON(data.responseText);
          callback(jsonResponse);
        },
        error: function(xhr, ajaxOptions, thrownError) {
          callback(null);
        }
      });
    },


  });
});
