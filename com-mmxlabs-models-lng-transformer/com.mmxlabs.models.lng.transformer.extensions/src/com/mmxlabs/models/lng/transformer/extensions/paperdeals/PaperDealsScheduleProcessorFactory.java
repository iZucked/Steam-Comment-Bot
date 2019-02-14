/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.paperdeals;

import com.mmxlabs.models.lng.transformer.IOutputScheduleProcessor;
import com.mmxlabs.models.lng.transformer.inject.IOutputScheduleProcessorFactory;

public class PaperDealsScheduleProcessorFactory implements IOutputScheduleProcessorFactory {

	@Override
	public IOutputScheduleProcessor createInstance() {
		return new PaperDealsScheduleProcessor();
	}

}
