//Initialization of search widget
if ($('body').find('#home-search-container').length > 0) {
    var searchWidgetContainer = $('#search-widget-form');
    searchWidget = new SearchWidget($('#search-widget-form'),searchWidgetContainer, searchWidgetContainer.find('.controls-container'));
}