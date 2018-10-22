package model;

public class Feedback {
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

	public void setRating(int rating) {
		this.rating = rating;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

}
