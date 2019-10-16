package com.github.tort32.api.lifx;

import java.io.IOException;
import java.util.Collection;

import com.github.tort32.api.lifx.server.LifxLight;
import com.github.tort32.api.lifx.server.LifxServer;
import com.github.tort32.common.ILight;
import com.github.tort32.common.entity.LightState;
import com.github.tort32.lightserver.LightServer;

public class LifxServerTest {
	
	public static void main(String[] args) {
		
		LifxServer server = null;
		try {
			server = LifxServer.INSTANCE;
			server.start(LightServer.getLocalAddress());
			Collection<LifxLight> lights = server.discover();
			for(ILight light : lights) {
				LightState state = light.getState();
				System.out.println("Light <" + light.getSelector() + "> " + state);
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
