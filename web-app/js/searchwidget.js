//Search widget
SearchWidget = function(form, widget, controls) {
    this.init(form, widget, controls);
}

$.extend(SearchWidget.prototype, {

    connectedForm : null,
    connectedControlsContainer : null,
    connectedWidgetContainer : null,
    selectedValues : new Array(),

    init : function(form, widget, controls) {
        currObjInstance = this;
        this.connectedForm = form;
        this.connectedWidgetContainer = widget;
        this.connectedControlsContainer = controls;
        this.selectedValues.push('default');

        this.connectedControlsContainer.find('select').each(
                function() {
                    $(this).change(
                            function() {
                                if ($(this).val() == "default") {
                                    $(this).addClass("empty");
                                } else {
                                    $(this).removeClass("empty");
                                }
                                var filterValue = $(this).find('option:selected').attr('value');
                                var content = this.value;
                                if (jQuery.inArray(filterValue,
                                        currObjInstance.selectedValues) == -1) {
                                    currObjInstance.renderSelection(filterValue, content);
                                    currObjInstance.selectedValues.push(filterValue);
                                }
                            });
                });

    },
    renderSelection : function(filterValue, content) {

        currObjInstance = this;

        // Create hidden input
        var hiddenInput = $(document.createElement('input'));
        hiddenInput.attr('type', 'hidden');
        hiddenInput.attr('name', 'filter')
        hiddenInput.attr('value', filterValue);
        this.connectedForm.append(hiddenInput)

        // Create pillbox
        var li = $(document.createElement('li'));
        li.attr('data-value', filterValue);
        li.html(content);
        this.connectedWidgetContainer.find('ul').append(li);
        li.mouseup(function() {
            currObjInstance.selectedValues = jQuery.grep(
                    currObjInstance.selectedValues, function(value) {
                        return value != filterValue;
                    });
        });
    }
});