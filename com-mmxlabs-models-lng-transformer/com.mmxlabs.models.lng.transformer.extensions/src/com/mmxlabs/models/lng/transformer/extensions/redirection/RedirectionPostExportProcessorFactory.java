/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection;

import com.mmxlabs.models.lng.transformer.IPostExportProcessor;
import com.mmxlabs.models.lng.transformer.inject.IPostExportProcessorFactory;

public class RedirectionPostExportProcessorFactory implements IPostExportProcessorFactory {

	@Override
	public IPostExportProcessor createInstance() {
		return new RedirectionPostExportProcessor();
	}
}