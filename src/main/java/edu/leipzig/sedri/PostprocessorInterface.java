package edu.leipzig.sedri;

import com.hp.hpl.jena.rdf.model.Model;

interface PostprocessorInterface{

    public Model process(Model model);

}
