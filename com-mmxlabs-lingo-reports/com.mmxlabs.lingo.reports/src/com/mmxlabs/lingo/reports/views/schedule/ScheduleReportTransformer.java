/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.RowGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportFactory;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterLengthEvent;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.GroupedCharterLengthEvent;
import com.mmxlabs.models.lng.schedule.GroupedCharterOutEvent;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.scenario.service.ScenarioResult;

public class ScheduleReportTransformer {

	@NonNull
	public static List<Row> transform(final ScenarioResult pinned, final Collection<ScenarioResult> others, final @NonNull ISelectedDataProvider selectedDataProvider) {

		final @NonNull List<Row> allRows = new LinkedList<>();
		ScheduleReportTransformer transformer = new ScheduleReportTransformer();

		if (pinned != null) {
			allRows.addAll(transformer.generateRows(pinned, true));
		}
		for (final ScenarioResult other : others) {
			allRows.addAll(transformer.generateRows(other, false));
		}

		return allRows;
	}

	private List<Row> generateRows(final @NonNull ScenarioResult scenarioResult, final boolean isPinned) {
		Schedule schedule = scenarioResult.getTypedResult(ScheduleModel.class).getSchedule();
		if (schedule == null) {
			return Collections.emptyList();
		}
		// final Map<Sequence, GroupedCharterLengthEvent> extraEvents = new HashMap<>();

		final List<EObject> interestingEvents = new LinkedList<>();
		final Set<EObject> allEvents = new HashSet<>();
		for (final Sequence sequence : schedule.getSequences()) {
			for (final Event event : sequence.getEvents()) {
				if (event instanceof CharterLengthEvent) {
					// final CharterLengthEvent charterLengthEvent = (CharterLengthEvent) event;
					// extraEvents.computeIfAbsent(charterLengthEvent.getSequence(), k -> ScheduleFactory.eINSTANCE.createGroupedCharterLengthEvent()).getEvents().add(charterLengthEvent);
					// continue;
					interestingEvents.add(event);
					allEvents.add(event);
					continue;
				}

				if (event instanceof GeneratedCharterOut) {
					// final GeneratedCharterOutEvent charterOutEvent = (GeneratedCharterOutEvent) event;
					// extraEvents.computeIfAbsent(charterOutEvent.getSequence(), k -> ScheduleFactory.eINSTANCE.createGroupedCharterOutEvent()).getEvents().add(charterOutEvent);
					// continue;
					interestingEvents.add(event);
					allEvents.add(event);
					continue;
				}

				if (showEvent(event)) {
					interestingEvents.add(event);
				}
				allEvents.add(event);
			}
		}

		for (final OpenSlotAllocation openSlotAllocation : schedule.getOpenSlotAllocations()) {
			interestingEvents.add(openSlotAllocation);
			allEvents.add(openSlotAllocation);
		}
		// for (final GroupedCharterLengthEvent cle : extraEvents.values()) {
		// long pnl1 = 0L;
		// long pnl2 = 0L;
		// for (final Event e : cle.getEvents()) {
		// final CharterLengthEvent c = (CharterLengthEvent) e;
		// pnl1 += c.getGroupProfitAndLoss().getProfitAndLoss();
		// pnl2 += c.getGroupProfitAndLoss().getProfitAndLossPreTax();
		// cle.setLinkedSequence(c.getSequence());
		// }
		// final GroupProfitAndLoss groupProfitAndLoss = ScheduleFactory.eINSTANCE.createGroupProfitAndLoss();
		// groupProfitAndLoss.setProfitAndLoss(pnl1);
		// groupProfitAndLoss.setProfitAndLossPreTax(pnl2);
		// cle.setGroupProfitAndLoss(groupProfitAndLoss);
		// }
		// for (final GroupedCharterOutEvent cle : extraEvents.values()) {
		// long pnl1 = 0L;
		// long pnl2 = 0L;
		// for (final Event e : cle.getEvents()) {
		// final CharterLengthEvent c = (CharterLengthEvent) e;
		// pnl1 += c.getGroupProfitAndLoss().getProfitAndLoss();
		// pnl2 += c.getGroupProfitAndLoss().getProfitAndLossPreTax();
		// cle.setLinkedSequence(c.getSequence());
		// }
		// final GroupProfitAndLoss groupProfitAndLoss = ScheduleFactory.eINSTANCE.createGroupProfitAndLoss();
		// groupProfitAndLoss.setProfitAndLoss(pnl1);
		// groupProfitAndLoss.setProfitAndLossPreTax(pnl2);
		// cle.setGroupProfitAndLoss(groupProfitAndLoss);
		// }
		// allEvents.addAll(extraEvents.values());
		// interestingEvents.addAll(extraEvents.values());

		return generateRows(scenarioResult, schedule, interestingEvents, allEvents, isPinned);
	}

