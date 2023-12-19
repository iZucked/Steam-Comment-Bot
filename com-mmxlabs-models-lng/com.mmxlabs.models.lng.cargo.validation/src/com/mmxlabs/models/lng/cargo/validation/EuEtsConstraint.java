package com.mmxlabs.models.lng.cargo.validation;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.EmissionsCoveredTable;
import com.mmxlabs.models.lng.cargo.EuEtsModel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class EuEtsConstraint extends AbstractModelMultiConstraint{

	@Override
	public void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof EuEtsModel euEtsModel) {
			String message;
			
			/* Validate port group
			 * 	- Is within the EU
			 * 	- Is not empty
			 */
			if(euEtsModel.getEuPortGroup() == null || euEtsModel.getEuPortGroup().getContents().isEmpty()) {
				message = "[EU ETS] Port Group is empty";
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dsd.addEObjectAndFeature(euEtsModel, CargoPackage.Literals.EU_ETS_MODEL__EU_PORT_GROUP);
				statuses.add(dsd);
			}
			else {
				// Check port is in EU
				for(var entry : euEtsModel.getEuPortGroup().getContents()) {
					if(entry instanceof Port port && (!port.getLocation().getTimeZone().contains("Europe"))) {
						message = "[EU ETS] Port Group has ports not in the EU";
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dsd.addEObjectAndFeature(euEtsModel, CargoPackage.Literals.EU_ETS_MODEL__EU_PORT_GROUP);
						statuses.add(dsd);
						break;
					}
				}
			}
			
			/* Validate that ramp up table:
				- Isn't empty
				- Has positive values
				- Doesn't have repeated years
			*/
			if(euEtsModel.getEmissionsCovered() == null || euEtsModel.getEmissionsCovered().isEmpty()) {
				message = "[EU ETS] Ramp up table is empty";
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dsd.addEObjectAndFeature(euEtsModel, CargoPackage.Literals.EU_ETS_MODEL__EMISSIONS_COVERED);
				statuses.add(dsd);
			}
			else {
				final Map<Integer, Integer> yearCountMapping = new HashMap<>();
				for(EmissionsCoveredTable entry : euEtsModel.getEmissionsCovered()) {
					yearCountMapping.compute(entry.getStartYear(), (key, v) -> (v == null) ? 1 : v+1);
					
					if(entry.getStartYear() < 0 || entry.getEmissionsCovered() < 0) {
						message = "[EU ETS] Ramp up table has negative entries in table";
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dsd.addEObjectAndFeature(euEtsModel, CargoPackage.Literals.EU_ETS_MODEL__EMISSIONS_COVERED);
						statuses.add(dsd);
					}
				}
				
				for(Entry<Integer, Integer> entry : yearCountMapping.entrySet()) {
					if(entry.getValue() > 1) {
						message = String.format("[EU ETS] Ramp up table has duplicate entries for year (%s)", entry.getKey());
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dsd.addEObjectAndFeature(euEtsModel, CargoPackage.Literals.EMISSIONS_COVERED_TABLE__START_YEAR);
						statuses.add(dsd);
					}
				}
			}
			
			/* Check price expression:
				- Isn't empty
				- Is a valid price expression
			*/
			// Not sure on correct type for this function
			validatePriceExpression(ctx, statuses, euEtsModel.getEuaPriceExpression(), PriceIndexType.CURRENCY, euEtsModel, CargoPackage.Literals.EU_ETS_MODEL__EUA_PRICE_EXPRESSION);
		}
	}
	
	private void validatePriceExpression(final IValidationContext ctx, final List<IStatus> failures, final String price, final PriceIndexType type, //
			final EuEtsModel euEtsModel, final EStructuralFeature feature) {
		final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, euEtsModel, feature, price, type);
		if (!result.isOk()) {
			final String message = String.format("[EU ETS]%s", result.getErrorDetails());
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
			dsd.addEObjectAndFeature(euEtsModel, feature);
			failures.add(dsd);
		}
		final YearMonth start;
		if (!euEtsModel.getEmissionsCovered().isEmpty()) {
			final int smallestYear = euEtsModel.getEmissionsCovered().stream().map(e -> e.getStartYear()).filter(y -> y > 0).min(Integer::compare).get();
			start = YearMonth.of(smallestYear, 1);
		} else {
			// Dummy pricing event
			start = null;
		}
		if (start != null) {

			final YearMonth key = YearMonth.from(start);
			final double minExpressionValue = 0;
			final double maxExpressionValue = Integer.MAX_VALUE;
			PriceExpressionUtils.constrainPriceExpression(ctx, euEtsModel, feature, price, minExpressionValue, maxExpressionValue, key, failures, type);
		}
	}

}
