package test;

import java.util.ArrayList;
import java.util.List;

import model.Address;
import model.Feedback;
import model.Location;
import model.Route;
import model.Users;
import util.JSON;

public class JSON_Test_unsauber {

	public static void main(String args[]) {

		List<Route> routes = new ArrayList<Route>();
		Users user = new Users();
		user.setId(1);
		user.setUsername("Test User");
		user.setEmail("test@test.de");
		user.setPassword("test");

		for (Integer i = 0; i < 2; i++) {

			Route route = new Route();
			route.setId(i);
			route.setName("Test Route " + i);
			route.setOwner(user);

			try {
				route.setType("Party");
			} catch (Exception e) {
				e.printStackTrace();
			}

			List<Location> locations = new ArrayList<Location>();
			List<Feedback> feedback = new ArrayList<Feedback>();

			for (Integer j = 0; j < 2; j++) {

				Location location = new Location();
				location.setId(j);
				location.setLatitude(j);
				location.setLongitude(j);
				location.setName("Test Location " + j);
				location.setTimeInMinutes(j);

				try {
					location.setType("Party");
				} catch (Exception e) {
					e.printStackTrace();
				}

				Address address = new Address();
				address.setCityName("Mannheim");
				address.setCountry("Deutschland");
				address.setHouseNumber(j);
				address.setPostCode(12345 + j);
				address.setStreetName("Test Street");

				Feedback rating = new Feedback();
				rating.setId(j);
				rating.setComment("Test Comment " + j);
				try {
					rating.setRating(j + 1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				rating.setAuthor(user);

				location.setAddress(address);
				List<Feedback> ratings = new ArrayList<Feedback>();
				ratings.add(rating);
				location.setFeedback(ratings);
				locations.add(location);

				feedback.add(rating);

				if (j == 0) {
					// System.out.println("Only Address:");
					// System.out.println(JSON.addressToJSON(address));
					// System.out.println("\n\n\n");
				}
			}

			route.setFeedback(feedback);
			route.setStops(locations);

			routes.add(route);

			if (i == 0) {
				// System.out.println("Only Feedback:");
				// System.out.println(JSON.feedbackToJSON(feedback));
				// System.out.println("\n\n\n");
				// System.out.println("Only Locations:");
				// System.out.println(JSON.locationToJSON(locations));
				// System.out.println("\n\n\n");
			}
		}

		System.out.println("Only Routes:");
		String leon = JSON.routeToJSON(routes);
		System.out.println(leon);

		Route testRoute = new Route();
		String json = leon;

		try {
			testRoute = JSON.toRoute(json).get(0);
			System.out.println(testRoute.getId());
			System.out.println(testRoute.getName());
			System.out.println(testRoute.getType());
			System.out.println(testRoute.getFeedback());
			System.out.println(testRoute.getOwner());
			System.out.println(testRoute.getStops());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
