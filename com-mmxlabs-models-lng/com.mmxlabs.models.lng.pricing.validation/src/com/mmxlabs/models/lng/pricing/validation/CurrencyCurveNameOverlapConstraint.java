package com.mmxlabs.models.lng.pricing.validation;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.pricing.CurrencyIndex;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.validation.internal.Activator;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class CurrencyCurveNameOverlapConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof PricingModel) {
			final PricingModel pricingModel = (PricingModel) target;
			// Get list of existing names
			final Set<String> currencyCurveNames = pricingModel.getCurrencyIndices().stream() //
					.map(CurrencyIndex::getName) //
					.collect(Collectors.toSet());
			// Remove nulls
			currencyCurveNames.remove(null);

			// Consumer to check a given index name against currency curve names
			final Consumer<NamedIndexContainer<?>> checker = index -> {
				if (currencyCurveNames.contains(index.getName())) {
					final String message = String.format("Curve %s is already registered as a currency curve", index.getName());
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dcsd.addEObjectAndFeature(index, MMXCorePackage.Literals.NAMED_OBJECT__NAME);
					statuses.add(dcsd);
				}
			};
			pricingModel.getCommodityIndices().forEach(idx -> checker.accept(idx));
			pricingModel.getBaseFuelPrices().forEach(idx -> checker.accept(idx));
			pricingModel.getCharterIndices().forEach(idx -> checker.accept(idx));
		}

		return Activator.PLUGIN_ID;
	}

}
