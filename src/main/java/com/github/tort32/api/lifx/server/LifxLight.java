package com.github.tort32.api.lifx.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import com.github.tort32.api.lifx.protocol.HSBK;
import com.github.tort32.api.lifx.protocol.device.recieve.Acknowledgement;
import com.github.tort32.api.lifx.protocol.device.recieve.StatePower;
import com.github.tort32.api.lifx.protocol.device.send.GetPower;
import com.github.tort32.api.lifx.protocol.device.send.SetPower;
import com.github.tort32.api.lifx.protocol.light.recieve.State;
import com.github.tort32.api.lifx.protocol.light.send.Get;
import com.github.tort32.api.lifx.protocol.light.send.SetColor;
import com.github.tort32.api.lifx.protocol.message.Message;
import com.github.tort32.common.BaseLight;
import com.github.tort32.common.entity.LightColor;
import com.github.tort32.common.entity.LightState;

public class LifxLight extends BaseLight {
	
	protected LifxServer server;
	public String mac;
	
	LifxLight(LifxServer server, InetAddress ip, int port, String mac) throws IOException {
		super(new InetSocketAddress(ip, port));
		this.server = server;
		this.mac = mac;
	}
	
	public static String getSelector(String mac) {
		return mac; // just MAC
	}
	
	@Override
	public String getSelector() {
		return LifxLight.getSelector(mac);
	}
	
	public boolean isOnline() throws IOException {
		// Check we got a response
		return (getPower() != null);
	}
	
	@Override
	public LightState getState() throws IOException {
		State state = server.send(this, new Get(), State.class);
		if (state == null) {
			return null;
		}
		return new LightState(state);
	}
	
	public StatePower getPower() throws IOException {
		return server.send(this, new GetPower(), StatePower.class);
	}
	
	@Override
	public void setColor(LightColor color, int duration, boolean doTcp) throws IOException {
		HSBK col = color.toHSBK();
		Message msg = new Message(new SetColor(false, col, duration));
		msg.setSource(Message.SENDER_ID);
		msg.setTarget(mac);
		msg.requestAcknowledgement();
		if (doTcp) {
			Acknowledgement ack = null;
			int i = 0;
			do {
				msg.setSequenceNumber(i++);
				ack = server.send(this, msg, Acknowledgement.class);
			} while(ack == null);
		} else {
			server.send(this, new SetColor(false, col, duration));
		}
	}
	
	@Override
	public void setPower(boolean enable) throws IOException {
		server.send(this, new SetPower(enable));
	}
}
