/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject;

import com.mmxlabs.models.lng.transformer.IOutputScheduleProcessor;

/**
 */
public interface IOutputScheduleProcessorFactory {

	IOutputScheduleProcessor createInstance();
}
