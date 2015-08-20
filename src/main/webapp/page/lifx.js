var current = {
	bulb: null,
	state: {
		color: {hue: 0, saturation: 100, brightness: 100, kelvin: 6600},
		power: false,
		label: "Label"
	}
};

var updatePickers = function() {}

function discoverLifxBulbs() {
	/*$.get("/api/lifx/discover",
		function(bulbs) {
			updateBulbList(bulbs);
		});*/
	var bulbs = [
		{mac:"0B281E50A054", ip:"127.0.1.7", port:56700},
		{mac:"0B281E50A055", ip:"127.0.1.8", port:56700},
		{mac:"0B281E50A056", ip:"127.0.1.9", port:56700},
		{mac:"0B281E50A057", ip:"127.0.1.234", port:56700},
		{mac:"0B281E50A058", ip:"127.0.1.124", port:56700},
		{mac:"0B281E50A059", ip:"127.0.1.212", port:56700},
		{mac:"0B281E50A060", ip:"127.0.1.10", port:56700},
		{mac:"0B281E50A061", ip:"127.0.1.11", port:56700},
		{mac:"0B281E50A062", ip:"127.0.1.12", port:56700},
		{mac:"0B281E50A063", ip:"127.254.254.254", port:56700}
	];
	updateBulbList(bulbs);
}

function readLightState() {
	if (current.bulb === null) return;
	/*$.get("/api/lifx/" + current.bulb.mac + "/state",
		function(state) {
			updateState(state);
		});*/
	var state = {
	  "color": {
		"hue": 213,
		"saturation": 100,
		"brightness": 75,
		"kelvin": 3500
	  },
	  "power": true,
	  "label": "test label\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
	};
	updateState(state);
};

function sendLightColor() {
	if (current.bulb === null) return;
	var setColor = {
		"color": current.state.color,
		"duration": 100
	}
	$.ajax({
		type: "PUT",
		url: "/api/lifx/" + current.bulb.mac + "/color",
		dataType: "json",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(setColor)
    });
}

function sendLightPower() {
	if (current.bulb === null) return;
	var setPower = {
		"power": current.state.power,
	}
	$.ajax({
		type: "PUT",
		url: "/api/lifx/" + current.bulb.mac + "/power",
		dataType: "json",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(setPower)
    });
}

function selectBulb(event) {
	current.bulb = event.data;
	$('#bulbList .bulbItem').removeClass('selectedBulbItem');
	$("#" + current.bulb.mac).addClass('selectedBulbItem');
	readLightState();
}

function updateBulbList(bulbs) {
	$('#bulbList .bulbItem').remove(); // remove all items
	for (var i in bulbs) {
		var bulb = bulbs[i];
		var bulbElement = $('#bulbTemplate').clone(true);
		bulbElement.removeClass("bulbItemTemplate").addClass("bulbItem");
		bulbElement.attr('id', bulb.mac);
		var name = bulb.mac + " " + bulb.ip + ":" + bulb.port;
		bulbElement.find("#bulbName").text(name).click(bulb, selectBulb);
		bulbElement.appendTo('#bulbList');
	}
	// Select first item
	if(bulbs.length > 0) {
		selectBulb({data: bulbs[0]});
	}
}

function updateState(state) {
	current.state = state;
	$('#powerChecker').prop('checked', state.power);
	$('#powerValue').text(state.power ? "On" : "Off");
	$('#currentLabel').html(state.label);
	updateCurrentColor(false);
}

function HSB2RGB(h, s, b) {
	var c = {};
	var t2 = (100 - s) * b / 100;
	var t3 = (s * b / 100) * (h % 60) / 60;
	var i = Math.floor(h / 60);
	switch (i % 6) {
		case 6:
		case 0: c.r = b; c.b = t2; c.g = t2 + t3; break;
		case 1: c.g = b; c.b = t2; c.r = b - t3; break;
		case 2: c.g = b; c.r = t2; c.b = t2 + t3; break;
		case 3: c.b = b; c.r = t2; c.g = b - t3; break;
		case 4: c.b = b; c.g = t2; c.r = t2 + t3; break;
		case 5: c.r = b; c.g = t2; c.b = b - t3; break;
	}
	c.r = Math.round(c.r * 255 / 100);
	c.g = Math.round(c.g * 255 / 100);
	c.b = Math.round(c.b * 255 / 100);
    return c;
}

function K2RGB(k) {
	var c = {};
    if (k < 6600) {
        c.r = 255;
		c.g = 100 * Math.log(k) - 620;
		c.b = 200 * Math.log(k) - 1500;
	} else {
        c.r = 480 * Math.pow(k - 6000, -0.1);
		c.g = 400 * Math.pow(k - 6000, -0.07);
		c.b = 255;
    }
	c.r = Math.round(Math.min(Math.max(c.r, 0), 255));
	c.g = Math.round(Math.min(Math.max(c.g, 0), 255));
	c.b = Math.round(Math.min(Math.max(c.b, 0), 255));
    return c;
}

function RGB2COL(c) {
	return "rgb(" + c.r + "," + c.g + "," + c.b + ")";
}

function updateCurrentColorFromSliders(send) {
	current.state.color.hue = $('#hSlider').val();
	current.state.color.saturation = $('#sSlider').val();
	current.state.color.brightness = $('#bSlider').val();
	current.state.color.kelvin = $('#kSlider').val();
	updateCurrentColor(send);
}

