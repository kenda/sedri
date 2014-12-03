package edu.leipzig.sedri.test.integration;

/**
 * Unit test for simple App.
 */
public class ServerRDFTest 
    extends ServerTest
{
    /**
     * Create the test case
     */
    public ServerRDFTest()
    {
    	super.fileType = "rdf";
    	super.configFiles.add("../testconfig-ontologie1.ttl");
    	super.configFiles.add("../testconfig-ontologie2.ttl");
    	super.wrongConfigFile = "../testconfig-ontologie3.ttl";
    }
}
