/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.ui.extensions;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.ui.IComponentHelper;

@ExtensionBean("com.mmxlabs.models.ui.componenthelpers")
public interface IComponentHelperExtension {
	@MapName("id")
	String getID();

	@MapName("helperClass")
	IComponentHelper instantiate();

	@MapName("helperClass")
	String getHelperClass();

	@MapName("modelClass")
	String getEClassName();

	@MapName("inheritable")
	String isInheritable();
}
