package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import model.Feedback;
import model.Location;
import model.Route;
import model.Users;
import util.Time;

/**
 * Servlet implementation class RouteServlet - This servlet is responsible for
 * reading route data from the database, creating new entries, deleting and
 * updating entries
 */
@WebServlet("/RouteServlet")
public class RouteServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RouteServlet() {
		super();
	}

	/**
	 * Method to read route data from the database
	 * 
	 * @param em {EntityManager} - EntityManager manages database operations
	 * @param type {String} - Type of Location (must be "Kultur" or "Party")
	 * @param maxTime {String} - Maximum time that the routes selected should have
	 * @param minRating {Integer} - Minimal rating that the routes selected should have
	 * @param maxNoStops {Integer} - Maximum number of stops that the routes selected should have
	 * @param boundNorthWestLat {Double} - Latitude of upper left corner of the map
	 * @param boundNorthWestLong {Double} - Longitude of upper left corner of the map
	 * @param boundSouthEastLat {Double} - Latitude of lower right corner of the map
	 * @param boundSouthEastLong {Double} - Longitude of lower right corner of the map
	 * @return {String} - JSONData with found routes; if no entries are found it returns an empty array []
	 * @exception - Exception if type is null
	 * @exception - Exception if type is neither "Kultur" nor "Party"
	 */
	private static String read(EntityManager em, String type, String maxTime, double minRating, int maxNoStops,
			double boundNorthWestLat, double boundNorthWestLong, double boundSouthEastLat, double boundSouthEastLong,
			Integer owner) throws Exception {

		if (type == null) {
			throw new Exception("Type cannot be null.");
		} else if (!(type.equals("Party") || type.equals("Kultur"))) {
			throw new Exception("Type can only be \"Party\" or \"Kultur\".");
		}
		// Build query with given parameters
		String selectQuery = "SELECT r FROM Route r" + " WHERE r.type = '" + type + "'";

		String routeParams = " AND r.avgRating >= " + minRating + " AND r.numberOfStops <= " + maxNoStops
				+ " AND r.firstLat BETWEEN " + boundSouthEastLat + " AND " + boundNorthWestLat
				+ " AND r.firstLong BETWEEN " + boundNorthWestLong + " AND " + boundSouthEastLong;

		selectQuery = (owner != -1) ? (selectQuery + " AND r.owner.id = " + owner) : selectQuery + routeParams;

		// Select Route from database table
		Query query = em.createQuery(selectQuery);
		List<Route> result = query.getResultList();

		String JSONData;

		// check for empty resultList
		if (result.size() > 0) {
			
			// Filter result and remove Routes that are not applicable for the given maxtime
			for (Iterator<Route> iter = result.iterator(); iter.hasNext();) {
				Route route = iter.next();
				Time t = new Time(maxTime);
				route.setTime(new Time(route.getTimeString()));

				if (route.getTime().IsLessThan(t)) {
					
					// Convert and save Images
					List<String> images = new ArrayList<String>();
					
					// convert pictures and data to JSON
					if (route.getPictures() != null) {
						for (byte[] picture : route.getPictures()) {
							String image64 = new String(picture, "UTF-8");
							images.add(image64);
						}
						route.setImages(images);
					}
					
					// Set Stops
					if (route.getStops() != null) {
						for (Location stop : route.getStops()) {
							if (stop.getPictures() != null) {
								images = new ArrayList<String>();
								for (byte[] picture : stop.getPictures()) {
									String image64 = new String(picture, "UTF-8");
									images.add(image64);
								}
								stop.setImages(images);
							}
						}
					}
				} else {
					iter.remove();
				}
			}
			
			//Convert to JSON
			GsonBuilder builder = new GsonBuilder();
			builder.excludeFieldsWithoutExposeAnnotation();
			Gson gson = builder.create();
			JSONData = gson.toJson(result);
		} else {
			JSONData = "[]";
		}
		// return results
		return JSONData;
	}

	/**
	 * Method to create new routes in the database
	 * 
	 * @param routes {List<Route>} - List of routes that should be created
	 * @param em {EntityManager} - EntityManager manages database operations
	 * @param session {HttpSession} - Needed to retrieve the user that creates the route
	 * @return {String} - "Success" if the routes are successfully created
	 */
	private static String create(List<Route> routes, EntityManager em, HttpSession session) throws Exception {
		// Loop over Routes that should be created
		for (Route route : routes) {
			
			// Set Route Values
			Route newRoute = new Route();
			newRoute.setName(route.getName());
			newRoute.setType(route.getType());
			newRoute.setTimeString(route.getTime().getTime());
			newRoute.setFeedback((List<Feedback>) new ArrayList<Feedback>());
			newRoute.setAvgRating(3);
			newRoute.setDescription(route.getDescription());

			// Select Route from database table
			Query query = em
					.createQuery("SELECT u FROM Users u WHERE u.username = '" + session.getAttribute("username") + "'");
			List<Users> result = query.getResultList();
			if (result.size() > 0) {
				Users owner = result.get(0);
				newRoute.setOwner(result.get(0));
				owner.addRoute(newRoute);
			} else {
				newRoute.setOwner(null);
			}
			
			// Convert and Save images
			List<byte[]> images = new ArrayList<byte[]>();
			if (route.getImages() != null) {
				for (String sBase64 : route.getImages()) {
					byte[] image = sBase64.getBytes("UTF-8");
					images.add(image);
				}
				newRoute.setPictures(images);
			} else {
				newRoute.setPictures(null);
			}
			em.persist(newRoute);
			
			// Set Locations
			List<Location> locations = new ArrayList<Location>();
			if (route.getStops() != null && route.getStops().size() > 0) {
				for (Location location : route.getStops()) {
					Location l = em.find(Location.class, location.getId());
					if (l != null) {
						locations.add(l);
					}
				}
				newRoute.setFirstLong(locations.get(0).getLongitude());
				newRoute.setFirstLat(locations.get(0).getLatitude());
			}
			newRoute.setStops(locations);
			newRoute.setNumberOfStops(locations.size());

		}
		return "Success";
	}

	/**
	 * Method to delete routes from database
	 * 
	 * @param routes {List<Route>} - List of routes that should be deleted
	 * @param em {EntityManager} - EntityManager manages database operations
	 * @return {String} - "Success" if the routes are successfully deleted
	 * @exception - Exception if route doesn't exist
	 */
	private static String delete(List<Route> routes, EntityManager em) throws Exception {
		// Loop over Routes that should be deleted
		for (Route route : routes) {
			Route result = em.find(Route.class, route.getId());
			if (result != null) {
				em.remove(result);
			} else {
				throw new Exception("Route \"" + route.getName() + "\"does not exist.");
			}
		}
		return "Success";
	}

	/**
	 * Method to update routes in the database
	 * 
	 * @param routes {List<Route>} - List of routes that should be updated
	 * @param em {EntityManager} - EntityManager manages database operations
	 * @return {String} - "Success" if the routes are successfully updated
	 * @exception - Exception if route doesn't exist
	 */
	private static String update(List<Route> routes, EntityManager em) throws Exception {

		// Loop over Routes that should be updated
		for (Route route : routes) {
			Route result = em.find(Route.class, route.getId());

			if (result != null) {
				// Set Route Values
				result.setName(route.getName());
				result.setTimeString(route.getTime().getTime());
				result.setType(route.getType());
				result.setFeedback(route.getFeedback());
				result.setOwner(route.getOwner());

				//Update Locations
				List<Location> locations = new ArrayList<Location>();
				if (route.getStops() != null && route.getStops().size() > 0) {
					for (Location location : route.getStops()) {
						Location l = em.find(Location.class, location.getId());
						if (l != null) {
							locations.add(l);
						}
					}
					result.setFirstLong(locations.get(0).getLongitude());
					result.setFirstLat(locations.get(0).getLatitude());
				}
				result.setStops(locations);
				result.setNumberOfStops(locations.size());
				
				// Update Images
				List<byte[]> images = new ArrayList<byte[]>();
				if (route.getImages() != null) {
					for (String sBase64 : route.getImages()) {
						byte[] image = sBase64.getBytes("UTF-8");
						images.add(image);
					}
				}
				result.setPictures(images);

			} else {
				throw new Exception("Route \"" + route.getName() + "\" does not exist.");
			}

		}
		return "Success";
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// retrieve EntityManagerFactory and create EntityManager
		EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
		EntityManager em = emf.createEntityManager();
		// Define response
		String res;

		// read Data
		try {
			// retrieve all parameters
			String paramType = request.getParameter("type");
			String paramTime = request.getParameter("time");

			Integer paramStops, owner;
			Double paramRating, paramBoundNorthWestLat, paramBoundNorthWestLong,
					paramBoundSouthEastLat, paramBoundSouthEastLong;
			
			// Check if User Routes or all Routes
			if (request.getParameter("rating") != null) {
				
				// Set and Convert Parameters
				paramRating = Double.valueOf(request.getParameter("rating"));
				paramStops = Integer.valueOf(request.getParameter("stops"));
				paramBoundNorthWestLat = Double.valueOf(request.getParameter("boundNorthWestLat"));
				paramBoundNorthWestLong = Double.valueOf(request.getParameter("boundNorthWestLng"));
				paramBoundSouthEastLat = Double.valueOf(request.getParameter("boundSouthEastLat"));
				paramBoundSouthEastLong = Double.valueOf(request.getParameter("boundSouthEastLng"));
				owner = -1;
				
			} else {
				
				// Set and Convert Parameters
				owner = Integer.valueOf(request.getParameter("owner"));
				paramRating = -1.0;
				paramStops = -1;
				paramBoundNorthWestLat = -1.0;
				paramBoundNorthWestLong = -1.0;
				paramBoundSouthEastLat = -1.0;
				paramBoundSouthEastLong = -1.0;
			}
			
			// Do read operation
			res = read(em, paramType, paramTime, paramRating, paramStops, paramBoundNorthWestLat,
					paramBoundNorthWestLong, paramBoundSouthEastLat, paramBoundSouthEastLong, owner);

			response.setStatus(200);
		} catch (Exception e) {
			// send back error
			response.setStatus(500);
			e.printStackTrace();
			res = e.getMessage();
		}
		// Send Response
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.append(res);
		em.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// retrieve EntityManagerFactory, create EntityManager and retrieve data
		HttpSession session = request.getSession();
		EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
		EntityManager em = emf.createEntityManager();
		Gson gson = new Gson();
		List<Route> routes = gson.fromJson(request.getParameter("json"), new TypeToken<List<Route>>() {
		}.getType());
		String res = "";

		em.getTransaction().begin();
		
		// Check for operation and do it
		try {
			if ((boolean) session.getAttribute("loggedin") != true) {
				throw new Exception("You are not logged in.");
			}
			switch (request.getParameter("operation")) {
			case "update":
				res = update(routes, em);
				break;
			case "create":
				res = create(routes, em, session);
				break;
			case "delete":
				res = delete(routes, em);
				break;
			}
			
			// Create Response
			response.setStatus(200);
			em.getTransaction().commit();
		} catch (Exception e) {
			// send back error
			response.setStatus(500);
			res = e.getMessage();
			em.getTransaction().rollback();
		}
		
		// Send Response
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.print(res);
		em.close();
	}
}