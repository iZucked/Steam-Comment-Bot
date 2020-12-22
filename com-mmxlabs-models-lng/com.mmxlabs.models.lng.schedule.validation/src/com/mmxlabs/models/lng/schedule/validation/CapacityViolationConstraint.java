/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.validation;

import java.util.List;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.CapacityViolationsHolder;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class CapacityViolationConstraint extends AbstractModelMultiConstraint {

	private IStatus createCapacityViolationStatus(final IValidationContext ctx, final CapacityViolationType type, final Long amount) {
		EObject target = ctx.getTarget();

		if (!ScheduleModelValidationHelper.isMainScheduleModel(target)) {
			return Status.OK_STATUS;
		}

		// delegate attention to the "real target"
		if (target instanceof SlotVisit) {
			if (((SlotVisit) target).getSlotAllocation() != null) {
				target = ((SlotVisit) target).getSlotAllocation().getSlot();
			}
		}

		if (target == null) {
			return Status.OK_STATUS;
		}

		String nameString = target instanceof NamedObject ? String.format("[%s] ", ((NamedObject) target).getName()) : "";
		if (nameString == "") {
			if (target instanceof VesselEventVisit) {
				nameString = ((VesselEventVisit) target).name();
			}
		}
		final String message;

		// make the right message for the violation type

		// fits into a standard message template?
		final String simpleViolation;
		if (type == CapacityViolationType.MAX_DISCHARGE) {
			simpleViolation = "maximum discharge";
		} else if (type == CapacityViolationType.MAX_LOAD) {
			simpleViolation = "maximum load";
		} else if (type == CapacityViolationType.MIN_DISCHARGE) {
			simpleViolation = "minimum discharge";
		} else if (type == CapacityViolationType.MIN_LOAD) {
			simpleViolation = "minimum load";
		} else {
			simpleViolation = null;
		}

		// standard message template
		if (simpleViolation != null) {
			message = String.format("Volume issue: %s - can't meet %s in schedule.", nameString, simpleViolation);
		}
		// non-standard message
		else {
			if (type == CapacityViolationType.FORCED_COOLDOWN) {
				if (target instanceof GeneratedCharterOut) {
					final String marketName = ((GeneratedCharterOut) target).getCharterOutMarket().getName();
					String vesselName = "";
					if (target.eContainer() instanceof Sequence) {
						final Sequence sequence = (Sequence) target.eContainer();
						if (sequence.getVesselAvailability() != null && sequence.getVesselAvailability().getVessel() != null) {
							vesselName = " for " + sequence.getVesselAvailability().getVessel().getName();
						}
					}
					
					message = String.format("Volume issue: [%s] cooldown is required%s in schedule but is not permitted at load", marketName, vesselName);
				} else {
					message = String.format("Volume issue: cooldown is required in schedule but is not permitted at load %s", nameString);
				}
			} else if (type == CapacityViolationType.VESSEL_CAPACITY) {
				message = String.format("Schedule requires exceeding vessel capacity at load %s", nameString);
			} else {
				String typeDescription = type.getName();
				switch (type) {
				case MIN_HEEL:
					typeDescription = "minimum heel is breached";
					break;
				case MAX_HEEL:
					typeDescription = "maximum heel is breached"; 
					break;
				default:
					typeDescription += " is breached";
					break;
				}
				if (type == CapacityViolationType.MIN_HEEL) {
					typeDescription = "minimum heel is breached";
				}
				message = String.format("Volume issue: [%s] %s in schedule", nameString, typeDescription);
			}
		}

		final DetailConstraintStatusDecorator result = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

		if (target instanceof Slot) {
			result.addEObjectAndFeature(target, CargoPackage.Literals.SLOT__MAX_QUANTITY);
			result.addEObjectAndFeature(target, CargoPackage.Literals.SLOT__MIN_QUANTITY);
		} else {
			target.getClass();
		}
		result.setTag(ValidationConstants.TAG_EVALUATED_SCHEDULE);
		return result;
	}

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof CapacityViolationsHolder) {
			final EMap<CapacityViolationType, Long> violations = ((CapacityViolationsHolder) target).getViolations();

			if (!violations.isEmpty()) {

				for (final Entry<CapacityViolationType, Long> entry : violations) {
					statuses.add(createCapacityViolationStatus(ctx, entry.getKey(), entry.getValue()));
				}

			}
		}

		return Activator.PLUGIN_ID;
	}
}
