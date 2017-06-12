package com.mmxlabs.scheduler.optimiser.scheduling;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequences;

public interface ITimeWindowScheduler {

	ScheduledTimeWindows schedule(@NonNull ISequences fullSequences);
}
