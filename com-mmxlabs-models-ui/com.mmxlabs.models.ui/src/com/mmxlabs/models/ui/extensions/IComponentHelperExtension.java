/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.extensions;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.ui.IComponentHelper;

@ExtensionBean("com.mmxlabs.models.ui.componenthelpers")
public interface IComponentHelperExtension {
	@MapName("id") public String getID();
	@MapName("helperClass") public IComponentHelper instantiate();
	@MapName("modelClass") public String getEClassName();
	@MapName("inheritable") public String isInheritable();
}
