package util;

import java.io.Serializable;

public class Time implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1156215663793678712L;

	private String time;

	private Integer hours;

	private Integer minutes;

	private Integer seconds;

	public Time(String time) {
		Integer hours = Integer.parseInt(time.substring(0, time.indexOf(":")));

		String sMinutes = time.substring(time.indexOf(":") + 1);
		Integer minutes = Integer.parseInt(sMinutes.substring(0, time.indexOf(":")));

		Integer seconds = Integer.parseInt(sMinutes.substring(time.indexOf(":") + 1));

		this.setTime(hours, minutes, seconds);
	}

	public Time(Integer hours, Integer minutes, Integer seconds) {
		this.setTime(hours, minutes, seconds);
	}

	private void setTime(Integer hours, Integer minutes, Integer seconds) {
		// if seconds are > 60 --> change 60 seconds to 1 minute
		if (seconds > 60) {
			minutes = minutes + (seconds / 60);
			seconds = seconds % 60;
		}
		// if minutes are > 60 --> change 60 minutes to 1 hour
		if (minutes > 60) {
			hours = hours + (minutes / 60);
			minutes = minutes % 60;
		}

		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.time = "" + hours + ":" + minutes + ":" + seconds;
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

}
