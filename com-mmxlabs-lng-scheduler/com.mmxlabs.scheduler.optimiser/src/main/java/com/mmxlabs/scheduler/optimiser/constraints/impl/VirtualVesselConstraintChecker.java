/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.List;

import javax.inject.Inject;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * Check that each {@link IVessel} with {@link VesselInstanceType} of {@link VesselInstanceType#VIRTUAL VIRTUAL} has a maximum of four elements on its corresponding {@link ISequence}, and that at
 * least one of those elements is an un-shipped port slot (a FOB sale or DES purchase). Other sequencing rules will ensure that the load/discharge pairing is correct. Additionally, it also checks that
 * virtual vessels have the correct elements. A virtual vessel always has the same start and end elements. If used it always has the same FOB or DES slot.
 * 
 * @author hinton
 * 
 */
public class VirtualVesselConstraintChecker implements IPairwiseConstraintChecker {
	private final String name;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IStartEndRequirementProvider startEndProvider;

	@Inject
	private IVirtualVesselSlotProvider virtualVesselSlotProvider;

	/**
	 * @since 2.0
	 */
	public VirtualVesselConstraintChecker(final String name) {
		super();
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
		for (final IResource resource : sequences.getResources()) {
			final IVessel vessel = vesselProvider.getVessel(resource);
			if (vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE || vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE) {
				if (isInvalid(resource, sequences.getSequence(resource))) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Check whether this sequence is valid for a virtual vessel
	 * 
	 * @param resource
	 * 
	 * @param sequence
	 *            - a sequence which should be on a virtual vessel (vessel instance type is VIRTUAL)
	 * @return true if there is a problem with this sequence, false if the sequence is OK.
	 */
	private boolean isInvalid(final IResource resource, final ISequence sequence) {
		if (sequence.size() == 2) {
			return false;
		}
		if (sequence.size() != 4) {
			return true;
		}

		ISequenceElement prevElement = null;
		for (final ISequenceElement element : sequence) {
			if (prevElement != null) {
				if (!checkPairwiseConstraint(prevElement, element, resource)) {
					return true;
				}
				prevElement = element;
			}
		}

		return false;
	}

	@Override
	public void setOptimisationData(final IOptimisationData optimisationData) {
	}

	@Override
	public boolean checkPairwiseConstraint(final ISequenceElement first, final ISequenceElement second, final IResource resource) {

		final IVessel vessel = vesselProvider.getVessel(resource);
		if (vessel.getVesselInstanceType() != VesselInstanceType.FOB_SALE && vessel.getVesselInstanceType() != VesselInstanceType.DES_PURCHASE) {

			if (virtualVesselSlotProvider.getVesselForElement(first) != null) {
				return false;
			}
			if (virtualVesselSlotProvider.getVesselForElement(second) != null) {
				return false;
			}

			return true;
		}

		final PortType firstType = portTypeProvider.getPortType(first);
		final PortType secondType = portTypeProvider.getPortType(second);

		final ISequenceElement startElement = startEndProvider.getStartElement(resource);
		final ISequenceElement endElement = startEndProvider.getEndElement(resource);

		if (first == startElement && second == endElement) {
			return true;
		}

		final ISequenceElement elementForVessel = virtualVesselSlotProvider.getElementForVessel(vessel);

		final PortType elementForVesselType = portTypeProvider.getPortType(elementForVessel);
		if (elementForVesselType == PortType.Load) {
			// DES Purchase
			if (firstType == PortType.Start) {
				if (first != startElement) {
					return false;
				}
				if (second != elementForVessel) {
					return false;
				}
				return true;
			}
			if (firstType == PortType.Load) {
				if (first != elementForVessel) {
					return false;
				}
				if (secondType != PortType.Discharge) {
					return false;
				}
				return true;
			}
			if (firstType == PortType.Discharge) {
				if (second != endElement) {
					return false;
				}
				return true;
			}
		} else if (elementForVesselType == PortType.Discharge) {
			// FOB Sale
			if (firstType == PortType.Start) {
				if (first != startElement) {
					return false;
				}
				if (secondType != PortType.Load) {
					return false;
				}
				return true;
			}
			if (firstType == PortType.Load) {
				if (second != elementForVessel) {
					return false;
				}
				return true;
			}
			if (firstType == PortType.Discharge) {
				if (second != endElement) {
					return false;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public String explain(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		// TODO Auto-generated method stub
		return null;
	}
}
