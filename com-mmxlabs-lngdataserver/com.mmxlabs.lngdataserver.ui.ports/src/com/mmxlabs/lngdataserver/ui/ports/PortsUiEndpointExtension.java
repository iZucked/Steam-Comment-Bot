package com.mmxlabs.lngdataserver.ui.ports;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.ui.server.EndpointExtension;

public class PortsUiEndpointExtension implements EndpointExtension {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PortsUiEndpointExtension.class);

	@Override
	public Handler getHandler() {
		ResourceHandler resourceHandler= new ResourceHandler();
		resourceHandler.setDirectoriesListed(true);
//		resourceHandler.setWelcomeFiles(new String[]{ "index.html" });
//		resourceHandler.setResourceBase(this.getClass().getResource("/web_files").toString());
		try {
			resourceHandler.setResourceBase(Activator.getWebFilesPath());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		resourceHandler.setDirectoriesListed(true);
		ContextHandler contextHandler= new ContextHandler(Activator.URL_PREFIX);
		try {
			contextHandler.setBaseResource(Resource.newResource(Activator.getWebFilesPath()));
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		contextHandler.setHandler(resourceHandler);
		return contextHandler;
	}
}
