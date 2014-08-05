package edu.leipzig.sedri.test.integration;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import edu.leipzig.sedri.*;

import java.net.URL;

/**
 * Unit test for simple App.
 */
public class ServerTest 
    extends TestCase
{
	private JAXBContext 	jc;
	private	 Unmarshaller 	unmarshaller;
	private Server 			server1;
	private Server 			server2;
	private Server 			server3;
	private List<Endpoint> endpoints;
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ServerTest( String testName )
    {
        super( testName );
    }

    protected void setUp() throws Exception
    {
    	// read the configuration file
    	jc = JAXBContext.newInstance("edu.leipzig.sedri");
    	unmarshaller = jc.createUnmarshaller();
    	final URL configFile1 = ServerTest.class.getResource("../testConfig1.xml");
    	final URL configFile2 = ServerTest.class.getResource("../testConfig2.xml");
    	final URL configFile3 = ServerTest.class.getResource("../testConfig3.xml");
    	server1 = (Server) unmarshaller.unmarshal(new File(configFile1.toURI()));
    	server2 = (Server) unmarshaller.unmarshal(new File(configFile2.toURI()));
    	server3 = (Server) unmarshaller.unmarshal(new File(configFile3.toURI()));
    	endpoints = new ArrayList<Endpoint>();
    	endpoints.addAll(server1.getEndpoint());
    	endpoints.addAll(server2.getEndpoint());
    	endpoints.addAll(server3.getEndpoint());
    }
    
    /**
     * Server tests
     */
    
    /**
     * Test correct port of the server
     */
    public void testCorrectPort()
    {
    	assertEquals("Wrong port number!", 1234, server1.getPort().intValue());
    }
    
    /**
     * Test wrong port of the server
     */
    public void testWrongPort()
    {
    	assertTrue ("Wrong port number!", 8080 != server1.getPort().intValue());
    }
    
    /**
     * Test correct count of endpoints of the server
     */
    public void testCorrectCountOfEndpoints()
    {
    	assertEquals ("Wrong count of endpoints!", 1, server1.getEndpoint().size());
    	assertEquals ("Wrong count of endpoints!", 2, server2.getEndpoint().size());
    	assertEquals ("Wrong count of endpoints!", 2, server3.getEndpoint().size());
    }
    
    /**
     * Test wrong count of endpoints of the server
     */
    public void testWrongCountOfEndpoints()
    {
    	assertTrue ("Wrong count of endpoints!", 0 != server1.getEndpoint().size());
    	assertTrue ("Wrong count of endpoints!", 0 != server2.getEndpoint().size());
    	assertTrue ("Wrong count of endpoints!", 0 != server3.getEndpoint().size());
    }
    
    /**
     * Test load wrong config
     */
    public void testLoadWrongConfig()
    {
    	final URL configFile4 = ServerTest.class.getResource("../testConfig4.xml");
    	try {
    		unmarshaller.unmarshal(new File(configFile4.toURI()));
    		assertTrue("No Exception was thrown", false);
    	} catch (Exception e) {
    		assertTrue(true);
    	}
    }
    
    /**
     * Endpoint tests
     */
    
    /**
     * Test correct endpoint count
     */
    public void testCorrectEndpointCount()
    {
    	assertEquals ("Wrong entpoint count!", 5, endpoints.size());
    }
    
    /**
     * Test correct endpoint url
     */
    public void testCorrectEndpointURL()
    {
    	assertEquals ("Wrong entpoint url!", "/test", endpoints.get(0).getUrl());
    	assertEquals ("Wrong entpoint url!", "/test1", endpoints.get(1).getUrl());
    	assertEquals ("Wrong entpoint url!", "/test2/test3", endpoints.get(2).getUrl());
    	assertEquals ("Wrong entpoint url!", "", endpoints.get(3).getUrl());
    	assertEquals ("Wrong entpoint url!", null, endpoints.get(4).getUrl());
    }
    
    /**
     * Params tests
     */
    
    /**
     * Test correct params count
     */
    public void testCorrectParamsCount()
    {
    	assertEquals ("Wrong params count!", 1, endpoints.get(0).getParams().getParam().size());
    	assertEquals ("Wrong params count!", null, endpoints.get(1).getParams());
    	assertEquals ("Wrong params count!", 2, endpoints.get(2).getParams().getParam().size());
    	assertEquals ("Wrong params count!", 1, endpoints.get(3).getParams().getParam().size());
    	assertEquals ("Wrong params count!", 2, endpoints.get(4).getParams().getParam().size());
    }
    
    /**
     * Test correct params
     */
    public void testCorrectParams()
    {
    	assertEquals ("Wrong params!", "testLimit", endpoints.get(0).getParams().getParam().get(0).toString());
    	assertEquals ("Wrong params!", "testLimit", endpoints.get(2).getParams().getParam().get(0).toString());
    	assertEquals ("Wrong params!", "testLimit", endpoints.get(2).getParams().getParam().get(1).toString());
    	assertEquals ("Wrong params!", "", endpoints.get(3).getParams().getParam().get(0).toString());
    	assertEquals ("Wrong params!", "testLimit", endpoints.get(4).getParams().getParam().get(0).toString());
    	assertEquals ("Wrong params!", "testOrder", endpoints.get(4).getParams().getParam().get(1).toString());
    }
    
    /**
     * Processor tests
     */
    
    /**
     * Test correct PreProcessor count
     */
    public void testCorrectPreProcessorCount()
    {
    	assertEquals ("Wrong preprocessor count!", 1, endpoints.get(0).getPreprocessors().getPreprocessor().size());
    	assertEquals ("Wrong preprocessor count!", 1, endpoints.get(1).getPreprocessors().getPreprocessor().size());
    	assertEquals ("Wrong preprocessor count!", null, endpoints.get(2).getPreprocessors());
    	assertEquals ("Wrong preprocessor count!", 1, endpoints.get(3).getPreprocessors().getPreprocessor().size());
    	assertEquals ("Wrong preprocessor count!", 2, endpoints.get(4).getPreprocessors().getPreprocessor().size());
    }
    
    /**
     * Test correct PostProcessor count
     */
    public void testCorrectPostProcessorCount()
    {
    	assertEquals ("Wrong postprocessor count!", 1, endpoints.get(0).getPostprocessors().getPostprocessor().size());
    	assertEquals ("Wrong postprocessor count!", null, endpoints.get(1).getPostprocessors());
    	assertEquals ("Wrong postprocessor count!", 1, endpoints.get(2).getPostprocessors().getPostprocessor().size());
    	assertEquals ("Wrong postprocessor count!", 0, endpoints.get(3).getPostprocessors().getPostprocessor().size());
    	assertEquals ("Wrong postprocessor count!", 2, endpoints.get(4).getPostprocessors().getPostprocessor().size());
    }
    
    /**
     * Test correct PreProcessor
     */
    public void testCorrectPreProcessor()
    {
    	assertEquals ("Wrong preprocessor!", "edu.leipzig.sedri.test.TestPreprocessor", endpoints.get(0).getPreprocessors().getPreprocessor().get(0).toString());
    	assertEquals ("Wrong preprocessor!", "edu.leipzig.sedri.test.TestPreprocessor", endpoints.get(1).getPreprocessors().getPreprocessor().get(0).toString());
    	assertEquals ("Wrong preprocessor!", "", endpoints.get(3).getPreprocessors().getPreprocessor().get(0).toString());
    	assertEquals ("Wrong preprocessor!", "edu.leipzig.sedri.test.TestPreprocessor", endpoints.get(4).getPreprocessors().getPreprocessor().get(0).toString());
    	assertEquals ("Wrong preprocessor!", "edu.leipzig.sedri.test.WrongTestPreprocessor", endpoints.get(4).getPreprocessors().getPreprocessor().get(1).toString());
    }
    
    /**
     * Test correct PostProcessor
     */
    public void testCorrectPostProcessor()
    {
    	assertEquals ("Wrong postprocessor!", "edu.leipzig.sedri.test.TestPostprocessor", endpoints.get(0).getPostprocessors().getPostprocessor().get(0).toString());
    	assertEquals ("Wrong postprocessor!", "edu.leipzig.sedri.test.TestPostprocessor", endpoints.get(2).getPostprocessors().getPostprocessor().get(0).toString());
    	assertEquals ("Wrong postprocessor!", "edu.leipzig.sedri.test.TestPostprocessor", endpoints.get(4).getPostprocessors().getPostprocessor().get(0).toString());
    	assertEquals ("Wrong postprocessor!", "edu.leipzig.sedri.test.WrongTestPostprocessor", endpoints.get(4).getPostprocessors().getPostprocessor().get(1).toString());
    }
}
