/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.pricing.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class UnitConversionConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof UnitConversion) {
			final UnitConversion unitConversion = (UnitConversion) target;
			if (unitConversion.getFrom() == null || unitConversion.getFrom().isEmpty()) {
				final String failureMessage = String.format("Conversion factor is missing a from");
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage, IStatus.WARNING));
				dsd.addEObjectAndFeature(unitConversion, PricingPackage.Literals.UNIT_CONVERSION__FROM);
				failures.add(dsd);
			}
			if (unitConversion.getTo() == null || unitConversion.getTo().isEmpty()) {
				final String failureMessage = String.format("Conversion factor is missing a to");
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage, IStatus.WARNING));
				dsd.addEObjectAndFeature(unitConversion, PricingPackage.Literals.UNIT_CONVERSION__TO);
				failures.add(dsd);
			}
			if (unitConversion.getFactor() == 0.0) {
				final String failureMessage = String.format("Conversion factor has zero as factor");
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage, IStatus.WARNING));
				dsd.addEObjectAndFeature(unitConversion, PricingPackage.Literals.UNIT_CONVERSION__FACTOR);
				failures.add(dsd);
			}

			// Check for duplicates
			Set<Pair<String, String>> badNames = (Set<Pair<String, String>>) ctx.getCurrentConstraintData();
			if (badNames == null) {
				badNames = new HashSet<>();
				ctx.putCurrentConstraintData(badNames);
			}

			final List<EObject> objects = extraContext.getSiblings(target);

			final Set<Pair<String, String>> temp = new HashSet<>();
			for (final EObject no : objects) {
				if (no instanceof UnitConversion) {
					final UnitConversion uc = (UnitConversion) no;
					final Pair<String, String> key = makeKey(uc);
					if (!temp.add(key)) {
						badNames.add(key);
					}
				}
			}

			final Pair<String, String> key = makeKey(unitConversion);

			if (badNames.contains(key)) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("Multiple conversion factors for %s to %s", unitConversion.getFrom(), unitConversion.getTo())));
				dsd.addEObjectAndFeature(target, PricingPackage.Literals.UNIT_CONVERSION__FROM);
				dsd.addEObjectAndFeature(target, PricingPackage.Literals.UNIT_CONVERSION__TO);
				failures.add(dsd);
			}
		}

		return Activator.PLUGIN_ID;
	}

	private Pair<String, String> makeKey(final UnitConversion uc) {
		String from = uc.getFrom();
		if (from != null) {
			from = from.toLowerCase();
		}
		String to = uc.getTo();
		if (to != null) {
			to = to.toLowerCase();
		}
		return new Pair<>(from, to);
	}
}
