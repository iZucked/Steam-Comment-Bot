/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.application;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

@ExtensionBean("com.mmxlabs.rcp.common.ViewPartRewrite")
public interface ViewPartRewriteExtension {

	
	@MapName("oldViewID")
	String getOldViewID();
	
	@MapName("newViewID")
	String getNewViewID();
}
