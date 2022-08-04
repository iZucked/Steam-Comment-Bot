/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.debug.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.rcp.common.debug.IDebugOptionsProvider;

@ExtensionBean("com.mmxlabs.rcp.common.DebugOptionsProvider")
public interface DebugOptionsExtensionPoint {

	@MapName("cls")
	@NonNull
	IDebugOptionsProvider getDebugOptionsProvider();
}
