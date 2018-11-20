package test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.servlets.TestLocationServlet;
import test.servlets.TestRegistrationServlet;
import test.servlets.TestRouteServlet;

@RunWith(Suite.class)
@SuiteClasses({TestRouteServlet.class, TestLocationServlet.class, TestRegistrationServlet.class})
public class AllTests {

}
