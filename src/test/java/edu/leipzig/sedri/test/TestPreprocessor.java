package edu.leipzig.sedri.test;

import java.util.Hashtable;
import edu.leipzig.sedri.PreprocessorInterface;

public class TestPreprocessor implements PreprocessorInterface{

	public Hashtable<String, String> process(Hashtable<String, String> params){
		if (!params.containsKey("class")) {
			params.put("class","Drug");
		}
		return params;
	}
}
