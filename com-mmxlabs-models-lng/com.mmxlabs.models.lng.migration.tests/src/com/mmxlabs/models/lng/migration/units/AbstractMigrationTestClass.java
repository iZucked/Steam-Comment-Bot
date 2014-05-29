/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

import com.mmxlabs.scenario.service.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scenario.service.util.encryption.impl.PassthroughCipherProvider;

public abstract class AbstractMigrationTestClass {

	private static ServiceRegistration<IScenarioCipherProvider> cipherServiceRef = null;

	@BeforeClass
	public static void registerCipherProvider() {
		final BundleContext bundleContext = FrameworkUtil.getBundle(AbstractMigrationTestClass.class).getBundleContext();
		IScenarioCipherProvider provider = new PassthroughCipherProvider();
		Dictionary<String, Object> properties = new Hashtable<>();
		cipherServiceRef = bundleContext.registerService(IScenarioCipherProvider.class, provider, properties);
	}

	@AfterClass
	public static void dregisterCipherProvider() {
		if (cipherServiceRef != null) {
			cipherServiceRef.unregister();
			cipherServiceRef = null;
		}
	}
}
