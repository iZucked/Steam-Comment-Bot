/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.scheduleview.views;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.Viewer;

import scenario.cargo.Slot;
import scenario.schedule.Schedule;
import scenario.schedule.Sequence;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.events.SlotVisit;

import com.mmxlabs.ganttviewer.IGanttChartContentProvider;

/**
 * A gantt chart content provider which provides content for a selected EMF
 * Schedule object.
 * 
 * @author hinton
 * 
 */
public class EMFScheduleContentProvider implements IGanttChartContentProvider {

	@Override
	public Object[] getElements(final Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(final Object parent) {
		if (parent instanceof Schedule) {
			final Schedule schedule = (Schedule) parent;

			final EList<Sequence> sequences = schedule.getSequences();

			return sequences.toArray();
		} else if (parent instanceof Sequence) {
			final Sequence sequence = (Sequence) parent;

			return sequence.getEvents().toArray();
		}
		return null;
	}

	@Override
	public Object getParent(final Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(final Object element) {
		return element instanceof Schedule || element instanceof Sequence;
	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

	}

	@Override
	public Calendar getElementStartTime(final Object element) {
		if (element instanceof ScheduledEvent) {
			final ScheduledEvent event = (ScheduledEvent) element;
			Calendar c = Calendar.getInstance();

			final Date startTime = event.getStartTime();
			if (startTime != null) {
				c.setTime(startTime);
				return c;
			}
		}
		return null;
	}

	@Override
	public Calendar getElementEndTime(final Object element) {
		if (element instanceof ScheduledEvent) {
			final ScheduledEvent event = (ScheduledEvent) element;
			Calendar c = Calendar.getInstance();

			final Date endTime = event.getEndTime();
			if (endTime != null) {
				c.setTime(endTime);
				return c;
			}
		}
		return null;
	}

	@Override
	public Calendar getElementPlannedStartTime(final Object element) {
		if (element instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) element;
			Calendar c = Calendar.getInstance();
			final Slot slot = visit.getSlot();

			final Date windowStart = slot.getWindowStart();
			if (windowStart != null) {
				c.setTime(windowStart);
				return c;
			}
		}

		return null;
	}

	@Override
	public Calendar getElementPlannedEndTime(final Object element) {
		if (element instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) element;
			Calendar c = Calendar.getInstance();
			final Slot slot = visit.getSlot();
			final Date windowStart = slot.getWindowStart();
			if (windowStart != null) {
				c.setTime(windowStart);
				c.add(Calendar.HOUR, slot.getWindowDuration());
				return c;
			}
		}

		return null;
	}

}
