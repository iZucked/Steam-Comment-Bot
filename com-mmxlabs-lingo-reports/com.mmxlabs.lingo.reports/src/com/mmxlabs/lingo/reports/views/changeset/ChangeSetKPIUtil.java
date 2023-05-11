/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.util.HashSet;
import java.util.Set;
import java.util.function.ToLongFunction;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetVesselType;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.OtherPNL;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils.ShippingCostType;

/**
 * Util methods to obtain the required data for the columns in the {@link ChangeSetView}. This class should make it easier to unit test the displayed output is correct without needing to work with the
 * displayed values.
 * 
 * @author Simon Goodall
 *
 */
public class ChangeSetKPIUtil {

	public enum ResultType {
		Before, After
	}

	public enum FlexType {
		TotalIfFlexInsufficient, TotalIfWithinFlex
	}

	public static long getPNL(@Nullable final ChangeSetTableRow tableRow, @NonNull final ResultType type) {
		return getRowProfitAndLossValue(tableRow, type, ScheduleModelKPIUtils::getGroupProfitAndLoss);
	}

	public static long getSalesRevenue(@NonNull final ChangeSetTableRow tableRow, @NonNull final ResultType type) {
		ChangeSetRowData rowData;
		switch (type) {
		case After: {
			rowData = tableRow.getLhsAfter();
			break;
		}
		case Before: {
			rowData = tableRow.getLhsBefore();
			break;
		}
		default:
			throw new IllegalArgumentException();
		}
		// Loop over all rows in the group to get final value. For LDD cargoes there is only one load, so the second row should not have a LHS and we skip the sum
		long sum = 0L;
		if (rowData != null) {
			for (final ChangeSetRowData m : rowData.getRowDataGroup().getMembers()) {
				final SlotAllocation allocation = m.getDischargeAllocation();
				if (allocation != null) {
					sum += allocation.getVolumeValue();
				}
			}
		}
		return sum;
	}

	public static long getPurchaseCost(@NonNull final ChangeSetTableRow tableRow, @NonNull final ResultType type) {
		ChangeSetRowData rowData;
		switch (type) {
		case After: {
			rowData = tableRow.getLhsAfter();
			break;
		}
		case Before: {
			rowData = tableRow.getLhsBefore();
			break;
		}
		default:
			throw new IllegalArgumentException();
		}
		// Loop over all rows in the group to get final value. For LDD cargoes there is only one load, so the second row should not have a LHS and we skip the sum
		long sum = 0L;
		if (rowData != null) {
			for (final ChangeSetRowData m : rowData.getRowDataGroup().getMembers()) {
				final SlotAllocation allocation = m.getLoadAllocation();
				if (allocation != null) {
					sum += allocation.getVolumeValue();
				}
			}
		}
		return sum;
	}

	public static long getAdditionalUpsidePNL(@NonNull final ChangeSetTableRow tableRow, @NonNull final ResultType type) {
		return -getRowProfitAndLossValue(tableRow, type, ScheduleModelKPIUtils::getAdditionalUpsideProfitAndLoss);
	}

	public static long getUpstreamPNL(@NonNull final ChangeSetTableRow tableRow, @NonNull final ResultType type) {

		return getRowProfitAndLossValue(tableRow, type, ScheduleModelKPIUtils::getElementUpstreamPNL);
	}

