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
  }

  $.extend(InstitutionsApiWrapper.prototype, {

    init: function() {
      console.log("##############1 init ");
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
    
    getArchiveList: function(searchQuery, facets, callback) {
      console.log("##############1 getArchiveList ");
      var fullUrl = jsContextPath + "/institutions/archives";
      fullUrl += "?searchQuery=" + searchQuery;
      fullUrl += "?facets=" + facets;
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
    
    getObjectTreeNodeDetails: function(treeNodeId, query, offset, pagesize, callback) {
      console.log("##############1 getObjectTreeNodeDetails "+treeNodeId);
      var fullUrl = jsContextPath + "/liste/detail/"+treeNodeId;
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
    
    getObjectTreeNodeChildren: function(treeNodeId, callback) {
      console.log("##############1 getObjectTreeNodeChildren "+treeNodeId);
      var fullUrl = jsContextPath + "/liste/children/"+treeNodeId;
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

    
  });
});