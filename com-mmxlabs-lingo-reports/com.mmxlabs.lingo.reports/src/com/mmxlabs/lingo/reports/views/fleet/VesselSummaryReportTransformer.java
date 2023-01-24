/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.views.schedule.model.CompositeRow;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.RowGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportFactory;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.scenario.service.ScenarioResult;

public class VesselSummaryReportTransformer {

	private final VesselSummaryReportBuilder builder;

	public VesselSummaryReportTransformer(final VesselSummaryReportBuilder builder) {
		this.builder = builder;
	}

	public List<Object> generatePinDiffRows(final ScenarioResult pinned, final ScenarioResult other) {

		final Map<Pair<Object, Integer>, Row> pinnedMap = new HashMap<>();

		final List<Row> rows = new LinkedList<>();
		if (pinned != null) {

			final ScenarioResult scenarioResult = pinned;

			final Schedule schedule = scenarioResult.getTypedResult(ScheduleModel.class).getSchedule();
			if (schedule != null) {
				for (final Sequence sequence : schedule.getSequences()) {
					if (builder.showEvent(sequence)) {

						final Pair<Object, Integer> key;

						final VesselCharter vesselCharter = sequence.getVesselCharter();
						if (vesselCharter != null) {
							key = Pair.of(vesselCharter.getVessel().getName(), vesselCharter.getCharterNumber());
						} else if (sequence.getSequenceType() == SequenceType.SPOT_VESSEL) {
							key = Pair.of(sequence.getCharterInMarket().getName(), sequence.getSpotIndex());
						} else {
							continue;
						}

						final Row row = createRow(scenarioResult, schedule, sequence);
						row.setReference(true);

						rows.add(row);
						pinnedMap.put(key, row);

						addInputEquivalents(row, sequence);
					}
				}
			}
		}
		if (other != null) {
			final ScenarioResult scenarioResult = other;
			final Schedule schedule = scenarioResult.getTypedResult(ScheduleModel.class).getSchedule();
			if (schedule != null) {
				for (final Sequence sequence : schedule.getSequences()) {
					if (builder.showEvent(sequence)) {

						final Pair<Object, Integer> key;

						final VesselCharter vesselCharter = sequence.getVesselCharter();
						if (vesselCharter != null) {
							key = Pair.of(vesselCharter.getVessel().getName(), vesselCharter.getCharterNumber());
						} else if (sequence.getSequenceType() == SequenceType.SPOT_VESSEL) {
							key = Pair.of(sequence.getCharterInMarket().getName(), sequence.getSpotIndex());
						} else {
							continue;
						}
						final Row row = createRow(scenarioResult, schedule, sequence);

						rows.add(row);
						if (pinnedMap.containsKey(key)) {
							final Row pinnedRow = pinnedMap.get(key);

							row.setLhsLink(pinnedRow);
							pinnedRow.setLhsLink(row);

						}
						addInputEquivalents(row, sequence);
					}
				}
			}
		}

		final List<Object> finalRows = new LinkedList<>();
		{
			final List<CompositeRow> compositeRows = new LinkedList<>();
			for (final Row row : rows) {
				if (row.isReference()) {

					final Row lhs = row.getLhsLink();
					if (lhs != null) {
						final CompositeRow compositeRow = ScheduleReportFactory.eINSTANCE.createCompositeRow();

						compositeRow.setPinnedRow(row);
						compositeRow.setPreviousRow(lhs);

						compositeRows.add(compositeRow);
					} else {
						// Case deleted from pinned
						final CompositeRow compositeRow = ScheduleReportFactory.eINSTANCE.createCompositeRow();
						compositeRow.setPinnedRow(row);
						compositeRow.setPreviousRow(null);
						compositeRows.add(compositeRow);
					}
				} else if (row.getLhsLink() == null && row.getRhsLink() == null) {
					final CompositeRow compositeRow = ScheduleReportFactory.eINSTANCE.createCompositeRow();
					compositeRow.setPinnedRow(null);
					compositeRow.setPreviousRow(row);
					compositeRows.add(compositeRow);
				}
			}

			for (final CompositeRow compositeRow : compositeRows) {
				final RowGroup rowGroup = ScheduleReportFactory.eINSTANCE.createRowGroup();

				final Row previousRow = compositeRow.getPreviousRow();
				final Row pinnedRow = compositeRow.getPinnedRow();

				if (previousRow != null) {
					previousRow.setRowGroup(rowGroup);
				}

				if (pinnedRow != null) {
					pinnedRow.setRowGroup(rowGroup);
				}

			}

			finalRows.addAll(rows);
			finalRows.addAll(compositeRows);
			finalRows.add(compositeRows);

		}

		return finalRows;
	}

