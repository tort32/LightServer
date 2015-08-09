package com.github.tort32.lifx.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import com.github.tort32.lifx.device.recieve.StateService;
import com.github.tort32.lifx.protocol.message.Message;

public class UdpSocket {
	
	public static final int SO_TIMEOUT = 1000; 
	public static final int BUFFER_SIZE = 1024;
	
	public interface ResponseListener {
		boolean handle(Message msg);
	}
	
	protected DatagramSocket socket;
	protected InetAddress destAddress;
	protected int destPort;

	public UdpSocket(InetAddress address, int port) throws IOException {
		socket = new DatagramSocket(StateService.DEFAULT_PORT);
		this.destAddress = address;
		this.destPort = port;
		socket.setSoTimeout(SO_TIMEOUT);
	}
	
	public void send(Message msg) throws IOException {
		byte[] packet = msg.toArray();
		DatagramPacket sendPacket = new DatagramPacket(packet, packet.length, destAddress, destPort);
		socket.send(sendPacket);
	}
	
	public void receive(ResponseListener callback) throws IOException {
		byte[] buf = new byte[BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		boolean found = false;
		while(!found) {
			try {
				socket.receive(packet);
			} catch(SocketTimeoutException timeout) {
				break;
			}
			byte[] received = packet.getData();
			//System.out.println("Quote of the Moment: " + Types.dumpBytes(received));
			try {
				Message rcvMsg = new Message(received);
				found = callback.handle(rcvMsg);
			} catch(RuntimeException e) {
				continue; // Not ours data
			}
		}
	}
	
	public void close() {
		socket.close();
	}
}
