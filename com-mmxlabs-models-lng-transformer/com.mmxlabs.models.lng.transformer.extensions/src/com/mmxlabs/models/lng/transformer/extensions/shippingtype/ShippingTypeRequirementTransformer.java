/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.shippingtype;

import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.IContractTransformer;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

/**
 */
public class ShippingTypeRequirementTransformer implements IContractTransformer {

	@Inject
	private IShippingTypeRequirementProviderEditor shippingTypeProviderEditor;

	@Inject
	private IPortSlotProvider portSlotProvider;

	/**
	 */
	@Override
	public void startTransforming(final LNGScenarioModel rootObject, final ModelEntityMap modelEntityMap, final ISchedulerBuilder builder) {
	}

	/**
	 */
	@Override
	public ISalesPriceCalculator transformSalesPriceParameters(@Nullable final SalesContract salesContract, @NonNull final LNGPriceCalculatorParameters priceParameters) {
		return null;
	}

	/**
	 */
	@Override
	public ILoadPriceCalculator transformPurchasePriceParameters(@Nullable final PurchaseContract purchaseContract, @NonNull final LNGPriceCalculatorParameters priceParameters) {
		return null;
	}

	@Override
	public void slotTransformed(@NonNull final Slot modelSlot, @NonNull final IPortSlot optimiserSlot) {
		final ISequenceElement sequenceElement = portSlotProvider.getElement(optimiserSlot);

		if (modelSlot instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) modelSlot;
			// Set the slot type
			if (loadSlot.isDESPurchase()) {
				shippingTypeProviderEditor.setPurchaseSlotDeliveryType(sequenceElement, CargoDeliveryType.NOT_SHIPPED);
			} else {
				shippingTypeProviderEditor.setPurchaseSlotDeliveryType(sequenceElement, CargoDeliveryType.SHIPPED);
			}

			// Set the required match type
			final CargoDeliveryType cargoType = loadSlot.getSlotOrContractDeliveryType();
			if (cargoType != CargoDeliveryType.ANY) {
				shippingTypeProviderEditor.setPurchaseSlotRequiredDeliveryType(sequenceElement, cargoType);
			}

		}

		else if (modelSlot instanceof DischargeSlot) {
			final DischargeSlot dischargeSlot = (DischargeSlot) modelSlot;

			// Set the slot type
			if (dischargeSlot.isFOBSale()) {
				shippingTypeProviderEditor.setPurchaseSlotDeliveryType(sequenceElement, CargoDeliveryType.NOT_SHIPPED);
			} else {
				shippingTypeProviderEditor.setPurchaseSlotDeliveryType(sequenceElement, CargoDeliveryType.SHIPPED);
			}
			// Set the required match type
			final CargoDeliveryType cargoType = dischargeSlot.getSlotOrContractDeliveryType();
			if (cargoType != CargoDeliveryType.ANY) {
				shippingTypeProviderEditor.setSalesSlotRequiredDeliveryType(sequenceElement, cargoType);
			}
		}
	}

	@Override
	public Collection<EClass> getContractEClasses() {
		return Collections.emptySet();
	}

	@Override
	public void finishTransforming() {
	}

}
