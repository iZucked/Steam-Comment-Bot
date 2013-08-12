/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes.impl;

import org.eclipse.jdt.annotation.Nullable;
import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeCustomiser;

@ExtensionBean("com.mmxlabs.models.lng.transformer.ui.ParameterMode")
public interface ParameterModeExtension {

	@MapName("id")
	@Nullable
	String getID();

	@MapName("name")
	@Nullable
	String getName();

	@MapName("class")
	@Nullable
	IParameterModeCustomiser createCustomiser();
}
