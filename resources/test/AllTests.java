package test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.model.TestJPAClasses;
import test.servlets.TestLocationServlet;
import test.servlets.TestRegistrationServlet;
import test.servlets.TestRouteServlet;

@RunWith(Suite.class)
@SuiteClasses({
	TestJPAClasses.class, 
	TestRegistrationServlet.class, 
	TestLocationServlet.class, 
	})
public class AllTests {

}
