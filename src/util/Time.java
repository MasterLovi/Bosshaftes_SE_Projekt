package util;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * This class is an individual util class that represents duration.
 *
 */

public class Time implements Serializable {

	private static final long serialVersionUID = 1156215663793678712L;
	@Expose
	private String time;
	@Expose
	private Integer hours;
	@Expose
	private Integer minutes;
	@Expose
	private Integer seconds;

	/**
	 * Individual constructor to save a time object with a given String
	 * 
	 * @param time Time-String to be converted (format: "HH:MM:SS")
	 */
	public Time(String time) {
		try {
			if (time == null) {
				time = "00:00:00";
			}
			Integer hours = Integer.parseInt(time.substring(0, time.indexOf(":")));

			String sMinutes = time.substring(time.indexOf(":") + 1);
			
			Integer minutes = Integer.parseInt(sMinutes.substring(0, sMinutes.indexOf(":")));

			Integer seconds = Integer.parseInt(sMinutes.substring(sMinutes.indexOf(":") + 1));

			this.setTime(hours, minutes, seconds);
		} catch (Exception e) {
			this.time = "00:00:00";
			this.hours = 0;
			this.minutes = 0;
			this.seconds = 0;
		}
	}
	
	public Time() {
		this.time = "00:00:00";
		this.hours = 0;
		this.minutes = 0;
		this.seconds = 0;
	}

	/**
	 * Individual constructor to save time object with given hours, minutes and
	 * seconds
	 * 
	 * @param hours   Hours of the duration
	 * @param minutes Minutes of the duration
	 * @param seconds Seconds of the duration
	 */
	public Time(Integer hours, Integer minutes, Integer seconds) {
		this.setTime(hours, minutes, seconds);
	}

	private void setTime(Integer hours, Integer minutes, Integer seconds) {
		// if seconds are > 60 --> change 60 seconds to 1 minute
		if (seconds >= 60) {
			minutes = minutes + (seconds / 60);
			seconds = seconds % 60;
		}
		// if minutes are > 60 --> change 60 minutes to 1 hour
		if (minutes >= 60) {
			hours = hours + (minutes / 60);
			minutes = minutes % 60;
		}

		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.time = "";
		if (hours < 10) {
			this.time = this.time + 0;
		}
		this.time = this.time + hours + ":";
		if (minutes < 10) {
			this.time = this.time + 0;
		}
		this.time = this.time + minutes + ":";
		if (seconds < 10) {
			this.time = this.time + 0;
		}
		this.time = this.time + seconds;
	}

	public Integer getHours() {
		return hours;
	}

	public Integer getMinutes() {
		return minutes;
	}

	public Integer getSeconds() {
		return seconds;
	}

	public String getTime() {
		return time;
	}

	@Override
	public String toString() {
		return getTime();
	}

	public Boolean IsLessThan(Time t) {
		if (this.getHours() < t.getHours()) {
			return true;
		} else if (this.getHours() > t.getHours()) {
			return false;
		} else {

			if (this.getMinutes() < t.getMinutes()) {
				return true;
			} else if (this.getMinutes() > t.getMinutes()) {
				return false;
			} else {

				if (this.getSeconds() < t.getSeconds()) {
					return true;
				} else if (this.getSeconds() > t.getSeconds()) {
					return false;
				} else {
					return true;
				}

			}

		}

	}

}
