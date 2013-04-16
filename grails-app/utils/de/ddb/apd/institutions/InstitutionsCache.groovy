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

package de.ddb.apd.institutions

import java.security.MessageDigest;
import org.apache.commons.codec.binary.Base64;


/**
 * This class builds a static cache (used from all request-theads -> THREADSAFETY!) for the institution list request
 * to safe request time and bandwidth. It contains three data field: the cache itself, containing the Json-Array from 
 * the backend, the etag containing the last etag received from the backend and the hash of the cache-object in 
 * url-safe BASE64 encoding.
 * 
 * @author hla
 */
class InstitutionsCache {

    private def cache
    private def etag = ""+System.currentTimeMillis()
    private def hash = ""+System.currentTimeMillis()

    synchronized def updateCache(Object institutions, String etag) {
        def institutionsListString = ""
        if(institutions){
            institutionsListString = institutions.toString()
        }
        this.cache = institutions
        this.etag = etag

        byte[] bytesOfMessage = institutionsListString.getBytes("UTF-8")
        MessageDigest md = MessageDigest.getInstance("MD5")
        byte[] urlUnsafeHash = md.digest(bytesOfMessage)
        hash = Base64.encodeBase64URLSafeString(urlUnsafeHash)
    }

    def hasChanged(hashId){
        return hash != hashId
    }

    private def setHash() {
        // prevent unsynchronized setting!
    }

    private def setEtag() {
        // prevent unsynchronized setting!
    }

    private def setCache() {
        // prevent unsynchronized setting!
    }

    private def getHash() {
        return hash
    }

    private def getEtag() {
        return etag
    }

    private def getCache() {
        return cache
    }
}
