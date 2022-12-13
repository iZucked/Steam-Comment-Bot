/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.evaluation;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequencesAttributesProvider;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselStartState;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class CheckingVoyagePlanEvaluator implements IVoyagePlanEvaluator {
	private static final Logger LOG = LoggerFactory.getLogger(CheckingVoyagePlanEvaluator.class);

	private final @NonNull IVoyagePlanEvaluator reference;
	private final @NonNull IVoyagePlanEvaluator delegate;

	private long delegateSeconds = 0L;
	private long referenceSeconds = 0L;
	private long counter = 0L;

	public CheckingVoyagePlanEvaluator(final @NonNull IVoyagePlanEvaluator reference, final @NonNull IVoyagePlanEvaluator delegate) {
		super();
		this.reference = reference;
		this.delegate = delegate;
	}

	private void check() {
		if (counter++ > 50000) {
			System.out.printf("Delegate %,d - Reference %,d -- Saved %,d (%,.2f%%)\n", delegateSeconds, referenceSeconds, referenceSeconds - delegateSeconds,
					(double) (referenceSeconds - delegateSeconds) / (double) referenceSeconds * 100.0);
			counter = 0;
		}
	}

	@Override
	public @NonNull ImmutableList<@NonNull ScheduledVoyagePlanResult> evaluateRoundTrip(@NonNull IResource resource, @NonNull IVesselCharter vesselCharter,
			@NonNull ICharterCostCalculator charterCostCalculator, @NonNull IPortTimesRecord portTimesRecord, boolean returnAll, boolean keepDetails,
			ISequencesAttributesProvider sequencesAttributesProvider, @Nullable IAnnotatedSolution annotatedSolution) {

		return delegate.evaluateRoundTrip(resource, vesselCharter, charterCostCalculator, portTimesRecord, returnAll, keepDetails, sequencesAttributesProvider, annotatedSolution);
	}
	
	public Consumer<List<@NonNull Pair<VoyagePlan, IPortTimesRecord>>> evaluateVoyagePlan(final IResource resource, final IVesselCharter vesselCharter, final VesselStartState vesselStartState, final PreviousHeelRecord previousHeelRecord, final IPortTimesRecord initialPortTimesRecord, final boolean lastPlan, final boolean keepDetails,
			final @Nullable IAnnotatedSolution annotatedSolution, final List<ScheduledVoyagePlanResult> results){
		return delegate.evaluateVoyagePlan(resource, vesselCharter, vesselStartState, previousHeelRecord, initialPortTimesRecord, lastPlan, keepDetails, annotatedSolution, results);
	}

	@Override
	public @NonNull ImmutableList<@NonNull ScheduledVoyagePlanResult> evaluateShipped(@NonNull IResource resource, @NonNull IVesselCharter vesselCharter,
			ICharterCostCalculator charterCostCalculator, VesselStartState vesselStartState, @NonNull PreviousHeelRecord previousHeelRecord, @NonNull IPortTimesRecord portTimesRecord,
			boolean lastPlan, boolean returnAll, boolean keepDetails, ISequencesAttributesProvider sequencesAttributesProvider, @Nullable IAnnotatedSolution annotatedSolution) {

		long a = System.currentTimeMillis();
		final ImmutableList<ScheduledVoyagePlanResult> value_d = delegate.evaluateShipped(resource, vesselCharter, charterCostCalculator, vesselStartState, previousHeelRecord,
				portTimesRecord, lastPlan, returnAll, keepDetails, sequencesAttributesProvider, annotatedSolution);
		long b = System.currentTimeMillis();
		final ImmutableList<ScheduledVoyagePlanResult> value_r = reference.evaluateShipped(resource, vesselCharter, charterCostCalculator, vesselStartState, previousHeelRecord,
				portTimesRecord, lastPlan, returnAll, keepDetails, sequencesAttributesProvider, annotatedSolution);
		long c = System.currentTimeMillis();

		delegateSeconds += (b - a);
		referenceSeconds += (c - b);

		check();

		if (value_d.size() != value_r.size()) {
			LOG.error("Checking VPE Error: (Result list size differs)");
			LOG.error("   reference value:" + value_r.size());
			LOG.error("    delegate value:" + value_d.size());
			throw new RuntimeException("Cache consistency failure");
		} else {
			Iterator<ScheduledVoyagePlanResult> itr_r = value_r.iterator();
			Iterator<ScheduledVoyagePlanResult> itr_d = value_d.iterator();

			while (itr_d.hasNext()) {
				ScheduledVoyagePlanResult v_d = itr_d.next();
				ScheduledVoyagePlanResult v_r = itr_r.next();

				if (!v_d.isEqual(v_r)) {
					LOG.error("Checking VPE Error: (ScheduledVoyagePlanResult differs)");
					throw new RuntimeException("Cache consistency failure");

				}
			}
		}

		return value_d;
	}

	@Override
	public @NonNull ScheduledVoyagePlanResult evaluateNonShipped(@NonNull IResource resource, @NonNull IVesselCharter vesselCharter, @NonNull IPortTimesRecord portTimesRecord,
			boolean keepDetails, ISequencesAttributesProvider sequencesAttributesProvider, @Nullable IAnnotatedSolution annotatedSolution) {
		long a = System.currentTimeMillis();
		final ScheduledVoyagePlanResult value_d = delegate.evaluateNonShipped(resource, vesselCharter, portTimesRecord, keepDetails, sequencesAttributesProvider, annotatedSolution);
		long b = System.currentTimeMillis();
		final ScheduledVoyagePlanResult value_r = reference.evaluateNonShipped(resource, vesselCharter, portTimesRecord, keepDetails, sequencesAttributesProvider, annotatedSolution);
		long c = System.currentTimeMillis();

		delegateSeconds += (b - a);
		referenceSeconds += (c - b);

		check();

		if (!value_d.isEqual(value_r)) {
			LOG.error("Checking VPE Error: (ScheduledVoyagePlanResult differs)");
			throw new RuntimeException("Cache consistency failure");

		}

		return value_d;
	}
}
