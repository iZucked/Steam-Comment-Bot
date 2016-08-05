package com.mmxlabs.models.lng.transformer.optimiser.valuepair;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;

@NonNullByDefault
@FunctionalInterface
public interface ProfitAndLossRecorder {
	void record(ILoadOption load, IDischargeOption discharge, long profitAndLoss);

}
