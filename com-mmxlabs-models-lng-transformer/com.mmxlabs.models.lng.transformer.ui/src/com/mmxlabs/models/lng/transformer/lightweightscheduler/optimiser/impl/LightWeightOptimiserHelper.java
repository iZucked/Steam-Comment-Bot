/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.transformer.optimiser.common.AbstractOptimiserHelper;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class LightWeightOptimiserHelper extends AbstractOptimiserHelper {
	private static final long VESSEL_EVENT_PNL = 400_000_000_000L;

	public static long[] getCargoPNL(Long[][] profit, List<List<IPortSlot>> cargoes, List<ILoadOption> loads, List<IDischargeOption> discharges, @NonNull IVesselAvailability pnlVessel,
			LightWeightCargoDetails[] cargoDetails) {
		long[] pnl = new long[cargoes.size()];
		int idx = 0;
		for (List<IPortSlot> cargo : cargoes) {
			if (cargoDetails[idx].getType() == PortType.CharterOut //
					|| cargoDetails[idx].getType() == PortType.DryDock //
					|| cargoDetails[idx].getType() == PortType.Maintenance) {
				pnl[idx++] = VESSEL_EVENT_PNL;
			} else {
				if (cargoDetails[idx].getType() == PortType.Load //
						|| cargoDetails[idx].getType() == PortType.Discharge) {
					pnl[idx++] = profit[loads.indexOf(cargo.get(0))][discharges.indexOf(cargo.get(cargo.size() - 1))] / pnlVessel.getVessel().getCargoCapacity();
				}
			}
		}
		return pnl;
	}

	public static ILoadOption getLoadOption(List<IPortSlot> cargo) {
		for (IPortSlot portSlot : cargo) {
			if (portSlot instanceof ILoadSlot) {
				return (ILoadSlot) portSlot;
			}
		}
		return null;
	}

	public static IPortSlot getVesselEvent(List<IPortSlot> cargo) {
		if (cargo.get(0).getPortType() == PortType.CharterOut || cargo.get(0).getPortType() == PortType.DryDock) {
			return cargo.get(0);
		}
		return null;
	}

	public static IDischargeOption getDischargeOption(List<IPortSlot> cargo) {
		if (cargo.get(cargo.size() - 1) instanceof IDischargeSlot) {
			return (IDischargeSlot) cargo.get(cargo.size() - 1);
		}
		return null;
	}

}
