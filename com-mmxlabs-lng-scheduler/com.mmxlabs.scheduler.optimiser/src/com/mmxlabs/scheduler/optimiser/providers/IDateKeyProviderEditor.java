/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;


/**
 */
public interface IDateKeyProviderEditor extends IDateKeyProvider {
	
	void setTimeZero(long timeZeroInMillis);
}
