package com.mmxlabs.lingo.reports.diff.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lingo.reports.utils.CargoAllocationUtils;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;

public class ChangeDescriptionUtil {

	private static final Logger LOG = LoggerFactory.getLogger(ChangeDescriptionUtil.class);

	public static String getChangeDescription(final Row row) {

		if (!row.isReference()) {
			final Row referenceRow = row.getReferenceRow();
			final CargoAllocation ca = row.getCargoAllocation();
			if (referenceRow != null) {

				final CargoAllocation ref = referenceRow.getCargoAllocation();
				if (ca != null && ref != null) {

					final String elementString = CargoAllocationUtils.getWiringAsString(ca);
					final String referenceString = CargoAllocationUtils.getWiringAsString(ref);

					if (elementString.equals(referenceString)) {

						if (!ca.getSequence().getName().equals(ref.getSequence().getName())) {
							return String.format("Vessel: %s -> %s", ref.getSequence().getName(), ca.getSequence().getName());
						}

						final int rowDuration = getEventGroupingDuration(ca);
						final int referenceDuration = getEventGroupingDuration(ref);

						if (rowDuration > referenceDuration) {
							return String.format("Shipping duration increased by %.1f days", ((double) (rowDuration - referenceDuration)) / 24.0);
						} else if (rowDuration < referenceDuration) {
							return String.format("Shipping duration decreased by %.1f days", ((double) (referenceDuration - rowDuration)) / 24.0);
						}

						return "";
					}
					if (row.getLoadAllocation().getSlot() instanceof SpotLoadSlot) {
						final SpotLoadSlot spotLoadSlot = (SpotLoadSlot) row.getLoadAllocation().getSlot();
						return String.format("Buy spot '%s' to %s", spotLoadSlot.getMarket().getName(), CargoAllocationUtils.getSalesWiringAsString(ca));
					} else if (row.getDischargeAllocation().getSlot() instanceof SpotDischargeSlot) {
						// Sell spot?
						final SpotDischargeSlot spotDischargeSlot = (SpotDischargeSlot) row.getDischargeAllocation().getSlot();
						return String.format("Sell spot '%s' to %s", row.getLoadAllocation().getSlot().getName(), spotDischargeSlot.getMarket().getName());
					} else {
						return String.format("Redirect '%s' : %s -> %s", row.getLoadAllocation().getSlot().getName(), CargoAllocationUtils.getSalesWiringAsString(ref),
								CargoAllocationUtils.getSalesWiringAsString(ca));
					}
				}
				if (row.getOpenSlotAllocation() != null && referenceRow.getOpenSlotAllocation() == null) {
					return String.format("Cancelled '%s'", row.getOpenSlotAllocation().getSlot().getName());
				}
				if (row.getTarget() instanceof GeneratedCharterOut && referenceRow.getTarget() instanceof GeneratedCharterOut) {
					final GeneratedCharterOut rowTarget = (GeneratedCharterOut) row.getTarget();
					final GeneratedCharterOut referenceTarget = (GeneratedCharterOut) referenceRow.getTarget();
					if (rowTarget.getDuration() > referenceTarget.getDuration()) {
						return String.format("Charter duration increased by %.1f days", ((double) (rowTarget.getDuration() - referenceTarget.getDuration())) / 24.0);
					} else if (rowTarget.getDuration() < referenceTarget.getDuration()) {
						return String.format("Charter duration decreased by %.1f days", ((double) (referenceTarget.getDuration() - rowTarget.getDuration())) / 24.0);
					}
				} else if (referenceRow.getTarget() instanceof GeneratedCharterOut) {
					return "Removed charter out";
				}
				if (row.getTarget() instanceof EventGrouping && referenceRow.getTarget() instanceof EventGrouping) {
					final EventGrouping rowTarget = (EventGrouping) row.getTarget();
					final EventGrouping referenceTarget = (EventGrouping) referenceRow.getTarget();
					final int rowDuration = getEventGroupingDuration(rowTarget);
					final int referenceDuration = getEventGroupingDuration(referenceTarget);

					if (rowDuration > referenceDuration) {
						return String.format("Event duration increased by %.1f days", ((double) (rowDuration - referenceDuration)) / 24.0);
					} else if (rowDuration < referenceDuration) {
						return String.format("Event duration decreased by %.1f days", ((double) (referenceDuration - rowDuration)) / 24.0);
					}
				}
			} else {

				if (row.getOpenSlotAllocation() != null) {
					return String.format("Cancelled '%s'", row.getOpenSlotAllocation().getSlot().getName());
				}

				if (row.getLoadAllocation() != null && row.getLoadAllocation().getSlot() instanceof SpotLoadSlot) {
					final SpotLoadSlot spotLoadSlot = (SpotLoadSlot) row.getLoadAllocation().getSlot();
					return String.format("Buy spot '%s' to %s", spotLoadSlot.getMarket().getName(), CargoAllocationUtils.getSalesWiringAsString(ca));
				}

				if (row.getTarget() instanceof GeneratedCharterOut) {
					return "Added charter out";
				}
			}
		} else {
			if (row.getReferringRows().isEmpty()) {
				// This is a reference row, nothing refers to it.

				// GCO in reference case, but not comparison case.
				if (row.getTarget() instanceof GeneratedCharterOut) {
					return "Removed charter out";
				}
			}
		}
		return "";
	}

