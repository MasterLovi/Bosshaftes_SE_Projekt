package util;
import java.util.ArrayList;
import java.util.List;

import model.Address;
import model.Feedback;
import model.Location;
import model.Users;

/** Util - Class JSON
 * - Converts DB-Objects to JSON String
 * - Parses JSON String to DB-Objects
 **/
public class JSON {
	
	/**
     * Get the Value of a single property which is formatted in JSON-String. Therefore uses a substring of a JSON-String.
     * @param substr - String containing at least the name of the property and it's value, split by the ":" character (like in JSON format).
     * @return {string} Value of the property within the given String.
     */
	private static String getStringValue(String substr) {
		substr = substr.indexOf(",") > -1 ? substr.substring(0, substr.indexOf(",")) : substr.substring(0, substr.indexOf("}"));
		substr = substr.substring(substr.indexOf(":"), substr.length());
		substr = substr.substring(substr.indexOf("\""), substr.length());
		substr = substr.substring(0, substr.indexOf("\""));
		return substr;
	}
	
	/**
     * Counts the occurrences of a given Substring, to count how many Objects there are within a JSON-String.
     * @param str - JSON-formatted String containing multiple objects.
     * @param strFind - String, whose occurrences in str indicate how many objects there are within the JSON-String str.
     * @return {number} Number of occurrences within the JSON-String.
     */
	private static Integer countObjectsInJSON(String str, String strFind) {
		
        int count = 0, fromIndex = 0;
        
        while ((fromIndex = str.indexOf(strFind, fromIndex)) != -1 ){
            count++;
            fromIndex++;
        }
        
		return count;
	}
	
	/**
     * Get the Value of a single property which is formatted in JSON-String. Therefore uses a substring of a JSON-String.
     * @param substr - String containing at least the name of the property and it's value, split by the ":" character (like in JSON format).
     * @return Value of the property within the given String.
     */
	private static Integer getIntValue(String substr) {
		substr = substr.indexOf(",") > -1 ? substr.substring(0, substr.indexOf(",")) : substr.substring(0, substr.indexOf("}"));
		substr = substr.substring(substr.indexOf(":"), substr.length());
		return Integer.parseInt(substr);
	}
	
	/**
     * Convert a List of Location-Objects to JSON-formatted String.
     * @param locations - List of Location-Objects. Will be formatted to JSON-String.
     * @return JSON-formatted String.
     */
	public static String locationToJSON(List<Location> locations) {
		
		if (locations != null && locations.size() > 0) {
			String json = "[";
		
			for(Location location : locations) {
			
				json = json 
						+ "\n" + "  {"
						+ "\n" + "    \"type\": \"Feature\","
						+ "\n" + "    \"properties\": {"
						+ "\n" + "      \"id\": " + location.getId() + ","
						+ "\n" + "      \"name\": \"" + location.getName() + "\","
						+ "\n" + "      \"location_type\": \"" + location.getType() + "\","
						+ "\n" + "      \"time\": " + location.getTimeInMinutes() + ","
						+ "\n" + "      \"address\": " + JSON.addressToJSON(location.getAddress()) + ","
						+ "\n" + "      \"feedback\": " + JSON.feedbackToJSON(location.getFeedback()) + ","
						+ "\n" + "      \"popupContent\": \"\""
						+ "\n" + "    },"
						+ "\n" + "    \"geometry\": {"
						+ "\n" + "      \"type\": \"Point\","
						+ "\n" + "      \"coordinates\": ["
						+ "\n" + "        " + location.getLatitude() + ","
						+ "\n" + "        " + location.getLongitude()
						+ "\n" + "      ]"
						+ "\n" + "    }"
						+ "\n" + "  },";
			}
			json = json.substring(0, json.length() - 1);
			json = json + "\n]";
			return json;
		} else {
			return "[]";
		}
	}
	
	/**
     * Convert an Address-Object to JSON-formatted String.
     * @param address - Address-Object. Will be formatted to JSON-String.
     * @return JSON-formatted String.
     */
	public static String addressToJSON(Address address) {
		if (address != null) {
			String json = "{";
			json = json 
					+ "\n" + "        \"address\": \"" + address.getStreetName() + " " + address.getHouseNumber() + ", " + address.getPostCode() + " " + address.getCityName() + ", " + address.getCountry() + "\","
					+ "\n" + "        \"cityName\": \"" + address.getCityName() + "\","
					+ "\n" + "        \"country\": \"" + address.getCountry() + "\","
					+ "\n" + "        \"houseNumber\": \"" + address.getHouseNumber() + "\","
					+ "\n" + "        \"postCode\": \"" + address.getPostCode() + "\","
					+ "\n" + "        \"streetName\": \"" + address.getStreetName() + "\""
					+ "\n" + "      }";
			return json;
		} else {
			return "{}";
		}
	}
	
