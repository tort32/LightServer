package com.github.tort32.lightserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LifxEndpoint {

	@XmlElement
	public String ip;

	@XmlElement
	public int port;

	public LifxEndpoint(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

}