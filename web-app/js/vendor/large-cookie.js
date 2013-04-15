// LargeCookie provides a way to store larger than normal cookies (~4k+)
// Long values are split and stored in multiple cookies
LargeCookie = (function ($, defaultCookieService) {

    function PagedCookie(name, limit, cookieService) {
        this.prefix = name;
        this.indexCookie = name + '-n';
        this.limit = limit || 1300;
        this.cookieService = cookieService || defaultCookieService;
    }

    PagedCookie.prototype.set = function (value) {
        if (!value) {
            this.del();
            return;
        }
        
        // dont bother creating extra cookies for small values
        if (value.length <= this.limit) {
            this.del();
            this.cookieService.set(this.prefix + 0, value);
            return;
        }

        var currentIndex = 0;
        var prevLastIndex = parseInt(this.cookieService.get(this.indexCookie), 10) || 0;

        do {
            var part = value.substring(0, Math.min(this.limit, value.length));
            this.cookieService.set(this.prefix + currentIndex, part);
            value = value.substring(this.limit);
            currentIndex++;
        } while (value.length > 0);

        this.cookieService.set(this.indexCookie, currentIndex - 1);

        while (currentIndex - 1 < prevLastIndex) {
            this.del(currentIndex++);
        }
    };

    PagedCookie.prototype.get = function () {
        var result = "";
        var currentIndex = 0;
        var total = parseInt(this.cookieService.get(this.indexCookie)) || 0;
        do {
            result += this.cookieService.get(this.prefix + currentIndex++) || "";
        }
        while (currentIndex - 1 < total);
        return result;
    };

    PagedCookie.prototype.del = function (index) {
        if (typeof (index) != 'undefined' && !isNaN(parseInt(index, 10))) {
            this.cookieService.del(this.prefix + index);
        } else {
            var prevLastIndex = parseInt(this.cookieService.get(this.indexCookie), 10) || 0;

            this.cookieService.del(this.indexCookie);

            for (var i = prevLastIndex; i >= 0; i--) {
                this.cookieService.del(this.prefix + i);
            }
        }
    };

    return PagedCookie;
})(jQuery, jQuery.cookies);