	/**
     * Convert a List of Feedback-Objects to JSON-formatted String.
     * @param ratings - List of Feedback-Objects. Will be formatted to JSON-String.
     * @return JSON-formatted String.
     */
	public static String feedbackToJSON(List<Feedback> ratings) {
		
		String json = "[";
		
		if (ratings != null && ratings.size() > 0) {
			for(Feedback feedback : ratings) {
				
				json = json
						+ "\n" + "        {"
						+ "\n" + "          \"id\": " + feedback.getId() + ","
						+ "\n" + "          \"author\": \"" + feedback.getAuthor().getUsername() + "\","
						+ "\n" + "          \"comment\": \"" + feedback.getComment() + "\","
						+ "\n" + "          \"rating\": " + feedback.getRating()
						+ "\n" + "        },";
			}
			
			json = json.substring(0, json.length() - 1);
			json = json
					+ "\n" + "      ]";
			
			return json;
		} else {
			return "[]";
		}
	}
	
	/**
     * Parses a JSON formatted String into a List of Location-Objects.
     * @param json - JSON formatted String or Substring containing one or more Location-Objects.
     * @return List of Location-Objects.
     */
	public static List<Location> toLocation(String json) {
		
		List<Location> locations = new ArrayList<Location>();
		Integer count = countObjectsInJSON(json, "properties");
		
		for (Integer i = 0; i < count; i++) {
			
			try {
				
				Location location = new Location();
				
				//find properties
				String id = json.substring(json.indexOf("id"));
				String name = json.substring(json.indexOf("name"));
				String type = json.substring(json.indexOf("location_type"));
				String time = json.substring(json.indexOf("time"));
				String address = json.substring(json.indexOf("address"));
				String coordinates = json.substring(json.indexOf("coordinates"));
				String feedback = json.substring(json.indexOf("feedback"), json.indexOf("]"));
				
				//set Properties
				location.setId(getIntValue(id));
				location.setName(getStringValue(name));
				location.setTimeInMinutes(getIntValue(time));
				location.setType(getStringValue(type));
				location.setAddress(toAddress(address));
				location.setFeedback(toFeedback(feedback));
				
				//set Coordinates
				String latitude = coordinates.substring(coordinates.indexOf("[") + 1, coordinates.indexOf(","));
				String longitude = coordinates.substring(coordinates.indexOf(",") + 1, coordinates.indexOf("]"));
				location.setLatitude(Double.parseDouble(latitude));
				location.setLongitude(Double.parseDouble(longitude));
				
				//add to list
				locations.add(location);
				
				json = json.substring(json.indexOf("properties"));
				
			} catch (Exception e) {
				e.printStackTrace();
				json = json.substring(json.indexOf("properties"));
			}
		}
		
		return locations;
	}
	
	/**
     * Parses a JSON formatted String into Address-Object.
     * @param json - JSON formatted String or Substring containing one Address-Object.
     * @return Address-Object.
     */
	public static Address toAddress(String json) {
		
		Address address = new Address();
		String cityName = json.substring(json.indexOf("cityName"));
		String country = json.substring(json.indexOf("country"));
		String houseNumber = json.substring(json.indexOf("houseNumber"));
		String postCode = json.substring(json.indexOf("postCode"));
		String streetName = json.substring(json.indexOf("streetName"));
		
		address.setCityName(getStringValue(cityName));
		address.setCountry(getStringValue(country));
		address.setHouseNumber(getIntValue(houseNumber));
		address.setPostCode(getIntValue(postCode));
		address.setStreetName(streetName);
		
		return address;
	}
	
	/**
     * Parses a JSON formatted String into a List of Feedback-Objects.
     * @param json - JSON formatted String or Substring containing one or more Feedback-Objects.
     * @return List of Feedback-Objects.
     */
	public static List<Feedback> toFeedback(String json) {
		
		List<Feedback> ratings = new ArrayList<Feedback>();
		Integer count = countObjectsInJSON(json, "{");
		
		for (Integer i = 0; i < count; i++) {
			try {
				
				//find properties
				String author = json.substring(json.indexOf("author"));
				String comment = json.substring(json.indexOf("comment"));
				String rating = json.substring(json.indexOf("ratings"));
				
				//create Feedback Object and set Values ('Comment' first)
				Feedback feedback = new Feedback();
				feedback.setComment(getStringValue(comment));
				
				//set Rating Property
				if (getIntValue(rating) >= 1 || getIntValue(rating) <= 5) {
					feedback.setRating(getIntValue(rating));
				}
				
				//set Author
				Users user = new Users();
				user.setUsername(getStringValue(author));
				feedback.setAuthor(user);
				
				//Add Feedback Object to Rating List
				ratings.add(feedback);
				
				//remove first Object from JSON String
				json = json.substring(json.indexOf("}"));
				
			} catch (Exception e) {
				e.printStackTrace();
				json = json.substring(json.indexOf("}"));
			}
		}
		
		return ratings;
		
	}

}
