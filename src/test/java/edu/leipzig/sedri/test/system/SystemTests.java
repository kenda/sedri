package edu.leipzig.sedri.test.system;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Unit test for simple App.
 */
@RunWith(Suite.class)
@SuiteClasses({
        WebserviceXMLTest.class,
        WebserviceRDFTest.class
})
public class SystemTests {

}
