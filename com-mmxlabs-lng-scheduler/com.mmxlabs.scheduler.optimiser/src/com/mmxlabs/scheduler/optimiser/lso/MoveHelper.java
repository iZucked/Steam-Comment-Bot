/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Singleton;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintChecker;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.constraints.impl.FOBDESCompatibilityConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PromptRoundTripVesselPermissionConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.VesselEventConstraintChecker;
import com.mmxlabs.scheduler.optimiser.providers.IFOBDESCompatibilityProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPromptPeriodProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRoundTripVesselPermissionProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * Class to check element to vessel resource restrictions. Note this replicates logic from various constraint checks included {@link ResourceAllocationConstraintChecker},
 * {@link FOBDESCompatibilityConstraintChecker}, {@link PromptRoundTripVesselPermissionConstraintChecker} and {@link VesselEventConstraintChecker}. Consider changes API's to allow re-use. E.g. This
 * class becomes used by a single constraint checker OR the constraint checkers offer up an API to check single elements.
 * 
 * @author Simon Goodall
 *
 */
@Singleton
public class MoveHelper {

	@Inject
	private IOptimisationData optimisationData;

	@Inject
	@NonNull
	private IVesselProvider vesselProvider;

	@Inject
	private IOptionalElementsProvider optionalElementsProvider;

	@Inject
	private IResourceAllocationConstraintDataComponentProvider racDCP;

	@Inject
	@NonNull
	private IFOBDESCompatibilityProvider fobDesCompatibilityProvider;

	@Inject
	@NonNull
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IPromptPeriodProvider promptPeriodProvider;

	@Inject
	@NonNull
	private IRoundTripVesselPermissionProvider roundTripVesselPermissionProvider;

	private final Map<ISequenceElement, Collection<IResource>> cachedResult = new ConcurrentHashMap<>();

	private final Function<? super ISequenceElement, ? extends Collection<IResource>> cacheComputeFunction = element -> {
		@Nullable
		Collection<@NonNull IResource> allowedResources = racDCP.getAllowedResources(element);
		if (allowedResources == null) {
			allowedResources = new HashSet<>(optimisationData.getResources());
		} else {
			allowedResources = new HashSet<>(allowedResources);
		}

		final Iterator<@NonNull IResource> itr = allowedResources.iterator();
		while (itr.hasNext()) {
			final @NonNull IResource resource = itr.next();
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

			final @NonNull IPortSlot portSlot = portSlotProvider.getPortSlot(element);
			final @NonNull PortType portType = portSlot.getPortType();
			final @NonNull VesselInstanceType vesselInstanceType = vesselAvailability.getVesselInstanceType();

			if (portType == PortType.DryDock || portType == PortType.Maintenance || portType == PortType.CharterOut) {
				if (!(vesselInstanceType == VesselInstanceType.FLEET || vesselInstanceType == VesselInstanceType.TIME_CHARTER)) {
					itr.remove();
					continue;
				}
			}

			if (vesselInstanceType == VesselInstanceType.ROUND_TRIP) {
				if (!roundTripVesselPermissionProvider.isPermittedOnResource(element, resource)) {
					itr.remove();
					continue;
				}
				if (portType == PortType.Load || portType == PortType.Discharge) {
					final int endOfPromptPeriod = promptPeriodProvider.getEndOfPromptPeriod();
					// If timewindow start is in prompt, then we want to remove the cargo.
					final ITimeWindow timeWindow = portSlot.getTimeWindow();
					if (timeWindow != null && (timeWindow.getInclusiveStart() < endOfPromptPeriod)) {
						itr.remove();
						continue;
					}
				}
			}
			if (vesselInstanceType == VesselInstanceType.FOB_SALE || vesselInstanceType == VesselInstanceType.DES_PURCHASE) {
				if (portType != PortType.Start || portType != PortType.End) {
					if (!fobDesCompatibilityProvider.isPermittedOnResource(element, resource)) {
						itr.remove();
						continue;
					}
				}
			}
		}

		return allowedResources;
	};

	public boolean checkResource(final @NonNull ISequenceElement element, final @Nullable IResource resource) {

		if (resource == null) {
			return optionalElementsProvider.isElementOptional(element);
		}

		final Collection<@NonNull IResource> resources = cachedResult.computeIfAbsent(element, cacheComputeFunction);
		return resources.contains(resource);

	}

	public @NonNull Collection<@NonNull IResource> getAllowedResources(final @NonNull ISequenceElement element) {
		return cachedResult.computeIfAbsent(element, cacheComputeFunction);
	}
}
