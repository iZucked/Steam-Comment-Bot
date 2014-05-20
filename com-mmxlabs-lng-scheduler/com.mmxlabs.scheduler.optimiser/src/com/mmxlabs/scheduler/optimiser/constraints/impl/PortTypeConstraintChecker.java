/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.mmxlabs.optimiser.common.dcproviders.IOrderedSequenceElementsDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * {@link IConstraintChecker} implementation to enforce correct ordering of port types. Specifically:
 * 
 * <pre>
 *  * {@link PortType#Start} can only occur at the start of a {@link ISequence}
 *  * {@link PortType#End} can only occur at the start of a {@link ISequence}
 *  * {@link PortType#Load} must be followed by a {@link PortType#Discharge}
 *  * {@link PortType#Load} cannot be followed by another {@link PortType#Load}
 *  * {@link PortType#Discharge} cannot be followed by another {@link PortType#Discharge}
 *  * {@link PortType#Waypoint} can occur anywhere in the sequence, including between {@link PortType#Load} and {@link PortType#Discharge}.
 *  * {@link PortType#DryDock} and {@link PortType#Other} cannot occur between a {@link PortType#Load} and a {@link PortType#Discharge}.
 *  * {@link PortType#Unknown} should not be seen
 * 
 * @author Simon Goodall
 * 
 */
public final class PortTypeConstraintChecker implements IPairwiseConstraintChecker {

	private final String name;

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IOrderedSequenceElementsDataComponentProvider orderedSequenceProvider;

	/**
	 */
	public PortTypeConstraintChecker(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(final ISequences sequences) {

		return checkConstraints(sequences, null);
	}

	@Override
	public boolean checkConstraints(final ISequences sequences, final List<String> messages) {

		if (portTypeProvider == null) {
			// Cannot check port if there is no port type provider
			return true;
		}

		for (final Map.Entry<IResource, ISequence> entry : sequences.getSequences().entrySet()) {
			if (!checkSequence(entry.getValue(), messages, vesselProvider.getVessel(entry.getKey()).getVesselInstanceType())) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void setOptimisationData(final IOptimisationData optimisationData) {
	}

	/**
	 * Check ISequence for {@link PortType} ordering violations.
	 * 
	 * @param sequence
	 * @param messages
	 * @return
	 */
	public final boolean checkSequence(final ISequence sequence, final List<String> messages, final VesselInstanceType instanceType) {

		if (instanceType == VesselInstanceType.FOB_SALE || instanceType == VesselInstanceType.DES_PURCHASE) {
			int size = sequence.size();
			if (size == 2) {
				PortType ptStart = portTypeProvider.getPortType(sequence.get(0));
				PortType ptEnd = portTypeProvider.getPortType(sequence.get(1));
				return ptStart == PortType.Start && ptEnd == PortType.End;
			}
			// if (size == 3) {
			// PortType ptStart = portTypeProvider.getPortType(sequence.get(0));
			// PortType pt1 = portTypeProvider.getPortType(sequence.get(1));
			// PortType ptEnd = portTypeProvider.getPortType(sequence.get(2));
			// return ptStart == PortType.Start && ptEnd == PortType.End && (pt1 == PortType.Load || pt1 == PortType.Discharge);
			// }
			if (size == 4) {
				PortType ptStart = portTypeProvider.getPortType(sequence.get(0));
				PortType pt1 = portTypeProvider.getPortType(sequence.get(1));
				PortType pt2 = portTypeProvider.getPortType(sequence.get(2));
				PortType ptEnd = portTypeProvider.getPortType(sequence.get(3));
				return ptStart == PortType.Start && ptEnd == PortType.End && pt1 == PortType.Load && pt2 == PortType.Discharge;
			}

			return false;
		}

		boolean seenLoad = false;
		boolean seenDischarge = false;
		ISequenceElement previous = null;
		PortType previousType = null;
		for (final ISequenceElement t : sequence) {
			final PortType type = portTypeProvider.getPortType(t);
			if (previous == null) {
				if (!(((type == PortType.Start) && (instanceType != VesselInstanceType.SPOT_CHARTER)) || ((instanceType == VesselInstanceType.CARGO_SHORTS) && (type == PortType.Load)) || ((instanceType == VesselInstanceType.SPOT_CHARTER) && ((type == PortType.Load) || (type == PortType.End))))) {
					// must either start with Start and be not a spot charter,
					// or must start with a load or an End and be a spot charter

					if (messages != null) {
						messages.add("Sequence must begin with PortType.Start or, if charter, End or Load, but actually begins with " + type + " (for instance type " + instanceType + ")");
					}
					return false;
				}
			} else {
				if (previousType == PortType.End) {
					// Cannot have two elements with an End type.
					if (messages != null) {
						messages.add("Sequence can only have one PortType.End");
					}
					return false;
				}
			}

			// don't enforce any constraints here if the ordered sequence provider specifies a previous element
			final boolean checkConstraint = (orderedSequenceProvider.getPreviousElement(t) == null && (previous == null || orderedSequenceProvider.getNextElement(previous) == null));

			switch (type) {
			case Discharge:
				if (seenDischarge && checkConstraint) {
					// Cannot have two discharges in a row
					if (messages != null) {
						messages.add("Cannot have two PortType.Discharge in a row");
					}
					return false;
				}
				if (!seenLoad && checkConstraint) {
					// Cannot discharge without loading
					if (messages != null) {
						messages.add("Cannot have PortType.Discharge without a PortType.Load");
					}
					return false;
				}
				seenLoad = false;
				seenDischarge = true;
				break;
			case Load:
				if (seenLoad && checkConstraint) {
					// Cannot have two loads in a row
					if (messages != null) {
						messages.add("Cannot have two PortType.Load in a row");
					}
					return false;
				}
				seenLoad = true;
				seenDischarge = false;
				break;
			case Start:
				if (previous != null && checkConstraint) {
					// Start must occur at the start
					if (messages != null) {
						messages.add("PortType.Start must occur at beginning of Sequence");
					}
					return false;
				}
				break;
			case Waypoint:
				break;
			case CharterOut:
			case Short_Cargo_End:
			case DryDock:
			case Maintenance:
			case Other:
			case Virtual:
				if (seenLoad && checkConstraint) {
					// Cannot insert between load and discharge
					if (messages != null) {
						messages.add("Cannot insert " + type + " between PortType.Load and PortType.Discharge");
					}
					return false;
				}
				break;
			case End:
				if (seenLoad && checkConstraint) {
					// Need a discharge
					if (messages != null) {
						messages.add("Cannot leave unused Need to PortType.Load");
					}
					return false;
				}
				break;
			default:
				if (messages != null) {
					messages.add("Unsupported PortType");
				}
				// Unsupported type
				return false;

			}

			previous = t;
			previousType = type;
		}

		if ((instanceType == VesselInstanceType.CARGO_SHORTS && !(previousType == null || previousType == PortType.Short_Cargo_End))
				|| (instanceType != VesselInstanceType.CARGO_SHORTS && previousType != PortType.End)) {
			// Must end with an End type.
			if (messages != null) {
				messages.add("Sequence must end with PortType.End");
			}
			return false;
		}

		return true;
	}

	public IPortTypeProvider getPortTypeProvider() {
		return portTypeProvider;
	}

	public void setPortTypeProvider(final IPortTypeProvider portTypeProvider) {
		this.portTypeProvider = portTypeProvider;
	}

	public IVesselProvider getVesselProvider() {
		return vesselProvider;
	}

	public void setVesselProvider(final IVesselProvider vesselProvider) {
		this.vesselProvider = vesselProvider;
	}

	@Override
	public boolean checkPairwiseConstraint(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		final PortType firstType = portTypeProvider.getPortType(first);
		final PortType secondType = portTypeProvider.getPortType(second);

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
			return false;
		}

		// Check Virtual Routes
		final IVessel vessel = vesselProvider.getVessel(resource);
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

			return false;
		}

		// Discharge must follow a load
		if (firstType != PortType.Load && secondType == PortType.Discharge) {
			return false;
		}
		//
		if (secondType != PortType.Discharge && firstType == PortType.Load) {
			return false;
		}
		return true;
	}

	@Override
	public String explain(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		final PortType firstType = portTypeProvider.getPortType(first);
		final PortType secondType = portTypeProvider.getPortType(second);

		// check the legality of this sequencing decision
		// End can't come before anything and Start can't come after anything
		if (firstType.equals(PortType.End) || secondType.equals(PortType.Start)) {
			return "End can't come before anything and Start can't come after anything";
		}

		if (firstType.equals(PortType.Start) && secondType.equals(PortType.Discharge)) {
			// first port should be a load slot (TODO is this true?)
			return "Need a discharge after a Start";
		}

		// load must precede discharge or waypoint, but nothing else
		if (firstType.equals(PortType.Load)) {

			if (!(secondType.equals(PortType.Discharge) || secondType.equals(PortType.Waypoint))) {
				return "Discharge or Waypoint must follow a Load";
			}
		}

		// discharge may precede anything but Discharge (and start, but we
		// already did that)
		if (firstType.equals(PortType.Discharge) && secondType.equals(PortType.Discharge)) {
			return "Discharge cannot follow a discharge";
		}

		// Check Virtual Routes
		final IVessel vessel = vesselProvider.getVessel(resource);
		if (vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE || vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE) {
			if (firstType.equals(PortType.Discharge)) {
				return "Nothing can come after a discharge on a virtual vessel";
			}
			if (secondType.equals(PortType.Load)) {
				return "Nothing can come before a load on a virtual vessel";
			}
		}

		return null;
	}
}
