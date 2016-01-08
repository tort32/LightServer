package com.github.tort32.api.lifx;

import java.io.IOException;
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

import com.github.tort32.api.lifx.server.LifxLight;
import com.github.tort32.api.lifx.server.LifxServer;
import com.github.tort32.common.entity.LightEndpoint;
import com.github.tort32.common.entity.LightSetColor;
import com.github.tort32.common.entity.LightSetPower;
import com.github.tort32.common.entity.LightState;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Singleton
@Path("/lifx")
@Api(value = "/lifx", description = "LIFX light API")
public class LifxService {

	@GET
	@Path("discover")
	@Produces({MediaType.APPLICATION_JSON})
	@ApiOperation(
			value = "Discover LIFX endpoints",
			response = LightEndpoint.class,
			responseContainer = "List")
	public List<LightEndpoint> getEndpoints() throws IOException {
		List<LightEndpoint> endpoints = new LinkedList<LightEndpoint>();
		for(LifxLight light : LifxServer.INSTANCE.discover()) {
			LightEndpoint endpoint = new LightEndpoint(light.getIp().getHostAddress(), light.getMac(), light.getPort());
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
			response = LightState.class)
	@ApiResponses({
			@ApiResponse(code = 502, message = "Target light is not found"),
			@ApiResponse(code = 504, message = "No response from the light")
		})
	public Response getState(
			@PathParam("selector")
			@ApiParam(
					value = "Light selector",
					required = true)
			String selector) throws IOException {
		LifxLight light = LifxServer.INSTANCE.getLight(selector);
		if (light == null) {
			return Response.status(Status.BAD_GATEWAY).build();
		}
		LightState state = light.getState();
		if (state == null) {
			return Response.status(Status.GATEWAY_TIMEOUT).build();
		}
		return Response.ok().entity(state).build();
	}
	
	@PUT
	@Path("{selector}/color")
	@Consumes({MediaType.APPLICATION_JSON})
	@ApiOperation(
			value = "Set light color",
			httpMethod = "PUT",
			consumes = "application/json")
	@ApiResponses({
		@ApiResponse(code = 502, message = "Target light is not found")
	})
	public Response setColor(
			@PathParam("selector")
			@ApiParam(
					value = "Light selector",
					required = true)
			String selector,
			@ApiParam(
					value = "Set color description",
					required = true)
			LightSetColor desc) throws IOException {
		LifxLight light = LifxServer.INSTANCE.getLight(selector);
		if (light == null) {
			return Response.status(Status.BAD_GATEWAY).build();
		}
		light.setColor(desc.color, desc.duration, true);
		return Response.ok().build();
	}
	
	@PUT
	@Path("{selector}/power")
	@Consumes({MediaType.APPLICATION_JSON})
	@ApiOperation(
			value = "Set light power",
			httpMethod = "PUT",
			consumes = "application/json")
	@ApiResponses({
		@ApiResponse(code = 502, message = "Target light is not found")
	})
	public Response setPower(
			@PathParam("selector")
			@ApiParam(
					value = "Light selector",
					required = true )
			String selector,
			@ApiParam(
					value = "Set color description",
					required = true)
			LightSetPower desc) throws IOException {
		LifxLight light = LifxServer.INSTANCE.getLight(selector);
		if (light == null) {
			return Response.status(Status.BAD_GATEWAY).build();
		}
		light.setPower(desc.power);
		return Response.ok().build();
	}
}
