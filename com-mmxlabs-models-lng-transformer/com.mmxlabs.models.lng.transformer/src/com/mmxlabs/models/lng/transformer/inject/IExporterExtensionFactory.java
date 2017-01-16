/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject;

import com.mmxlabs.models.lng.transformer.export.IExporterExtension;

/**
 * Factory method to create {@link IExporterExtension} instances.
 * 
 */
public interface IExporterExtensionFactory {

	IExporterExtension createInstance();
}
