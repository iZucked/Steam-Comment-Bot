/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV69 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 68;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 69;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {

		final EPackage cargoPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);
		final EEnum enum_VesselTankState = MetamodelUtils.getEEnum(cargoPackage, "EVesselTankState");
		final EClass class_StartHeelOptions = MetamodelUtils.getEClass(cargoPackage, "StartHeelOptions");
		final EClass class_EndHeelOptions = MetamodelUtils.getEClass(cargoPackage, "EndHeelOptions");
		final EClass class_CharterOutEvent = MetamodelUtils.getEClass(cargoPackage, "CharterOutEvent");

		final EEnumLiteral enum_EITHER = MetamodelUtils.getEEnum_Literal(enum_VesselTankState, "EITHER");
		final EEnumLiteral enum_MUST_BE_COLD = MetamodelUtils.getEEnum_Literal(enum_VesselTankState, "MUST_BE_COLD");
		final EEnumLiteral enum_MUST_BE_WARM = MetamodelUtils.getEEnum_Literal(enum_VesselTankState, "MUST_BE_WARM");

		final EObjectWrapper cargoModel = model.getRef("cargoModel");
		if (cargoModel == null) {
			return;
		}
		final List<EObjectWrapper> vesselAvailabilities = cargoModel.getRefAsList("vesselAvailabilities");
		if (vesselAvailabilities != null) {
			for (final EObjectWrapper vesselAvailability : vesselAvailabilities) {
				EObjectWrapper ref = vesselAvailability.getRef("vessel");
				String name = ref.getAttrib("name");
				final EObjectWrapper startHeel = vesselAvailability.getRef("startHeel");
				final EObjectWrapper newStartHeel = (EObjectWrapper) cargoPackage.getEFactoryInstance().create(class_StartHeelOptions);
				if (startHeel != null) {
					final double startHeelVolume = startHeel.getAttrib("volumeAvailable");
					newStartHeel.setAttrib("minVolumeAvailable", startHeelVolume);
					newStartHeel.setAttrib("maxVolumeAvailable", startHeelVolume);
					newStartHeel.setAttrib("priceExpression", Double.toString(startHeel.getAttrib("pricePerMMBTU")));
					newStartHeel.setAttrib("cvValue", startHeel.getAttrib("cvValue"));
				}
				vesselAvailability.setRef("startHeelTmp", newStartHeel);
				vesselAvailability.unsetFeature("startHeel");

				// DEBUG: Why does the "CHarter Ship" end warm?
				final EObjectWrapper endHeel = vesselAvailability.getRef("endHeel");
				if (endHeel != null) {
					if (endHeel.isSetFeature("targetEndHeel")) {
						final int endHeelVolume = endHeel.getAttrib("targetEndHeel");
						if (endHeelVolume == 0) {
							endHeel.setAttrib("tankState", enum_MUST_BE_WARM);
						} else {
							endHeel.setAttrib("minimumEndHeel", endHeelVolume);
							endHeel.setAttrib("maximumEndHeel", endHeelVolume);
						}
						endHeel.setAttrib("tankState", enum_MUST_BE_COLD);
					} else {
						endHeel.setAttrib("tankState", enum_EITHER);
					}
					endHeel.unsetFeature("targetEndHeel");
				}
			}
		}
		final List<EObjectWrapper> vesselEvents = cargoModel.getRefAsList("vesselEvents");
		if (vesselEvents != null) {
			for (final EObjectWrapper vesselEvent : vesselEvents) {
				if (class_CharterOutEvent.isInstance(vesselEvent)) {
					final EObjectWrapper startHeel = vesselEvent.getRef("heelOptions");
					final EObjectWrapper newStartHeel = (EObjectWrapper) cargoPackage.getEFactoryInstance().create(class_StartHeelOptions);
					if (startHeel != null) {
						final double startHeelVolume = startHeel.getAttrib("volumeAvailable");
						newStartHeel.setAttrib("minVolumeAvailable", startHeelVolume);
						newStartHeel.setAttrib("maxVolumeAvailable", startHeelVolume);
						newStartHeel.setAttrib("priceExpression", Double.toString(startHeel.getAttrib("pricePerMMBTU")));
						newStartHeel.setAttrib("cvValue", startHeel.getAttrib("cvValue"));
					}

					vesselEvent.setRef("availableHeel", newStartHeel);
					vesselEvent.unsetFeature("heelOptions");

					final EObjectWrapper newEndHeel = (EObjectWrapper) cargoPackage.getEFactoryInstance().create(class_EndHeelOptions);
					newEndHeel.setAttrib("tankState", enum_EITHER);
					vesselEvent.setRef("requiredHeel", newEndHeel);
					newEndHeel.setAttrib("minimumEndHeel", 0);
					newEndHeel.setAttrib("maximumEndHeel", 0);
				}
			}
		}
	}
}
