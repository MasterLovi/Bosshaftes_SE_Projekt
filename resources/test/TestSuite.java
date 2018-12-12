package test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.model.TestJPAClasses;
import test.servlets.TestFeedbackServlet;
import test.servlets.TestLocationServlet;
import test.servlets.TestRegistrationServlet;
import test.servlets.TestRouteServlet;
import test.servlets.TestUserSessionServlet;
import test.util.TestTimeClass;

/**
 * 
 * Test Suite to run all Test Classes at once and to build the code coverage.
 *
 */
@RunWith(Suite.class)
@SuiteClasses({
	TestJPAClasses.class,
	TestTimeClass.class,
	TestRegistrationServlet.class, 
	TestUserSessionServlet.class,
	TestLocationServlet.class,
	TestRouteServlet.class,
	TestFeedbackServlet.class
	})
public class TestSuite {

}
