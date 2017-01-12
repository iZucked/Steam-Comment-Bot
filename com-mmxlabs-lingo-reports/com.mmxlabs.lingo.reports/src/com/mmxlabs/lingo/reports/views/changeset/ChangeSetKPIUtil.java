package com.mmxlabs.lingo.reports.views.changeset;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;

/**
 * Util methods to obtain the required data for the columns in the {@link ChangeSetView}. This class should make it easier to unit test the displayed output is correct without needing to work with the
 * displayed values.
 * 
 * @author Simon Goodall
 *
 */
public class ChangeSetKPIUtil {

	public enum ResultType {
		ORIGINAL, NEW
	}

	public enum FlexType {
		WithoutFlex, WithFlex
	}

	public static long getPNL(@NonNull final ChangeSetRow row, @NonNull final ResultType type) {

		switch (type) {
		case NEW:
			return ChangeSetTransformerUtil.getNewRowProfitAndLossValue(row, ScheduleModelKPIUtils::getGroupProfitAndLoss);
		case ORIGINAL:
			return ChangeSetTransformerUtil.getOriginalRowProfitAndLossValue(row, ScheduleModelKPIUtils::getGroupProfitAndLoss);
		default:
			throw new IllegalArgumentException();
		}
	}

	public static long getSalesRevenue(@NonNull final ChangeSetRow row, @NonNull final ResultType type) {

		switch (type) {
		case NEW: {
			SlotAllocation allocation = row.getNewDischargeAllocation();
			if (allocation != null) {
				return allocation.getVolumeValue();
			}
			return 0L;
		}
		case ORIGINAL: {
			SlotAllocation allocation = row.getOriginalDischargeAllocation();
			if (allocation != null) {
				return allocation.getVolumeValue();
			}
			return 0L;
		}
		default:
			throw new IllegalArgumentException();
		}
	}

	public static long getPurchaseCost(@NonNull final ChangeSetRow row, @NonNull final ResultType type) {

		switch (type) {
		case NEW: {
			SlotAllocation allocation = row.getNewLoadAllocation();
			if (allocation != null) {
				return allocation.getVolumeValue();
			}
			return 0L;
		}
		case ORIGINAL: {
			SlotAllocation allocation = row.getOriginalLoadAllocation();
			if (allocation != null) {
				return allocation.getVolumeValue();
			}
			return 0L;
		}
		default:
			throw new IllegalArgumentException();
		}
	}

	public static long getAdditionalUpsidePNL(@NonNull final ChangeSetRow row, @NonNull final ResultType type) {
		// TODO: Check sign is correct!

		switch (type) {
		case NEW:
			return -ChangeSetTransformerUtil.getNewRowProfitAndLossValue(row, ScheduleModelKPIUtils::getAdditionalUpsideProfitAndLoss);
		case ORIGINAL:
			return -ChangeSetTransformerUtil.getOriginalRowProfitAndLossValue(row, ScheduleModelKPIUtils::getAdditionalUpsideProfitAndLoss);
		default:
			throw new IllegalArgumentException();
		}
	}

	public static long getUpstreamPNL(@NonNull final ChangeSetRow row, @NonNull final ResultType type) {

		switch (type) {
		case NEW:
			return ChangeSetTransformerUtil.getNewRowProfitAndLossValue(row, ScheduleModelKPIUtils::getElementUpstreamPNL);
		case ORIGINAL:
			return ChangeSetTransformerUtil.getOriginalRowProfitAndLossValue(row, ScheduleModelKPIUtils::getElementUpstreamPNL);
		default:
			throw new IllegalArgumentException();
		}
	}

	public static long getAdditionalShippingPNL(@NonNull final ChangeSetRow row, @NonNull final ResultType type) {
		// TODO: Check sign is correct!
		switch (type) {
		case NEW:
			return -ChangeSetTransformerUtil.getNewRowProfitAndLossValue(row, ScheduleModelKPIUtils::getAdditionalShippingProfitAndLoss);
		case ORIGINAL:
			return -ChangeSetTransformerUtil.getOriginalRowProfitAndLossValue(row, ScheduleModelKPIUtils::getAdditionalShippingProfitAndLoss);
		default:
			throw new IllegalArgumentException();
		}
	}

