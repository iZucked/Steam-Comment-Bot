/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.extensions;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.lng.transformer.ITransformerExtension;

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

	/**
	 * Returns a new instance of the {@link ITransformerExtension}
	 * @since 2.0
	 */
	@MapName("transformer")
	public ITransformerExtension createTransformer();
}