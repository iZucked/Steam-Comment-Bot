/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV125 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 124;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 125;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		EPackage cargoModelPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);
		EPackage scheduleModelPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);

		EObjectWrapper modelRoot = modelRecord.getModelRoot();

		EClass classSlot = MetamodelUtils.getEClass(cargoModelPackage, "Slot");
		EClass classPNLDetails = MetamodelUtils.getEClass(scheduleModelPackage, "BasicSlotPNLDetails");

		EObjectWrapper cargoModel = modelRoot.getRef("cargoModel");
		EObjectWrapper scheduleModel = modelRoot.getRef("scheduleModel");

		EObjectWrapper analyticsModel = modelRoot.getRef("cargoModel");

		List<EObjectWrapper> loadSlots = cargoModel.getRefAsList("loadSlots");
		List<EObjectWrapper> dischargeSlots = cargoModel.getRefAsList("dischargeSlots");

		if (loadSlots != null) {
			loadSlots.forEach(s -> s.unsetFeature("hedges"));
		}
		if (loadSlots != null) {
			dischargeSlots.forEach(s -> s.unsetFeature("hedges"));
		}

		scheduleModel.eAllContents().forEachRemaining(obj -> {
			if (classSlot.isInstance(obj)) {
				((EObjectWrapper) obj).unsetFeature("hedges");
			}
			if (classPNLDetails.isInstance(obj)) {
				((EObjectWrapper) obj).unsetFeature("hedgingValue");
			}
		});

		analyticsModel.eAllContents().forEachRemaining(obj -> {
			if (classSlot.isInstance(obj)) {
				((EObjectWrapper) obj).unsetFeature("hedges");
			}
			if (classPNLDetails.isInstance(obj)) {
				((EObjectWrapper) obj).unsetFeature("hedgingValue");
			}
		});
	}
}