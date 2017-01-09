/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.extensions;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.ui.editors.IDisplayCompositeFactory;

/**
 * Peaberry mapping interface for an extension in com.mmxlabs.models.ui.displaycompositefactories
 * @author hinton
 *
 */
@ExtensionBean("com.mmxlabs.models.ui.displaycompositefactories")
public interface IDisplayCompositeFactoryExtension {
	@MapName("id")
	public String getID();
	@MapName("eClass")
	public String getEClassName();
	// there is a bug here in peaberry;;
	@MapName("inheritable")
	public String isInheritable();
	@MapName("factory")
	public IDisplayCompositeFactory instantiate();
}
