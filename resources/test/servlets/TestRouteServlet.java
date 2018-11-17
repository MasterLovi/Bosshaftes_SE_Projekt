package test.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import servlets.RouteServlet;
import test.data.Reader;

public class TestRouteServlet {
	
	HttpServletRequest request;
	HttpServletResponse response;
	ServletContext servletContext;
	RouteServlet routeServlet;
	
	@Before
	public void setUp() throws Exception {
		 request = mock(HttpServletRequest.class);       
	     response = mock(HttpServletResponse.class);
	     servletContext = Mockito.mock(ServletContext.class);
	     routeServlet = new RouteServlet(){
	       public ServletContext getServletContext() {
	         return servletContext;
	       }
	     };
	     
	     EntityManagerFactory emf = Persistence.createEntityManagerFactory("DerbyDB");
	     Mockito.doReturn(emf).when(servletContext).getAttribute("emf");
	}

	@Test
	public void testCreate() throws IOException, ServletException {
		
		String testDataCreate = Reader.readFile("resources/test/data/routeCreate.json", StandardCharsets.UTF_8);
		when(request.getParameter("data")).thenReturn(testDataCreate);
		when(request.getParameter("operation")).thenReturn("create");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);
		
		routeServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		
        //verify that parameters were called
        verify(request, atLeast(1)).getParameter("operation");
		verify(request, atLeast(1)).getParameter("data");
		
		System.out.println(result);
        assertTrue(result.equals("Success"));
	}
	
	@Test
	public void testUpdate() throws IOException, ServletException {
		
		String testDataUpdate = Reader.readFile("resources/test/data/routeUpdate.json", StandardCharsets.UTF_8);
		when(request.getParameter("data")).thenReturn(testDataUpdate);
		when(request.getParameter("operation")).thenReturn("update");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);
		
		routeServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		
        //verify that parameters were called
        verify(request, atLeast(1)).getParameter("operation");
		verify(request, atLeast(1)).getParameter("data");
		
		System.out.println(result);
        assertTrue(result.equals("Success"));
	}

}

