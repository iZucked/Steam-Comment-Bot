/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.browser.ui;

import java.io.InputStream;

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

	public static Image createScaledImage(final String imageType) {

		final Bundle bundle = FrameworkUtil.getBundle(Activator.class);
		final ImageDataProvider imageDataProvider = zoom -> {

			final String resource;
			switch (zoom) {
			case 150:
				resource = String.format("/icons/%s_24.png", imageType);
				break;
			case 200:
				resource = String.format("/icons/%s_32.png", imageType);
				break;
			default:
				resource = String.format("/icons/%s_16.png", imageType);
				break;
			}
			try (InputStream is = bundle.getResource(resource).openStream()) {
				return new ImageData(is);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
		return new Image(PlatformUI.getWorkbench().getDisplay(), imageDataProvider);
	}
}
