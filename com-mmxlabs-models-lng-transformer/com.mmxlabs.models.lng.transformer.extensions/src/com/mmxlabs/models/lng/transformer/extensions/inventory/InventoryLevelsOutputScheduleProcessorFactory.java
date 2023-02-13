/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.inventory;

import com.mmxlabs.models.lng.transformer.IOutputScheduleProcessor;
import com.mmxlabs.models.lng.transformer.inject.IOutputScheduleProcessorFactory;

public class InventoryLevelsOutputScheduleProcessorFactory implements IOutputScheduleProcessorFactory {

	@Override
	public IOutputScheduleProcessor createInstance() {
		return new InventoryLevelsOutputScheduleProcessor();
	}

}
