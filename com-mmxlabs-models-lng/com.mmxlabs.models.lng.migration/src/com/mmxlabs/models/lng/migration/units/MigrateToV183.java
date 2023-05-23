/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;
import java.util.function.Consumer;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV183 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 182;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 183;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {

		EObjectWrapper scenarioModel = modelRecord.getModelRoot();
		EObjectWrapper referenceModel = scenarioModel.getRef("referenceModel");
		final EPackage commercialPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CommercialModel);

		EObjectWrapper commercialModel = referenceModel.getRef("commercialModel");
		Consumer<List<EObjectWrapper>> cleanUpContracts = contracts -> {
			if (contracts != null) {
				for (EObjectWrapper contract : contracts) {
					
					final EClass eClassExpressionPriceParameters = MetamodelUtils.getEClass(commercialPackage, "ExpressionPriceParameters");
					final EObjectWrapper priceInfo = contract.getRef("priceInfo");
					if (eClassExpressionPriceParameters.isInstance(priceInfo)) {
						priceInfo.unsetFeature("pricingBasis");
						priceInfo.unsetFeature("preferredPBs");
					}
				}
			}
		};
		cleanUpContracts.accept(commercialModel.getRefAsList("purchaseContracts"));
		cleanUpContracts.accept(commercialModel.getRefAsList("salesContracts"));

		EObjectWrapper cargoModel = scenarioModel.getRef("cargoModel");

		Consumer<List<EObjectWrapper>> cleanUpRecords = recs -> {
			if (recs != null) {
				for (EObjectWrapper r : recs) {
					r.unsetFeature("pricingBasis");
				}
			}
		};
		cleanUpRecords.accept(cargoModel.getRefAsList("loadSlots"));
		cleanUpRecords.accept(cargoModel.getRefAsList("dischargeSlots"));
		
		EObjectWrapper transferModel = scenarioModel.getRef("transferModel");
		
		if (transferModel != null) {
			cleanUpRecords.accept(transferModel.getRefAsList("transferRecords"));
			cleanUpRecords.accept(transferModel.getRefAsList("transferAgreements"));
			
			Consumer<List<EObjectWrapper>> cleanUpAgreements = recs -> {
				if (recs != null) {
					for (EObjectWrapper r : recs) {
						r.unsetFeature("preferredPBs");
					}
				}
			};
			
			cleanUpAgreements.accept(transferModel.getRefAsList("transferAgreements"));
		}
		
		EObjectWrapper pricingModel = referenceModel.getRef("pricingModel");
		
		pricingModel.unsetFeature("pricingBases");
		
	}
}
