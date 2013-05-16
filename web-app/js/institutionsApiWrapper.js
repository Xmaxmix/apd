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

  InstitutionsApiWrapper = function() {};

  $.extend(InstitutionsApiWrapper.prototype, {

    getFullInstitutionsList: function(callback) {
      var url = jsContextPath + '/institutions/outdated/' + jsInstitutionsListHash;
      
      var complete = function(data) {
        var jsonResponse = jQuery.parseJSON(data.responseText);
        if (jsonResponse.content.isOutdated) {
          jsInstitutionsListHash = jsonResponse.content.hashId; //Refresh hash url if dataset changed
        }
        var innestedComplete = function(dataResponse) {
          var jsonResponse = jQuery.parseJSON(dataResponse.responseText);
          callback(jsonResponse);
        }
        this.executeCall('json', url, true, true, innestedComplete, null);
      };
      
      var error = function(){};
      
      this.executeCall('json', url, true, false, complete, error);
    },

    getObjectTreeRootNodes: function(query, callback) {
      var url = jsContextPath + '/liste/root'+'?query=' + query;
      
      var complete = function(data) {
        var jsonResponse = jQuery.parseJSON(data.responseText);
        callback(jsonResponse);
      };
      
      var error = function(xhr, ajaxOptions, thrownError) {
        callback(null);
      };
      
      this.executeCall('json', url, true, false, complete, error);
    },

    getObjectTreeNodeDetails: function(itemId, query, offset, pagesize, sortBy, callback) {
      var url = jsContextPath + '/liste/detail/' + itemId;
      url += '?query=' + query;
      url += '&offset=' + offset;
      url += '&pagesize=' + pagesize;
      url += '&sort=' + sortBy;
      var isAlready404 = false;
      
      var complete = function(data) {
        if (isAlready404) { // prevent redirects after 404 answer
          callback(null);
          return;
        }
        callback(data.responseText);
      };
      
      var error = function(xhr, ajaxOptions, thrownError) {
        isAlready404 = true;
        callback(null);
      };
      
      this.executeCall('html', url, true, false, complete, error);
    },

    getObjectTreeNodeObjectCount: function(itemId, itemName, query, callback) {
      var url = jsContextPath + '/liste/objectcount/' + itemId;
      url += '?query=' + query;
      url += '&itemName=' + itemName;
      
      var complete = function(data) {
        var jsonResponse = jQuery.parseJSON(data.responseText);
        callback(jsonResponse);
      };
      
      var error = function(xhr, ajaxOptions, thrownError) {
        callback(null);
      };
      
      this.executeCall('json', url, true, false, complete, error);
    },

    getObjectTreeNodeChildren: function(itemId, callback) {
      var url = jsContextPath + '/liste/children/' + itemId;
      
      var complete = function(data) {
        var jsonResponse = jQuery.parseJSON(data.responseText);
        callback(jsonResponse);
      };
      
      var error = function(xhr, ajaxOptions, thrownError) {
        callback(null);
      };
      
      this.executeCall('json', url, true, false, complete, error);
    },

    getStructureTreeRootNodes: function(query, callback) {
      var url = jsContextPath + '/struktur/root'+'?query=' + query;
      
      var complete = function(data) {
        var jsonResponse = jQuery.parseJSON(data.responseText);
        callback(jsonResponse);
      };
      
      var error = function() {};
      
      this.executeCall('json', url, true, false, complete, error);
    },

    getStructureTreeNodeDetails: function(itemId, query, isInstitution, callback) {
      var url = jsContextPath + "/struktur/detail/"+itemId;
      url += "?query="+query+"&isInstitution="+isInstitution;
      var isAlready404 = false;
      
      var complete = function(data){
        if(isAlready404){ // prevent redirects after 404 answer
          callback(null);
          return;
        }
        callback(data.responseText);
      };
      
      var error = function (xhr, ajaxOptions, thrownError){
        isAlready404 = true;
        callback(null);
      };
      
      this.executeCall('json', url, true, false, complete, error);
    },

    getStructureTreeNodeChildren: function(itemId, callback) {
      var url = jsContextPath + '/struktur/children/' + itemId;
      
      var complete = function(data) {
        var jsonResponse = jQuery.parseJSON(data.responseText);
        callback(jsonResponse);
      };
      
      var error = function(xhr, ajaxOptions, thrownError) {
        callback(null);
      };
      
      this.executeCall('json', url, true, false, complete, error);
    },
    
    executeCall: function(dataType, url, async, cache, complete, error){
      $.ajax({
        type: 'GET',
        dataType: dataType,
        async: async,
        cache: cache,
        url: url,
        complete: function(data){complete(data)},
        error: function(xhr, ajaxOptions, thrownError){error(xhr, ajaxOptions, thrownError)}
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
