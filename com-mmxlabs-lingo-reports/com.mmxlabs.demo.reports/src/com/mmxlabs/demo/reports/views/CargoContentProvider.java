package com.mmxlabs.demo.reports.views;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.optimiser.core.IAnnotatedSequence;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.events.IDischargeEvent;
import com.mmxlabs.scheduler.optimiser.events.ILoadEvent;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.events.IScheduledEvent;

/**
 * Content provider for the {@link CargoReportView}.
 * 
 * @author Simon Goodall
 * 
 */
public class CargoContentProvider implements IStructuredContentProvider {

	class RowData {
		String id;
//		IVessel vessel;
		String vessel;
		IPortSlot loadSlot;
		IPortSlot dischargeSlot;
		Calendar loadDate;
		Calendar dischargeDate;
		long loadVolume;
		long dischargeVolume;
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

		// Mapping between cargo ID and rowData.
		final Map<String, RowData> rowData = new HashMap<String, RowData>();

		for (final IResource resource : solution.getContext().getOptimisationData().getResources()) {

			ISequence<ISequenceElement> sequence = solution.getSequences()
					.getSequence(resource);
			IAnnotatedSequence<ISequenceElement> annotatedSequence = solution
					.getAnnotatedSequence(resource);

			RowData rd = null;
			for (final ISequenceElement o : sequence) {

				final IPortVisitEvent<ISequenceElement> event = annotatedSequence.getAnnotation(
						o, SchedulerConstants.AI_visitInfo,
						IPortVisitEvent.class);

				final IPortSlot slot = event.getPortSlot();

				if (slot instanceof ILoadSlot) {
					assert event instanceof ILoadEvent;
					final ILoadSlot loadSlot = (ILoadSlot) slot;
					final String id = loadSlot.getId();

					// final String cargoId = event.getCargoId();
					final String cargoId = slot.getId();

						rd = new RowData();
						rd.id = cargoId;
						rd.vessel = resource.getName();
						rowData.put(cargoId, rd);

					rd.loadSlot = loadSlot;
					rd.loadDate = getElementStartTime(event);
					rd.loadVolume = ((ILoadEvent) event).getLoadVolume();
					
				} else if (slot instanceof IDischargeSlot) {
					assert event instanceof IDischargeEvent;
					final IDischargeSlot dischargeSlot = (IDischargeSlot) slot;
					final String id = dischargeSlot.getId();

					rd.dischargeSlot = dischargeSlot;
					rd.dischargeDate = getElementStartTime(event);
					rd.dischargeVolume = ((IDischargeEvent) event)
							.getDischargeVolume();
					
					// Null entry as next one should be a load
					rd = null;
				}
			}

			cargoes = new RowData[rowData.size()];
			int idx = 0;
			for (final Map.Entry<String, RowData> entry : rowData.entrySet()) {
				cargoes[idx++] = entry.getValue();
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
