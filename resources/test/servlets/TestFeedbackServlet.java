package test.servlets;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
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
import com.google.gson.GsonBuilder;

import model.Location;
import model.Route;
import model.Users;
import servlets.FeedbackServlet;
import servlets.RouteServlet;
import test.data.Reader;

public class TestFeedbackServlet {
	
	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;
	ServletContext servletContext;
	FeedbackServlet feedbackServlet;
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
	     feedbackServlet = new FeedbackServlet(){
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
	public void runLocation() throws IOException, ServletException {
		t1LCreate();
		t2LUpdate();
		t3LDelete();
	}
	
	@Test
	public void runRoute() throws IOException, ServletException {
		t1RCreate();
		t2RUpdate();
		t3RDelete();
	}
	
	@Test
	public void tTypeIncorrect() throws IOException, ServletException {
		System.out.println("\n\nFeedback Wrong Type Test");
		
		when(request.getParameter("data")).thenReturn("[]");
		when(request.getParameter("type")).thenReturn("test");
		when(request.getParameter("id")).thenReturn("5");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);
		
		feedbackServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		
		System.out.println(result);
        assertTrue(result.equals("Type muss entweder \"Route\" oder \"Location\" sein!"));
	}
	
	@Test
	public void tLoggedOut() throws IOException, ServletException {
		System.out.println("\n\nFeedback Test 'Not Logged In'");
		
		when(request.getParameter("data")).thenReturn("[]");
		when(request.getParameter("id")).thenReturn("5");
		when(session.getAttribute("loggedin")).thenReturn(false);

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);
		
		feedbackServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		
		System.out.println(result);
        assertTrue(result.equals("You are not logged in."));
	}
	
	
	public void t1LCreate() throws IOException, ServletException {
		System.out.println("\n\nLocation Feedback Test Create");
		
		String testData = Reader.readFile("resources/test/data/feedbackCreate.json", StandardCharsets.UTF_8);
		String testFeedback = Reader.readFile("resources/test/data/feedbackLocationCreateTest.json", StandardCharsets.UTF_8);
		
		when(request.getParameter("data")).thenReturn(testData);
		when(request.getParameter("type")).thenReturn("Location");
		when(request.getParameter("operation")).thenReturn("create");
		when(request.getParameter("id")).thenReturn("5");
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);
		
		feedbackServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		
		System.out.println(result);
		assertTrue(result.equals("Success"));
		
		
		Location location = emf.createEntityManager().find(Location.class, 5);
		assertNotNull(location);
		
		GsonBuilder builder = new GsonBuilder();  
		builder.excludeFieldsWithoutExposeAnnotation();
		Gson gson = builder.create();
		String JSONData = gson.toJson(location);
		
		System.out.println(JSONData);
		assertTrue(JSONData.equals(testFeedback));
		
		
		pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        feedbackServlet.doPost(request, response);
        result = sw.getBuffer().toString();

        System.out.println(result);
        assertTrue(result.equals("Dieser User hat schon ein Feedback für diese Location abgegeben."));
        
		
		when(session.getAttribute("username")).thenReturn("testFail");
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        feedbackServlet.doPost(request, response);
        result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("User \"testFail\" existiert nicht."));
        
        
        when(request.getParameter("id")).thenReturn("-1");
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        feedbackServlet.doPost(request, response);
        result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("Zugehörige Location existiert nicht!"));
		
        
	}
	
	public void t2LUpdate() throws IOException, ServletException {
		System.out.println("\n\nLocation Feedback Test Update");
		
		String testData = Reader.readFile("resources/test/data/feedbackUpdate.json", StandardCharsets.UTF_8);
		String testFeedback = Reader.readFile("resources/test/data/feedbackLocationUpdateTest.json", StandardCharsets.UTF_8);
		
		when(request.getParameter("data")).thenReturn(testData);
		when(request.getParameter("type")).thenReturn("Location");
		when(request.getParameter("operation")).thenReturn("update");
		when(request.getParameter("id")).thenReturn("5");
		when(session.getAttribute("username")).thenReturn("test");
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);
		
		feedbackServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		
		System.out.println(result);
		assertTrue(result.equals("Success"));
		
		
		Location location = emf.createEntityManager().find(Location.class, 5);
		assertNotNull(location);
		
		GsonBuilder builder = new GsonBuilder();  
		builder.excludeFieldsWithoutExposeAnnotation();
		Gson gson = builder.create();
		String JSONData = gson.toJson(location);
		
		System.out.println(JSONData);
		assertTrue(JSONData.equals(testFeedback));
		
		
		
		when(request.getParameter("data")).thenReturn("[{\"author\":{username:\"testFail\"}}]");
		pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        feedbackServlet.doPost(request, response);
        result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("Du kannst nur dein eigenes Feedback bearbeiten."));
        
		
		when(session.getAttribute("username")).thenReturn("testFail");
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        feedbackServlet.doPost(request, response);
        result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("User \"testFail\" existiert nicht."));
        
        
        when(request.getParameter("id")).thenReturn("-1");
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        feedbackServlet.doPost(request, response);
        result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("Zugehörige Location existiert nicht!"));
	}
	
	public void t3LDelete() throws IOException, ServletException {
		System.out.println("\n\nLocation Feedback Test Delete");
		
		Users testUser = new Users();
		testUser.setUsername("testFail");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(testUser);
		em.getTransaction().commit();
		
		String testData = Reader.readFile("resources/test/data/feedbackUpdate.json", StandardCharsets.UTF_8);
		String testFeedback = Reader.readFile("resources/test/data/feedbackLocationUpdateTest.json", StandardCharsets.UTF_8);
		
		when(request.getParameter("type")).thenReturn("Location");
		when(request.getParameter("operation")).thenReturn("delete");
		when(request.getParameter("id")).thenReturn("5");
		when(session.getAttribute("username")).thenReturn("testFail");
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);
		
		when(request.getParameter("data")).thenReturn("[{\"id\":1}]");
		pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        feedbackServlet.doPost(request, response);
        String result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("Du kannst nur dein eigenes Feedback löschen."));
		
        
        em.getTransaction().begin();
		em.remove(testUser);
		em.getTransaction().commit();
		em.close();
		when(session.getAttribute("username")).thenReturn("testFail");
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        feedbackServlet.doPost(request, response);
        result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("User \"testFail\" existiert nicht."));
        
        
        when(request.getParameter("id")).thenReturn("-1");
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        feedbackServlet.doPost(request, response);
        result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("Zugehörige Location existiert nicht!"));   
        
        when(request.getParameter("id")).thenReturn("5");
        when(request.getParameter("data")).thenReturn(testData);
		when(session.getAttribute("username")).thenReturn("test");
		pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
		feedbackServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		
		System.out.println(result);
		assertTrue(result.equals("Success"));
		
		
		Location location = emf.createEntityManager().find(Location.class, 5);
		assertNotNull(location);
		assertTrue(location.getFeedback().size() == 0);
	}
	
	public void t1RCreate() throws IOException, ServletException {
		System.out.println("\n\n Route Feedback Test Create");
		
		String testData = Reader.readFile("resources/test/data/feedbackCreate.json", StandardCharsets.UTF_8);
		String testFeedback = Reader.readFile("resources/test/data/feedbackRouteCreateTest.json", StandardCharsets.UTF_8);
		
		when(request.getParameter("data")).thenReturn(testData);
		when(request.getParameter("type")).thenReturn("Route");
		when(request.getParameter("operation")).thenReturn("create");
		when(request.getParameter("id")).thenReturn("2");
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);
		
		feedbackServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		
		System.out.println(result);
		assertTrue(result.equals("Success"));
		
		
		Route route = emf.createEntityManager().find(Route.class, 2);
		assertNotNull(route);
		
		GsonBuilder builder = new GsonBuilder();  
		builder.excludeFieldsWithoutExposeAnnotation();
		Gson gson = builder.create();
		String JSONData = gson.toJson(route);
		
		System.out.println(JSONData);
		assertTrue(JSONData.equals(testFeedback));
		
		
		pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        feedbackServlet.doPost(request, response);
        result = sw.getBuffer().toString();

        System.out.println(result);
        assertTrue(result.equals("Dieser User hat schon ein Feedback für diese Route abgegeben."));
        
		
		when(session.getAttribute("username")).thenReturn("testFail");
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        feedbackServlet.doPost(request, response);
        result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("User \"testFail\" existiert nicht."));
        
        
        when(request.getParameter("id")).thenReturn("-1");
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        feedbackServlet.doPost(request, response);
        result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("Zugehörige Route existiert nicht!"));
		
	}
	
	public void t2RUpdate() throws IOException, ServletException {
		System.out.println("\n\nRoute Feedback Test Update");
		
		String testData = Reader.readFile("resources/test/data/feedbackUpdate2.json", StandardCharsets.UTF_8);
		String testFeedback = Reader.readFile("resources/test/data/feedbackRouteUpdateTest.json", StandardCharsets.UTF_8);
		
		when(request.getParameter("data")).thenReturn(testData);
		when(request.getParameter("type")).thenReturn("Route");
		when(request.getParameter("operation")).thenReturn("update");
		when(request.getParameter("id")).thenReturn("2");
		when(session.getAttribute("username")).thenReturn("test");
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);
		
		feedbackServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		
		System.out.println(result);
		assertTrue(result.equals("Success"));
		
		
		Route route = emf.createEntityManager().find(Route.class, 2);
		assertNotNull(route);
		
		GsonBuilder builder = new GsonBuilder();  
		builder.excludeFieldsWithoutExposeAnnotation();
		Gson gson = builder.create();
		String JSONData = gson.toJson(route);
		
		System.out.println(JSONData);
		assertTrue(JSONData.equals(testFeedback));
		
		
		
		when(request.getParameter("data")).thenReturn("[{\"author\":{username:\"testFail\"}}]");
		pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        feedbackServlet.doPost(request, response);
        result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("Du kannst nur dein eigenes Feedback bearbeiten."));
        
		
		when(session.getAttribute("username")).thenReturn("testFail");
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        feedbackServlet.doPost(request, response);
        result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("User \"testFail\" existiert nicht."));
        
        
        when(request.getParameter("id")).thenReturn("-1");
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        feedbackServlet.doPost(request, response);
        result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("Zugehörige Route existiert nicht!"));
	}
	
	public void t3RDelete() throws IOException, ServletException {
		System.out.println("\n\nLocation Feedback Test Delete");
		
		Users testUser = new Users();
		testUser.setUsername("testFail");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(testUser);
		em.getTransaction().commit();
		
		String testData = Reader.readFile("resources/test/data/feedbackUpdate2.json", StandardCharsets.UTF_8);
		String testFeedback = Reader.readFile("resources/test/data/feedbackRouteUpdateTest.json", StandardCharsets.UTF_8);
		
		when(request.getParameter("type")).thenReturn("Route");
		when(request.getParameter("operation")).thenReturn("delete");
		when(request.getParameter("id")).thenReturn("2");
		when(session.getAttribute("username")).thenReturn("testFail");
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);
		
		when(request.getParameter("data")).thenReturn("[{\"id\":2}]");
		pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        feedbackServlet.doPost(request, response);
        String result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("Du kannst nur dein eigenes Feedback löschen."));
		
        
        em.getTransaction().begin();
		em.remove(testUser);
		em.getTransaction().commit();
		em.close();
		when(session.getAttribute("username")).thenReturn("testFail");
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        feedbackServlet.doPost(request, response);
        result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("User \"testFail\" existiert nicht."));
        
        
        when(request.getParameter("id")).thenReturn("-1");
        pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
        feedbackServlet.doPost(request, response);
        result = sw.getBuffer().toString();
        
        System.out.println(result);
        assertTrue(result.equals("Zugehörige Route existiert nicht!"));   
        
        when(request.getParameter("id")).thenReturn("2");
        when(request.getParameter("data")).thenReturn(testData);
		when(session.getAttribute("username")).thenReturn("test");
		pw.flush();
 	    sw.getBuffer().delete(0, sw.getBuffer().length());
		feedbackServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		
		System.out.println(result);
		assertTrue(result.equals("Success"));
		
		
		Route route = emf.createEntityManager().find(Route.class, 2);
		assertNotNull(route);
		assertTrue(route.getFeedback().size() == 0);
	}
	
}
