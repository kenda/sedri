package edu.leipzig.sedri.test.system;

import junit.framework.TestCase;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.lang.String;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import edu.leipzig.sedri.Server;
import edu.leipzig.sedri.Webservice;
import edu.leipzig.sedri.test.integration.ServerTest;

import com.hp.hpl.jena.rdf.model.*;

/**
 * Unit test for simple App.
 */
public class WebserviceTest 
    extends TestCase
{
	private JAXBContext 					 jc;
	private	 Unmarshaller 					 unmarshaller;
	private Server 							 server1;
	private org.eclipse.jetty.server.Server webserver;
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public WebserviceTest( String testName )
    {
        super( testName );
    }

    protected void setUp() throws Exception {
    	
    	/*
    	 * Setup Webserver
    	 */
    	// read the configuration file 
    	jc = JAXBContext.newInstance("edu.leipzig.sedri");
    	unmarshaller = jc.createUnmarshaller();
    	final URL configFile1 = ServerTest.class.getResource("../testConfig1.xml");
    	server1 = (Server) unmarshaller.unmarshal(new File(configFile1.toURI()));

    	// start webserver
    	BigInteger port = server1.getPort();
    	webserver = new org.eclipse.jetty.server.Server(port.intValue());
    	webserver.setHandler(new Webservice(server1));
    	
    	webserver.start();
    	//webserver.join();
    }
    
    /**
     * Test Webservice for correct model size
     */
    public void testWebServiceCorrectModelSize() throws Exception
    {
    	Model model = ModelFactory.createDefaultModel();
    	model.getReader().read(model, "http://localhost:1234/test?class=Drug");
    	assertEquals("Return Model has wrong size!", 15, model.size());
    }
    
    /**
     * Test Webservice for correct resource exists
     */
    public void testWebServiceCorrectResourceExist() throws Exception
    {
    	Model model = ModelFactory.createDefaultModel();
    	model.getReader().read(model, "http://localhost:1234/test?class=Drug");
    	    	
    	Resource resource = ResourceFactory.createResource("http://dbpedia.org/resource/%CE%91-Methylfentanyl");
    	assertTrue("Required Resource not found!", model.containsResource(resource));
    }
    
    /**
     * Test Webservice for resource not exists
     */
    public void testWebServiceResourceNotExist() throws Exception
    {
    	Model model = ModelFactory.createDefaultModel();
    	model.getReader().read(model, "http://localhost:1234/test?class=FooBar");
    	    	
    	Resource resource = ResourceFactory.createResource("http://dbpedia.org/resource/%CE%91-Methylfentanyl");
    	assertTrue("Resource should not be found!", !model.containsResource(resource));
    }
    
    /**
     * Test Webservice for correct PreProcessor
     */
    public void testWebServiceCorrectPreProcessor() throws Exception
    {
    	Model model = ModelFactory.createDefaultModel();
    	model.getReader().read(model, "http://localhost:1234/test");
    	    	
    	Resource resource = ResourceFactory.createResource("http://dbpedia.org/resource/%CE%91-Methylfentanyl");
    	assertTrue("Required Resource not found!", model.containsResource(resource));
    }
    
    /**
     * Test Webservice for correct PostProcessor
     */
    public void testWebServiceCorrectPostProcessor() throws Exception
    {
    	Model model = ModelFactory.createDefaultModel();
    	model.getReader().read(model, "http://localhost:1234/test?class=Drug");
    	    	
    	Resource resource = ResourceFactory.createResource("http://examples.org/TestResource1");
    	Property property = ResourceFactory.createProperty("http://examples.org/TestProperty1");
    	assertTrue("Required Statement not found!", model.contains(resource, property, "TestObject1"));
    }
    
    
    protected void tearDown() throws Exception {
    	webserver.stop();
    }
    
}
