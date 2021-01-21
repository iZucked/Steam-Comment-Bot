/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject;

import com.mmxlabs.scheduler.optimiser.builder.IBuilderExtension;

/**
 */
public interface IBuilderExtensionFactory {

	IBuilderExtension createInstance();
}
