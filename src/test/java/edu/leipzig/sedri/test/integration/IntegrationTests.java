package edu.leipzig.sedri.test.integration;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class IntegrationTests	
{
    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    	TestSuite suite = new TestSuite();
    	suite.addTestSuite(ServerXMLTest.class);
        return suite;
    }
}
