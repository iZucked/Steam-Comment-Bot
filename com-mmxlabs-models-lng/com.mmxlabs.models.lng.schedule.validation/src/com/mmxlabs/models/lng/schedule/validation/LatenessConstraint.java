/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
import com.mmxlabs.models.lng.cargo.ui.util.AssignmentLabelProvider;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class LatenessConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (!ScheduleModelValidationHelper.isMainScheduleModel(target)) {
			return Activator.PLUGIN_ID;
		}
		if (target instanceof Event && target.eContainer() instanceof Sequence) {
			if (LatenessUtils.isLateExcludingFlex((Event) target)) {
				EObject obj = null;
				EStructuralFeature feature = null;
				String message = null;
				if (target instanceof SlotVisit) {
					final SlotAllocation allocation = ((SlotVisit) target).getSlotAllocation();
					Sequence seq = allocation.getCargoAllocation().getSequence();
					String tmp = "";
					if (seq.isSetVesselAvailability() && seq.getVesselAvailability() != null) {
			            tmp = seq.getVesselAvailability().getVessel().getName();
						tmp = AssignmentLabelProvider.getLabelFor(seq.getVesselAvailability().getVessel(), false);
					} else if (seq.isSetCharterInMarket() && seq.getCharterInMarket() != null) {
						CharterInMarket cim = seq.getCharterInMarket();
						tmp = AssignmentLabelProvider.getLabelFor(cim, seq.getSpotIndex(), false);
					}		        
					if(tmp.isEmpty()) tmp ="Vessel";
							
					if (allocation != null) {
						obj = allocation.getSlot();
						message = String.format("%s is %s hrs late to reach %s in schedule.", tmp, LatenessUtils.getLatenessInHours((SlotVisit) target), ((Slot) obj).getName());
						feature = CargoPackage.Literals.SLOT__WINDOW_START;
					}
				} else if (target instanceof VesselEventVisit) {
					final VesselEvent ve = ((VesselEventVisit) target).getVesselEvent();
					message = String.format("Vessel reaches %s late in schedule.", ve.getName());
					obj = ve;
					feature = CargoPackage.Literals.VESSEL_EVENT__START_BY;
				} else if (target instanceof EndEvent) {
					final EndEvent event = (EndEvent) target;
					obj = event.getSequence().getVesselAvailability();
					feature = CargoPackage.Literals.VESSEL_AVAILABILITY__END_BY;
					message = "Schedule has vessel travelling after it is no longer available.";
				} else {
					message = "Late arrival in schedule.";
				}

				if (message != null) {
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message), IStatus.WARNING);
					if (obj != null) {
						failure.addEObjectAndFeature(obj, feature);
					}
					failure.setTag(ValidationConstants.TAG_EVALUATED_SCHEDULE);

					statuses.add(failure);
				}

			}
		}

		
//		if (target instanceof Event && target.eContainer() instanceof Sequence) {
//			if (LatenessUtils.isLateExcludingFlex((Event) target)) {
//				EObject obj = null;
//				EStructuralFeature feature = null;
//				String message = null;
//				String vessel = "Vessel";
//				if (target instanceof SlotVisit) {
//					final SlotAllocation allocation = ((SlotVisit) target).getSlotAllocation();
//					Sequence seq = allocation.getCargoAllocation().getSequence();
//					vessel = getVesselNamefromSequence(vessel, seq);		        							
//					if (allocation != null) {
//						message = String.format("%s is %s hrs late to reach %s.", vessel, LatenessUtils.getLatenessInHours((SlotVisit) target), ((Slot) obj).getName());
//						obj = allocation.getSlot();
//						feature = CargoPackage.Literals.SLOT__WINDOW_START;
//					}
//				} else if (target instanceof VesselEventVisit) {
//					final VesselEvent ve = ((VesselEventVisit) target).getVesselEvent();
//					VesselAssignmentType vat = ve.getVesselAssignmentType();
//					if (vat instanceof VesselAvailability) {
//						VesselAvailability va = (VesselAvailability) vat;
//						vessel = va.getVessel().getName();
//					}
//					message = String.format("%s reaches '%s' late.", vessel, ve.getName());
//					obj = ve;
//					feature = CargoPackage.Literals.VESSEL_EVENT__START_BY;
//				} else if (target instanceof EndEvent) {
//					final EndEvent event = (EndEvent) target;
//					vessel = getVesselNamefromSequence(vessel, event.getSequence());
//					message = String.format("%s is travelling after it is no longer available.", vessel);
//					obj = event.getSequence().getVesselAvailability();
//					feature = CargoPackage.Literals.VESSEL_AVAILABILITY__END_BY;
//				} else {
//					message = "Late arrival in schedule.";
//				}
//
//				if (message != null) {
//					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message), IStatus.WARNING);
//					if (obj != null) {
//						failure.addEObjectAndFeature(obj, feature);
//					}
//					failure.setTag(ValidationConstants.TAG_EVALUATED_SCHEDULE);
//					statuses.add(failure);
//				}
//			}
//		}

		return Activator.PLUGIN_ID;
	}
}