	public static int getEventGroupingDuration(final EventGrouping eventGrouping) {
		int duration = 0;
		for (final Event event : eventGrouping.getEvents()) {
			duration += event.getDuration();
		}

		return duration;
	}

	public static String getPreviousVessel(final Row row) {
		if (row.getCargoAllocation() == null) {
			return null;
		}

		final String currentAssignment = CargoAllocationUtils.getVesselAssignmentName(row.getCargoAllocation());

		String result = "";

		final Row referenceRow = row.getReferenceRow();
		if (referenceRow != null) {
			try {
				final CargoAllocation ca = (CargoAllocation) referenceRow.eGet(ScheduleReportPackage.Literals.ROW__CARGO_ALLOCATION);
				result = CargoAllocationUtils.getVesselAssignmentName(ca);
			} catch (final Exception e) {
				LOG.warn("Error formatting previous assignment", e);
			}
		}

		// Only show if different.
		if (currentAssignment.equals(result)) {
			return null;
		}
		return result;

	}

	public static String getPreviousWiring(final Row row) {

		if (row.getCargoAllocation() == null) {
			return null;
		}

		final Row referenceRow = row.getReferenceRow();
		if (referenceRow != null) {
			//
			if (row.getCargoAllocation() != null) {
				final String currentWiring = CargoAllocationUtils.getSalesWiringAsString(row.getCargoAllocation());
				//
				String result = "";
				// // for objects not coming from the pinned scenario,
				// // return the pinned counterpart's wiring to display as the previous wiring
				try {
					final CargoAllocation pinnedCargoAllocation = referenceRow.getCargoAllocation();
					if (pinnedCargoAllocation != null) {
						// convert this cargo's wiring of slot allocations to a string
						result = CargoAllocationUtils.getSalesWiringAsString(pinnedCargoAllocation);
					} else if (referenceRow.getOpenSlotAllocation() != null) {
						final OpenSlotAllocation openSlotAllocation = referenceRow.getOpenSlotAllocation();
						if (openSlotAllocation != null) {
							if (openSlotAllocation.getSlot() instanceof LoadSlot) {
								result = "Long";
							} else {
								result = "Short";
							}
						}
					}
				} catch (final Exception e) {
					LOG.warn("Error formatting previous wiring", e);
				}

				// Do not display if same
				if (currentWiring.equals(result)) {
					return "";
				}

				return result;
			} else if (row.getOpenSlotAllocation() != null) {
				final OpenSlotAllocation openSlotAllocation = row.getOpenSlotAllocation();
				if (openSlotAllocation != null) {

					if (referenceRow.getCargoAllocation() != null) {
						final CargoAllocation pinnedCargoAllocation = referenceRow.getCargoAllocation();
						if (pinnedCargoAllocation != null) {

							final String result;
							if (openSlotAllocation.getSlot() instanceof LoadSlot) {
								result = CargoAllocationUtils.getSalesWiringAsString(pinnedCargoAllocation);
							} else {
								result = CargoAllocationUtils.getPurchaseWiringAsString(pinnedCargoAllocation);
							}
							return result;
						}
					}
				}
			}
		}
		return null;

	}
}
