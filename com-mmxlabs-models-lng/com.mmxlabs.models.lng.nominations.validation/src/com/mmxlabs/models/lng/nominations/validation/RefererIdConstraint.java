/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.AbstractNominationSpec;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.Side;
import com.mmxlabs.models.lng.nominations.validation.internal.Activator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class RefererIdConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof AbstractNominationSpec && !(target instanceof AbstractNomination) ) {
			final AbstractNominationSpec spec = (AbstractNominationSpec)target;
			final String refererId = spec.getRefererId();	
			if (refererId != null && !"".equals(refererId)) {
				final LNGScenarioModel scenarioModel = (LNGScenarioModel)extraContext.getRootObject();
				final CommercialModel cm = ScenarioModelUtil.getCommercialModel(scenarioModel);
				String contractType = "";
				if (spec.getSide() == Side.BUY) {
					final EList<PurchaseContract> pcs = cm.getPurchaseContracts();
					for (final PurchaseContract pc : pcs) {
						if (pc.getName() != null && pc.getName().equals(spec.getRefererId())) {
							return Activator.PLUGIN_ID;						
						}
					}
					contractType += "Purchase ";
				}
				else if (spec.getSide() == Side.SELL) {
					final EList<SalesContract> pcs = cm.getSalesContracts();
					for (final SalesContract pc : pcs) {
						if (pc.getName() != null && pc.getName().equals(spec.getRefererId())) {
							return Activator.PLUGIN_ID;						
						}
					}
					contractType += "Sales ";
				}
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format(
						"Nomination specification with unknown contract name: \"%s\"", contractType, spec.getRefererId())));
				status.addEObjectAndFeature(spec, NominationsPackage.eINSTANCE.getAbstractNominationSpec_RefererId());
				statuses.add(status);				
			} else {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format(
						"Nomination specification with missing contract name.")));
				status.addEObjectAndFeature(spec, NominationsPackage.eINSTANCE.getAbstractNominationSpec_RefererId());
				statuses.add(status);
			}
		}
		return Activator.PLUGIN_ID;
	}
}
