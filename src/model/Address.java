package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

/**
 * This class represents the Address database table as mapped Java object
 */
@Entity
@Table(name = "ADDRESS")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	@Expose
	private int id;
	
	@Expose
	private String country;
	
	@Expose
	private int postCode;
	
	@Expose
	private String cityName;
	
	@Expose
	private String streetName;
	
	@Expose
	private int houseNumber;

	// getter
	public Address getAddress() {
		return this;
	}

	public int getId() {
		return id;
	}

	public String getCountry() {
		return country;
	}

	public int getPostCode() {
		return postCode;
	}

	public String getCityName() {
		return cityName;
	}

	public String getStreetName() {
		return streetName;
	}

	public int getHouseNumber() {
		return houseNumber;
	}

	// setter
	public void setId(int id) {
		this.id = id;
	}

	public void setAddress(Address address) {
		this.country = address.country;
		this.postCode = address.postCode;
		this.cityName = address.cityName;
		this.streetName = address.streetName;
		this.houseNumber = address.houseNumber;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setPostCode(int postCode) {
		this.postCode = postCode;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public void setHouseNumber(int houseNumber) {
		this.houseNumber = houseNumber;
	}

	// other methods
	@Override
	public String toString() {
		String addressString = "ADDRESS= "
						+ "Id: " + this.id + ", "
						+ this.streetName + " " + this.houseNumber + ", "
						+ this.postCode + " " + this.cityName + ", "
						+ this.country;

		return addressString;
	}

}
