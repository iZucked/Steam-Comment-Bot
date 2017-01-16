/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.intro;

import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.mmxlabs.license.features.LicenseFeatures;

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
		IProduct product = Platform.getProduct();
		StringBuilder title = new StringBuilder(product.getName());

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
}
