/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.service;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;

public abstract class EquivalenceService {

	

	// Handy data mapping methods
	public abstract EObject getModelObjectForEvent(Event event);

	public abstract Event getEventForModelObject(EObject eObject);

	public abstract EventGrouping getEventGroupForModelObject(EObject eObject);

	public abstract ProfitAndLossContainer getProfitAndLossContainerForModelObject(EObject eObject);

//	public <T extends EObject> T getPinDiffEquivalent(T t);
//
//	public <T extends EObject> T getEquivalentFor(T t, Object scenario);
//
//	private IScenarioServiceListener l = new IScenarioServiceListener() {
//
//		@Override
//		public void onPreScenarioInstanceUnload(IScenarioService scenarioService, ScenarioInstance scenarioInstance) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void onPreScenarioInstanceSave(IScenarioService scenarioService, ScenarioInstance scenarioInstance) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void onPreScenarioInstanceLoad(IScenarioService scenarioService, ScenarioInstance scenarioInstance) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void onPreScenarioInstanceDelete(IScenarioService scenarioService, ScenarioInstance scenarioInstance) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void onPostScenarioInstanceUnload(IScenarioService scenarioService, ScenarioInstance scenarioInstance) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void onPostScenarioInstanceSave(IScenarioService scenarioService, ScenarioInstance scenarioInstance) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void onPostScenarioInstanceLoad(IScenarioService scenarioService, ScenarioInstance scenarioInstance) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void onPostScenarioInstanceDelete(IScenarioService scenarioService, ScenarioInstance scenarioInstance) {
//			// TODO Auto-generated method stub
//
//		}
//	};
//
//	private static class MappingRecord {
//
//		public void linkEventGrouping(Slot slot, EventGrouping eventGrouping) {
//
//		}
//
//		public void linkEventGrouping(VesselEvent vesselEvent, EventGrouping eventGrouping) {
//
//		}
//
//		public void linkEventGrouping(Event scheduleEvent, EventGrouping eventGrouping) {
//
//		}
//
//		public void linkProfitAndLossContainer(Slot slot, ProfitAndLossContainer profitAndLossContainer) {
//
//		}
//
//		public void linkProfitAndLossContainer(VesselEvent vesselEvent, ProfitAndLossContainer profitAndLossContainer) {
//
//		}
//
//		public void linkProfitAndLossContainer(Event scheduleEvent, ProfitAndLossContainer profitAndLossContainer) {
//
//		}
//
//		// All known objects
//		public Collection<EObject> getKeys() {
//			
//		}
//	}

//	WeakHashMap<EObject, MappingRecord> objectLinks;
//WeakHashMap<ScenarioInstance, MappingRecord> scenarioLinks;
//	
//	private void generateDataMapping(ScenarioInstance instance) {
//		try (ModelReference ref = instance.getReference()) {
//			// Ensure loaded.
//			ref.getInstance();
//
//			MappingRecord rec = new MappingRecord();
//
//			for (Event evt : schedule.getSequences()) {
//				if (evt instanceof SlotVisit) {
//					SlotVisit slotVisit = (SlotVisit) evt;
//					rec.link(slotVisit.getSlotAllocation(), slotVisit);
//					rec.link(slotVisit.getSlotAllocation().getSlot(), slotVisit);
//					rec.link(slotVisit.getSlotAllocation().getSlot(), slotVisit.getSlotAllocation());
//
//					rec.linkEventGrouping(slotVisit.getSlotAllocation().getSlot(), slotVisit.getSlotAllocation().getCargoAllocation());
//					rec.linkProfitAndLossContainer(slotVisit.getSlotAllocation().getSlot(), slotVisit.getSlotAllocation().getCargoAllocation());
//				}
//				if (evt instanceof VesselEventVisit) {
//					VesselEventVisit vesselEventVisit = (VesselEventVisit) evt;
//					rec.link(vesselEventVisit.getVesselEvent(), vesselEventVisit);
//
//					rec.linkEventGrouping(vesselEventVisit.getVesselEvent(), vesselEventVisit);
//					rec.linkProfitAndLossContainer(vesselEventVisit.getVesselEvent(), vesselEventVisit);
//				}
//
//				if (evt instanceof EventGrouping) {
//					EventGrouping eventGrouping = (EventGrouping) evt;
//					rec.linkEventGrouping(evt, eventGrouping);
//				}
//				if (evt instanceof ProfitAndLossContainer) {
//					ProfitAndLossContainer profitAndLossContainer = (ProfitAndLossContainer) evt;
//					rec.linkProfitAndLossContainer(evt, profitAndLossContainer);
//
//				}
//			}
//			// Open slot allocations...
//
//		}
//
//	}
}
