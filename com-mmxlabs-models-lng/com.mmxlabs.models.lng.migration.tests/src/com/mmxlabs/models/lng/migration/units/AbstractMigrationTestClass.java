/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scenario.service.model.util.encryption.impl.PassthroughCipherProvider;

public abstract class AbstractMigrationTestClass {

	private static ServiceRegistration<IScenarioCipherProvider> cipherServiceRef = null;

	@BeforeAll
	public static void registerCipherProvider() {
		final Bundle bundle = FrameworkUtil.getBundle(AbstractMigrationTestClass.class);
		if (bundle != null) {
			final BundleContext bundleContext = bundle.getBundleContext();
			final IScenarioCipherProvider provider = new PassthroughCipherProvider();
			final Dictionary<String, Object> properties = new Hashtable<>();
			cipherServiceRef = bundleContext.registerService(IScenarioCipherProvider.class, provider, properties);
		}
	}

	@AfterAll
	public static void dregisterCipherProvider() {
		if (cipherServiceRef != null) {
			cipherServiceRef.unregister();
			cipherServiceRef = null;
		}
	}
}
