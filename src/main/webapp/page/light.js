var current = {
	bulb: null,
	state: {
		color: {hue: 0, saturation: 100, brightness: 100, kelvin: 6600},
		power: false,
		label: "Label"
	}
};

var updatePickers = function() {};
var updatePreview = function() {};

var apiPath = "/api/light/";

function discoverBulbs() {
	$.get(apiPath + "discover",
		function(bulbs) {
			updateBulbList(bulbs);
		});
	/*var bulbs = [
		{selector:"0B281E50A054", ip:"127.0.1.7", port:56700},
		{selector:"0B281E50A055", ip:"127.0.1.8", port:56700},
		{selector:"0B281E50A056", ip:"127.0.1.9", port:56700},
		{selector:"0B281E50A057", ip:"127.0.1.234", port:56700},
		{selector:"0B281E50A058", ip:"127.0.1.124", port:56700},
		{selector:"0B281E50A059", ip:"127.0.1.212", port:56700},
		{selector:"0B281E50A060", ip:"127.0.1.10", port:56700},
		{selector:"0B281E50A061", ip:"127.0.1.11", port:56700},
		{selector:"0B281E50A062", ip:"127.0.1.12", port:56700},
		{selector:"0B281E50A063", ip:"127.254.254.254", port:56700}
	];
	updateBulbList(bulbs);*/
}

function readLightState() {
	if (current.bulb === null) return;
	$.get(apiPath + current.bulb.selector + "/state",
		function(state) {
			updateState(state);
		});
	/*var state = {
	  "color": {
		"hue": 213,
		"saturation": 100,
		"brightness": 75,
		"kelvin": 3500
	  },
	  "power": true,
	  "label": "test label\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
	};
	updateState(state);*/
};

function readAnimState() {
	if (current.bulb === null) return;
	$.get(apiPath + current.bulb.selector + "/animation",
		function(desc) {
			updateAnimationPanel(desc);
		});
	/*var desc = {
	  "name": "random",
	  "params": [
		{
		  "type": "range",
		  "name": "duration",
		  "desc": "Animation step (ms)",
		  "value": "1000",
		  "min": "100",
		  "max": "30000",
		},
		{
		  "type": "range",
		  "name": "transition",
		  "desc": "Color transition (ms)",
		  "value": "100",
		  "min": "100",
		  "max": "30000",
		},
		{
		  "type": "range",
		  "name": "brightness",
		  "desc": "Color brightness",
		  "value": "50",
		  "min": "0",
		  "max": "100",
		},
		{
		  "type": "checkbox",
		  "name": "randSaturation",
		  "desc": "Randomize stauration",
		  "value": "false",
		},
		{
		  "type": "select",
		  "name": "selector",
		  "desc": "Choose one",
		  "options": ['one', 'two', 'three'],
		  "value": "two"
		}
	  ]
	};
	updateAnimationPanel(desc);*/
}

function sendLightColor() {
	if (current.bulb === null) return;
	var setColor = {
		"color": current.state.color,
		"duration": 100
	}
	$.ajax({
		type: "PUT",
		url: apiPath + current.bulb.selector + "/color",
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
		url: apiPath + current.bulb.selector + "/power",
		dataType: "json",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(setPower)
    });
}

function loadAnimations() {
	$.get(apiPath + "animations",
		function(anims) {
			updateAnimations(anims);
		});
	/*var anims = ['none', 'random', 'anim1'];
	updateAnimations(anims);*/
}

function updateAnimations(anims) {
	var animations = $("#animations");
	$.each(anims, function() {
		animations.append($("<option />").val(this).text(this));
	});
	//animations.change(function() {changeAnim($( this ).val())});
}

function selectBulb(event) {
	current.bulb = event.data;
	$('#bulbList .bulbItem').removeClass('selectedBulbItem');
	$("#" + current.bulb.selector).addClass('selectedBulbItem');
	readLightState();
	readAnimState();
}

