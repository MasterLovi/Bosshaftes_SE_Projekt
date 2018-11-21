package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * This class represents the Feedback database table as mapped Java object
 */
@Entity
@Table(name = "FEEDBACK")
public class Feedback {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	private String comment;
	private int rating;

	@OneToOne
	@JoinColumn(name = "AUTHOR_ID")
	private Users author;

	// getter
	public int getId() {
		return id;
	}

	public String getComment() {
		return comment;
	}

	public int getRating() {
		return rating;
	}

	public Users getAuthor() {
		return author;
	}

	// setter
	public void setId(int id) {
		this.id = id;
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

	public void setAuthor(Users author) {
		this.author = author;
	}

	// other methods
	@Override
	public String toString() {
		String feedbackString = "FEEDBACK= "
						+ "Id: " + this.id + ", "
						+ "Comment: " + this.comment + ", "
						+ "Rating: " + this.rating + ", "
						+ "Author (Username): " + this.author.getUsername() + ", ";

		return feedbackString;
	}

}
