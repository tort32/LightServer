package com.github.tort32.api.nodemcu.server;

import java.io.IOException;
import java.net.InetAddress;

import com.github.tort32.api.nodemcu.protocol.RawColor;
import com.github.tort32.common.BaseLight;
import com.github.tort32.common.entity.LightColor;
import com.github.tort32.common.entity.LightState;

public class EspLight extends BaseLight {
	
	protected EspServer server;
	protected String chipId;
	
	EspLight(EspServer server, String mac, InetAddress ip, String chipId) throws IOException {
		super(mac, ip, EspServer.DEFAULT_PORT);
		this.server = server;
		this.chipId = chipId;
	}
	
	@Override
	public LightState getState() throws IOException {
		String result = server.send(this, "G");
		// OK 123
		if (!result.startsWith("OK ")) {
			return null;
		}
		String[] parts = result.trim().split(" ");
		if(parts.length != 4) {
			return null;
		}
		int rawR = 0, rawG = 0, rawB = 0; // 0..1023
		try {
			rawR = Integer.parseInt(parts[1]);
			rawG = Integer.parseInt(parts[2]);
			rawB = Integer.parseInt(parts[3]);
		} catch(NumberFormatException e) {
			return null;
		}
		RawColor color = new RawColor(rawR, rawG, rawB);
		boolean isOn = (rawR != 0 && rawG != 0 && rawB != 0);
		return new LightState(color.toLightHSBK(), isOn, chipId);
	}
	
	@Override
	public void setColor(LightColor color, int duration, boolean doTcp) throws IOException {
		RawColor rawColor = new RawColor(color);
		setValue(rawColor, doTcp);
	}
	
	@Override
	public void setPower(boolean enable) throws IOException {
		setValue(new RawColor(), true);
	}
	
	private void setValue(RawColor color, boolean doTcp) throws IOException {
		String msg = String.format("S%d,%d,%d", color.rawR, color.rawG, color.rawB);
		if (doTcp) {
			int i = 0;
			do {
				String result = server.send(this, msg);
				if (result.startsWith("OK")) {
					break; // done successfully
				}
				i++;
			} while(i <= 10); // lets try 10 times
		} else {
			server.send(this, msg);
		}
	}
}