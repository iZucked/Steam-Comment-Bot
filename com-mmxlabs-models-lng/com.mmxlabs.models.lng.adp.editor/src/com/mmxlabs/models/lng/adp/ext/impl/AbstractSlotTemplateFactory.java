/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext.impl;

import java.time.YearMonth;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.ext.ISlotTemplateFactory;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ContractType;

@NonNullByDefault
public class AbstractSlotTemplateFactory implements ISlotTemplateFactory {

	public static final String TEMPLATE_GENERIC_FOB_PURCHASE = "generic-fob-purchase";
	public static final String TEMPLATE_GENERIC_FOB_SALE = "generic-fob-sale";
	public static final String TEMPLATE_GENERIC_DES_PURCHASE = "generic-des-purchase";
	public static final String TEMPLATE_GENERIC_DES_SALE = "generic-des-sale";

	@Override
	public <T extends Slot> T createSlot(final String templateID, final ContractProfile<T> profile, final SubContractProfile<T> subProfile) {
		switch (templateID) {
		case TEMPLATE_GENERIC_FOB_PURCHASE:
		case TEMPLATE_GENERIC_DES_PURCHASE:
			return (T) createGenericPurchase(profile, subProfile);
		case TEMPLATE_GENERIC_FOB_SALE:
		case TEMPLATE_GENERIC_DES_SALE:
			return (T) createGenericSale(profile, subProfile);
		}

		throw new IllegalArgumentException("Unknown template ID " + templateID);
	}

	@Override
	public <T extends Slot> String generateName(final String templateID, final ContractProfile<T> profile, final SubContractProfile<T> subProfile, final YearMonth yearStart, final int cargoNumber) {

		final String contractShortName = DistributionModelGeneratorUtil.getContractShortName(profile, profile.getContract());
		if (profile.getSubProfiles().size() == 1) {
			return String.format("%2d-%s-%02d", yearStart.getYear() - 2000, contractShortName, 1 + cargoNumber);
		} else {
			final String code = subProfile.getName();
			return String.format("%2d-%s-%s-%02d", yearStart.getYear() - 2000, contractShortName, code, 1 + cargoNumber);
		}
	}

	protected LoadSlot createGenericPurchase(final ContractProfile<?> profile, final SubContractProfile<?> subProfile) {

		final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
		Contract contract = profile.getContract();

		slot.setContract(contract);
		slot.setPort(contract.getPreferredPort());

		if (contract.getContractType() == ContractType.DES) {
			slot.setDESPurchase(true);
			slot.setNominatedVessel(subProfile.getNominatedVessel());
			if (subProfile.getShippingDays() > 0) {
				slot.setDivertible(true);
				slot.setShippingDaysRestriction(subProfile.getShippingDays());
			}
		}

		return slot;
	}

	protected DischargeSlot createGenericSale(final ContractProfile<?> profile, final SubContractProfile<?> subProfile) {

		final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();

		Contract contract = profile.getContract();
		slot.setContract(contract);
		slot.setPort(contract.getPreferredPort());
		if (contract.getContractType() == ContractType.DES) {
			slot.setFOBSale(true);
			slot.setNominatedVessel(subProfile.getNominatedVessel());
			if (subProfile.getShippingDays() > 0) {
				slot.setDivertible(true);
				slot.setShippingDaysRestriction(subProfile.getShippingDays());
			}
		}

		return slot;
	}
}
