package com.github.tort32.common.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.tort32.common.ILight;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Light endpoint")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class LightEndpoint {
	
	@XmlElement(required = true)
	@ApiModelProperty( value = "Device selector", required = true )
	public String selector;

	@XmlElement(required = true)
	@ApiModelProperty( value = "Device service IP", required = true )
	public String ip;

	@XmlElement(required = true)
	@ApiModelProperty( value = "Device service port", required = true )
	public int port;
	
	protected LightEndpoint() {
		// Empty
	}

	public LightEndpoint(ILight light) {
		this.selector = light.getSelector();
		this.ip = light.getAddress().getAddress().getHostAddress();
		this.port = light.getAddress().getPort();
	}
}