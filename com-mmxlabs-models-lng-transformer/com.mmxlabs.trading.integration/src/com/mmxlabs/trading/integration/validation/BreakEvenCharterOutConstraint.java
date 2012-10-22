package com.mmxlabs.trading.integration.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.pricing.CharterCostModel;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IBreakEvenEvaluator;
import com.mmxlabs.trading.integration.internal.Activator;

public class BreakEvenCharterOutConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();

		if (target instanceof Slot) {

			final Slot slot = (Slot) target;
			final IExtraValidationContext extraContext = Activator.getDefault().getExtraValidationContext();
			final MMXRootObject rootObject = extraContext.getRootObject();
			final PricingModel pricingModel = rootObject.getSubModel(PricingModel.class);

			if (pricingModel == null) {
				// Nothing to validate against
				return Activator.PLUGIN_ID;
			}
			boolean charterOutEnabled = false;
			for (final CharterCostModel ccm : pricingModel.getFleetCost().getCharterCosts()) {
				if (ccm.getCharterOutPrice() != null) {
					charterOutEnabled = true;
					break;
				}
			}

			if (!charterOutEnabled) {
				// Charter out mode not enabled
				return Activator.PLUGIN_ID;
			}

			if (slot.isSetPriceExpression() && slot.getPriceExpression().contains(IBreakEvenEvaluator.MARKER)) {
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus("Missing prices cannot be used with charter out generation"));
				dcsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_PriceExpression());
				statuses.add(dcsd);
			}
		}

		return Activator.PLUGIN_ID;
	}
}
