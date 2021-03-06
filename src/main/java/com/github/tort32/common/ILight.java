package com.github.tort32.common;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.github.tort32.common.animation.IAnimation;
import com.github.tort32.common.entity.LightColor;
import com.github.tort32.common.entity.LightState;

public interface ILight {
	
	LightState getState() throws IOException;
	
	void setColor(LightColor color, int duration, boolean doTcp) throws IOException;
	
	void setPower(boolean enable) throws IOException;
	
	String getSelector();
	
	InetSocketAddress getAddress();
	
	IAnimation getAnimation();
	
	void setAnimation(String animName);
}
