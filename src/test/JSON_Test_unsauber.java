package test;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import model.Address;
import model.Feedback;
import model.Location;
import model.Route;
import model.Users;

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

			Time time = new Time(2, 30, 0);

			for (Integer j = 0; j < 2; j++) {

				Location location = new Location();
				location.setId(j);
				location.setLatitude(j);
				location.setLongitude(j);
				location.setName("Test Location " + j);
				location.setTime(time);

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

			}

			route.setFeedback(feedback);
			route.setStops(locations);

			routes.add(route);

		}
		
		Gson gson = new Gson();
        String json = gson.toJson(routes);
        System.out.println(json + "\n\n\n");
        
        routes = gson.fromJson(json, new TypeToken<List<Route>>(){}.getType());
        
        for (Route r : routes) {
        	System.out.println(r.toString());
        	for (Location l : r.getStops()) {
        		System.out.println(l.toString());
        		for (Feedback f : r.getFeedback()) {
            		System.out.println(f.toString());
            	}
        	}
        	for (Feedback f : r.getFeedback()) {
        		System.out.println(f.toString());
        	}
        }
        
//        Type routeListType = new TypeToken<List<Route>>(){}.getType();
//        List<Route> founderList = gson.fromJson(founderJson, founderListType);  

	}

}
