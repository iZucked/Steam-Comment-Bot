/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV145 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 144;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 145;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		final @NonNull EObjectWrapper scenarioModel = modelRecord.getModelRoot();
		
		final EObjectWrapper cargoModel = scenarioModel.getRef("cargoModel");
		final EObjectWrapper analyticsModel = scenarioModel.getRef("analyticsModel");
		
		processVesselAvailabilities(cargoModel);
		clearUpAnalyticsModel(analyticsModel);
	}
	
	private void processVesselAvailabilities(final EObjectWrapper cargoModel) {
		final List<EObjectWrapper> vesselAvailabilities = cargoModel.getRefAsList("vesselAvailabilities");
		if (vesselAvailabilities != null) {
			for (final EObjectWrapper vesselAvailability : vesselAvailabilities) {
				vesselAvailability.unsetFeature("fleet");
			}
		}
	}
	
	private void clearUpAnalyticsModel(final EObjectWrapper analyticsModel) {
		if (analyticsModel != null) {
			final List<EObjectWrapper> optionModels = analyticsModel.getRefAsList("optionModels");
			if (optionModels != null) {
				for(final EObjectWrapper optionModel : optionModels) {
					clearUpAbstractSolutionSet(optionModel.getRef("results"));
				}
			}
			final List<EObjectWrapper> optimisations = analyticsModel.getRefAsList("optimisations");
			if (optimisations != null) {
				for(final EObjectWrapper optimisation : optimisations) {
					clearUpAbstractSolutionSet(optimisation);
				}
			}
		}
	}
	
	private void clearUpAbstractSolutionSet(final EObjectWrapper abstractSolutionSet) {
		if (abstractSolutionSet != null) {
			final List<EObjectWrapper> vesselAvailabilities = abstractSolutionSet.getRefAsList("extraVesselAvailabilities");
			if (vesselAvailabilities != null) {
				for (final EObjectWrapper vesselAvailability : vesselAvailabilities) {
					vesselAvailability.unsetFeature("fleet");
				}
			}
		}
	}
}