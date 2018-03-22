/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

/**
 */
@ExtensionBean("com.mmxlabs.lngdataserver.integration.ui.scenarios.ReportPublisherExtension")
public interface ReportPublisherExtensionPoint {

	@MapName("class")
	public IReportPublisherExtension createInstance();

}
