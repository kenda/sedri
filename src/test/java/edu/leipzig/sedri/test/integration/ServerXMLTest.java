package edu.leipzig.sedri.test.integration;

/**
 * Unit test for simple App.
 */
public class ServerXMLTest 
    extends ServerTest
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ServerXMLTest( String testName )
    {
        super( testName );
    	super.fileType = "xml";
    	super.configFiles.add("../testConfig1.xml");
    	super.configFiles.add("../testConfig2.xml");
    	super.configFiles.add("../testConfig3.xml");
    	super.wrongConfigFile = "../testConfig4.xml";
    }
}
