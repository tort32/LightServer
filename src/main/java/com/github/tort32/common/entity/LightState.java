package com.github.tort32.common.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.tort32.api.lifx.protocol.light.recieve.State;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Light state")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LightState {

	@XmlElement(required = true)
	@ApiModelProperty(value = "Current color", required = true)
	public LightColor color;
	
	@XmlElement(required = true)
	@ApiModelProperty(value = "Current power state", required = true)
	public boolean power;
	
	@XmlElement(required = true)
	@ApiModelProperty(value = "Device label", required = true)
	public String label;
	
	protected LightState() {
		// Empty
	}
	
	public LightState(LightColor color, boolean power, String label) {
		this.color = color;
		this.power = power;
		this.label = label;
	}
	
	public LightState(State state) {
		this.color = new LightColor(state.mColor);
		this.power = state.mPower.isOn();
		this.label = state.mLabel.getValue();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName() + " {\n");
		sb.append("  color=" + color + "\n");
		sb.append("  power=" + power + "\n");
		sb.append("  label=" + label + "\n");
		sb.append("}");
		return sb.toString();
	}

}