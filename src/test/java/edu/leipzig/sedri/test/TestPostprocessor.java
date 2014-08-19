package edu.leipzig.sedri.test;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import edu.leipzig.sedri.PostprocessorInterface;

public class TestPostprocessor implements PostprocessorInterface{

    public Model process(Model model){
    	Resource resource = model.createResource("http://examples.org/TestResource1");
    	Property property = model.createProperty("http://examples.org/TestProperty1");
    	resource.addProperty(property, "TestObject1");
    	return model;
    }
}
