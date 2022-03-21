/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Common super class for pricing parameter validation to help with common task
 * of finding the container name.
 * 
 * @author Simon Goodall
 *
 * @param <T>
 */
public abstract class AbstractPriceParametersConstraint<T extends LNGPriceCalculatorParameters> extends AbstractModelMultiConstraint {

	protected final Class<T> type;
	protected final String defaultContainerName;

	protected AbstractPriceParametersConstraint(final Class<T> type, @Nullable String defaultContainerName) {
		this.type = type;
		this.defaultContainerName = defaultContainerName;
	}

	protected AbstractPriceParametersConstraint(final Class<T> type) {
		this(type, null);
	}

	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (type.isInstance(target)) {
			final T pricingParams = type.cast(target);
			if (pricingParams != null) {
				final EObject eContainer = pricingParams.eContainer();

				final DetailConstraintStatusFactory factory;
				if (eContainer instanceof final Contract contract) {
					factory = DetailConstraintStatusFactory.makeStatus() //
							.withTypedName("Contract", contract.getName());
				} else if (eContainer instanceof final SpotMarket spotMarket) {
					factory = DetailConstraintStatusFactory.makeStatus() //
							.withTypedName("Market", spotMarket.getName());
				} else if (eContainer instanceof final NamedObject namedObject) {
					factory = DetailConstraintStatusFactory.makeStatus() //
							.withName(namedObject.getName());
				} else if (defaultContainerName != null) {
					factory = DetailConstraintStatusFactory.makeStatus() //
							.withName(defaultContainerName);
				} else {
					factory = DetailConstraintStatusFactory.makeStatus();
				}

				doParamsValidate(ctx, extraContext, failures, pricingParams, eContainer, factory);
			}
		}
	}

	protected abstract void doParamsValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures, final T pricingParams, final EObject eContainer,
			final DetailConstraintStatusFactory factory);
}