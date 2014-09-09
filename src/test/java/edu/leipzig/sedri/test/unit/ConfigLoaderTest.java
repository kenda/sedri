package edu.leipzig.sedri.test.unit;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;

import junit.framework.TestCase;
import edu.leipzig.sedri.ConfigLoader;
import edu.leipzig.sedri.Server;

public class ConfigLoaderTest
	extends TestCase
{	
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ConfigLoaderTest( String testName )
    {
        super( testName );
    }
    
    /**
     * Test ConfigLoader with correct xml file
     * @throws URISyntaxException 
     */
	public void testLoadCorrectXML() throws URISyntaxException {
		URL resourceUrl = getClass(). getResource("../testConfig1.xml");
    	assertNotNull("Test file missing", resourceUrl);
		File testConfig = Paths.get(resourceUrl.toURI()).toFile();
		
		ConfigLoader configLoader = new ConfigLoader(testConfig, "xml");
		ArrayList<Server> servers = configLoader.load();
		assertTrue("Servers shouldn't be empty!", 0 != servers.size());
	}
	
	/**
     * Test ConfigLoader with correct rdf file
     * @throws URISyntaxException 
     */
	public void testLoadCorrectRDF() throws URISyntaxException {
		URL resourceUrl = getClass(). getResource("../testconfig-ontologie.ttl");
    	assertNotNull("Test file missing", resourceUrl);
		File testConfig = Paths.get(resourceUrl.toURI()).toFile();
		
		ConfigLoader configLoader = new ConfigLoader(testConfig, "rdf");
		ArrayList<Server> servers = configLoader.load();
		assertTrue("Servers shouldn't be empty!",  0 != servers.size());
	}
	
	/**
     * Test ConfigLoader with wrong file type
     * @throws URISyntaxException 
     */
	public void testLoadWrongFiletype() throws URISyntaxException {
		URL resourceUrl = getClass(). getResource("../testConfig1.xml");
    	assertNotNull("Test file missing", resourceUrl);
		File testConfig = Paths.get(resourceUrl.toURI()).toFile();
		
		ConfigLoader configLoader = new ConfigLoader(testConfig, "r12f");
		ArrayList<Server> servers = configLoader.load();
		assertTrue("Servers should be empty!", 0 == servers.size());
	}
	
	/**
     * Test ConfigLoader with wrong file
     * @throws URISyntaxException 
     */
	public void testLoadWrongFile() {
		
		ConfigLoader configLoader = new ConfigLoader(new File("t1e2s3tConfig.xml"), "xml");
		ArrayList<Server> servers = configLoader.load();
		assertTrue("Server should be null!", 0 == servers.size());
	}

}
