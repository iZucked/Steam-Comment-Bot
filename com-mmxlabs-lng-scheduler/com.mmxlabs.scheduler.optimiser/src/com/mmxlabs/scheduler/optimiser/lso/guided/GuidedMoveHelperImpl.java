package com.mmxlabs.scheduler.optimiser.lso.guided;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.dcproviders.ILockedElementsProvider;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScope;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

@PerChainUnitScope
public class GuidedMoveHelperImpl implements IGuidedMoveHelper {

	@Inject
	@NonNull
	private IResourceAllocationConstraintDataComponentProvider resourceAllocationConstraintDataComponentProvider;

	@Inject
	@NonNull
	private IOptionalElementsProvider optionalElementsProvider;

	@Inject
	@NonNull
	private IPortSlotProvider portSlotProvider;

	@Inject
	@NonNull
	private ILockedElementsProvider lockedElementsProvider;

	@Inject
	private ISequencesManipulator sequencesManipulator;

	@Inject
	private @NonNull List<IConstraintChecker> constraintCheckers;

	@Inject
	private IVesselProvider vesselProvider;

	private final @NonNull List<IResource> vesselResources = new LinkedList<>();
	private final @NonNull List<IResource> desPurchaseResources = new LinkedList<>();
	private final @NonNull List<IResource> fobSaleResources = new LinkedList<>();

//	@Inject
//	@Named("GUIDED_MOVE_HELPER_RANDOM")
	private Random random = new Random(1);

	public void setRandom(Random random) {
		this.random = random;
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

	// public boolean isLockedInSequence(@NonNull final ISequenceElement element) {
	// return false;
	// }

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
	public boolean isSimpleCharterOutEvent(@NonNull final ISequenceElement element) {
		// FIXME: Need a way to tell difference between simple and complex
		// final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
		// return (portSlot.getPortType() == PortType.CharterOut);
		return false;
	}

	@Override
	public boolean isRelocatedCharterOutEvent(@NonNull final ISequenceElement element) {
		final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
		return (portSlot.getPortType() == PortType.CharterOut);
	}

	@Override
	public boolean checkPermittedResource(final ISequenceElement sequenceElement, final IResource resource) {

		final Collection<IResource> allowedResources = resourceAllocationConstraintDataComponentProvider.getAllowedResources(sequenceElement);
		if (allowedResources == null) {
			return true;
		}
		return allowedResources.contains(resource);
	}

	@Override
	public Random getSharedRandom() {
		return random;
	}

	@Override
	public boolean isOptional(@NonNull final ISequenceElement element) {
		return optionalElementsProvider.isElementOptional(element);
	}

	@Override
	public boolean doesMovePassConstraints(final ISequences rawSequences) {

		// Do normal manipulation
		final ISequences movedFullSequences = sequencesManipulator.createManipulatedSequences(rawSequences);

		// Run through the constraint checkers
		for (final IConstraintChecker checker : constraintCheckers) {
			if (!checker.checkConstraints(movedFullSequences, null)) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	@NonNull
	public Collection<IResource> getAllVesselResources() {
		return vesselResources;
	}

	@Override
	public boolean isStrictOptional() {
		return true;
	}

	@Override
	public boolean isStartOrEndSlot(@NonNull ISequenceElement element) {
		final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
		return (portSlot.getPortType() == PortType.Start || portSlot.getPortType() == PortType.End);
	}
}
