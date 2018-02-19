package com.mmxlabs.lngdataserver.browser.ui;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageDataProvider;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator extends Plugin {

	private static final Logger LOG = LoggerFactory.getLogger(Activator.class);

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.lngdataserver.browser.ui"; //$NON-NLS-1$
	// The shared instance
	private static Activator plugin;

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Image createScaledImage(String imageType) {

		final Bundle bundle = FrameworkUtil.getBundle(Activator.class);
		ImageDataProvider imageDataProvider;
		try {
			final String basePath = new File(FileLocator.toFileURL(bundle.getResource("/icons/")).toURI()).getAbsolutePath();

			imageDataProvider = zoom -> {
				switch (zoom) {
				case 150:
					return new ImageData(basePath + "/" + imageType + "_24.png");
				case 200:
					return new ImageData(basePath + "/" + imageType + "_32.png");
				default:
					return new ImageData(basePath + "/" + imageType + "_16.png");
				}
			};
		} catch (URISyntaxException | IOException e) {
			throw new RuntimeException(e);
		}
		return new Image(PlatformUI.getWorkbench().getDisplay(), imageDataProvider);
	}
}
