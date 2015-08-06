package com.github.tort32.lightserver.service;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.github.tort32.lifx.UdpBroadcast;
import com.github.tort32.lifx.UdpSocket;
import com.github.tort32.lifx.device.recieve.StateService;
import com.github.tort32.lifx.device.send.GetService;
import com.github.tort32.lifx.light.recieve.State;
import com.github.tort32.lifx.light.send.Get;
import com.github.tort32.lifx.protocol.message.Message;
import com.github.tort32.lightserver.entity.LifxEndpoint;
import com.github.tort32.lightserver.entity.LifxState;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Path("/lifx")
@Api(value = "/lifx", description = "LIFX raw operations")
public class LifxService {

	@GET
	@Path("discover")
	@Produces({MediaType.APPLICATION_JSON})
	@ApiOperation(
			value = "Discover LIFX endpoints",
			response = LifxEndpoint.class,
			responseContainer = "List")
	public List<LifxEndpoint> discover() throws IOException {
		UdpBroadcast socket = new UdpBroadcast();
		
		socket.send(new Message(new GetService()) {{
			setBroadcast();
			setSource(Message.SENDER_ID);
		}});
		
		List<LifxEndpoint> endpoints = new ArrayList<LifxEndpoint>();
		socket.receive((ip, rcvMsg) -> {
			if (rcvMsg.mPayload instanceof StateService) {
				StateService payload = (StateService) rcvMsg.mPayload;
				if (Message.SENDER_ID == rcvMsg.mFrame.mSource.getValue() &&
						StateService.Service.UDP.getValue() == payload.mService.getValue()) {
					String mac = rcvMsg.mFrameAddress.mTarget.getHexValue();
					int port = (int) payload.mPort.getValue();
					endpoints.add(new LifxEndpoint(ip, mac, port));
				}
			}
		});
		
		socket.close();
		
		return endpoints;
	}
	
	@POST
	@Path("state")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@ApiOperation(
			value = "Get LIFX device state",
			httpMethod = "POST",
			consumes = "application/json",
			response = LifxState.class)
	public LifxState getState(
			@ApiParam( value = "LIFX endpoint description", required = true )
			LifxEndpoint endpoint) throws IOException {
		InetAddress ip = InetAddress.getByName(endpoint.ip);
		
		UdpSocket socket = new UdpSocket(ip, endpoint.port);
		
		socket.send(new Message(new Get()) {{
			setSource(Message.SENDER_ID);
			setTarget(endpoint.mac);
		}});
		
		final AtomicReference<LifxState> ret = new AtomicReference<LifxState>();
		socket.receive((msg) -> {
			if (Message.SENDER_ID == msg.mFrame.mSource.getValue()) {
				if (msg.mPayload instanceof State) {
					ret.set(new LifxState((State) msg.mPayload));
					return true;
				}
			}
			return false;
		});
		
		socket.close();
		
		return ret.get();
	}
}
