package edu.leipzig.sedri;

import java.io.File;
import java.math.BigInteger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 * This is the main class of SEDRI.
 *
 * It basically reads in the configuration and starts the webserver.
 */
public class Main {

    public static void main(String[] args) throws Exception {

	// read the configuration file 
	JAXBContext jc = JAXBContext.newInstance("edu.leipzig.sedri");
	Unmarshaller unmarshaller = jc.createUnmarshaller();
	Server server = (Server) unmarshaller.unmarshal(new File("config.xml"));

	// start webserver
	BigInteger port = server.getPort();
	org.eclipse.jetty.server.Server webserver = new org.eclipse.jetty.server.Server(port.intValue());
	webserver.setHandler(new Webservice(server));
 
	webserver.start();
	webserver.join();
    }
}
