/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;


/**
 * @since 2.0
 */
public interface IDateKeyProviderEditor extends IDateKeyProvider {
	
	void setTimeZero(long timeZeroInMillis);
}
