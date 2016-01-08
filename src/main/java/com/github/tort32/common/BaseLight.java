package com.github.tort32.common;

import java.io.IOException;
import java.net.InetAddress;

import com.github.tort32.common.animation.AnimationFactory;
import com.github.tort32.common.animation.IAnimation;
import com.github.tort32.common.animation.LightAnimator;

public abstract class BaseLight implements ILight {
	
	protected String mac;
	protected transient InetAddress ip;
	protected transient int port;
	protected transient LightAnimator animator;
	
	protected BaseLight(String mac, InetAddress ip, int port) throws IOException {
		this.mac = mac;
		this.ip = ip;
		this.port = port;
		this.animator = new LightAnimator(this);
	}
	
	@Override
	public String getMac() {
		return mac;
	}
	
	@Override
	public InetAddress getIp() {
		return ip;
	}
	
	@Override
	public int getPort() {
		return port;
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
