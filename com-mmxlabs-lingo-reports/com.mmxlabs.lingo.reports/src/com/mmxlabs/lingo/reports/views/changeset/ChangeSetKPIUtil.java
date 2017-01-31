/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.util.HashSet;
import java.util.Set;
import java.util.function.ToLongFunction;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.models.lng.schedule.EventGrouping;
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
		WithoutFlex, WithFlex
	}

	public static long getPNL(@NonNull final ChangeSetTableRow tableRow, @NonNull final ResultType type) {
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
		if (rowData != null) {
			SlotAllocation allocation = rowData.getDischargeAllocation();
			if (allocation != null) {
				return allocation.getVolumeValue();
			}
		}
		return 0L;
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
		if (rowData != null) {
			SlotAllocation allocation = rowData.getLoadAllocation();
			if (allocation != null) {
				return allocation.getVolumeValue();
			}
		}
		return 0L;
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
			if (row.getRhsAfter() != null) {
				rows.add(row.getRhsAfter().getRhsGroupProfitAndLoss());
				rows.add(row.getRhsAfter().getOpenDischargeAllocation());
			}
			break;
		case Before:
			if (row.getLhsBefore() != null) {
				rows.add(row.getLhsBefore().getLhsGroupProfitAndLoss());
				rows.add(row.getLhsBefore().getOpenLoadAllocation());
			}
			if (row.getRhsBefore() != null) {
				rows.add(row.getRhsBefore().getRhsGroupProfitAndLoss());
				rows.add(row.getRhsBefore().getOpenDischargeAllocation());
			}
			break;
		default:
			throw new IllegalArgumentException();
		}
		//
		// final Set<ProfitAndLossContainer> containers = new HashSet<ProfitAndLossContainer>();
		// for (final ChangeSetRowData d : rows) {
		// if (d == null) {
		// continue;
		// }
		// containers.add(d.getLhsGroupProfitAndLoss());
		// containers.add(d.getRhsGroupProfitAndLoss());
		// containers.add(d.getOpenLoadAllocation());
		// containers.add(d.getOpenDischargeAllocation());
		// }

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

	private static @Nullable EventGrouping getEventGrouping(final ChangeSetTableRow tableRow, final ResultType type) {

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
				+ getRowProfitAndLossValue(tableRow, type, ScheduleModelKPIUtils::getHedgeValue) //
				+ getRowProfitAndLossValue(tableRow, type, ScheduleModelKPIUtils::getMiscCostsValue) //
				- getRowProfitAndLossValue(tableRow, type, ScheduleModelKPIUtils::getCancellationFees);
	}

	public static long getTax(@NonNull final ChangeSetTableRow tableRow, @NonNull final ResultType type) {
		return getRowProfitAndLossValue(tableRow, type, ScheduleModelKPIUtils::getGroupProfitAndLoss) - getRowProfitAndLossValue(tableRow, type, ScheduleModelKPIUtils::getGroupPreTaxProfitAndLoss);
	}

	public static long[] getLateness(@NonNull final ChangeSetTableRow tableRow, @NonNull final ResultType type) {

		final EventGrouping eventGrouping = getEventGrouping(tableRow, type);

		final long[] result = new long[FlexType.values().length];
		if (eventGrouping != null) {
			result[FlexType.WithFlex.ordinal()] = LatenessUtils.getLatenessAfterFlex(eventGrouping);
			result[FlexType.WithoutFlex.ordinal()] = LatenessUtils.getLatenessExcludingFlex(eventGrouping);
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

	public static long getPNLSum(@NonNull ChangeSetTableRow tableRow, @NonNull ResultType type) {

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
}
