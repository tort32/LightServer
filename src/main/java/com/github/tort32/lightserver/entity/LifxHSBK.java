package com.github.tort32.lightserver.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.tort32.lifx.protocol.HSBK;
import com.wordnik.swagger.annotations.ApiModel;

@ApiModel(value = "LIFX color")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LifxHSBK {

	@XmlElement(required = true)
	public int hue; // Range 0° and 360°
	
	@XmlElement(required = true)
	public int saturation; // Range 0% to 100%
	
	@XmlElement(required = true)
	public int brightness; // Range 0% to 100%
	
	@XmlElement(required = true)
	public int kelvin; // Range 2500° (warm) to 9000° (cool).
	
	protected LifxHSBK() {
		// Empty
	}
	
	public LifxHSBK(int hue, int saturation, int brightness, int kelvin) {
		this.hue = hue;
		this.saturation = saturation;
		this.brightness = brightness;
		this.kelvin = kelvin;
	}
	
	public LifxHSBK(HSBK color) {
		this.hue = color.getHue();
		this.saturation = color.getSaturation();
		this.brightness = color.getBrightness();
		this.kelvin = color.getKelvin();
	}
	
	public HSBK toHSBK() {
		return new HSBK(hue, saturation, brightness, kelvin);
	}
}
