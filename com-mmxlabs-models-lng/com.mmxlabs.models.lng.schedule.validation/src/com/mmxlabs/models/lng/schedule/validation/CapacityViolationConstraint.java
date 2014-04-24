/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.validation;

import java.util.List;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.CapacityViolationsHolder;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class CapacityViolationConstraint extends AbstractModelMultiConstraint {
	
	private IStatus createCapacityViolationStatus(final IValidationContext ctx, final CapacityViolationType type, final Long amount) {
		EObject target = ctx.getTarget();
		
		// delegate attention to the "real target"
		if (target instanceof SlotVisit) {
			if (((SlotVisit) target).getSlotAllocation() != null) {
				target = ((SlotVisit) target).getSlotAllocation().getSlot();
			}
		}
		
		final String nameString = target instanceof NamedObject ? String.format("[%s] ", ((NamedObject) target).getName()) : ""; 
		final String message;
		
		// make the right message for the violation type
		
		// fits into a standard message template?
		final String simpleViolation;
		if (type == CapacityViolationType.MAX_DISCHARGE) {
			simpleViolation = "maximum discharge";
		}
		else if (type == CapacityViolationType.MAX_LOAD) {
			simpleViolation = "maximum load";
		}
		else if (type == CapacityViolationType.MIN_DISCHARGE) {
			simpleViolation = "minimum discharge";
		}
		else if (type == CapacityViolationType.MIN_LOAD) {
			simpleViolation = "minimum load";
		}
		else {
			simpleViolation = null;
		}
		
		// standard message template
		if (simpleViolation != null) {
			message = String.format("%sCan't meet %s in generated schedule.", nameString, simpleViolation);
		}
		// non-standard message
		else {
			if (type == CapacityViolationType.FORCED_COOLDOWN) {
				message = String.format("Cooldown is required in generated schedule but is not permitted at load %s", nameString);
			}
			else if (type == CapacityViolationType.VESSEL_CAPACITY) {
				message = String.format("Generated schedule requires exceeding vessel capacity at load %s", nameString);
			}
			else {
				message = String.format("%sCapacity violation %s in generated schedule.", nameString, type.getName());
			}
		}
		
		final DetailConstraintStatusDecorator result = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
		
		if (target instanceof Slot) {
			result.addEObjectAndFeature(target, CargoPackage.Literals.SLOT__MAX_QUANTITY);
			result.addEObjectAndFeature(target, CargoPackage.Literals.SLOT__MIN_QUANTITY);
		}
		else {
			target.getClass();
		}
		
		return result;
	}
	
	@Override
	protected String validate(final IValidationContext ctx, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		
		if (target instanceof CapacityViolationsHolder) {			
			EMap<CapacityViolationType, Long> violations = ((CapacityViolationsHolder) target).getViolations();
			
			if (violations.isEmpty() == false) {
				
				for (Entry<CapacityViolationType, Long> entry: violations) {	
					if (entry.getValue() != 0) {
						statuses.add(createCapacityViolationStatus(ctx, entry.getKey(), entry.getValue()));
					}
				}
				
			}
		}
		
		return Activator.PLUGIN_ID;
	}
}
