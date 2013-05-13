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

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?"{ constraints {
                // apply constraints here
            } }

        "/" {
            controller="index"
            action="index"
        }

        "/suche" {
            controller="advancedsearch"
            action="index"
        }

        "/struktur" {
            controller="structureview"
            action="index"
        }

        "/struktur/item/$id" {
            controller="structureview"
            action="show"
        }

        "/struktur/detail/$id" {
            controller="structureview"
            action="getTreeNodeDetails"
        }

        "/struktur/children/$id" {
            controller="structureview"
            action="getTreeNodeChildren"
        }

        "/struktur/root" {
            controller="structureview"
            action="getTreeRootItems"
        }

        "/liste" {
            controller="objectview"
            action="index"
        }

        "/liste/detail/$id" {
            controller="objectview"
            action="getTreeNodeDetails"
        }

        "/liste/children/$id" {
            controller="objectview"
            action="getTreeNodeChildren"
        }

        "/liste/root" {
            controller="objectview"
            action="getTreeRootItems"
        }

        "/liste/objectcount/$id" {
            controller="objectview"
            action="getTreeNodeObjectCount"
        }

        "/liste/getLowerLevel/$id" {
            controller="objectview"
            action="getSecondLevelNodes"
        }

        "/liste/getinstitutionparent/$id" {
            controller="objectview"
            action="getTopParentInstitution"
        }

        "/institutions/$action/$hashId?" {
            controller="institutions"
            action="getAjaxListFull"
        }

        "/institutions/outdated/$hashId?" {
            controller="institutions"
            action="isAjaxListFullOutdated"
        }

        "/item/$id/$name?" {
            controller="detailview"
            action="index"
        }

        "/info/$page" {
            controller="info"
            action="index"
        }

        "/binary/$filename**" {
            controller="apis"
            action="index"
        }

        "404"(controller: "error", action: "notFound")

        "500"(controller: "error", action: "notFound", exception: de.ddb.apd.exception.ItemNotFoundException)
        "500"(controller: "error", action: "serverError", exception: de.ddb.apd.exception.ConfigurationException)
        "500"(controller: "error", action: "serverError", exception: de.ddb.apd.exception.BackendErrorException)
        "500"(controller: "error", action: "serverError")
    }
}
