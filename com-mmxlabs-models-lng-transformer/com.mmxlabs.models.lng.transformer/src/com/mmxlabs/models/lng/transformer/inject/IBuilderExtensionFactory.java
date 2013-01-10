package com.mmxlabs.models.lng.transformer.inject;

import com.mmxlabs.scheduler.optimiser.builder.IBuilderExtension;

/**
 * @since 2.0
 */
public interface IBuilderExtensionFactory {

	IBuilderExtension createInstance();
}
