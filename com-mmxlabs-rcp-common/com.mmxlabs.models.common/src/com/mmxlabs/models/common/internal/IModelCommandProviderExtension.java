/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.common.internal;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.common.commandservice.IModelCommandProvider;

/**
 * Interface defining an Extension used in Peaberry
 * 
 * @author Simon Goodall
 * 
 */
@ExtensionBean("com.mmxlabs.models.common.modelcommandprovider")
public interface IModelCommandProviderExtension {

	@MapName("id")
	String getID();

	@MapName("commandProvider")
	IModelCommandProvider getModelCommadProvider();
}
