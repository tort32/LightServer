package com.github.tort32.lightserver.service;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.github.tort32.lifx.light.recieve.State;
import com.github.tort32.lifx.light.send.Get;
import com.github.tort32.lifx.server.LifxServer;
import com.github.tort32.lifx.server.Light;
import com.github.tort32.lightserver.entity.LifxEndpoint;
import com.github.tort32.lightserver.entity.LifxSetColor;
import com.github.tort32.lightserver.entity.LifxSetPower;
import com.github.tort32.lightserver.entity.LifxState;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Singleton
@Path("/lifx")
@Api(value = "/lifx", description = "LIFX light API")
public class LifxService {

	@GET
	@Path("discover")
	@Produces({MediaType.APPLICATION_JSON})
	@ApiOperation(
			value = "Discover LIFX endpoints",
			response = LifxEndpoint.class,
			responseContainer = "List")
	public List<LifxEndpoint> getEndpoints() throws IOException {
		List<LifxEndpoint> endpoints = new LinkedList<LifxEndpoint>();
		Collection<Light> lights = LifxServer.INSTANCE.getLights();
		if (lights.isEmpty()) {
			// Try to discover
			lights = LifxServer.INSTANCE.discover();
		}
		for(Light light : lights) {
			LifxEndpoint endpoint = new LifxEndpoint(light.getIp(), light.getMac(), light.getPort());
			endpoints.add(endpoint);
		}
		return endpoints;
	}
	
	@GET
	@Path("{selector}/state")
	@Produces({MediaType.APPLICATION_JSON})
	@ApiOperation(
			value = "Get light state",
			httpMethod = "GET",
			response = LifxState.class)
	public Response getState(
			@ApiParam( value = "Light selector", required = true )
			@PathParam("selector") String selector) throws IOException {
		Light light = LifxServer.INSTANCE.getLight(selector);
		if (light == null) {
			return Response.status(Status.BAD_GATEWAY).build();
		}
		State state = LifxServer.INSTANCE.send(light, new Get(), State.class);
		if (state != null) {
			return Response.ok()
					.entity(new LifxState(state))
					.build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
	
	@PUT
	@Path("{selector}/color")
	@Consumes({MediaType.APPLICATION_JSON})
	@ApiOperation(
			value = "Set light color",
			httpMethod = "PUT",
			consumes = "application/json")
	public Response setColor(
			@ApiParam( value = "Light selector", required = true ) @PathParam("selector") String selector,
			@ApiParam( value = "Set color description", required = true ) LifxSetColor desc) throws IOException {
		Light light = LifxServer.INSTANCE.getLight(selector);
		if (light != null) {
			light.setColor(desc.color.toHSBK(), desc.duration);
			return Response.ok().build();
		} else {
			return Response.status(Status.BAD_GATEWAY).build();
		}
	}
	
	@PUT
	@Path("{selector}/power")
	@Consumes({MediaType.APPLICATION_JSON})
	@ApiOperation(
			value = "Set light power",
			httpMethod = "PUT",
			consumes = "application/json")
	public Response setPower(
			@ApiParam( value = "Light selector", required = true ) @PathParam("selector") String selector,
			@ApiParam( value = "Set color description", required = true ) LifxSetPower desc) throws IOException {
		Light light = LifxServer.INSTANCE.getLight(selector);
		if (light != null) {
			light.setPower(desc.power);
			return Response.ok().build();
		} else {
			return Response.status(Status.BAD_GATEWAY).build();
		}
	}
}
