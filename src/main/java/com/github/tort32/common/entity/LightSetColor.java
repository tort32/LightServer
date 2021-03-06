package com.github.tort32.common.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Light color to set")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LightSetColor {
	
	@XmlElement(required = true)
	@ApiModelProperty(value = "Color to set", required = true)
	public LightColor color;
	
	@XmlElement(required = true)
	@ApiModelProperty(value = "Duration of color transition in ms", required = true)
	public int duration;
}