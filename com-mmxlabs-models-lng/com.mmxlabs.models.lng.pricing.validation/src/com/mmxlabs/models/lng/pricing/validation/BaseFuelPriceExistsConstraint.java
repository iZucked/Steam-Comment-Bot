/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class BaseFuelPriceExistsConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();

		// Disable for now in light of API change..
		if (target instanceof final CostModel costModel) {

			final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(extraContext.getScenarioDataProvider());
			final Set<BaseFuel> baseFuels = new LinkedHashSet<>();
			baseFuels.addAll(fleetModel.getBaseFuels());

			for (final BaseFuelCost bfc : costModel.getBaseFuelCosts()) {
				baseFuels.remove(bfc.getFuel());
			}

			if (!baseFuels.isEmpty()) {
				final String fuelList = baseFuels.stream() //
						.map(BaseFuel::getName) //
						.collect(Collectors.joining(", "));

				final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
						.withMessage("The following base fuels have no cost - " + fuelList);

				factory.withObjectAndFeature(costModel, PricingPackage.Literals.COST_MODEL__BASE_FUEL_COSTS);

				factory.make(ctx, statuses);
			}
		}
	}

}
