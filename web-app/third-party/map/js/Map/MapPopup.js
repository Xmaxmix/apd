/*
* MapPopup.js
*
* Copyright (c) 2012, Stefan Jänicke. All rights reserved.
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 3 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
* MA 02110-1301  USA
*/

/**
 * @class MapPopup
 * map popup implementaion
 * @author Stefan Jänicke (stjaenicke@informatik.uni-leipzig.de)
 * @release 1.0
 * @release date: 2012-07-27
 * @version date: 2012-07-27
 */
function MapPopup(parent) {

	this.parentDiv = parent.gui.mapWindow;

	var popup = this;

	this.initialize = function(x, y, onclose) {

		this.x = x;
		this.y = y;

		this.popupDiv = document.createElement("div");
		this.popupDiv.setAttribute('class', 'ddbPopupDiv');
		this.popupDiv.onclick = function() {
			if ( typeof onclose != 'undefined') {
				onclose();
			}
			popup.reset();
		}
		this.parentDiv.appendChild(this.popupDiv);

		this.headDiv = document.createElement("div");
		this.headDiv.setAttribute('class', 'ddbPopupHead');

		this.title = document.createElement("div");
		this.title.setAttribute('class', 'ddbPopupTitle');

		this.cancel = document.createElement("div");
		this.cancel.setAttribute('class', 'ddbPopupCancel');
		this.cancel.title = GeoTemConfig.getString('close');
		this.cancel.onclick = function() {
			if ( typeof onclose != 'undefined') {
				onclose();
			}
			popup.reset();
		}

        this.headDiv.appendChild(this.title);
        this.headDiv.appendChild(this.cancel);


		this.input = document.createElement("div");
  		this.input.style.maxWidth = Math.floor(this.parentDiv.offsetWidth * 0.75) + "px";
  		this.input.style.maxHeight = Math.floor(this.parentDiv.offsetHeight * 0.75) + "px";
		this.input.setAttribute('class', 'ddbPopupInput');

		this.popupDiv.appendChild(this.headDiv);
		this.popupDiv.appendChild(this.input);

	}

    this.makeAndPlacePeak = function(orientation) {
		var peak = document.createElement("div");
		peak.setAttribute('class', 'popupPeak'+orientation);
		peak.title = GeoTemConfig.getString('close');
		peak.onclick = function() {
			if ( typeof onclose != 'undefined') {
				onclose();
			}
			popup.reset();
		}

		this.popupDiv.appendChild(peak);

    }

	this.setContent = function(content) {
		$(this.input).empty();
		this.visible = true;
		$(this.input).append(content);
		this.decorate();
	}

	this.reset = function() {
		$(this.popupDiv).remove();
		this.visible = false;
	}

	this.decorate = function() {
        var divHeight=  this.popupDiv.offsetHeight;
        var divWidth =  this.popupDiv.offsetWidth ;

        var orientation = ["NO","SO","NW","SW"];

		this.left = this.x; // NO: + 9;
		this.top = this.y - this.popupDiv.offsetHeight; // NO: - 10;
        var bitval = 0;
        if( this.top < 10) {
            this.top = this.top  + divHeight + 10;
            bitval = 1;
        } else {
            this.top = this.top - 10;
            bitval = 0;
        }
        if(this.left + divWidth > 730 - 9) {
            bitval +=2;
            this.left = this.left - divWidth - 9;
        } else {
            this.left += 9; 
        }

		this.popupDiv.style.left = this.left + "px";
		this.popupDiv.style.top = this.top + "px";

        this.makeAndPlacePeak(orientation[bitval]);

		var shiftX = 0, shiftY = 0;
		if (this.popupDiv.offsetTop < parent.gui.headerHeight + 10) {
			shiftY = -1 * (parent.gui.headerHeight + 10 - this.popupDiv.offsetTop);
		}
		if (this.popupDiv.offsetLeft + this.popupDiv.offsetWidth > parent.gui.headerWidth - 10) {
			shiftX = -1 * (parent.gui.headerWidth - 10 - this.popupDiv.offsetLeft - this.popupDiv.offsetWidth);
		}
		parent.shift(shiftX, shiftY);
	}

	this.shift = function(x, y) {
		this.left = this.left - this.x + x;
		this.top = this.top - this.y + y;
		this.x = x;
		this.y = y;
		if (this.left + this.popupDiv.offsetWidth > this.parentDiv.offsetWidth) {
			this.popupDiv.style.left = 'auto';
			this.popupDiv.style.right = (this.parentDiv.offsetWidth - this.left - this.popupDiv.offsetWidth) + "px";
		} else {
			this.popupDiv.style.right = 'auto';
			this.popupDiv.style.left = this.left + "px";
		}
		this.popupDiv.style.top = this.top + "px";
	}
}
