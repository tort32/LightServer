package com.github.tort32.lifx.server;

import java.io.IOException;
import java.net.InetAddress;

import com.github.tort32.lifx.device.recieve.StatePower;
import com.github.tort32.lifx.device.send.GetPower;
import com.github.tort32.lifx.device.send.SetPower;
import com.github.tort32.lifx.light.recieve.State;
import com.github.tort32.lifx.light.send.Get;
import com.github.tort32.lifx.light.send.SetColor;
import com.github.tort32.lifx.protocol.HSBK;
import com.github.tort32.lifx.server.animation.AnimationFactory;
import com.github.tort32.lifx.server.animation.IAnimation;
import com.github.tort32.lifx.server.animation.LightAnimator;

public class Light {
	
	protected LifxServer server;
	protected String mac;
	protected transient InetAddress ip;
	protected transient int port;
	protected transient LightAnimator animator;
	
	Light(LifxServer server, String mac, String ip, int port) throws IOException {
		this.server = server;
		this.mac = mac;
		this.ip = InetAddress.getByName(ip);
		this.port = port;
		this.animator = new LightAnimator(this);
	}
	
	public boolean isOnline() throws IOException {
		// Check we got a response
		return (getPower() != null);
	}
	
	public State getState() throws IOException {
		return server.send(this, new Get(), State.class);
	}
	
	public StatePower getPower() throws IOException {
		return server.send(this, new GetPower(), StatePower.class);
	}
	
	public void setColor(HSBK color, int duration) throws IOException {
		server.send(this, new SetColor(false, color, duration));
	}
	
	public void setPower(boolean enable) throws IOException {
		server.send(this, new SetPower(enable));
	}
	
	public String getMac() {
		return mac;
	}
	
	public String getIp() {
		return ip.getHostAddress();
	}
	
	public int getPort() {
		return port;
	}
	
	public IAnimation getAnimation() {
		return animator.getAnimation();
	}
	
	public void setAnimation(String animName) {
		IAnimation anim = AnimationFactory.createAnimationByName(animName);
		animator.setAnimation(anim);
	}
}
