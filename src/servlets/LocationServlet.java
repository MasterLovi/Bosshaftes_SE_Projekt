package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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

import model.Address;
import model.Feedback;
import model.Location;
import util.Time;

/**
 * Servlet implementation class LocationServlet - This servlet is responsible
 * for reading location data from the database, creating new entries, deleting
 * and updating entries, as well as reporting a location (different kind of
 * update)
 */
@WebServlet("/LocationServlet")
public class LocationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LocationServlet() {
		super();
	}

	/**
	 * Method to read location data from the database
	 * 
	 * @param em {EntityManager} - EntityManager manages database operations
	 * @param type {String} - Type of Location (must be "Kultur" or "Party")
	 * @param boundNorthWestLat {Double} - Latitude of upper left corner of the map
	 * @param boundNorthWestLong {Double} - Longitude of upper left corner of the map
	 * @param boundSouthEastLat {Double} - Latitude of lower right corner of the map
	 * @param boundSouthEastLong {Double} - Longitude of lower right corner of the map
	 * @return {String} - JSONData with found locations; if no entries are found it returns an empty array []
	 * @exception - Exception if type is null
	 * @exception - Exception if type is neither "Kultur" nor "Party"
	 */
	private static String read(EntityManager em, String type, double boundNorthWestLat, double boundNorthWestLong,
			double boundSouthEastLat, double boundSouthEastLong) throws Exception {

		// check if required type parameter is set
		if (type == null) {
			throw new Exception("Type darf nicht null sein!");
		} else if (!(type.equals("Party") || type.equals("Kultur"))) {
			throw new Exception("Type can only be \"Party\" or \"Kultur\".");
		}

		// Build query with given parameters
		String selectQuery = "SELECT l FROM Location l " + "WHERE l.type = '" + type + "'" + " AND l.latitude BETWEEN "
				+ boundSouthEastLat + " AND " + boundNorthWestLat + " AND l.longitude BETWEEN " + boundNorthWestLong
				+ " AND " + boundSouthEastLong;

		// Select Location from database table
		Query query = em.createQuery(selectQuery);
		List<Location> result = query.getResultList();

		String JSONData;

		// check for empty resultList
		if (result.size() > 0) {
			for (Location location : result) {
				location.setTime(new Time(location.getTimeString()));
				List<String> images = new ArrayList<String>();
				// convert pictures and data to JSON
				if (location.getPictures() != null) {
					for (byte[] picture : location.getPictures()) {
						String image64 = new String(picture, "UTF-8");
						images.add(image64);
					}
					location.setImages(images);
//					System.out.println(images);
				} else {
					location.setImages(null);
				}
			}
			// Convert Data to JSON
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
	 * Method to create new locations in the database
	 * 
	 * @param locations {List<Location>} - List of locations that should be created
	 * @param em {EntityManager} - EntityManager manages database operations
	 * @return {String} - "Success" if the locations are successfully created
	 * @exception - Exception if one of the locations is already in the database
	 */
	private static String create(List<Location> locations, EntityManager em) throws Exception {
		// Loop over Routes that should be created
		for (Location location : locations) {

			// find out if location already exists
			String selectQuery = "SELECT l from Location l" + " WHERE l.latitude = " + location.getLatitude()
					+ " AND l.longitude = " + location.getLatitude();
			Query query = em.createQuery(selectQuery);
			List<Location> result = query.getResultList();

			// if the locations aren't already in the database, start persisting
			if (result.size() == 0) {
				
				// Set Location Attributes
				Location newLocation = new Location();
				newLocation.setName(location.getName());
				newLocation.setType(location.getType());
				newLocation.setTimeString(location.getTime().getTime());
				newLocation.setFeedback((List<Feedback>) new ArrayList<Feedback>());
				newLocation.setAddress(location.getAddress());
				newLocation.setLatitude(location.getLatitude());
				newLocation.setLongitude(location.getLongitude());
				newLocation.setTimesReported(0);
				newLocation.setDescription(location.getDescription());
				newLocation.setUserReports((List<String>) new ArrayList<String>());

				// Convert and save Images
				List<byte[]> images = new ArrayList<byte[]>();
				if (location.getImages() != null) {
					for (String sBase64 : location.getImages()) {
						if (sBase64 != null) {
							System.out.println(sBase64);
							byte[] image = sBase64.getBytes("UTF-8");
							images.add(image);
						}
					}
					newLocation.setPictures(images);
				} else {
					newLocation.setPictures(null);
				}
				
				// Persist Location
				em.persist(newLocation);
			} else {
				throw new Exception("Location \"" + location.getName() + "\" exists already.");
			}
		}
		return "Success";
	}

	/**
	 * Method to delete locations from the database
	 * 
	 * @param locations {List<Location>} - List of locations that should be deleted
	 * @param em [{EntityManager} - EntityManager manages database operations
	 * @return {String} - "Success" if locations are successfully deleted
	 * @exception - Exception if one location that should be deleted doesn't exist in the database
	 */
	private static String delete(List<Location> locations, EntityManager em) throws Exception {
		// Loop over Locations that should be deleted
		for (Location location : locations) {
			Location result = em.find(Location.class, location.getId());
			if (result != null) {
				em.remove(result);
			} else {
				throw new Exception("Location \"" + location.getName() + "\" does not exist.");
			}
		}
		return "Success";
	}

	/**
	 * Method to update locations in the database; all information will be updated,
	 * there is no check if only some attributes have changed
	 * 
	 * @param locations {List<Location>} - List of locations that should be updated
	 * @param em {EntityManager} - EntityManager manages database operations
	 * @return {String} - "Success" if locations are successfully updated
	 * @exception - Exception if one location that should be updated doesn't exist
	 */
	private static String update(List<Location> locations, EntityManager em) throws Exception {

		// Loop over Locations that should be updated
		for (Location location : locations) {
			String selectQuery = "SELECT l from Location l WHERE l.id = " + location.getId();
			Query query = em.createQuery(selectQuery);

			List<Location> result = query.getResultList();

			// If location was found, it should be updated
			if (result.size() > 0) {
				Location resultLocation = result.get(0);
				resultLocation.setName(location.getName());
				resultLocation.setTimeString(location.getTime().getTime());
				resultLocation.setType(location.getType());
				resultLocation.setLatitude(location.getLatitude());
				resultLocation.setLongitude(location.getLongitude());
				resultLocation.setDescription(location.getDescription());

				// Convert and update Images
				List<byte[]> images = new ArrayList<byte[]>();
				if (location.getImages() != null) {
					for (String sBase64 : location.getImages()) {
						if (sBase64 != null) {
							byte[] image = sBase64.getBytes("UTF-8");
							images.add(image);
						}
					}
					resultLocation.setPictures(images);
				} else {
					resultLocation.setPictures(null);
				}

				// update corresponding Address
				Address address = location.getAddress();
				query = em.createQuery("SELECT a from Address a WHERE" + " a.id = " + address.getId());
				List<Address> resultAddresses = query.getResultList();

				if (resultAddresses.size() > 0) {
					Address resultAddress = resultAddresses.get(0);
					resultAddress.setAddress(address);
				} else {
					em.persist(address);
					resultLocation.setAddress(address);
				}
			} else {
				throw new Exception("Location \"" + location.getName() + "\" does not exist.");
			}
		}
		return "Success";
	}

	/**
	 * Method to update the timesReported counter of a location
	 * 
	 * @param locations {List<Location>} - List of locations that should be reported
	 * @param em {EntityManager} - EntityManager manages database operations
	 * @return {String} - "Success" if timesReported was successfully updated
	 * @exception - Exception if the location that should be reported doesn't exist
	 */
	private static String report(List<Location> locations, EntityManager em, HttpSession session) throws Exception {
		// Loop over Locations that should be reported
		for (Location location : locations) {
			Query query = em.createQuery("SELECT l from Location l WHERE l.id = " + location.getId());
			List<Location> result = query.getResultList();

			// If location was found, it should be updated
			if (result.size() > 0) {
				Location resultLocation = result.get(0);
				int timesReported = resultLocation.getTimesReported();

				for (String username : resultLocation.getUserReports()) {
					if (username.equals(session.getAttribute("username"))) {
						throw new Exception("You have already reported this location.");
					}
				}

				if (timesReported == 2) {
					// delete Location
					List<Location> deleteLocations = new ArrayList<Location>();
					deleteLocations.add(location);
					delete(deleteLocations, em);
				} else {
					timesReported++;
					resultLocation.setTimesReported(timesReported);
					resultLocation.getUserReports().add((String) session.getAttribute("username"));
				}

			} else {
				throw new Exception("Location \"" + location.getName() + "\" does not exist.");
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
			double paramBoundNorthWestLat = Double.valueOf(request.getParameter("boundNorthWestLat"));
			double paramBoundNorthWestLong = Double.valueOf(request.getParameter("boundNorthWestLng"));
			double paramBoundSouthEastLat = Double.valueOf(request.getParameter("boundSouthEastLat"));
			double paramBoundSouthEastLong = Double.valueOf(request.getParameter("boundSouthEastLng"));

			// read with parameters
			res = read(em, paramType, paramBoundNorthWestLat, paramBoundNorthWestLong, paramBoundSouthEastLat,
					paramBoundSouthEastLong);
			response.setStatus(200);
		} catch (Exception e) {
			// send back error
			response.setStatus(500);
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
	 * @exception Exception if user is not logged in
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// retrieve EntityManagerFactory, create EntityManager and retrieve data (= list
		// of locations)
		HttpSession session = request.getSession();
		EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
		EntityManager em = emf.createEntityManager();
		Gson gson = new Gson();
		List<Location> locations = gson.fromJson(request.getParameter("json"), new TypeToken<List<Location>>() {
		}.getType());
		String res = "";
		em.getTransaction().begin();
		try {
			if ((boolean) session.getAttribute("loggedin") != true) {
				throw new Exception("You are not logged in.");
			}
			// get operation parameter and run the corresponding method
			switch (request.getParameter("operation")) {
			case "update":
				res = update(locations, em);
				break;
			case "create":
				res = create(locations, em);
				break;
			case "delete":
				res = delete(locations, em);
				break;
			case "report":
				res = report(locations, em, session);
				break;
			}
			response.setStatus(200);
			em.getTransaction().commit();
		} catch (Exception e) {
			// Create Error Response
			response.setStatus(500);
			res = e.getMessage();
			em.getTransaction().rollback();
		}
		// Send back Response
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.append(res);
		em.close();
	}
}
