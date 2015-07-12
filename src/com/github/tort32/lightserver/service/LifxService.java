package com.github.tort32.lightserver.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/lifx")
public class LifxService {

	@GET
	@Path("test")
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
		return "Test";
	}

	@GET
	@Path("discover")
	@Produces({MediaType.APPLICATION_JSON})
	public Response discover() throws IOException {
		List<LifxEndpoint> endpoints = new ArrayList<LifxEndpoint>();

		// Hardcoded packet for GetService message
		byte[] discoverPacket = new byte[]{0x24, 0x00, 0x00, 0x34, 0x55, (byte) 0xAA, (byte) 0xAD, (byte) 0xBA, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00};

		DatagramSocket socket = new DatagramSocket();
		socket.setBroadcast(true);
		socket.setSoTimeout(1000);

		InetAddress broadcast = InetAddress.getByName("255.255.255.255");
		DatagramPacket sendPacket = new DatagramPacket(discoverPacket, discoverPacket.length, broadcast, 56700);
		socket.send(sendPacket);

		byte[] buf = new byte[64];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		while(true) {
			try {
				socket.receive(packet);
			} catch(SocketTimeoutException timeout) {
				break;
			}
			byte[] received = packet.getData();
			packet.getLength();
			System.out.println("Quote of the Moment: " + dumpBytes(received));
			String ip = packet.getAddress().getHostAddress();
			int port = packet.getPort();
			endpoints.add(new LifxEndpoint(ip, port));
		}
		socket.close();

		return Response.ok().entity(endpoints).build();
	}

	String dumpBytes(byte[] a) {
		StringBuilder sb = new StringBuilder();
		for(byte b : a) {
			sb.append(String.format("%02X ", b));
		}
		return sb.toString();
	}
}
