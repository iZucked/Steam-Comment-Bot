/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;
import java.util.function.Consumer;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV85 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 84;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 85;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		final MetamodelLoader loader = modelRecord.getMetamodelLoader();
		final EObjectWrapper model = modelRecord.getModelRoot();
		final EObjectWrapper referenceModel = model.getRef("referenceModel");
		if (referenceModel == null) {
			return;
		}
		final EObjectWrapper costModel = referenceModel.getRef("costModel");
		if (costModel == null) {
			return;
		}

		final List<EObjectWrapper> baseFuelCosts = costModel.getRefAsList("baseFuelCosts");
		if (baseFuelCosts != null) {
			baseFuelCosts.forEach(bfc -> {
				final EObjectWrapper index = bfc.getRef("index");
				bfc.unsetFeature("index");
				if (index != null) {
					bfc.setAttrib("expression", index.getAttrib("name"));
				}
			});
		}

		final Consumer<EObjectWrapper> exposuresMigrator = (schedule) -> {
			if (schedule != null) {
				final List<EObjectWrapper> slotAllocations = schedule.getRefAsList("slotAllocations");
				if (slotAllocations != null) {
					for (final EObjectWrapper slotAllocation : slotAllocations) {
						final List<EObjectWrapper> exposureDetails = slotAllocation.getRefAsList("exposures");
						if (exposureDetails != null) {
							for (final EObjectWrapper exposureDetail : exposureDetails) {
								final EObjectWrapper index = exposureDetail.getRef("index");
								if (index != null) {
									exposureDetail.unsetFeature("index");
									exposureDetail.setAttrib("indexName", index.getAttrib("name"));
								}
							}
						}
					}
				}
			}
		};
		{
			final EObjectWrapper scheduleModel = model.getRef("scheduleModel");
			final EObjectWrapper schedule = scheduleModel.getRef("schedule");
			exposuresMigrator.accept(schedule);
		}
		final EPackage package_ScheduleModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);

		final EClass class_ScheduleModel = MetamodelUtils.getEClass(package_ScheduleModel, "ScheduleModel");

		final EObjectWrapper analyticsModel = model.getRef("analyticsModel");
		final TreeIterator<EObject> itr = analyticsModel.eAllContents();
		while (itr.hasNext()) {
			final EObject obj = (EObject) itr.next();
			if (class_ScheduleModel.isInstance(obj)) {
				final EObjectWrapper schedule = ((EObjectWrapper) obj).getRef("schedule");
				exposuresMigrator.accept(schedule);
				itr.prune();
			}
		}
	}
}
