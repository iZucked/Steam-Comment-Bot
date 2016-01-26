/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
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
public class MigrateToV9 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 8;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 9;
	}

	@Override
	protected MetamodelLoader getSourceMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.createV8Loader(extraPackages);
		}
		return sourceLoader;
	}

	@Override
	protected MetamodelLoader getDestinationMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (destinationLoader == null) {
			destinationLoader = MetamodelVersionsUtil.createV9Loader(extraPackages);
		}
		return destinationLoader;
	}

	@Override
	protected void doMigration(final EObject model) {
		// This should get the cached loader instance
		final MetamodelLoader loader = getDestinationMetamodelLoader(null);

		migrateScenarioFleetModel(loader, model);
	}

	protected void migrateScenarioFleetModel(MetamodelLoader loader, EObject model) {

		// final EPackage mmxCorePackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_MMXCore);

		final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
		final EClass class_LNGPortfolioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGPortfolioModel");
		final EReference reference_LNGScenarioModel_portfolioModel = MetamodelUtils.getReference(class_LNGScenarioModel, "portfolioModel");

		final EReference reference_LNGPortfolioModel_scheduleModel = MetamodelUtils.getReference(class_LNGPortfolioModel, "scheduleModel");

		final EPackage package_ScheduleModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);

		final EClass class_Schedule = MetamodelUtils.getEClass(package_ScheduleModel, "Schedule");
		final EClass class_ScheduleModel = MetamodelUtils.getEClass(package_ScheduleModel, "ScheduleModel");
		final EClass class_Sequence = MetamodelUtils.getEClass(package_ScheduleModel, "Sequence");
		final EClass class_VesselEventVisit = MetamodelUtils.getEClass(package_ScheduleModel, "VesselEventVisit");

		final EReference reference_ScheduleModule_schedule = MetamodelUtils.getReference(class_ScheduleModel, "schedule");
		final EReference reference_Schedule_sequences = MetamodelUtils.getReference(class_Schedule, "sequences");
		final EReference reference_Sequence_events = MetamodelUtils.getReference(class_Sequence, "events");

		final EReference reference_VesselEventVisit_vesselEvent = MetamodelUtils.getReference(class_VesselEventVisit, "vesselEvent");
		final EReference reference_VesselEventVisit_vesselEvent2 = MetamodelUtils.getReference(class_VesselEventVisit, "vesselEvent2");
		final EReference reference_Sequence_vesselAvailability = MetamodelUtils.getReference(class_Sequence, "vesselAvailability");
		final EReference reference_Sequence_vesselAvailability2 = MetamodelUtils.getReference(class_Sequence, "vesselAvailability2");

		EObject portfolioModel = (EObject) model.eGet(reference_LNGScenarioModel_portfolioModel);
		if (portfolioModel == null) {
			return;
		}

		EObject scheduleModel = (EObject) portfolioModel.eGet(reference_LNGPortfolioModel_scheduleModel);
		if (scheduleModel == null) {
			return;
		}
		EObject schedule = (EObject) scheduleModel.eGet(reference_ScheduleModule_schedule);
		if (schedule == null) {
			return;
		}

		List<EObject> sequences = MetamodelUtils.getValueAsTypedList(schedule, reference_Schedule_sequences);
		if (sequences != null) {
			for (EObject sequence : sequences) {
				EObject vesselAvailability = (EObject) sequence.eGet(reference_Sequence_vesselAvailability2);
				sequence.eUnset(reference_Sequence_vesselAvailability2);
				if (vesselAvailability != null) {
					sequence.eSet(reference_Sequence_vesselAvailability, vesselAvailability);
				}

				List<EObject> events = MetamodelUtils.getValueAsTypedList(sequence, reference_Sequence_events);
				if (events != null) {
					for (EObject event : events) {
						if (class_VesselEventVisit.isInstance(event)) {
							EObject vesselEvent = (EObject) event.eGet(reference_VesselEventVisit_vesselEvent2);
							event.eUnset(reference_VesselEventVisit_vesselEvent2);
							if (vesselEvent != null) {
								event.eSet(reference_VesselEventVisit_vesselEvent, vesselEvent);
							}
						}
					}
				}
			}
		}
	}
}
