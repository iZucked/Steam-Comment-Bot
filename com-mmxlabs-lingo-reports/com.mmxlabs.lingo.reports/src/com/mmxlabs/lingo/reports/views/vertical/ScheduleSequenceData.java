/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotVisit;

/**
 * Record class for holding information on the sequences in a Schedule. Provides the following fields:
 * 
 * <ul>
 * <li>{@code Sequence [] vessels} (all non-fob non-des sequences)</li>
 * <li>{@code Sequence fobSales}</li>
 * <li>{@code Date start} (first event date)</li>
 * <li>{@code Date end} (last event date)</li>
 * </ul>
 * 
 */
public class ScheduleSequenceData {
	final public @NonNull Sequence[] vessels;
	final public Sequence fobSales;
	final public Sequence desPurchases;
	final public VirtualSequence longLoads;
	final public VirtualSequence shortDischarges;
	final public LocalDate start;
	final public LocalDate end;
	private final AbstractVerticalReportVisualiser verticalReportVisualiser;

	/** Extracts the relevant information from the model */
	public ScheduleSequenceData(final LNGScenarioModel model, final AbstractVerticalReportVisualiser verticalReportVisualiser) {
		this.verticalReportVisualiser = verticalReportVisualiser;
		final ScheduleModel scheduleModel = (model == null ? null : model.getScheduleModel());
		final Schedule schedule = (scheduleModel == null ? null : scheduleModel.getSchedule());

		if (schedule == null) {
			vessels = null;
			fobSales = desPurchases = null;
			start = end = null;
			longLoads = null;
			shortDischarges = null;
			return;
		}

		LocalDate startDate = null;
		LocalDate endDate = null;

		Event latestInterestingEvent = null;
		// find start and end dates of entire calendar
		for (final Sequence seq : schedule.getSequences()) {
			for (final Event event : seq.getEvents()) {

				// Event data
				final LocalDate sDate = verticalReportVisualiser.getLocalDateFor(event.getStart());
				final LocalDate eDate = verticalReportVisualiser.getLocalDateFor(event.getEnd());

				// final Date sDate = event.getStart();
				// final Date eDate = event.getEnd();
				if (startDate == null || startDate.isAfter(sDate)) {
					startDate = sDate;
				}
				if (endDate == null || endDate.isBefore(eDate)) {
					endDate = eDate;
				}
				// Event window data
				{
					if (event instanceof SlotVisit) {
						SlotVisit slotVisit = (SlotVisit) event;
						Slot slot = slotVisit.getSlotAllocation().getSlot();

						final LocalDate sWDate = verticalReportVisualiser.getLocalDateFor(slot.getWindowStartWithSlotOrPortTime());

						if (startDate == null || startDate.isAfter(sWDate)) {
							startDate = sWDate;
						}
					}
				}
				// Track interesting events and record the latest event
				if (event instanceof SlotVisit) {
					if (latestInterestingEvent == null) {
						latestInterestingEvent = event;

					} else {
						final LocalDate interestingDate = verticalReportVisualiser.getLocalDateFor(latestInterestingEvent.getEnd());
						if (endDate == null || interestingDate.isBefore(eDate)) {
							latestInterestingEvent = event;
						}
					}

				}
			}
		}
		// Ignore anything after the latest interesting event.
		if (verticalReportVisualiser.filterAfterLastEvent() && latestInterestingEvent != null) {
			final LocalDate eDate = verticalReportVisualiser.getLocalDateFor(latestInterestingEvent.getEnd());
			if (endDate == null || eDate.isBefore(endDate)) {
				endDate = eDate;
			}
		}

		// Run this after the interesting event selection to ensure we keep these events.
		for (final OpenSlotAllocation openSlotAllocation : schedule.getOpenSlotAllocations()) {
			final LocalDate sDate = verticalReportVisualiser.getLocalDateFor(openSlotAllocation.getSlot().getWindowStartWithSlotOrPortTime());
			final LocalDate eDate = sDate;// verticalReportVisualiser.getLocalDateFor(openSlotAllocation.getSlot().getWindowStartWithSlotOrPortTime());

			// final Date sDate = event.getStart();
			// final Date eDate = event.getEnd();
			if (startDate == null || startDate.isAfter(sDate)) {
				startDate = sDate;
			}
			if (endDate == null || endDate.isBefore(eDate)) {
				endDate = eDate;
			}
		}

		// set the final record fields
		start = startDate;
		end = endDate;

		// find the sequences per vessel, and the FOB & DES sequences
		Sequence tempDes = null;
		Sequence tempFob = null;

		final ArrayList<Sequence> vesselList = new ArrayList<Sequence>();

		for (final Sequence seq : schedule.getSequences()) {
			if (seq.getSequenceType() == SequenceType.DES_PURCHASE) {
				tempDes = seq;
			} else if (seq.getSequenceType() == SequenceType.FOB_SALE) {
				tempFob = seq;
			} else {
				vesselList.add(seq);
			}
		}
		// set the final record fields
		desPurchases = tempDes;
		fobSales = tempFob;
		vessels = vesselList.toArray(new Sequence[0]);

		// find the open slots in the schedule
		final List<VirtualSlotVisit> longLoadList = new ArrayList<>();
		final List<VirtualSlotVisit> shortDischargeList = new ArrayList<>();

		for (final OpenSlotAllocation allocation : schedule.getOpenSlotAllocations()) {
			final Slot slot = allocation.getSlot();
			if (slot instanceof LoadSlot) {
				longLoadList.add(new VirtualSlotVisit(slot));
			} else if (slot instanceof DischargeSlot) {
				shortDischargeList.add(new VirtualSlotVisit(slot));
			}
		}

		this.longLoads = new VirtualSequence(longLoadList);
		this.shortDischarges = new VirtualSequence(shortDischargeList);
	}
}
