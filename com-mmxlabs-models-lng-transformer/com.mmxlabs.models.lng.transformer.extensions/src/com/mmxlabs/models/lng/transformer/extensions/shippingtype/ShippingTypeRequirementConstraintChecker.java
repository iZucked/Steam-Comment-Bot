/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.shippingtype;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.constraints.impl.AbstractPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
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

	@Inject
	private IVesselProvider vesselProvider;
	
	public ShippingTypeRequirementConstraintChecker(@NonNull final String name) {
		super(name);
	}

	@Override
	public boolean checkPairwiseConstraint(final ISequenceElement first, final ISequenceElement second, final IResource resource, final List<String> messages) {
		CargoDeliveryType shippingType = getCargoDeliveryType(resource);

		final CargoDeliveryType requiredSalesSlotShippingType = shippingTypeRequirementProvider.getSalesSlotRequiredDeliveryType(second);
		if (requiredSalesSlotShippingType != null && requiredSalesSlotShippingType != CargoDeliveryType.ANY) {
			if (requiredSalesSlotShippingType != shippingType) {
				messages.add(explain(first, second, resource));
				return false;
			}
		}

		final CargoDeliveryType requiredPurchaseSlotShippingType = shippingTypeRequirementProvider.getPurchaseSlotRequiredDeliveryType(first);
		if (requiredPurchaseSlotShippingType != null && requiredPurchaseSlotShippingType != CargoDeliveryType.ANY) {
			if (requiredPurchaseSlotShippingType != shippingType) {
				messages.add(explain(first, second, resource));
				return false;
			}
		}
		return true;
	}

	private CargoDeliveryType getCargoDeliveryType(final IResource resource) {
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		boolean notShipped = vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || 
				vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE;
		if (notShipped) {
			return CargoDeliveryType.NOT_SHIPPED;
		}
		else {
			return CargoDeliveryType.SHIPPED;
		}
	}
	
	@Override
	public String explain(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		CargoDeliveryType shippingType = getCargoDeliveryType(resource);
		final CargoDeliveryType requiredSalesSlotShippingType = shippingTypeRequirementProvider.getSalesSlotRequiredDeliveryType(second);
		if (requiredSalesSlotShippingType != null && requiredSalesSlotShippingType != shippingType) {
			return String.format("Required shipping delivery type of %s does not match purchase delivery type of %s.", requiredSalesSlotShippingType.getName(), shippingType.getName());
		}
		final CargoDeliveryType requiredPurchaseSlotShippingType = shippingTypeRequirementProvider.getPurchaseSlotRequiredDeliveryType(first);
		if (requiredPurchaseSlotShippingType != null && requiredPurchaseSlotShippingType != shippingType) {
			return String.format("Required shipping delivery type of %s does not match sales delivery type of %s.", requiredPurchaseSlotShippingType.getName(), shippingType.getName());
		}
		return null;
	}
}
