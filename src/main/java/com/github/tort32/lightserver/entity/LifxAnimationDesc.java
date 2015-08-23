package com.github.tort32.lightserver.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.tort32.lifx.server.animation.IAnimation;
import com.github.tort32.lifx.server.animation.IAnimation.AnimationDescriptor;
import com.github.tort32.lifx.server.animation.IAnimation.AnimationParam;
import com.wordnik.swagger.annotations.ApiModel;

@ApiModel(value = "LIFX light animation descriptor")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LifxAnimationDesc {
	
	public static final String NILL_ANIM_NAME = "none";
	
	@XmlElement(required = true)
	public String name;
	
	@XmlElement(required = true)
	public List<LifxAnimationParam> params;
	
	public LifxAnimationDesc(IAnimation anim) {
		if(anim != null) {
			AnimationDescriptor desc = anim.getDescriptor();
			this.name = desc.name;
			this.params = new ArrayList<LifxAnimationParam>();
			for(AnimationParam param : desc.params) {
				params.add(new LifxAnimationParam(param));
			}
		} else {
			this.name = NILL_ANIM_NAME;
			this.params = null; 
		}
	}
}
