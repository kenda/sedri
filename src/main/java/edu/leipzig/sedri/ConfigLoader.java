/**
 * 
 */
package edu.leipzig.sedri;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.jena.riot.RDFDataMgr;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

/**
 * Loads config files for the webservice configuration.
 * Possible formats are xml and rdf (rdf/xml, turtel aso.)
 * @author Lars Eidam
 */
public class ConfigLoader {

	private File configfile;
	private String filetype;
	
	public ConfigLoader(File configfile, String filetype) {
		this.configfile = configfile;
		this.filetype = filetype;
	}
	/**
	 * Try to load the config file according to the filetype.
	 * @return ArrayList<Server> The loaded Server instance if file available, null otherwise 
	 */
	public ArrayList<Server> load() {
		ArrayList<Server> returnValue;
		
		if (filetype.equals("xml")) {
			returnValue = this.loadXML();
		} else if (filetype.equals("rdf")) {
			returnValue = this.loadRDF();
		} else {
			returnValue = new ArrayList<Server>();
		}

		return returnValue;
	}
	
	/**
	 * Load a xml config file with the java xml unmarshal function
	 * @return ArrayList<Server>  The loaded server instances if file available, empty List otherwise
	 */
	private ArrayList<Server> loadXML() {
		ArrayList<Server> server = new ArrayList<Server>();
		try {
			JAXBContext jc = JAXBContext.newInstance("edu.leipzig.sedri");
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			server.add((Server) unmarshaller.unmarshal(configfile));
		} catch(Exception e) {
			System.err.println("Failure while loading xml config file!");
			System.err.println("Exception: " + e);
		}
		return server;
	}
	
	/**
	 * Load a RDF configuration Ontology from file
	 * 
	 * @return ArrayList<Server> The loaded server instances if file available, empty List otherwise
	 */
	private ArrayList<Server> loadRDF() {
		ArrayList<Server> servers = new ArrayList<Server>();
		
		Model model = RDFDataMgr.loadModel(configfile.getAbsolutePath());
		
		servers = loadWebservices(model);
		
		return servers;
	}
	
	/**
	 * Load Webservices instances form rdf config ontology
	 * 
	 * @return ArrayList<Server> The loaded server instances if file available, empty List otherwise
	 */
	private ArrayList<Server> loadWebservices(Model model) {
		ArrayList<Server> servers = new ArrayList<Server>();
		Node webserverVariableNode = NodeFactory.createVariable("WebserviceUri");
		Node typePropertyNode = NodeFactory.createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
		Node webserverClassNode = NodeFactory.createURI("http://sedri.de/vocab/Webservice");

		ExtendedIterator<Triple>  webserviceResources = model.getGraph().find(webserverVariableNode, typePropertyNode, webserverClassNode);

		while (webserviceResources.hasNext()) {
			Server server = new Server();
			// get portnumber
			Node webserviceNode = webserviceResources.next().getSubject();
			Node portPropertyNode = NodeFactory.createURI("http://sedri.de/vocab/portNumber");
			Node portVariableNode = NodeFactory.createVariable("portNumber");
			ExtendedIterator<Triple> portLiterals = model.getGraph().find(webserviceNode, portPropertyNode, portVariableNode);
			BigInteger portnumber = BigInteger.valueOf((Integer) portLiterals.next().getObject().getLiteralValue());
			server.setPort(portnumber);
			
			// get endpoints
			ArrayList<Endpoint> endpoints = loadEndpoints(model, webserviceNode);
			if (0 != endpoints.size()) {
				server.getEndpoint().addAll(endpoints);
				servers.add(server);
			} else {
				System.err.println("No endpoints config found!");
			}
		}
		return servers;
	}
	
