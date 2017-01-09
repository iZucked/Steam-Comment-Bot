/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.viewfactory;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;
import org.osgi.framework.Bundle;

@ExtensionBean("com.mmxlabs.rcp.common.ReplaceableViewFactory")
public interface ReplaceableViewFactoryExtension {

	Bundle getBundle();

	@MapName("replaceableViewId")
	String getReplaceableViewId();

	@MapName("class")
	String getViewClass();

	@MapName("icon")
	String getIcon();

}