	private List<Row> generateRows(final ScenarioResult scenarioResult, final Schedule schedule, final List<EObject> interestingElements, final Set<EObject> allElements,
			final boolean isReferenceSchedule) {
		final List<Row> rows = new ArrayList<>(interestingElements.size());

		for (final EObject element : interestingElements) {

			if (element instanceof SlotVisit) {
				final SlotVisit slotVisit = (SlotVisit) element;
				final CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();

				// Build up list of slots assigned to cargo, sorting into loads and discharges
				final List<SlotAllocation> loadSlots = new ArrayList<>();
				final List<SlotAllocation> dischargeSlots = new ArrayList<>();
				for (final SlotAllocation slot : cargoAllocation.getSlotAllocations()) {
					if (slot.getSlot() instanceof LoadSlot) {
						loadSlots.add(slot);
					} else if (slot.getSlot() instanceof DischargeSlot) {
						dischargeSlots.add(slot);
					} else {
						// Assume some kind of discharge?
						// dischargeSlots.add((Slot) slot);
					}

				}

				final RowGroup group = ScheduleReportFactory.eINSTANCE.createRowGroup();
				// Create a row for each pair of load and discharge slots in the cargo. This may lead to a row with only one slot
				for (int i = 0; i < Math.max(loadSlots.size(), dischargeSlots.size()); ++i) {
					final Row row = ScheduleReportFactory.eINSTANCE.createRow();
					row.setSequence(cargoAllocation.getSequence());
					row.setTarget(cargoAllocation);
					row.setCargoAllocation(cargoAllocation);
					row.setRowGroup(group);
					
					final Set<EObject> equivalents = new LinkedHashSet<>();
					if (i < loadSlots.size()) {
						final SlotAllocation slot = loadSlots.get(i);
						row.setLoadAllocation(slot);
						row.setName(slot.getName());

						allElements.add(slot);

						Event evt = slot.getSlotVisit().getNextEvent();
						while (evt != null && !(evt instanceof SlotVisit)) {
							evt = evt.getNextEvent();
							if (!cargoAllocation.getEvents().contains(evt)) {
								break;
							}
						}
						equivalents.add(slot);
						equivalents.add(slot.getSlot());
						equivalents.add(slot.getSlotVisit());
					}
					if (i < dischargeSlots.size()) {
						final SlotAllocation slot = dischargeSlots.get(i);
						row.setDischargeAllocation(slot);
						row.setName2(slot.getName());

						allElements.add(slot);

						Event evt = slot.getSlotVisit().getNextEvent();
						while (evt != null && !(evt instanceof SlotVisit)) {
							evt = evt.getNextEvent();
							if (!cargoAllocation.getEvents().contains(evt)) {
								break;
							}
						}
						equivalents.add(slot);
						equivalents.add(slot.getSlot());
						equivalents.add(slot.getSlotVisit());
					}
					rows.add(row);

					if (i == 0) {
						equivalents.addAll(cargoAllocation.getEvents());
					}
					row.getInputEquivalents().addAll(equivalents);
				}
			} else if (element instanceof OpenSlotAllocation openSlotAllocation) {

				final Row row = ScheduleReportFactory.eINSTANCE.createRow();
				row.setTarget(openSlotAllocation);
				if (openSlotAllocation.getSlot() instanceof LoadSlot) {
					row.setOpenLoadSlotAllocation(openSlotAllocation);
				} else if (openSlotAllocation.getSlot() instanceof DischargeSlot) {
					row.setOpenDischargeSlotAllocation(openSlotAllocation);
				} else {
					// This has happened in the sandbox code...
					// assert false;
				}
				allElements.add(openSlotAllocation);
				final Slot slot = openSlotAllocation.getSlot();
				if (slot == null) {
					row.setName("??");
				} else {
					if (slot instanceof DischargeSlot) {
						row.setName2(slot.getName());
					} else {
						row.setName(slot.getName());

					}
				}
				addInputEquivalents(row, openSlotAllocation);
				rows.add(row);
			} else if (element instanceof Event event) {
				final Row row = ScheduleReportFactory.eINSTANCE.createRow();
				row.setSequence(event.getSequence());
				row.setTarget(event);
				row.setName(event.name());
				rows.add(row);

				allElements.add(event);
				addInputEquivalents(row, event);
			}

		}

		for (final Row row : rows) {
			row.setScenarioName(scenarioResult.getModelRecord().getName());
			row.setSchedule(schedule);
			row.setScenarioDataProvider(scenarioResult.getScenarioDataProvider());
			// If this is the reference schedule, then mark row as reference
			row.setReference(isReferenceSchedule);

			row.setVisible(true);
		}

		return rows;
	}

