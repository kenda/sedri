package edu.leipzig.sedri;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.cli.*;

/**
 * This is the main class of SEDRI.
 *
 * It basically reads in the configuration and starts the webserver.
 */
public class Main {
	
	private static String standardFiletype = "xml";
	private static String standardConfigFile = "config.xml";

    public static void main(String[] args) {
    	String filetype = standardFiletype;
    	File configFile = new File(standardConfigFile);
    	
    	// parse args
    	CommandLine commandLine;
    	Option option_t = OptionBuilder.withArgName("opt1").hasArg().withDescription("The t option").create("t");
    	Option option_f = OptionBuilder.withArgName("opt2").hasArg().withDescription("The f option").create("f");
    	Options options = new Options();
    	CommandLineParser parser = new GnuParser();
    	options.addOption(option_t);
    	options.addOption(option_f);
    	try {
    		commandLine = parser.parse(options, args);
    		
    		if (commandLine.hasOption("t")) {
    			String optionValue_t = commandLine.getOptionValue("t");
    			if (optionValue_t.toLowerCase().equals("xml")) {
    				filetype = "xml";
    			} else if (optionValue_t.toLowerCase().equals("rdf")) {
    				filetype = "rdf";
    			} else {
    				System.out.println("Option t is present but the filetype " + commandLine.getOptionValue("t") + " is is not supported!");
    				System.out.println("Standard filetype xml will be used!");
    			}
    		}
    		if (commandLine.hasOption("f")) {
    			String optionValue_f = commandLine.getOptionValue("f");
    			try {
    				configFile = new File(optionValue_f);
    			} catch (Exception e) {
        			System.out.println("Option f is present but the file " + optionValue_f + " couldn't be loaded!");
        			System.out.println("The standard config file (config.xml) will be used!");
    			}
    		}
    	} catch (ParseException exception) {
    		System.out.println("Parse error: ");
    		System.out.println(exception.getMessage());
    	}
    	ConfigLoader configLoader = new ConfigLoader(configFile, filetype);
    	ArrayList<Server> servers = configLoader.load();
    	if (0 == servers.size()) {
    		System.err.println("No webservice configuration could be loaded!");
    		return;
    	}
    	
    	ArrayList<org.eclipse.jetty.server.Server> webservers = new ArrayList<org.eclipse.jetty.server.Server>();
    	
    	// load webservices and start server
    	for(Iterator<Server> it = servers.iterator(); it.hasNext(); ) {
    		// load webservices
    		Server server = it.next();
    		BigInteger port = server.getPort();
    		org.eclipse.jetty.server.Server webserver = new org.eclipse.jetty.server.Server(port.intValue());
    		webserver.setHandler(new Webservice(server));
    		// start server
    		try {
    			webserver.start();
    			webservers.add(webserver);
    		} catch (Exception e) {
    			System.err.println("Server won't start!");
    			System.err.println("Exception: " + e);
    		}
    		
    	}

		try {
	    	webservers.get(0).join();
		} catch (Exception e) {
			System.err.println("Can't join server!");
			System.err.println("Exception: " + e);
		}
    }
}
