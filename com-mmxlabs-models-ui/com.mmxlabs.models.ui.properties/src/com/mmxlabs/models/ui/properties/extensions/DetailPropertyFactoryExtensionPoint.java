package com.mmxlabs.models.ui.properties.extensions;

import org.eclipse.emf.ecore.EClass;
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
	EClass getEClass();

	@MapName("factory")
	IDetailPropertyFactory createDetailPropertyFactory();

}
