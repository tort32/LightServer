package com.github.tort32.common.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.tort32.api.lifx.protocol.HSBK;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Light color")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LightColor {

	@XmlElement(required = true)
	@ApiModelProperty(value = "Color hue in the range from 0 to 360 degrees", required = true)
	public int hue; // Range 0° and 360°
	
	@XmlElement(required = true)
	@ApiModelProperty(value = "Color saturations in the range from 0 to 100 percents", required = true)
	public int saturation; // Range 0% to 100%
	
	@XmlElement(required = true)
	@ApiModelProperty(value = "Color brightness in the range from 0 to 100 percents", required = true)
	public int brightness; // Range 0% to 100%
	
	@XmlElement(required = true)
	@ApiModelProperty(value = "Color temperature in the range from 2500 to 9000 degrees", required = true)
	public int kelvin; // Range 2500° (warm) to 9000° (cool).
	
	public static final int KELVIN_DEFAULT = 3500;
	
	protected LightColor() {
		// Empty
	}
	
	public LightColor(int hue, int saturation, int brightness) {
		this.hue = hue;
		this.saturation = saturation;
		this.brightness = brightness;
		this.kelvin = HSBK.KELVIN_DEFAULT;
	}
	
	public LightColor(int hue, int saturation, int brightness, int kelvin) {
		this.hue = hue;
		this.saturation = saturation;
		this.brightness = brightness;
		this.kelvin = kelvin;
	}
	
	public LightColor(HSBK color) {
		this.hue = color.getHue();
		this.saturation = color.getSaturation();
		this.brightness = color.getBrightness();
		this.kelvin = color.getKelvin();
	}
	
	public HSBK toHSBK() {
		return new HSBK(hue, saturation, brightness, kelvin);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName() + " {\n");
		sb.append("  hue=" + hue + "\n");
		sb.append("  saturation=" + saturation + "\n");
		sb.append("  brightness=" + brightness + "\n");
		sb.append("  kelvin=" + kelvin + "\n");
		sb.append("}");
		return sb.toString();
	}
}
