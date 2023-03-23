/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;
import java.util.function.BiConsumer;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

@NonNullByDefault
public class MigrateToV178 extends AbstractMigrationUnit {

	private static final String CARGOES_FOR_EXPOSURES = "cargoesForExposures";
	private static final String CARGOES_FOR_HEDGING = "cargoesForHedging";


	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 177;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 178;
	}

	/*
	 * Individual exposures and hedging are now controlled by the flag on the slot
	 * Migration would take the lists of cargoes for exposures and hedging
	 * Iterate over the slots on the cargoes
	 * Set the relevant flags for non spot slots
	 * And remove the lists from the cargo model, once done.
	 * 
	 * 1. Grab slots from CargoModel's cargoesForExposures and cargoesForHedging
	 * 2. Set flags on the slots, as long as these are not SpotSlot
	 * 3. Clear and remove lists
	 */
	@Override
	protected void doMigration(final MigrationModelRecord modelRecord) {
		final EPackage cargoPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);

		final EClass spotSlotClass = MetamodelUtils.getEClass(cargoPackage, "SpotSlot");

		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();
		final EObjectWrapper cargoModel = scenarioModel.getRef("cargoModel");

		BiConsumer<List<EObjectWrapper>, String> processListToFlag = (cargoes, attributeName) -> {
			if (cargoes != null) {
				cargoes.forEach( cargo -> {
					if (cargo != null) {
						final List<EObjectWrapper> slots = cargo.getRefAsList("slots");
						if (slots != null) {
							slots.forEach( slot -> {
								if (!spotSlotClass.isInstance(slot)) {
									slot.setAttrib(attributeName, Boolean.TRUE);
								}
							});
						}
					}
				});
			}
		};

		processListToFlag.accept(cargoModel.getRefAsList(CARGOES_FOR_EXPOSURES), "computeExposure");
		processListToFlag.accept(cargoModel.getRefAsList(CARGOES_FOR_HEDGING), "computeHedge");

		cargoModel.unsetFeature(CARGOES_FOR_EXPOSURES);
		cargoModel.unsetFeature(CARGOES_FOR_HEDGING);
	}
}