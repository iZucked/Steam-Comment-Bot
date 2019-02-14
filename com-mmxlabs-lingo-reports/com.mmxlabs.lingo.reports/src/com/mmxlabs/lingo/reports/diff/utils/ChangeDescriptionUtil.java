/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lingo.reports.utils.CargoAllocationUtils;
import com.mmxlabs.lingo.reports.views.schedule.diffprocessors.CycleGroupUtils;
import com.mmxlabs.lingo.reports.views.schedule.model.ChangeType;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterLengthEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupedCharterLengthEvent;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;

public class ChangeDescriptionUtil {

	private static final Logger LOG = LoggerFactory.getLogger(ChangeDescriptionUtil.class);

	public static String getCapacityViolationDescription(final Row row) {
		if (!row.isReference()) {
			final Row lhsLink = row.getLhsLink();
			String loadViolationString = "";
			if (lhsLink != null) {
				loadViolationString = CapacityViolationDiffUtils.checkSlotAllocationForCapacityViolations(row.getLoadAllocation(), lhsLink.getLoadAllocation());
			}
			final Row rhsLink = row.getRhsLink();
			String dischargeViolationString = "";
			if (rhsLink != null) {
				dischargeViolationString = CapacityViolationDiffUtils.checkSlotAllocationForCapacityViolations(row.getDischargeAllocation(), rhsLink.getDischargeAllocation());
			}
			return String.format("%s%s%s", loadViolationString, loadViolationString.equals("") || dischargeViolationString.equals("") ? "" : " | ", dischargeViolationString);
		}
		return "";
	}

	public static String getLatenessDescription(final Row row) {
		if (!row.isReference()) {
			final Row lhsLink = row.getLhsLink();
			String loadLatenessString = "";
			if (lhsLink != null) {
				loadLatenessString = LatenessDiffUtils.checkSlotAllocationForLateness(row.getLoadAllocation(), lhsLink.getLoadAllocation());
			}
			final Row rhsLink = row.getRhsLink();
			String dischargeLatenessString = "";
			if (rhsLink != null) {
				dischargeLatenessString = LatenessDiffUtils.checkSlotAllocationForLateness(row.getDischargeAllocation(), rhsLink.getDischargeAllocation());
			}
			return String.format("%s%s%s", loadLatenessString, loadLatenessString.equals("") || dischargeLatenessString.equals("") ? "" : " | ", dischargeLatenessString);
		}
		return "";
	}

