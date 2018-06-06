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
			return (T) createGenericFOBPurchase(profile, subProfile);
		case TEMPLATE_GENERIC_FOB_SALE:
			return (T) createGenericFOBSale(profile, subProfile);
		case TEMPLATE_GENERIC_DES_PURCHASE:
			return (T) createGenericDESPurchase(profile, subProfile);
		case TEMPLATE_GENERIC_DES_SALE:
			return (T) createGenericDESSale(profile, subProfile);
		}

		throw new IllegalArgumentException("Unknown template ID " + templateID);
	}

	@Override
	public <T extends Slot> String generateName(final String templateID, final ContractProfile<T> profile, final SubContractProfile<T> subProfile, final YearMonth yearStart, final int i) {

		final String contractShortName = DistributionModelGeneratorUtil.getContractShortName(profile, profile.getContract());
		final String type = subProfile.getName();
		return String.format("%2d-%s-%s-%02d", yearStart.getYear() - 2000, contractShortName, type, 1 + i);
	}

	protected LoadSlot createGenericFOBPurchase(final ContractProfile<?> profile, final SubContractProfile<?> subProfile) {

		final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();

		slot.setContract(profile.getContract());
		slot.setPort(profile.getContract().getPreferredPort());
		return slot;
	}

	protected LoadSlot createGenericDESPurchase(final ContractProfile<?> profile, final SubContractProfile<?> subProfile) {

		final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
		slot.setDESPurchase(true);

		slot.setContract(profile.getContract());
		slot.setPort(profile.getContract().getPreferredPort());

		slot.setNominatedVessel(subProfile.getNominatedVessel());

		return slot;
	}

	protected DischargeSlot createGenericDESSale(final ContractProfile<?> profile, final SubContractProfile<?> subProfile) {

		final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();

		slot.setContract(profile.getContract());
		slot.setPort(profile.getContract().getPreferredPort());
		return slot;
	}

	protected DischargeSlot createGenericFOBSale(final ContractProfile<?> profile, final SubContractProfile<?> subProfile) {

		final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
		slot.setFOBSale(true);

		slot.setContract(profile.getContract());
		slot.setPort(profile.getContract().getPreferredPort());
		slot.setNominatedVessel(subProfile.getNominatedVessel());

		return slot;
	}
}
