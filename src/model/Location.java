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

	public void setAddress(Address address) {
		address.setAddress(address);
		;
	}

}
