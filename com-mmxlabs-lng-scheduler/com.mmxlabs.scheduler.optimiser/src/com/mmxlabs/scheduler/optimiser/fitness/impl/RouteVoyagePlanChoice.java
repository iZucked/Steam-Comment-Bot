/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider.CostType;
import com.mmxlabs.scheduler.optimiser.shared.port.DistanceMatrixEntry;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;

/**
 * Implementation of {@link IVoyagePlanChoice} to change the route used between
 * ports.
 * 
 * @author Simon Goodall
 * 
 */
public final class RouteVoyagePlanChoice implements IVoyagePlanChoice {

	private int choice;

	private final @Nullable VoyageOptions previousOptions;

	private final @NonNull VoyageOptions options;

	private final @NonNull List<@NonNull DistanceMatrixEntry> routeOptions;

	private final @NonNull IRouteCostProvider routeCostProvider;

	private final @NonNull IVessel vessel;

	private int voyageStartTime;

	private int panamaIdleHours;

	public RouteVoyagePlanChoice(@Nullable final VoyageOptions previousOptions, @NonNull final VoyageOptions options, @NonNull final List<@NonNull DistanceMatrixEntry> routeOptions,
			@NonNull final IVessel vessel, int voyageStartTime, @NonNull final IRouteCostProvider routeCostProvider, int panamaIdleHours) {
		this.previousOptions = previousOptions;
		this.options = options;
		this.routeOptions = routeOptions;
		this.vessel = vessel;
		this.voyageStartTime = voyageStartTime;
		this.routeCostProvider = routeCostProvider;
		this.panamaIdleHours = panamaIdleHours;
		assert panamaIdleHours >= 0;
	}

	@Override
	public final boolean reset() {
		for (int i = 0; i < numChoices(); i++) {
			if (apply(i)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public final boolean nextChoice() {
		while (true) {
			if ((choice + 1) == numChoices()) {
				return true;
			}
			if (apply(choice + 1)) {
				return false;
			}
		}
	}

	@Override
	public int numChoices() {
		return routeOptions.size();
	}

	@Override
	public final boolean apply(final int choice) {
		this.choice = choice;

		final DistanceMatrixEntry entry = routeOptions.get(choice);
		final CostType costType;

		VoyageOptions pPreviousOption = previousOptions;
		if (options.getVesselState() == VesselState.Laden) {
			costType = CostType.Laden;
		} else if (previousOptions == null) {
			costType = CostType.Ballast;
		} else {
			// Is it round trip?
			if (pPreviousOption != null) {
				// Needs same route and load and next load port
				if (pPreviousOption.getRoute() == entry.getRoute()) {
					costType = CostType.RoundTripBallast;
				} else {
					costType = CostType.Ballast;
				}
			} else {
				costType = CostType.Ballast;
			}
		}

		final long routeCost = routeCostProvider.getRouteCost(entry.getRoute(), options.getFromPortSlot().getPort(), options.getToPortSlot().getPort(), vessel, voyageStartTime, costType);

		options.setRoute(entry.getRoute(), entry.getDistance(), routeCost, entry.getRoute() == ERouteOption.PANAMA ? panamaIdleHours : 0);

		return true;

	}

	@Override
	public final boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj instanceof RouteVoyagePlanChoice) {

			final RouteVoyagePlanChoice other = (RouteVoyagePlanChoice) obj;

			if (!Objects.equals(routeOptions, other.routeOptions)) {
				return false;
			}

			if (!Objects.equals(options, other.options)) {
				return false;
			}

			return true;
		}

		return false;
	}
}
