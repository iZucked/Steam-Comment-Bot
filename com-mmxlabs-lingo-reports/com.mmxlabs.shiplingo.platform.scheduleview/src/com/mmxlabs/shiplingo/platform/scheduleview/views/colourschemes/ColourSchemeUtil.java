/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes;

import java.util.Collection;
import java.util.Date;

import org.eclipse.swt.graphics.RGB;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;

public class ColourSchemeUtil {

	static final RGB Gas_Blue = new RGB(50,60,225);
//	static final RGB Light_Gas_Blue = new RGB(150,255,255);
	static final RGB Light_Gas_Blue = new RGB(150,200,255);
	static final RGB Green = new RGB(0,180,50);
	static final RGB Light_Green = new RGB(40, 255, 50);
	static final RGB Teal = new RGB(0, 120, 120);

	static final RGB Slot_White = new RGB(255,255,255);
	static final RGB FOBDES_Grey = new RGB(96,96,96);
	static final RGB Locked_White = new RGB(255,255,255);
	static final RGB VesselEvent_Purple = new RGB(120, 0, 120);
	static final RGB VesselEvent_LightPurple = new RGB(150, 0, 200);
	static final RGB VesselEvent_Green = new RGB(0, 225, 150);
	static final RGB VesselEvent_Green2 = new RGB(80, 180, 50);
	static final RGB VesselEvent_Green3 = new RGB(50, 200, 80);
//	static final RGB VesselEvent_Brown = new RGB(120, 125, 60);
	static final RGB VesselEvent_Brown = new RGB(77, 88, 50);
		
	static final RGB Warning_Yellow = new RGB(255,255,25);
	static final RGB Warning_Orange = new RGB(255,120,25);
	static final RGB Alert_Crimson = new RGB(255,0,0);
	
	static final int Faded_Alpha = 200;

	
	public static boolean isOutsideTimeWindow(Event ev) {
		Date start = ev.getStart();
		if((ev instanceof VesselEventVisit) && start.after(((VesselEventVisit) ev).getVesselEvent().getStartBy())){
			return true;
		}
		
		if(ev instanceof SlotVisit){
			final SlotVisit visit = (SlotVisit) ev;
			if (visit.getStart().after(visit.getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime())) {
				return true;
			}
//			if (visit.getStart().before(visit.getSlotAllocation().getSlot().getWindowStartWithSlotOrPortTime())) {
//				return true;
//			}
		}
		return false;
	}
	
	public static boolean isLocked(final Event event, GanttChartViewer viewer) {
		// Stage 1: Find the cargo
		final Sequence sequence = event.getSequence();
		int index = sequence.getEvents().indexOf(event);
		Cargo cargo = null;
		while (cargo == null && index >= 0 ) {
			Object obj = sequence.getEvents().get(index);
			if (obj instanceof SlotVisit) {
				final SlotVisit slotVisit = ((SlotVisit) obj);
				final SlotAllocation slotAllocation = slotVisit.getSlotAllocation();
				final CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
				cargo = cargoAllocation.getInputCargo();
			} else if (obj instanceof VesselEventVisit) {
				break;
			}
			--index;
		}

		// Stage 2: Find the input assignment
		if (cargo != null) {

			final Object input = viewer.getInput();
			if (input instanceof IScenarioViewerSynchronizerOutput) {
				final IScenarioViewerSynchronizerOutput output = (IScenarioViewerSynchronizerOutput) input;

				final Collection<Object> collectedElements = output.getCollectedElements();
				if (collectedElements.size() > 0) {
					final ScenarioInstance instance = output.getScenarioInstance(sequence.eContainer());
					final MMXRootObject rootObject = (MMXRootObject) instance.getInstance();
					final InputModel inputModel = rootObject.getSubModel(InputModel.class);
					final ElementAssignment assignment = AssignmentEditorHelper.getElementAssignment(inputModel, cargo);
					if (assignment != null && assignment.isLocked()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	static Idle findIdleForJourney(final Journey journey) {
		final Sequence sequence = journey.getSequence();
		final int index = sequence.getEvents().indexOf(journey);
		if (index != -1 && index + 1 < sequence.getEvents().size()) {
			final Event event = sequence.getEvents().get(index + 1);
			if (event instanceof Idle) {
				return (Idle) event;
			}
		}
		return null;
	}

	
	
	static Journey findJourneyForIdle(final Idle idle) {
		final Sequence sequence = idle.getSequence();
		final int index = sequence.getEvents().indexOf(idle);
		if (index != -1 && index - 1 >= 0) {
			final Event event = sequence.getEvents().get(index - 1);
			if (event instanceof Journey) {
				return (Journey) event;
			}
		}
		return null;
	}

	static boolean isRiskyVoyage(final Journey journey, final Idle idle, float IdleRisk_speed, float IdleRisk_threshold) {

		if (journey == null) {
			return false;
		}

		final int distance = journey.getDistance();
		int totalTime = journey.getDuration();
		if (idle != null) {
			totalTime += idle.getDuration();
		}

		final int travelTime = (int) Math.round((float) distance / IdleRisk_speed);

		return (travelTime / totalTime > IdleRisk_threshold);
	}
	
	public static boolean isSpot(final SlotVisit visit) {
		Slot slot = visit.getSlotAllocation().getSlot();
		if (slot instanceof SpotSlot) {
			return true;
		}
		return false;
	}
	
	public static boolean isFOBSaleCargo(final SlotVisit visit) {
		boolean isFOB;
		Slot slot = visit.getSlotAllocation().getSlot();
		if (slot instanceof LoadSlot) {
			isFOB = ((DischargeSlot) visit.getSlotAllocation().getCargoAllocation().getDischargeAllocation().getSlot()).isFOBSale();				
		}
		else {
			isFOB = ((DischargeSlot) slot).isFOBSale();
		}
		return isFOB;
	}
	
	public static boolean isDESPurchaseCargo(final SlotVisit visit) {
		Slot slot = visit.getSlotAllocation().getSlot();
		if (slot instanceof LoadSlot) {
			return ((LoadSlot) slot).isDESPurchase();				
		}
		else {
			return ((LoadSlot) visit.getSlotAllocation().getCargoAllocation().getLoadAllocation().getSlot()).isDESPurchase();
		}
	}
}
