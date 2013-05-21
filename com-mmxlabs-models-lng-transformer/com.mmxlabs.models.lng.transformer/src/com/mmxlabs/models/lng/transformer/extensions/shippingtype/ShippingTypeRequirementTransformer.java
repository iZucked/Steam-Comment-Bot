/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.shippingtype;

import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
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
 * @since 2.0
 */
public class ShippingTypeRequirementTransformer implements IContractTransformer {

	@Inject
	private IShippingTypeRequirementProviderEditor desPermissionProviderEditor;

	@Inject
	private IPortSlotProvider portSlotProvider;

	/**
	 * @since 3.0
	 */
	@Override
	public void startTransforming(final LNGScenarioModel rootObject, final ModelEntityMap map, final ISchedulerBuilder builder) {
	}

	/**
	 * @since 3.0
	 */
	@Override
	public ISalesPriceCalculator transformSalesPriceParameters(final LNGPriceCalculatorParameters priceParameters) {
		return null;
	}

	/**
	 * @since 3.0
	 */
	@Override
	public ILoadPriceCalculator transformPurchasePriceParameters(final LNGPriceCalculatorParameters priceParameters) {
		return null;
	}

	@Override
	public void slotTransformed(final Slot modelSlot, final IPortSlot optimiserSlot) {
		final ISequenceElement sequenceElement = portSlotProvider.getElement(optimiserSlot);

		if (modelSlot instanceof LoadSlot) {
			if (((LoadSlot) modelSlot).isDESPurchase()) {
				desPermissionProviderEditor.setPurchaseSlotDeliveryType(sequenceElement, CargoDeliveryType.NOT_SHIPPED);
			} else {
				desPermissionProviderEditor.setPurchaseSlotDeliveryType(sequenceElement, CargoDeliveryType.SHIPPED);
			}
		}

		else if (modelSlot instanceof DischargeSlot) {
			DischargeSlot dischargeSlot = (DischargeSlot) modelSlot;
			CargoDeliveryType cargoType = CargoDeliveryType.ANY;

			if (dischargeSlot.isSetPurchaseDeliveryType()) {
				cargoType = dischargeSlot.getPurchaseDeliveryType();
			} else {
				Contract contract = dischargeSlot.getContract();
				if (contract instanceof SalesContract) {
					cargoType = ((SalesContract) contract).getPurchaseDeliveryType();
				}
			}
			if (cargoType != CargoDeliveryType.ANY) {
				desPermissionProviderEditor.setSalesSlotRequiredPurchaseType(sequenceElement, cargoType);
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
