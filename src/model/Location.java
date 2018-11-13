package model;

import java.sql.Time;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "LOCATION")
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	private String name;
	private double longitude;
	private double latitude;
	private String type;
	private Time time;
	private int timesReported;
	private String description;

	@OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "ADDRESS_ID")
	private Address address;

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "FEEDBACK_ID")
	private List<Feedback> feedback;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public String getType() {
		return type;
	}

	public Time getTime() {
		return time;
	}

	public int getTimesReported() {
		return timesReported;
	}

	public String getDescription() {
		return description;
	}

	public Address getAddress() {
		return address;
	}

	public List<Feedback> getFeedback() {
		return feedback;
	}

	// setter
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setType(String type) throws Exception {
		if (type.equals("Kultur") || type.equals("Party")) {
			this.type = type;
		} else {
			throw new Exception("You can only add Locations of type \"Kultur\" or \"Party\"");
		}
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setTimesReported(int timesReported) {
		this.timesReported = timesReported;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setFeedback(List<Feedback> feedback) {
		this.feedback = feedback;
	}

	// other methods
	@Override
	public String toString() {
		String locationString = "LOCATION= "
						+ "Id: " + this.id + ", "
						+ "Name: " + this.name + ", "
						+ "Longtitude: " + this.longitude + ", "
						+ "Latitude: " + this.latitude + ", "
						+ "Type: " + this.type + ", "
						+ "Time: " + this.time.toString() + ", "
						+ "Address: " + this.address.toString() + ", "
						+ "Feedback: geb ich jetzt dazu sicherlich nicht aus";

		return locationString;
	}

}
