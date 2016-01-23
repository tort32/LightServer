package com.github.tort32.common.animation;

import com.github.tort32.common.entity.LightColor;

public abstract class BaseAnimation implements IAnimation {
	
	protected AnimationDescriptor desc;
	protected AnimationFrame frame;
	
	public BaseAnimation() {
		frame = new AnimationFrame();
	}
	
	@Override
	public String getName() {
		return desc.name;
	}

	@Override
	public AnimationDescriptor getDescriptor() {
		return desc;
	}

	@Override
	public void setInitColor(LightColor color) {
		// Empty by default
	}

}
