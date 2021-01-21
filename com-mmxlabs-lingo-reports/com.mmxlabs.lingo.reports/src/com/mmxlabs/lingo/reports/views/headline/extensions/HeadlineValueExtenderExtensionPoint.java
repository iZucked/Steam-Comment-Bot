/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.headline.extensions;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

@ExtensionBean("com.mmxlabs.lingo.reports.HeadlineValueExtender")
public interface HeadlineValueExtenderExtensionPoint {

	@MapName("class")
	IHeadlineValueExtender getExtender();
}
