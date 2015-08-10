package com.github.tort32.lifx.protocol.test;

import java.io.IOException;
import java.util.Collection;

import com.github.tort32.lifx.light.recieve.State;
import com.github.tort32.lifx.server.LifxServer;
import com.github.tort32.lifx.server.Light;

public class LifxServerTest {

	public static void main(String[] args) {
		
		LifxServer server = null;
		try {
			server = LifxServer.INSTANCE;
			server.start();
			Collection<Light> lights = server.discover();
			for(Light light : lights) {
				State state = light.getState();
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
