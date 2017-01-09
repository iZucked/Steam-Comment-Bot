/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.license.features.internal;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;
import org.osgi.framework.Bundle;

@ExtensionBean("com.mmxlabs.license.features.PluginXMLEnablement")
public interface PluginXMLEnablementExtension {

	Bundle getBundle();
	
	@MapName("feature")
	String getFeature();
	
	@MapName("plugin.xml")
	String getPluginXML();
	
	
}
