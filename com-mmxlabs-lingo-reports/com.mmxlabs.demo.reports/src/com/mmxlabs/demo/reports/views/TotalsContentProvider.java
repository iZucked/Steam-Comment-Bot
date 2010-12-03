/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.demo.reports.views;

import java.util.EnumMap;
import java.util.Map;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IAnnotations;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.events.IIdleEvent;
import com.mmxlabs.scheduler.optimiser.events.IJourneyEvent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

/**
 * Content provider for the {@link CargoReportView}.
 * 
 * @author Simon Goodall
 * 
 */
public class TotalsContentProvider implements IStructuredContentProvider {

	public class RowData {
		RowData(String c, long f) {
			this.component = c;
			this.fitness = f;
		}

		public String component;
		public long fitness;
	}

	private RowData[] rowData = new RowData[0];

	@Override
	public Object[] getElements(final Object inputElement) {

		return rowData;
	}

	@Override
	public synchronized void inputChanged(final Viewer viewer,
			final Object oldInput, final Object newInput) {

		IAnnotatedSolution<ISequenceElement> solution = null;
		if (newInput instanceof IAnnotatedSolution) {
			solution = (IAnnotatedSolution) newInput;
		}

		// Process input dats.
		if (solution == null) {
			rowData = new RowData[0];
			return;
		}

		EnumMap<FuelComponent, Long> fuelTotals = new EnumMap<FuelComponent, Long>(
				FuelComponent.class);
		// Init totals
		for (FuelComponent f : FuelComponent.values()) {
			fuelTotals.put(f, 0l);
		}
		long distance = 0l;

		long totalCost = 0l;
		
		for (final IResource resource : solution.getContext().getOptimisationData().getResources()) {

			ISequence<ISequenceElement> sequence = solution.getSequences()
					.getSequence(resource);
			IAnnotations<ISequenceElement> annotatedSequence = solution
					.getElementAnnotations();

			for (final ISequenceElement o : sequence) {

				final IJourneyEvent<ISequenceElement> journey = annotatedSequence
						.getAnnotation(o, SchedulerConstants.AI_journeyInfo,
								IJourneyEvent.class);

				if (journey != null) {
					for (FuelComponent f : FuelComponent.values()) {
						long l = fuelTotals.get(f);
						l += journey.getFuelCost(f);
						fuelTotals.put(f, l);
						totalCost += journey.getFuelCost(f);
					}
					distance += journey.getDistance();
				}
				final IIdleEvent<ISequenceElement> idle = annotatedSequence
						.getAnnotation(o, SchedulerConstants.AI_idleInfo,
								IIdleEvent.class);
				if (idle != null) {
					for (FuelComponent f : FuelComponent.values()) {
						long l = fuelTotals.get(f);
						l += idle.getFuelCost(f);
						fuelTotals.put(f, l);
						totalCost += idle.getFuelCost(f);
					}
				}
			}
		}
		rowData = new RowData[fuelTotals.size() + 2];

		int idx = 0;
		for (final Map.Entry<FuelComponent, Long> entry : fuelTotals.entrySet()) {

			rowData[idx++] = new RowData(entry.getKey().toString(),
					entry.getValue() / 1000);
		}
		rowData[idx++] = new RowData("Distance", distance);
		rowData[idx++] = new RowData("Total Cost", totalCost / 1000);
	}

	@Override
	public void dispose() {

	}
}
