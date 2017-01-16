/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public enum PricingEventType {
	START_OF_LOAD, START_OF_LOAD_WINDOW, END_OF_LOAD, END_OF_LOAD_WINDOW, START_OF_DISCHARGE, START_OF_DISCHARGE_WINDOW, END_OF_DISCHARGE, END_OF_DISCHARGE_WINDOW, DATE_SPECIFIED
}
