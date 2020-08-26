package com.github.tort32.lightserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.ProtectionDomain;
import java.util.Collection;
import java.util.Enumeration;
import java.util.concurrent.CountDownLatch;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tort32.api.lifx.server.LifxLight;
import com.github.tort32.api.lifx.server.LifxServer;
import com.github.tort32.api.nodemcu.server.EspLight;
import com.github.tort32.api.nodemcu.server.EspServer;

public class LightServer {
	
	private static Logger logger = LoggerFactory.getLogger(LightServer.class);

	private static CountDownLatch shutdownLatch = new CountDownLatch(1);

	public static void main(String[] args) {
		
		InetAddress laddr;
		try {
			laddr = getLocalAddress();
			if(args.length > 0)
				laddr = InetAddress.getByName(args[0]);
		} catch (SocketException | UnknownHostException e) {
			throw new RuntimeException("Can't determine local IP address", e);
		}
		//Server server = new Server(new InetSocketAddress(laddr, 8080));
		Server server = new Server(8080);
		
		ProtectionDomain protectionDomain = LightServer.class.getProtectionDomain();
		String webdir = protectionDomain.getCodeSource().getLocation().toExternalForm();
		
		WebAppContext webAppContext = new WebAppContext(webdir, "/");
		server.setHandler(webAppContext);
		
		LifxServer lifxServer = LifxServer.INSTANCE;
		try {
			logger.info("LifxServer starting at " + laddr);
			lifxServer.start(laddr);
			logger.info("LifxServer discover ...");
			Collection<LifxLight> lights = lifxServer.discover();
			if(!lights.isEmpty()) {
				for(LifxLight light : lights) {
					logger.info("Found Lifx device " + light.getSelector() + " at " +
							light.getAddress().getAddress().getHostAddress() + ":" + light.getAddress().getPort());
				}
			} else {
				logger.warn("No Lifx devices found!");
			}
			
			logger.info("LifxServer started");
		} catch(IOException e) {
			throw new RuntimeException("Can't start LifxServer", e);
		}
		EspServer espServer = EspServer.INSTANCE;
		try {
			logger.info("EspServer starting ...");
			espServer.start(laddr);
			logger.info("EspServer discover ...");
			Collection<EspLight> lights = espServer.discover();
			if(!lights.isEmpty()) {
				for(EspLight light : lights) {
					logger.info("Found NodeMCU device " + light.getSelector() + " at " +
							light.getAddress().getAddress().getHostAddress() + ":" + light.getAddress().getPort());
				}
			} else {
				logger.warn("No NodeMCU devices found!");
			}
			
			logger.info("EspServer started");
		} catch(IOException e) {
			throw new RuntimeException("Can't start EspServer", e);
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
		espServer.stop();

		try {
			server.stop();
		} catch(Exception e) {
			throw new RuntimeException("Can't stop Jetty server", e);
		}
	}

	public static void shutdown() {
		shutdownLatch.countDown();
	}
	
	public static InetAddress getLocalAddress() throws SocketException, UnknownHostException  {
		// Search real local address
		Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
		while(e.hasMoreElements())
		{
		    NetworkInterface n = e.nextElement();
		    if(!n.isUp() || n.isVirtual() || n.isLoopback() || !n.supportsMulticast())
		    	continue;
		    Enumeration<InetAddress> ee = n.getInetAddresses();
		    while (ee.hasMoreElements())
		    {
		        InetAddress laddr = ee.nextElement();
		        if(laddr.isLinkLocalAddress() || laddr.isLoopbackAddress())
		        	continue;
		        return laddr;
		    }
		}
		return InetAddress.getLocalHost(); // Try default way
	}
}
