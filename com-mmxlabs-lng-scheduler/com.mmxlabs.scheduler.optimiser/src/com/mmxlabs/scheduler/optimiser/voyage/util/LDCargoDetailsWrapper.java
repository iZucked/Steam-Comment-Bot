/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.util.CargoTypeUtil;
import com.mmxlabs.scheduler.optimiser.components.util.CargoTypeUtil.SimpleCargoType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;

public class LDCargoDetailsWrapper {

	private PortDetails load;
	private VoyageDetails laden;
	private PortDetails discharge;
	private VoyageDetails ballast;

	public static final int IDX_LADEN = 0;
	public static final int IDX_BALLAST = 1;

	public LDCargoDetailsWrapper(IDetailsSequenceElement[] sequence) {
		for (int idx = 0; idx < getSequenceLength(sequence); ++idx) {
			final Object obj = sequence[idx];
			if (obj instanceof PortDetails) {
				final PortDetails portDetails = (PortDetails) obj;
				final int arrayIdx;
				if (idx == 0) {
					// Load!
					arrayIdx = IDX_LADEN;
					setLoad(portDetails);
				} else {
					// Discharge!
					arrayIdx = IDX_BALLAST;
					setDischarge(portDetails);
				}

			} else if (obj instanceof VoyageDetails) {
				final int arrayIdx;
				final VoyageDetails voyageDetails = (VoyageDetails) obj;
				if (voyageDetails.getOptions().getVesselState() == VesselState.Laden) {
					// Laden!
					arrayIdx = IDX_LADEN;
					setLaden(voyageDetails);
				} else {
					// Ballast!
					arrayIdx = IDX_BALLAST;
					setBallast(voyageDetails);
				}
				if (allDetailsFound()) {
					break;
				}
			}
		}
		assert allDetailsFound();
	}

	private int getSequenceLength(IDetailsSequenceElement[] sequence) {
		return (sequence.length == 2) ? 2 : sequence.length - 1;
	}

	private boolean allDetailsFound() {
		if (getLoad() != null && getDischarge() != null) {
			ILoadOption buy;
			IDischargeOption sell;
			if (getLoad().getOptions().getPortSlot() instanceof ILoadOption) {
				buy = (ILoadOption) getLoad().getOptions().getPortSlot();
			} else {
				return false;
			}
			if (getDischarge().getOptions().getPortSlot() instanceof IDischargeOption) {
				sell = (IDischargeOption) getDischarge().getOptions().getPortSlot();
			} else {
				return false;
			}
			SimpleCargoType simpleCargoType = CargoTypeUtil.getSimpleCargoType(buy, sell);
			if (simpleCargoType == SimpleCargoType.DES_PURCHASE || simpleCargoType == SimpleCargoType.FOB_SALE) {
				return true;
			} else if (getDischarge() != null && getBallast() != null) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("null")
	@NonNull
	public PortDetails getLoad() {
		return load;
	}

	public void setLoad(PortDetails load) {
		this.load = load;
	}
	
	public ILoadOption getLoadOption() {
		IPortSlot portSlot = load.getOptions().getPortSlot();
		assert portSlot instanceof ILoadOption;
		return (ILoadOption) portSlot;
	}

	public VoyageDetails getLaden() {
		return laden;
	}

	public void setLaden(VoyageDetails laden) {
		this.laden = laden;
	}

	@SuppressWarnings("null")
	@NonNull
	public PortDetails getDischarge() {
		return discharge;
	}

	public IDischargeOption getDischargeOption() {
		IPortSlot portSlot = discharge.getOptions().getPortSlot();
		assert portSlot instanceof IDischargeOption;
		return (IDischargeOption) portSlot;
	}

	public void setDischarge(PortDetails discharge) {
		this.discharge = discharge;
	}

	public VoyageDetails getBallast() {
		return ballast;
	}

	public void setBallast(VoyageDetails ballast) {
		this.ballast = ballast;
	}
	
	public static final boolean isCargoVoyage(IDetailsSequenceElement[] sequence) {
		if (sequence.length == 5 && sequence[0] instanceof PortDetails && ((PortDetails) sequence[0]).getOptions().getPortSlot() instanceof ILoadOption) {
			return true;
		} else {
			return false;
		}
	}
}
