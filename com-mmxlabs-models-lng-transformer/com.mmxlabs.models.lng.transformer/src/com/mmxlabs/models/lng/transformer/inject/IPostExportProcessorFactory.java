/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject;

import com.mmxlabs.models.lng.transformer.IPostExportProcessor;

/**
 */
public interface IPostExportProcessorFactory {

	IPostExportProcessor createInstance();
}
