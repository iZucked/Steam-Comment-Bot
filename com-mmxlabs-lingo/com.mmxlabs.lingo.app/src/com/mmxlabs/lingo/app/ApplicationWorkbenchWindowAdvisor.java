/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.app;

import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.rcp.common.appearance.AppearancePreferencePage;
import com.mmxlabs.rcp.common.locking.WellKnownTriggers;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(final IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	@Override
	public void preWindowOpen() {
		final IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(1280, 1024));

		configurer.setShowProgressIndicator(true);
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
		configurer.setShowPerspectiveBar(true);

		// Get the base product name
		final IProduct product = Platform.getProduct();
		final StringBuilder title = new StringBuilder(product.getName());

		// Append modifiers
		// Old (3.11.8(9?) Style.
		if (LicenseFeatures.isPermitted("features:beta-cores-features")) {
			title.append(" - Cores - Beta");
		} else {
			// Newer style
			if (LicenseFeatures.isPermitted("features:beta-features")) {
				title.append(" - Beta");
			}
			if (LicenseFeatures.isPermitted("features:cores-features")) {
				title.append(" - Cores");
			}
		}
		configurer.setTitle(title.toString());
	}

	@Override
	public void postWindowOpen() {
		super.postWindowOpen();
		// Apply the theme. This is either the default light theme, or the testing mode theme
		{
			// Look up testing mode status
			final var scopedPreferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, "org.eclipse.ui.workbench");
			final String themeId = scopedPreferenceStore.getBoolean(AppearancePreferencePage.PROPERTY_NAME) ? "com.mmxlabs.lingo.app.testing-mode" : "org.eclipse.e4.ui.css.theme.e4_default";

			// Grab the theme engine
			final IWorkbench workbench = PlatformUI.getWorkbench();
			final MApplication application = workbench.getService(MApplication.class);
			final IEclipseContext context = application.getContext();
			final var engine = context.get(IThemeEngine.class);

			// Now apply the theme
			// TODO: What happens if the theme is missing?
			final var l = engine.getThemes();
			for (final var t : l) {
				if (t.getId().equals(themeId)) {
					engine.setTheme(t, true);
					break;
				}
			}
		}
		WellKnownTriggers.WORKSPACE_STARTED.fireTrigger();
	}
}
