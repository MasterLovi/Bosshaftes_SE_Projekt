package servlets;

import java.io.IOException;
import java.io.PrintWriter;
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

	private static String read(EntityManager em, String type) throws Exception {

		// Select Location from database table
		Query query = em.createQuery("SELECT l FROM Location l WHERE l.type = '" + type + "'");
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
		// return
		return JSONData;
	}

	private static String create(List<Location> locations, EntityManager em) throws Exception {
		em.getTransaction().begin();
		// Loop over Locations that should be created
		for (Location location : locations) {
			Query query = em.createQuery("SELECT l from Location l WHERE l.latitude = " + location.getLatitude()
					+ " AND l.longitude = " + location.getLatitude());
			List<Location> result = query.getResultList();

			// If location was not found, it should be created
			if (result.size() == 0) {
				em.persist(location);
			} else {
				throw new Exception("Location: " + location.getName() + " existiert bereits.");
			}
		}
		em.getTransaction().commit();
		return "Sucess";
	}

	private static String delete(List<Location> locations, EntityManager em) throws Exception {
		// Loop over Locations that should be deleted
		em.getTransaction().begin();
		for (Location location : locations) {
			Location result = em.find(Location.class, location.getId());
			em.remove(result);
		}
		em.getTransaction().commit();
		return "Success";
	}

	private static String update(List<Location> locations, EntityManager em) throws Exception {
		
		em.getTransaction().begin();
		// Loop over Locations that should be updated
		for (Location location : locations) {
			Query query = em.createQuery("SELECT l from Location l WHERE l.id = " + location.getId());
			List<Location> result = query.getResultList();

			// If location was found, it should be updated
			if (result.size() > 0) {
				Location resultlocation = result.get(0);
				resultlocation.setName(location.getName());
				resultlocation.setTime(location.getTime());
				resultlocation.setType(location.getType());
				resultlocation.setLatitude(location.getLatitude());
				resultlocation.setLongitude(location.getLongitude());

				// update corresponding Address
				Address address = location.getAddress();
				query = em.createQuery(
						"SELECT a from Address a WHERE" + " a.country = " + address.getCountry() + " a.postCode = "
								+ address.getPostCode() + " a.cityName = " + address.getCityName() + " a.streetName = "
								+ address.getStreetName() + " a.houseNumber = " + address.getHouseNumber());
				List<Address> resultAddresses = query.getResultList();

				if (resultAddresses.size() > 0) {
					Address resultAddress = resultAddresses.get(0);
					resultAddress.setAddress(address);
				} else {
					em.persist(address);
					resultlocation.setAddress(address);
				}
			} else {
				throw new Exception("Location: " + location.getName() + " does not exist.");
			}
		}
		em.getTransaction().commit();
		return "Success";
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