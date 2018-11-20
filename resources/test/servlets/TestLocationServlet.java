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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.gson.Gson;

import model.Location;
import servlets.LocationServlet;
import test.data.Reader;

public class TestLocationServlet {
	
	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;
	ServletContext servletContext;
	LocationServlet locationServlet;
	
	@SuppressWarnings("serial")
	@Before
	public void setUp() throws Exception {
		 request = mock(HttpServletRequest.class);       
	     response = mock(HttpServletResponse.class);
	     session = mock(HttpSession.class);
	     session = mock(HttpSession.class);
	     servletContext = Mockito.mock(ServletContext.class);
	     locationServlet = new LocationServlet(){
	       public ServletContext getServletContext() {
	         return servletContext;
	       }
	     };
	     
	     EntityManagerFactory emf = Persistence.createEntityManagerFactory("DerbyDB");
	     Mockito.doReturn(emf).when(servletContext).getAttribute("emf");
	     Mockito.doReturn(session).when(request).getSession();
	     Mockito.doReturn("true").when(session).getAttribute("loggedin");
	     Mockito.doReturn("test").when(session).getAttribute("username");
	}
	
	@Test
	public void testCreate() throws IOException, ServletException {
		System.out.println("\n\nLocation Test Create");
		
		String testData = Reader.readFile("resources/test/data/locationCreate.json", StandardCharsets.UTF_8);
		when(request.getParameter("data")).thenReturn(testData);
		when(request.getParameter("operation")).thenReturn("create");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);
		
		locationServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		
		System.out.println(result);
        assertTrue(result.equals("Success"));
	}
	
	@Test
	public void testRead() throws IOException, ServletException {
		System.out.println("\n\nLocation Test Read");
		
		String testData = Reader.readFile("resources/test/data/locationCreate.json", StandardCharsets.UTF_8);
		String[] northWest = {"90", "90"};
		String[] southEast = {"90", "90"};
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		when(response.getWriter()).thenReturn(pw);
		when(request.getParameter("data")).thenReturn(testData);
		when(request.getParameterValues("boundNorthWest")).thenReturn(northWest);
		when(request.getParameterValues("boundSouthEast")).thenReturn(southEast);

		
		when(request.getParameter("type")).thenReturn("Party");
		locationServlet.doGet(request, response);
		String result = sw.getBuffer().toString();
		
		System.out.println(result);
        assertTrue(result.equals(testData));
        
        
        when(request.getParameter("type")).thenReturn("");
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        locationServlet.doGet(request, response);
        result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("Type muss entweder \"Party\" oder \"Kultur\" sein!"));
        
        
        when(request.getParameter("type")).thenReturn(null);
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        locationServlet.doGet(request, response);
        result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("Type darf nicht null sein!"));
	}
	
	@Test
	public void testUpdate() throws IOException, ServletException {
		System.out.println("\n\nLocation Test Update");
		
		String testData = Reader.readFile("resources/test/data/locationUpdate.json", StandardCharsets.UTF_8);
		when(request.getParameter("data")).thenReturn(testData);
		when(request.getParameter("operation")).thenReturn("update");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);
		
		locationServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		
		System.out.println(result);
        assertTrue(result.equals("Success"));
        
        
        List<Location> locations = new ArrayList<Location>();
        Location l = new Location();
        l.setId(-1);
        l.setName("rama lama ding dong");
        locations.add(l);
        Gson gson = new Gson();
        String delete = gson.toJson(locations);
        when(request.getParameter("data")).thenReturn(delete);
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        locationServlet.doPost(request, response);
        result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("Location \"rama lama ding dong\" existiert nicht."));
	}
	
	@Test
	public void testReport() throws IOException, ServletException {
		System.out.println("\n\nLocation Test Report");
		
		String testData = Reader.readFile("resources/test/data/locationUpdate.json", StandardCharsets.UTF_8);
		when(request.getParameter("data")).thenReturn(testData);
		when(request.getParameter("operation")).thenReturn("report");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);
		
		locationServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		
		System.out.println(result);
        assertTrue(result.equals("Success"));
        
        
        List<Location> locations = new ArrayList<Location>();
        Location l = new Location();
        l.setId(-1);
        l.setName("rama lama ding dong");
        locations.add(l);
        Gson gson = new Gson();
        String delete = gson.toJson(locations);
        when(request.getParameter("data")).thenReturn(delete);
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        locationServlet.doPost(request, response);
        result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("Location \"rama lama ding dong\" existiert nicht."));
	}
	
	@Test
	public void testDelete() throws IOException, ServletException {
		System.out.println("\n\nLocation Test Delete");
		
		
		String testData = Reader.readFile("resources/test/data/locationUpdate.json", StandardCharsets.UTF_8);
		when(request.getParameter("data")).thenReturn(testData);
		when(request.getParameter("operation")).thenReturn("delete");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);
		
		locationServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		
		System.out.println(result);
        assertTrue(result.equals("Success"));
        
        
        List<Location> locations = new ArrayList<Location>();
        Location l = new Location();
        l.setId(-1);
        l.setName("rama lama ding dong");
        locations.add(l);
        Gson gson = new Gson();
        String delete = gson.toJson(locations);
        when(request.getParameter("data")).thenReturn(delete);
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        locationServlet.doPost(request, response);
        result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("Location \"rama lama ding dong\"existiert nicht und kann daher nicht gelöscht werden"));
        
	}
	
	@Test
	public void testLoggedOut() throws IOException, ServletException {
		System.out.println("\n\nLocation Test 'Not Logged In'");
		when(request.getParameter("data")).thenReturn("[]");
		when(session.getAttribute("loggedin")).thenReturn("false");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);
		
		locationServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		
		System.out.println(result);
        assertTrue(result.equals("You are not logged in."));
	}

}

