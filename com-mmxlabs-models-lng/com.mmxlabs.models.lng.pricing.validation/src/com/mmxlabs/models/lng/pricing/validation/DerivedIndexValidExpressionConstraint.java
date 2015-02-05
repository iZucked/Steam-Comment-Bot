/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.validation.internal.Activator;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class DerivedIndexValidExpressionConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof NamedIndexContainer<?>) {
			final NamedIndexContainer<?> namedIndexContainer = (NamedIndexContainer<?>) target;
			final Index<?> data = namedIndexContainer.getData();
			if (data instanceof DerivedIndex<?>) {
				final DerivedIndex<?> c = (DerivedIndex<?>) data;
				if (c.getExpression() == null) {
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Price index is missing a price expression."));
					dcsd.addEObjectAndFeature(target, PricingPackage.Literals.NAMED_INDEX_CONTAINER__DATA);
					failures.add(dcsd);
				} else {
					final SeriesParser parser = PriceExpressionUtils.getIndexParser(null, extraContext.getContainment(namedIndexContainer));
					PriceExpressionUtils.validatePriceExpression(ctx, target, PricingPackage.Literals.NAMED_INDEX_CONTAINER__DATA, c.getExpression(), parser, failures);
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
