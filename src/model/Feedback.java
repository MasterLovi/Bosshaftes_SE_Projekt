package model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Feedback {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	private int id;
	private Route route;
	private Location location;
	private String comment;
	private int rating;
	private User author;

	// getter
	public int getId() {
		return id;
	}

	public Route getRoute() {
		return route;
	}

	public Location getLocation() {
		return location;
	}

	public String getComment() {
		return comment;
	}

	public int getRating() {
		return rating;
	}

	public User getAuthor() {
		return author;
	}

	// setter
	public void setRoute(Route route) {
		this.route = route;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setRating(int rating) throws Exception {
		if (rating > 0 && rating < 6) {
			this.rating = rating;
		} else {
			throw new Exception("Rating can only be in the range 1..5");
		}
	}

	public void setAuthor(User author) {
		this.author = author;
	}

}
