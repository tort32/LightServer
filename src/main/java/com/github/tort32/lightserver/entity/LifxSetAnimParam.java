package com.github.tort32.lightserver.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "LIFX set light animation param")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LifxSetAnimParam {
	
	@XmlElement(required = true)
	@ApiModelProperty(value = "Name of animation parameter", required = true)
	public String name;
	
	@XmlElement(required = true)
	@ApiModelProperty(value = "Param value to set", required = true)
	public String value;
}