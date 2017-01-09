/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.viewfactory;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;
import org.osgi.framework.Bundle;

@ExtensionBean("com.mmxlabs.rcp.common.ReplaceableView")
public interface ReplaceableViewExtension {

	Bundle getBundle();

	@MapName("viewId")
	String getViewId();
	
	@MapName("name")
	String getName();

	@MapName("category")
	String getCategory();

	@MapName("icon")
	String getIcon();

	@MapName("class")
	String getDefaultClass();
}
