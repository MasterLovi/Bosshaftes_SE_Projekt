package test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * 
 * Test Runner Class in addition to the Test Suite for running the Test Classes.
 *
 */
public class TestRunner {
	public static void main(String[] args) {
		
		//Start Unit Tests
		Result result = JUnitCore.runClasses(AllTests.class);
		
		//Print Failures to console
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		
		//Print Success to console
		System.out.println(result.wasSuccessful());
	}
}