	public static long getRowProfitAndLossValue(@Nullable final ChangeSetTableRow row, @NonNull final ResultType type, final @NonNull ToLongFunction<@Nullable ProfitAndLossContainer> f) {
		if (row == null) {
			return 0L;
		}

		final Set<ProfitAndLossContainer> rows = new HashSet<>();
		switch (type) {
		case After:
			if (row.getLhsAfter() != null) {
				rows.add(row.getLhsAfter().getLhsGroupProfitAndLoss());
				rows.add(row.getLhsAfter().getOpenLoadAllocation());
			}
			if (row.getCurrentRhsAfter() != null) {
				rows.add(row.getCurrentRhsAfter().getRhsGroupProfitAndLoss());
				rows.add(row.getCurrentRhsAfter().getOpenDischargeAllocation());
			}
			break;
		case Before:
			if (row.getLhsBefore() != null) {
				rows.add(row.getLhsBefore().getLhsGroupProfitAndLoss());
				rows.add(row.getLhsBefore().getOpenLoadAllocation());
			}
			if (row.getCurrentRhsBefore() != null) {
				rows.add(row.getCurrentRhsBefore().getRhsGroupProfitAndLoss());
				rows.add(row.getCurrentRhsBefore().getOpenDischargeAllocation());
			}
			break;
		default:
			throw new IllegalArgumentException();
		}

		long sum = 0L;
		for (final ProfitAndLossContainer c : rows) {
			if (c == null) {
				continue;
			}
			sum += f.applyAsLong(c);
		}
		return sum;
	}

	public static long getAdditionalShippingPNL(@NonNull final ChangeSetTableRow tableRow, @NonNull final ResultType type) {
		return -getRowProfitAndLossValue(tableRow, type, ScheduleModelKPIUtils::getAdditionalShippingProfitAndLoss);
	}

	public static long getShipping(@NonNull final ChangeSetTableRow tableRow, @NonNull final ResultType type) {

		final EventGrouping eventGrouping = getEventGrouping(tableRow, type);
		if (eventGrouping != null) {
			return ScheduleModelKPIUtils.calculateEventShippingCost(eventGrouping, false, true, ShippingCostType.ALL);
		}
		return 0L;
	}

	public static long getShipping(@NonNull final ChangeSetTableRow tableRow, @NonNull final ResultType type, final ShippingCostType shippingCostType) {

		final EventGrouping eventGrouping = getEventGrouping(tableRow, type);
		if (eventGrouping != null) {
			return ScheduleModelKPIUtils.calculateEventShippingCost(eventGrouping, false, true, shippingCostType);
		}
		return 0L;
	}

	public static @Nullable EventGrouping getEventGrouping(final ChangeSetTableRow tableRow, final ResultType type) {

		EventGrouping eventGrouping = null;

		switch (type) {
		case After:
			if (tableRow.getLhsAfter() != null) {
				eventGrouping = tableRow.getLhsAfter().getEventGrouping();
			}
			break;
		case Before:
			if (tableRow.getLhsBefore() != null) {
				eventGrouping = tableRow.getLhsBefore().getEventGrouping();
			}
			break;
		default:
			throw new IllegalArgumentException();
		}
		return eventGrouping;
	}

	public static long getCargoOtherPNL(@NonNull final ChangeSetTableRow tableRow, @NonNull final ResultType type) {

		return getRowProfitAndLossValue(tableRow, type, ScheduleModelKPIUtils::getAdditionalProfitAndLoss) //
				+ getRowProfitAndLossValue(tableRow, type, ScheduleModelKPIUtils::getMiscCostsValue) //
				- getRowProfitAndLossValue(tableRow, type, ScheduleModelKPIUtils::getCancellationFees);
	}

	public static long getTax(@NonNull final ChangeSetTableRow tableRow, @NonNull final ResultType type) {
		return getRowProfitAndLossValue(tableRow, type, ScheduleModelKPIUtils::getGroupProfitAndLoss) - getRowProfitAndLossValue(tableRow, type, ScheduleModelKPIUtils::getGroupPreTaxProfitAndLoss);
	}

	public static long getFlexAvailableInHours(@NonNull final ChangeSetTableRow tableRow, @NonNull final ResultType type, @NonNull final String slotName) {

		final EventGrouping eventGrouping = getEventGrouping(tableRow, type);
		long result = 0;

		if (eventGrouping != null) {
			result = LatenessUtils.getEventGroupingFlexInHours(eventGrouping, slotName);
		}

		return result;
	}

