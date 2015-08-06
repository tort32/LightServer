package com.github.tort32.lifx;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import com.github.tort32.lifx.device.recieve.StateService;
import com.github.tort32.lifx.protocol.message.Message;

public class UdpBroadcast extends UdpSocket {
	
	public interface ResponseListener {
		void handle(String hostAddress, Message msg);
	}

	public UdpBroadcast() throws IOException {
		super(InetAddress.getByName("255.255.255.255"), StateService.DEFAULT_PORT);
		socket.setBroadcast(true);
	}
	
	public void receive(ResponseListener callback) throws IOException {
		byte[] buf = new byte[BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		while(true) {
			try {
				socket.receive(packet);
			} catch(SocketTimeoutException timeout) {
				break;
			}
			byte[] received = packet.getData();
			//System.out.println("Quote of the Moment: " + Types.dumpBytes(received));
			try {
				Message rcvMsg = new Message(received);
				String hostAddress = packet.getAddress().getHostAddress();
				callback.handle(hostAddress, rcvMsg);
			} catch(RuntimeException e) {
				continue; // Not ours data
			}
		}
	}
}
