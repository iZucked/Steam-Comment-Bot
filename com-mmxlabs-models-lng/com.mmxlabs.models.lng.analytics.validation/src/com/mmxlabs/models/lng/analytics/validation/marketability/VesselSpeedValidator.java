package com.mmxlabs.models.lng.analytics.validation.marketability;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.MarketabilityModel;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.models.ui.validation.ValidationGroup;

public class VesselSpeedValidator extends AbstractModelMultiConstraint {

	private static final ValidationGroup TAG_MARKETABLE_WINDOWS = new ValidationGroup("Marketable Windows", (short) 0, true);

	public static Collection<Vessel> invalidVessels(Collection<ShippingOption> shippingOptions, int speed) {
		Set<Vessel> invalid = new HashSet<>();
		for (ShippingOption shipping : shippingOptions) {
			Vessel vessel = null;
			if (shipping instanceof ExistingVesselCharterOption evco) {
				vessel = evco.getVesselCharter().getVessel();
			} else if (shipping instanceof ExistingCharterMarketOption ecmo) {
				vessel = ecmo.getCharterInMarket().getVessel();
			}
			if(vessel == null) {
				continue;
			}
			// TODO: USE CONSUMPTION CURVES
			if (vessel.getVesselOrDelegateMinSpeed() > speed || vessel.getVesselOrDelegateMaxSpeed() < speed) {
				invalid.add(vessel);
			}
		}
		return invalid;
	}

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();
		if (target instanceof @NonNull MarketabilityModel model && model.isSetVesselSpeed()) {
			Collection<Vessel> invalidVessels = invalidVessels(model.getShippingTemplates(), model.getVesselSpeed());
			invalidVessels.stream().forEach(vessel -> {

				final String message = String.format("'%s': %d kts outside of valid speed for vessel", vessel.getName(), model.getVesselSpeed());
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dsd.addEObjectAndFeature(model, AnalyticsPackage.eINSTANCE.getMarketabilityModel_VesselSpeed());
				dsd.setTag(TAG_MARKETABLE_WINDOWS);
				failures.add(dsd);
			});
		}
	}
}
