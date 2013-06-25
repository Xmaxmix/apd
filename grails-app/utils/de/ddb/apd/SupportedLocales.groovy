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
package de.ddb.apd;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Utility Enum class for internationalization issues. It contains the supported locales of the project and offers
 * Methods to handle locales in the logic.
 * 
 * @author hla
 */
public enum SupportedLocales {

    DE(new Locale("de", "DE")), EN(new Locale("en", "EN"));

    private Locale locale;

    /**
     * Hidden constructor
     * @param locale The locale of this SupportedLocales object
     */
    SupportedLocales(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return this.locale
    }

    /**
     * Returns the default locale for the project. For APD it is "DE"
     * @return The "DE" locale
     */
    public static Locale getDefaultLocale() {
        return SupportedLocales.DE.locale;
    }

    /**
     * Returns the ISO2 String representation of the locale
     * @return the ISO2 String representation of the locale
     */
    public String getISO2() {
        return this.locale.getLanguage();
    }

    /**
     * Returns the ISO3 String representation of the locale
     * @return the ISO3 String representation of the locale
     */
    public String getISO3() {
        return this.locale.getISO3Language();
    }

    /**
     * Returns a list with all locales supported in this project
     * @return A list with all locales supported in this project
     */
    public static List<Locale> getSupportedLocales() {
        return SupportedLocales.values().collect {  it.locale }
    }

    /**
     * Returns a list with the ISO2 String representations of all supported locales in this project
     * @return A list with the ISO2 String representations of all supported locales in this project
     */
    public static List<String> getSupportedLanguagesISO2() {
        return SupportedLocales.values().collect { it.locale.getLanguage() }
    }

    /**
     * Returns a list with the ISO3 String representations of all supported locales in this project
     * @return A list with the ISO3 String representations of all supported locales in this project
     */
    public static List<String> getSupportedLanguagesISO3() {
        return SupportedLocales.values().collect { it.locale.getISO3Language() }
    }

    /**
     * Checks if the project supports a given locale. This is done by comparison of the ISO2 String 
     * representation of the locales. 
     * @param locale The locale to check
     * @return true if the locale is supported, false otherwise
     */
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

    /**
     * Returns a defined locale that best matches a given input locale. The returned locale is always on of 
     * the locales defined in this enum object. For example for a locale "de_CH" it would return the SupportedLocales.DE locale.
     * If no locale really matches, the default locale SupportedLocales.getDefaultLocale() is returned.
     * This method is usefull if a locale object comming from a user action (e.g. the URL param "?lang=de") has to be transfered 
     * to a defined state.
     * @param input A locale of any language
     * @return A locale supported by the project which matches best
     */
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
}
