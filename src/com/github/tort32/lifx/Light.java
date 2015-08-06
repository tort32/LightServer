package com.github.tort32.lifx;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.github.tort32.lifx.device.recieve.StatePower;
import com.github.tort32.lifx.device.send.GetPower;
import com.github.tort32.lifx.light.recieve.State;
import com.github.tort32.lifx.light.send.Get;
import com.github.tort32.lifx.light.send.SetColor;
import com.github.tort32.lifx.protocol.HSBK;
import com.github.tort32.lifx.protocol.message.Message;
import com.github.tort32.lightserver.entity.LifxEndpoint;

public class Light {
	
	protected InetAddress ip;
	protected int port;
	protected String mac;

	public Light(LifxEndpoint endpoint) throws IOException {
		ip = InetAddress.getByName(endpoint.ip);
		port = endpoint.port;
		mac = endpoint.mac; 
	}
	
	public boolean isOnline() throws IOException {
		UdpSocket socket = new UdpSocket(ip, port);
		
		socket.send(new Message(new GetPower()) {{
			setSource(Message.SENDER_ID);
			setTarget(mac);
		}});
		
		final AtomicBoolean online = new AtomicBoolean(false);
		socket.receive((msg) -> {
			if (Message.SENDER_ID == msg.mFrame.mSource.getValue()) {
				if (msg.mPayload instanceof StatePower) {
					online.set(true);
					return true;
				}
			}
			return false;
		});
		
		socket.close();
		
		return online.get();
	}
	
	public HSBK getColor() throws IOException {
		UdpSocket socket = new UdpSocket(ip, port);
		
		socket.send(new Message(new Get()) {{
			setSource(Message.SENDER_ID);
			setTarget(mac);
		}});
		
		final AtomicReference<HSBK> value = new AtomicReference<HSBK>();
		socket.receive((msg) -> {
			if (Message.SENDER_ID == msg.mFrame.mSource.getValue()) {
				if (msg.mPayload instanceof State) {
					State state = (State) msg.mPayload;
					value.set(state.mColor);
					return true;
				}
			}
			return false;
		});
		
		socket.close();
		
		return value.get();
	}
	
	public void setColor(HSBK color, int duration) throws IOException {
		UdpSocket socket = new UdpSocket(ip, port);
		
		socket.send(new Message(new SetColor(false, color, duration)) {{
			setSource(Message.SENDER_ID);
			setTarget(mac);
		}});
		
		socket.close();
	}
}
