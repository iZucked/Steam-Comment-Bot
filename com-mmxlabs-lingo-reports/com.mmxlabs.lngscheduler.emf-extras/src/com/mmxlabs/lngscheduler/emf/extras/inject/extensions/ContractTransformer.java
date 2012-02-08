package com.mmxlabs.lngscheduler.emf.extras.inject.extensions;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.lngscheduler.emf.extras.ITransformerExtension;

/**
 * Java class mapping to extension point schema. Changes to the schema will need to be reflected in this java class.
 */
@ExtensionBean("com.mmxlabs.lngscheduler.transformer")
public interface ContractTransformer {

	public interface ModelClass {
		@MapName("class")
		String getTransformer();
	}

	@MapName("modelclass")
	public ModelClass[] getModelClass();

	@MapName("transformer")
	public ITransformerExtension getTransformer();
}