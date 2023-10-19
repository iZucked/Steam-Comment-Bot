/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class FormulaeNameUniquenessConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull IValidationContext ctx, @NonNull IExtraValidationContext extraValidationContext, @NonNull List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		final EObject container = extraValidationContext.getContainer(target);
		final EReference reference = extraValidationContext.getContainment(target);
		final Pair<EObject, EReference> containerAndReference = Pair.of(container, reference);
		
		if (target instanceof final CommodityCurve curve //
				&& reference == PricingPackage.eINSTANCE.getPricingModel_FormulaeCurves()//
				&& container instanceof final PricingModel pricingModel) {

			Map<Pair<EObject, EReference>, Set<String>> badNames = (Map<Pair<EObject, EReference>, Set<String>>) ctx.getCurrentConstraintData();
			if (badNames == null) {
				badNames = new HashMap<>();
				ctx.putCurrentConstraintData(badNames);
			}
			Set<String> bad = badNames.get(Pair.of(target, reference));
			if (bad == null) {
				bad = new HashSet<String>();
				badNames.put(containerAndReference, bad);
				
				final List<EObject> objects = extraValidationContext.getSiblings(target);
				objects.remove(curve);
				objects.addAll(pricingModel.getBunkerFuelCurves());
				objects.addAll(pricingModel.getCharterCurves());
				objects.addAll(pricingModel.getCommodityCurves());
				objects.addAll(pricingModel.getCurrencyCurves());
				for (final EObject eo : objects) {
					if (eo instanceof final NamedObject no && no.getName() != null) {
						bad.add(no.getName().toLowerCase());
					}
				}
			}
			
			final String name = curve.getName();
			
			if (name != null && bad.contains(name.toLowerCase())) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus("Formulae has non-unique name (names are case insensitive) " + name));
				dsd.addEObjectAndFeature(target, MMXCorePackage.Literals.NAMED_OBJECT__NAME);
				statuses.add(dsd);
			}
		}
		
	}

}
