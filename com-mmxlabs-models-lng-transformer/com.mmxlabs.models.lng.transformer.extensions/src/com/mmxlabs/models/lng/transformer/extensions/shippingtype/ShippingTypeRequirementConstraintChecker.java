/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.shippingtype;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.AbstractPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * An implementation of {@link IPairwiseConstraintChecker} to forbid certain {@link ISequenceElement} pairings
 * 
 * FIXME: Properly handle multiple loads and discharges within the {@link VoyagePlan}
 * 
 */
public class ShippingTypeRequirementConstraintChecker extends AbstractPairwiseConstraintChecker {

	@Inject
	private IShippingTypeRequirementProvider shippingTypeRequirementProvider;

	public ShippingTypeRequirementConstraintChecker(final String name) {
		super(name);
	}

	@Override
	public boolean checkPairwiseConstraint(final ISequenceElement first, final ISequenceElement second, final IResource resource) {

		{
			final CargoDeliveryType requiredShippingType = shippingTypeRequirementProvider.getSalesSlotRequiredDeliveryType(second);

			if (requiredShippingType != null && requiredShippingType != CargoDeliveryType.ANY) {
				if (requiredShippingType != shippingTypeRequirementProvider.getPurchaseSlotDeliveryType(first)) {
					return false;
				}
			}
		}
		{
			final CargoDeliveryType requiredShippingType = shippingTypeRequirementProvider.getPurchaseSlotRequiredDeliveryType(first);

			if (requiredShippingType != null && requiredShippingType != CargoDeliveryType.ANY) {

				if (requiredShippingType != shippingTypeRequirementProvider.getSalesSlotDeliveryType(second)) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public String explain(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		{
			final CargoDeliveryType requiredShippingType = shippingTypeRequirementProvider.getSalesSlotRequiredDeliveryType(second);
			final CargoDeliveryType shippingType = shippingTypeRequirementProvider.getPurchaseSlotDeliveryType(first);

			if (requiredShippingType != null && requiredShippingType != shippingType) {
				return String.format("Required shipping delivery type of %s does not match purchase delivery type of %s.", requiredShippingType.getName(), shippingType.getName());
			}
		}
		{
			final CargoDeliveryType requiredShippingType = shippingTypeRequirementProvider.getPurchaseSlotRequiredDeliveryType(first);
			final CargoDeliveryType shippingType = shippingTypeRequirementProvider.getSalesSlotDeliveryType(second);

			if (requiredShippingType != null && requiredShippingType != shippingType) {
				return String.format("Required shipping delivery type of %s does not match sales delivery type of %s.", requiredShippingType.getName(), shippingType.getName());
			}
		}

		return null;
	}
}
