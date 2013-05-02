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

// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [
    all:           '*/*',
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          [
        'text/html',
        'application/xhtml+xml'
    ],
    js:            'text/javascript',
    json:          [
        'application/json',
        'text/json'
    ],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    xml:           [
        'text/xml',
        'application/xml'
    ]
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = [
    '/images/*',
    '/css/*',
    '/js/*',
    '/plugins/*'
]

// Diables bundling, caching, zipping of resources.
// TODO remove again after the basic website features are up and running.
//grails.resources.processing.enabled = false
grails.resources.bundle.excludes = ['**/*.*']
grails.resources.mappers.hashandcache.excludes = ['**/*.*']
grails.resources.mappers.bundle.excludes = ['**/*.*']
grails.resources.mappers.zip.excludes = ['**/*.*']

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

apd {
    backend {
        facets {
            filter = [
                [facetName:'language_fct', filter:'term:unknown' ],
                [facetName:'language_fct', filter:'term:termunknown']
            ]
        }
    }
}

environments {
    development {
        grails.logging.jul.usebridge = true
        grails.config.locations = [
            "file:${userHome}/.grails/${appName}.properties"
        ]

    }
    production {
        grails.logging.jul.usebridge = false
        grails.config.locations = [
            "file:"+ System.getProperty('catalina.base')+ "/grails/app-config/${appName}.properties"
        ]
    }
}

//APD SPECIFIC Configuration variables
//The variables can be overwritten by defining local configurations, see below environments
apd.binary.backend.url="http://www.binary-p1.deutsche-digitale-bibliothek.de/binary/"
// TODO: which one should we use?
//apd.static.url="http://static-p1.deutsche-digitale-bibliothek.de"
apd.static.url="http://dev.escidoc.org"  //temporary workaround for development until we have existing pages on the real backend server
apd.apis.url="http://localhost:8080"
apd.backend.url="http://backend-p1.deutsche-digitale-bibliothek.de:9998"
apd.ddb.url="http://www.deutsche-digitale-bibliothek.de"
//apd.backend.search.autocomplete.url="http://backend.deutsche-digitale-bibliothek.de:9998"
apd.logging.folder="target/logs"
//apd.tracking.piwikfile="${userHome}/.grails/tracking.txt"
apd.advancedSearch.searchGroupCount="3"
apd.advancedSearch.searchFieldCount="5"
apd.advancedSearch.defaultOffset="0"
apd.advancedSearch.defaultRows="20"

// log4j configuration
log4j = {

    // The appenders define the output method of the loggings
    appenders {
        console name: "console", threshold: org.apache.log4j.Level.INFO, layout:pattern(conversionPattern: "%-5p: %d{dd:MM:yyyy HH:mm:ss,SSS} %c: %m%n")
        rollingFile name: "apd-info", threshold: org.apache.log4j.Level.INFO, file: config.apd.logging.folder+"/apd-info.log", maxFileSize: "50MB", layout:pattern(conversionPattern: "%-5p: %d{dd:MM:yyyy HH:mm:ss,SSS} %c: %m%n")
        rollingFile name: "apd-warn", threshold: org.apache.log4j.Level.WARN, file: config.apd.logging.folder+"/apd-warn.log", maxFileSize: "50MB", layout:pattern(conversionPattern: "%-5p: %d{dd:MM:yyyy HH:mm:ss,SSS} %c: %m%n")
        rollingFile name: "apd-error", threshold: org.apache.log4j.Level.ERROR, file: config.apd.logging.folder+"/apd-error.log", maxFileSize: "50MB", layout:pattern(conversionPattern: "%-5p: %d{dd:MM:yyyy HH:mm:ss,SSS} %c: %m%n")
        rollingFile name: "stacktrace", threshold: org.apache.log4j.Level.ERROR, file: config.apd.logging.folder+"/apd-stacktrace.log", maxFileSize: "50MB", layout:pattern(conversionPattern: "%-5p: %d{dd:MM:yyyy HH:mm:ss,SSS} %c: %m%n")
    }

    // The root logger defines the basic log level and to which appenders the logging is going
    environments {
        development {
            root {  debug "console", "apd-info", "apd-warn", "apd-error", "stacktrace"  }
        }
        production {
            root { info "apd-info", "apd-warn", "apd-error", "stacktrace" }
        }
    }

    // This part can be used to filter out all loggings that are not interesting
    environments {
        development {
            warn    "org.codehaus.groovy.grails",               // only warnings or errors from grails
                    "grails.plugin",                            // only warnings or errors from grails.plugins
                    "org.grails.plugin",                        // only warnings or errors from plugins
                    "org.springframework",                      // only warnings or errors from spring
                    "net.jawr",                                 // only warnings or errors from jawr
                    "org.apache.catalina.core",                 // only warnings or errors from catalina core
                    "org.apache.coyote.http11.Http11Protocol",  // only warnings or errors from Http11Protocol
                    "org.apache.catalina.startup.ContextConfig" // only warnings or errors from ContextConfig

            error   "grails.util.GrailsUtil"                    // hide deprecated warnings on startup

        }
        production {
            //Don't filter messages in production
        }
    }
}

jawr {
    js {
        // Specific mapping to disable resource handling by plugin.
        mapping = '/jawr/'
        bundle {
            lib {
                // Bundle id is used in views.
                id = '/i18n/messages.js'

                // Tell which messages need to localized in Javascript.
                mappings = 'messages:grails-app.i18n.messages'
            }
        }
    }
    locale { // Define resolver so ?lang= Grails functionality works with controllers.
        resolver = 'net.jawr.web.resource.bundle.locale.SpringLocaleResolver' }
}

jquery {
    sources = 'vendor/jquery' // Holds the value where to store jQuery-js files /web-app/js/
    version = '1.9.1' // The jQuery version in use
}
