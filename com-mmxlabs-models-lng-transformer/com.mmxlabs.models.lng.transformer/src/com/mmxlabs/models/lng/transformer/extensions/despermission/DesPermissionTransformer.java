package com.mmxlabs.models.lng.transformer.extensions.despermission;

import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.transformer.ResourcelessModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.IContractTransformer;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

/**
 * @since 2.0
 */
public class DesPermissionTransformer implements IContractTransformer {

	@Inject
	private IDesPermissionProviderEditor desPermissionProviderEditor;

	@Inject
	private IPortSlotProvider portSlotProvider;
	
	@Override
	public void startTransforming(final MMXRootObject rootObject, final ResourcelessModelEntityMap map, final ISchedulerBuilder builder) {
	}

	@Override
	public ISalesPriceCalculator transformSalesContract(final SalesContract sc) {
		return null;
	}

	@Override
	public ILoadPriceCalculator transformPurchaseContract(final PurchaseContract pc) {
		return null;
	}

	@Override
	public void slotTransformed(final Slot modelSlot, final IPortSlot optimiserSlot) {
		final ISequenceElement sequenceElement = portSlotProvider.getElement(optimiserSlot);

		if (modelSlot instanceof LoadSlot && ((LoadSlot) modelSlot).isDESPurchase()) {
			desPermissionProviderEditor.addDesPurchaseSlot(sequenceElement);
		}
				
		else if (modelSlot instanceof DischargeSlot) {
			DischargeSlot dischargeSlot = (DischargeSlot) modelSlot;
			boolean desPurchaseProhibited = false;
			if (dischargeSlot.isSetPurchaseDeliveryType() && (dischargeSlot.getPurchaseDeliveryType() == CargoDeliveryType.SHIPPED)) {
				desPurchaseProhibited = true;
			}
			else {
				Contract contract = dischargeSlot.getContract();
				if (contract instanceof SalesContract && (((SalesContract) contract).getPurchaseDeliveryType() == CargoDeliveryType.SHIPPED)) {
					desPurchaseProhibited = true;					
				}
			}
			if (desPurchaseProhibited) {
				desPermissionProviderEditor.addDesProhibitedSalesSlot(sequenceElement);
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
