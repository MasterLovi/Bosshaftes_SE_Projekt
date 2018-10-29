package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	private int id;
	private String name;
	private long longitude;
	private long latitude;
	private String type;
	private int timeInMinutes;
	private Address address;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public long getLongitude() {
		return longitude;
	}

	public long getLatitude() {
		return latitude;
	}

	public String getType() {
		return type;
	}

	public int getTimeInMinutes() {
		return timeInMinutes;
	}

	public Address getAddress() {
		return address;
	}

	// setter
	public void setName(String name) {
		this.name = name;
	}

	public void setLongitude(long longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(long latitude) {
		this.latitude = latitude;
	}

	public void setType(String type) throws Exception {
		if (type.equals("Kultur") || type.equals("Party")) {
			this.type = type;
		} else {
			throw new Exception("You can only add Locations of type \"Kultur\" or \"Party\"");
		}
	}

	public void setTimeInMinutes(int timeInMinutes) {
		this.timeInMinutes = timeInMinutes;
	}

	public void setAddress(Address address) {
		address.setAddress(address);
		;
	}

}
