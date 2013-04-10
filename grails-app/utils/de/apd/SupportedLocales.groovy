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
package de.apd;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public enum SupportedLocales {

    DE(new Locale("de", "DE")), EN(new Locale("en", "EN"));

    private Locale locale;

    SupportedLocales(Locale locale) {
        this.locale = locale;
    }

    public static Locale getDefaultLocale() {
        return SupportedLocales.EN.locale;
    }

    public String getISO2() {
        return this.locale.getLanguage();
    }

    public String getISO3() {
        return this.locale.getISO3Language();
    }

    public static List<Locale> getSupportedLocales() {
        return SupportedLocales.values().collect {  it.locale }
    }

    public static List<String> getSupportedLanguagesISO2() {
        return SupportedLocales.values().collect { it.locale.getLanguage() }
    }

    public static List<String> getSupportedLanguagesISO3() {
        return SupportedLocales.values().collect { it.locale.getISO3Language() }
    }

    public static boolean supports(Locale locale) {
        if (locale == null) {
            return false;
        }
        String language = locale.getLanguage();
        SupportedLocales[] supported = SupportedLocales.values();
        for (SupportedLocales support : supported) {
            if (support.locale.getLanguage().equals(language)) {
                return true;
            }
        }
        return false;
    }

    public static Locale getBestMatchingLocale(Locale input){
        Locale locale = input
        if(!locale){
            locale = SupportedLocales.getDefaultLocale()
        }
        if(!SupportedLocales.supports(locale)){
            locale = SupportedLocales.getDefaultLocale()
        }
        return locale
    }

    public static Locale getDefinedLocale(Locale locale) {
        if (locale == null) {
            return null;
        }
        String language = locale.getLanguage();
        SupportedLocales[] supported = SupportedLocales.values();
        for (SupportedLocales support : supported) {
            if (support.locale.getLanguage().equals(language)) {
                return support.locale;
            }
        }
        return null;
    }
}
