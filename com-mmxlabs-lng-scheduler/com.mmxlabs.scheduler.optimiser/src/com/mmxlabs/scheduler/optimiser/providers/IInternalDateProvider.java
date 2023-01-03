/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.time.LocalDate;
import org.eclipse.jdt.annotation.NonNull;

public interface IInternalDateProvider {

	public int convertTime(@NonNull final LocalDate time);
	
}
