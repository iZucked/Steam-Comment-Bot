package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.List;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class ContractCvConstraintChecker implements IPairwiseConstraintChecker {

	private final String name;

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	public ContractCvConstraintChecker(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public boolean checkSequence(final ISequence sequence, final IResource resource) {
		return checkSequence(sequence, resource, null);
	}

	public boolean checkSequence(final ISequence sequence, final IResource resource, final List<String> messages) {
		boolean valid = true;
		ISequenceElement prev = null;
		for (final ISequenceElement current : sequence) {
			if (prev != null) {
				if (!checkPairwiseConstraint(prev, current, resource)) {
					if (messages == null) {
						return false;
					} else {
						valid = false;
						messages.add(explain(prev, current, resource));
					}
				}
			}
			prev = current;
		}
		return valid;
	}

	@Override
	public boolean checkConstraints(final ISequences sequences) {
		return checkConstraints(sequences, null);
	}

	@Override
	public boolean checkConstraints(final ISequences sequences, final List<String> messages) {
		boolean valid = true;

		for (final IResource resource : sequences.getResources()) {
			final ISequence sequence = sequences.getSequence(resource);
			if (!checkSequence(sequence, resource, messages)) {
				if (messages == null) {
					return false;
				} else {
					valid = false;
				}
			}
		}

		return valid;
	}

	@Override
	public boolean checkPairwiseConstraint(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		final PortType firstType = portTypeProvider.getPortType(first);
		final PortType secondType = portTypeProvider.getPortType(second);

		// for load / discharge pairs, ensure that the CV value for the load is in any
		// CV range specified at the discharge
		if (firstType == PortType.Load && secondType == PortType.Discharge) {
			final ILoadOption loadSlot = (ILoadOption) portSlotProvider.getPortSlot(first);
			final IDischargeOption dischargeSlot = (IDischargeOption) portSlotProvider.getPortSlot(second);

			final int cv = loadSlot.getCargoCVValue();

			return (dischargeSlot.getMinCvValue() <= cv && dischargeSlot.getMaxCvValue() >= cv);
		}

		return true;
	}

	@Override
	public String explain(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		final PortType firstType = portTypeProvider.getPortType(first);
		final PortType secondType = portTypeProvider.getPortType(first);

		// for load / discharge pairs, ensure that the CV value for the load is in any
		// CV range specified at the discharge
		if (firstType == PortType.Load && secondType == PortType.Discharge) {
			final ILoadOption loadSlot = (ILoadOption) portSlotProvider.getPortSlot(first);
			final IDischargeOption dischargeSlot = (IDischargeOption) portSlotProvider.getPortSlot(second);

			final int cv = loadSlot.getCargoCVValue();

			final String format = "CV (%d) for load slot %s is %s than %s CV (%d) for discharge slot %s.";
			final long minCv = dischargeSlot.getMinCvValue();
			final long maxCv = dischargeSlot.getMaxCvValue();
			if (cv < minCv) {
				return String.format(format, cv, loadSlot.getId(), "less", "minimum", minCv, dischargeSlot.getId());
			} else if (cv > maxCv) {
				return String.format(format, cv, loadSlot.getId(), "more", "maximum", maxCv, dischargeSlot.getId());
			}
		}

		return null;
	}

	@Override
	public void setOptimisationData(final IOptimisationData optimisationData) {

	}
}
