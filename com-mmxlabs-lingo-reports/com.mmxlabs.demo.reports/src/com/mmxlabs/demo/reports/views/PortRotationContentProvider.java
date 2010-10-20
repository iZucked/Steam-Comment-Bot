/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.demo.reports.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.optimiser.core.IAnnotatedSequence;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.events.IScheduledEvent;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * Content provider for the {@link CargoReportView}.
 * 
 * @author Simon Goodall
 * 
 */
public class PortRotationContentProvider implements IStructuredContentProvider {

	class RowData {
		String vessel;
		Calendar date;
		IPortSlot portSlot;
		PortType portSlotType;
		String portSlotID;
	}

	private RowData[] cargoes = new RowData[0];

	@Override
	public Object[] getElements(final Object inputElement) {

		return cargoes;
	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput,
			final Object newInput) {

		IAnnotatedSolution<ISequenceElement> solution = null;
		if (newInput instanceof IAnnotatedSolution) {
			solution = (IAnnotatedSolution) newInput;
		}

		// Process input dats.
		if (solution == null) {
			cargoes = new RowData[0];
			return;
		}

		IPortTypeProvider portTypeProvider = solution.getContext().getOptimisationData().getDataComponentProvider(SchedulerConstants.DCP_portTypeProvider, IPortTypeProvider.class);
		
		// Mapping between cargo ID and rowData.
		final List<RowData> rowData = new ArrayList<RowData>();

		for (final IResource resource : solution.getContext().getOptimisationData().getResources()) {

			ISequence<ISequenceElement> sequence = solution.getSequences()
					.getSequence(resource);
			IAnnotatedSequence<ISequenceElement> annotatedSequence = solution
					.getAnnotatedSequence(resource);

			for (final ISequenceElement o : sequence) {

				final IPortVisitEvent<ISequenceElement> event = annotatedSequence.getAnnotation(
						o, SchedulerConstants.AI_visitInfo,
						IPortVisitEvent.class);

				final IPortSlot slot = event.getPortSlot();
				RowData rd = new RowData();
				rd.vessel = resource.getName();
				rd.date = getElementStartTime(event);
				rd.portSlot = slot;
				rd.portSlotID = slot.getId();
				rd.portSlotType = portTypeProvider.getPortType(o) ;
				
				rowData.add(rd);
				
			}

			cargoes = new RowData[rowData.size()];
			int idx = 0;
			for (final  RowData entry : rowData) {
				cargoes[idx++] = entry;
			}
		}

	}

	public Calendar getElementStartTime(final Object element) {

		if (element instanceof IScheduledEvent) {
			final IScheduledEvent event = (IScheduledEvent) element;
			final int startTime = event.getStartTime();
			// TODO: Get proper start time
			final Calendar c = Calendar.getInstance();
			c.add(Calendar.HOUR_OF_DAY, startTime);

			return c;
		}

		return null;
	}

	public Calendar getElementEndTime(final Object element) {

		if (element instanceof IScheduledEvent) {
			final IScheduledEvent event = (IScheduledEvent) element;
			final int endTime = event.getEndTime();
			// TODO: Get proper end time
			final Calendar c = Calendar.getInstance();
			c.add(Calendar.HOUR_OF_DAY, endTime);

			return c;
		}

		return null;
	}

	@Override
	public void dispose() {

	}
}
