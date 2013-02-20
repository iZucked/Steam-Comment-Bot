/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * @since 2.0
 */
public class SlotPriceExpressionConstraint extends AbstractModelMultiConstraint {
	@Override
	public String validate(final IValidationContext ctx, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof Slot) {
			final SeriesParser parser = getParser();
			final Slot slot = (Slot) target;

			if (slot.isSetPriceExpression()) {
				validatePriceExpression(ctx, slot, CargoPackage.eINSTANCE.getSlot_PriceExpression(), slot.getPriceExpression(), parser, failures);
			}
		}
		return Activator.PLUGIN_ID;
	}

	private void validatePriceExpression(final IValidationContext ctx, final Slot slot, final EStructuralFeature feature, final String priceExpression, final SeriesParser parser,
			final List<IStatus> failures) {

		if (priceExpression == null || priceExpression.isEmpty()) {
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Price Expression is missing."));
			dsd.addEObjectAndFeature(slot, feature);
			failures.add(dsd);
			return;
		}

		// Permit break even marker
		if ("?".equals(priceExpression)) {
			return;
		}
		
		if (parser != null) {
			ISeries parsed = null;
			String hints = "";
			try {
				final IExpression<ISeries> expression = parser.parse(priceExpression);
				parsed = expression.evaluate();

			} catch (final Exception e) {
				hints = e.getMessage();
			}
			if (parsed == null) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Unable to parse price expression. " + hints));
				dsd.addEObjectAndFeature(slot, feature);
				failures.add(dsd);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private SeriesParser getParser() {
		final Activator activator = Activator.getDefault();
		if (activator == null) {
			return null;
		}
		final IExtraValidationContext extraValidationContext = activator.getExtraValidationContext();
		if (extraValidationContext != null) {
			final MMXRootObject rootObject = extraValidationContext.getRootObject();

			final SeriesParser indices = new SeriesParser();

			final PricingModel pricingModel = rootObject.getSubModel(PricingModel.class);
			if (pricingModel == null) {
				return null;
			}
			for (final Index<Double> index : pricingModel.getCommodityIndices()) {
				if (index instanceof DataIndex) {
					// For this validation, we do not need real times or values
					final int[] times = new int[1];
					final Number[] nums = new Number[1];
					indices.addSeriesData(index.getName(), times, nums);
				} else if (index instanceof DerivedIndex) {
					indices.addSeriesExpression(index.getName(), ((DerivedIndex) index).getExpression());
				}
			}
			return indices;
		}
		return null;
	}
}