	private @NonNull Row createRow(final ScenarioResult scenarioResult, final Schedule schedule, final Sequence sequence) {
		final Row row = ScheduleReportFactory.eINSTANCE.createRow();
		row.setTarget(sequence);
		row.setName(sequence.getName());
		row.setSequence(sequence);
		row.setVisible(true);

		row.setScenarioName(scenarioResult.getModelRecord().getName());
		row.setSchedule(schedule);

		return row;
	}

	public List<Object> generateSimpleRows(final Collection<ScenarioResult> others) {
		final List<Object> rows = new LinkedList<>();

		for (final ScenarioResult other : others) {
			final ScenarioResult scenarioResult = other;
			final Schedule schedule = scenarioResult.getTypedResult(ScheduleModel.class).getSchedule();
			if (schedule != null) {
				for (final Sequence sequence : schedule.getSequences()) {
					if (builder.showEvent(sequence)) {
						final Row row = createRow(scenarioResult, schedule, sequence);
						rows.add(row);
						addInputEquivalents(row, sequence);
					}
				}
			}
		}

		return rows;
	}

	public void addInputEquivalents(@NonNull final Row row, @NonNull final EObject a) {

		// map to events
		if (a instanceof CargoAllocation) {
			final CargoAllocation allocation = (CargoAllocation) a;

			final List<Object> equivalents = new LinkedList<Object>();
			for (final SlotAllocation slotAllocation : allocation.getSlotAllocations()) {
				equivalents.add(slotAllocation.getSlot());
				equivalents.add(slotAllocation.getSlotVisit());
			}
			row.getInputEquivalents().addAll(allocation.getEvents());
			// row.getInputEquivalents().add(allocation.getInputCargo());
		} else if (a instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) a;

			final CargoAllocation allocation = slotVisit.getSlotAllocation().getCargoAllocation();
			for (final SlotAllocation slotAllocation : allocation.getSlotAllocations()) {
				row.getInputEquivalents().add(slotAllocation.getSlot());
				row.getInputEquivalents().add(slotAllocation.getSlotVisit());
			}
			row.getInputEquivalents().addAll(allocation.getEvents());
			// row.getInputEquivalents().add(allocation.getInputCargo());
		} else if (a instanceof VesselEventVisit) {
			final VesselEventVisit vesselEventVisit = (VesselEventVisit) a;
			row.getInputEquivalents().addAll(Lists.<EObject> newArrayList(vesselEventVisit, vesselEventVisit.getVesselEvent()));
			row.getInputEquivalents().addAll(vesselEventVisit.getEvents());
		} else if (a instanceof StartEvent) {
			final StartEvent startEvent = (StartEvent) a;
			row.getInputEquivalents().addAll(startEvent.getEvents());
			final VesselCharter vesselCharter = startEvent.getSequence().getVesselCharter();
			if (vesselCharter != null) {
				row.getInputEquivalents().addAll(Lists.<EObject> newArrayList(startEvent, vesselCharter.getVessel()));
			}
		} else if (a instanceof OpenSlotAllocation) {
			final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) a;
			row.getInputEquivalents().addAll(Lists.<EObject> newArrayList(openSlotAllocation, openSlotAllocation.getSlot()));
		} else {
			row.getInputEquivalents().addAll(Lists.<EObject> newArrayList(a));
		}
	}
}