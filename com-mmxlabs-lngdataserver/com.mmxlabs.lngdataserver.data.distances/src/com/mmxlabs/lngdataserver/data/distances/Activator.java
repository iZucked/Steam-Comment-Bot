/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.data.distances;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
//		BackEndUrlProvider.INSTANCE.addAvailableListener(() -> new DataImporter().importData());
	}

	@Override
	public void stop(BundleContext context) throws Exception {

	}
}