	private void addInputEquivalents(@NonNull final Row row, @NonNull final EObject a) {

		// map to events
		final Set<EObject> equivalents = new LinkedHashSet<>();
		if (a instanceof CargoAllocation allocation) {
			for (final SlotAllocation slotAllocation : allocation.getSlotAllocations()) {
				equivalents.add(slotAllocation.getSlot());
				equivalents.add(slotAllocation.getSlotVisit());
			}
			equivalents.addAll(allocation.getEvents());
		} else if (a instanceof SlotAllocation slotAllocation) {
			equivalents.add(slotAllocation.getSlot());
			equivalents.add(slotAllocation.getSlotVisit());
		} else if (a instanceof SlotVisit slotVisit) {
			final CargoAllocation allocation = slotVisit.getSlotAllocation().getCargoAllocation();
			for (final SlotAllocation slotAllocation : allocation.getSlotAllocations()) {
				equivalents.add(slotAllocation.getSlot());
				equivalents.add(slotAllocation.getSlotVisit());
			}
			equivalents.addAll(allocation.getEvents());
			// equivalents.add(allocation.getInputCargo());
		} else if (a instanceof VesselEventVisit vesselEventVisit) {
			equivalents.addAll(Lists.<EObject> newArrayList(vesselEventVisit, vesselEventVisit.getVesselEvent()));
			equivalents.addAll(vesselEventVisit.getEvents());
		} else if (a instanceof GeneratedCharterOut generatedCharterOut) {
			equivalents.add(generatedCharterOut);
			equivalents.addAll(generatedCharterOut.getEvents());
		} else if (a instanceof CharterLengthEvent charterLengthEvent) {
			equivalents.add(charterLengthEvent);
			equivalents.addAll(charterLengthEvent.getEvents());
		} else if (a instanceof GroupedCharterLengthEvent) {
			final GroupedCharterLengthEvent charterLengthEvent = (GroupedCharterLengthEvent) a;
			equivalents.addAll(charterLengthEvent.getEvents());
		} else if (a instanceof StartEvent startEvent) {
			equivalents.addAll(startEvent.getEvents());
			final VesselCharter vesselCharter = startEvent.getSequence().getVesselCharter();
			if (vesselCharter != null) {
				equivalents.addAll(Lists.<EObject> newArrayList(startEvent, vesselCharter.getVessel()));
			}
		} else if (a instanceof EndEvent endEvent) {
			equivalents.add(endEvent);
		} else if (a instanceof OpenSlotAllocation openSlotAllocation) {
			equivalents.addAll(Lists.<EObject> newArrayList(openSlotAllocation, openSlotAllocation.getSlot()));
		} else {
			equivalents.addAll(Lists.<EObject> newArrayList(a));
		}

		equivalents.stream()//
				.filter(Objects::nonNull)// Filter out nulls
				.forEach(e -> row.getInputEquivalents().add(e));
	}

	private boolean showEvent(final Event event) {
		if (event instanceof StartEvent) {
			return true;
		} else if (event instanceof EndEvent) {
			return true;
		} else if (event instanceof VesselEventVisit) {
			return true;
		} else if (event instanceof GeneratedCharterOut) {
			return true;
		} else if (event instanceof CharterLengthEvent) {
			return true;
		} else if (event instanceof SlotVisit slotVisit) {
			if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
				return true;
			}
		}
		return false;
	}

}
