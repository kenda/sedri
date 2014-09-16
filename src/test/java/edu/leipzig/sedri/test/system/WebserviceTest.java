package edu.leipzig.sedri.test.system;

import junit.framework.TestCase;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.lang.String;

import edu.leipzig.sedri.ConfigLoader;
import edu.leipzig.sedri.Server;
import edu.leipzig.sedri.Webservice;
import edu.leipzig.sedri.test.integration.ServerTest;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.hp.hpl.jena.rdf.model.*;

/**
 * Unit test for simple App.
 */
public abstract class WebserviceTest 
    extends TestCase
{
	protected String								fileType;
	protected List<String>							configFiles;
	
	private List<Server>							servers;
	private List<org.eclipse.jetty.server.Server>	webservers;
	private WebClient						 		webClient;
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public WebserviceTest( String testName )
    {
        super( testName );
    	configFiles = new ArrayList<String>();
    	servers = new ArrayList<Server>();
    	webservers = new ArrayList<org.eclipse.jetty.server.Server>();
    }

    protected void setUp() throws Exception {
    	
    	/*
    	 * Setup Webserver
    	 */
    	// read the configuration file
    	Iterator<String> configFilesIt = configFiles.iterator();
    	
    	while (configFilesIt.hasNext()) {
    		File configFile = new File(ServerTest.class.getResource(configFilesIt.next()).toURI());
    		ConfigLoader configLoader = new ConfigLoader(configFile, fileType);
    		ArrayList<Server> serversFromConfig = configLoader.load();
    		if (0 == serversFromConfig.size()) {
        		System.err.println("No webservice configuration could be loaded!");
        		return;
    		}
    		servers.addAll(serversFromConfig);
    	}
    	
    	Iterator<Server> serversIt = servers.iterator();
    	
    	while (serversIt.hasNext()) {
        	// start webserver1
    		Server server = serversIt.next();
        	BigInteger port = server.getPort();
        	org.eclipse.jetty.server.Server webserver = new org.eclipse.jetty.server.Server(port.intValue());
        	webserver.setHandler(new Webservice(server));
        	webserver.start();
        	webservers.add(webserver);
    	}
    	
    	//webservers.get(0).join();
    	//webservers.get(1).join();
    	
    	webClient = new WebClient();
    }
    
    protected void tearDown() throws Exception {
    	Iterator<org.eclipse.jetty.server.Server> webserversIt = webservers.iterator();
    	
    	while (webserversIt.hasNext()) {
    		webserversIt.next().stop();
    	}
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
    
    /**
     * Test Webservice with two correct DESCRIBE queries
     */
    public void testWebServiceTwoCorrectDescribeQueries() throws Exception
    {
    	Model model = ModelFactory.createDefaultModel();
    	model.getReader().read(model, "http://localhost:9876/test5?class1=Drug&class2=Drug");
    	    	
    	Resource resource1 = ResourceFactory.createResource("http://dbpedia.org/ontology/Drug");
    	assertTrue("Required Resource not found!", model.containsResource(resource1));
    	
    	Resource resource2 = ResourceFactory.createResource("http://bio2rdf.org/drugbank_vocabulary:Drug");
    	assertTrue("Required Resource not found!", model.containsResource(resource2));
    }
    
    /**
     * Test Webservice with one correct and one wrong DESCRIBE query
     */
    public void testWebServiceCorractAndWrongDescribeQuery() throws Exception
    {
    	Model model = ModelFactory.createDefaultModel();
    	model.getReader().read(model, "http://localhost:9876/test5?class1=Drug&class2=D1r2u3g");
    	    	
    	Resource resource1 = ResourceFactory.createResource("http://dbpedia.org/ontology/Drug");
    	assertTrue("Required Resource not found!", model.containsResource(resource1));
    	
    	Resource resource2 = ResourceFactory.createResource("http://bio2rdf.org/drugbank_vocabulary:Drug");
    	assertTrue("Resource should not be found!", !model.containsResource(resource2));
    }
    
    /**
     * Test Webservice with two wrong DESCRIBE queries
     */
    public void testWebServiceTwoWrongDescribeQueries() throws Exception
    {
    	Model model = ModelFactory.createDefaultModel();
    	model.getReader().read(model, "http://localhost:9876/test5?class1=D1r2u3g&class2=D1r2u3g");
    	    	
    	Resource resource1 = ResourceFactory.createResource("http://dbpedia.org/ontology/Drug");
    	assertTrue("Resource should not be found!", !model.containsResource(resource1));
    	
    	Resource resource2 = ResourceFactory.createResource("http://bio2rdf.org/drugbank_vocabulary:Drug");
    	assertTrue("Resource should not be found!", !model.containsResource(resource2));
    	
    	assertEquals("Return Model has wrong size!", 0, model.size());
    }
    
}
