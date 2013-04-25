//Initialization of search widget
if ($('body').find('#home-search-container').length > 0) {
    searchWidget = new SearchWidget($('#search-widget-form'),
            $('#search-widget'), $('#search-widget')
                    .find('.controls-container'));
}