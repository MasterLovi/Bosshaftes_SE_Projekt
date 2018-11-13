package util;

public class Time {

	
	private String time;
	
	private Integer hours;
	
	private Integer minutes;
	
	private Integer seconds;
	
	public Time(String time) {
		this.time = time;
		this.hours = Integer.parseInt(time.substring(0, time.indexOf(":")));
		
		String minutes = time.substring(time.indexOf(":")+1);
		this.minutes = Integer.parseInt(minutes.substring(0, time.indexOf(":")));
		
		this.seconds = Integer.parseInt(minutes.substring(time.indexOf(":") + 1));
	}
	
	public Time(Integer h, Integer m, Integer s) {
		
		//if seconds are > 60 --> change 60 seconds to 1 minute
		if (s > 60) {
			m = m + (s/60);
			s = s%60;
		}
		//if minutes are > 60 --> change 60 minutes to 1 hour
		if (m > 60) {
			h = h + (m/60);
			m = m%60;
		}
		
		this.hours = h;
		this.minutes = m;
		this.seconds = s;
		this.time = "" + h + ":" + m + ":" + s;
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
