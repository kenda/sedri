package edu.leipzig.sedri.test.unit;

import junit.framework.TestCase;

import java.util.Hashtable;
import java.security.*;

import org.apache.http.HttpException;

import edu.leipzig.sedri.QueryProcessor;

import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * Unit test for simple App.
 */
public class QueryProcessorTest 
    extends TestCase
{
	private QueryProcessor 				queryProcessor;
	private String 						endpoint;
	private String 						queryString;
	private Hashtable<String,String> 	params;
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public QueryProcessorTest( String testName )
    {
        super( testName );
    }

    protected void setUp() {
    	queryProcessor = new QueryProcessor();
    	endpoint = "http://dbpedia.org/sparql";
    	queryString = "SELECT ?s WHERE {?s a <http://dbpedia.org/ontology/Drug>} ORDER BY (?s) LIMIT $testLimit";
    	params = new Hashtable<String,String>();
    	params.put("testLimit", "13");
    }
    
    /**
     * Test process() methode with correct data
     */
    public void testMethodeProcessCorrect() throws NoSuchAlgorithmException
    {
    	Model resultModel = queryProcessor.process(endpoint, queryString, params);
        assertEquals(55, resultModel.size());
    }
    
    /**
     * Test process() methode with wrong endpoint data
     * @throws HttpException
     */
    public void testMethodeProcessWithWrongEndpoint()
    {
    	Model resultModel = queryProcessor.process("http://www.g1oogle.de", queryString, params);

    	assertEquals(0, resultModel.size());
        assertEquals("<ModelCom   {} | >", resultModel.toString());
    }
    
    /**
     * Test process() methode with wrong query
     * @throws QueryParseException 
     */
    public void testMethodeProcessWithWrongQuery()
    {
    	Model resultModel = ModelFactory.createDefaultModel();
    	try {
        	resultModel = queryProcessor.process(endpoint, "", params);
        	assertTrue("No Exception was thrown", false);
    	} catch (QueryParseException e) {
            assertEquals(-1992127352, resultModel.toString().hashCode());
            assertEquals(0, resultModel.size());
    	}
    }
    
    /**
     * Test process() methode with wrong params
     * @throws QueryParseException 
     */
    public void testMethodeProcessWithWrongParams()
    {
    	Model resultModel = ModelFactory.createDefaultModel();
    	Hashtable<String,String> wrongParams = new Hashtable<String,String>();
    	try {
        	resultModel = queryProcessor.process(endpoint, queryString, wrongParams);
        	assertTrue("No Exception was thrown", false);
    	} catch (QueryParseException e) {
            assertEquals(-1992127352, resultModel.toString().hashCode());
            assertEquals(0, resultModel.size());
    	}
    }
}
