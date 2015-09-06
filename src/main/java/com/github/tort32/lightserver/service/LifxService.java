package com.github.tort32.lightserver.service;

import java.io.IOException;
import java.util.ArrayList;
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
import com.github.tort32.lifx.server.animation.AnimationFactory;
import com.github.tort32.lifx.server.animation.IAnimation;
import com.github.tort32.lifx.server.animation.IAnimation.AnimationDescriptor;
import com.github.tort32.lifx.server.animation.IAnimation.AnimationParam;
import com.github.tort32.lightserver.entity.LifxAnimationDesc;
import com.github.tort32.lightserver.entity.LifxEndpoint;
import com.github.tort32.lightserver.entity.LifxSetAnim;
import com.github.tort32.lightserver.entity.LifxSetAnimParam;
import com.github.tort32.lightserver.entity.LifxSetColor;
import com.github.tort32.lightserver.entity.LifxSetPower;
import com.github.tort32.lightserver.entity.LifxState;
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
		Light light = LifxServer.INSTANCE.getLight(selector);
		if (light == null) {
			return Response.status(Status.BAD_GATEWAY).build();
		}
		State state = LifxServer.INSTANCE.send(light, new Get(), State.class);
		if (state == null) {
			return Response.status(Status.GATEWAY_TIMEOUT).build();
		}
		LifxState ret = new LifxState(state);
		return Response.ok().entity(ret).build();
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
			LifxSetColor desc) throws IOException {
		Light light = LifxServer.INSTANCE.getLight(selector);
		if (light == null) {
			return Response.status(Status.BAD_GATEWAY).build();
		}
		light.setColor(desc.color.toHSBK(), desc.duration);
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
			LifxSetPower desc) throws IOException {
		Light light = LifxServer.INSTANCE.getLight(selector);
		if (light == null) {
			return Response.status(Status.BAD_GATEWAY).build();
		}
		light.setPower(desc.power);
		return Response.ok().build();
	}
	
	@GET
	@Path("animations")
	@Produces({MediaType.APPLICATION_JSON})
	@ApiOperation(
			value = "Get list of available light animations",
			httpMethod = "GET",
			response = String.class,
			responseContainer = "List")
	public Response getAnimations() {
		List<String> anims = new ArrayList<String>();
		anims.add(LifxAnimationDesc.NILL_ANIM_NAME);
		anims.addAll(AnimationFactory.getAnimationNames());
		return Response.ok(anims).build();
	}
	
	@GET
	@Path("{selector}/animation")
	@Produces({MediaType.APPLICATION_JSON})
	@ApiOperation(
			value = "Get light animation",
			httpMethod = "GET",
			response = LifxAnimationDesc.class)
	@ApiResponses({
		@ApiResponse(code = 502, message = "Target light is not found")
	})
	public Response getAnimation(
			@PathParam("selector")
			@ApiParam(
					value = "Light selector",
					required = true)
			String selector) {
		Light light = LifxServer.INSTANCE.getLight(selector);
		if (light == null) {
			return Response.status(Status.BAD_GATEWAY).build();
		}
		LifxAnimationDesc anim = new LifxAnimationDesc(light.getAnimation());
		return Response.ok(anim).build();
	}
	
	@PUT
	@Path("{selector}/animation")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@ApiOperation(
			value = "Set light animation",
			httpMethod = "PUT",
			consumes = "application/json",
			response = LifxAnimationDesc.class)
	@ApiResponses({
		@ApiResponse(code = 502, message = "Target light is not found")
	})
	public Response setAnimation(
			@PathParam("selector")
			@ApiParam(
					value = "Light selector",
					required = true)
			String selector,
			@ApiParam(
					value = "Set animation description",
					required = true)
			LifxSetAnim desc) {
		Light light = LifxServer.INSTANCE.getLight(selector);
		if (light == null) {
			return Response.status(Status.BAD_GATEWAY).build();
		}
		light.setAnimation(desc.name);
		LifxAnimationDesc anim = new LifxAnimationDesc(light.getAnimation());
		return Response.ok().entity(anim).build();
	}
	
	@PUT
	@Path("{selector}/animation/param")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@ApiOperation(
			value = "Set light animation param",
			httpMethod = "PUT",
			consumes = "application/json",
			response = LifxAnimationDesc.class)
	@ApiResponses({
		@ApiResponse(code = 502, message = "Target light is not found"),
		@ApiResponse(code = 404, message = "Animation param is not found"),
		@ApiResponse(code = 406, message = "Animation param value is invalid")
	})
	public Response setAnimationParam(
			@PathParam("selector")
			@ApiParam(
				value = "Light selector",
				required = true)
			String selector,
			@ApiParam(
					value = "Set animation param description",
					required = true)
			LifxSetAnimParam desc) {
		Light light = LifxServer.INSTANCE.getLight(selector);
		if (light == null) {
			return Response.status(Status.BAD_GATEWAY).build();
		}
		IAnimation anim = light.getAnimation();
		if (anim == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		AnimationDescriptor animDesc = anim.getDescriptor();
		try {
			animDesc.setParam(desc.name, desc.value);
		} catch(NullPointerException e) {
			return Response.status(Status.NOT_FOUND).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Status.NOT_ACCEPTABLE).build();
		}
		LifxAnimationDesc ret = new LifxAnimationDesc(anim);
		return Response.ok().entity(ret).build();
	}
}
