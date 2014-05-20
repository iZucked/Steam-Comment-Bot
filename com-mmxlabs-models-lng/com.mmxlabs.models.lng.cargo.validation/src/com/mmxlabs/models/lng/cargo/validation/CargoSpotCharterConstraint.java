/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterCostModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * @author Simon Goodall
 * 
 */
public class CargoSpotCharterConstraint extends AbstractModelMultiConstraint {
	
	private boolean isMatchingSpotMarketExist(VesselClass vc) {
		final IExtraValidationContext extraValidationContext = Activator.getDefault().getExtraValidationContext();
		final MMXRootObject rootObject = extraValidationContext.getRootObject();
		if (rootObject instanceof LNGScenarioModel) {
			SpotMarketsModel spotModel = ((LNGScenarioModel) rootObject).getSpotMarketsModel();
			if (spotModel == null) {
				return false;
			}
			for (CharterCostModel market: spotModel.getCharteringSpotMarkets()) {
				if (market.getVesselClasses().contains(vc)) {
					return true;
				}
			}			
		}		
		return false;
	}
	
	@Override
	protected String validate(final IValidationContext ctx, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof Cargo) {
			Cargo cargo = (Cargo) target;
			AVesselSet<? extends Vessel> assignment = cargo.getAssignment();
			if (assignment instanceof VesselClass && (isMatchingSpotMarketExist((VesselClass) assignment) == false)) {
				final String message = String.format("No charter market provides %s vessels as required by cargo '%s'", assignment.getName(), cargo.getName());
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dcsd.addEObjectAndFeature(target, CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT);
				statuses.add(dcsd);				
			}			
		}
		return Activator.PLUGIN_ID;
	}
}
