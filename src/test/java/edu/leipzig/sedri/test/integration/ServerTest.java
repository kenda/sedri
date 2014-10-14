package edu.leipzig.sedri.test.integration;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.File;

import edu.leipzig.sedri.*;

import java.net.URL;

/**
 * Unit test for simple App.
 */
public abstract class ServerTest {
	protected String			fileType;
	protected List<String>		configFiles;
	protected String			wrongConfigFile;
	
	private List<Server>		servers;
	private List<Endpoint> 		endpoints;
	
    /**
     * Create the test case
     */
    public ServerTest()
    {
    	configFiles = new ArrayList<String>();
    	servers = new ArrayList<Server>();
    	endpoints = new ArrayList<Endpoint>();
    }

    @Before
    public void setUp() throws Exception
    {
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
    		endpoints.addAll(serversIt.next().getEndpoint());
    	}
    }
    
    /**
     * Server tests
     */
    
    /**
     * Test correct port of the server
     */
    @Test
    public void testCorrectPort()
    {
    	assertEquals("Wrong port number!", 1234, servers.get(0).getPort().intValue());
    }
    
    /**
     * Test wrong port of the server
     */
    @Test
    public void testWrongPort()
    {
    	assertTrue ("Wrong port number!", 8080 != servers.get(0).getPort().intValue());
    }
    
    /**
     * Test correct count of endpoints of the server
     */
    @Test
    public void testCorrectCountOfEndpoints()
    {
    	assertEquals ("Wrong count of endpoints!", 1, servers.get(0).getEndpoint().size());
    	assertEquals ("Wrong count of endpoints!", 2, servers.get(1).getEndpoint().size());
    	assertEquals ("Wrong count of endpoints!", 2, servers.get(2).getEndpoint().size());
    }
    
    /**
     * Test wrong count of endpoints of the server
     */
    @Test
    public void testWrongCountOfEndpoints()
    {
    	assertTrue ("Wrong count of endpoints!", 0 != servers.get(0).getEndpoint().size());
    	assertTrue ("Wrong count of endpoints!", 0 != servers.get(1).getEndpoint().size());
    	assertTrue ("Wrong count of endpoints!", 0 != servers.get(2).getEndpoint().size());
    }
    
    /**
     * Test load wrong config
     */
    @Test
    public void testLoadWrongConfig()
    {
    	try {
	    	final URL configFile4 = ServerTest.class.getResource(wrongConfigFile);
	    	ConfigLoader configLoader4 = new ConfigLoader(new File(configFile4.toURI()), fileType);
	    	ArrayList<Server> servers = configLoader4.load();
			assertEquals("No server should be found", 0, servers.size());
    	} catch (Exception e) {
    		assertTrue("Exception was thrown: " + e, false);
    	}
    }
    
    /**
     * Endpoint tests
     */
    
    /**
     * Test correct endpoint count
     */
    @Test
    public void testCorrectEndpointCount()
    {
    	assertEquals ("Wrong entpoint count!", 5, endpoints.size());
    }
    
    /**
     * Test correct endpoint url
     */
    @Test
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
    @Test
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
    @Test
    public void testCorrectParams()
    {
    	assertEquals ("Wrong params!", "class", endpoints.get(0).getParams().getParam().get(0).toString());
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
    @Test
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
    @Test
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
    @Test
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
    
    /**
     * Sources tests
     */
    
    /**
     * Test correct Sources count
     */
    @Test
    public void testCorrectSourcesCount()
    {
    	assertEquals ("Wrong sources count!", 2, endpoints.get(0).getSources().getSource().size());
    	assertEquals ("Wrong sources count!", 1, endpoints.get(1).getSources().getSource().size());
    	assertEquals ("Wrong sources count!", 0, endpoints.get(2).getSources().getSource().size());
    	assertEquals ("Wrong sources count!", null, endpoints.get(3).getSources());
    	assertEquals ("Wrong sources count!", 2, endpoints.get(4).getSources().getSource().size());
    }
    
    /**
     * Test correct Sources urls
     */
    @Test
    public void testCorrectSourcesUrls()
    {
    	assertEquals ("Wrong sources url!", "http://dbpedia.org/sparql", endpoints.get(0).getSources().getSource().get(0).getUrl());
    	assertEquals ("Wrong sources url!", "http://drugbank.bio2rdf.org/sparql", endpoints.get(0).getSources().getSource().get(1).getUrl());
    	assertEquals ("Wrong sources url!", "http://drugbank.bio2rdf.org/sparql", endpoints.get(1).getSources().getSource().get(0).getUrl());
    	assertEquals ("Wrong sources url!", null, endpoints.get(4).getSources().getSource().get(0).getUrl());
    	assertEquals ("Wrong sources url!", null, endpoints.get(4).getSources().getSource().get(1).getUrl());
    }
    
    /**
     * Test correct Sources queries
     */
    @Test
    public void testCorrectSourcesQueries()
    {
    	assertEquals ("Wrong sources query!", "select ?s where {?s a <http://dbpedia.org/ontology/$class>. FILTER regex(?s, \"Methylfentanyl\")} order by ?s limit 1", endpoints.get(0).getSources().getSource().get(0).getQuery());
    	assertEquals ("Wrong sources query!", "select ?s where {?s a <http://bio2rdf.org/drugbank_vocabulary:Drug>} order by ?s limit 1", endpoints.get(0).getSources().getSource().get(1).getQuery());
    	assertEquals ("Wrong sources query!", null, endpoints.get(1).getSources().getSource().get(0).getQuery());
    	assertEquals ("Wrong sources query!", "select ?s where {?s a <http://bio2rdf.org/drugbank_vocabulary:Drug>} limit $testLimit", endpoints.get(4).getSources().getSource().get(1).getQuery());
    }
    
    /**
     * Test correct Format
     */
    @Test
    public void testCorrectFormat()
    {
    	assertEquals ("Wrong format!", "RDF/XML", endpoints.get(0).getFormat());
    	assertEquals ("Wrong format!", "JSON-LD", endpoints.get(1).getFormat());
    	assertEquals ("Wrong format!", "", endpoints.get(2).getFormat());
    	assertEquals ("Wrong format!", "N-TRIPLE", endpoints.get(3).getFormat());
    	assertEquals ("Wrong format!", null, endpoints.get(4).getFormat());
    }
}
