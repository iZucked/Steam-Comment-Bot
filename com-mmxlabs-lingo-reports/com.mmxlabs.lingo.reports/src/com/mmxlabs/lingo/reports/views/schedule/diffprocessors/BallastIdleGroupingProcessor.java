/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.diffprocessors;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.Nullable;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Interval;

import com.mmxlabs.lingo.reports.views.schedule.model.ChangeType;
import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.analytics.Journey;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;

public class BallastIdleGroupingProcessor implements IDiffProcessor {

	@Override
	public void processSchedule(final Schedule schedule, final boolean isPinned) {
	}

	private void generateCycleDiffForElement(final Table table, final Map<EObject, Set<EObject>> equivalancesMap, final Map<EObject, Row> elementToRowMap, final EObject referenceElement) {
		final Row referenceRow = elementToRowMap.get(referenceElement);
		if (referenceRow == null) {
			return;
		}
		EventGrouping referenceGrouping = null;
		if (referenceElement instanceof SlotAllocation) {
			referenceGrouping = ((SlotAllocation) referenceElement).getCargoAllocation();
		} else if (referenceElement instanceof EventGrouping) {
			referenceGrouping = (EventGrouping) referenceElement;
		}
		// Event referenceEvent = (Event) referenceElement;
		if (referenceGrouping != null) {
			// SlotVisit slotVisit = (SlotVisit) referenceEvent;
			// Slot slot = slotVisit.getSlotAllocation().getSlot();
			// if (slot instanceof LoadSlot) {
			//
			// CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();
			EList<Event> events = referenceGrouping.getEvents();
			Event firstEvent = events.get(0);
			Event lastEvent = events.get(events.size() - 1);

			final DateTime start = new DateTime(firstEvent.getStart());

			final DateTime end = new DateTime(lastEvent.getEnd());

			final Interval referenceInterval = new Interval(start, end);

			// bindEvent(referenceRow, referenceInterval, elementToRowMap, slotVisit, interval);
			// }
			// }

			//
			// final Idle referenceIdle = getLastIdle(referenceRow);
			// if (referenceIdle == null) {
			// return;
			// }
			// if (referenceIdle.isLaden()) {
			// return;
			// }
			//
			// final Event previousEvent = referenceIdle.getPreviousEvent();
			// final DateTime start = new DateTime(previousEvent instanceof Journey ? previousEvent.getStart() : referenceIdle.getStart());
			// final DateTime end = new DateTime(referenceIdle.getEnd());
			//
			// final Interval referenceInterval = new Interval(start, end);

			final Sequence referenceSequence = firstEvent.getSequence();

			for (final EObject scenario : table.getScenarios()) {
				if (scenario instanceof LNGScenarioModel) {
					final LNGPortfolioModel portfolioModel = ((LNGScenarioModel) scenario).getPortfolioModel();
					if (portfolioModel != null) {
						final ScheduleModel scheduleModel = portfolioModel.getScheduleModel();
						if (scheduleModel != null) {
							if (scheduleModel.getSchedule() != referenceRow.getSchedule()) {
								for (final Sequence sequence : scheduleModel.getSchedule().getSequences()) {
									if (sequence.getName().equals(referenceSequence.getName())) {
										bindToLadenOverlaps(sequence, referenceRow, referenceInterval, elementToRowMap);
									}
								}
							}
						}
						// }
					}
				}
			}
		}
	}

	private void bindToLadenOverlaps(final Sequence sequence, final Row referenceRow, final Interval referenceInterval, final Map<EObject, Row> elementToRowMap) {
		for (final Event event : sequence.getEvents()) {
			// if (event instanceof Idle) {
			// final Idle rowIdle = (Idle) event;
			// if (rowIdle.isLaden()) {
			// continue;
			// }
			//
			// bindEvent(referenceRow, referenceInterval, elementToRowMap, rowIdle);
			// }

			EventGrouping thisGrouping = null;
			if (event instanceof SlotVisit) {
				thisGrouping = ((SlotVisit) event).getSlotAllocation().getCargoAllocation();
			} else if (event instanceof EventGrouping) {
				thisGrouping = (EventGrouping) event;
			}
			// Event referenceEvent = (Event) referenceElement;
			if (thisGrouping != null) {

				if (thisGrouping != null) {
					// SlotVisit slotVisit = (SlotVisit) event;
					// Slot slot = slotVisit.getSlotAllocation().getSlot();
					// if (slot instanceof LoadSlot) {
					// CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();
					EList<Event> events = thisGrouping.getEvents();
					Event firstEvent = events.get(0);
					Event lastEvent = events.get(events.size() - 1);

					final DateTime start = new DateTime(firstEvent.getStart());

					final DateTime end = new DateTime(lastEvent.getEnd());

					final Interval interval = new Interval(start, end);

					bindEvent(referenceRow, referenceInterval, elementToRowMap, firstEvent, interval);
					// }
					// }
				}
			}
		}
	}

