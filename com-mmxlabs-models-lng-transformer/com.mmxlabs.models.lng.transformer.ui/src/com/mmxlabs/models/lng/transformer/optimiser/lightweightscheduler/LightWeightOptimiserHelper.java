package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.transformer.optimiser.common.AbstractOptimiserHelper;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class LightWeightOptimiserHelper extends AbstractOptimiserHelper {
	
	public static long[] getCargoPNL(Long[][] profit, List<List<IPortSlot>> cargoes, List<ILoadOption> loads, List<IDischargeOption> discharges, @NonNull IVesselAvailability pnlVessel, LightWeightCargoDetails[] cargoDetails) {
		long[] pnl = new long[cargoes.size()];
		int idx = 0;
		for (List<IPortSlot> cargo : cargoes) {
			if (cargoDetails[idx].getType() == PortType.CharterOut || cargoDetails[idx].getType() == PortType.DryDock) {
				idx++;
				continue;
			}
			
			pnl[idx++] = profit[loads.indexOf(cargo.get(0))][discharges.indexOf(cargo.get(cargo.size() - 1))]/pnlVessel.getVessel().getCargoCapacity();
		}
		return pnl;
	}
}
