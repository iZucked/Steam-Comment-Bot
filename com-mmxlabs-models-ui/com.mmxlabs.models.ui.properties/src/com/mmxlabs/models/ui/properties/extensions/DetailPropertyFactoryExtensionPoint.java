/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.properties.extensions;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.ui.properties.factory.IDetailPropertyFactory;

/**
 * Peaberry representation of the com.mmxlabs.models.ui.properties.DetailPropertyFactory extension point.
 * 
 * @author Simon Goodall
 * 
 */
@ExtensionBean("com.mmxlabs.models.ui.properties.DetailPropertyFactory")
public interface DetailPropertyFactoryExtensionPoint {

	@MapName("category")
	String getCategory();

	@MapName("EClass")
	String getEClass();

	@MapName("factory")
	IDetailPropertyFactory createDetailPropertyFactory();

}
