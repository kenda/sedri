package edu.leipzig.sedri.test.unit;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class UnitTests	
{
    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    	TestSuite suite = new TestSuite();
    	suite.addTestSuite(QueryProcessorTest.class);
        return suite;
    }
}
