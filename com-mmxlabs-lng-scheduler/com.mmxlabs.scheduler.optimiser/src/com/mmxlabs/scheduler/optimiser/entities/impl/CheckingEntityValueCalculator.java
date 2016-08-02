/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities.impl;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator.EvaluationMode;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.CargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class CheckingEntityValueCalculator implements IEntityValueCalculator {
	private static final Logger log = LoggerFactory.getLogger(CheckingEntityValueCalculator.class);

	private final @NonNull IEntityValueCalculator reference;
	private final @NonNull IEntityValueCalculator delegate;

	private long delegateSeconds = 0L;
	private long referenceSeconds = 0L;
	private long counter = 0L;

	public CheckingEntityValueCalculator(final @NonNull IEntityValueCalculator reference, final @NonNull CachingEntityValueCalculator delegate) {
		super();
		this.reference = reference;
		this.delegate = delegate;
	}

	@Override
	public @NonNull Pair<@NonNull CargoValueAnnotation, @NonNull Long> evaluate(EvaluationMode evaluationMode, @NonNull final VoyagePlan plan, @NonNull final IAllocationAnnotation currentAllocation,
			@NonNull final IVesselAvailability vesselAvailability, final int vesselStartTime, @Nullable final VolumeAllocatedSequences volumeAllocatedSequences,
			@Nullable final IAnnotatedSolution annotatedSolution) {

		long a = System.currentTimeMillis();
		final Pair<@NonNull CargoValueAnnotation, @NonNull Long> value_d = delegate.evaluate(evaluationMode, plan, currentAllocation, vesselAvailability, vesselStartTime, volumeAllocatedSequences,
				annotatedSolution);
		long b = System.currentTimeMillis();
		final Pair<@NonNull CargoValueAnnotation, @NonNull Long> value_r = reference.evaluate(evaluationMode, plan, currentAllocation, vesselAvailability, vesselStartTime, volumeAllocatedSequences,
				annotatedSolution);
		long c = System.currentTimeMillis();

		delegateSeconds += (b - a);
		referenceSeconds += (c - b);

		check();

		if (value_d.getSecond().longValue() != value_r.getSecond().longValue()) {

			boolean data = Objects.equals(value_d.getFirst(), value_r.getFirst());
			log.error("Checking EVC Error: (Evaluate cargo P&L differs)");
			log.error("   reference value:" + value_r.getSecond());
			log.error("    delegate value:" + value_d.getSecond());
			throw new RuntimeException("Cache consistency failure");

		}
		if (!Objects.equals(value_d.getFirst(), value_r.getFirst())) {
			log.error("Checking EVC Error: (Evaluate cargo allocation differs)");
			log.error("   reference value:" + value_r.getFirst());
			log.error("    delegate value:" + value_d.getFirst());
			throw new RuntimeException("Cache consistency failure");
		}

		return value_d;
	}

	@Override
	public long evaluate(EvaluationMode evaluationMode, @NonNull final VoyagePlan plan, @NonNull final IVesselAvailability vesselAvailability, final int planStartTime, final int vesselStartTime,
			@Nullable final VolumeAllocatedSequences volumeAllocatedSequences, @Nullable final IAnnotatedSolution annotatedSolution) {

		long a = System.currentTimeMillis();
		final long value_d = delegate.evaluate(evaluationMode, plan, vesselAvailability, planStartTime, vesselStartTime, volumeAllocatedSequences, annotatedSolution);
		long b = System.currentTimeMillis();
		final long value_r = reference.evaluate(evaluationMode, plan, vesselAvailability, planStartTime, vesselStartTime, volumeAllocatedSequences, annotatedSolution);
		long c = System.currentTimeMillis();

		delegateSeconds += (b - a);
		referenceSeconds += (c - b);
		check();
		if (value_d != value_r) {
			log.error("Checking EVC Error: (Evaluate non-cargo P&L differs)");
			log.error("   reference value:" + value_r);
			log.error("    delegate value:" + value_d);
			throw new RuntimeException("Cache consistency failure");
		}

		return value_d;
	}

	@Override
	public long evaluateUnusedSlot(EvaluationMode evaluationMode, @NonNull final IPortSlot portSlot, @Nullable final VolumeAllocatedSequences volumeAllocatedSequences,
			@Nullable final IAnnotatedSolution annotatedSolution) {
		long a = System.currentTimeMillis();
		final long value_d = delegate.evaluateUnusedSlot(evaluationMode, portSlot, volumeAllocatedSequences, annotatedSolution);
		long b = System.currentTimeMillis();
		final long value_r = reference.evaluateUnusedSlot(evaluationMode, portSlot, volumeAllocatedSequences, annotatedSolution);
		long c = System.currentTimeMillis();

		delegateSeconds += (b - a);
		referenceSeconds += (c - b);

		check();

		if (value_d != value_r) {
			log.error("Checking EVC Error: (Unused Slot P&L differs)");
			log.error("   reference value:" + value_r);
			log.error("    delegate value:" + value_d);
			throw new RuntimeException("Cache consistency failure");

		}

		return value_d;
	}

	private void check() {
		if (counter++ > 50000) {
			System.out.printf("Delegate %,d - Reference %,d -- Saved %,d (%,.2f%%)\n", delegateSeconds, referenceSeconds, referenceSeconds - delegateSeconds,
					(double) (referenceSeconds - delegateSeconds) / (double) referenceSeconds * 100.0);
			counter = 0;
		}
	}
}
