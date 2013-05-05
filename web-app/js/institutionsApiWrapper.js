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

  InstitutionsApiWrapper = function(){
    this.init();
  };

  $.extend(InstitutionsApiWrapper.prototype, {

    init: function() {
    },

    getFullInstitutionsList: function(callback) {
      console.log("##############1 getFullInstitutionsList ");
      $.ajax({
        type: 'GET',
        dataType: 'json',
        async: true,
        cache: false, // always no-cache this request!
        url: jsContextPath+"/institutions/outdated/"+jsInstitutionsListHash, // it is important to always add the hash!
        complete: function(data){
          var jsonResponse = jQuery.parseJSON(data.responseText);
          if(jsonResponse.content.isOutdated){
            jsInstitutionsListHash = jsonResponse.content.hashId; //Refresh hash url if dataset changed
          }

          $.ajax({
            type: 'GET',
            dataType: 'text', // Explicitly use "text/plain" as contenttype because some browsers disable caching for JSON
            async: true,
            cache: true, // always cache this request!
            url: jsContextPath+"/institutions/full/"+jsInstitutionsListHash, // it is important to always add the hash!
            complete: function(data){
              var jsonResponse = jQuery.parseJSON(data.responseText);
              callback(jsonResponse);
            }
          });
        }
      });
    },

//    getArchiveList: function(query, facets, callback) {
//      console.log("##############1 getArchiveList ");
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
      console.log("##############1 getObjectTreeRootNodes "+query);
      var fullUrl = jsContextPath + "/liste/root";
      fullUrl += "?query="+query;
      $.ajax({
        type: 'GET',
        dataType: 'json',
        async: true,
        cache: false,
        url: fullUrl,
        complete: function(data){
          var jsonResponse = jQuery.parseJSON(data.responseText);
          callback(jsonResponse);
        }
      });
    },

    getObjectTreeNodeDetails: function(itemId, query, offset, pagesize, callback) {
      console.log("##############1 getObjectTreeNodeDetails "+itemId);
      var fullUrl = jsContextPath + "/liste/detail/"+itemId;
      fullUrl += "?query="+query;
      fullUrl += "&offset="+offset;
      fullUrl += "&pagesize="+pagesize;
      $.ajax({
        type: 'GET',
        dataType: 'html',
        async: true,
        cache: false,
        url: fullUrl,
        complete: function(data){
          callback(data.responseText);
        }
      });
    },

    getObjectTreeNodeChildren: function(itemId, callback) {
      console.log("##############1 getObjectTreeNodeChildren "+itemId);
      var fullUrl = jsContextPath + "/liste/children/"+itemId;
      $.ajax({
        type: 'GET',
        dataType: 'json',
        async: true,
        cache: false,
        url: fullUrl,
        complete: function(data){
          var jsonResponse = jQuery.parseJSON(data.responseText);
          callback(jsonResponse);
        }
      });
    },

    getStructureTreeRootNodes: function(query, callback) {
      console.log("##############1 getStructureTreeRootNodes "+query);
      var fullUrl = jsContextPath + "/struktur/root";
      fullUrl += "?query="+query;
      $.ajax({
        type: 'GET',
        dataType: 'json',
        async: true,
        cache: false,
        url: fullUrl,
        complete: function(data){
          var jsonResponse = jQuery.parseJSON(data.responseText);
          callback(jsonResponse);
        }
      });
    },

    getStructureTreeNodeDetails: function(itemId, query, callback) {
      console.log("##############1 getStructureTreeNodeDetails "+itemId);
      var fullUrl = jsContextPath + "/struktur/detail/"+itemId;
      fullUrl += "?query="+query;
      $.ajax({
        type: 'GET',
        dataType: 'html',
        async: true,
        cache: false,
        url: fullUrl,
        complete: function(data){
          callback(data.responseText);
        }
      });
    },

    getStructureTreeNodeChildren: function(itemId, callback) {
      console.log("##############1 getStructureTreeNodeChildren "+itemId);
      var fullUrl = jsContextPath + "/struktur/children/"+itemId;
      $.ajax({
        type: 'GET',
        dataType: 'json',
        async: true,
        cache: false,
        url: fullUrl,
        complete: function(data){
          var jsonResponse = jQuery.parseJSON(data.responseText);
          callback(jsonResponse);
        }
      });
    }

  });
});