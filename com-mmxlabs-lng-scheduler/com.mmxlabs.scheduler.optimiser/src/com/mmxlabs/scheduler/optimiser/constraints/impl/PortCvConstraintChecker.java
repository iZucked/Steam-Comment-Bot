/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortCVRangeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 */
public class PortCvConstraintChecker extends AbstractPairwiseConstraintChecker {

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IPortProvider portProvider;

	@Inject
	private IActualsDataProvider actualsDataProvider;

	@Inject
	private IPortCVRangeProvider portCVRangeProvider;

	public PortCvConstraintChecker(String name) {
		super(name);
	}

	@Override
	public boolean checkPairwiseConstraint(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		final PortType firstType = portTypeProvider.getPortType(first);
		final PortType secondType = portTypeProvider.getPortType(second);

		// for load / discharge pairs, ensure that the CV value for the load is in any
		// CV range specified at the discharge
//		if (firstType == PortType.Load && secondType == PortType.Discharge) {
//			final ILoadOption loadSlot = (ILoadOption) portSlotProvider.getPortSlot(first);
//			final IDischargeOption dischargeSlot = (IDischargeOption) portSlotProvider.getPortSlot(second);
//			final IPort dischargePort = (IPort) portProvider.getPortForElement(second);
//			// If data is actualised, we do not care
//			//TODO: Do we need this? (it was copied from ContractCvConstraintChecker)
//			if (actualsDataProvider.hasActuals(loadSlot) && actualsDataProvider.hasActuals(dischargeSlot)) {
//				return true;
//			}
//
//			final int cv = loadSlot.getCargoCVValue();
//			System.out.println(portCVRangeProvider);
//			System.out.println(portCVRangeProvider.getPortMinCV(dischargePort));
//			System.out.println(portCVRangeProvider.getPortMaxCV(dischargePort));
//			return (portCVRangeProvider.getPortMinCV(dischargePort) <= cv && portCVRangeProvider.getPortMaxCV(dischargePort) >= cv);
//		}
//
		return true;
	}

	@Override
	public String explain(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		final PortType firstType = portTypeProvider.getPortType(first);
		final PortType secondType = portTypeProvider.getPortType(second);

		// for load / discharge pairs, ensure that the CV value for the load is in any
		// CV range specified at the discharge
		if (firstType == PortType.Load && secondType == PortType.Discharge) {
			final ILoadOption loadSlot = (ILoadOption) portSlotProvider.getPortSlot(first);
			final IPort dischargePort = (IPort) portProvider.getPortForElement(second);

			final int cv = loadSlot.getCargoCVValue();

			final String format = "CV (%d) for load slot %s is %s than %s CV (%d) for port %s.";
			final long minCv = portCVRangeProvider.getPortMinCV(dischargePort);
			final long maxCv = portCVRangeProvider.getPortMinCV(dischargePort);
			if (cv < minCv) {
				return String.format(format, cv, loadSlot.getId(), "less", "minimum", minCv, dischargePort.getName());
			} else if (cv > maxCv) {
				return String.format(format, cv, loadSlot.getId(), "more", "maximum", maxCv, dischargePort.getName());
			}
		}

		return null;
	}

}
