/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.extensions;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

@ExtensionBean("com.mmxlabs.models.ui.valueproviders")
public interface IReferenceValueProviderExtension {
	@MapName("id") String getID();
	@MapName("ownerEClass") String getOwnerEClassName();
	@MapName("referenceEClass") String getReferenceEClassName();
	@MapName("factory") IReferenceValueProviderFactory instantiate();
}
