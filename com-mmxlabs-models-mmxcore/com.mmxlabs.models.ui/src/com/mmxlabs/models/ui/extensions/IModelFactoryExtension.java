/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.extensions;

import org.ops4j.peaberry.eclipse.ExtensionBean;

import com.mmxlabs.models.ui.modelfactories.IModelFactory;

@ExtensionBean("com.mmxlabs.models.ui.modelfactories")
public interface IModelFactoryExtension {
	public String getID();
	public String getTargetEClass();
	public String isInheritable();
	public IModelFactory createFactory();
}
