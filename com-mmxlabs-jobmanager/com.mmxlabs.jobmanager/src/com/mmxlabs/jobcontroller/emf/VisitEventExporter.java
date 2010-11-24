/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.jobcontroller.emf;

import org.eclipse.emf.common.util.EMap;

import scenario.cargo.Slot;
import scenario.schedule.SlotVisit;

import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;

/**
 * Exporter for getting out the details of {@link IPortVisitEvent}
 * 
 * @author hinton
 * 
 */
public class VisitEventExporter extends BaseAnnotationExporter {
	private EMap<Slot, SlotVisit> slotVisits;

	@Override
	public void init() {
		this.slotVisits = output.getSlotVisits();
	}

	@Override
	public void exportAnnotation(final ISequenceElement element, final Object annotation) {
		assert annotation instanceof IPortVisitEvent;
		
		@SuppressWarnings("unchecked")
		final IPortVisitEvent<ISequenceElement> event = (IPortVisitEvent<ISequenceElement>) annotation;
		
		final Slot eSlot = entities.getModelObject(event.getPortSlot(), Slot.class);
		if (eSlot != null) {
			final SlotVisit visit = factory.createSlotVisit();
			slotVisits.put(eSlot, visit);
			
			visit.setStartTime(entities.getDateFromHours(event.getStartTime()));
			visit.setEndTime(entities.getDateFromHours(event.getEndTime()));
		}
	}
}