	public static String getChangeDescription(final Row row) {

		if (!row.isReference()) {
			final Row referenceRow = row.getLhsLink();
			final CargoAllocation ca = row.getCargoAllocation();
			if (referenceRow != null) {

				final CargoAllocation ref = referenceRow.getCargoAllocation();
				if (ca != null && ref != null) {

					final String elementString = CargoAllocationUtils.getWiringAsString(ca);
					final String referenceString = CargoAllocationUtils.getWiringAsString(ref);

					if (elementString.equals(referenceString)) {

						if (!ca.getSequence().getName().equals(ref.getSequence().getName())) {
							CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.VESSEL);
							return String.format("Vessel: %s -> %s", ref.getSequence().getName(), ca.getSequence().getName());
						}

						final int rowDuration = getEventGroupingDuration(ca);
						final int referenceDuration = getEventGroupingDuration(ref);

						if (rowDuration > referenceDuration) {
							CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.DURATION);
							return String.format("Shipping duration increased by %.1f days", ((double) (rowDuration - referenceDuration)) / 24.0);
						} else if (rowDuration < referenceDuration) {
							CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.DURATION);
							return String.format("Shipping duration decreased by %.1f days", ((double) (referenceDuration - rowDuration)) / 24.0);
						}

						return "";
					}
					if (row.getLoadAllocation().getSlot() instanceof SpotLoadSlot) {
						final SpotLoadSlot spotLoadSlot = (SpotLoadSlot) row.getLoadAllocation().getSlot();
						CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.WIRING);
						return String.format("Buy spot '%s' to %s", spotLoadSlot.getMarket().getName(), CargoAllocationUtils.getSalesWiringAsString(ca));
					} else if (row.getDischargeAllocation().getSlot() instanceof SpotDischargeSlot) {
						// Sell spot?
						final SpotDischargeSlot spotDischargeSlot = (SpotDischargeSlot) row.getDischargeAllocation().getSlot();
						CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.WIRING);
						return String.format("Sell spot '%s' to %s", row.getLoadAllocation().getSlot().getName(), spotDischargeSlot.getMarket().getName());
					} else {
						CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.WIRING);
						return String.format("Redirect '%s' : %s -> %s", row.getLoadAllocation().getSlot().getName(), CargoAllocationUtils.getSalesWiringAsString(ref),
								CargoAllocationUtils.getSalesWiringAsString(ca));
					}
				}
				if (row.getOpenLoadSlotAllocation() != null && referenceRow.getOpenLoadSlotAllocation() == null) {
					CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.WIRING);
					return String.format("Cancelled '%s'", row.getOpenLoadSlotAllocation().getSlot().getName());
				}
				if (row.getOpenDischargeSlotAllocation() != null && referenceRow.getOpenLoadSlotAllocation() == null) {
					CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.WIRING);
					return String.format("Cancelled '%s'", row.getOpenDischargeSlotAllocation().getSlot().getName());
				}
				if (row.getTarget() instanceof CharterLengthEvent && referenceRow.getTarget() instanceof CharterLengthEvent) {
					final CharterLengthEvent rowTarget = (CharterLengthEvent) row.getTarget();
					final CharterLengthEvent referenceTarget = (CharterLengthEvent) referenceRow.getTarget();
					if (rowTarget.getDuration() > referenceTarget.getDuration()) {
						CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.DURATION);
						return String.format("Charter length duration increased by %.1f days", ((double) (rowTarget.getDuration() - referenceTarget.getDuration())) / 24.0);
					} else if (rowTarget.getDuration() < referenceTarget.getDuration()) {
						CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.DURATION);
						return String.format("Charter length duration decreased by %.1f days", ((double) (referenceTarget.getDuration() - rowTarget.getDuration())) / 24.0);
					}
				} else if (row.getTarget() instanceof GroupedCharterLengthEvent && referenceRow.getTarget() instanceof GroupedCharterLengthEvent) {
					final GroupedCharterLengthEvent rowTarget = (GroupedCharterLengthEvent) row.getTarget();
					final GroupedCharterLengthEvent referenceTarget = (GroupedCharterLengthEvent) referenceRow.getTarget();
					int durationA = getEventGroupingDuration(rowTarget);
					int durationB = getEventGroupingDuration(referenceTarget);
					if (durationA > durationB) {
						CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.DURATION);
						return String.format("Charter length duration increased by %.1f days", ((double) (durationA - durationB)) / 24.0);
					} else if (durationA < durationB) {
						CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.DURATION);
						return String.format("Charter length duration decreased by %.1f days", ((double) (durationB - durationA)) / 24.0);
					}
				} else if (referenceRow.getTarget() instanceof CharterLengthEvent) {
					CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.DURATION);
					return "Removed charter length";
				} else if (referenceRow.getTarget() instanceof GroupedCharterLengthEvent) {
					CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.DURATION);
					return "Removed charter length";
				}
				if (row.getTarget() instanceof GeneratedCharterOut && referenceRow.getTarget() instanceof GeneratedCharterOut) {
					final GeneratedCharterOut rowTarget = (GeneratedCharterOut) row.getTarget();
					final GeneratedCharterOut referenceTarget = (GeneratedCharterOut) referenceRow.getTarget();
					if (rowTarget.getDuration() > referenceTarget.getDuration()) {
						CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.DURATION);
						return String.format("Charter duration increased by %.1f days", ((double) (rowTarget.getDuration() - referenceTarget.getDuration())) / 24.0);
					} else if (rowTarget.getDuration() < referenceTarget.getDuration()) {
						CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.DURATION);
						return String.format("Charter duration decreased by %.1f days", ((double) (referenceTarget.getDuration() - rowTarget.getDuration())) / 24.0);
					}
				} else if (referenceRow.getTarget() instanceof GeneratedCharterOut) {
					CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.CHARTERING);
					return "Removed charter out";
				}
				if (row.getTarget() instanceof EventGrouping && referenceRow.getTarget() instanceof EventGrouping) {
					final EventGrouping rowTarget = (EventGrouping) row.getTarget();
					final EventGrouping referenceTarget = (EventGrouping) referenceRow.getTarget();
					final int rowDuration = getEventGroupingDuration(rowTarget);
					final int referenceDuration = getEventGroupingDuration(referenceTarget);

					if (rowTarget instanceof Event && referenceTarget instanceof Event) {
						final Event rowEvent = (Event) rowTarget;
						final Event referenceEvent = (Event) referenceTarget;
						if (!(rowEvent.getSequence().getName().equals(referenceEvent.getSequence().getName()))) {
							CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.VESSEL);
							return String.format("Vessel: %s -> %s", referenceEvent.getSequence().getName(), rowEvent.getSequence().getName());
						}
					}
					if (rowDuration > referenceDuration) {
						CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.DURATION);

						return String.format("Event duration increased by %.1f days", ((double) (rowDuration - referenceDuration)) / 24.0);
					} else if (rowDuration < referenceDuration) {
						CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.DURATION);
						return String.format("Event duration decreased by %.1f days", ((double) (referenceDuration - rowDuration)) / 24.0);
					}
				}
			} else {

				if (row.getOpenLoadSlotAllocation() != null) {
					CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.WIRING);
					return String.format("Cancelled '%s'", row.getOpenLoadSlotAllocation().getSlot().getName());
				}
				if (row.getOpenDischargeSlotAllocation() != null) {
					CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.WIRING);
					return String.format("Cancelled '%s'", row.getOpenDischargeSlotAllocation().getSlot().getName());
				}

				if (row.getLoadAllocation() != null && row.getLoadAllocation().getSlot() instanceof SpotLoadSlot) {
					final SpotLoadSlot spotLoadSlot = (SpotLoadSlot) row.getLoadAllocation().getSlot();
					CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.WIRING);

					return String.format("Buy spot '%s' to %s", spotLoadSlot.getMarket().getName(), CargoAllocationUtils.getSalesWiringAsString(ca));
				}

				if (row.getTarget() instanceof GeneratedCharterOut) {
					CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.CHARTERING);
					return "Added charter out";
				}
				if (row.getTarget() instanceof CharterLengthEvent) {
					CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.DURATION);
					return "Added charter length";
				}
				if (row.getTarget() instanceof GroupedCharterLengthEvent) {
					CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.DURATION);
					return "Added charter length";
				}
			}
		} else {
			if (row.getLhsLink() == null) {
				// This is a reference row, nothing refers to it.

				// GCO in reference case, but not comparison case.
				if (row.getTarget() instanceof GeneratedCharterOut) {
					CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.CHARTERING);
					return "Removed charter out";
				}
				if (row.getTarget() instanceof CharterLengthEvent) {
					CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.DURATION);
					return "Removed charter length";
				}
				if (row.getTarget() instanceof GroupedCharterLengthEvent) {
					CycleGroupUtils.setChangeType(row.getCycleGroup(), ChangeType.DURATION);
					return "Removed charter length";
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

		final Row referenceRow = row.isReference() ? null : row.getLhsLink();
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

		final Row referenceRow = row.isReference() ? null : row.getLhsLink();
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
					} else if (referenceRow.getOpenLoadSlotAllocation() != null) {
						result = "Long";
					} else if (referenceRow.getOpenDischargeSlotAllocation() != null) {
						result = "Short";
					}
				} catch (final Exception e) {
					LOG.warn("Error formatting previous wiring", e);
				}

				// Do not display if same
				if (currentWiring.equals(result)) {
					return "";
				}

				return result;
			} else if (row.getOpenLoadSlotAllocation() != null) {
				final OpenSlotAllocation openSlotAllocation = row.getOpenLoadSlotAllocation();
				if (openSlotAllocation != null) {

					if (referenceRow.getCargoAllocation() != null) {
						final CargoAllocation pinnedCargoAllocation = referenceRow.getCargoAllocation();
						if (pinnedCargoAllocation != null) {
							final String result = CargoAllocationUtils.getSalesWiringAsString(pinnedCargoAllocation);
							return result;
						}
					}
				}
			} else if (row.getOpenDischargeSlotAllocation() != null) {
				final OpenSlotAllocation openSlotAllocation = row.getOpenDischargeSlotAllocation();
				if (openSlotAllocation != null) {

					if (referenceRow.getCargoAllocation() != null) {
						final CargoAllocation pinnedCargoAllocation = referenceRow.getCargoAllocation();
						if (pinnedCargoAllocation != null) {

							final String result = CargoAllocationUtils.getPurchaseWiringAsString(pinnedCargoAllocation);
							return result;
						}
					}
				}
			}
		}
		return null;

	}

}
