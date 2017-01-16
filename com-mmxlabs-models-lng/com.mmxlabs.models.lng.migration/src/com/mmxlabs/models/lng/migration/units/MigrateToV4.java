/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

/**
 */
public class MigrateToV4 extends AbstractMigrationUnit {


	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 3;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 4;
	}

	@Override
	protected MetamodelLoader getSourceMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.createV3Loader(extraPackages);
		}
		return sourceLoader;
	}

	@Override
	protected MetamodelLoader getDestinationMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (destinationLoader == null) {
			destinationLoader = MetamodelVersionsUtil.createV4Loader(extraPackages);
		}
		return destinationLoader;
	}

	@Override
	protected void doMigration(final EObject model) {

		// This should get the cached loader instance
		final MetamodelLoader loader = getDestinationMetamodelLoader(null);

		migrateDailyHireRate(loader, model);
	}

	protected void migrateDailyHireRate(final MetamodelLoader loader, final EObject model) {
		final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
		final EClass class_LNGPortfolioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGPortfolioModel");

		final EPackage package_ScheduleModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);
		final EClass class_ScheduleModel = MetamodelUtils.getEClass(package_ScheduleModel, "ScheduleModel");
		final EClass class_Schedule = MetamodelUtils.getEClass(package_ScheduleModel, "Schedule");
		final EClass class_Sequence = MetamodelUtils.getEClass(package_ScheduleModel, "Sequence");

		final EReference reference_LNGScenarioModel_portfolioModel = MetamodelUtils.getReference(class_LNGScenarioModel, "portfolioModel");
		final EReference reference_LNGPortfolio_scheduleModel = MetamodelUtils.getReference(class_LNGPortfolioModel, "scheduleModel");

		final EReference reference_LNGScheduleModel_schedule = MetamodelUtils.getReference(class_ScheduleModel, "schedule");
		final EReference reference_Schedule_sequences = MetamodelUtils.getReference(class_Schedule, "sequences");
		final EAttribute attribute_Sequence_dailyHireRate = MetamodelUtils.getAttribute(class_Sequence, "dailyHireRate");

		if (model.eIsSet(reference_LNGScenarioModel_portfolioModel)) {

			final EObject portfolioModel = (EObject) model.eGet(reference_LNGScenarioModel_portfolioModel);
			if (portfolioModel != null) {

				final EObject scheduleModel = (EObject) portfolioModel.eGet(reference_LNGPortfolio_scheduleModel);
				if (scheduleModel != null) {
					if (scheduleModel.eIsSet(reference_LNGScheduleModel_schedule)) {

						final EObject schedule = (EObject) scheduleModel.eGet(reference_LNGScheduleModel_schedule);

						if (schedule != null) {

							final List<EObject> sequences = MetamodelUtils.getValueAsTypedList(schedule, reference_Schedule_sequences);
							if (sequences != null) {
								for (final EObject sequence : sequences) {
									sequence.eUnset(attribute_Sequence_dailyHireRate);
								}
							}
						}
					}
				}
			}
		}
	}
}
