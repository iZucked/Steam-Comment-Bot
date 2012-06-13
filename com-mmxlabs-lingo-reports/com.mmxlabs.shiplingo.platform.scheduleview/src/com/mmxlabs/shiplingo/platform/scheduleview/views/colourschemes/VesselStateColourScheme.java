/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes;

import java.util.Collection;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.input.Assignment;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.shiplingo.platform.scheduleview.views.IScheduleViewColourScheme;

public class VesselStateColourScheme implements IScheduleViewColourScheme {

	@Override
	public String getName() {
		return "Vessel State";
	}

	@Override
	public Color getForeground(final Object element) {
		return null;
	}

	@Override
	public Color getBackground(final Object element) {
		if (element instanceof Journey) {
			final Journey journey = (Journey) element;
			if (journey.isLaden()) {
				return ColorCache.getColor(0, 255, 0);
			} else {
				return ColorCache.getColor(0, 0, 255);
			}
		} else if (element instanceof VesselEventVisit) {
			return ColorCache.getColor(223, 115, 255);
		}

		// else if (mode == Mode.Lateness) {
		if (element instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) element;
			if (visit.getStart().after(visit.getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime())) {
				return ColorCache.getColor(255, 0, 0);
			}
			return ColorCache.getColor(0, 0, 0);
		} else if (element instanceof VesselEventVisit) {
			final VesselEventVisit vev = (VesselEventVisit) element;
			if (vev.getStart().after(vev.getVesselEvent().getStartBy())) {
				return ColorCache.getColor(255, 0, 0);
			}
		}
		return null;
	}

	@Override
	public int getAlpha(final Object element) {
		return 255;
	}

	@Override
	public Color getBorderColour(final Object element) {

		if (element instanceof Event) {
			final Event event = (Event) element;

			// Stage 1: Find the cargo
			final Sequence sequence = (Sequence) event.eContainer();
			int index = sequence.getEvents().indexOf(event);
			Cargo cargo = null;
			while (cargo == null && index != -1) {
				if (event instanceof SlotVisit) {
					final SlotVisit slotVisit = ((SlotVisit) event);
					final SlotAllocation slotAllocation = slotVisit.getSlotAllocation();
					final CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
					cargo = cargoAllocation.getInputCargo();
				}
				--index;
			}

			// Stage 2: Find the input assignment
			if (cargo != null) {

				// TODO: Get the input model - no access to viewer here?
				final Object input = null;// viewer.getInput();
				if (input instanceof IScenarioViewerSynchronizerOutput) {
					final IScenarioViewerSynchronizerOutput output = (IScenarioViewerSynchronizerOutput) input;

					final Collection<Object> collectedElements = output.getCollectedElements();
					if (collectedElements.size() > 1) {
						final ScenarioInstance instance = output.getScenarioInstance(sequence.eContainer());
						final MMXRootObject rootObject = (MMXRootObject) instance.getInstance();
						final InputModel inputModel = rootObject.getSubModel(InputModel.class);
						for (final Assignment assignment : inputModel.getAssignments()) {
							if (assignment.getAssignedObjects().contains(cargo)) {
								return ColorCache.getWhite();
							}
						}
					}
				}
			}
		}
		return null;
	}
}
