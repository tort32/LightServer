var lifxBulbs;
var curBulb = null;

function loadLifxBilbs() {
	var data = localStorage['lifxBulbs'];
	if (typeof(data) == 'undefined' || data === null) return [];
	return JSON.parse(data);
}

function saveLifxBilbs(bulbs) {
	localStorage.setItem('lifxBulbs', JSON.stringify(bulbs));
}

function selectBulb(event) {
	curBulb = event.data;
	$('#bulbList .bulbItem').removeClass('selectedBulbItem');
	$("#" + curBulb.mac).addClass('selectedBulbItem');
	getLightState(curBulb);
}

function addBulb(event) {
	var bulb = event.data;
	// Remove if already added
	lifxBulbs = jQuery.grep(lifxBulbs, function (item, index) { return item.mac != bulb.mac; });
	// Add item
	lifxBulbs.push(bulb);
	saveLifxBilbs(lifxBulbs);
	updateBulbList(lifxBulbs);
}

function removeBulb(event) {
	var bulb = event.data;
	// Remove item by mac
	lifxBulbs = jQuery.grep(lifxBulbs, function (item, index) { return item.mac != bulb.mac; });
	saveLifxBilbs(lifxBulbs);
	updateBulbList(lifxBulbs);
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
		bulbElement.find("#bulbRemove").click(bulb, removeBulb);
		bulbElement.appendTo('#bulbList');
	}
}

function updateDiscoverBulbList(bulbs) {
	$('#discoverBulbList .bulbItem').remove(); // remove all items
	for (var i in bulbs) {
		var bulb = bulbs[i];
		var bulbElement = $('#bulbDiscoverTemplate').clone(true);
		bulbElement.removeClass("bulbItemTemplate").addClass("bulbItem");
		bulbElement.attr('id', bulb.mac);
		var name = bulb.mac + " " + bulb.ip + ":" + bulb.port;
		bulbElement.find("#bulbName").text(name);
		bulbElement.click(bulb, addBulb);
		bulbElement.appendTo('#discoverBulbList');
	}
}

function discoverLights() {
	$.get("/api/lifx/discover",
		function(bulbs) {
			updateDiscoverBulbList(bulbs);
		});
	/*var bulbs = [
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
	updateDiscoverBulbList(bulbs);*/
};

function getLightState(bulb) {
	$.get("/api/lifx/" + bulb.mac + "/state",
		function(state) {
			updateState(state.color, state.power, state.label);
		});
	/*var state = {
	  "color": {
		"hue": 213,
		"saturation": 100,
		"brightness": 26,
		"kelvin": 2500
	  },
	  "power": true,
	  "label": "test label\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
	};
	updateState(state.label, state.power, state.color);*/
};

function setLightColor(bulb, h, s, b, k) {
	if (bulb === null) return;
	var setColor = {
		"color": {
			"hue": h,
			"saturation": s,
			"brightness": b,
			"kelvin": k
		},
		"duration": 100
	}
	$.ajax({
		type: "PUT",
		url: "/api/lifx/" + bulb.mac + "/color",
		dataType: "json",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(setColor)
    });
}

function setLightPower(bulb, power) {
	if (bulb === null) return;
	var setPower = {
		"power": power,
	}
	$.ajax({
		type: "PUT",
		url: "/api/lifx/" + bulb.mac + "/power",
		dataType: "json",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(setPower)
    });
}

function updateState(color, power, label) {
	console.log(color);
	$('#hValue').text(color.hue.toString());
	$('#sValue').text(color.saturation.toString());
	$('#bValue').text(color.brightness.toString());
	$('#kValue').text(color.kelvin.toString());
	$('#hSlider').val(color.hue.toString());
	$('#sSlider').val(color.saturation.toString());
	$('#bSlider').val(color.brightness.toString());
	$('#kSlider').val(color.kelvin.toString());
	$('#powerChecker').prop('checked', power);
	$('#powerValue').text(power ? "On" : "Off");
	$('#currentLabel').html(label);
	updateCurrentColor(false);
}

HSB2RGB = function (h, s, b) {
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
    return "rgb(" + c.r + "," + c.g + "," + c.b + ")";
}

K2RGB = function (k) {
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
    return "rgb(" + c.r + "," + c.g + "," + c.b + ")";
}

function updateCurrentColor(sendColor) {
	var h = $('#hSlider').val();
	var s = $('#sSlider').val();
	var b = $('#bSlider').val();
	var k = $('#kSlider').val();
	$('#currentColor').css({backgroundColor: HSB2RGB(h, s, b)});
	$('#currentKelvin').css({backgroundColor: K2RGB(k)});
	if (sendColor) {
		setLightColor(curBulb, h, s, b, k);
	}
}

function updateCurrentPower() {
	var power = $('#powerChecker').is(':checked');
	setLightPower(curBulb, power);
}

function setPower(checked) {
	$('#powerValue').text(checked ? "On" : "Off");
	updateCurrentPower();
}

function setHue(val) {
	$('#hValue').text(val.toString());
	updateCurrentColor(true);
}

function setSaturation(val) {
	$('#sValue').text(val.toString());
	updateCurrentColor(true);
}

function setBrightness(val) {
	$('#bValue').text(val.toString());
	updateCurrentColor(true);
}

function setKelvin(val) {
	$('#kValue').text(val.toString());
	updateCurrentColor(true);
}

$(function () {
	// Page loaded
	$('#buttonDiscover').click(discoverLights);
	lifxBulbs = loadLifxBilbs();
	updateBulbList(lifxBulbs);
})