/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.moves.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Singleton;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintChecker;
import com.mmxlabs.optimiser.common.dcproviders.ILockedElementsProvider;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.constraints.impl.FOBDESCompatibilityConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PromptRoundTripVesselPermissionConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.VesselEventConstraintChecker;
import com.mmxlabs.scheduler.optimiser.providers.IAllowedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IFOBDESCompatibilityProvider;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
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
public class MoveHelper implements IMoveHelper {

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
	private IAllowedVesselProvider allowedVesselProvider;

	@Inject
	@NonNull
	private IFOBDESCompatibilityProvider fobDesCompatibilityProvider;

	@Inject
	private IPromptPeriodProvider promptPeriodProvider;

	@Inject
	@NonNull
	private IRoundTripVesselPermissionProvider roundTripVesselPermissionProvider;

	@Inject
	@NonNull
	private IResourceAllocationConstraintDataComponentProvider resourceAllocationConstraintDataComponentProvider;

	@Inject
	@NonNull
	private IPortSlotProvider portSlotProvider;

	@Inject
	@NonNull
	private INominatedVesselProvider nominatedVesselProvider;

	@Inject
	@NonNull
	private ILockedElementsProvider lockedElementsProvider;

	private final @NonNull List<@NonNull IResource> vesselResources = new LinkedList<>();
	private final @NonNull List<@NonNull IResource> desPurchaseResources = new LinkedList<>();
	private final @NonNull List<@NonNull IResource> fobSaleResources = new LinkedList<>();

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

