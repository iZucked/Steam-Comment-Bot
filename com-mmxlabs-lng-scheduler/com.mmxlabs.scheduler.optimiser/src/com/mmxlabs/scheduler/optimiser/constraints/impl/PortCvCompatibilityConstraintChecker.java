/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.InternalNameMapper;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortCVRangeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IElementPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * {@link IPairwiseConstraintChecker} to enforce that the load Cv does not exceed the specified cv range of a discharge port *
 * 
 * @author Alex Churchill
 */
public class PortCvCompatibilityConstraintChecker extends AbstractPairwiseConstraintChecker {

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IElementPortProvider portProvider;

	@Inject
	private IActualsDataProvider actualsDataProvider;

	@Inject
	private IPortCVRangeProvider portCVRangeProvider;

	@Inject
	private InternalNameMapper internalNameMapper;

	public PortCvCompatibilityConstraintChecker(@NonNull final String name) {
		super(name);
	}

	@Override
	public boolean checkPairwiseConstraint(final ISequenceElement first, final ISequenceElement second, final IResource resource, final List<String> messages) {
		final PortType firstType = portTypeProvider.getPortType(first);
		final PortType secondType = portTypeProvider.getPortType(second);

		// for load / discharge pairs, ensure that the CV value for the load is
		// in any CV range specified at the discharge
		if (firstType == PortType.Load && secondType == PortType.Discharge) {
			final ILoadOption loadSlot = (ILoadOption) portSlotProvider.getPortSlot(first);
			final IDischargeOption dischargeSlot = (IDischargeOption) portSlotProvider.getPortSlot(second);
			final IPort dischargePort = portProvider.getPortForElement(second);
			// If data is actualised (i.e. the event has occurred), we do not
			// care
			if (actualsDataProvider.hasActuals(loadSlot) && actualsDataProvider.hasActuals(dischargeSlot)) {
				return true;
			}

			final int cv = loadSlot.getCargoCVValue();
			final boolean result = (portCVRangeProvider.getPortMinCV(dischargePort) <= cv && portCVRangeProvider.getPortMaxCV(dischargePort) >= cv);
			if (!result && messages != null)
				messages.add(explain(first, second, resource));
			return result;
		}

		return true;
	}

	private String explain(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		final PortType firstType = portTypeProvider.getPortType(first);
		final PortType secondType = portTypeProvider.getPortType(second);

		// for load / discharge pairs, ensure that the CV value for the load is
		// in any CV range specified at the discharge
		if (firstType == PortType.Load && secondType == PortType.Discharge) {
			final ILoadOption loadSlot = (ILoadOption) portSlotProvider.getPortSlot(first);
			final IPort dischargePort = portProvider.getPortForElement(second);

			final int cv = loadSlot.getCargoCVValue();

			final String format = "CV (%d) for load slot %s is %s than %s CV (%d) for port %s.";
			final long minCv = portCVRangeProvider.getPortMinCV(dischargePort);
			final long maxCv = portCVRangeProvider.getPortMinCV(dischargePort);
			if (cv < minCv) {
				return String.format(format, cv, internalNameMapper.generateString(loadSlot), "less", "minimum", minCv, internalNameMapper.generateString(dischargePort));
			} else if (cv > maxCv) {
				return String.format(format, cv, internalNameMapper.generateString(loadSlot), "more", "maximum", maxCv, internalNameMapper.generateString(dischargePort));
			}
		}

		return null;
	}

}
