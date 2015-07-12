package com.github.tort32.lightserver;

import java.util.concurrent.CountDownLatch;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import com.github.tort32.lightserver.service.LifxService;
import com.github.tort32.lightserver.servlets.HelloServlet;
import com.github.tort32.lightserver.servlets.ShutdownServlet;

public class LightServer {

	private static CountDownLatch shutdownLatch = new CountDownLatch(1);

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		context.addServlet(HelloServlet.class, "/hello");
		context.addServlet(ShutdownServlet.class, "/shutdown");

		ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(1);
		//jerseyServlet.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
		//jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "com.github.tort32.lightserver;org.glassfish.jersey.jackson");
		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", LifxService.class.getCanonicalName());

		server.setHandler(context);
		server.start();

		shutdownLatch.await();

		server.stop();
		server.destroy();
	}

	public static void shutdown() {
		shutdownLatch.countDown();
	}
}