function updateBulbList(bulbs) {
	$('#bulbList .bulbItem').remove(); // remove all items
	for (var i in bulbs) {
		var bulb = bulbs[i];
		var bulbElement = $('#bulbTemplate').clone(true);
		bulbElement.removeClass("bulbItemTemplate").addClass("bulbItem");
		bulbElement.attr('id', bulb.selector);
		var name = bulb.selector + " " + bulb.ip + ":" + bulb.port;
		bulbElement.find("#bulbName").text(name).click(bulb, selectBulb);
		bulbElement.appendTo('#bulbList');
	}
	// Select first item
	if (bulbs.length > 0) {
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

function updateAnimationPanel(desc) {
	$("#animations").change(function() {});
	$("#animations").val(desc.name);
	$("#animations").change(function() {changeAnim($( this ).val())});
	var panel = $("#animPanel");
	panel.empty(); // Remove all
	
	// TODO use template elements
	
	panel.append($("<div />").text(desc.name));
	for (var i in desc.params) {
		var param = desc.params[i];
		panel.append($("<div />").addClass("controlLabel").text(param.desc));
		if (param.type === "checkbox") {
			var div = $("<div />", {class: "controlSlider"});
			var input = $("<input/>", {
					name: param.name,
					type: "checkbox"
				});
			if (param.value === "true") {
				input.attr('checked', true);
			}
			input.change(function() {changeAnimParam($( this ).attr("name"), $( this ).is(':checked'))});
			div.append(input);
			panel.append(div);
		} else if (param.type === "range") {
			var div = $("<div />", {class: "controlSlider"});
			var input = $("<input/>", {
					name: param.name,
					type: "range",
					max: param.max,
					min: param.min,
					value: param.value
				});
			input.change(function() {changeAnimParam($( this ).attr("name"), $( this ).val())});
			div.append(input);
			panel.append(div);
			panel.append($("<div />").addClass("controlValue").text(param.value));
		} else if (param.type === "select") {
			var div = $("<div />", {class: "controlSlider"});
			var input = $("<select />").attr("name", param.name);
			input.change(function() {changeAnimParam($( this ).attr("name"), $( this ).val())});
			$.each(param.options, function() {
				var option = $("<option />").val(this).text(this)
				if (this == param.value) {
					option.attr("selected", "selected");
				}
				input.append(option);
			});
			div.append(input);
			panel.append(div);
		}
	}
}

function changeAnim(name) {
	var setAnim = {
		"name": name,
	};
	$.ajax({
		type: "PUT",
		url: apiPath + current.bulb.selector + "/animation",
		dataType: "json",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(setAnim),
		success: updateAnimationPanel
    });
}

function changeAnimParam(name, value) {
	var setParam = {
		"name": name,
		"value": value
	};
	$.ajax({
		type: "PUT",
		url: apiPath + current.bulb.selector + "/animation/param",
		dataType: "json",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(setParam),
		success: updateAnimationPanel
    });
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

function RGBA2COL(c) {
	return "rgba(" + c.r + "," + c.g + "," + c.b + "," + c.a + ")";
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
	var a = s / 100;
	var b = 1 - a;
	// Color components weighted by saturation
	// Saturation = 100 is not impacted by color temperature
	c.r = Math.round(hsb.r * a + klv.r * b);
	c.g = Math.round(hsb.g * a + klv.g * b);
	c.b = Math.round(hsb.b * a + klv.b * b);
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
	updatePreview();
	updatePickers();
	if (send) {
		sendLightColor();
	}
}

function updateCurrentPower() {
	current.state.power = $('#powerChecker').is(':checked');
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
	discoverBulbs();
	setControlPickers();
	setColorPreview();
	loadAnimations();
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
	
	// Set size for proper canvas rendering
	brtPicker[0].width = brtWidth;
	brtPicker[0].height = brtHeight;
	
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

function setColorPreview() {

	var colPreview = $('#currentColor');
	var colWidth = Math.round(colPreview.width());
	var colHeight = Math.round(colPreview.height());
	var colPosX = colWidth / 2;
	var colPosY = colHeight / 2;
	
	// Set size for proper canvas rendering
	colPreview[0].width = colWidth;
	colPreview[0].height = colHeight;
	
	var ctx2 = colPreview[0].getContext("2d");
	var colMaxRadius = Math.max(colWidth, colHeight) / 2;
	
	updatePreview = function (col) {
		var col	= current.state.color;
		var rad = colMaxRadius * col.brightness / 50;
		var rad2 = Math.max(rad * 2, 50);
		var c = HSBK2RGB(col.hue, col.saturation, 100, col.kelvin);
		c.a = Math.max(col.brightness / 100, 0.5);

		var fill = ctx2.createRadialGradient(colPosX, colPosY, 0, colPosX, colPosY, rad2);
		fill.addColorStop(0, RGBA2COL(c));
		fill.addColorStop(1, "transparent");
		var fill2 = ctx2.createRadialGradient(colPosX, colPosY, 0, colPosX, colPosY, rad * 0.5);
		c.a = 1;
		fill2.addColorStop(0, RGBA2COL(c));
		fill2.addColorStop(1, "transparent");
		
		ctx2.fillStyle = 'black';
		ctx2.globalCompositeOperation = "source-over";
		ctx2.fillRect(0, 0, colWidth, colHeight);
		ctx2.fillStyle = fill;
		ctx2.globalCompositeOperation = "screen";
		ctx2.fillRect(0, 0, colWidth, colHeight);
		ctx2.fillStyle = fill2;
		ctx2.globalCompositeOperation = "screen";
		ctx2.fillRect(0, 0, colWidth, colHeight);
	}
}