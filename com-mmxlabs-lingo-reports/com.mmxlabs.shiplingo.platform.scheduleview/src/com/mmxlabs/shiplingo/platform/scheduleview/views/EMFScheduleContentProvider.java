/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.scheduleview.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.ganttviewer.IGanttChartContentProvider;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;

/**
 * A gantt chart content provider which provides content for a selected EMF Schedule object.
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

		if (parent instanceof IScenarioViewerSynchronizerOutput) {
			final IScenarioViewerSynchronizerOutput synchronizerOutput = (IScenarioViewerSynchronizerOutput) parent;
			final List<Object> result = new ArrayList<Object>();
			for (final Object o : synchronizerOutput.getCollectedElements()) {
				if (o instanceof Schedule) {
					EList<Sequence> sequences = ((Schedule) o).getSequences();
					
					for (Sequence seq : sequences) {
						// TODO: Need a proper flag
						if (seq.getName().equals("<no vessel>")) {
							if (seq.getEvents().size() > 0) {
								result.add(seq);
							}
						} else {
							result.add(seq);
						}
					}
//					return seqs.toArray();
//					result.addAll(sequences);
				}
			}
			return result.toArray();
		} else if (parent instanceof Schedule) {
			EList<Sequence> sequences = ((Schedule) parent).getSequences();
			List<Sequence> seqs = new ArrayList<Sequence>(sequences.size());
			for (Sequence seq : sequences) {
				// TODO: Need a proper flag
				if (seq.getName().equals("<no vessel>")) {
					if (seq.getEvents().size() > 0) {
						seqs.add(seq);
					}
				} else {
					seqs.add(seq);
				}
			}
			return seqs.toArray();
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
		return (element instanceof Sequence) || (element instanceof Schedule);
	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

	}

	@Override
	public Calendar getElementStartTime(final Object element) {
		if (element instanceof Event) {
			final Event event = (Event) element;
			final Calendar c = Calendar.getInstance();

			final Date startTime = event.getStart();
			if (startTime != null) {
				c.setTime(startTime);
				return c;
			}
		}
		return null;
	}

	@Override
	public Calendar getElementEndTime(final Object element) {
		if (element instanceof Event) {
			final Event event = (Event) element;
			final Calendar c = Calendar.getInstance();

			final Date endTime = event.getEnd();
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
			final Calendar c = Calendar.getInstance();
			final Slot slot = visit.getSlotAllocation().getSlot();
			if (slot != null) {
				final Date windowStart = slot.getWindowStartWithSlotOrPortTime();
				if (windowStart != null) {
					c.setTime(windowStart);
					return c;
				}
			}
		}

		return null;
	}

	@Override
	public Calendar getElementPlannedEndTime(final Object element) {
		if (element instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) element;
			final Calendar c = Calendar.getInstance();
			final Slot slot = visit.getSlotAllocation().getSlot();
			if (slot != null) {
				final Date windowStart = slot.getWindowStartWithSlotOrPortTime();
				if (windowStart != null) {
					c.setTime(windowStart);
					c.add(Calendar.HOUR, slot.getSlotOrPortDuration());
					return c;
				}
			}
		}

		return null;
	}
}
