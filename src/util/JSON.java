package util;
import java.util.List;

import model.Address;
import model.Location;

public class JSON {
	
	public static String locationToJSON(List<Location> locations) {
		
		String json = "[";
		
		for(Location location : locations) {
			
			json = json 
					+ "\n" + "  {"
					+ "\n" + "    \"type\": \"Feature\","
				    + "\n" + "    \"properties\": {"
				    + "\n" + "      \"name\": \"" + location.getName() + "\","
					+ "\n" + "      \"location_type\": \"" + location.getType() + "\","
					+ "\n" + "      \"time\": " + location.getTimeInMinutes() + ","
					+ "\n" + "      \"address\": " + JSON.addressToJSON(location.getAddress()) + ","
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
	}
	
	public static String addressToJSON(Address address) {
		return "";
	}

}
