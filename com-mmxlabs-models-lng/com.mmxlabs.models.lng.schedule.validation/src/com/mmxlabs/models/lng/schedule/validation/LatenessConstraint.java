/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class LatenessConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof Event && target.eContainer() instanceof Sequence) {
			if (LatenessUtils.isLateExcludingFlex((Event) target)) {
				EObject obj = null;
				EStructuralFeature feature = null;
				String message = null;
				boolean multipleAvailabilityError = false;
				if (target instanceof SlotVisit) {
					SlotAllocation allocation = ((SlotVisit) target).getSlotAllocation();
					if (allocation != null) {
						obj = allocation.getSlot();
						message = String.format(Constants.GENERATED_SCHEDULE_LABEL + " Vessel reaches %s late in schedule.", ((Slot) obj).getName());
						feature = CargoPackage.Literals.SLOT__WINDOW_START;
					}
				} else if (target instanceof VesselEventVisit) {
					VesselEvent ve = ((VesselEventVisit) target).getVesselEvent();
					message = String.format(Constants.GENERATED_SCHEDULE_LABEL + " Vessel reaches %s late in schedule.", ve.getName());
					obj = ve;
					feature = CargoPackage.Literals.VESSEL_EVENT__START_BY;
				} else if (target instanceof EndEvent) {
					EndEvent event = (EndEvent) target;
					obj = event.getSequence().getVesselAvailability();
					feature = CargoPackage.Literals.VESSEL_AVAILABILITY__END_BY;
					message = Constants.GENERATED_SCHEDULE_LABEL + " Schedule has vessel travelling after it is no longer available.";
					multipleAvailabilityError = true;
				} else {
					message = Constants.GENERATED_SCHEDULE_LABEL + " Late arrival in schedule.";
				}

				if (message != null) {
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message), multipleAvailabilityError ? IStatus.ERROR : IStatus.WARNING);
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
