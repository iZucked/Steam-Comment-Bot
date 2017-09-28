package com.mmxlabs.lngdataserver.server;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.mmxlabs.embeddedmongo.MongoProvider;
import com.mmxlabs.lngdataserver.server.endpoint.impl.DataServerEndPointExtensionUtil;

public class Activator implements BundleActivator {
	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.lngdataserver.distances.server"; //$NON-NLS-1$
	// The shared instance
	private static Activator plugin;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Activator.class);
	
	private ConfigurableApplicationContext servletContext;

	private MongoDBService mongoService;
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		plugin = this;
		
		String binariesPath = new MongoProvider().getStringPath();
		
		mongoService = new MongoDBService();
		mongoService.setEmbeddedBinariesLocation(binariesPath);
		mongoService.setEmbeddedDataLocation("/Users/roberterdin/tmp/mongo_data");
		int port = 	mongoService.start();
		
		
		
		// this is a bit dangerous, should probably let the server choose it's own port with server.port=0
		int randomPort = randomPort();
		String[] args = {
				//"--db.embeddedBinaries=" + binariesPath, 
				"--server.port=" + randomPort,
				"--db.embedded=false",
				"--db.port=" + port,
				"--db.diagnosticDataCollectionEnabled=false",
				"--server.cors=true"
				};
		Thread background = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				Object[] endPoints = DataServerEndPointExtensionUtil.getEndPoints();
				
				servletContext = SpringApplication.run(endPoints, args);
				BackEndUrlProvider.INSTANCE.setPort(randomPort);
				BackEndUrlProvider.INSTANCE.setAvailable(true);
			}
		});
		
		background.start();
		System.out.println("starting in background...");
	}
	
	private int randomPort() throws IOException {
		for (int i = 65534; i > 49152; i--) {
			if (portAvailable(i)) {
				return i;
			}
		}
		LOGGER.error("No free port available 49152-65534");
		throw new IOException("No free port available 49152-65534");
	}
	
    private static boolean portAvailable(int port) {
        LOGGER.info("--------------Testing port " + port);
        Socket s = null;
        try {
            s = new Socket("localhost", port);

            // If the code makes it this far without an exception it means
            // something is using the port and has responded.
            LOGGER.info("--------------Port " + port + " is not available");
            return false;
        } catch (IOException e) {
            LOGGER.info("--------------Port " + port + " is available");
            return true;
        } finally {
            if( s != null){
                try {
                    s.close();
                } catch (IOException e) {
                	LOGGER.error("Error closing probing socket" , e);
                    throw new RuntimeException("Error closing probing socket" , e);
                }
            }
        }
    }
    
    private String getMongoDataPath() throws URISyntaxException, IOException {
		final Bundle bundle = FrameworkUtil.getBundle(Activator.class);
		String result = new File(FileLocator.toFileURL(bundle.getResource("/mongo_data")).toURI()).getAbsolutePath();
		LOGGER.info("MongoDB directory: " + result);
		return result;
    }
    
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
}
