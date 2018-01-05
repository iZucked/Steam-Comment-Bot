/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeExtender;

@ExtensionBean("com.mmxlabs.models.lng.transformer.ui.ParameterModeExtender")
public interface ParameterModeExtenderExtension {

	@MapName("extender")
	@NonNull
	IParameterModeExtender createExtender();
}
