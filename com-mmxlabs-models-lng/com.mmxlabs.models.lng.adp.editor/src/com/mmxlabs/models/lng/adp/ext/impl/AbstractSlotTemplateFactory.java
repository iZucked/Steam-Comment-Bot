/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext.impl;

import java.time.YearMonth;

import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.ext.ISlotTemplateFactory;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.port.Port;

public class AbstractSlotTemplateFactory implements ISlotTemplateFactory {

	public static final String TEMPLATE_GENERIC_FOB_PURCHASE = "generic-fob-purchase";
	public static final String TEMPLATE_GENERIC_FOB_SALE = "generic-fob-sale";
	public static final String TEMPLATE_GENERIC_DES_PURCHASE = "generic-des-purchase";
	public static final String TEMPLATE_GENERIC_DES_SALE = "generic-des-sale";

	@Override
	public <T extends Slot<U>, U extends Contract> T createSlot(final String templateID, final ContractProfile<T, U> profile, final SubContractProfile<T, U> subProfile) {
		switch (templateID) {
		case TEMPLATE_GENERIC_FOB_PURCHASE:
		case TEMPLATE_GENERIC_DES_PURCHASE:
			return (T) createGenericPurchase((PurchaseContractProfile) profile, (SubContractProfile<LoadSlot, PurchaseContract>) subProfile);
		case TEMPLATE_GENERIC_FOB_SALE:
		case TEMPLATE_GENERIC_DES_SALE:
			return (T) createGenericSale((SalesContractProfile) profile, (SubContractProfile<DischargeSlot, SalesContract>) subProfile);
		}

		throw new IllegalArgumentException("Unknown template ID " + templateID);
	}

	@Override
	public <T extends Slot<U>, U extends Contract> String generateName(final String templateID, final ContractProfile<T, U> profile, final SubContractProfile<T, U> subProfile, final YearMonth yearStart, final int cargoNumber) {

		final String contractShortName = DistributionModelGeneratorUtil.getContractShortName(profile, profile.getContract());
		if (profile.getSubProfiles().size() == 1) {
			return String.format("%2d-%s-%02d", yearStart.getYear() - 2000, contractShortName, 1 + cargoNumber);
		} else {
			final String code = subProfile.getName();
			return String.format("%2d-%s-%s-%02d", yearStart.getYear() - 2000, contractShortName, code, 1 + cargoNumber);
		}
	}

	protected LoadSlot createGenericPurchase(final ContractProfile<LoadSlot, PurchaseContract> profile, final SubContractProfile<LoadSlot, PurchaseContract> subProfile) {

		final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
		PurchaseContract contract = profile.getContract();

		slot.setContract(contract);
		final Port port = subProfile.getPort();
		slot.setPort(port);
		
		if (subProfile.getContractType() == ContractType.DES) {
			slot.setDESPurchase(true);
			slot.setNominatedVessel(subProfile.getNominatedVessel());
		}

		return slot;
	}

	protected DischargeSlot createGenericSale(final ContractProfile<DischargeSlot, SalesContract> profile, final SubContractProfile<DischargeSlot, SalesContract> subProfile) {

		final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();

		SalesContract contract = profile.getContract();
		slot.setContract(contract);
		final Port port = subProfile.getPort();
		slot.setPort(port);
		if (subProfile.getContractType() == ContractType.FOB) {
			slot.setFOBSale(true);
			slot.setNominatedVessel(subProfile.getNominatedVessel());
		}

		return slot;
	}
}
