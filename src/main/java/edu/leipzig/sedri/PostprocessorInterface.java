package edu.leipzig.sedri;

import com.hp.hpl.jena.rdf.model.Model;

public interface PostprocessorInterface{

    public Model process(Model model);

}