	public static long getShipping(@NonNull final ChangeSetRow row, @NonNull final ResultType type) {

		final EventGrouping eventGrouping;
		switch (type) {
		case NEW:
			eventGrouping = row.getNewEventGrouping();
			break;
		case ORIGINAL:
			eventGrouping = row.getOriginalEventGrouping();
			break;
		default:
			throw new IllegalArgumentException();
		}
		if (eventGrouping != null) {
			return ScheduleModelKPIUtils.calculateEventShippingCost(eventGrouping, false, true);
		}
		return 0L;
	}

	public static long getCargoOtherPNL(@NonNull final ChangeSetRow row, @NonNull final ResultType type) {

		switch (type) {
		case NEW:
			return ChangeSetTransformerUtil.getNewRowProfitAndLossValue(row, ScheduleModelKPIUtils::getAdditionalProfitAndLoss)
					+ ChangeSetTransformerUtil.getNewRowProfitAndLossValue(row, ScheduleModelKPIUtils::getHedgeValue)
					+ ChangeSetTransformerUtil.getNewRowProfitAndLossValue(row, ScheduleModelKPIUtils::getMiscCostsValue)
					- ChangeSetTransformerUtil.getNewRowProfitAndLossValue(row, ScheduleModelKPIUtils::getCancellationFees);
		case ORIGINAL:
			return ChangeSetTransformerUtil.getOriginalRowProfitAndLossValue(row, ScheduleModelKPIUtils::getAdditionalProfitAndLoss)
					+ ChangeSetTransformerUtil.getOriginalRowProfitAndLossValue(row, ScheduleModelKPIUtils::getHedgeValue)
					+ ChangeSetTransformerUtil.getOriginalRowProfitAndLossValue(row, ScheduleModelKPIUtils::getMiscCostsValue)
					- ChangeSetTransformerUtil.getOriginalRowProfitAndLossValue(row, ScheduleModelKPIUtils::getCancellationFees);
		default:
			throw new IllegalArgumentException();
		}
	}

	public static long getTax(@NonNull final ChangeSetRow row, @NonNull final ResultType type) {

		switch (type) {
		case NEW:
			return ChangeSetTransformerUtil.getNewRowProfitAndLossValue(row, ScheduleModelKPIUtils::getGroupProfitAndLoss)
					- ChangeSetTransformerUtil.getNewRowProfitAndLossValue(row, ScheduleModelKPIUtils::getGroupPreTaxProfitAndLoss);
		case ORIGINAL:
			return ChangeSetTransformerUtil.getOriginalRowProfitAndLossValue(row, ScheduleModelKPIUtils::getGroupProfitAndLoss)
					- ChangeSetTransformerUtil.getOriginalRowProfitAndLossValue(row, ScheduleModelKPIUtils::getGroupPreTaxProfitAndLoss);
		default:
			throw new IllegalArgumentException();
		}
	}

	public static long[] getLateness(@NonNull final ChangeSetRow row, @NonNull final ResultType type) {

		final EventGrouping eventGrouping;
		switch (type) {
		case NEW:
			eventGrouping = row.getNewEventGrouping();
			break;
		case ORIGINAL:
			eventGrouping = row.getOriginalEventGrouping();
			break;
		default:
			throw new IllegalArgumentException();
		}
		final long[] result = new long[FlexType.values().length];
		if (eventGrouping != null) {
			result[FlexType.WithFlex.ordinal()] = LatenessUtils.getLatenessAfterFlex(eventGrouping);
			result[FlexType.WithoutFlex.ordinal()] = LatenessUtils.getLatenessExcludingFlex(eventGrouping);
		}
		return result;
	}

	public static long getViolations(@NonNull final ChangeSetRow row, @NonNull final ResultType type) {

		final EventGrouping eventGrouping;
		switch (type) {
		case NEW:
			eventGrouping = row.getNewEventGrouping();
			break;
		case ORIGINAL:
			eventGrouping = row.getOriginalEventGrouping();
			break;
		default:
			throw new IllegalArgumentException();
		}
		if (eventGrouping != null) {
			return ScheduleModelKPIUtils.getCapacityViolationCount(eventGrouping);
		}
		return 0L;
	}

	public static long getPNLSum(@NonNull ChangeSetRow row, @NonNull ResultType type) {

		long pnl = 0L;
		pnl -= getPurchaseCost(row, type);
		pnl += getSalesRevenue(row, type);
		pnl -= getShipping(row, type);
		pnl -= getAdditionalShippingPNL(row, type);
		pnl -= getAdditionalUpsidePNL(row, type);
		pnl += getCargoOtherPNL(row, type);
		pnl += getTax(row, type);
		pnl += getUpstreamPNL(row, type);
		return pnl;
	}
}
