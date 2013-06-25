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

import java.text.Normalizer;
import java.text.Normalizer.Form;

/**
 * A utility class which contains methods for String manipulation.
 * 
 * @author hla
 */
class StringManipulation {

    public final static int FRIENDLY_URL_STRING_MAX_LENGTH = 100

    /**
     * Takes an input string and returns a URL-friendly (aka Google-friendly) URL representation. 
     * All non-url compatible characters are replaced by "-" chars and all Umlaute are replaced by their 
     * two-character representation (ä -> ae). The String is capped after a defined length (see FRIENDLY_URL_STRING_MAX_LENGTH)
     * E.g. the String "Italien, Rom / Villa Aldobrandini, Außenansicht" is transformed to 
     * "Italien--Rom---Villa-Aldobrandini--Aussenansicht"
     * 
     * @param inputString The String to put into URL-friendly form
     * @return The URL-friendly form of the input String
     */
    static def getFriendlyUrlString(String inputString){
        if(!inputString || inputString.length() == 0){
            return ""
        }
        String keepUmlauteInputString = inputString.replaceAll("ä", "ae")
        keepUmlauteInputString = keepUmlauteInputString.replaceAll("Ä", "Ae")
        keepUmlauteInputString = keepUmlauteInputString.replaceAll("ö", "oe")
        keepUmlauteInputString = keepUmlauteInputString.replaceAll("Ö", "Oe")
        keepUmlauteInputString = keepUmlauteInputString.replaceAll("ü", "ue")
        keepUmlauteInputString = keepUmlauteInputString.replaceAll("Ü", "Ue")
        keepUmlauteInputString = keepUmlauteInputString.replaceAll("ß", "ss")
        String onlyAlphanumericalCharacters = keepUmlauteInputString.replaceAll("[^\\p{Alnum}]","-")
        String urlEncodedInputString = URLEncoder.encode(onlyAlphanumericalCharacters, "UTF-8")
        int maxStringLength = Math.min(FRIENDLY_URL_STRING_MAX_LENGTH, urlEncodedInputString.length())
        String cappedInputString = urlEncodedInputString.substring(0, maxStringLength)
        String minifiedInputString = cappedInputString.replaceAll("-+", "-")
        return minifiedInputString;
    }
}
