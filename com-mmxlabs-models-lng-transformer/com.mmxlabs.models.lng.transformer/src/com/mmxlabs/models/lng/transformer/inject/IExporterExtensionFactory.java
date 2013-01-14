/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject;

import com.mmxlabs.models.lng.transformer.export.IExporterExtension;

/**
 * Factory method to create {@link IExporterExtension} instances.
 * 
 * @since 2.0
 */
public interface IExporterExtensionFactory {

	IExporterExtension createInstance();
}
