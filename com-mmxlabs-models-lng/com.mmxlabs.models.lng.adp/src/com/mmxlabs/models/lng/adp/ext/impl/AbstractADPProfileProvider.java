/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext.impl;

import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel;
import com.mmxlabs.models.lng.adp.CargoNumberDistributionModel;
import com.mmxlabs.models.lng.adp.IntervalType;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.ext.IADPProfileProvider;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.types.VolumeUnits;

@NonNullByDefault
public abstract class AbstractADPProfileProvider implements IADPProfileProvider {

	protected PurchaseContractProfile createGenericModel(final PurchaseContract contract, final int volume, final VolumeUnits volUnits) {
		// Base
		final PurchaseContractProfile profile = ADPFactory.eINSTANCE.createPurchaseContractProfile();
		profile.setEnabled(true);
		profile.setCustom(false);
		profile.setContract(contract);

		if (contract.getCode() == null || contract.getCode().isEmpty()) {
			profile.setContractCode(contract.getName());
		} else {
			profile.setContractCode(contract.getCode());
		}

		// Some defaults
		profile.setTotalVolume(volume);
		profile.setVolumeUnit(volUnits);

		return profile;
	}

	protected SalesContractProfile createGenericModel(final SalesContract contract, final int volume, final VolumeUnits volUnits) {
		// Base
		final SalesContractProfile profile = ADPFactory.eINSTANCE.createSalesContractProfile();
		profile.setEnabled(true);
		profile.setCustom(false);
		profile.setContract(contract);

		if (contract.getCode() != null) {
			profile.setContractCode(contract.getCode());
		}
		// Some defaults
		profile.setTotalVolume(volume);
		profile.setVolumeUnit(volUnits);

		return profile;
	}

	protected SubContractProfile<LoadSlot> createCargoNumberSubProfile(final PurchaseContract contract, final ContractType contractType, final String name, final int numCargoes, final int volume,
			final VolumeUnits volumeUnits) {

		return createCargoNumberSubProfile(contract, contractType, name, numCargoes, volume, volumeUnits, null);
	}

	protected SubContractProfile<LoadSlot> createCargoNumberSubProfile(final PurchaseContract contract, final ContractType contractType, final String name, final int numCargoes, final int volume,
			final VolumeUnits volumeUnits, @Nullable Consumer<SubContractProfile<LoadSlot>> action) {
		final SubContractProfile<LoadSlot> subProfile = ADPFactory.eINSTANCE.createSubContractProfile();
		subProfile.setName(name);

		final CargoNumberDistributionModel model = ADPFactory.eINSTANCE.createCargoNumberDistributionModel();
		model.setNumberOfCargoes(numCargoes);

		subProfile.setDistributionModel(model);

		if (contractType == ContractType.DES) {
			subProfile.setSlotTemplateId(AbstractSlotTemplateFactory.TEMPLATE_GENERIC_DES_PURCHASE);
		} else {
			subProfile.setSlotTemplateId(AbstractSlotTemplateFactory.TEMPLATE_GENERIC_FOB_PURCHASE);
		}
		subProfile.setTotalVolume(volume);
		subProfile.setContractType(contractType);
		subProfile.setVolumeUnit(volumeUnits);

		if (action != null) {
			action.accept(subProfile);
		}

		return subProfile;
	}

	protected SubContractProfile<DischargeSlot> createCargoNumberSubProfile(final SalesContract contract, final ContractType contractType, final String name, final int numCargoes, final int volume,
			final VolumeUnits volumeUnits) {

		return createCargoNumberSubProfile(contract, contractType, name, numCargoes, volume, volumeUnits, null);
	}

	protected SubContractProfile<DischargeSlot> createCargoNumberSubProfile(final SalesContract contract, final ContractType contractType, final String name, final int numCargoes, final int volume,
			final VolumeUnits volumeUnits, @Nullable Consumer<SubContractProfile<DischargeSlot>> action) {
		final SubContractProfile<DischargeSlot> subProfile = ADPFactory.eINSTANCE.createSubContractProfile();
		subProfile.setName(name);
		final CargoNumberDistributionModel model = ADPFactory.eINSTANCE.createCargoNumberDistributionModel();
		model.setNumberOfCargoes(numCargoes);

		subProfile.setDistributionModel(model);

		if (contractType == ContractType.FOB) {
			subProfile.setSlotTemplateId(AbstractSlotTemplateFactory.TEMPLATE_GENERIC_FOB_SALE);
		} else {
			subProfile.setSlotTemplateId(AbstractSlotTemplateFactory.TEMPLATE_GENERIC_DES_SALE);
		}
		subProfile.setContractType(contractType);
		subProfile.setTotalVolume(volume);
		subProfile.setVolumeUnit(volumeUnits);

		if (action != null) {
			action.accept(subProfile);
		}

		return subProfile;
	}

	protected SubContractProfile<DischargeSlot> createCargoIntervalSubProfile(final SalesContract contract, final ContractType contractType, final String name, IntervalType intervalType, int spacing,
			int quantity, final int perCargoVolume, final VolumeUnits volumeUnits, @Nullable Consumer<SubContractProfile<DischargeSlot>> action) {
		final SubContractProfile<DischargeSlot> subProfile = ADPFactory.eINSTANCE.createSubContractProfile();
		subProfile.setName(name);

		final CargoIntervalDistributionModel model = ADPFactory.eINSTANCE.createCargoIntervalDistributionModel();
		model.setIntervalType(intervalType);
		model.setSpacing(spacing);
		model.setQuantity(quantity);
		subProfile.setDistributionModel(model);

		if (contractType == ContractType.FOB) {
			subProfile.setSlotTemplateId(AbstractSlotTemplateFactory.TEMPLATE_GENERIC_FOB_SALE);
		} else {
			subProfile.setSlotTemplateId(AbstractSlotTemplateFactory.TEMPLATE_GENERIC_DES_SALE);
		}
		subProfile.setContractType(contractType);
		subProfile.setTotalVolume(perCargoVolume);
		subProfile.setVolumeUnit(volumeUnits);

		if (action != null) {
			action.accept(subProfile);
		}

		return subProfile;
	}

 
}
