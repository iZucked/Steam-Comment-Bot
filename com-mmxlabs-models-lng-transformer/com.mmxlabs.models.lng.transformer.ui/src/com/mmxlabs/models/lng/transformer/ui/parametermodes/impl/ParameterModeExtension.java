/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeCustomiser;

@ExtensionBean("com.mmxlabs.models.lng.transformer.ui.ParameterMode")
public interface ParameterModeExtension {

	@MapName("id")
	@NonNull
	String getID();

	@MapName("name")
	@NonNull
	String getName();

	@MapName("class")
	@NonNull
	IParameterModeCustomiser createCustomiser();
}
