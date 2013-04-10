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

        "/liste" {
            controller="listview"
            action="index"
        }

        "/item/$id/$name?" {
            controller="detailview"
            action="index"
        }

        "/info/$page" {
            controller="info"
            action="index"
        }

        "500"(controller: "error", action: "serverError")
        "500"(controller: "error", action: "uncaughtException", exception: Throwable)

        "404"(controller: "error", action: "notFound")
    }
}
