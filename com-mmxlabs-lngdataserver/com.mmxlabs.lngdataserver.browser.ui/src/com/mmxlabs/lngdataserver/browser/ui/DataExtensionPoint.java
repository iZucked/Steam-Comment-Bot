/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.browser.ui;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

@ExtensionBean("com.mmxlabs.lngdataserver.browser.ui.DataBrowserExtension")
public interface DataExtensionPoint {
	
	@MapName("class")
	DataExtension getDataExtension();

}
