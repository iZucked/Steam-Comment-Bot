package com.mmxlabs.rcp.icons.lingo.internal;

import java.util.Hashtable;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.url.URLConstants;
import org.osgi.service.url.URLStreamHandlerService;

/**
 * Bundle activator to register the icons: URI protocol handler
 * 
 * @author Simon Goodall
 *
 */
public class Activator extends AbstractUIPlugin {

	private ServiceRegistration<URLStreamHandlerService> urlHandlerService;

	private static Activator instance;

	public static Activator getInstance() {
		return instance;
	}

	@Override
	public void start(final BundleContext context) throws Exception {

		assert instance == null;
		super.start(context);

		final Hashtable<String, Object> properties = new Hashtable<>();
		properties.put(URLConstants.URL_HANDLER_PROTOCOL, new String[] { "icons" });
		urlHandlerService = context.registerService(URLStreamHandlerService.class, new IconsURLHandler(), properties);

		instance = this;
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		instance = null;

		urlHandlerService.unregister();

		super.stop(context);
	}

}