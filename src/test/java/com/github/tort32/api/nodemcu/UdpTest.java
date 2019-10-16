package com.github.tort32.api.nodemcu;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

import com.github.tort32.api.lifx.protocol.Types;

public class UdpTest {
	public static void main(String[] args) throws IOException {
		
		DatagramSocket socket = new DatagramSocket();
		socket.setSoTimeout(1000);
		
		//String sndMsg = "S123,234,345";
		//String sndMsg = "B";
		String sndMsg = "SB,255,0,0";
		
		System.out.println("Send: >" + sndMsg + "<");
		
		byte[] sendBytes = sndMsg.getBytes(StandardCharsets.US_ASCII);
		
		InetAddress ip = InetAddress.getByName("192.168.1.61");
		int port = 34500;
		DatagramPacket sendPacket = new DatagramPacket(sendBytes, sendBytes.length, ip, port);
		socket.send(sendPacket);
		
		byte[] buf = new byte[32];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		while(true) {
			try {
				socket.receive(packet);
				String fromIp = packet.getAddress().getHostAddress();
				System.out.println("Received from " + fromIp);
			} catch(SocketTimeoutException timeout) {
				System.out.println("Timeout");
				break;
			}
			byte[] received = packet.getData();
			System.out.println("Quote of the Moment: " + Types.dumpBytes(received));
			String recMsg = new String(received, StandardCharsets.US_ASCII);
			System.out.println("Receive: >" + recMsg + "<");
		}
		
		socket.close();
		System.out.println("Closed");
	}
}
