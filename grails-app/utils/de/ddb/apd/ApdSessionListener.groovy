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

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.log4j.Logger;


class ApdSessionListener implements HttpSessionListener {

    private Logger log = Logger.getLogger(this.getClass())

    // called by servlet container upon session creation
    void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession()
        log.error "A serverside session was created. This should not happen!"
        def stackElements = Thread.currentThread().getStackTrace()
        for(stackElement in stackElements){
            log.error "Stack: "+stackElement.getClassName()+"."+stackElement.getMethodName()+" ["+stackElement.getLineNumber()+"]"
        }
    }

    // called by servlet container upon session destruction
    void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession()
        log.info "A serverside session was destroyed."
    }
}
