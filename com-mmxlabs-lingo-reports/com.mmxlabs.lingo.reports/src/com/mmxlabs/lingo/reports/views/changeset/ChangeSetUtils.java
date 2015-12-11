/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.lingo.reports.diff.utils.ScheduleCostUtils;
import com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails;
import com.mmxlabs.models.lng.schedule.CapacityViolationsHolder;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotPNLDetails;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;

public final class ChangeSetUtils {

	public static long getGroupProfitAndLoss(@Nullable final ProfitAndLossContainer profitAndLossContainer) {
		if (profitAndLossContainer == null) {
			return 0L;
		}

		return profitAndLossContainer.getGroupProfitAndLoss().getProfitAndLoss();
	}

	public static long getGroupPreTaxProfitAndLoss(@Nullable final ProfitAndLossContainer profitAndLossContainer) {
		if (profitAndLossContainer == null) {
			return 0L;
		}

		return profitAndLossContainer.getGroupProfitAndLoss().getProfitAndLossPreTax();
	}

	public static long getAdditionalProfitAndLoss(@NonNull final ProfitAndLossContainer profitAndLossContainer) {
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

	public static long getAdditionalShippingProfitAndLoss(@NonNull final ProfitAndLossContainer profitAndLossContainer) {
		long addnPNL = 0;
		for (final GeneralPNLDetails generalPNLDetails : profitAndLossContainer.getGeneralPNLDetails()) {
			if (generalPNLDetails instanceof SlotPNLDetails) {
				final SlotPNLDetails slotPNLDetails = (SlotPNLDetails) generalPNLDetails;
				for (final GeneralPNLDetails details : slotPNLDetails.getGeneralPNLDetails()) {
					if (details instanceof BasicSlotPNLDetails) {
						addnPNL += ((BasicSlotPNLDetails) details).getExtraShippingPNL();
					}
				}
			}
		}
		return addnPNL;
	}

	public static long getAdditionalUpsideProfitAndLoss(@NonNull final ProfitAndLossContainer profitAndLossContainer) {
		long addnPNL = 0;
		for (final GeneralPNLDetails generalPNLDetails : profitAndLossContainer.getGeneralPNLDetails()) {
			if (generalPNLDetails instanceof SlotPNLDetails) {
				final SlotPNLDetails slotPNLDetails = (SlotPNLDetails) generalPNLDetails;
				for (final GeneralPNLDetails details : slotPNLDetails.getGeneralPNLDetails()) {
					if (details instanceof BasicSlotPNLDetails) {
						addnPNL += ((BasicSlotPNLDetails) details).getExtraUpsidePNL();
					}
				}
			}
		}
		return addnPNL;
	}

	public static long getLatenessAfterFlex(@Nullable final EventGrouping eventGrouping) {
		long lateness = 0;
		if (eventGrouping != null) {

			for (final Event evt : eventGrouping.getEvents()) {
				if (evt instanceof PortVisit) {
					final PortVisit visit = (PortVisit) evt;
					final boolean isLate = LatenessUtils.isLateAfterFlex(visit);
					if (isLate) {
						lateness += LatenessUtils.getLatenessInHours(visit);
					}
				}
			}
		}
		return lateness;
	}
	public static long getLatenessExcludingFlex(@Nullable final EventGrouping eventGrouping) {
		long lateness = 0;
		if (eventGrouping != null) {
			
			for (final Event evt : eventGrouping.getEvents()) {
				if (evt instanceof PortVisit) {
					final PortVisit visit = (PortVisit) evt;
					final boolean isLate = LatenessUtils.isLateExcludingFlex(visit);
					if (isLate) {
						lateness += LatenessUtils.getLatenessInHours(visit);
					}
				}
			}
		}
		return lateness;
	}

	public static long getCapacityViolationCount(@Nullable final EventGrouping eventGrouping) {
		long violations = 0;
		if (eventGrouping != null) {
			for (final Event evt : eventGrouping.getEvents()) {
				if (evt instanceof SlotVisit) {
					if (evt instanceof CapacityViolationsHolder) {
						final CapacityViolationsHolder capacityViolationsHolder = (CapacityViolationsHolder) evt;
						violations += capacityViolationsHolder.getViolations().size();
					}
				}
			}
		}
		return violations;
	}

	public static long getTotalShippingCost(@NonNull final EventGrouping eventGrouping) {
		return ScheduleCostUtils.calculateLegCost(eventGrouping);
	}

	public static long getTotalShippingCost(@NonNull final CargoAllocation cargoAllocation) {
		long shippingCost = 0;
		for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
			shippingCost += ScheduleCostUtils.calculateLegCost(cargoAllocation, slotAllocation);
		}
		return shippingCost;
	}
}
