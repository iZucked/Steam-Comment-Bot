/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.groupedslots;

import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.inject.ITransformerExtensionFactory;

/**
 */
public class GroupedSlotsTransformerFactory implements ITransformerExtensionFactory {

	@Override
	public ITransformerExtension createInstance() {
		return new GroupedSlotsTransformer();
	}

}
