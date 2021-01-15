/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.browser.ui.context;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

/**
 */
@ExtensionBean("com.mmxlabs.lngdataserver.browser.ui.DataBrowserContextMenuExtension")
public interface DataBrowserContextMenuExtensionPoint {

	@MapName("class")
	public IDataBrowserContextMenuExtension createInstance();

}
