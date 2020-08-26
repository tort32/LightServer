package com.github.tort32.api.lifx;

import java.io.IOException;
import java.net.InetAddress;
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
			InetAddress laddr = LightServer.getLocalAddress();
			if(args.length > 0)
				laddr = InetAddress.getByName(args[0]);
			System.out.println("LifxServer starting at " + laddr);
			server = LifxServer.INSTANCE;
			server.start(laddr);
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
