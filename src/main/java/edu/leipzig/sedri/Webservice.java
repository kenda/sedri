package edu.leipzig.sedri;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.lang.Class;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import com.github.jsonldjava.jena.JenaJSONLD;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
/**
 * The webserver class handles the incoming HTTP requests.
 */
public class Webservice extends AbstractHandler{

    /**
     * List of Endpoints given by the config.
     */
    private List<Endpoint> endpoints;
    
    public Webservice(edu.leipzig.sedri.Server server){
	this.endpoints = server.getEndpoint();
    }

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException
    {
	for( Endpoint endpoint : this.endpoints ){
	    String url = endpoint.getUrl();

	    if (url.equals(target)){

		Hashtable<String, String> paramTable = new Hashtable<String, String>();
		QueryProcessor queryProcessor = new QueryProcessor();

		// get parameter values
		if (endpoint.getParams() != null){
		    Params params = (Params) endpoint.getParams();
		    for ( String param : params.getParam() ){
			paramTable.put(param, request.getParameter(param));
		    }
		}

		// execute preprocessors
		Preprocessors preprocessors = endpoint.getPreprocessors();
		if (preprocessors != null){
		    for (String classname : preprocessors.getPreprocessor()){
			try{
			    Class cl = Class.forName(classname);
			    Object o = cl.getConstructor().newInstance();
			    Method m = cl.getMethod("process", Hashtable.class);
			    paramTable = (Hashtable<String,String>) m.invoke(o, paramTable);
			}
			catch (ClassNotFoundException cnfe){
			    System.out.println("Failed loading Preprocessor!");
			}
			catch (Exception e)
			    {System.out.println(e);}
		    }
		}
		
		Sources sources = (Sources) endpoint.getSources();

		// check if all queries have compatible query types
		boolean isSelect = QueryFactory.create(sources.getSource().get(0).getQuery()).isSelectType();
		if (isSelect == true){
		    // all queries have to be SELECT queries
		    for( int i = 1; i < sources.getSource().size(); i++ ){
			if (QueryFactory.create(sources.getSource().get(i).getQuery()).isSelectType() == false){
			    System.out.println("Incompatible query types");
			    throw new QueryException("Incompatible query types");
			}
		    }
		}
		else {
		    // all queries have to be CONSTRUCT or DESCRIBE queries
		    for( int i = 1; i < sources.getSource().size(); i++ ){
			if ( (QueryFactory.create(sources.getSource().get(i).getQuery()).isConstructType() == false) &&
			     (QueryFactory.create(sources.getSource().get(i).getQuery()).isDescribeType() == false)) {
			    System.out.println("Incompatible query types");
			    throw new QueryException("Incompatible query types");
			}
		    }
		}

		Model model = ModelFactory.createDefaultModel();

		// query the given sources
		for ( Sources.Source source : sources.getSource() ){
		    model.add(queryProcessor.process(source.getUrl(),
						     source.getQuery(),
						     paramTable));
		}

		// execute postprocessors
		Postprocessors postprocessors = endpoint.getPostprocessors();
		if (postprocessors != null){
		    for (String classname : postprocessors.getPostprocessor()){
			try{
			    Class cl = Class.forName(classname);
			    Object o = cl.getConstructor().newInstance();
			    Method m = cl.getMethod("process", Model.class);
			    model = (Model) m.invoke(o, model);
			}
			catch (ClassNotFoundException cnfe){
			    System.out.println("Failed loading Postprocessor!");
			}
			catch (Exception e)
			    {System.out.println(e);}
		    }
		}

		// output the model in the given format
		String accept = request.getHeader("accept");
		String format = endpoint.getFormat();
		if (accept.equals("text/turtle")){
		    format = "TURTLE";
		    response.setContentType("text/turtle;charset=utf-8");
		}
		else if (accept.equals("text/plain")){
		    format = "N-TRIPLE";
		    response.setContentType("text/plain;charset=utf-8");
		}
		else if (accept.equals("application/rdf+xml")){
		    format = "RDF/XML";
		    response.setContentType("application/rdf+xml;charset=utf-8");
		}
		
		JenaJSONLD.init();
		StringWriter output = new StringWriter();
		model.write(output, format);
		
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		response.getWriter().println(output.toString());

		break;
	    }
	    else{
		// Not found
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		baseRequest.setHandled(true);
	    }
	}
    }
}
