/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.viewfactory;

import java.io.ByteArrayInputStream;
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

@SuppressWarnings("restriction")
public class ReplaceableViewManager {

	private static final Logger log = LoggerFactory.getLogger(ReplaceableViewManager.class);

	@Inject
	private IExtensionRegistry extensionRegistry;

	@Inject(optional = true)
	private Iterable<ReplaceableViewExtension> replaceableViews;

	@Inject(optional = true)
	private Iterable<ReplaceableViewFactoryExtension> replaceableViewFactories;
	private static final String templateHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><?eclipse version=\"3.4\"?><plugin>";
	private static final String templateViewExtension = "<extension point=\"org.eclipse.ui.views\"><view class=\"%s\" id=\"%s\" name=\"%s\" category=\"%s\" icon=\"%s\" restorable=\"%s\"></view></extension>";
	private static final String templateFooter = "</plugin>";

	private String createPluginXML(final String viewId, final String name, final String category, final String icon, final String className) {
		final String body = String.format(templateViewExtension, className, viewId, name, category, icon, "true");
		return templateHeader + body + templateFooter;
	}

	public void registerViews() {

		// Dynamically register UI elements.
		if (extensionRegistry instanceof ExtensionRegistry) {
			// This is marked as experimental functionality.
			final Object token = ((ExtensionRegistry) extensionRegistry).getTemporaryUserToken();

			for (final ReplaceableViewExtension ext : replaceableViews) {
				final String name = ext.getName();
				final String viewId = ext.getViewId();
				final String category = ext.getCategory();
				// TODO: Restoreable etc
				String icon = ext.getIcon();
				Bundle declaringBundle = ext.getBundle();
				String className = ext.getDefaultClass();

				if (viewId != null) {
					for (final ReplaceableViewFactoryExtension factoryExt : replaceableViewFactories) {
						if (viewId.equals(factoryExt.getReplaceableViewId())) {
							className = factoryExt.getViewClass();
							declaringBundle = factoryExt.getBundle();
							icon = factoryExt.getIcon();
							break;
						}
					}
				}
				final String pluginXML = createPluginXML(viewId, name, category, icon, className);

				final IContributor contributor = ContributorFactoryOSGi.createContributor(declaringBundle);
				try (final InputStream is = new ByteArrayInputStream(pluginXML.getBytes())) {
					extensionRegistry.addContribution(is, contributor, false, null, null, token);
				} catch (final Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}

	public static void initialiseReplaceableViews() {
		final Injector injector = Guice.createInjector(new ReplaceableViewModule());
		final ReplaceableViewManager hook = new ReplaceableViewManager();
		injector.injectMembers(hook);
		hook.registerViews();
	}

}
