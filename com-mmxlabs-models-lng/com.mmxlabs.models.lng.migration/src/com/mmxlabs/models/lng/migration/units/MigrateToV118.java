/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV118 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 117;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 118;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();
		final EObjectWrapper referenceModel = scenarioModel.getRef("referenceModel");
		final EPackage commercialPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CommercialModel);
		final EObjectWrapper commercialModel = referenceModel.getRef("commercialModel");

		//if only one entity present, then set that in CharterInMarket objects, otherwise we have a problem.
		final List<EObjectWrapper> entities = commercialModel.getRefAsList("entities");
		EObjectWrapper defaultEntity = null;
		if (entities.size() == 1) {
			defaultEntity = entities.get(0);
		}
		
		//for each ballast bonus contract, remove entity from BallastBonusContract.
		final EClass ballastBonusContractClass = MetamodelUtils.getEClass(commercialPackage, "BallastBonusCharterContract");
		final List<EObjectWrapper> charteringContracts = commercialModel.getRefAsList("charteringContracts");
		
		//Go through the VesselAvailabilities, if no entity copy from BB contract if there is one, else use default entity if one.
		final EObjectWrapper cargoModel = scenarioModel.getRef("cargoModel");
		final List<EObjectWrapper> vesselAvailabilities = cargoModel.getRefAsList("vesselAvailabilities");
		for (EObjectWrapper vesselAvailability : vesselAvailabilities) {
			if (!vesselAvailability.isSetFeature("entity")) {
				if (vesselAvailability.isSetFeature("charterContract")) {
					EObjectWrapper entity = getEntityFromCharterContract(ballastBonusContractClass, vesselAvailability.getRef("charterContract"));
					if (entity != null) {
						 vesselAvailability.setRef("entity", entity);
					}
				}
				if (defaultEntity != null && vesselAvailability.getRef("entity") == null) {
					 vesselAvailability.setRef("entity", defaultEntity);
				}
			}
		}
		
		
		//Go through the CharterIn markets, take from BB contract if there is one, else use default entity.
		final EObjectWrapper spotMarketsModel = referenceModel.getRef("spotMarketsModel");
		final List<EObjectWrapper> charterInMarkets = spotMarketsModel.getRefAsList("charterInMarkets");
		for (EObjectWrapper charterInMarket : charterInMarkets) {
			if (charterInMarket.isSetFeature("charterContract")) {
				EObjectWrapper entity = getEntityFromCharterContract(ballastBonusContractClass, charterInMarket.getRef("charterContract"));
				if (entity != null) {
					charterInMarket.setRef("entity", entity);
				}
			}
			if (defaultEntity != null && charterInMarket.getRef("entity") == null) {
				charterInMarket.setRef("entity", defaultEntity);
			}
		}
		
		//Remove old entity from BB contracts.
		charteringContracts.forEach(cc -> removeObsoleteEntityFromBallastBonusContract(ballastBonusContractClass, cc));
	}
	
	private EObjectWrapper getEntityFromCharterContract(final EClass ballastBonusContractClass, EObjectWrapper cc) {
		if (ballastBonusContractClass.isInstance(cc)) {
			if (cc.isSetFeature("entity")) {
				EObjectWrapper entity = cc.getRef("entity");
				return entity;
			}
		}
		return null;
	}
	
	private List<EObjectWrapper> getBBEntities(final EClass ballastBonusContractClass, final List<EObjectWrapper> charteringContracts) {
		List<EObjectWrapper> entities = new ArrayList<EObjectWrapper>();
		for (EObjectWrapper cc : charteringContracts) {
			if (ballastBonusContractClass.isInstance(cc)) {
				if (cc.isSetFeature("entity")) {
					EObjectWrapper entity = cc.getRef("entity");
					if (!entityPresent(entities, entity)) {
						entities.add(entity);
					}
				}
			}
		}
		return entities;
	}
		
	private boolean entityPresent(List<EObjectWrapper> entities, EObjectWrapper entity) {
		final String name = entity.getAttrib("name");
		for (EObjectWrapper entity1 : entities) {
			if (entity1.isSetFeature("name") && name.equals(entity1.getAttrib("name"))) {
				return true;
			}
		}
		return false;
	}
		
	
	private void removeObsoleteEntityFromBallastBonusContract(final EClass ballastBonusContractClass, EObjectWrapper cc) {
		if (ballastBonusContractClass.isInstance(cc)) {
			if (cc.isSetFeature("entity")) {
				cc.unsetFeature("entity");
			}
		}
	}
}
