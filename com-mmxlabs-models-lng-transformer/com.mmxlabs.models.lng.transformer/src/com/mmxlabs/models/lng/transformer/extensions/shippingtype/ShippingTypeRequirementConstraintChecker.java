package com.mmxlabs.models.lng.transformer.extensions.shippingtype;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.AbstractPairwiseConstraintChecker;

/**
 * An implementation of {@link IPairwiseConstraintChecker} to forbid certain {@link ISequenceElement} pairings
 * 
 * @since 2.0
 */
public class ShippingTypeRequirementConstraintChecker extends AbstractPairwiseConstraintChecker {

	@Inject
	private IShippingTypeRequirementProvider shippingTypeRequirementProvider;

	public ShippingTypeRequirementConstraintChecker(final String name) {
		super(name);
	}

	@Override
	public boolean checkPairwiseConstraint(final ISequenceElement first, final ISequenceElement second, final IResource resource) {

		CargoDeliveryType requiredShippingType = shippingTypeRequirementProvider.getSalesSlotRequiredDeliveryType(second);
		
		if (requiredShippingType != null && requiredShippingType != CargoDeliveryType.ANY) {
			return requiredShippingType == shippingTypeRequirementProvider.getPurchaseSlotDeliveryType(first);
		}

		return true;
	}

	@Override
	public String explain(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		CargoDeliveryType requiredShippingType = shippingTypeRequirementProvider.getSalesSlotRequiredDeliveryType(second);
		CargoDeliveryType shippingType = shippingTypeRequirementProvider.getPurchaseSlotDeliveryType(first);
		
		if (requiredShippingType != null && requiredShippingType != shippingType) {
			return String.format("Required shipping delivery type of %s does not match purchase delivery type of %s.", requiredShippingType.getName(), shippingType.getName());
		}
		
		return null;
	}
}
