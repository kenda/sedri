package edu.leipzig.sedri.test.unit;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Unit test for simple App.
 */
@RunWith(Suite.class)
@SuiteClasses({
        ConfigLoaderTest.class,
        QueryProcessorTest.class
})
public class UnitTests {
}
