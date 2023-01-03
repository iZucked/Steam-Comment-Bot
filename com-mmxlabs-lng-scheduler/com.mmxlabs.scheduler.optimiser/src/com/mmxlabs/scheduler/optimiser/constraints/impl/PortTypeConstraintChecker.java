/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.dcproviders.IOrderedSequenceElementsDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * {@link IConstraintChecker} implementation to enforce correct ordering of port types. Specifically:
 * 
 * <pre>
 * * {@link PortType#Start} can only occur at the start of a {@link ISequence} * {@link PortType#End} can only occur at the start of a {@link ISequence} * {@link PortType#Load} must be followed by a
 * {@link PortType#Discharge} * {@link PortType#Load} cannot be followed by another {@link PortType#Load} * {@link PortType#Discharge} cannot be followed by another {@link PortType#Discharge} *
 * {@link PortType#Waypoint} can occur anywhere in the sequence, including between {@link PortType#Load} and {@link PortType#Discharge}. * {@link PortType#DryDock} and {@link PortType#Other} cannot
 * occur between a {@link PortType#Load} and a {@link PortType#Discharge}. * {@link PortType#Unknown} should not be seen
 * 
 * @author Simon Goodall
 * 
 */
public final class PortTypeConstraintChecker implements IPairwiseConstraintChecker {

	private final @NonNull String name;

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IOrderedSequenceElementsDataComponentProvider orderedSequenceProvider;

	public PortTypeConstraintChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, final List<String> messages) {

		boolean valid = true;

		final Collection<@NonNull IResource> loopResources;
		if (changedResources == null) {
			loopResources = sequences.getResources();
		} else {
			loopResources = changedResources;
		}

		for (final IResource resource : loopResources) {
			final ISequence sequence = sequences.getSequence(resource);
			@NonNull
			final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);
			if (!checkSequence(sequence, messages, vesselCharter.getVesselInstanceType(), vesselCharter.isOptional())) {
				return false;
			}
		}

		return valid;

	}

 
	/**
	 * Check ISequence for {@link PortType} ordering violations.
	 * 
	 * @param sequence
	 * @param messages
	 * @param isOptionalVesselCharter
	 *            TODO
	 * @return
	 */
	public final boolean checkSequence(@NonNull final ISequence sequence, final List<String> messages, @NonNull final VesselInstanceType instanceType,
			final boolean isOptionalVesselCharter) {

		if (instanceType == VesselInstanceType.FOB_SALE || instanceType == VesselInstanceType.DES_PURCHASE) {
			final int size = sequence.size();
			if (size == 2) {
				final PortType ptStart = portTypeProvider.getPortType(sequence.get(0));
				final PortType ptEnd = portTypeProvider.getPortType(sequence.get(1));
				return ptStart == PortType.Start && ptEnd == PortType.End;
			}
			// if (size == 3) {
			// PortType ptStart = portTypeProvider.getPortType(sequence.get(0));
			// PortType pt1 = portTypeProvider.getPortType(sequence.get(1));
			// PortType ptEnd = portTypeProvider.getPortType(sequence.get(2));
			// return ptStart == PortType.Start && ptEnd == PortType.End && (pt1 == PortType.Load || pt1 == PortType.Discharge);
			// }
			if (size == 4) {
				final PortType ptStart = portTypeProvider.getPortType(sequence.get(0));
				final PortType pt1 = portTypeProvider.getPortType(sequence.get(1));
				final PortType pt2 = portTypeProvider.getPortType(sequence.get(2));
				final PortType ptEnd = portTypeProvider.getPortType(sequence.get(3));
				return ptStart == PortType.Start && ptEnd == PortType.End && pt1 == PortType.Load && pt2 == PortType.Discharge;
			}
			if (messages != null)
				messages.add(String.format("%s: Sequence starting with sequence element %s has a wrong size!", this.name, 
					sequence.first() != null ? sequence.first().getName() : "(unknown element)"));
			return false;
		}

		boolean seenLoad = false;
		boolean seenDischarge = false;
		ISequenceElement previous = null;
		PortType previousType = null;
		for (final ISequenceElement t : sequence) {
			final PortType type = portTypeProvider.getPortType(t);
			if (previous == null) {
				if ((instanceType == VesselInstanceType.ROUND_TRIP && !(type == PortType.Load || type == PortType.End))
						|| (instanceType != VesselInstanceType.ROUND_TRIP && (type != PortType.Start))) {
					// must either start with Start and be not a round trip,
					// or must start with a load or an End and be a round trip.

					if (messages != null)
						messages.add(this.name + ": Sequence must begin with PortType. Start or, if roundtrip, End or Load, but actually begins with " + type + " (for instance type " + instanceType + ")");
					return false;
				}
			} else {
				if (previousType == PortType.End) {
					// Cannot have two elements with an End type.
					if (messages != null)
						messages.add(this.name + ": Sequence can only have one PortType.End");
					return false;
				}
			}

			// don't enforce any constraints here if the ordered sequence provider specifies a previous element
			final boolean checkConstraint = (orderedSequenceProvider.getPreviousElement(t) == null && (previous == null || orderedSequenceProvider.getNextElement(previous) == null));

			switch (type) {
			case Discharge:
				if (seenDischarge && checkConstraint) {
					// Cannot have two discharges in a row
					if (messages != null)
						messages.add(this.name + ": Cannot have two PortType.Discharge in a row");
					return false;
				}
				if (!seenLoad && checkConstraint) {
					// Cannot discharge without loading
					if (messages != null)
						messages.add(this.name + ": Cannot have PortType.Discharge without a PortType.Load");
					return false;
				}
				seenLoad = false;
				seenDischarge = true;
				break;
			case Load:
				if (seenLoad && checkConstraint) {
					// Cannot have two loads in a row
					if (messages != null)
						messages.add(this.name + ": Cannot have two PortType.Load in a row");
					return false;
				}
				seenLoad = true;
				seenDischarge = false;
				break;
			case Start:
				if (previous != null && checkConstraint) {
					// Start must occur at the start
					if (messages != null)
						messages.add(this.name + ": PortType.Start must occur at beginning of Sequence");
					return false;
				}
				break;
			case Waypoint:
				break;
			case CharterOut:
			case Round_Trip_Cargo_End:
			case DryDock:
			case Maintenance:
			case Other:
			case Virtual:
				if (seenLoad && checkConstraint) {
					// Cannot insert between load and discharge
					if (messages != null)
						messages.add(this.name + ": Cannot insert " + type + " between PortType.Load and PortType.Discharge");
					return false;
				}
				break;
			case End:
				if (seenLoad && checkConstraint) {
					// Need a discharge
					if (messages != null)
						messages.add(this.name + ": Cannot leave unused Need to PortType.Load");
					return false;
				}
				break;
			default:
				if (messages != null)
					messages.add(this.name + ": Unsupported PortType");
				// Unsupported type
				return false;

			}

			previous = t;
			previousType = type;
		}

		if ((instanceType == VesselInstanceType.ROUND_TRIP && !(previousType == null || previousType == PortType.Round_Trip_Cargo_End))
				|| (instanceType != VesselInstanceType.ROUND_TRIP && previousType != PortType.End)) {
			// Must end with an End type.
			if (messages != null)
				messages.add(this.name + ": Sequence must end with PortType.End");
			return false;
		}

		return true;
	}

	@Override
	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource, final List<String> messages) {
		final PortType firstType = portTypeProvider.getPortType(first);
		final PortType secondType = portTypeProvider.getPortType(second);
		final IVesselCharter vessel = vesselProvider.getVesselCharter(resource);

		// don't enforce any constraints here if the ordered sequence provider specifies a previous element
		if (orderedSequenceProvider.getPreviousElement(second) != null) {
			return true;
		}
		if (orderedSequenceProvider.getNextElement(first) != null) {
			return true;
		}

		// check the legality of this sequencing decision
		// End can't come before anything and Start can't come after anything
		if (firstType.equals(PortType.End) || secondType.equals(PortType.Start)) {
			if (messages != null)
				messages.add(explain(first, second, resource));
			return false;
		}

		// Check Virtual Routes
		if (vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE || vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE) {

			if (firstType == PortType.Start && secondType == PortType.End) {
				return true;
			}
			if (firstType == PortType.Start && secondType == PortType.Load) {
				return true;
			}
			if (firstType == PortType.Load && secondType == PortType.Discharge) {
				return true;
			}
			if (firstType == PortType.Discharge && secondType == PortType.End) {
				return true;
			}
			if (messages != null)
				messages.add(explain(first, second, resource));
			return false;
		}

		// Discharge must follow a load
		if (firstType != PortType.Load && secondType == PortType.Discharge) {
			if (messages != null)
				messages.add(explain(first, second, resource));
			return false;
		}
		//
		if (secondType != PortType.Discharge && firstType == PortType.Load) {
			if (messages != null)
				messages.add(explain(first, second, resource));
			return false;
		}
		final VesselInstanceType instanceType = vesselProvider.getVesselCharter(resource).getVesselInstanceType();
		if (instanceType == VesselInstanceType.ROUND_TRIP) {
			// No Discharge followed by a load permitted here....
			if (firstType == PortType.Discharge && secondType == PortType.Load) {
				if (messages != null)
					messages.add(explain(first, second, resource));
				return false;
			}
		}

		return true;
	}

	@Override
	public String explain(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {
		final PortType firstType = portTypeProvider.getPortType(first);
		final PortType secondType = portTypeProvider.getPortType(second);
		final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);
		final IVessel vessel = vesselCharter.getVessel();

		// check the legality of this sequencing decision
		// End can't come before anything and Start can't come after anything
		if (firstType.equals(PortType.End) || secondType.equals(PortType.Start)) {
			return this.name + ": Vessel " + vessel.getName() + ": End can't come before anything and Start can't come after anything";
		}

		if (firstType.equals(PortType.Start) && secondType.equals(PortType.Discharge)) {
			// first port should be a load slot (TODO is this true?)
			return this.name + ": Vessel " + vessel.getName() + ": Need a discharge after a Start";
		}

		// load must precede discharge or waypoint, but nothing else
		if (firstType.equals(PortType.Load)) {

			if (!(secondType.equals(PortType.Discharge) || secondType.equals(PortType.Waypoint))) {
				return this.name + ": Vessel " + vessel.getName() + ": Discharge or Waypoint must follow a Load";
			}
		}

		// discharge may precede anything but Discharge (and start, but we
		// already did that)
		if (firstType.equals(PortType.Discharge) && secondType.equals(PortType.Discharge)) {
			return this.name + ": Vessel " + vessel.getName() + ": Discharge cannot follow a discharge";
		}

		// Check Virtual Routes
		if (vesselCharter.getVesselInstanceType() == VesselInstanceType.FOB_SALE || vesselCharter.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE) {
			if (firstType.equals(PortType.Discharge)) {
				return this.name + ": Vessel " + vessel.getName() + ": Nothing can come after a discharge on a virtual vessel";
			}
			if (secondType.equals(PortType.Load)) {
				return this.name + ": Vessel " + vessel.getName() + ": Nothing can come before a load on a virtual vessel";
			}
		}

		return null;
	}
}
