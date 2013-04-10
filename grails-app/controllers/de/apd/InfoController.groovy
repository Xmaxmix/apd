package de.apd

class InfoController {

    def index() {
        render(view: "info", model: [:])
    }
}
