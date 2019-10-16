package com.github.tort32.api.nodemcu.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.github.tort32.api.lifx.protocol.message.Payload;

public class EspServer {
	
	public static final EspServer INSTANCE = new EspServer();
	
	public static final int SO_TIMEOUT = 500;
	public static final int BUFFER_SIZE = 64;
	public static final int PORT = 34500;
	
	private Map<String, EspLight> lightsMap = new HashMap<String, EspLight>();
	
	public interface ResponseListener<T extends Payload> {
		void handle(T payload);
	}
	
	protected DatagramSocket socket;
	
	private EspServer() {
		// Singleton
	}
	
	public void start(InetAddress laddr) throws SocketException {
		socket = new DatagramSocket(PORT, laddr);
		socket.setSoTimeout(SO_TIMEOUT);
	}
	
	public void start() throws SocketException {
		socket = new DatagramSocket(PORT);
		socket.setSoTimeout(SO_TIMEOUT);
	}
	
	public void stop() {
		socket.close();
	}
	
	public Collection<EspLight> discover() throws IOException {
		InetAddress broadcast = InetAddress.getByName("255.255.255.255");
		socket.setBroadcast(true);
		
		String msg = "B"; // Broadcast

		byte[] send = msg.getBytes(StandardCharsets.US_ASCII);
		DatagramPacket sendPacket = new DatagramPacket(send, send.length, broadcast, PORT);
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
				String recMsg = new String(received, StandardCharsets.US_ASCII);
				// OK 141919 5c:cf:7f:02:2a:5f
				if(recMsg.startsWith("OK ")) {
					String[] parts = recMsg.trim().split(" ");
					if(parts.length == 4) {
						InetAddress ip = rcvPacket.getAddress();
						String chipId = parts[1];
						String mac = parts[2].replace(":", "").toUpperCase();
						String endpointName = parts[3];
						String selector = EspLight.getSelector(mac, endpointName);
						if (!lightsMap.containsKey(selector)) {
							lightsMap.put(selector, new EspLight(this, ip, chipId, mac, endpointName));
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
	
	public Collection<EspLight> getLights() {
		return lightsMap.values();
	}
	
	public EspLight getLight(String selector) {
		return lightsMap.get(selector);
	}
	
	public String send(EspLight light, String payload) throws IOException {
		synchronized(socket) {
			byte[] send = payload.getBytes(StandardCharsets.US_ASCII);
			DatagramPacket sendPacket = new DatagramPacket(send, send.length, light.getAddress());
			socket.send(sendPacket);
			return recieve();
		}
	}
	
	private String recieve() throws IOException {
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
				String recMsg = new String(received, StandardCharsets.US_ASCII);
				return recMsg;
			} catch(RuntimeException e) {
				continue; // Not ours data
			}
		}
		return null;
	}
}
