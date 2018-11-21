package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.servlets.TestLocationServlet;
import test.servlets.TestRouteServlet;

@RunWith(Suite.class)
@SuiteClasses({TestRouteServlet.class, TestLocationServlet.class})
public class AllTests {

}
