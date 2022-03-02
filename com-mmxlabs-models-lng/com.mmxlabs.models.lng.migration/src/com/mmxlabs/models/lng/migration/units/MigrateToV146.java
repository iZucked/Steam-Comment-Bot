/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV146 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 145;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 146;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		final @NonNull MetamodelLoader loader = modelRecord.getMetamodelLoader();
		final @NonNull EObjectWrapper scenarioModel = modelRecord.getModelRoot();
		
		final EObjectWrapper cargoModel = scenarioModel.getRef("cargoModel");
		final EPackage cargoPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);
		
		processBookings(cargoModel, cargoPackage);
	}
	
	private void processBookings(final EObjectWrapper cargoModel, final EPackage cargoPackage) {
		
		final EObjectWrapper canalBookings = cargoModel.getRef("canalBookings");
		if (canalBookings != null) {
			final List<EObjectWrapper> panamaSeasonalityRecords = new ArrayList<EObjectWrapper>();
			final List<EObjectWrapper> vesselGroupCanalParameters = canalBookings.getRefAsList("vesselGroupCanalParameters");
			if (vesselGroupCanalParameters != null) {
				for(final EObjectWrapper vgcp : vesselGroupCanalParameters) {
					final String bc = vgcp.getAttrib("name");
					final Integer nb = vgcp.getAttrib("northboundWaitingDays");
					final Integer sb = vgcp.getAttrib("southboundWaitingDays");

					vgcp.unsetFeature("northboundWaitingDays");
					vgcp.unsetFeature("southboundWaitingDays");

					if (bc != null && nb != null && sb != null) {
						panamaSeasonalityRecords.add(createPanamaSeasonalityRecord(cargoPackage, vgcp, nb, sb));
					}
				}
			}
			canalBookings.setRef("panamaSeasonalityRecords", panamaSeasonalityRecords);
		} else {
			createDefaultCanalBookingsObject(cargoPackage, cargoModel);
		}
	}
	
	private EObjectWrapper createPanamaSeasonalityRecord(final EPackage cargoPackage, final EObjectWrapper vgcp, final Integer nb, final Integer sb) {
		final EClass class_PanamaSeasonalityRecord = MetamodelUtils.getEClass(cargoPackage, "PanamaSeasonalityRecord");
		final EObjectWrapper panamaSeasonalityRecord = (EObjectWrapper) cargoPackage.getEFactoryInstance().create(class_PanamaSeasonalityRecord);
		
		panamaSeasonalityRecord.setRef("vesselGroupCanalParameter", vgcp);
		panamaSeasonalityRecord.setAttrib("northboundWaitingDays", nb);
		panamaSeasonalityRecord.setAttrib("southboundWaitingDays", sb);
		
		return panamaSeasonalityRecord;
	}
	
	private void createDefaultCanalBookingsObject(final EPackage cargoPackage, final EObjectWrapper cargoModel) {
		final EClass class_CanalBookings = MetamodelUtils.getEClass(cargoPackage, "CanalBookings");
		final EObjectWrapper canalBookings = (EObjectWrapper) cargoPackage.getEFactoryInstance().create(class_CanalBookings);
		
		final EClass class_VesselGroupCanalParameters = MetamodelUtils.getEClass(cargoPackage, "VesselGroupCanalParameters");
		final EObjectWrapper vesselGroupCanalParameters = (EObjectWrapper) cargoPackage.getEFactoryInstance().create(class_VesselGroupCanalParameters);
		vesselGroupCanalParameters.setAttrib("name", "default");
		final List<EObjectWrapper> vgcp = new ArrayList<>(1);
		vgcp.add(vesselGroupCanalParameters);
		canalBookings.setRef("vesselGroupCanalParameters", vgcp);
		
		final List<EObjectWrapper> panamaSeasonalityRecords = new ArrayList<>(1);
		panamaSeasonalityRecords.add(createPanamaSeasonalityRecord(cargoPackage, vesselGroupCanalParameters, 0, 0));
		
		canalBookings.setRef("panamaSeasonalityRecords", panamaSeasonalityRecords);
		
		cargoModel.setRef("canalBookings", canalBookings);
	}
}