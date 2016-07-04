/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.license.features.pluginxml;

import java.io.InputStream;

import org.eclipse.core.internal.registry.ExtensionRegistry;
import org.eclipse.core.runtime.ContributorFactoryOSGi;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.license.features.internal.FeatureEnablementModule;
import com.mmxlabs.license.features.internal.PluginXMLEnablementExtension;

@SuppressWarnings("restriction")
public class PluginRegistryHook {

	private static final Logger log = LoggerFactory.getLogger(PluginRegistryHook.class);

	@Inject
	private IExtensionRegistry extensionRegistry;

	@Inject(optional = true)
	private Iterable<PluginXMLEnablementExtension> featureEnablements;

	public void registerEnablements() {

		// Dynamically register UI elements.
		if (extensionRegistry instanceof ExtensionRegistry) {
			// This is marked as experimental functionality.
			final Object token = ((ExtensionRegistry) extensionRegistry).getTemporaryUserToken();

			for (final PluginXMLEnablementExtension ext : featureEnablements) {

				final String permissionKey = "features:" + LicenseFeatures.clean(ext.getFeature());
				if (LicenseFeatures.isPermitted(permissionKey)) {
					final Bundle bundle = ext.getBundle();

					final IContributor contributor = ContributorFactoryOSGi.createContributor(bundle);
					try (InputStream is = bundle.getResource(ext.getPluginXML()).openStream()) {
						extensionRegistry.addContribution(is, contributor, false, null, null, token);
					} catch (final Exception e) {
						log.error(e.getMessage(), e);
					}
				}
			}
		}
	}

	public static void initialisePluginXMLEnablements() {
		final Injector injector = Guice.createInjector(new FeatureEnablementModule());
		final PluginRegistryHook hook = new PluginRegistryHook();
		injector.injectMembers(hook);
		hook.registerEnablements();
	}

}