			final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
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
			if (vesselInstanceType != VesselInstanceType.ROUND_TRIP) {
				IVessel vessel = null;
				IVesselClass vesselClass = null;

				if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.FLEET || vesselAvailability.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER) {
					vessel = vesselAvailability.getVessel();
					vesselClass = vessel.getVesselClass();
				} else if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER || vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
					// vessel = null; // Not a real vessel
					vesselClass = vesselAvailability.getVessel().getVesselClass();
				} else if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {

					vessel = nominatedVesselProvider.getNominatedVessel(resource);
					if (vessel != null) {
						vesselClass = vessel.getVesselClass();
					}
				}

				if (!allowedVesselProvider.isPermittedOnVessel(portSlot, vessel, vesselClass)) {
					itr.remove();
					continue;
				}
			}
		}

		return allowedResources;
	};

	@Override
	public boolean legacyCheckResource(final ISequenceElement sequenceElement, final IResource resource) {

		final Collection<IResource> allowedResources = resourceAllocationConstraintDataComponentProvider.getAllowedResources(sequenceElement);
		if (allowedResources == null) {
			return true;
		}
		return allowedResources.contains(resource);
	}

	@Override
	public boolean checkResource(final @NonNull ISequenceElement element, final @Nullable IResource resource) {

		if (resource == null) {
			return optionalElementsProvider.isElementOptional(element);
		}

		final Collection<@NonNull IResource> resources = cachedResult.computeIfAbsent(element, cacheComputeFunction);
		return resources.contains(resource);

	}

	@Override
	public @NonNull Collection<@NonNull IResource> getAllowedResources(final @NonNull ISequenceElement element) {
		return cachedResult.computeIfAbsent(element, cacheComputeFunction);
	}

	@Inject
	private void processInformation(final IOptimisationData optimisationData) {
		for (final IResource resource : optimisationData.getResources()) {
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
			final VesselInstanceType vesselInstanceType = vesselAvailability.getVesselInstanceType();
			switch (vesselInstanceType) {
			case ROUND_TRIP:
				break;
			case DES_PURCHASE:
				desPurchaseResources.add(resource);
				break;
			case FLEET:
				vesselResources.add(resource);
				break;
			case FOB_SALE:
				fobSaleResources.add(resource);
				break;
			case SPOT_CHARTER:
				vesselResources.add(resource);
				break;
			case TIME_CHARTER:
				vesselResources.add(resource);
				break;
			case UNKNOWN:
				break;
			default:
				break;
			}
		}
	}

	@Override
	public @NonNull IResource getDESPurchaseResource(@NonNull final ISequenceElement desPurchase) {
		final Collection<IResource> allowedResources = resourceAllocationConstraintDataComponentProvider.getAllowedResources(desPurchase);
		assert allowedResources != null && allowedResources.size() == 1;
		return allowedResources.iterator().next();

	}

	@Override
	@NonNull
	public IResource getFOBSaleResource(@NonNull final ISequenceElement fobSale) {
		final Collection<IResource> allowedResources = resourceAllocationConstraintDataComponentProvider.getAllowedResources(fobSale);
		assert allowedResources != null && allowedResources.size() == 1;
		return allowedResources.iterator().next();

	}

	@Override
	public boolean isLockedToVessel(@NonNull final ISequenceElement element) {

		boolean locked = lockedElementsProvider.isElementLocked(element);
		if (!locked) {
			final Collection<IResource> allowedResources = resourceAllocationConstraintDataComponentProvider.getAllowedResources(element);
			if (allowedResources != null && allowedResources.size() == 1) {
				locked = true;
			}
		}
		return locked;
	}

	@Override
	public boolean isLoadSlot(@NonNull final ISequenceElement element) {

		final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
		return portSlot instanceof ILoadOption;
	}

	@Override
	public boolean isDischargeSlot(@NonNull final ISequenceElement element) {

		final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
		return portSlot instanceof IDischargeOption;
	}

	@Override
	public boolean isFOBPurchase(@NonNull final ISequenceElement element) {

		final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
		return portSlot instanceof ILoadSlot;
	}

	@Override
	public boolean isFOBSale(@NonNull final ISequenceElement element) {
		final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
		return (portSlot instanceof IDischargeOption) && !(portSlot instanceof IDischargeSlot);
	}

	@Override
	public boolean isDESPurchase(@NonNull final ISequenceElement element) {
		final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
		return (portSlot instanceof ILoadOption) && !(portSlot instanceof ILoadSlot);
	}

	@Override
	public boolean isDESSale(@NonNull final ISequenceElement element) {
		final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
		return portSlot instanceof IDischargeSlot;
	}

	@Override
	public boolean isVesselEvent(@NonNull final ISequenceElement element) {
		final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
		return portSlot instanceof VesselEventPortSlot;
	}

	@Override
	public boolean isDryDockEvent(@NonNull final ISequenceElement element) {
		final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
		return (portSlot.getPortType() == PortType.DryDock);
	}

	@Override
	public boolean isMaintenanceEvent(@NonNull final ISequenceElement element) {
		final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
		return (portSlot.getPortType() == PortType.Maintenance);
	}

	@Override
	@NonNull
	public Collection<@NonNull IResource> getAllVesselResources() {
		return vesselResources;
	}

	@Override
	public boolean isStartOrEndSlot(@NonNull final ISequenceElement element) {
		final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
		return (portSlot.getPortType() == PortType.Start || portSlot.getPortType() == PortType.End);
	}

	@Override
	public boolean isOptional(@NonNull final ISequenceElement element) {
		return optionalElementsProvider.isElementOptional(element);
	}

	@Override
	public boolean isCharterOutEvent(@NonNull final ISequenceElement element) {
		final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
		return (portSlot.getPortType() == PortType.CharterOut);
	}

	// TODO: -- How to tell?
	@Override
	public boolean isRelocatedCharterOutEvent(@NonNull final ISequenceElement element) {
		final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
		return (portSlot.getPortType() == PortType.CharterOut);
	}

	@Override
	public boolean isSimpleCharterOutEvent(@NonNull final ISequenceElement element) {
		// TODO: -- How to tell?

		// FIXME: Need a way to tell difference between simple and complex
		// final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
		// return (portSlot.getPortType() == PortType.CharterOut);
		return false;
	}

	@Override
	public boolean isNonShippedResource(@NonNull IResource resource) {
		return desPurchaseResources.contains(resource) || fobSaleResources.contains(resource);
	}
}
