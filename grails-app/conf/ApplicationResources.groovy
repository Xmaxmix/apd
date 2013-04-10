modules = { 
    apd { 
        defaultBundle 'apd'
        dependsOn "cssscreen, javascript"
    }
    
    cssscreen {
        resource url:'/css/vendor/bootstrap.css', bundle: 'screen'
    }
    
    javascript {
        resource url:'/js/vendor/bootstrap.js'
    }
}