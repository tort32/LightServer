package com.github.tort32.lightserver.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.tort32.lifx.server.animation.IAnimation.AnimationParam;
import com.github.tort32.lifx.server.animation.IAnimation.AnimationParamType;
import com.wordnik.swagger.annotations.ApiModel;

@ApiModel(value = "LIFX light animation parameter")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LifxAnimationParam {
	
	@XmlElement(required = true)
	public AnimationParamType type;
	
	@XmlElement(required = true)
	public String name;
	
	@XmlElement(required = true)
	public String curValue;
	
	@XmlElement(required = false)
	public String minValue;
	
	@XmlElement(required = false)
	public String maxValue;
	
	@XmlElement(required = false)
	public String[] values;
	
	LifxAnimationParam(AnimationParam param) {
		this.type = param.type;
		this.name = param.name;
		this.curValue = param.curValue;
		this.minValue = param.minValue;
		this.maxValue = param.maxValue;
		this.values = param.values;
	}
}