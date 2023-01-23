/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.extpoint;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

/**
 */
@ExtensionBean("com.mmxlabs.scenario.service.model.WrappedCommandExtension")
public interface WrappedCommandProviderExtensionPoint {

	@MapName("class")
	public IWrappedCommandProvider createInstance();

}
