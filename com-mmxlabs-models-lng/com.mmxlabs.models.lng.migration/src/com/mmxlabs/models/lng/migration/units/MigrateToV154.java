/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;
import java.util.function.Consumer;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV154 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 153;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 154;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {

		// Section 1 - clean up obsolete optimiser paramters

		final EPackage schedulePackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);
		final EPackage parametersPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ParametersModel);

		final EClass classScheduleModel = MetamodelUtils.getEClass(schedulePackage, "ScheduleModel");
		final EClass classUserSettings = MetamodelUtils.getEClass(parametersPackage, "UserSettings");

		final EEnum enumSimilarityMode = MetamodelUtils.getEEnum(parametersPackage, "SimilarityMode");
		final EEnumLiteral literalModeAll = MetamodelUtils.getEEnum_Literal(enumSimilarityMode, "ALL");
		final EEnumLiteral literalModeNone = MetamodelUtils.getEEnum_Literal(enumSimilarityMode, "OFF");

		final Consumer<EObjectWrapper> updateSettings = userSettings -> {
			if (userSettings != null) {
				userSettings.unsetFeature("buildActionSets");
				final Object mode = userSettings.getAttrib("similarityMode");
				if (mode != literalModeNone) {
					userSettings.setAttrib("similarityMode", literalModeAll);
				}
			}
		};
		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();
		{
			final EObjectWrapper userSettings = scenarioModel.getRef("userSettings");
			updateSettings.accept(userSettings);
		}
		{
			final EObjectWrapper analyticsModel = scenarioModel.getRef("analyticsModel");

			final TreeIterator<EObject> itr = analyticsModel.eAllContents();
			while (itr.hasNext()) {
				final EObject obj = itr.next();
				if (classScheduleModel.isInstance(obj)) {
					itr.prune();
				}
				if (classUserSettings.isInstance(obj)) {
					updateSettings.accept((EObjectWrapper) obj);
					itr.prune();
				}
			}
		}

		// Section 2 - clean up old panama canal bookings fields
		EObjectWrapper cargoModel = scenarioModel.getRef("cargoModel");
		EObjectWrapper canalBookings = cargoModel.getRef("canalBookings");

		if (canalBookings != null) {
			canalBookings.unsetFeature("strictBoundaryOffsetDays");
			canalBookings.unsetFeature("relaxedBoundaryOffsetDays");
			canalBookings.unsetFeature("flexibleBookingAmountNorthbound");
			canalBookings.unsetFeature("flexibleBookingAmountSouthbound");
			canalBookings.unsetFeature("bookingExemptVessels");

			List<EObjectWrapper> bookings = canalBookings.getRefAsList("canalBookingSlots");
			if (bookings != null) {
				for (EObjectWrapper booking : bookings) {
					booking.unsetFeature("slot");
				}
			}
		}
	}
}