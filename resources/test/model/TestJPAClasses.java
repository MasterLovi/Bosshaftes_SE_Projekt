package test.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import model.Address;
import model.Feedback;
import model.Location;
import model.Route;
import model.Users;
import util.Time;

/**
 * 
 * Test Class for all Entities and their Methods
 *
 */
public class TestJPAClasses {
	
	// Private Test Entities
	private static Users user;
	private static Feedback feedback;
	private static Location location;
	private static Route route;
	private static Address address;
	
	/**
	 * Setup Tests once when Test Class is initialized.
	 *  - Initialize Test Entities and Collections
	 */
	@BeforeClass
	public static void setup() {
		System.out.println("JPA TestClasses Setup");
		
		// Create new Test Entities
		user = new Users();
		route = new Route();
		address = new Address();
		feedback = new Feedback();
		location = new Location();
		
		// Create new Test Collections to test Relationships between Entities
		List<Route> routes = new ArrayList<Route>();
		List<Feedback> rating = new ArrayList<Feedback>();
		List<Location> locations = new ArrayList<Location>();
		List<byte[]> pictures = new ArrayList<byte[]>();
		List<String> images = new ArrayList<String>();
		List<String> userReports = new ArrayList<String>();
		
		// Fill Collections with samples
		routes.add(route);
		locations.add(location);
		rating.add(feedback);
		images.add("test");
		userReports.add("test");
		
		// Set Test Values of Address Entity
		address.setId(-5);
		address.setPostCode(68169);
		address.setCityName("Mannheim");
		address.setCountry("Deutschland");
		address.setHouseNumber(0);
		address.setStreetName("testStreet");
		
		// Set Test Values for User Entity
		user.setId(-5);
		user.setUsername("test");
		user.setEmail("test@test.de");
		user.setPassword("xyz123");
		user.setRoutes(routes);
		user.setVisitedRoutes(routes);
		
		// Set Test Values for Feedback Entity
		feedback.setId(-5);
		feedback.setAuthor(user);
		feedback.setComment("test");
		
		// Set Test Values for Location Entity
		location.setId(-5);
		location.setName("test");
		location.setDescription("test");
		location.setLatitude(0);
		location.setLongitude(0);
		location.setAddress(address);
		location.setFeedback(rating);
		location.setPictures(pictures);
		location.setImages(images);
		location.setTime(new Time("05:05:05"));
		location.setTimesReported(0);
		location.setTimeString("05:05:05");
		location.setAvgRating(3.0);
		location.setUserReports(userReports);
		
		//Set Test Values for Route Entity
		route.setAvgRating(4);
		route.setDescription("test");
		route.setFeedback(rating);
		route.setFirstLat(0);
		route.setFirstLong(0);
		route.setId(-5);
		route.setImages(images);
		route.setName("test");
		route.setNumberOfStops(1);
		route.setOwner(user);
		route.setPictures(pictures);
		route.setStops(locations);
		route.setTime(new Time("05:05:05"));
		route.setTimeString("05:05:05");
	}
	
	/**
	 * Test User Entity Methods
	 */
	@Test
	public void tUsers() {
		
		// Test if Getter return expected Values from the Setup (also ensures, that setter are working)
		assertEquals("test@test.de", user.getEmail());
		assertEquals("test", user.getUsername());
		assertEquals(-5, user.getId());
		assertEquals("xyz123", user.getPassword());
		assertEquals(1, user.getRoutes().size());
		assertEquals(1, user.getVisitedRoutes().size());
		
		// Test if Routes can be removed from Users own Routes
		user.removeRoute(route);
		assertEquals(0, user.getVisitedRoutes().size());	
		
		// Test if Route can be added to Users own Routes
		user.addRoute(route);
		assertEquals(1, user.getVisitedRoutes().size());
		
		// Test User Entity toString() Method
		String test = "USER= "
				+ "Id: " + -5 + ", "
				+ "Username: " + "test" + ", "
				+ "Email: " + "test@test.de" + ", "
				+ "Password" + "xyz123";
		assertEquals(test, user.toString());
		
	}
	
	/**
	 * Test Address Entity Methods
	 */
	@Test
	public void tAddress() {
		
		// Test if Getter return expected Values from the Setup (also ensures, that setter are working)
		assertEquals(-5, address.getId());
		assertEquals("Mannheim", address.getCityName());
		assertEquals("Deutschland", address.getCountry());
		assertEquals(0, address.getHouseNumber());
		assertEquals(68169, address.getPostCode());
		assertEquals("testStreet", address.getStreetName());
		assertEquals(address, address.getAddress());
		
		// Test Address Entity toString() Method
		String test = "ADDRESS= "
				+ "Id: " + -5 + ", "
				+ "testStreet" + " " + 0 + ", "
				+ 68169 + " " + "Mannheim" + ", "
				+ "Deutschland";
		assertEquals(test, address.toString());
		
	}
	
