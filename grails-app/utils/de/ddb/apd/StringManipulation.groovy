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

class StringManipulation {

    public final static int FRIENDLY_URL_STRING_MAX_LENGTH = 100

    static def getFriendlyUrlString(String inputString){
        if(!inputString || inputString.length() == 0){
            return ""
        }
        String keepUmlauteInputString = inputString.replaceAll("ä", "ae")
        keepUmlauteInputString = inputString.replaceAll("Ä", "Ae")
        keepUmlauteInputString = inputString.replaceAll("ö", "oe")
        keepUmlauteInputString = inputString.replaceAll("Ö", "Oe")
        keepUmlauteInputString = inputString.replaceAll("ü", "ue")
        keepUmlauteInputString = inputString.replaceAll("Ü", "Ue")
        keepUmlauteInputString = inputString.replaceAll("ß", "ss")
        String onlyAlphanumericalCharacters = keepUmlauteInputString.replaceAll("[^\\p{Alnum}]","-")
        String urlEncodedInputString = URLEncoder.encode(onlyAlphanumericalCharacters, "UTF-8")
        int maxStringLength = Math.min(FRIENDLY_URL_STRING_MAX_LENGTH, urlEncodedInputString.length())
        String cappedInputString = urlEncodedInputString.substring(0, maxStringLength)
        return cappedInputString;
    }
}
