/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.valuepair;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

@NonNullByDefault
public class ProfitAndLossExtractor implements ResultRecorder {

	private final ProfitAndLossRecorder recorder;

	public ProfitAndLossExtractor(final ProfitAndLossRecorder recorder) {
		this.recorder = recorder;
	}

	@Override
	public void record(final ILoadOption load, final IDischargeOption discharge, final IVesselAvailability vessel, final Pair<IAnnotatedSolution, @NonNull EvaluationState> result) {
		final EvaluationState evaluationState = result.getSecond();
		final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);

		final VoyagePlan voyagePlan = profitAndLossSequences.getVolumeAllocatedSequences().getVoyagePlan(load);
		if (voyagePlan != null) {
			final long value = profitAndLossSequences.getVoyagePlanGroupValue(voyagePlan);
			recorder.record(load, discharge, value);
		} else {
			System.out.printf("Failed Plan Pair %s -> %s\n", load.getId(), discharge.getId());
		}
	}

}