	public static long getLatenessInHours(@NonNull final ChangeSetTableRow tableRow, @NonNull final ResultType type, @NonNull final String slotName) {

		final EventGrouping eventGrouping = getEventGrouping(tableRow, type);
		long result = 0;

		if (eventGrouping != null) {
			result = LatenessUtils.getEventGroupingLatenessInHours(eventGrouping, slotName);
		}

		switch (type) {
		case After:
			if (tableRow.getLhsAfter() != null && slotName.equals(tableRow.getLhsName())) {
				if (tableRow.getLhsAfter().getLhsGroupProfitAndLoss() instanceof OtherPNL otherPNL) {
					result += LatenessUtils.getLatenessInHours(otherPNL);
				}
			}
			if (tableRow.getCurrentRhsAfter() != null && slotName.equals(tableRow.getCurrentRhsName())) {
				if (tableRow.getCurrentRhsAfter().getRhsGroupProfitAndLoss() instanceof OtherPNL otherPNL) {
					result += LatenessUtils.getLatenessInHours(otherPNL);
				}
			}
			break;
		case Before:
			if (tableRow.getLhsBefore() != null && slotName.equals(tableRow.getLhsName())) {
				if (tableRow.getLhsBefore().getLhsGroupProfitAndLoss() instanceof OtherPNL otherPNL) {
					result += LatenessUtils.getLatenessInHours(otherPNL);
				}
			}
			if (tableRow.getCurrentRhsBefore() != null && slotName.equals(tableRow.getCurrentRhsName())) {
				if (tableRow.getCurrentRhsBefore().getRhsGroupProfitAndLoss() instanceof OtherPNL otherPNL) {
					result += LatenessUtils.getLatenessInHours(otherPNL);
				}
			}
			break;
		}

		return result;
	}

	/**
	 * Get the lateness with and without flex e.g. 1 day window, 5 days flex, 3 days late, WithFlex = 3 days, withoutflex = 0. If 6 days late, then WithFlex = Undefined as optimiser doesn't care,
	 * WithoutFlex = 6 days
	 * 
	 * @param tableRow
	 * @param type
	 * @return
	 */
	public static long[] getLateness(@NonNull final ChangeSetTableRow tableRow, @NonNull final ResultType type) {

		final EventGrouping eventGrouping = getEventGrouping(tableRow, type);

		final long[] result = new long[FlexType.values().length];
		if (eventGrouping != null) {
			result[FlexType.TotalIfWithinFlex.ordinal()] = LatenessUtils.getLatenessAfterFlex(eventGrouping);
			result[FlexType.TotalIfFlexInsufficient.ordinal()] = LatenessUtils.getLatenessExcludingFlex(eventGrouping);
		}

		switch (type) {
		case After:
			if (tableRow.getLhsAfter() != null) {
				if (tableRow.getLhsAfter().getLhsGroupProfitAndLoss() instanceof final OtherPNL otherPNL) {
					result[FlexType.TotalIfFlexInsufficient.ordinal()] += LatenessUtils.getLatenessExcludingFlex(otherPNL);
					result[FlexType.TotalIfWithinFlex.ordinal()] += LatenessUtils.getLatenessExcludingFlex(otherPNL);
				}
			}
			break;
		case Before:
			if (tableRow.getLhsBefore() != null) {
				if (tableRow.getLhsBefore().getLhsGroupProfitAndLoss() instanceof final OtherPNL otherPNL) {
					result[FlexType.TotalIfFlexInsufficient.ordinal()] += LatenessUtils.getLatenessExcludingFlex(otherPNL);
					result[FlexType.TotalIfWithinFlex.ordinal()] += LatenessUtils.getLatenessExcludingFlex(otherPNL);
				}
			}
			break;
		}

		return result;
	}

	public static long getViolations(@NonNull final ChangeSetTableRow tableRow, @NonNull final ResultType type) {

		final EventGrouping eventGrouping = getEventGrouping(tableRow, type);

		if (eventGrouping != null) {
			return ScheduleModelKPIUtils.getCapacityViolationCount(eventGrouping);
		}
		return 0L;
	}

