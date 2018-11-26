package test.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.gson.Gson;
import model.Route;
import servlets.RouteServlet;
import test.data.Reader;

public class TestRouteServlet {
	
	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;
	ServletContext servletContext;
	RouteServlet routeServlet;
	static EntityManagerFactory emf;
	
	@BeforeClass
	public static void connectDB() {
		emf = Persistence.createEntityManagerFactory("TEST");
	}
	
	@AfterClass
	public static void closeDB() {
		emf.close();
	}
	
	@SuppressWarnings("serial")
	@Before
	public void setUp() throws Exception {
		 request = mock(HttpServletRequest.class);       
	     response = mock(HttpServletResponse.class);
	     session = mock(HttpSession.class);
	     session = mock(HttpSession.class);
	     servletContext = Mockito.mock(ServletContext.class);
	     routeServlet = new RouteServlet(){
	       public ServletContext getServletContext() {
	         return servletContext;
	       }
	     };
	     
	     Mockito.doReturn(emf).when(servletContext).getAttribute("emf");
	     Mockito.doReturn(session).when(request).getSession();
	     Mockito.doReturn(true).when(session).getAttribute("loggedin");
	     Mockito.doReturn("test").when(session).getAttribute("username");
	}
	
	@Test
	public void run() throws IOException, ServletException {
		t1Create();
		t2Read();
		t3Update();
		t4Delete();
		t5LoggedOut();
	}
	
	
	public void t1Create() throws IOException, ServletException {
		System.out.println("\n\nRoute Test Create");
		
		String testData = Reader.readFile("resources/test/data/routeCreate.json", StandardCharsets.UTF_8);
		when(request.getParameter("data")).thenReturn(testData);
		when(request.getParameter("operation")).thenReturn("create");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);
		
		routeServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		
		System.out.println(result);
        assertTrue(result.equals("Success"));
        
	}
	
	public void t2Read() throws IOException, ServletException {
		System.out.println("\n\nRoute Test Read");
		
		String testData = Reader.readFile("resources/test/data/routeCreate.json", StandardCharsets.UTF_8);
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		when(response.getWriter()).thenReturn(pw);
		
		when(request.getParameter("time")).thenReturn("180:00:00");
		when(request.getParameter("stops")).thenReturn("20");
		when(request.getParameter("rating")).thenReturn("0");
		when(request.getParameter("boundNorthWestLat")).thenReturn("180");
		when(request.getParameter("boundNorthWestLng")).thenReturn("0");
		when(request.getParameter("boundSouthEastLat")).thenReturn("0");
		when(request.getParameter("boundSouthEastLng")).thenReturn("180");
		
		
		when(request.getParameter("type")).thenReturn("Party");
		routeServlet.doGet(request, response);
		String result = sw.getBuffer().toString();
		
		assertTrue(result.equals(testData));
		
		when(request.getParameter("time")).thenReturn("00:00:05");
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        routeServlet.doGet(request, response);
        result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("[]"));
        
        
        when(request.getParameter("time")).thenReturn("180:00:00");
        when(request.getParameter("rating")).thenReturn("100");
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        routeServlet.doGet(request, response);
        result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("[]"));
        
        
        when(request.getParameter("type")).thenReturn("");
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        routeServlet.doGet(request, response);
        result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("Type muss entweder \"Party\" oder \"Kultur\" sein!"));
        
        
        when(request.getParameter("type")).thenReturn(null);
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        routeServlet.doGet(request, response);
        result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("Type darf nicht null sein!"));
	}
	
	public void t3Update() throws IOException, ServletException {
		System.out.println("\n\nRoute Test Update");
		
		String testData = Reader.readFile("resources/test/data/routeUpdate.json", StandardCharsets.UTF_8);
		when(request.getParameter("data")).thenReturn(testData);
		when(request.getParameter("operation")).thenReturn("update");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);
		
		routeServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		
		System.out.println(result);
        assertTrue(result.equals("Success"));
        
        List<Route> routes = new ArrayList<Route>();
        Route r = new Route();
        r.setId(-1);
        r.setName("rama lama ding dong");
        routes.add(r);
        Gson gson = new Gson();
        String delete = gson.toJson(routes);
        when(request.getParameter("data")).thenReturn(delete);
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        routeServlet.doPost(request, response);
        result = sw.getBuffer().toString();
        
        //verify that parameters were called
        System.out.println(result);
        assertTrue(result.equals("Route \"rama lama ding dong\" existiert net."));
        
        when(request.getParameter("data")).thenReturn("[]");
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        routeServlet.doPost(request, response);
        result = sw.getBuffer().toString();
        
        //verify that parameters were called
        System.out.println(result);
        assertTrue(result.equals("Success"));
	}
	
	public void t4Delete() throws IOException, ServletException {
		System.out.println("\n\nRoute Test Delete");
		
		when(request.getParameter("data")).thenReturn("[{\"id\":1}]");
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("operation")).thenReturn("delete");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);
		
		routeServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		
		System.out.println(result);
        assertTrue(result.equals("Success"));
        
        
        List<Route> routes = new ArrayList<Route>();
        Route r = new Route();
        r.setId(-1);
        r.setName("rama lama ding dong");
        routes.add(r);
        Gson gson = new Gson();
        String delete = gson.toJson(routes);
        when(request.getParameter("data")).thenReturn(delete);
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        routeServlet.doPost(request, response);
        result = sw.getBuffer().toString();
        
        //verify that parameters were called
        System.out.println(result);
        assertTrue(result.equals("Route \"rama lama ding dong\"existiert nicht und kann daher nicht gelöscht werden"));
        
	}
	
	public void t5LoggedOut() throws IOException, ServletException {
		System.out.println("\n\nRoute Test 'Not Logged In'");
		
		when(request.getParameter("data")).thenReturn("[]");
		when(session.getAttribute("loggedin")).thenReturn(false);

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);
		
		routeServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		
		System.out.println(result);
        assertTrue(result.equals("You are not logged in."));
	}

}

