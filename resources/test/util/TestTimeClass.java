package test.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import util.Time;

/**
 * 
 * Test Class for Time Utility Class and its Methods
 *
 */
public class TestTimeClass {
	
	// Test Object
	public Time time;
	
	/**
	 * Test Time Methods
	 */
	@Test
	public void tTime() {
		
		// Test the different Constructors and the different cases
		time = new Time();
		assertEquals("00:00:00", time.getTime());
		assertTrue(time.getHours() == 0 && time.getMinutes() == 0 && time.getSeconds() == 0);
		
		time = new Time(null);
		assertEquals("00:00:00", time.getTime());
		assertTrue(time.getHours() == 0 && time.getMinutes() == 0 && time.getSeconds() == 0);
		
		time = new Time(0, 0, 0);
		assertEquals("00:00:00", time.getTime());
		assertTrue(time.getHours() == 0 && time.getMinutes() == 0 && time.getSeconds() == 0);
		
		time = new Time("test");
		assertEquals("00:00:00", time.getTime());
		assertTrue(time.getHours() == 0 && time.getMinutes() == 0 && time.getSeconds() == 0);
		
		time = new Time("00:60:61");
		assertEquals("01:01:01", time.getTime());
		assertTrue(time.getHours() == 1 && time.getMinutes() == 1 && time.getSeconds() == 1);
		
		// Test the compare Method --> time Object has time 01:01:01
		assertTrue(time.IsLessThan(new Time("02:00:00")));
		assertTrue(time.IsLessThan(new Time("01:02:00")));
		assertTrue(time.IsLessThan(new Time("01:01:02")));
		assertTrue(time.IsLessThan(new Time("01:01:01")));
		assertFalse(time.IsLessThan(new Time("00:00:00")));
		assertFalse(time.IsLessThan(new Time("01:00:00")));
		assertFalse(time.IsLessThan(new Time("01:01:00")));
		
	}
	
}
