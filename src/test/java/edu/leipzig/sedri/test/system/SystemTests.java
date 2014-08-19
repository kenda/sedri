package edu.leipzig.sedri.test.system;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class SystemTests	
{
    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    	TestSuite suite = new TestSuite();
    	suite.addTestSuite(WebserviceTest.class);
        return suite;
    }
}
