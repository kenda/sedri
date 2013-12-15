package edu.leipzig.sedri;

import com.hp.hpl.jena.rdf.model.Model;
import edu.leipzig.sedri.PostprocessorInterface;

public class TestPostprocessor implements PostprocessorInterface{

    public Model process(Model model){
	model.removeAll();
	return model;
    }
}
