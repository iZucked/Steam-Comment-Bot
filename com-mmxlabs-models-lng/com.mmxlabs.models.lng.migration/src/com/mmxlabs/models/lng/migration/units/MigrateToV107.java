/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;
import java.util.function.Consumer;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;

public class MigrateToV107 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 106;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 107;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {

		EObjectWrapper scenarioModel = modelRecord.getModelRoot();
		EObjectWrapper referenceModel = scenarioModel.getRef("referenceModel");

		EObjectWrapper commercialModel = referenceModel.getRef("commercialModel");
		Consumer<List<EObjectWrapper>> func = contracts -> {
			if (contracts != null) {
				for (EObjectWrapper contract : contracts) {

					boolean permissive = contract.getAttribAsBoolean("restrictedListsArePermissive");
					boolean pContracts = permissive;
					boolean pPorts = permissive;
					if (permissive) {
						List<EObjectWrapper> restrictedPorts = contract.getRefAsList("restrictedPorts");
						if (restrictedPorts == null || restrictedPorts.isEmpty()) {
							pPorts = false;
						}
						List<EObjectWrapper> restrictedContracts = contract.getRefAsList("restrictedContracts");
						if (restrictedContracts == null || restrictedContracts.isEmpty()) {
							pContracts = false;
						}
					}
					contract.setAttrib("restrictedContractsArePermissive", pContracts);
					contract.setAttrib("restrictedPortsArePermissive", pPorts);

					contract.unsetFeature("restrictedListsArePermissive");
				}
			}
		};
		func.accept(commercialModel.getRefAsList("purchaseContracts"));
		func.accept(commercialModel.getRefAsList("salesContracts"));

		EObjectWrapper cargoModel = scenarioModel.getRef("cargoModel");

		Consumer<List<EObjectWrapper>> func2 = slots -> {
			if (slots != null) {
				for (EObjectWrapper slot : slots) {
					boolean overridesSet = slot.getAttribAsBoolean("overrideRestrictions");
					if (overridesSet) {
						boolean permissive = slot.getAttribAsBoolean("restrictedListsArePermissive");
						
						boolean pContracts = permissive;
						boolean pPorts = permissive;
						if (permissive) {
							List<EObjectWrapper> restrictedPorts = slot.getRefAsList("restrictedPorts");
							if (restrictedPorts == null || restrictedPorts.isEmpty()) {
								pPorts = false;
							}
							List<EObjectWrapper> restrictedContracts = slot.getRefAsList("restrictedContracts");
							if (restrictedContracts == null || restrictedContracts.isEmpty()) {
								pContracts = false;
							}
						}
						slot.setAttrib("restrictedContractsArePermissive", pContracts);
						slot.setAttrib("restrictedPortsArePermissive", pPorts);

						slot.setAttrib("restrictedContractsOverride", Boolean.TRUE);
						slot.setAttrib("restrictedPortsOverride", Boolean.TRUE);
					}

					List<EObjectWrapper> allowedVessels = slot.getRefAsList("allowedVessels");
					if (allowedVessels != null && !allowedVessels.isEmpty()) {
						slot.setAttrib("restrictedVesselsArePermissive", Boolean.TRUE);
						slot.setAttrib("restrictedVesselsOverride", Boolean.TRUE);
						slot.setRef("restrictedVessels", allowedVessels);
					}

					slot.unsetFeature("overrideRestrictions");
					slot.unsetFeature("restrictedListsArePermissive");
					slot.unsetFeature("allowedVessels");
				}
			}
		};
		func2.accept(cargoModel.getRefAsList("loadSlots"));
		func2.accept(cargoModel.getRefAsList("dischargeSlots"));
		
		EObjectWrapper spotMarketModel = referenceModel.getRef("spotMarketsModel");
		EObjectWrapper spotMarketGroup = spotMarketModel.getRef("desSalesSpotMarket");
		List<EObjectWrapper> markets = spotMarketGroup.getRefAsList("markets");
		if (markets != null) {
			for(EObjectWrapper market : markets) {
				boolean permissive = market.getAttribAsBoolean("restrictedListsArePermissive");
				market.setAttrib("restrictedContractsArePermissive", permissive);
				market.setAttrib("restrictedPortsArePermissive", permissive);
				
				List<EObjectWrapper> allowedVessels = market.getRefAsList("allowedVessels");
				if (allowedVessels != null && !allowedVessels.isEmpty()) {
					market.setAttrib("restrictedVesselsArePermissive", Boolean.TRUE);
					market.setRef("restrictedVessels", allowedVessels);
				}
				market.unsetFeature("allowedVessels");
				market.unsetFeature("restrictedListsArePermissive");
			}
		}
		
	}
}
