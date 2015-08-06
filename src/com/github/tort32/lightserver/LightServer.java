package com.github.tort32.lightserver;

import java.util.concurrent.CountDownLatch;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class LightServer {

	private static CountDownLatch shutdownLatch = new CountDownLatch(1);

	public static void main(String[] args) throws Exception {
		
		Server server = new Server(8080);
		
		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setDescriptor(webAppContext + "/WEB-INF/web.xml");
		webAppContext.setResourceBase(".");
		webAppContext.setContextPath("/");
		server.setHandler(webAppContext);

		server.start();

		shutdownLatch.await();

		server.stop();
		server.destroy();
	}

	public static void shutdown() {
		shutdownLatch.countDown();
	}
}