	private void bindEvent(final Row referenceRow, final Interval referenceInterval, final Map<EObject, Row> elementToRowMap, Event visit, final Interval interval) {

		if (referenceInterval.overlaps(interval)) {

			int overlapHours = Hours.hoursIn(referenceInterval.overlap(interval)).getHours();
			int totalHours = Hours.hoursIn(interval).getHours();

			double ratio = (double) overlapHours / ((double) totalHours);

			if (ratio > .7) {
				final CycleGroup group = CycleGroupUtils.createOrReturnCycleGroup(referenceRow.getTable(), referenceRow);
				CycleGroupUtils.setChangeType(group, ChangeType.DURATION);
				final Row r = elementToRowMap.get(visit);
				if (r != null) {
					CycleGroupUtils.addToOrMergeCycleGroup(referenceRow.getTable(), r, group);
				}
			} else {
				int overlapHours2 = Hours.hoursIn(referenceInterval.overlap(interval)).getHours();
				int totalHours2 = Hours.hoursIn(referenceInterval).getHours();

				double ratio2 = (double) overlapHours2 / ((double) totalHours2);

				if (ratio2 > .7) {
					final CycleGroup group = CycleGroupUtils.createOrReturnCycleGroup(referenceRow.getTable(), referenceRow);
					CycleGroupUtils.setChangeType(group, ChangeType.DURATION);
					final Row r = elementToRowMap.get(visit);
					if (r != null) {
						CycleGroupUtils.addToOrMergeCycleGroup(referenceRow.getTable(), r, group);
					}

				}

			}

		}
	}

	private void bindEvent(final Row referenceRow, final Interval referenceInterval, final Map<EObject, Row> elementToRowMap, final Idle event) {
		final Event previousEvent = event.getPreviousEvent();

		final DateTime start = new DateTime(previousEvent instanceof Journey ? previousEvent.getStart() : event.getStart());

		final DateTime end = new DateTime(event.getEnd());

		final Interval interval = new Interval(start, end);

		if (referenceInterval.overlaps(interval)) {
			// Min 2 days overlap!
			if (Days.daysIn(referenceInterval.overlap(interval)).getDays() > 2) {
				final CycleGroup group = CycleGroupUtils.createOrReturnCycleGroup(referenceRow.getTable(), referenceRow);
				CycleGroupUtils.setChangeType(group, ChangeType.DURATION);
				final Row r = elementToRowMap.get(event);
				if (r != null) {
					CycleGroupUtils.addToOrMergeCycleGroup(referenceRow.getTable(), r, group);
				}
			}
		}
	}

	@Nullable
	private Idle getLastIdle(final Row row) {
		EventGrouping grouping = null;
		if (row.getCargoAllocation() != null) {
			grouping = row.getCargoAllocation();

		} else if (row.getTarget() instanceof EventGrouping) {
			grouping = (EventGrouping) row.getTarget();
		}
		if (grouping != null) {
			final EList<Event> events = grouping.getEvents();
			if (!events.isEmpty()) {
				Event lastEvent = events.get(events.size() - 1);
				// If last event is a cooldown, then work backwards as idle should preceed this.
				if (lastEvent instanceof Cooldown) {
					lastEvent = lastEvent.getPreviousEvent();
				}
				if (lastEvent instanceof Idle) {
					return (Idle) lastEvent;
				}
			}
		}
		return null;
	}

	@Override
	public void runDiffProcess(final Table table, final List<EObject> referenceElements, final List<EObject> uniqueElements, final Map<EObject, Set<EObject>> equivalancesMap,
			final Map<EObject, Row> elementToRowMap) {

		for (final EObject referenceElement : referenceElements) {
			generateCycleDiffForElement(table, equivalancesMap, elementToRowMap, referenceElement);
		}
		for (final EObject element : uniqueElements) {
			generateCycleDiffForElement(table, equivalancesMap, elementToRowMap, element);
		}
	}
}
