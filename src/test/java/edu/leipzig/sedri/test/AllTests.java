package edu.leipzig.sedri.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import edu.leipzig.sedri.test.unit.UnitTests;
import edu.leipzig.sedri.test.integration.IntegrationTests;
import edu.leipzig.sedri.test.system.SystemTests;

/**
 * All tests for this Application.
 */
public class AllTests
{
    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    	TestSuite suite = new TestSuite();
    	suite.addTest(UnitTests.suite());
    	suite.addTest(IntegrationTests.suite());
    	suite.addTest(SystemTests.suite());
        return suite;
    }
}
