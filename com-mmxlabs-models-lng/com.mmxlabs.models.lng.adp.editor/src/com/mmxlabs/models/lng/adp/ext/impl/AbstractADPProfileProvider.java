/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext.impl;

import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel;
import com.mmxlabs.models.lng.adp.CargoNumberDistributionModel;
import com.mmxlabs.models.lng.adp.IntervalType;
import com.mmxlabs.models.lng.adp.LNGVolumeUnit;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.ext.IADPProfileProvider;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.types.VolumeUnits;

public abstract class AbstractADPProfileProvider implements IADPProfileProvider {
	@Override
	public void createProfiles(final LNGScenarioModel scenarioModel, final CommercialModel commercialModel, final ADPModel existingModel) {
		for (final PurchaseContract contract : commercialModel.getPurchaseContracts()) {
			PurchaseContractProfile profile = createProfile(scenarioModel, commercialModel, existingModel, contract);
			if (profile != null) {
				existingModel.getPurchaseContractProfiles().add(profile);
			}
		}

		for (final SalesContract contract : commercialModel.getSalesContracts()) {
			SalesContractProfile profile = createProfile(scenarioModel, commercialModel, existingModel, contract);
			if (profile != null) {
				existingModel.getSalesContractProfiles().add(profile);
			}
		}

	}

	public abstract @Nullable PurchaseContractProfile createProfile(final LNGScenarioModel scenarioModel, final CommercialModel commercialModel, final ADPModel existingModel,
			PurchaseContract purchaseContract);

	public abstract @Nullable SalesContractProfile createProfile(final LNGScenarioModel scenarioModel, final CommercialModel commercialModel, final ADPModel existingModel,
			SalesContract salesContract);

	protected PurchaseContractProfile createGenericModel(final PurchaseContract contract, final int volume, final LNGVolumeUnit volUnits) {
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

	protected SalesContractProfile createGenericModel(final SalesContract contract, final int volume, final LNGVolumeUnit volUnits) {
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

	protected SubContractProfile<LoadSlot, PurchaseContract> createCargoNumberSubProfile(final PurchaseContract contract, final ContractType contractType, final String name, final int numCargoes,
			final int volume, final LNGVolumeUnit volumeUnits) {

		return createCargoNumberSubProfile(contract, contractType, name, numCargoes, volume, volumeUnits, null);
	}

	protected SubContractProfile<LoadSlot, PurchaseContract> createCargoNumberSubProfile(final PurchaseContract contract, final ContractType contractType, final String name, final int numCargoes,
			final int volume, final LNGVolumeUnit volumeUnits, @Nullable Consumer<SubContractProfile<LoadSlot, PurchaseContract>> action) {
		final SubContractProfile<LoadSlot, PurchaseContract> subProfile = ADPFactory.eINSTANCE.createSubContractProfile();
		subProfile.setName(name);

		final CargoNumberDistributionModel model = ADPFactory.eINSTANCE.createCargoNumberDistributionModel();
		model.setNumberOfCargoes(numCargoes);

		subProfile.setDistributionModel(model);

		if (contractType == ContractType.DES) {
			subProfile.setSlotTemplateId(AbstractSlotTemplateFactory.TEMPLATE_GENERIC_DES_PURCHASE);
		} else {
			subProfile.setSlotTemplateId(AbstractSlotTemplateFactory.TEMPLATE_GENERIC_FOB_PURCHASE);
		}
		subProfile.setContractType(contractType);

		if (action != null) {
			action.accept(subProfile);
		}

		return subProfile;
	}

	protected SubContractProfile<DischargeSlot, SalesContract> createCargoNumberSubProfile(final SalesContract contract, final ContractType contractType, final String name, final int numCargoes,
			final int volume, final LNGVolumeUnit volumeUnits) {

		return createCargoNumberSubProfile(contract, contractType, name, numCargoes, volume, volumeUnits, null);
	}

	protected SubContractProfile<DischargeSlot, SalesContract> createCargoNumberSubProfile(final SalesContract contract, final ContractType contractType, final String name, final int numCargoes,
			final int volume, final LNGVolumeUnit volumeUnits, @Nullable Consumer<SubContractProfile<DischargeSlot, SalesContract>> action) {
		final SubContractProfile<DischargeSlot, SalesContract> subProfile = ADPFactory.eINSTANCE.createSubContractProfile();
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

		if (action != null) {
			action.accept(subProfile);
		}

		return subProfile;
	}

	protected SubContractProfile<DischargeSlot, SalesContract> createCargoIntervalSubProfile(final SalesContract contract, final ContractType contractType, final String name,
			IntervalType intervalType, int spacing, int quantity, final int perCargoVolume, final LNGVolumeUnit volumeUnits,
			@Nullable Consumer<SubContractProfile<DischargeSlot, SalesContract>> action) {
		final SubContractProfile<DischargeSlot, SalesContract> subProfile = ADPFactory.eINSTANCE.createSubContractProfile();
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

		if (action != null) {
			action.accept(subProfile);
		}

		return subProfile;
	}

	protected LNGVolumeUnit mapVolumeUnits(VolumeUnits units) {
		switch (units) {
		case M3:
			return LNGVolumeUnit.M3;
		case MMBTU:
			return LNGVolumeUnit.MMBTU;
		default:
			throw new IllegalArgumentException();
		}
	}

}