function HSBK2RGB(h, s, b, k) {
	var c = {};
	var hsb = HSB2RGB(h, s, b);
	var klv = K2RGB(k);
	c.r = Math.round(hsb.r * klv.r / 255);
	c.g = Math.round(hsb.g * klv.g / 255);
	c.b = Math.round(hsb.b * klv.b / 255);
	return c;
}

function updateCurrentColor(send) {
	var col = current.state.color;
	$('#hValue').text(col.hue.toString());
	$('#sValue').text(col.saturation.toString());
	$('#bValue').text(col.brightness.toString());
	$('#kValue').text(col.kelvin.toString());
	$('#hSlider').val(col.hue.toString());
	$('#sSlider').val(col.saturation.toString());
	$('#bSlider').val(col.brightness.toString());
	$('#kSlider').val(col.kelvin.toString());
	var c = RGB2COL(HSBK2RGB(col.hue, col.saturation, col.brightness, col.kelvin));
	$('#currentColor').css({backgroundColor: c});
	updatePickers();
	if (send) {
		sendLightColor();
	}
}

function updateCurrentPower() {
	var power = $('#powerChecker').is(':checked');
	sendLightPower();
}

function setPower(checked) {
	$('#powerValue').text(checked ? "On" : "Off");
	updateCurrentPower();
}

function setHue(val) {
	$('#hValue').text(val.toString());
	updateCurrentColorFromSliders(true);
}

function setSaturation(val) {
	$('#sValue').text(val.toString());
	updateCurrentColorFromSliders(true);
}

function setBrightness(val) {
	$('#bValue').text(val.toString());
	updateCurrentColorFromSliders(true);
}

function setKelvin(val) {
	$('#kValue').text(val.toString());
	updateCurrentColorFromSliders(true);
}

$(function () {
	// Page loaded
	discoverLifxBulbs();
	setControlPickers();
})

function setControlPickers() {
	
	var huePicker = $('#huePicker');
	var brtPicker = $('#brtPicker');
	var klvPicker = $('#klvPicker');
	
	var hueRad = Math.floor(huePicker.width() / 2);
	var brtWidth = Math.floor(brtPicker.width());
	var brtHeight = Math.floor(brtPicker.height());
	var klvWidth = Math.floor(klvPicker.width());
	var klvHeight = Math.floor(klvPicker.height());
	
	pick2hs = function (i, j, size) {
		var c = {};
		var dx = (i - size) / size; // (-1, +1)
		var dy = (j - size) / size; // (-1..+1)
		var r = Math.pow(dx * dx + dy * dy, 0.85);
		c.s = Math.min(r * 100, 100); // (0..100)
		var th = Math.atan2(dy, dx); // (-PI..+PI)
		c.h = 180 * th / Math.PI + 180; // (0..360)
		return c;
	};
	
	pick2brt = function (i, size) {
		var dx = i / (size - 1); // (0, 1)
		return dx * 100; // (0..100)
	};
	
	pick2klv = function (i, size) {
		var dx = i / (size - 1); // (0, 1)
		var min = 2500;
		var max = 9000;
		return dx * (max - min) + min; // (min..max)
	};
	
	var genBrtPicker = function (imgData, width, height) {
		for (var i = 0; i < width; ++i) {
			for (var j = 0; j < height; ++j) {
				var brt = pick2brt(i, width);
				var c = HSB2RGB(current.state.color.hue, current.state.color.saturation, brt);
				var offset = (i + j * width) * 4;
				imgData.data[offset + 0] = c.r;
				imgData.data[offset + 1] = c.g;
				imgData.data[offset + 2] = c.b;
				imgData.data[offset + 3] = 255;
			}
		}
		return imgData;
	};
	
	// Set size for proper canvas rendering
	brtPicker[0].width = brtWidth;
	brtPicker[0].height = brtHeight;
    var ctx = brtPicker[0].getContext("2d");
	var imgData = ctx.createImageData(brtWidth, brtHeight);
    imgData = genBrtPicker(imgData, brtWidth, brtHeight);
    ctx.putImageData(imgData, 0, 0);
	
	updatePickers = function () {
		imgData = genBrtPicker(imgData, brtWidth, brtHeight);
		ctx.putImageData(imgData, 0, 0);
	};
	
	huePicker.click(function(e) {
			var canvasX = Math.floor(e.pageX - huePicker[0].offsetLeft);
			var canvasY = Math.floor(e.pageY - huePicker[0].offsetTop);
			var hs = pick2hs(canvasX, canvasY, hueRad);
			current.state.color.hue = Math.round(hs.h);
			current.state.color.saturation = Math.round(hs.s);
			updateCurrentColor(true);
		});
    
    brtPicker.click(function(e) {
			var canvasX = Math.floor(e.pageX - brtPicker[0].offsetLeft);
			var brt = pick2brt(canvasX, brtWidth)
			current.state.color.brightness = Math.round(brt);
			updateCurrentColor(true);
		});
		
	klvPicker.click(function(e) {
			var canvasX = Math.floor(e.pageX - klvPicker[0].offsetLeft);
			var klv = pick2klv(canvasX, klvWidth);
			current.state.color.kelvin = Math.round(klv);
			updateCurrentColor(true);
		});
}