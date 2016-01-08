package com.github.tort32.common.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.tort32.common.animation.IAnimation;
import com.github.tort32.common.animation.IAnimation.AnimationDescriptor;
import com.github.tort32.common.animation.IAnimation.AnimationParam;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Light animation descriptor")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LightAnimationDesc {
	
	public static final String NILL_ANIM_NAME = "none";
	
	@XmlElement(required = true)
	@ApiModelProperty(value = "Animation name", required = true)
	public String name;
	
	@XmlElement(required = true)
	@ApiModelProperty(value = "List of animation parameters", required = true)
	public List<LightAnimationParam> params;
	
	public LightAnimationDesc(IAnimation anim) {
		if (anim != null) {
			AnimationDescriptor desc = anim.getDescriptor();
			this.name = desc.name;
			this.params = new ArrayList<LightAnimationParam>();
			for (AnimationParam<?> param : desc.params) {
				params.add(new LightAnimationParam(param));
			}
		} else {
			this.name = NILL_ANIM_NAME;
			this.params = null; 
		}
	}
}
