package model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;

import util.Time;

/**
 * This class represents the Route database table as mapped Java object
 */
@Entity
@Table(name = "ROUTE")
public class Route {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	@Expose
	private int id;
	
	@Expose
	private String name;
	
	@Expose
	private String type;

	private String timeString;

	@Transient
	@Expose
	private Time time;
	
	@Expose
	private String description;

	private List<byte[]> pictures;

	@Transient
	@Expose
	private List<String> images;

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "FEEDBACK_ID")
	@Expose
	private List<Feedback> feedback;
	
	@Expose
	private double avgRating;

	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "ROUTES_LOCATION", joinColumns = @JoinColumn(name = "ROUTE_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "LOCATION_ID", referencedColumnName = "ID"))
	@Expose
	private List<Location> stops;

	private int numberOfStops;

	private double firstLong;

	private double firstLat;

	@ManyToOne
	@JoinColumn(name = "OWNER_ID")
	@Expose
	private Users owner;

	// getter
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}

	public List<String> getImages() {
		return images;
	}

	public List<byte[]> getPictures() {
		return pictures;
	}

	public List<Location> getStops() {
		return stops;
	}

	public int getNumberOfStops() {
		return numberOfStops;
	}

	public double getFirstLong() {
		return firstLong;
	}

	public double getFirstLat() {
		return firstLat;
	}

	public Location getStop(int index) {
		return stops.get(index);
	}

	public Users getOwner() {
		return owner;
	}

	public Time getTime() {
		return time;
	}

	public String getTimeString() {
		return timeString;
	}

	public List<Feedback> getFeedback() {
		return feedback;
	}

	public double getAvgRating() {
		return avgRating;
	}

	// setter
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) throws Exception {
		if (type.equals("Kultur") || type.equals("Party")) {
			this.type = type;
		} else {
			throw new Exception("You can only add Routes of type \"Kultur\" or \"Party\"");
		}
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setStops(List<Location> stops) {
		this.stops = stops;
	}

	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}

	public void setNumberOfStops(int numberOfStops) {
		this.numberOfStops = numberOfStops;
	}

	public void setFirstLong(double firstLong) {
		this.firstLong = firstLong;
	}

	public void setFirstLat(double firstLat) {
		this.firstLat = firstLat;
	}

	public void setOwner(Users owner) {
		this.owner = owner;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public void setPictures(List<byte[]> pictures) {
		this.pictures = pictures;
	}

	public void setFeedback(List<Feedback> feedback) {
		this.feedback = feedback;
	}

	public void setAvgRating(double avgRating) {
		this.avgRating = avgRating;
	}

	// other methods
	public void addStop(Location location) {
		this.stops.add(location);
	}

	public void removeStop(Location location) {
		this.stops.remove(location);
	}

	public void removeStopAtIndex(int index) {
		this.stops.remove(index);
	}

	public void addFeedback(Feedback feedback) {
		this.feedback.add(feedback);
	}

	public void removeFeedback(Feedback feedback) {
		this.feedback.remove(feedback);
	}

	public void removeFeedbackAtIndex(int index) {
		this.feedback.remove(index);
	}

	public void addImage(String image) {
		this.images.add(image);
	}

	public void removeImage(String image) {
		this.images.remove(image);
	}

	public void removeImageAtIndex(int index) {
		this.images.remove(index);
	}

	@Override
	public String toString() {
		String routeString = "ROUTE= "
						+ "Id: " + this.id + ", "
						+ "Name: " + this.name + ", "
						+ "Type: " + this.type + ", "
						+ "Time: " + this.time.toString() + ", "
						+ "Description: " + this.description + ", "
						+ "|| Feedback und Location spar ich mir";

		return routeString;
	}

}
