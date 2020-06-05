/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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

@SuppressWarnings("restriction")
public class CustomReportsRegistryHook {

	private static final Logger log = LoggerFactory.getLogger(CustomReportsRegistryHook.class);
	
	@Inject
	private IExtensionRegistry extensionRegistry;
	
	public void registerEnablements() {

		// Dynamically register UI elements.
		if (extensionRegistry instanceof ExtensionRegistry) {
			// This is marked as experimental functionality.
			final Object token = ((ExtensionRegistry) extensionRegistry).getTemporaryUserToken();

			final Bundle bundle = FrameworkUtil.getBundle(CustomReportsRegistryHook.class);

			final IContributor contributor = ContributorFactoryOSGi.createContributor(bundle);
	
			CustomReportsRegistry.getInstance().regenerateReportsPluginXMLFile();
			
			final String pluginXMLPath = CustomReportsRegistry.getReportsPluginXMLPath();
			
			File pluginXMLFile = new File(pluginXMLPath);
			
			if (pluginXMLFile.exists()) {
				try (InputStream is = new FileInputStream(pluginXMLFile)) {
					extensionRegistry.addContribution(is, contributor, false, null, null, token);
				} catch (final Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			else {
				log.warn("Could not open custom reports plugin.xml file in: "+pluginXMLPath);
			}
		}
	}
	
	public static void initialisePluginXMLEnablements() {
		final Injector injector = Guice.createInjector(new CustomReportsModule());
		final CustomReportsRegistryHook hook = new CustomReportsRegistryHook();
		injector.injectMembers(hook);
		hook.registerEnablements();
	}
}
