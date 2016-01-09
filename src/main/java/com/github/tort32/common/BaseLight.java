package com.github.tort32.common;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.github.tort32.common.animation.AnimationFactory;
import com.github.tort32.common.animation.IAnimation;
import com.github.tort32.common.animation.LightAnimator;

public abstract class BaseLight implements ILight {
	
	protected transient InetSocketAddress address;
	protected transient LightAnimator animator;
	
	protected BaseLight(InetSocketAddress address) throws IOException {
		this.address = address;
		this.animator = new LightAnimator(this);
	}
	
	@Override
	public InetSocketAddress getAddress() {
		return address;
	}
	
	@Override
	public IAnimation getAnimation() {
		return animator.getAnimation();
	}
	
	@Override
	public void setAnimation(String animName) {
		IAnimation anim = AnimationFactory.createAnimationByName(animName);
		animator.setAnimation(anim);
	}
}
