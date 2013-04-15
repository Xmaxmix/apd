Archivportal Deutschland
========================

APD is a beta-web interface for Archiveportal Deutschland.

[Git](https://github.com/Deutsche-Digitale-Bibliothek/apd) | [Jira](https://jira.deutsche-digitale-bibliothek.de/browse/APD) | Demo Coming Soon

-----

# Information for developers 

The APD is a Web Application build on Grails.


## Download 

The project is still under beta development. 
There are no ''war-s'' for download war-s released yet. 



### Configuration 
No special configurations right now

### Developer Info 
#### Development Environment 
Developed with [Groovy/Grails Tool Suite](http://www.grails.org/products/ggts) ([download tool link](http://www.springsource.org/groovy-grails-tool-suite-download)). 
The Grails Tool Suite is an Eclipse based application with built in support for Grails and [vFabric tc Server](http://www.vmware.com/products/application-platform/vfabric-tcserver/overview.html) a neat solution to help developers launch apps easily.

##### Grails Project does not show "controllers", "services" and other elements in IDE!?
Open the .classpath file (Ctlr+Shift+R -> enter .classpath) and add the following: 
hange the .classpath adding the paths for the entries we want.
 
For everybody, the .classpath file should have the following:
```<classpathentry kind="src" path="grails-app/conf"/>
    <classpathentry kind="src" path="grails-app/controllers"/>
    <classpathentry kind="src" path="grails-app/domain"/>
    <classpathentry kind="src" path="grails-app/services"/>
    <classpathentry kind="src" path="grails-app/taglib"/>
    <classpathentry kind="src" path="grails-app/utils"/>
    <classpathentry kind="src" path="test/unit"/> ```

#### Github 
You are probably on Github, or you received this file from there

#### Usages 
In order to run the app from Grails Tools Suite a developer can navigate to the project, right click and Select ''Run As'' > ''Grails Command (run-app)'' or ''Grails Command (test-app)''
This operation is the same as a direct execution from the command line on the

    grails run-app

To run Selenium tests use the command

    grails run-selenium

#### External Configurations
In case additional configurations are needed, then the following apply:
In development mode the external configuration is located on: $USER_HOME/.grails/$appname.properties 
In Production Mode: /grails/app-config/$appname.properties

####Proxy configuration 
In development environment, proxies are read from $USER_HOME/.grails/ProxySettings.groovy by default.


####Tomcat configuration
The tomcat configuration in the server.xml must ensure, that the used Connector must contain a valid URIEncoding tag.

  <Connector [...] URIEncoding="UTF-8" /> 
 

