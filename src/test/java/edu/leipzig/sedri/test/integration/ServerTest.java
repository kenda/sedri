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
        boolean firstPort = false;
        boolean secondPort = false;
        boolean thirdPort = false;

        Iterator<Server> serversIt = servers.iterator();
        while (serversIt.hasNext()) {
            Server server = serversIt.next();
            if (1234 == server.getPort().intValue() && !firstPort)
                firstPort = true;
            if (5678 == server.getPort().intValue() && !secondPort)
                secondPort = true;
            if (9876 == server.getPort().intValue() && !thirdPort)
                thirdPort = true;
        }
        assertTrue("Wrong port number! Correct on is 1234", firstPort);
        assertTrue("Wrong port number! Correct on is 5678", secondPort);
        assertTrue("Wrong port number! Correct on is 9876", thirdPort);
    }
    
    /**
     * Test correct count of endpoints of the server
     */
    @Test
    public void testCorrectCountOfEndpoints()
    {
    	assertEquals ("Wrong count of endpoints!", 5, endpoints.size());
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
            if (e.getMessage().contains("[line: 17, col: 31] Unrecognized: [SEMICOLON]")) {
                assertTrue(true);
            } else {
                assertTrue("Exception was thrown: " + e, false);
            }
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
        boolean firstEndpoint = false;
        boolean secondEndpoint = false;
        boolean thirdEndpoint = false;
        boolean fourthEndpoint = false;
        boolean fifthEndpoint = false;

        Iterator<Endpoint> endpointIt = endpoints.iterator();
        while (endpointIt.hasNext()) {
            Endpoint endpoint = endpointIt.next();

            if (null == endpoint.getUrl() && !fifthEndpoint) {
                fifthEndpoint = true;
            } else if (null == endpoint.getUrl() && !fourthEndpoint) {
                fourthEndpoint = true;
            } else if (endpoint.getUrl().equals("/test2/test3") && !thirdEndpoint) {
                thirdEndpoint = true;
            } else if (endpoint.getUrl().equals("/test1") && !secondEndpoint) {
                secondEndpoint = true;
            } else if (endpoint.getUrl().equals("/test") && !firstEndpoint) {
                firstEndpoint = true;
            } else if (endpoint.getUrl().equals("") && !fourthEndpoint) {
                fourthEndpoint = true;
            }
        }
        assertTrue("Wrong entpoint url! Correct on is /test", firstEndpoint);
        assertTrue("Wrong entpoint url! Correct on is /test1", secondEndpoint);
        assertTrue("Wrong entpoint url! Correct on is /test2/test3", thirdEndpoint);
        assertTrue("Wrong entpoint url! Correct on is '' or null", fourthEndpoint);
        assertTrue("Wrong entpoint url! Correct on is null", fifthEndpoint);
    }
    
    /**
     * Params tests
     */
    
    /**
     * Test correct params count
     */
    @Test
    public void testCorrectParamCount()
    {
        int paramsCount = 0;
        Iterator<Endpoint> endpointIt = endpoints.iterator();
        while (endpointIt.hasNext()) {
            Endpoint endpoint = endpointIt.next();
            if (null != endpoint.getParams()) {
                paramsCount += endpoint.getParams().getParam().size();
            }
        }
    	assertTrue("Wrong count of params!", 5 == paramsCount || 6 == paramsCount);
    }

    /**
     * Test correct params
     */
    @Test
    public void testCorrectParams()
    {
        boolean firstParam = false;
        boolean secondParam = false;
        boolean thirdParam = false;
        boolean fourthParam = false;
        boolean fifthParam = false;
        boolean sixthParam = false;

        Iterator<Endpoint> endpointIt = endpoints.iterator();
        while (endpointIt.hasNext()) {
            Endpoint endpoint = endpointIt.next();
            if (null != endpoint.getParams()) {
                Iterator<String> paramsIt = endpoint.getParams().getParam().iterator();
                while (paramsIt.hasNext()) {
                    String param = paramsIt.next();
                    if (param.equals("testOrder") && !sixthParam) {
                        sixthParam = true;
                    } else if (param.equals("testLimit") && !fifthParam) {
                        fifthParam = true;
                    } else if (param.equals("testLimit") && !thirdParam) {
                        thirdParam = true;
                    } else if (param.equals("testLimit") && !secondParam) {
                        secondParam = true;
                    } else if (param.equals("class") && !firstParam) {
                        firstParam = true;
                    } else if (param.equals("") && !fourthParam) {
                        fourthParam = true;
                    }
                }
            } else if (!secondParam) {
                secondParam = true;
            }
        }
        assertTrue("Wrong param! Correct on is class", firstParam);
        assertTrue("Wrong param! Correct on is testLimit", secondParam);
        assertTrue("Wrong param! Correct on is testLimit", thirdParam);
        assertTrue("Wrong param! Correct on is '' or null", fourthParam);
        assertTrue("Wrong param! Correct on is testLimit", fifthParam);
        assertTrue("Wrong param! Correct on is testOrder", sixthParam);
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
        int preProcessorCount = 0;
        Iterator<Endpoint> endpointIt = endpoints.iterator();
        while (endpointIt.hasNext()) {
            Endpoint endpoint = endpointIt.next();
            if (null != endpoint.getPreprocessors()) {
                preProcessorCount += endpoint.getPreprocessors().getPreprocessor().size();
            }
        }
        assertEquals("Wrong count of PreProcessors!", 5, preProcessorCount);
    }
    
    /**
     * Test correct PostProcessor count
     */
    @Test
    public void testCorrectPostProcessorCount()
    {
        int postProcessorCount = 0;
        Iterator<Endpoint> endpointIt = endpoints.iterator();
        while (endpointIt.hasNext()) {
            Endpoint endpoint = endpointIt.next();
            if (null != endpoint.getPostprocessors()) {
                postProcessorCount += endpoint.getPostprocessors().getPostprocessor().size();
            }
        }
        assertEquals("Wrong count of PostProcessors!", 4, postProcessorCount);
    }
    
    /**
     * Test correct PreProcessors
     */
    @Test
    public void testCorrectPreProcessors()
    {
        boolean firstPreProcessor = false;
        boolean secondPreProcessor = false;
        boolean thirdPreProcessor = false;
        boolean fourthPreProcessor = false;
        boolean fifthPreProcessor = false;

        Iterator<Endpoint> endpointIt = endpoints.iterator();
        while (endpointIt.hasNext()) {
            Endpoint endpoint = endpointIt.next();
            if (null != endpoint.getPreprocessors()) {
                Iterator<String> preProcessorsIt = endpoint.getPreprocessors().getPreprocessor().iterator();
                while (preProcessorsIt.hasNext()) {
                    String preProcessor = preProcessorsIt.next();
                    if (preProcessor.equals("edu.leipzig.sedri.test.WrongTestPreprocessor") && !fifthPreProcessor) {
                        fifthPreProcessor = true;
                    } else if (preProcessor.equals("edu.leipzig.sedri.test.TestPreprocessor") && !fourthPreProcessor) {
                        fourthPreProcessor = true;
                    } else if (preProcessor.equals("edu.leipzig.sedri.test.TestPreprocessor") && !secondPreProcessor) {
                        secondPreProcessor = true;
                    } else if (preProcessor.equals("edu.leipzig.sedri.test.TestPreprocessor") && !firstPreProcessor) {
                        firstPreProcessor = true;
                    } else if (preProcessor.equals("") && !thirdPreProcessor) {
                        thirdPreProcessor = true;
                    }
                }
            }
        }
        assertTrue("Wrong PreProcessor! Correct on is edu.leipzig.sedri.test.TestPreprocessor", firstPreProcessor);
        assertTrue("Wrong PreProcessor! Correct on is edu.leipzig.sedri.test.TestPreprocessor", secondPreProcessor);
        assertTrue("Wrong PreProcessor! Correct on is '' or null", thirdPreProcessor);
        assertTrue("Wrong PreProcessor! Correct on is edu.leipzig.sedri.test.TestPreprocessor", fourthPreProcessor);
        assertTrue("Wrong PreProcessor! Correct on is edu.leipzig.sedri.test.WrongTestPreprocessor", fifthPreProcessor);
    }
    
    /**
     * Test correct PostProcessors
     */
    @Test
    public void testCorrectPostProcessors()
    {
        boolean firstPostProcessor = false;
        boolean secondPostProcessor = false;
        boolean thirdPostProcessor = false;
        boolean fourthPostProcessor = false;

        Iterator<Endpoint> endpointIt = endpoints.iterator();
        while (endpointIt.hasNext()) {
            Endpoint endpoint = endpointIt.next();
            if (null != endpoint.getPostprocessors()) {
                Iterator<String> postProcessorsIt = endpoint.getPostprocessors().getPostprocessor().iterator();
                while (postProcessorsIt.hasNext()) {
                    String postProcessor = postProcessorsIt.next();
                    if (postProcessor.equals("edu.leipzig.sedri.test.WrongTestPostprocessor") && !fourthPostProcessor) {
                        fourthPostProcessor = true;
                    } else if (postProcessor.equals("edu.leipzig.sedri.test.TestPostprocessor") && !thirdPostProcessor) {
                        thirdPostProcessor = true;
                    } else if (postProcessor.equals("edu.leipzig.sedri.test.TestPostprocessor") && !secondPostProcessor) {
                        secondPostProcessor = true;
                    } else if (postProcessor.equals("edu.leipzig.sedri.test.TestPostprocessor") && !firstPostProcessor) {
                        firstPostProcessor = true;
                    }
                }
            }
        }
        assertTrue("Wrong PostProcessor! Correct on is edu.leipzig.sedri.test.TestPostprocessor", firstPostProcessor);
        assertTrue("Wrong PostProcessor! Correct on is edu.leipzig.sedri.test.TestPostprocessor", secondPostProcessor);
        assertTrue("Wrong PostProcessor! Correct on is edu.leipzig.sedri.test.TestPostprocessor", thirdPostProcessor);
        assertTrue("Wrong PostProcessor! Correct on is edu.leipzig.sedri.test.WrongTestPostprocessor", fourthPostProcessor);
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
        int sourcesCount = 0;
        Iterator<Endpoint> endpointIt = endpoints.iterator();
        while (endpointIt.hasNext()) {
            Endpoint endpoint = endpointIt.next();
            if (null != endpoint.getSources()) {
                sourcesCount += endpoint.getSources().getSource().size();
            }
        }
        assertEquals("Wrong count of params!", 5, sourcesCount);
    }
    
    /**
     * Test correct Sources urls
     */
    @Test
    public void testCorrectSourcesUrls()
    {
        boolean firstSourceUrl = false;
        boolean secondSourceUrl = false;
        boolean thirdSourceUrl = false;
        boolean fourthSourceUrl = false;
        boolean fifthSourceUrl = false;

        Iterator<Endpoint> endpointIt = endpoints.iterator();
        while (endpointIt.hasNext()) {
            Endpoint endpoint = endpointIt.next();
            if (null != endpoint.getSources()) {
                Iterator<Sources.Source> sourceUrlsIt = endpoint.getSources().getSource().iterator();
                while (sourceUrlsIt.hasNext()) {
                    String sourceUrl = sourceUrlsIt.next().getUrl();
                    if (null == sourceUrl && !fifthSourceUrl) {
                        fifthSourceUrl = true;
                    } else if (null == sourceUrl && !fourthSourceUrl) {
                        fourthSourceUrl = true;
                    } else if (sourceUrl.equals("http://drugbank.bio2rdf.org/sparql") && !thirdSourceUrl) {
                        thirdSourceUrl = true;
                    } else if (sourceUrl.equals("http://drugbank.bio2rdf.org/sparql") && !secondSourceUrl) {
                        secondSourceUrl = true;
                    } else if (sourceUrl.equals("http://dbpedia.org/sparql") && !firstSourceUrl) {
                        firstSourceUrl = true;
                    }
                }
            }
        }
        assertTrue("Wrong sources url! Correct on is http://dbpedia.org/sparql", firstSourceUrl);
        assertTrue("Wrong sources url! Correct on is http://drugbank.bio2rdf.org/sparql", secondSourceUrl);
        assertTrue("Wrong sources url! Correct on is http://drugbank.bio2rdf.org/sparql", thirdSourceUrl);
        assertTrue("Wrong sources url! Correct on is 'null'", fourthSourceUrl);
        assertTrue("Wrong sources url! Correct on is 'null'", fifthSourceUrl);
    }

    /**
     * Test correct Sources queries
     */
    @Test
    public void testCorrectSourcesQueries()
    {
        boolean firstSourceQuery = false;
        boolean secondSourceQuery = false;
        boolean thirdSourceQuery = false;
        boolean fourthSourceQuery = false;
        boolean fifthSourceQuery = false;

        Iterator<Endpoint> endpointIt = endpoints.iterator();
        while (endpointIt.hasNext()) {
            Endpoint endpoint = endpointIt.next();
            if (null != endpoint.getSources()) {
                Iterator<Sources.Source> sourceQueriesIt = endpoint.getSources().getSource().iterator();
                while (sourceQueriesIt.hasNext()) {
                    String sourceQuery = sourceQueriesIt.next().getQuery();
                    if (null == sourceQuery && !fifthSourceQuery) {
                        fifthSourceQuery = true;
                    } else if (null == sourceQuery && !thirdSourceQuery) {
                        thirdSourceQuery = true;
                    } else if (sourceQuery.equals("select ?s where {?s a <http://bio2rdf.org/drugbank_vocabulary:Drug>} limit $testLimit") && !fourthSourceQuery) {
                        fourthSourceQuery = true;
                    } else if (sourceQuery.equals("select ?s where {?s a <http://bio2rdf.org/drugbank_vocabulary:Drug>} order by ?s limit 1") && !secondSourceQuery) {
                        secondSourceQuery = true;
                    } else if (sourceQuery.equals("select ?s where {?s a <http://dbpedia.org/ontology/$class>. FILTER regex(?s, \"Methylfentanyl\")} order by ?s limit 1") && !firstSourceQuery) {
                        firstSourceQuery = true;
                    }
                }
            }
        }
        assertTrue("Wrong sources query! Correct on is select ?s where {?s a <http://dbpedia.org/ontology/$class>. FILTER regex(?s, \"Methylfentanyl\")} order by ?s limit 1", firstSourceQuery);
        assertTrue("Wrong sources query! Correct on is select ?s where {?s a <http://bio2rdf.org/drugbank_vocabulary:Drug>} order by ?s limit 1", secondSourceQuery);
        assertTrue("Wrong sources query! Correct on is 'null'", thirdSourceQuery);
        assertTrue("Wrong sources query! Correct on is select ?s where {?s a <http://bio2rdf.org/drugbank_vocabulary:Drug>} limit $testLimit", fourthSourceQuery);
        assertTrue("Wrong sources query! Correct on is 'null'", fifthSourceQuery);
    }
    
    /**
     * Test correct Format
     */
    @Test
    public void testCorrectFormat()
    {
        boolean firstFormat = false;
        boolean secondFormat = false;
        boolean thirdFormat = false;
        boolean fourthFormat = false;
        boolean fifthFormat = false;

        Iterator<Endpoint> endpointIt = endpoints.iterator();
        while (endpointIt.hasNext()) {
            Endpoint endpoint = endpointIt.next();
            if (null != endpoint.getFormat()) {
                String format = endpoint.getFormat();
                if (format.equals("N-TRIPLE") && !fourthFormat) {
                    fourthFormat = true;
                } else if (format.equals("JSON-LD") && !secondFormat) {
                    secondFormat = true;
                } else if (format.equals("RDF/XML") && !firstFormat) {
                    firstFormat = true;
                } else if (format.equals("") && !thirdFormat) {
                    thirdFormat = true;
                }
            } else {
                if (!thirdFormat) {
                    thirdFormat = true;
                } else if (!fifthFormat) {
                    fifthFormat = true;
                }
            }
        }
        assertTrue("Wrong format! Correct on is RDF/XML", firstFormat);
        assertTrue("Wrong format! Correct on is JSON-LD", secondFormat);
        assertTrue("Wrong format! Correct on is '' or null", thirdFormat);
        assertTrue("Wrong format! Correct on is N-TRIPLE", fourthFormat);
        assertTrue("Wrong format! Correct on is null", fifthFormat);
    }
}
