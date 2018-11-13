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

import model.Address;
import model.Location;
import util.JSON;

/**
 * Servlet implementation class LocationServlet
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

	private static String read(EntityManager em, String type) throws Exception {

		try {
			// Select Location from database table
			Query query = em.createQuery("SELECT l FROM Location l WHERE l.type = '" + type + "'");
			List<Location> result = query.getResultList();
			String JSONData;

			// check for empty resultList
			if (result.size() > 0) {
				// convert data to JSON
				JSONData = JSON.locationToJSON(result);
			} else {
				JSONData = "[]";
			}
			// return
			return JSONData;
		} catch (Exception e) {
			throw e;
		}
	}

	private static String create(List<Location> locations, EntityManager em) {

		Integer errorCount = 0;
		ArrayList<String> loc = new ArrayList<String>();

		// Loop over Locations that should be created
		for (Location location : locations) {
			Query query = em.createQuery("SELECT l from Location l WHERE l.latitude = " + location.getLatitude()
							+ " AND l.longitude = " + location.getLatitude());
			List<Location> result = query.getResultList();

			// If location was not found, it should be created
			if (result.size() == 0) {
				em.getTransaction().begin();
				em.persist(location.getAddress());
				/*
				 * for (Base64 image : location.getImages()) { byte[] decodedImage =
				 * Base64.decode(image, 0); }
				 */
				em.persist(location);
				em.getTransaction().commit();
			} else {
				errorCount++;
				loc.add(location.getName());
			}
		}
		return errorCount == 0 ? "Success" : "{ errors: " + errorCount + ", locations: " + loc.toString() + "}";
	}

	private static String delete(List<Location> locations, EntityManager em) throws Exception {
		// Loop over Locations that should be deleted
		for (Location location : locations) {
			Location result = em.find(Location.class, location.getId());
			em.getTransaction().begin();
			em.remove(result);
			em.getTransaction().commit();
		}
		return "Success";
	}

	private static String update(List<Location> locations, EntityManager em) throws Exception {

		try {
			Integer errorCount = 0;
			ArrayList<String> loc = new ArrayList<String>();

			// Loop over Locations that should be updated
			for (Location location : locations) {
				Query query = em.createQuery("SELECT l from Location l WHERE l.id = " + location.getId());
				List<Location> result = query.getResultList();

				// If location was found, it should be updated
				if (result.size() > 0) {
					Location resultLocation = result.get(0);
					resultLocation.setName(location.getName());
					resultLocation.setTime(location.getTime());
					resultLocation.setType(location.getType());
					resultLocation.setDescription(location.getDescription());
					resultLocation.setImages(location.getImages());
					resultLocation.setLatitude(location.getLatitude());
					resultLocation.setLongitude(location.getLongitude());

					// update corresponding Address
					Address address = location.getAddress();
					query = em.createQuery("SELECT a from Address a WHERE"
									+ " a.country = " + address.getCountry()
									+ " a.postCode = " + address.getPostCode()
									+ " a.cityName = " + address.getCityName()
									+ " a.streetName = " + address.getStreetName()
									+ " a.houseNumber = " + address.getHouseNumber());
					List<Address> resultAddresses = query.getResultList();

					if (resultAddresses.size() > 0) {
						Address resultAddress = resultAddresses.get(0);
						resultAddress.setAddress(address);
					} else {
						em.getTransaction().begin();
						em.persist(address);
						resultLocation.setAddress(address);
						em.getTransaction().commit();
					}
				} else {
					errorCount++;
					loc.add(location.getName());
				}
			}
			return errorCount == 0 ? "Success" : "{ errors: " + errorCount + ", locations: " + loc.toString() + "}";
		} catch (Exception e) {
			throw e;
		}
	}

	private static String report(List<Location> locations, EntityManager em) throws Exception {

		try {
			Integer errorCount = 0;
			ArrayList<String> loc = new ArrayList<String>();

			// Loop over Locations that should be reported
			for (Location location : locations) {
				Query query = em.createQuery("SELECT l from Location l WHERE l.id = " + location.getId());
				List<Location> result = query.getResultList();

				// If location was found, it should be updated
				if (result.size() > 0) {
					Location resultLocation = result.get(0);
					int timesReported = resultLocation.getTimesReported();

					if (timesReported == 2) {
						// delete Location
						ArrayList<Location> deleteLocations = new ArrayList<Location>();
						deleteLocations.add(location);
						delete(deleteLocations, em);
					} else {
						resultLocation.setTimesReported(timesReported++);
					}

				} else {
					errorCount++;
					loc.add(location.getName());
				}
			}
			return errorCount == 0 ? "Success" : "{ errors: " + errorCount + ", locations: " + loc.toString() + "}";
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {

		// retrieve EntityManagerFactory and create EntityManager
		EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
		EntityManager em = emf.createEntityManager();

		// Define response
		String res;

		// read Data
		try {
			String paramType = request.getParameter("type");
			res = read(em, paramType);
			response.setStatus(200);
		} catch (Exception e) {
			// send back error
			response.setStatus(500);
			res = e.getMessage();
		}
		// Send Response
		response.setContentType("application/json");
		PrintWriter writer = response.getWriter();
		writer.append(res);
		em.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {

		// retrieve EntityManagerFactory, create EntityManager and retrieve data
		EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
		EntityManager em = emf.createEntityManager();
		String JSONData = request.getParameter("data");
		List<Location> locations = JSON.toLocation(JSONData);
		String res = "";

		try {
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
				res = report(locations, em);
				break;
			}
			response.setStatus(200);
		} catch (Exception e) {
			// send back error
			response.setStatus(500);
			res = e.getMessage();
		}
		response.setContentType("application/json");
		PrintWriter writer = response.getWriter();
		writer.append(res);
		em.close();
	}
}