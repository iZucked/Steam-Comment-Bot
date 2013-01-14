/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class ContractCvValueConstraint extends AbstractModelConstraint  {

	@Override
	public IStatus validate(IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		final List<IStatus> failures = new LinkedList<IStatus>();
				
		if (target instanceof Cargo) {
			final Cargo cargo = (Cargo) target;
			DischargeSlot dischargeSlot = cargo.getDischargeSlot();
			
			final Contract contract = dischargeSlot.getContract();
			if (contract instanceof SalesContract) {
				LoadSlot loadSlot = cargo.getLoadSlot();
				final SalesContract salesContract = (SalesContract) contract;
				double cv = loadSlot.getSlotOrPortCV();
				String format = "Cargo '%s' has CV %.2f which is %s than the %s CV (%.2f) for the sales contract '%s'.";
				if (salesContract.isSetMinCvValue()) {
					double minCvValue = salesContract.getMinCvValue(); 
					if (cv < minCvValue) {
						final String failureMessage = String.format(format, cargo.getName(), cv, "less", "minimum", minCvValue, contract.getName());
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
						dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_Contract());
						dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getLoadSlot_CargoCV());
						failures.add(dsd);
					}
				}
				if (salesContract.isSetMaxCvValue()) {
					double maxCvValue = salesContract.getMaxCvValue(); 
					if (cv > maxCvValue) {
						final String failureMessage = String.format(format, cargo.getName(), cv, "more", "maximum", maxCvValue, contract.getName());
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
						dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_Contract());
						dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getLoadSlot_CargoCV());
						failures.add(dsd);
					}					
				}
			}
		}
		
		if (failures.isEmpty()) {
			return ctx.createSuccessStatus();
		} else if (failures.size() == 1) {
			return failures.get(0);
		} else {
			final MultiStatus multi = new MultiStatus(Activator.PLUGIN_ID, IStatus.ERROR, null, null);
			for (final IStatus s : failures) {
				multi.add(s);
			}
			return multi;
		}
	}

}
