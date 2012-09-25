/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * {@link IConstraintChecker} implementation to enforce load windows are before discharge windows. This does not care about sequencing, the {@link PortTypeConstraintChecker} is left to handle that.
 * 
 * 
 * @author Simon Goodall
 * 
 */
public final class TimeSortConstraintChecker implements IPairwiseConstraintChecker {

	private final String name;

	private final String portTypeKey, portSlotKey, vesselKey;

	private IPortTypeProvider portTypeProvider;
	private IPortSlotProvider portSlotProvider;

	private IVesselProvider vesselProvider;

	public TimeSortConstraintChecker(final String name, final String portTypeKey, final String portSlotKey, final String vesselKey) {
		this.name = name;
		this.portTypeKey = portTypeKey;
		this.portSlotKey = portSlotKey;
		this.vesselKey = vesselKey;
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
			return true;
		}
		if (portSlotProvider == null) {
			return true;
		}
		if (vesselProvider == null) {
			return true;
		}

		for (final Map.Entry<IResource, ISequence> entry : sequences.getSequences().entrySet()) {
			final VesselInstanceType vesselInstanceType = vesselProvider.getVessel(entry.getKey()).getVesselInstanceType();
			if (vesselInstanceType == VesselInstanceType.UNKNOWN || vesselInstanceType == VesselInstanceType.DES_PURCHASE || vesselInstanceType == VesselInstanceType.FOB_SALE) {
				continue;
			}
			if (!checkSequence(entry.getValue(), messages, vesselInstanceType)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void setOptimisationData(final IOptimisationData optimisationData) {

		setPortTypeProvider(optimisationData.getDataComponentProvider(portTypeKey, IPortTypeProvider.class));
		setPortSlotProvider(optimisationData.getDataComponentProvider(portSlotKey, IPortSlotProvider.class));
		setVesselProvider(optimisationData.getDataComponentProvider(vesselKey, IVesselProvider.class));
	}

	/**
	 * Check ISequence for {@link PortType} ordering violations.
	 * 
	 * @param sequence
	 * @param messages
	 * @return
	 */
	public final boolean checkSequence(final ISequence sequence, final List<String> messages, final VesselInstanceType instanceType) {

		ITimeWindow loadTimeWindow = null;

		for (final ISequenceElement t : sequence) {
			final PortType type = portTypeProvider.getPortType(t);

			switch (type) {
			case Load:

				final IPortSlot loadSlot = portSlotProvider.getPortSlot(t);
				loadTimeWindow = loadSlot.getTimeWindow();
				break;
			case Discharge:

				final IPortSlot dischargeSlot = portSlotProvider.getPortSlot(t);
				final ITimeWindow tw = dischargeSlot.getTimeWindow();
				if (loadTimeWindow != null && tw != null) {
					if (tw.getEnd() < loadTimeWindow.getStart()) {
						if (messages != null) {
							messages.add("Discharge window is before load window");
						}
						return false;
					}
				}
				loadTimeWindow = null;
				break;

			default:
				break;
			}

		}

		return true;
	}

	public IPortTypeProvider getPortTypeProvider() {
		return portTypeProvider;
	}

	public void setPortTypeProvider(final IPortTypeProvider portTypeProvider) {
		this.portTypeProvider = portTypeProvider;
	}

	public IPortSlotProvider getPortSlotProvider() {
		return portSlotProvider;
	}

	public void setPortSlotProvider(final IPortSlotProvider portSlotProvider) {
		this.portSlotProvider = portSlotProvider;
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

		if (firstType.equals(PortType.Load) && secondType.equals(PortType.Discharge)) {

			final IPortSlot loadSlot = portSlotProvider.getPortSlot(first);
			final IPortSlot dischargeSlot = portSlotProvider.getPortSlot(second);
			final ITimeWindow loadTimeWindow = loadSlot.getTimeWindow();
			final ITimeWindow dischargeTimeWindow = dischargeSlot.getTimeWindow();
			if (loadTimeWindow != null && dischargeTimeWindow != null) {
				if (dischargeTimeWindow.getEnd() < loadTimeWindow.getStart()) {
					return false;
				}
			}

		}

		return true;
	}

	@Override
	public String explain(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		final PortType firstType = portTypeProvider.getPortType(first);
		final PortType secondType = portTypeProvider.getPortType(second);

		if (firstType.equals(PortType.Load) && secondType.equals(PortType.Discharge)) {

			final IPortSlot loadSlot = portSlotProvider.getPortSlot(first);
			final IPortSlot dischargeSlot = portSlotProvider.getPortSlot(second);
			final ITimeWindow loadTimeWindow = loadSlot.getTimeWindow();
			final ITimeWindow dischargeTimeWindow = dischargeSlot.getTimeWindow();
			if (loadTimeWindow != null && dischargeTimeWindow != null) {
				if (dischargeTimeWindow.getEnd() < loadTimeWindow.getStart()) {
					return "Discharge window is before load window";
				}
			}

		}

		return null;
	}
}
