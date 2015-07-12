package com.github.tort32.lightserver.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.tort32.lightserver.LightServer;

@SuppressWarnings("serial")
public class ShutdownServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println("<head><meta http-equiv=\"refresh\" content=\"1\" /></head>");
		response.getWriter().println("<h1>Start shutdown</h1>");

		LightServer.shutdown();
	}
}