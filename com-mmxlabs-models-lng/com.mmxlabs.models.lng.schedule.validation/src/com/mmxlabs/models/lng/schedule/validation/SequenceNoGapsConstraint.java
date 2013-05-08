/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.validation;

import java.util.Date;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;

/**
 * Check that output sequences have no un-accounted for time in them.
 * @author Tom Hinton
 *
 */
public class SequenceNoGapsConstraint extends AbstractModelConstraint {


	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject object = ctx.getTarget();
		
		
		if (object instanceof Sequence) {
			final Sequence sequence = (Sequence) object;
			Date lastEndDate = (sequence.getEvents().isEmpty() ? null : sequence.getEvents().get(0).getStart());

			for (final Event event : sequence.getEvents()) {
				final Date eventStart = event.getStart();
				final Date eventEnd = event.getEnd();
				if (eventStart == null || eventEnd == null)
					continue;
				if (eventStart.before(lastEndDate)) {
					return ctx.createFailureStatus(sequence.getVesselAvailability().getVessel().getName());
				} else if (eventStart.after(lastEndDate)) {
					return ctx.createFailureStatus(sequence.getVesselAvailability().getVessel().getName());
				}
				lastEndDate = eventEnd;
			}
		}
		return ctx.createSuccessStatus();
	}

}
