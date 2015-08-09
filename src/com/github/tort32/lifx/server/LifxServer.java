package com.github.tort32.lifx.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.github.tort32.lifx.device.recieve.StateService;
import com.github.tort32.lifx.device.send.GetService;
import com.github.tort32.lifx.protocol.message.Message;
import com.github.tort32.lifx.protocol.message.Payload;

public class LifxServer {
	
	public static final LifxServer INSTANCE = new LifxServer();
	
	public static final int SO_TIMEOUT = 1000; 
	public static final int BUFFER_SIZE = 1024;
	
	private Map<String, Light> lightsMap = new HashMap<String, Light>();
	
	public interface ResponseListener<T extends Payload> {
		void handle(T payload);
	}
	
	protected DatagramSocket socket;
	
	private LifxServer() {
		// Singleton
	}
	
	public void start() throws SocketException {
		socket = new DatagramSocket(StateService.DEFAULT_PORT);
		socket.setSoTimeout(SO_TIMEOUT);
	}
	
	public void stop() {
		socket.close();
	}
	
	public Collection<Light> discover() throws IOException {
		
		InetAddress broadcast = InetAddress.getByName("255.255.255.255");
		socket.setBroadcast(true);
		
		Message msg = new Message(new GetService()) {{
			setBroadcast();
			setSource(Message.SENDER_ID);
		}};
		byte[] send = msg.toArray();
		DatagramPacket sendPacket = new DatagramPacket(send, send.length, broadcast, StateService.DEFAULT_PORT);
		socket.send(sendPacket);
		
		byte[] buf = new byte[BUFFER_SIZE];
		DatagramPacket rcvPacket = new DatagramPacket(buf, buf.length);
		while(true) {
			try {
				socket.receive(rcvPacket);
			} catch(SocketTimeoutException timeout) {
				break;
			}
			try {
				byte[] received = rcvPacket.getData();
				Message rcvMsg = new Message(received);
				if (rcvMsg.mPayload instanceof StateService
						&& Message.SENDER_ID == rcvMsg.mFrame.mSource.getValue()) {
					StateService payload = (StateService) rcvMsg.mPayload;
					if (StateService.Service.UDP.getValue() == payload.mService.getValue()) {
						String ip = rcvPacket.getAddress().getHostAddress();
						String mac = rcvMsg.mFrameAddress.mTarget.getHexValue();
						int port = (int) payload.mPort.getValue();
						lightsMap.put(mac, new Light(this, mac, ip, port));
					}
				}
			} catch(RuntimeException e) {
				continue; // Not ours data
			}
		}
		
		socket.setBroadcast(false);
		
		return lightsMap.values();
	}
	
	public Light getLight(String mac) {
		return lightsMap.get(mac);
	}
	
	public void send(Light light, Message message) throws IOException {
		send(light, message, null);
	}
	
	public void send(Light light, Payload payload) throws IOException {
		send(light, payload, null);
	}
	
	public <T extends Payload> T send(Light light, Payload payload, Class<T> responseType) throws IOException {
		Message msg = new Message(payload);
		msg.setSource(Message.SENDER_ID);
		msg.setTarget(light.mac);
		return send(light, msg, responseType);
	}
	
	public <T extends Payload> T send(Light light, Message message, Class<T> responseType) throws IOException {
		synchronized(socket) {
			byte[] send = message.toArray();
			DatagramPacket sendPacket = new DatagramPacket(send, send.length, light.ip, light.port);
			socket.send(sendPacket);	
			if (responseType == null) {
				return null;
			}
			return recieve(message, responseType);
		}  
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Payload> T recieve(Message message, Class<T> responseType) throws IOException {
		byte[] buf = new byte[BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		while(true) {
			try {
				socket.receive(packet);
			} catch(SocketTimeoutException timeout) {
				break;
			}
			try {
				byte[] received = packet.getData();
				Message rcvMsg = new Message(received);
				if(message.mFrame.mSource.getValue() != rcvMsg.mFrame.mSource.getValue()) {
					continue;
				}
				if (rcvMsg.mPayload.getClass() != responseType) {
					continue;
				}
				return (T) rcvMsg.mPayload;
			} catch(RuntimeException e) {
				continue; // Not ours data
			}
		}
		return null;
	}
}
