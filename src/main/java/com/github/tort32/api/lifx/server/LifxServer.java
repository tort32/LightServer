package com.github.tort32.api.lifx.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tort32.api.lifx.protocol.Types;
import com.github.tort32.api.lifx.protocol.device.recieve.StateService;
import com.github.tort32.api.lifx.protocol.device.send.GetService;
import com.github.tort32.api.lifx.protocol.message.Message;
import com.github.tort32.api.lifx.protocol.message.Payload;

public class LifxServer {
	
	private static Logger logger = LoggerFactory.getLogger(LifxServer.class);
	
	public static final LifxServer INSTANCE = new LifxServer();
	
	public static final int SO_TIMEOUT = 100; 
	public static final int BUFFER_SIZE = 1024;
	
	private Map<String, LifxLight> lightsMap = new HashMap<String, LifxLight>();
	
	public interface ResponseListener<T extends Payload> {
		void handle(T payload);
	}
	
	protected DatagramSocket socket;
	
	private LifxServer() {
		// Singleton
	}
	
	public void start(InetAddress laddr) throws SocketException {
		socket = new DatagramSocket(StateService.DEFAULT_PORT, laddr);
		socket.setSoTimeout(SO_TIMEOUT);
	}
	
	public void start() throws SocketException {
		socket = new DatagramSocket(StateService.DEFAULT_PORT);
		socket.setSoTimeout(SO_TIMEOUT);
	}
	
	public void stop() {
		socket.close();
	}
	
	public Collection<LifxLight> discover() throws IOException {
		
		InetAddress broadcast = InetAddress.getByName("255.255.255.255");
		socket.setBroadcast(true);
		
		Message msg = new Message(new GetService()) {{
			setBroadcast();
			setSource(Message.SENDER_ID);
		}};
		byte[] send = msg.toArray();
		logger.trace("Sent: " + Types.dumpBytes(send));
		logger.trace(msg.toString());
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
				logger.trace("Received: " + Types.dumpBytes(received));
				logger.trace(rcvMsg.toString());
				if (rcvMsg.mPayload instanceof StateService
						&& Message.SENDER_ID == rcvMsg.mFrame.mSource.getValue()) {
					StateService payload = (StateService) rcvMsg.mPayload;
					if (StateService.Service.UDP.getValue() == payload.mService.getValue()) {
						InetAddress ip = rcvPacket.getAddress();
						String mac = rcvMsg.mFrameAddress.mTarget.getHexValue();
						int port = (int) payload.mPort.getValue();
						String selector = LifxLight.getSelector(mac);
						if (!lightsMap.containsKey(selector)) {
							lightsMap.put(selector, new LifxLight(this, ip, port, mac));
						}
					}
				}
			} catch(RuntimeException e) {
				continue; // Not ours data
			}
		}

		socket.setBroadcast(false);
		
		return lightsMap.values();
	}
	
	public Collection<LifxLight> getLights() {
		return lightsMap.values();
	}
	
	public LifxLight getLight(String selector) {
		return lightsMap.get(selector);
	}
	
	public void send(LifxLight light, Message message) throws IOException {
		send(light, message, null);
	}
	
	public void send(LifxLight light, Payload payload) throws IOException {
		send(light, payload, null);
	}
	
	public <T extends Payload> T send(LifxLight light, Payload payload, Class<T> responseType) throws IOException {
		Message msg = new Message(payload);
		msg.setSource(Message.SENDER_ID);
		msg.setTarget(light.mac);
		return send(light, msg, responseType);
	}
	
	public <T extends Payload> T send(LifxLight light, Message message, Class<T> responseType) throws IOException {
		synchronized(socket) {
			byte[] send = message.toArray();
			DatagramPacket sendPacket = new DatagramPacket(send, send.length, light.getAddress());
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
