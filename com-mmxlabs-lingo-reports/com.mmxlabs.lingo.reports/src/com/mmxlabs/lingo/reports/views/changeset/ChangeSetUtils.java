package com.mmxlabs.lingo.reports.views.changeset;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lingo.reports.diff.utils.ScheduleCostUtils;
import com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotPNLDetails;

public final class ChangeSetUtils {

	public static long getGroupProfitAndLoss(@NonNull ProfitAndLossContainer profitAndLossContainer) {
		return profitAndLossContainer.getGroupProfitAndLoss().getProfitAndLoss();
	}

	public static long getAdditionalProfitAndLoss(@NonNull ProfitAndLossContainer profitAndLossContainer) {
		long addnPNL = 0;
		for (final GeneralPNLDetails generalPNLDetails : profitAndLossContainer.getGeneralPNLDetails()) {
			if (generalPNLDetails instanceof SlotPNLDetails) {
				final SlotPNLDetails slotPNLDetails = (SlotPNLDetails) generalPNLDetails;
				for (final GeneralPNLDetails details : slotPNLDetails.getGeneralPNLDetails()) {
					if (details instanceof BasicSlotPNLDetails) {
						addnPNL += ((BasicSlotPNLDetails) details).getAdditionalPNL();
					}
				}
			}
		}
		return addnPNL;
	}

	public static long getTotalShippingCost(@NonNull EventGrouping eventGrouping) {
		return ScheduleCostUtils.calculateLegCost(eventGrouping);
	}

	public static long getTotalShippingCost(@NonNull CargoAllocation cargoAllocation) {
		long shippingCost = 0;
		for (SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
			shippingCost += ScheduleCostUtils.calculateLegCost(cargoAllocation, slotAllocation);
		}
		return shippingCost;
	}
}
