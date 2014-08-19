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

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.hp.hpl.jena.rdf.model.*;

/**
 * Unit test for simple App.
 */
public class WebserviceTest 
    extends TestCase
{
	private JAXBContext 					 jc;
	private	 Unmarshaller 					 unmarshaller;
	private Server 							 server1, server2;
	private org.eclipse.jetty.server.Server webserver1, webserver2;
	private WebClient						 webClient;
	
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
    	final URL configFile5 = ServerTest.class.getResource("../testConfig5.xml");
    	server2 = (Server) unmarshaller.unmarshal(new File(configFile5.toURI()));

    	// start webserver1
    	BigInteger port1 = server1.getPort();
    	webserver1 = new org.eclipse.jetty.server.Server(port1.intValue());
    	webserver1.setHandler(new Webservice(server1));
    	webserver1.start();
    	
    	// start webserver2
    	BigInteger port2 = server2.getPort();
    	webserver2 = new org.eclipse.jetty.server.Server(port2.intValue());
    	webserver2.setHandler(new Webservice(server2));
    	webserver2.start();
    	
    	//webserver1.join();
    	//webserver2.join();
    	
    	webClient = new WebClient();
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
     * Test Webservice for wrong PreProcessor
     */
    public void testWebServiceWrongPreProcessor() throws Exception
    {
    	HtmlPage currentPage = webClient.getPage("http://localhost:9876/test1?class=Drug");
    	assertEquals("Preprocessor shouldn't be found!", "Failed loading Preprocessor!", currentPage.asText());
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
    
    /**
     * Test Webservice for wrong PostProcessor
     */
    public void testWebServiceWrongPostProcessor() throws Exception
    {
    	HtmlPage currentPage = webClient.getPage("http://localhost:9876/test2?class=Drug");
    	assertEquals("Postprocessor shouldn't be found!", "Failed loading Postprocessor!", currentPage.asText());
    }
    
    /**
     * Test Webservice for wrong Param
     */
    public void testWebServiceWrongParam() throws Exception
    {
    	HtmlPage currentPage = webClient.getPage("http://localhost:9876/test3?wrongclass=Drug");
    	assertEquals("Parameter shouldn't be found!", "Parameter is missing!", currentPage.asText());
    }
    
    /**
     * Test Webservice for incompatible query types
     */
    public void testWebServiceIncompatibleQueryTypes() throws Exception
    {
    	HtmlPage currentPage = webClient.getPage("http://localhost:9876/test4?class=Drug");
    	assertEquals("Queries should be incompatible!", "Incompatible query types!", currentPage.asText());
    }
    
    //TODO: Write more tests, for example test the concatenation of two sources 
    
    protected void tearDown() throws Exception {
    	webserver1.stop();
    	webserver2.stop();
    }
    
}
