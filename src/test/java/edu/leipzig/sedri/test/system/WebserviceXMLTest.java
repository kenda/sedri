package edu.leipzig.sedri.test.system;

/**
 * Unit test for simple App.
 */
public class WebserviceXMLTest 
    extends WebserviceTest
{
    /**
     * Create the test case
     */
    public WebserviceXMLTest()
    {
        super.fileType = "xml";
        super.configFiles.add("../testConfig1.xml");
        super.configFiles.add("../testConfig5.xml");
    }
}
