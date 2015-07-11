package com.github.tort32.lightserver;

import java.util.concurrent.CountDownLatch;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import com.github.tort32.lightserver.servlets.HelloServlet;
import com.github.tort32.lightserver.servlets.ShutdownServlet;

public class LightServer {
	
	private static CountDownLatch shutdownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(HelloServlet.class, "/hello");
        handler.addServletWithMapping(ShutdownServlet.class, "/shutdown");
        server.setHandler(handler);            
        server.start();
        
        shutdownLatch.await();
        System.exit(0);
    }
    
    public static void shutdown() {
    	shutdownLatch.countDown();
    }
}
