/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.evaluation;

import java.util.Optional;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.Subscribe;
import com.mmxlabs.optimiser.common.events.OptimisationPhaseEndEvent;
import com.mmxlabs.optimiser.common.events.OptimisationPhaseStartEvent;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequencesAttributesProvider;
import com.mmxlabs.scheduler.optimiser.cache.CacheVerificationFailedException;
import com.mmxlabs.scheduler.optimiser.cache.GeneralCacheSettings;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

public final class CachingVoyagePlanEvaluator implements IVoyagePlanEvaluator {

	private final Random randomForVerification = new Random(0);

	private final LoadingCache<ShippedVoyagePlanCacheKey, Optional<ImmutableList<ScheduledVoyagePlanResult>>> shippedCache;
	private final LoadingCache<NonShippedVoyagePlanCacheKey, Optional<ScheduledVoyagePlanResult>> nonShippedCache;

	private IVoyagePlanEvaluator delegate;

	public CachingVoyagePlanEvaluator(final IVoyagePlanEvaluator delegate, final int concurrencyLevel) {
		super();
		this.delegate = delegate;

		CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder() //
				.concurrencyLevel(concurrencyLevel) //
				.maximumSize(GeneralCacheSettings.VoyagePlanEvaluator_Default_CacheSize);

		if (GeneralCacheSettings.VoyagePlanEvaluator_RecordStats) {
			builder = builder.recordStats();
		}

		this.shippedCache = builder //
				.build(new CacheLoader<ShippedVoyagePlanCacheKey, Optional<ImmutableList<ScheduledVoyagePlanResult>>>() {

					@Override
					public Optional<ImmutableList<ScheduledVoyagePlanResult>> load(final ShippedVoyagePlanCacheKey record) throws Exception {
						final ImmutableList<ScheduledVoyagePlanResult> result = delegate.evaluateShipped(record.resource, record.vesselAvailability, record.charterCostCalculator,
								record.vesselStartTime, //
								record.firstLoadPort, record.previousHeelRecord, record.portTimesRecord, record.lastPlan, //
								false, // Just return the best options
								record.keepDetails, // Keep all the details
								record.sequencesAttributesProvider, null // No annotated solution
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
								record.sequencesAttributesProvider, null // No annotated solution
						);
						return Optional.ofNullable(result);
					}

				});

	}

	@Override
	public @NonNull ImmutableList<@NonNull ScheduledVoyagePlanResult> evaluateRoundTrip(@NonNull final IResource resource, @NonNull final IVesselAvailability vesselAvailability,
			@NonNull final ICharterCostCalculator charterCostCalculator, @NonNull final IPortTimesRecord portTimesRecord, final boolean returnAll, final boolean keepDetails,
			ISequencesAttributesProvider sequencesAttributesProvider, @Nullable final IAnnotatedSolution annotatedSolution) {

		// Default implementation of this method wraps around #evaluateShipped
		return delegate.evaluateRoundTrip(resource, vesselAvailability, charterCostCalculator, portTimesRecord, returnAll, keepDetails, sequencesAttributesProvider, annotatedSolution);
	}

	@Override
	public ImmutableList<ScheduledVoyagePlanResult> evaluateShipped(@NonNull final IResource resource, @NonNull final IVesselAvailability vesselAvailability,
			final ICharterCostCalculator charterCostCalculator, final int vesselStartTime, final IPort firstLoadPort, @NonNull final PreviousHeelRecord previousHeelRecord,
			@NonNull final IPortTimesRecord portTimesRecord, final boolean lastPlan, final boolean returnAll, final boolean keepDetails, ISequencesAttributesProvider sequencesAttributesProvider,
			@Nullable final IAnnotatedSolution annotatedSolution) {

		if (annotatedSolution != null) {
			return delegate.evaluateShipped(resource, vesselAvailability, charterCostCalculator, vesselStartTime, firstLoadPort, previousHeelRecord, portTimesRecord, lastPlan, returnAll, keepDetails,
					sequencesAttributesProvider, annotatedSolution);
		}
		final ShippedVoyagePlanCacheKey key = new ShippedVoyagePlanCacheKey(resource, vesselAvailability, charterCostCalculator, vesselStartTime, firstLoadPort, previousHeelRecord, portTimesRecord,
				lastPlan, keepDetails, sequencesAttributesProvider);

		final Optional<ImmutableList<ScheduledVoyagePlanResult>> optional = shippedCache.getUnchecked(key);

		if (optional.isPresent()) {
			final ImmutableList<ScheduledVoyagePlanResult> actual = optional.get();
			if (GeneralCacheSettings.ENABLE_RANDOM_VERIFICATION && randomForVerification.nextDouble() < GeneralCacheSettings.VERIFICATION_CHANCE) {
				final ImmutableList<ScheduledVoyagePlanResult> expected = delegate.evaluateShipped(resource, vesselAvailability, charterCostCalculator, vesselStartTime, firstLoadPort,
						previousHeelRecord, portTimesRecord, lastPlan, returnAll, keepDetails, sequencesAttributesProvider, annotatedSolution);

				if (expected.size() != actual.size()) {
					throw new CacheVerificationFailedException();
				}
				for (int i = 0; i < expected.size(); ++i) {
					if (!expected.get(i).isEqual(actual.get(i))) {
						throw new CacheVerificationFailedException();
					}
				}
			}

			return actual;
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public @NonNull ScheduledVoyagePlanResult evaluateNonShipped(@NonNull final IResource resource, @NonNull final IVesselAvailability vesselAvailability,
			@NonNull final IPortTimesRecord portTimesRecord, final boolean keepDetails, ISequencesAttributesProvider sequencesAttributesProvider,
			@Nullable final IAnnotatedSolution annotatedSolution) {

		if (annotatedSolution != null) {
			return delegate.evaluateNonShipped(resource, vesselAvailability, portTimesRecord, keepDetails, sequencesAttributesProvider, annotatedSolution);
		}

		final NonShippedVoyagePlanCacheKey key = new NonShippedVoyagePlanCacheKey(resource, vesselAvailability, portTimesRecord, keepDetails, sequencesAttributesProvider);

		final Optional<ScheduledVoyagePlanResult> optional = nonShippedCache.getUnchecked(key);

		if (optional.isPresent()) {

			if (GeneralCacheSettings.ENABLE_RANDOM_VERIFICATION && randomForVerification.nextDouble() < GeneralCacheSettings.VERIFICATION_CHANCE) {
				final ScheduledVoyagePlanResult expected = delegate.evaluateNonShipped(resource, vesselAvailability, portTimesRecord, keepDetails, sequencesAttributesProvider, annotatedSolution);

				if (!expected.isEqual(optional.get())) {
					throw new CacheVerificationFailedException();
				}
			}

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
		if (GeneralCacheSettings.VoyagePlanEvaluator_RecordStats) {
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