	public static long getPNLSum(@NonNull final ChangeSetTableRow tableRow, @NonNull final ResultType type) {

		long pnl = 0L;
		pnl -= getPurchaseCost(tableRow, type);
		pnl += getSalesRevenue(tableRow, type);
		pnl -= getShipping(tableRow, type);
		pnl -= getAdditionalShippingPNL(tableRow, type);
		pnl -= getAdditionalUpsidePNL(tableRow, type);
		pnl += getCargoOtherPNL(tableRow, type);
		pnl += getTax(tableRow, type);
		pnl += getUpstreamPNL(tableRow, type);
		return pnl;
	}

	public static long getNominalVesselCount(@NonNull final ChangeSetTableRow change, @NonNull final ResultType type) {
		switch (type) {
		case Before:
			final ChangeSetVesselType vesselTypeBefore = change.getBeforeVesselType();
			return vesselTypeBefore == ChangeSetVesselType.NOMINAL ? 1 : 0;
		case After:
			final ChangeSetVesselType vesselTypeAfter = change.getAfterVesselType();
			return vesselTypeAfter == ChangeSetVesselType.NOMINAL ? 1 : 0;
		default:
			throw new IllegalArgumentException("Not supported ResultType: " + type.toString());
		}
	}

	public static @Nullable Double getPurchasePrice(@NonNull final ChangeSetTableRow tableRow, @NonNull final ResultType type) {

		ChangeSetRowData rowData;
		switch (type) {
		case After: {
			rowData = tableRow.getLhsAfter();
			break;
		}
		case Before: {
			rowData = tableRow.getLhsBefore();
			break;
		}
		default:
			throw new IllegalArgumentException();
		}
		if (rowData != null) {
			final SlotAllocation allocation = rowData.getLoadAllocation();
			if (allocation != null) {
				return allocation.getPrice();
			}
		}
		return null;
	}

	public static @Nullable Double getSalesPrice(@NonNull final ChangeSetTableRow tableRow, @NonNull final ResultType type) {
		ChangeSetRowData rowData;
		switch (type) {
		case After: {
			rowData = tableRow.getCurrentRhsAfter();
			break;
		}
		case Before: {
			rowData = tableRow.getPreviousRhsBefore();
			break;
		}
		default:
			throw new IllegalArgumentException();
		}
		if (rowData != null) {
			final SlotAllocation allocation = rowData.getDischargeAllocation();
			if (allocation != null) {
				return allocation.getPrice();
			}
		}
		return null;
	}

	public static @Nullable Integer getPurchaseVolume(@NonNull final ChangeSetTableRow tableRow, @NonNull final ResultType type) {

		ChangeSetRowData rowData;
		switch (type) {
		case After: {
			rowData = tableRow.getLhsAfter();
			break;
		}
		case Before: {
			rowData = tableRow.getLhsBefore();
			break;
		}
		default:
			throw new IllegalArgumentException();
		}
		if (rowData != null) {
			final SlotAllocation allocation = rowData.getLoadAllocation();
			if (allocation != null) {
				return allocation.getEnergyTransferred();
			}
		}
		return null;
	}

	public static @Nullable Integer getSalesVolume(@NonNull final ChangeSetTableRow tableRow, @NonNull final ResultType type) {
		ChangeSetRowData rowData;
		switch (type) {
		case After: {
			rowData = tableRow.getCurrentRhsAfter();
			break;
		}
		case Before: {
			rowData = tableRow.getPreviousRhsBefore();
			break;
		}
		default:
			throw new IllegalArgumentException();
		}
		if (rowData != null) {
			final SlotAllocation allocation = rowData.getDischargeAllocation();
			if (allocation != null) {
				return allocation.getEnergyTransferred();
			}
		}
		return null;
	}
}
