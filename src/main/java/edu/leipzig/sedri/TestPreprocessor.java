package edu.leipzig.sedri;

import java.util.Hashtable;
import edu.leipzig.sedri.PreprocessorInterface;

public class TestPreprocessor implements PreprocessorInterface{

    public Hashtable<String, String> process(Hashtable<String, String> params){
	params.put("muh","meh");
	return params;
    }
}
