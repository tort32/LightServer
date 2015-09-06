package com.github.tort32.lightserver.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.tort32.lifx.server.animation.IAnimation.AnimationParam;
import com.github.tort32.lifx.server.animation.IAnimation.CheckerParam;
import com.github.tort32.lifx.server.animation.IAnimation.RangeParam;
import com.github.tort32.lifx.server.animation.IAnimation.SelectorParam;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "LIFX light animation parameter")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LifxAnimationParam {
	
	@XmlElement(required = true)
	@ApiModelProperty(value = "Type of parameter", allowableValues="checkbox, range, select", required = true)
	public String type;
	
	@XmlElement(required = true)
	@ApiModelProperty(value = "Name of parameter", required = true)
	public String name;
	
	@XmlElement(required = true)
	@ApiModelProperty(value = "Description of parameter", required = true)
	public String desc;
	
	@XmlElement(required = true)
	@ApiModelProperty(value = "Current value", required = true)
	public String value;
	
	@XmlElement(required = false)
	@ApiModelProperty(value = "Minimal value of range", required = true)
	public String min;
	
	@XmlElement(required = false)
	@ApiModelProperty(value = "Maximal value of range", required = true)
	public String max;
	
	@XmlElement(required = false)
	@ApiModelProperty(value = "Possible value options for selector", required = true)
	public String[] options;
	
	LifxAnimationParam(AnimationParam<?> param) {
		this.type = param.type.getValue();
		this.name = param.name;
		this.desc = param.desc;
		if (param instanceof CheckerParam) {
			CheckerParam p = (CheckerParam) param;
			this.value = String.valueOf(p.get());
		} else if (param instanceof RangeParam) {
			RangeParam p = (RangeParam) param;
			this.min = String.valueOf(p.minValue);
			this.max = String.valueOf(p.maxValue);
			this.value = String.valueOf(p.get());
		} else if (param instanceof SelectorParam) {
			SelectorParam p = (SelectorParam) param;
			this.options = p.options;
			this.value = String.valueOf(p.get());
		}
	}
}