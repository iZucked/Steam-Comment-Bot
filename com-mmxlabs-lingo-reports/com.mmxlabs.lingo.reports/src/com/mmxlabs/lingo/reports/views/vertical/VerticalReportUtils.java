/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Sets;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public final class VerticalReportUtils {

	public enum SlotType {
		PURCHASE, SALE, BOTH
	};

	public static List<Event> eventsFromSequences(final Sequence... sequences) {
		final List<Event> result = new ArrayList<Event>();
		if (sequences != null) {
			for (final Sequence seq : sequences) {
				if (seq != null) {
					result.addAll(seq.getEvents());
				}
			}
		}
		return result;
	}

	public static boolean isEventLate(final Event event) {

		if (event instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) event;
			final Slot slot = slotVisit.getSlotAllocation().getSlot();

			if (slot instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slot;
				if (loadSlot.isDESPurchase()) {
					return false;
				}
			}

			if (slotVisit.getStart().isAfter(slot.getWindowEndWithSlotOrPortTime())) {
				return true;
			}
		} else if (event instanceof VesselEventVisit) {
			final VesselEventVisit vesselEventVisit = (VesselEventVisit) event;
			final VesselEvent vesselEvent = vesselEventVisit.getVesselEvent();
			if (vesselEventVisit.getStart().isAfter(vesselEvent.getStartByAsDateTime())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a list of all 00h00 UTC Date objects which fall within the specified range
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	@NonNull
	public static List<@NonNull LocalDate> getUTCDaysBetween(final @Nullable LocalDate start, final @Nullable LocalDate end) {
		final List<@NonNull LocalDate> result = new ArrayList<>();
		if (start != null && end != null) {
			LocalDate current = start;
			while (!current.isAfter(end)) {
				result.add(current);
				current = current.plusDays(1);
			}

		}

		return result;
	}
	//
	// public static LocalDateTime getLocalDateFor(final Date date, final TimeZone timeZone, final boolean asUTCEquivalent) {
	// final Calendar cal = Calendar.getInstance(timeZone);
	// cal.setTime(date);
	// return getLocalDateFor(cal, asUTCEquivalent);
	// }
	//
	// public static LocalDateTime getLocalDateFor(final ZonedDateTime cal, final boolean asUTCEquivalent) {
	// if (asUTCEquivalent) {
	// return cal.toLocalDateTime();
	// } else {
	// return cal.withZone(ZoneId.of("UTC")).toLocalDateTime();
	// }
	// }
	//
	// public static <T extends ITimezoneProvider & EObject> LocalDateTime getLocalDateFor(final T object, final EAttribute feature, final boolean asUTCEquivalent) {
	// final Date date = (Date) object.eGet(feature);
	// return getLocalDateFor(date, TimeZone.getTimeZone(object.getTimeZone(feature)), asUTCEquivalent);
	//
	// }
	//
	// public static <T extends ITimezoneProvider & EObject> LocalDateTime getLocalDateFor(final T object, final Date date, final EAttribute feature, final boolean asUTCEquivalent) {
	// return getLocalDateFor(date, TimeZone.getTimeZone(object.getTimeZone(feature)), asUTCEquivalent);
	//
	// }

	public static List<Event> getSaleSlotsForContract(final ScheduleSequenceData data, String contractName) {

		final List<Event> loadings = new LinkedList<>();

		if (data.shortDischarges != null) {
			for (final Event evt : data.shortDischarges.getEvents()) {
				if (evt instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) evt;
					final Slot slot = slotVisit.getSlotAllocation().getSlot();
					final Contract contract = slot.getContract();
					if (contract != null && contract.getName().equals(contractName)) {
						loadings.add(slotVisit);
					}
				}
			}
		}
		if (data.vessels != null) {
			for (final Sequence seq : data.vessels) {
				for (final Event evt : seq.getEvents()) {
					if (evt instanceof SlotVisit) {
						final SlotVisit slotVisit = (SlotVisit) evt;
						final Slot slot = slotVisit.getSlotAllocation().getSlot();
						if (slot instanceof DischargeSlot) {
							final Contract contract = slot.getContract();
							if (contract != null && contract.getName().equals(contractName)) {
								loadings.add(slotVisit);
							}
						}
					}
				}
			}
		}
		return loadings;
	}

	/**
	 * Matches slots with given named contract
	 * 
	 * @param contractNames
	 * @return
	 */
	public static Function<Slot, Boolean> getContractMatcher(String contractName) {
		return slot -> {
			Contract contract = slot.getContract();
			if (contract != null) {
				return contractName.equals(contract.getName());
			}

			return false;
		};
	}

	/**
	 * Matches slots with given substring in the contract name
	 * 
	 * @param contractNames
	 * @return
	 */
	public static Function<Slot, Boolean> getContractSubstringMatcher(String contractName) {
		return slot -> {
			Contract contract = slot.getContract();
			if (contract != null) {
				return contract.getName().contains(contractName);
			}

			return false;
		};
	}

	/**
	 * Matches slots with a contract, but excludes given names
	 * 
	 * @param contractNames
	 * @return
	 */
	public static Function<Slot, Boolean> getExceptContractsMatcher(String... contractNames) {
		Set<String> names = Sets.newHashSet(contractNames);

		return slot -> {
			Contract contract = slot.getContract();
			if (contract != null) {
				return !names.contains(contract.getName());
			}

			return false;
		};
	}

	public static List<Event> getMatchingSlots(final ScheduleSequenceData data, SlotType slotType, Function<Slot, Boolean> matcher) {

		final List<Event> events = new LinkedList<>();
		if (slotType == SlotType.SALE || slotType == SlotType.BOTH) {
			if (data.shortDischarges != null) {
				for (final Event evt : data.shortDischarges.getEvents()) {
					if (evt instanceof SlotVisit) {
						final SlotVisit slotVisit = (SlotVisit) evt;
						final Slot slot = slotVisit.getSlotAllocation().getSlot();
						if (matcher.apply(slot)) {
							events.add(slotVisit);
						}
					}
				}
			}
		}
		if (slotType == SlotType.PURCHASE || slotType == SlotType.BOTH) {
			if (data.longLoads != null) {
				for (final Event evt : data.longLoads.getEvents()) {
					if (evt instanceof SlotVisit) {
						final SlotVisit slotVisit = (SlotVisit) evt;
						final Slot slot = slotVisit.getSlotAllocation().getSlot();
						if (matcher.apply(slot)) {
							events.add(slotVisit);
						}
					}
				}
			}
		}
		if (data.vessels != null) {
			for (final Sequence seq : data.vessels) {
				for (final Event evt : seq.getEvents()) {
					if (evt instanceof SlotVisit) {
						final SlotVisit slotVisit = (SlotVisit) evt;
						final Slot slot = slotVisit.getSlotAllocation().getSlot();
						boolean checkSlot = false;
						checkSlot |= (slot instanceof LoadSlot) && (slotType == SlotType.PURCHASE || slotType == SlotType.BOTH);
						checkSlot |= (slot instanceof DischargeSlot) && (slotType == SlotType.SALE || slotType == SlotType.BOTH);

						if (checkSlot) {
							if (matcher.apply(slot)) {
								events.add(slotVisit);
							}
						}
					}
				}
			}
		}
		return events;
	}
}
