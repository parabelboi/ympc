package eu.jmpd.service.auth;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;

public class AuthService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8084753416145549317L;

	//@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8000");
		resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
		resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER");
		resp.addHeader("Access-Control-Max-Age", "1728000");
		//super.service(req, resp);
	}
	
	

}
