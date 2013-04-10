package de.apd

class IndexController {

    def index() {
        render(view: "index", model: [isStartValue:1])
    }
}
