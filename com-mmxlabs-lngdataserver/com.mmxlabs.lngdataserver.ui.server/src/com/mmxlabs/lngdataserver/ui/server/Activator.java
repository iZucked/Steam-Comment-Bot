package com.mmxlabs.lngdataserver.ui.server;

import static org.ops4j.peaberry.eclipse.EclipseRegistry.eclipseRegistry;
import static org.ops4j.peaberry.Peaberry.osgiModule;
import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.Iterables;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;


public class Activator extends Plugin {
	
	private static final Logger LOG = LoggerFactory.getLogger(Activator.class);
	
	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.lngdataserver.server"; //$NON-NLS-1$
	// The shared instance
	private static Activator plugin;
	
	private Server server;
	private final ContextHandlerCollection myHandlers = new ContextHandlerCollection();
	
	public Activator() {
	}

	/*
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		server = new Server(0);
		
		ContextHandler baseContext = new ContextHandler();
        baseContext.setContextPath("/");
        myHandlers.addHandler(baseContext);
		
		server.setHandler(myHandlers);
		
		// get registered extensions
		Injector injector = Guice.createInjector(new ServerExtensionsModule());
		Iterable<EndpointExtensionPoint> extensions = injector.getInstance(Key.get(new TypeLiteral<Iterable<EndpointExtensionPoint>>() {
		}));
		LOG.debug("Found " + Iterables.size(extensions) + " extensions");
		for (EndpointExtensionPoint ext : extensions) {
			EndpointExtension pluginInstance = ext.getInstance();
			if (pluginInstance != null) {
				myHandlers.addHandler(pluginInstance.getHandler());
//				pluginInstance.getHandler().start();
			}
		}
		
		// Starting the Server
		server.start();
		int port = ((ServerConnector)server.getConnectors()[0]).getLocalPort();
		
		ServerUrlProvider.INSTANCE.setPort(port);
		ServerUrlProvider.INSTANCE.setAvailable(true);
		System.out.println("Started on port " + port);
	}
	
    private String getWebFilesPath() throws URISyntaxException, IOException {
		final Bundle bundle = FrameworkUtil.getBundle(Activator.class);
		String result = new File(FileLocator.toFileURL(bundle.getResource("/web_files")).toURI()).getAbsolutePath();
		return result;
    }

	/*
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		server.stop();
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	private static class ServerExtensionsModule extends AbstractModule {
		@Override
		protected void configure() {
			install(osgiModule(FrameworkUtil.getBundle(Activator.class).getBundleContext(), eclipseRegistry()));
			bind(iterable(EndpointExtensionPoint.class)).toProvider(service(EndpointExtensionPoint.class).multiple());
		}
	}
	
}
