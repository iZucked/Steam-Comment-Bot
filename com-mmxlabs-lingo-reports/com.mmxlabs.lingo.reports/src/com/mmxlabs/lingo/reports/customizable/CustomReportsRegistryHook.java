/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.customizable;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.eclipse.core.internal.registry.ExtensionRegistry;
import org.eclipse.core.runtime.ContributorFactoryOSGi;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/***
 * Registers Custom Reports with the Show Views of eclipse menu. Unfortunately,
 * we still have to find a way to do this without restarting eclipse, but for
 * now this should suffice.
 * 
 * @author Patrick
 */
@SuppressWarnings("restriction")
public class CustomReportsRegistryHook {

	private static final Logger LOG = LoggerFactory.getLogger(CustomReportsRegistryHook.class);

	@Inject
	private IExtensionRegistry extensionRegistry;

	public void registerEnablements() {

		// Dynamically register UI elements.
		if (extensionRegistry instanceof final ExtensionRegistry regImpl) {
			// This is marked as experimental functionality.
			final Object token = regImpl.getTemporaryUserToken();

			final Bundle bundle = FrameworkUtil.getBundle(CustomReportsRegistryHook.class);

			final IContributor contributor = ContributorFactoryOSGi.createContributor(bundle);

			final String pluginXMLPath = CustomReportsRegistry.getReportsPluginXMLPath();

			final File pluginXMLFile = new File(pluginXMLPath);

			if (pluginXMLFile.exists()) {
				try (InputStream is = new FileInputStream(pluginXMLFile)) {
					regImpl.addContribution(is, contributor, false, null, null, token);
				} catch (final Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
			// As per SG, do not write any errors or warning if first time startup and no
			// plugin.xml exists and now we write on exit as user may get curious if they
			// see it in the error log.
			// At any rate, if there is an issue accessing/writing (at least, if they have
			// write they will probably have read permission on it also).
		}
	}

	public static void initialisePluginXMLEnablements() {
		final Injector injector = Guice.createInjector(new CustomReportsModule());
		final CustomReportsRegistryHook hook = new CustomReportsRegistryHook();
		injector.injectMembers(hook);
		hook.registerEnablements();
	}
}
