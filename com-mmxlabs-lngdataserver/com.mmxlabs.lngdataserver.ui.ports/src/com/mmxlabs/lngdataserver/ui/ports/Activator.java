package com.mmxlabs.lngdataserver.ui.ports;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;


public class Activator extends AbstractUIPlugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.lngdataserver.ui.ports"; //$NON-NLS-1$
	
	static final String URL_PREFIX = "/ports"; 
	
	// The shared instance
	private static Activator plugin;
	
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
		super.start(context);
		plugin = this;

//		// TODO: should we move this logic to the front-end?
//		while (!BackEndUrlProvider.INSTANCE.isAvailable()) {
//			Thread.sleep(5000);
//			System.out.println("waiting for back-end...");
//		}
	}
	
    static String getWebFilesPath() throws URISyntaxException, IOException {
		final Bundle bundle = FrameworkUtil.getBundle(Activator.class);
		String result = new File(FileLocator.toFileURL(bundle.getResource("/web_files")).toURI()).getAbsolutePath();
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
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in relative
	 * path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(final String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	
}
