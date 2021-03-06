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
import org.apache.log4j.Logger;

import groovy.util.logging.Log4j;
import groovy.xml.StreamingMarkupBuilder

/**
 * This callback will be triggered when the dynamically generated web.xml file is finished. We can modify it programmatically to 
 * add further functionality like listeners or mime-types.
 */
eventWebXmlEnd = {String tmpfile ->
    def log = Logger.getLogger(this.getClass());
    log.info "Dynamically adjusting web.xml in /scripts/_Events.groovy"
    
    def root = new XmlSlurper().parse(webXmlFile)
    
    log.info "Adding security filter (de.ddb.apd.security.ApdSecurityFilter) to web.xml"
    root.appendNode {
        'filter' {
            'filter-name' ('ApdSecurityFilter')
            'filter-class' ('de.ddb.apd.security.ApdSecurityFilter')
        }
    }
    root.appendNode {
        'filter-mapping' {
            'filter-name' ('ApdSecurityFilter')
            'url-pattern' ('/*')
        }
    }

    log.info "Adding session listener (de.ddb.apd.ApdSessionListener) to web.xml"
    root.appendNode {
        'listener' {
            'listener-class' (
            'de.ddb.apd.ApdSessionListener'
            )
        }
    }
    
//    log.info "Adding mime-mapping (woff -> application/x-font-woff) to web.xml"
//    root.appendNode {
//        'mime-mapping' {
//            'extension' (
//                'woff'
//            )
//            'mime-type' (
//                'application/x-font-woff'
//            )
//        }
//    }
        
    webXmlFile.text = new StreamingMarkupBuilder().bind {
        mkp.declareNamespace("": "http://java.sun.com/xml/ns/javaee")
        mkp.yield(root)
    }
}
