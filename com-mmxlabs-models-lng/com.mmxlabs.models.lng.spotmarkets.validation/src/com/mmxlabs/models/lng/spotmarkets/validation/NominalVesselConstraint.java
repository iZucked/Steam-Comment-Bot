/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.validation;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.spotmarkets.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Ensure there is at least one nominal vessel market
 * 
 * @author Simon Goodall
 *
 */
public class NominalVesselConstraint extends AbstractModelMultiConstraint {
	@Override
	protected String validate(@NonNull IValidationContext ctx, @NonNull IExtraValidationContext extraContext, @NonNull List<IStatus> failures) {

		final EObject target = ctx.getTarget();

		if (target instanceof SpotMarketsModel) {
			SpotMarketsModel spotMarketsModel = (SpotMarketsModel) target;

			if (spotMarketsModel.getDefaultNominalMarket() == null) {
				if (LicenseFeatures.isPermitted("features:default-nominal-vessels")) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("A default charter-in market should be specified for nominal cargoes."));
					dsd.addEObjectAndFeature(spotMarketsModel, SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_DefaultNominalMarket());
					failures.add(dsd);
				} else {
					// The feature is not enabled (and will not be enabled while the application is open, so disable the constraint checker on this object.
					//
					ctx.skipCurrentConstraintForAll(Collections.singleton(spotMarketsModel));
				}
			}
		}
		return Activator.PLUGIN_ID;
	}

}
