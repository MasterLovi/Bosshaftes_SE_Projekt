package model;

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
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;

import util.Time;

/**
 * This class represents the Location database table as mapped Java object
 */
@Entity
@Table(name = "LOCATION")
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	@Expose
	private int id;

	@Expose
	private String name;

	@Expose
	private double longitude;

	@Expose
	private double latitude;

	@Expose
	private String type;

	private String timeString;

	@Transient
	@Expose
	private Time time;

	@Expose
	private Integer timesReported;

	@Expose
	private String description;

	private List<byte[]> pictures;

	@Transient
	@Expose
	private List<String> images;

	private List<String> userReports;

	@OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "ADDRESS_ID")
	@Expose
	private Address address;

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "FEEDBACK_ID_L")
	@Expose
	private List<Feedback> feedback;

	@Expose
	private double avgRating;

	// getter
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

	public String getTimeString() {
		return timeString;
	}

	public int getTimesReported() {
		return timesReported;
	}

	public String getDescription() {
		return description;
	}

	public List<String> getImages() {
		return images;
	}

	public List<String> getUserReports() {
		return userReports;
	}

	public Address getAddress() {
		return address;
	}

	public List<Feedback> getFeedback() {
		return feedback;
	}

	public double getAvgRating() {
		return avgRating;
	}

	public List<byte[]> getPictures() {
		return pictures;
	}

	// setter
	public void setId(int id) {
		this.id = id;
	}

	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}

	public void setPictures(List<byte[]> pictures) {
		this.pictures = pictures;
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

	public void setUserReports(List<String> userReports) {
		this.userReports = userReports;
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

	public void setImages(List<String> images) {
		this.images = images;
	}

	public void setFeedback(List<Feedback> feedback) {
		this.feedback = feedback;
	}

	// other methods
	public void addFeedback(Feedback feedback) {
		this.feedback.add(feedback);
	}

	public void setAvgRating(double avgRating) {
		this.avgRating = avgRating;
	}

	public void removeFeedback(Feedback feedback) {
		this.feedback.remove(feedback);
	}

	public void removeFeedbackAtIndex(int index) {
		this.feedback.remove(index);
	}

	@Override
	public String toString() {
		String locationString = "LOCATION= " + "Id: " + this.id + ", " + "Name: " + this.name + ", " + "Longtitude: "
						+ this.longitude + ", " + "Latitude: " + this.latitude + ", " + "Type: " + this.type + ", "
						+ "Time: "
						+ this.time.toString() + ", " + "TimesReported: " + this.timesReported + ", " + "Description: "
						+ this.description + ", " + "Address: " + this.address.toString() + ", "
						+ "Feedback: not gonna print this here";

		return locationString;
	}

}