	/**
	 * Test Feedback Entity Methods
	 */
	@Test
	public void tFeedback() {
		
		// Test if Getter return expected Values from the Setup (also ensures, that setter are working)
		assertEquals(-5, feedback.getId());
		assertEquals("test", feedback.getComment());
		assertEquals(user, feedback.getAuthor());
		
		// Test if Rating can be set and if getter returns expected Value
		try {
			feedback.setRating(5);
			assertEquals(5, feedback.getRating());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Invoke Rating Exception to ensure that Users can only set specified Values
		try {
			feedback.setRating(-5);
		} catch (Exception e) {
			assertEquals("Rating can only be in the range 1..5", e.getMessage());
		}
		
		// Test Feedback Entity toString() Method
		String test = "FEEDBACK= "
				+ "Id: " + -5 + ", "
				+ "Comment: " + "test" + ", "
				+ "Rating: " + 5 + ", "
				+ "Author (Username): " + "test" + ", ";
		assertEquals(test, feedback.toString());
		
	}
	
	/**
	 * Test Location Entity Methods
	 */
	@Test
	public void tLocation() {
		
		// Test if Getter return expected Values from the Setup (also ensures, that setter are working)
		assertEquals(-5, location.getId());
		assertTrue(location.getAvgRating() == 3.0);
		assertEquals(address, location.getAddress());
		assertEquals("test", location.getDescription());
		assertEquals(feedback, location.getFeedback().get(0));
		assertEquals("test", location.getImages().get(0));
		assertTrue(location.getLatitude() == 0.0);
		assertTrue(location.getLongitude() == 0.0);
		assertEquals("test", location.getName());
		assertEquals(0, location.getPictures().size());
		assertEquals("05:05:05", location.getTime().toString());
		assertEquals(0, location.getTimesReported());
		assertEquals("05:05:05", location.getTimeString());
		assertEquals("test", location.getUserReports().get(0));
		
		// Test if setter and Getter for Location Feedback work
		location.removeFeedback(feedback);
		assertEquals(0, location.getFeedback().size());
		location.addFeedback(feedback);
		assertEquals(feedback, location.getFeedback().get(0));
		location.removeFeedbackAtIndex(0);
		assertEquals(0, location.getFeedback().size());
		location.addFeedback(feedback);
		assertEquals(feedback, location.getFeedback().get(0));
		
		// Test if Type can be set and if getter returns expected Value
		try {
			location.setType("Kultur");
			assertEquals("Kultur", location.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Invoke Type Exception to ensure that Users can only set specified Values
		try {
			location.setType("test");
		} catch (Exception e) {
			assertEquals("You can only add Locations of type \"Kultur\" or \"Party\"", e.getMessage());
		}
		
		// Test Location toString() method
		String test = "LOCATION= " + "Id: " + -5 + ", " + "Name: " + "test" + ", " + "Longtitude: "
				+ 0.0 + ", " + "Latitude: " + 0.0 + ", " + "Type: " + "Kultur" + ", "
				+ "Time: "
				+ "05:05:05" + ", " + "TimesReported: " + 0 + ", " + "Description: "
				+ "test" + ", " + "Address: " + "ADDRESS= "
				+ "Id: " + -5 + ", "
				+ "testStreet" + " " + 0 + ", "
				+ 68169 + " " + "Mannheim" + ", "
				+ "Deutschland" + ", "
				+ "Feedback: not gonna print this here";
		assertEquals(test, location.toString());
	}
	
	/**
	 * Test Route Entity Methods
	 */
	@Test
	public void tRoute() {
		
		// Test if Getter return expected Values from the Setup (also ensures, that setter are working)
		assertEquals(-5, route.getId());
		assertTrue(route.getAvgRating() == 4);
		assertEquals("test", route.getDescription());
		assertEquals(feedback, route.getFeedback().get(0));
		assertEquals("test", route.getImages().get(0));
		assertTrue(route.getFirstLat() == 0.0);
		assertTrue(route.getFirstLong() == 0.0);
		assertEquals("test", route.getName());
		assertEquals(0, route.getPictures().size());
		assertEquals("05:05:05", route.getTime().toString());
		assertEquals(1, route.getNumberOfStops());
		assertEquals("05:05:05", route.getTimeString());
		assertEquals(user, route.getOwner());
		assertEquals(location, route.getStop(0));
		assertEquals(1, route.getStops().size());
		
		// Test if setter and Getter for Route Feedback work
		route.removeFeedback(feedback);
		assertEquals(0, route.getFeedback().size());
		route.addFeedback(feedback);
		assertEquals(feedback, route.getFeedback().get(0));
		route.removeFeedbackAtIndex(0);
		assertEquals(0, route.getFeedback().size());
		route.addFeedback(feedback);
		assertEquals(feedback, route.getFeedback().get(0));
		
		// Test if setter and Getter for Images work
		route.removeImage("test");
		assertEquals(0, route.getImages().size());
		route.addImage("test");
		assertEquals("test", route.getImages().get(0));
		route.removeImageAtIndex(0);
		assertEquals(0, route.getImages().size());
		route.addImage("test");
		assertEquals("test", route.getImages().get(0));
		
		// Test if setter and Getter for Route Stops work
		route.removeStop(location);
		assertEquals(0, route.getStops().size());
		route.addStop(location);
		assertEquals(location, route.getStops().get(0));
		route.removeStopAtIndex(0);
		assertEquals(0, route.getStops().size());
		route.addStop(location);
		assertEquals(location, route.getStops().get(0));
		
		// Test if Type can be set and if getter returns expected Value
		try {
			route.setType("Kultur");
			assertEquals("Kultur", route.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Invoke Type Exception to ensure that Users can only set specified Values
		try {
			route.setType("test");
		} catch (Exception e) {
			assertEquals("You can only add Routes of type \"Kultur\" or \"Party\"", e.getMessage());
		}
		
		// Test Route toString() method
		String test = "ROUTE= "
				+ "Id: " + -5 + ", "
				+ "Name: " + "test" + ", "
				+ "Type: " + "Kultur" + ", "
				+ "Time: " + "05:05:05" + ", "
				+ "Description: " + "test" + ", "
				+ "|| won't print Feedback and Location here";
		assertEquals(test, route.toString());
		
	}
	
}
