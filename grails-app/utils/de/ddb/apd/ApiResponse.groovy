package de.ddb.apd

/**
 * Wrapper for all responses of the backend servers.
 * The response itself and additional meta informations about the request and response 
 * are added and available for further computation. The following data is available:
 * 
 * calledUrl: The called URL (e.g. http://backend-p1.deutsche-digitale-bibliothek.de:9998/access/AYKQ6FKHP6A7KFKCK2K3DP6HCVNZQEQC/components/view?client=AP)
 * method: The request method used (GET, POST)
 * content: The requested content type (TEXT, JSON, XML, BINARY)
 * response: The actual response from the server. Dependent of the requested content type, this can contain different object types. (String, InputStream, etc)
 * duration: The duration of the whole backend request
 * exception: If an exception has occured, it will be stored here. This happens also on 404 (ItemNotFoundException) and 500 (BackendErrorException)
 * status: The response status of type ApiResponse.HttpStatus (HTTP_200, HTTP_404, HTTP_500)
 * 
 * @author hla
 */
class ApiResponse {

    public static enum HttpStatus {
        HTTP_200, HTTP_404, HTTP_500
    }

    def calledUrl
    def method
    def content
    def response
    def duration
    def exception
    def status

    ApiResponse(calledUrl, method, content, response, duration, exception, status){
        this.calledUrl = calledUrl
        this.method = method
        this.content = content
        this.response = response
        this.duration = duration
        this.exception = exception
        this.status = status
    }

    def getCalledUrl() {
        return calledUrl
    }

    def getMethod() {
        return method
    }

    def getContent() {
        return content
    }

    def getResponse() {
        return response
    }

    def getDuration() {
        return duration
    }

    def getException() {
        return exception
    }

    def getStatus() {
        return status
    }

    def isOk() {
        return this.status == HttpStatus.HTTP_200
    }

    String toString() {
        def out = "ApiResponse: " + status + " / " + duration + "ms / " + method + " / " + content + " / URL='" + calledUrl+"'"
        if(exception){
            out += " / Exception='" + exception.getMessage()+"'"
        }
        return out
    }
}
