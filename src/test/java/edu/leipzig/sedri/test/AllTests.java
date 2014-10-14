package edu.leipzig.sedri.test;

import edu.leipzig.sedri.test.unit.ConfigLoaderTest;
import edu.leipzig.sedri.test.unit.QueryProcessorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import edu.leipzig.sedri.test.unit.UnitTests;
import edu.leipzig.sedri.test.integration.IntegrationTests;
import edu.leipzig.sedri.test.system.SystemTests;

/**
 * All tests for this Application.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        UnitTests.class,
        IntegrationTests.class,
        SystemTests.class
})
public class AllTests {

}
