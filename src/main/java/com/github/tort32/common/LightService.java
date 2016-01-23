package com.github.tort32.common;

import java.io.IOException;
import java.util.ArrayList;
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

import com.github.tort32.api.lifx.server.LifxServer;
import com.github.tort32.api.nodemcu.server.EspServer;
import com.github.tort32.common.animation.AnimationFactory;
import com.github.tort32.common.animation.IAnimation;
import com.github.tort32.common.animation.IAnimation.AnimationDescriptor;
import com.github.tort32.common.entity.LightAnimationDesc;
import com.github.tort32.common.entity.LightEndpoint;
import com.github.tort32.common.entity.LightSetAnim;
import com.github.tort32.common.entity.LightSetAnimParam;
import com.github.tort32.common.entity.LightSetColor;
import com.github.tort32.common.entity.LightSetPower;
import com.github.tort32.common.entity.LightState;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Singleton
@Path("/light")
@Api(value = "/light", description = "Common light API")
public class LightService {

	@GET
	@Path("discover")
	@Produces({MediaType.APPLICATION_JSON})
	@ApiOperation(
			value = "Discover light endpoints",
			response = LightEndpoint.class,
			responseContainer = "List")
	public List<LightEndpoint> getEndpoints() throws IOException {
		List<LightEndpoint> endpoints = new LinkedList<LightEndpoint>();
		for(ILight light : LifxServer.INSTANCE.discover()) {
			LightEndpoint endpoint = new LightEndpoint(light);
			endpoints.add(endpoint);
		}
		for(ILight light : EspServer.INSTANCE.discover()) {
			LightEndpoint endpoint = new LightEndpoint(light);
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
		ILight light = LifxServer.INSTANCE.getLight(selector);
		if (light == null) {
			light = EspServer.INSTANCE.getLight(selector);
		}
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
		ILight light = LifxServer.INSTANCE.getLight(selector);
		if (light == null) {
			light = EspServer.INSTANCE.getLight(selector);
		}
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
		ILight light = LifxServer.INSTANCE.getLight(selector);
		if (light == null) {
			light = EspServer.INSTANCE.getLight(selector);
		}
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
		anims.add(LightAnimationDesc.NILL_ANIM_NAME);
		anims.addAll(AnimationFactory.getAnimationNames());
		return Response.ok(anims).build();
	}
	
	@GET
	@Path("{selector}/animation")
	@Produces({MediaType.APPLICATION_JSON})
	@ApiOperation(
			value = "Get light animation",
			httpMethod = "GET",
			response = LightAnimationDesc.class)
	@ApiResponses({
		@ApiResponse(code = 502, message = "Target light is not found")
	})
	public Response getAnimation(
			@PathParam("selector")
			@ApiParam(
					value = "Light selector",
					required = true)
			String selector) {
		ILight light = LifxServer.INSTANCE.getLight(selector);
		if (light == null) {
			light = EspServer.INSTANCE.getLight(selector);
		}
		if (light == null) {
			return Response.status(Status.BAD_GATEWAY).build();
		}
		LightAnimationDesc anim = new LightAnimationDesc(light.getAnimation());
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
			response = LightAnimationDesc.class)
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
			LightSetAnim desc) {
		ILight light = LifxServer.INSTANCE.getLight(selector);
		if (light == null) {
			light = EspServer.INSTANCE.getLight(selector);
		}
		if (light == null) {
			return Response.status(Status.BAD_GATEWAY).build();
		}
		light.setAnimation(desc.name);
		IAnimation anim = light.getAnimation();
		
		if (desc.params != null) {
			AnimationDescriptor animDesc = anim.getDescriptor();
			for(LightSetAnimParam param : desc.params) {
				try {
					animDesc.setParam(param.name, param.value);
				} catch (RuntimeException e) {
					continue;
				}
			}
		}
		LightAnimationDesc animDesc = new LightAnimationDesc(anim);
		return Response.ok().entity(animDesc).build();
	}
	
	@PUT
	@Path("{selector}/animation/param")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@ApiOperation(
			value = "Set light animation param",
			httpMethod = "PUT",
			consumes = "application/json",
			response = LightAnimationDesc.class)
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
			LightSetAnimParam desc) {
		ILight light = LifxServer.INSTANCE.getLight(selector);
		if (light == null) {
			light = EspServer.INSTANCE.getLight(selector);
		}
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
		LightAnimationDesc ret = new LightAnimationDesc(anim);
		return Response.ok().entity(ret).build();
	}
}
