/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.extensions;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.ui.modelfactories.IModelFactory;

@ExtensionBean("com.mmxlabs.models.ui.modelfactories")
public interface IModelFactoryExtension {
	@MapName("ID")
	public String getID();
	@MapName("targetEClass")
	public String getTargetEClass();
	@MapName("outputEClass")
	public String getOutputEClass();

	/**
	 */
	@MapName("replacementEReference")
	public String getReplacementEReference();
	/**
	 */
	@MapName("replacementEClass")
	public String getReplacementEClass();
	@MapName("label")
	public String getLabel();
	@MapName("factory")
	public IModelFactory createInstance();
}
