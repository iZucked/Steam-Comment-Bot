package com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes;

import java.util.Collection;

import org.eclipse.swt.graphics.RGB;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;

public class ColourSchemeUtil {

	static final RGB Gas_Blue = new RGB(50,60,225);
//	static final RGB Light_Gas_Blue = new RGB(150,255,255);
	static final RGB Light_Gas_Blue = new RGB(150,200,255);
	static final RGB Green = new RGB(0,180,50);
	static final RGB Light_Green = new RGB(40, 255, 50);

	static final RGB Slot_White = new RGB(255,255,255);
	static final RGB Locked_White = new RGB(255,255,255);
	static final RGB VesselEvent_Purple = new RGB(120, 0, 120);
	
	static final RGB Warning_Yellow = new RGB(255,255,25);
	static final RGB Alert_Crimson = new RGB(255,0,0);
	
	private static final float IdleRisk_threshold = 0.95f;
	private static final float IdleRisk_speed = 19.0f;

	
	public  static boolean isLocked(final Event event, GanttChartViewer viewer) {
		// Stage 1: Find the cargo
		final Sequence sequence = (Sequence) event.eContainer();
		int index = sequence.getEvents().indexOf(event);
		Cargo cargo = null;
		while (cargo == null && index >= 0 ) {
			Object obj = sequence.getEvents().get(index);
			if (obj instanceof SlotVisit) {
				final SlotVisit slotVisit = ((SlotVisit) obj);
				final SlotAllocation slotAllocation = slotVisit.getSlotAllocation();
				final CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
				cargo = cargoAllocation.getInputCargo();
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
					if (inputModel.getLockedAssignedObjects().contains(cargo)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	static Idle findIdleForJourney(final Journey journey) {
		final Sequence sequence = (Sequence) journey.eContainer();
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
		final Sequence sequence = (Sequence) idle.eContainer();
		final int index = sequence.getEvents().indexOf(idle);
		if (index != -1 && index - 1 >= 0) {
			final Event event = sequence.getEvents().get(index - 1);
			if (event instanceof Journey) {
				return (Journey) event;
			}
		}
		return null;
	}

	static boolean isRiskyVoyage(final Journey journey, final Idle idle) {

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
	
	
}
