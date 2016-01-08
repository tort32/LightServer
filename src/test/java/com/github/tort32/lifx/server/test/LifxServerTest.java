package com.github.tort32.lifx.server.test;

import java.io.IOException;
import java.util.Collection;

import com.github.tort32.api.lifx.server.LifxLight;
import com.github.tort32.api.lifx.server.LifxServer;
import com.github.tort32.common.ILight;
import com.github.tort32.common.entity.LightState;

public class LifxServerTest {
	
	public static void main(String[] args) {
		
		LifxServer server = null;
		try {
			server = LifxServer.INSTANCE;
			server.start();
			Collection<LifxLight> lights = server.discover();
			for(ILight light : lights) {
				LightState state = light.getState();
				System.out.println("Light <" + light.getMac() + "> " + state);
			}
		} catch(RuntimeException | IOException e) {
			e.printStackTrace();
		} finally {
			// Unbind socket
			if (server != null) {
				server.stop();
			}
		}
	}
}
