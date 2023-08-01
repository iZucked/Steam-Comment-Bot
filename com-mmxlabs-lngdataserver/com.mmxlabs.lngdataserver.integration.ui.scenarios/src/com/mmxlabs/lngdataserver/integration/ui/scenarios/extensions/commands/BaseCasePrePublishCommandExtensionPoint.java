/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.commands;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

@ExtensionBean("com.mmxlabs.lngdataserver.integration.ui.scenarios.PrePublishCommand")
public interface BaseCasePrePublishCommandExtensionPoint {
	
	@MapName("class")
	public IBaseCasePrePublishCommandExtension createInstance();
	
}