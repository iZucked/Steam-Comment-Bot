package com.mmxlabs.scheduler.optimiser.providers;

import java.time.LocalDate;
import org.eclipse.jdt.annotation.NonNull;

public interface IInternalDateProvider {

	public int convertTime(@NonNull final LocalDate time);
	
}
