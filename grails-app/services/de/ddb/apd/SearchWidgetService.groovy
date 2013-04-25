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
package de.ddb.apd

/**
 * Set of services used in the SearchController for views/search
 * 
 * @author ema
 *
 */

class SearchWidgetService {

    def transactional=false
    
    def getFilterList(){
        
        def sparte = [
            "Staatliche Archive",
            "Kommunale Archive",
            "Kirchliche Archive",
            ["Evangelische Kirchenarchive","Katholische Kirchenarchive"],
            "Herrschafts- und Familienarchive",
            "Wirtschaftsarchive",
            "Archive der Parlamente, politischen Parteien, Stiftungen und Verbände",
            "Medienarchive",
            "Archive der Hochschulen sowie wissenschaftlicher Institutionen",
            "Sonstige Archive"
        ];
    
        def bundesland = [
            "Baden-Württemberg",
            "Bayern",
            "Berlin",
            "Brandenburg",
            "Bremen",
            "Hamburg",
            "Hessen",
            "Mecklenburg",
            "Vorpommern",
            "Niedersachsen",
            "Nordrhein-Westfalen",
            "Rheinland-Pfalz",
            "Saarland",
            "Sachsen",
            "Sachsen-Anhalt",
            "Schleswig-Hostein"
        ]
        
        def alphabet = ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"]
        return [sparte:sparte, bundesland: bundesland, alphabet:alphabet]
    }

}