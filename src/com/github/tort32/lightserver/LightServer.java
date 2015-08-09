package com.github.tort32.lightserver;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tort32.lifx.server.LifxServer;
import com.github.tort32.lifx.server.Light;

public class LightServer {
	
	private static Logger logger = LoggerFactory.getLogger(LightServer.class);

	private static CountDownLatch shutdownLatch = new CountDownLatch(1);

	public static void main(String[] args) {
		
		Server server = new Server(8080);
		
		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setDescriptor(webAppContext + "/WEB-INF/web.xml");
		webAppContext.setResourceBase(".");
		webAppContext.setContextPath("/");
		server.setHandler(webAppContext);
		
		LifxServer lifxServer = LifxServer.INSTANCE;
		try {
			logger.info("LifxServer starting ...");
			lifxServer.start();
			logger.info("LifxServer discover ...");
			Collection<Light> lights = lifxServer.discover();
			if(!lights.isEmpty()) {
				for(Light light : lights) {
					logger.info("Found Lifx device " + light.getMac() + " at " + light.getIp() + ":" + light.getPort());
				}
			} else {
				logger.error("No Lifx devices found!");
			}
			
			logger.info("LifxServer started");
		} catch(IOException e) {
			throw new RuntimeException("Can't start LifxServer", e);
		}
		
		try {
			server.start();
		} catch(Exception e) {
			throw new RuntimeException("Can't start Jetty server", e);
		}

		try {
			shutdownLatch.await();
		} catch(InterruptedException unimportant) {
			// Empty
		}
		
		lifxServer.stop();

		try {
			server.stop();
		} catch(Exception e) {
			throw new RuntimeException("Can't stop Jetty server", e);
		}
	}

	public static void shutdown() {
		shutdownLatch.countDown();
	}
}
