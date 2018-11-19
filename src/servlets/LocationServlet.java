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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import model.Address;
import model.Location;

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

	private static String read(EntityManager em, String type, double[] boundNorthWest, double[] boundSouthEast)
					throws Exception {

		if (type == null) {
			throw new Exception("Type darf nicht null sein!");
		} else if (!(type.equals("Party") || type.equals("Kultur"))) {
			throw new Exception("Type muss entweder \"Party\" oder \"Kultur\" sein!");
		}

		// Build query with given parameters
		String selectQuery = "SELECT l FROM Location l "
						+ "WHERE l.type = '" + type + "'"
						+ " AND l.longtitude BETWEEN " + boundNorthWest[0] + " AND " + boundSouthEast[0]
						+ " AND l.latitude BETWEEN " + boundNorthWest[1] + " AND " + boundSouthEast[1];

		// Select Location from database table
		Query query = em.createQuery(selectQuery);
		List<Location> result = query.getResultList();

		String JSONData;

		// check for empty resultList
		if (result.size() > 0) {
			// convert data to JSON
			Gson gson = new Gson();
			JSONData = gson.toJson(result);
		} else {
			JSONData = "[]";
		}
		// return results
		return JSONData;
	}

	private static String create(List<Location> locations, EntityManager em) throws Exception {
		em.getTransaction().begin();
		// Loop over Locations that should be created
		for (Location location : locations) {
			// find out if location already exists
			String selectQuery = "SELECT l from Location l"
							+ " WHERE l.latitude = " + location.getLatitude()
							+ " AND l.longitude = " + location.getLatitude();
			Query query = em.createQuery(selectQuery);
			List<Location> result = query.getResultList();

			// If location was not found, it should be created
			if (result.size() == 0) {
				/*
				 * for (Base64 image : location.getImages()) { byte[] decodedImage =
				 * Base64.decode(image, 0); }
				 */
				em.persist(location);
			} else {
				throw new Exception("Location \"" + location.getName() + "\" existiert bereits.");
			}
		}
		em.getTransaction().commit();
		return "Success";
	}

	private static String delete(List<Location> locations, EntityManager em) throws Exception {
		// Loop over Locations that should be deleted
		em.getTransaction().begin();
		for (Location location : locations) {
			Location result = em.find(Location.class, location.getId());
			if (result != null) {
				em.remove(result);
			} else {
				throw new Exception("Location \"" + location.getName()
								+ "\"existiert nicht und kann daher nicht gelöscht werden");
			}
		}
		em.getTransaction().commit();
		return "Success";
	}

	private static String update(List<Location> locations, EntityManager em) throws Exception {
		em.getTransaction().begin();
		// Loop over Locations that should be updated
		for (Location location : locations) {
			String selectQuery = "SELECT l from Location l WHERE l.id = " + location.getId();
			Query query = em.createQuery(selectQuery);
			List<Location> result = query.getResultList();

			// If location was found, it should be updated
			if (result.size() > 0) {
				Location resultLocation = result.get(0);
				resultLocation.setName(location.getName());
				resultLocation.setTime(location.getTime());
				resultLocation.setType(location.getType());
				resultLocation.setLatitude(location.getLatitude());
				resultLocation.setLongitude(location.getLongitude());
				resultLocation.setDescription(location.getDescription());
				resultLocation.setImages(location.getImages());

				// update corresponding Address
				Address address = location.getAddress();
				query = em.createQuery("SELECT a FROM Address a"
								+ " WHERE" + " a.country = " + address.getCountry()
								+ " a.postCode = "
								+ address.getPostCode() + " a.cityName = " + address.getCityName()
								+ " a.streetName = "
								+ address.getStreetName() + " a.houseNumber = "
								+ address.getHouseNumber());
				List<Address> resultAddresses = query.getResultList();

				if (resultAddresses.size() > 0) {
					Address resultAddress = resultAddresses.get(0);
					resultAddress.setAddress(address);
				} else {
					em.persist(address);
					resultLocation.setAddress(address);
				}
			} else {
				throw new Exception("Location \"" + location.getName() + "\" existiert nicht");
			}
		}
		em.getTransaction().commit();
		return "Success";
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
			// retrieve all parameters
			String paramType = request.getParameter("type");

			String[] paramBoundNorthWestString = request.getParameterValues("boundNorthWest");
			double[] paramBoundNorthWest = new double[2];
			paramBoundNorthWest[0] = Double.valueOf(paramBoundNorthWestString[0]);
			paramBoundNorthWest[1] = Double.valueOf(paramBoundNorthWestString[1]);

			String[] paramBoundSouthEastString = request.getParameterValues("boundSouthEast");
			double[] paramBoundSouthEast = new double[2];
			paramBoundSouthEast[0] = Double.valueOf(paramBoundSouthEastString[0]);
			paramBoundSouthEast[1] = Double.valueOf(paramBoundSouthEastString[1]);

			// read with parameters
			res = read(em, paramType, paramBoundNorthWest, paramBoundSouthEast);
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
		Gson gson = new Gson();
		List<Location> locations = gson.fromJson(request.getParameter("data"), new TypeToken<List<Location>>() {
		}.getType());
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