package edu.leipzig.sedri;

import java.util.Hashtable;
import java.lang.UnsupportedOperationException;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;

/**
 * This class handles the execution of the given SPARQL queries.
 */
public class QueryProcessor{

	public QueryProcessor(){
	}

	/**
	 * This method queries the given SPARQL endpoint with the given query.
	 *
	 * @param endpoint Url of the SPARQL endpoint
	 * @param queryString the SPARQL query possibly including substitution variables
	 * @param params Hashtable of the key value pairs that should be substituted
	 *
	 * @return the jena rdf model of the results
	 */
	public Model process(String endpoint, String queryString, Hashtable<String,String> params){
		// subsitute given variables in the query
		queryString = this.substituteQuery(queryString, params);

		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);
		qexec.setTimeout(2000, 0);

		Model result = ModelFactory.createDefaultModel();
		try{
			if (query.isDescribeType()){
				result = qexec.execDescribe();
			} else if (query.isConstructType()){
				result = qexec.execConstruct();
			} else if (query.isSelectType()){
				result = ResultSetFormatter.toModel(qexec.execSelect());
			}
			else {
				throw new UnsupportedOperationException();
			};
		} catch (QueryException qe){
			System.out.println(qe);
		}

		return result;
	}

	/**
	 * This methods substitutes the given $-variables of a SPARQL query.
	 *
	 * @param query the SPARQL query possibly including substitution variables
	 * @param params Hashtable of the key value pairs that should be substituted
	 *
	 * @return final SPARQL query with all variables substituted.
	 */
	private String substituteQuery(String query, Hashtable<String,String> params){
		for (String key : params.keySet()) {
			String value = params.get(key);
			query = query.replace("$"+key, value);
		}
		return query;
	}
}
