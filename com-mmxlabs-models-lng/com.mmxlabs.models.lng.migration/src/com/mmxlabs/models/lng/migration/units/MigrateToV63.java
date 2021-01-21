/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV63 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 62;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 63;
	}

	@Override
	protected void doMigration(final MigrationModelRecord modelRecord) {
		final MetamodelLoader loader = modelRecord.getMetamodelLoader();
		final EObjectWrapper model = modelRecord.getModelRoot();
		final EObjectWrapper cargoModel = model.getRef("cargoModel");
		final List<EObjectWrapper> availabilities = cargoModel.getRefAsList("vesselAvailabilities");

		// Consumer to update the set the fleet flag by default
		if (availabilities != null) {
			availabilities.forEach(va -> va.setAttrib("fleet", true));
		}

		// Move any existing analytics models.
		final EObjectWrapper referenceModel = model.getRef("referenceModel");
		if (referenceModel != null) {
			final EObjectWrapper analysisModel = referenceModel.getRef("analyticsModel");
			if (analysisModel != null) {
				model.setRef("analyticsModel", analysisModel);
				// Should have been cleared by above call....
				referenceModel.unsetFeature("analyticsModel");
			}
		}

		final EPackage package_CargoPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);
		final EClass class_DischargeSlot = MetamodelUtils.getEClass(package_CargoPackage, "DischargeSlot");

		final EPackage package_SchedulePackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);

		final EEnum enum_SlotAllocationType = MetamodelUtils.getEEnum(package_SchedulePackage, "SlotAllocationType");
		final EEnumLiteral enum_SlotAllocationType_PURCHASE = MetamodelUtils.getEEnum_Literal(enum_SlotAllocationType, "PURCHASE");
		final EEnumLiteral enum_SlotAllocationType_SALE = MetamodelUtils.getEEnum_Literal(enum_SlotAllocationType, "SALE");

		final EObjectWrapper scheduleModel = model.getRef("scheduleModel");
		if (scheduleModel != null) {
			final EObjectWrapper schedule = scheduleModel.getRef("schedule");
			if (schedule != null) {
				final List<EObjectWrapper> slotAllocations = schedule.getRefAsList("slotAllocations");
				if (slotAllocations != null) {
					for (final EObjectWrapper slotAllocation : slotAllocations) {
						final EObjectWrapper slot = slotAllocation.getRef("slot");
						if (class_DischargeSlot.isInstance(slot)) {
							slotAllocation.setAttrib("slotAllocationType", enum_SlotAllocationType_SALE);
						} else {
							slotAllocation.setAttrib("slotAllocationType", enum_SlotAllocationType_PURCHASE);
						}
					}
				}
			}
		}
	}
}
