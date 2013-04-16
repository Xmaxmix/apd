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
    },
	
    getFullInstitutionsList: function(callback) {

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
    }
  });
});