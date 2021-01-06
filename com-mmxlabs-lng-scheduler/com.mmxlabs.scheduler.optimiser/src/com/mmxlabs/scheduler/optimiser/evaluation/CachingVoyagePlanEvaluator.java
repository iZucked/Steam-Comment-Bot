/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.evaluation;

import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.eventbus.Subscribe;
import com.mmxlabs.optimiser.common.events.OptimisationPhaseEndEvent;
import com.mmxlabs.optimiser.common.events.OptimisationPhaseStartEvent;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

public final class CachingVoyagePlanEvaluator implements IVoyagePlanEvaluator {

	private final LoadingCache<ShippedVoyagePlanCacheKey, Optional<List<ScheduledVoyagePlanResult>>> shippedCache;
	private final LoadingCache<NonShippedVoyagePlanCacheKey, Optional<ScheduledVoyagePlanResult>> nonShippedCache;

	private final boolean recordStats = false;

	private IVoyagePlanEvaluator delegate;

	public CachingVoyagePlanEvaluator(final IVoyagePlanEvaluator delegate, final int cacheSize, final int concurrencyLevel) {
		super();
		this.delegate = delegate;

		CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder() //
				.concurrencyLevel(concurrencyLevel) //
				.maximumSize(cacheSize);

		if (recordStats) {
			builder = builder.recordStats();
		}

		this.shippedCache = builder //
				.build(new CacheLoader<ShippedVoyagePlanCacheKey, Optional<List<ScheduledVoyagePlanResult>>>() {

					@Override
					public Optional<List<ScheduledVoyagePlanResult>> load(final ShippedVoyagePlanCacheKey record) throws Exception {
						final List<ScheduledVoyagePlanResult> result = delegate.evaluateShipped(record.resource, record.vesselAvailability, 
								record.charterCostCalculator, record.vesselStartTime, //
								record.firstLoadPort,
								record.previousHeelRecord, record.portTimesRecord, record.lastPlan, //
								false, // Just return the best options
								record.keepDetails, // Keep all the details
								null // No annotated solution
						);
						return Optional.ofNullable(result);
					}

				});
		this.nonShippedCache = builder //
				.build(new CacheLoader<NonShippedVoyagePlanCacheKey, Optional<ScheduledVoyagePlanResult>>() {
					@Override
					public Optional<ScheduledVoyagePlanResult> load(final NonShippedVoyagePlanCacheKey record) throws Exception {
						final ScheduledVoyagePlanResult result = delegate.evaluateNonShipped(record.resource, record.vesselAvailability, //
								record.portTimesRecord, //
								record.keepDetails, // Keep all the details
								null // No annotated solution
						);
						return Optional.ofNullable(result);
					}

				});

	}

	@Override
	public @NonNull List<@NonNull ScheduledVoyagePlanResult> evaluateRoundTrip(@NonNull IResource resource, @NonNull IVesselAvailability vesselAvailability,
			@NonNull ICharterCostCalculator charterCostCalculator, @NonNull IPortTimesRecord portTimesRecord, boolean returnAll, boolean keepDetails, @Nullable IAnnotatedSolution annotatedSolution) {

		// Default implementation of this method wraps around #evaluateShipped
		return delegate.evaluateRoundTrip(resource, vesselAvailability, charterCostCalculator, portTimesRecord, returnAll, keepDetails, annotatedSolution);
	}

	@Override
	public List<ScheduledVoyagePlanResult> evaluateShipped(@NonNull final IResource resource, @NonNull final IVesselAvailability vesselAvailability, ICharterCostCalculator charterCostCalculator,
			final int vesselStartTime, final IPort firstLoadPort, @NonNull final PreviousHeelRecord previousHeelRecord, @NonNull final IPortTimesRecord portTimesRecord, final boolean lastPlan, final boolean returnAll,
			final boolean keepDetails, @Nullable final IAnnotatedSolution annotatedSolution) {

		if (annotatedSolution != null) {
			return delegate.evaluateShipped(resource, vesselAvailability, charterCostCalculator, vesselStartTime, firstLoadPort, previousHeelRecord, portTimesRecord, lastPlan, returnAll, keepDetails,
					annotatedSolution);
		}
		final ShippedVoyagePlanCacheKey key = new ShippedVoyagePlanCacheKey(resource, vesselAvailability, charterCostCalculator, vesselStartTime, firstLoadPort, previousHeelRecord, portTimesRecord, lastPlan,
				keepDetails);

		final Optional<List<ScheduledVoyagePlanResult>> optional = shippedCache.getUnchecked(key);

		if (optional.isPresent()) {
			return optional.get();
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public @NonNull ScheduledVoyagePlanResult evaluateNonShipped(@NonNull final IResource resource, @NonNull final IVesselAvailability vesselAvailability,
			@NonNull final IPortTimesRecord portTimesRecord, final boolean keepDetails, @Nullable final IAnnotatedSolution annotatedSolution) {

		if (annotatedSolution != null) {
			return delegate.evaluateNonShipped(resource, vesselAvailability, portTimesRecord, keepDetails, annotatedSolution);
		}

		final NonShippedVoyagePlanCacheKey key = new NonShippedVoyagePlanCacheKey(resource, vesselAvailability, portTimesRecord, keepDetails);

		final Optional<ScheduledVoyagePlanResult> optional = nonShippedCache.getUnchecked(key);

		if (optional.isPresent()) {
			return optional.get();
		} else {
			throw new IllegalStateException();
		}
	}

	@Subscribe
	public void startPhase(final OptimisationPhaseStartEvent event) {
		// TODO: Inspect settings and invalidate only if needed
		// E.g. gco on/off
		// E.g. lateness parameter changes.
		clearCaches();
	}

	@Subscribe
	public void endPhase(final OptimisationPhaseEndEvent event) {
		if (recordStats) {
			System.out.println("VPE " + this);
			System.out.println("Non shipped: " + nonShippedCache.stats());
			System.out.println("Shipped: " + shippedCache.stats());
		}
		clearCaches();
	}

	private void clearCaches() {
		nonShippedCache.invalidateAll();
		shippedCache.invalidateAll();
	}
}
