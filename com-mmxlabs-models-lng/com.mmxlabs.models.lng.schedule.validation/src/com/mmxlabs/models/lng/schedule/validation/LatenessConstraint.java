/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class LatenessConstraint extends AbstractModelMultiConstraint {
	
	@Override
	protected String validate(final IValidationContext ctx, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		
		if (target instanceof Event) {	
			if (LatenessUtils.isLate((Event) target)) {
				EObject obj = null;
				EStructuralFeature feature = null;
				String message = null;
				
				if (target instanceof SlotVisit) {					
					SlotAllocation allocation = ((SlotVisit) target).getSlotAllocation();
					if (allocation != null) {
						obj = allocation.getSlot();
						message = String.format("Vessel reaches %s late in generated schedule.", ((Slot) obj).getName());
						feature = CargoPackage.Literals.SLOT__WINDOW_START;
					}
				}
				else if (target instanceof VesselEventVisit) {
					VesselEvent ve = ((VesselEventVisit) target).getVesselEvent();
					message = String.format("Vessel reaches %s late in generated schedule.", ve.getName());
					obj = ve;
					feature = CargoPackage.Literals.VESSEL_EVENT__START_BY;
				}
				else if (target instanceof EndEvent) {
					EndEvent event = (EndEvent) target;
					obj = event.getSequence().getVesselAvailability();
					feature = CargoPackage.Literals.VESSEL_AVAILABILITY__END_BY;
					message = "Generated schedule has vessel travelling after it is no longer available.";					
				}
				else {
					message = "Late arrival in generated schedule.";					
				}
				
				if (message != null) {
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					if (obj != null) {
						failure.addEObjectAndFeature(obj, feature);
					}
					statuses.add(failure);
				}
				
			}
		}
		
		return Activator.PLUGIN_ID;
	}
}
