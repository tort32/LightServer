package com.github.tort32.api.lifx.protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import com.github.tort32.api.lifx.protocol.Types;
import com.github.tort32.api.lifx.protocol.device.send.SetPower;
import com.github.tort32.api.lifx.protocol.message.Message;
import com.github.tort32.api.lifx.protocol.message.Payload;

public class ProtocolTest {
	public static void main(String[] args) throws IOException {
		
		StringBuilder sb = new StringBuilder();
		
		DatagramSocket socket = new DatagramSocket();
		socket.setSoTimeout(1000);
		
		//Payload payload = new SetColor(true, HSBK.Red, 1000);
		//Payload payload = new SetLabel("My bulb");
		//Payload payload = new Get();
		Payload payload = new SetPower(true);
		
		Message sndMsg = new Message(payload);
		sndMsg.setSource(Message.SENDER_ID);
		sndMsg.setTarget("D073D5034927");
		
		//sb.append(sndMsg);
		
		byte[] sendBytes = sndMsg.toArray();
		InetAddress ip = InetAddress.getByName("192.168.1.121");
		int port = 56700;
		DatagramPacket sendPacket = new DatagramPacket(sendBytes, sendBytes.length, ip, port);
		
		socket.send(sendPacket);
		
		
		
		byte[] buf = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		while(true) {
			try {
				socket.receive(packet);
			} catch(SocketTimeoutException timeout) {
				break;
			}
			byte[] received = packet.getData();
			sb.append("Quote of the Moment: " + Types.dumpBytes(received)).append("\n\n");
			Message recMsg = null;
			try {
				recMsg = new Message(received);
			} catch(RuntimeException e) {
				continue; // Not ours data
			}
			sb.append(recMsg);
		}
		
		socket.close();
		
		System.out.println(sb.toString());
	}
}