	/**
	 * Load Endpoint instances from rdf config ontology
	 * 
	 * @return ArrayList<Endpoint> The loaded endpoint instances if available, empty List otherwise
	 */
	private ArrayList<Endpoint> loadEndpoints(Model model, Node webserviceNode) {
		ArrayList<Endpoint> endpoints = new ArrayList<Endpoint>();
		Node endpointPropertyNode = NodeFactory.createURI("http://sedri.de/vocab/nodes");
		Node endpointVariableNode = NodeFactory.createVariable("endpointUri");
		ExtendedIterator<Triple> endpointResources = model.getGraph().find(webserviceNode, endpointPropertyNode, endpointVariableNode);
		
		while (endpointResources.hasNext()) {
			Endpoint endpoint = new Endpoint();
			Node endpointNode = endpointResources.next().getObject();
			
			// load url-path
			String urlPath = loadUrlPath(model, endpointNode);
			if (!urlPath.isEmpty()) {
				endpoint.setUrl(urlPath);
			} else {
				System.err.println("No URL-path found for endpoint " + endpointNode.getURI());
				break;
			}
			
			// load resultFormat
			String resultFormat = loadResultFormat(model, endpointNode);
			if (!resultFormat.isEmpty()) {
				endpoint.setFormat(resultFormat);
			} else {
				System.err.println("No result format found for endpoint " + endpointNode.getURI());
				break;
			}
			
			// load preprocessor
			Preprocessors preProcessors = loadPreProcessors(model, endpointNode);
			if (0 != preProcessors.getPreprocessor().size()) {
				endpoint.setPreprocessors(preProcessors);
			}
			
			// load postprocessor
			Postprocessors postProcessors = loadPostProcessors(model, endpointNode);
			if (0 != postProcessors.getPostprocessor().size()) {
				endpoint.setPostprocessors(postProcessors);
			}
			
			// TODO: hier gehts weiter mit dem laden von param und sources
			
			System.out.println(endpoint.getPostprocessors().getPostprocessor());
		}
		
		return endpoints;
	}
	
	/**
	 * Load URL-path string from an endpoint of a rdf config ontology
	 * 
	 * @return String The URL-path of the endpoint
	 */
	private String loadUrlPath(Model model, Node endpointNode) {
		Node urlPathPropertyNode = NodeFactory.createURI("http://sedri.de/vocab/url-path");
		Node urlPathVariableNode = NodeFactory.createVariable("urlPath");
		ExtendedIterator<Triple> urlPathLiterals = model.getGraph().find(endpointNode, urlPathPropertyNode, urlPathVariableNode);
		
		return (String) urlPathLiterals.next().getMatchObject().getLiteralValue();
	}
	
	/**
	 * Load result format string from an endpoint of a rdf config ontology
	 * 
	 * @return String The result format of the endpoint
	 */
	private String loadResultFormat(Model model, Node endpointNode) {
		Node resultFormatPropertyNode = NodeFactory.createURI("http://sedri.de/vocab/resultFormat");
		Node resultFormatVariableNode = NodeFactory.createVariable("resultFormat");
		ExtendedIterator<Triple> resultFormatLiterals = model.getGraph().find(endpointNode, resultFormatPropertyNode, resultFormatVariableNode);
		
		return (String) resultFormatLiterals.next().getMatchObject().getLiteralValue();
	}
	
	/**
	 * Load Preprocessors from an endpoint of a rdf config ontology
	 * 
	 * @return Preprocessors The preprocessors of the endpoint
	 */
	private Preprocessors loadPreProcessors(Model model, Node endpointNode) {
		Preprocessors preprocessors = new Preprocessors();
		Node preProcessorPropertyNode = NodeFactory.createURI("http://sedri.de/vocab/preprocessor");
		Node preprocessorVariableNode = NodeFactory.createVariable("preprocessors");
		ExtendedIterator<Triple> preProcessorLiterals = model.getGraph().find(endpointNode, preProcessorPropertyNode, preprocessorVariableNode);
		while (preProcessorLiterals.hasNext()) {
			preprocessors.getPreprocessor().add((String) preProcessorLiterals.next().getObject().getLiteralValue());
		}
		return preprocessors;
	}
	
	/**
	 * Load Postprocessors from an endpoint of a rdf config ontology
	 * 
	 * @return Postprocessors The postprocessors of the endpoint
	 */
	private Postprocessors loadPostProcessors(Model model, Node endpointNode) {
		Postprocessors postprocessors = new Postprocessors();
		Node postProcessorPropertyNode = NodeFactory.createURI("http://sedri.de/vocab/postprocessor");
		Node postprocessorVariableNode = NodeFactory.createVariable("postprocessors");
		ExtendedIterator<Triple> postProcessorLiterals = model.getGraph().find(endpointNode, postProcessorPropertyNode, postprocessorVariableNode);
		while (postProcessorLiterals.hasNext()) {
			postprocessors.getPostprocessor().add((String) postProcessorLiterals.next().getObject().getLiteralValue());
		}
		return postprocessors;
	}
}