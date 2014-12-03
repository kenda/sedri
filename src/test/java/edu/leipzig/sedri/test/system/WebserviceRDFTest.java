package edu.leipzig.sedri.test.system;

/**
 * Unit test for simple App.
 */
public class WebserviceRDFTest
    extends WebserviceTest
{
    /**
     * Create the test case
     */
    public WebserviceRDFTest()
    {
        super.fileType = "rdf";
        super.configFiles.add("../testconfig-ontologie1.ttl");
        super.configFiles.add("../testconfig-ontologie5.ttl");
    }
}
