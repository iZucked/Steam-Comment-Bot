/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV35 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 34;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 35;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {
		final EObjectWrapper portfolioModel = model.getRef("portfolioModel");
		final EPackage package_ParametersModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ParametersModel);
		final EClass class_SimilaritySettings = MetamodelUtils.getEClass(package_ParametersModel, "SimilaritySettings");
		final EReference reference_SimilaritySettings_lowInterval = MetamodelUtils.getReference(class_SimilaritySettings, "lowInterval");
		final EReference reference_SimilaritySettings_medInterval = MetamodelUtils.getReference(class_SimilaritySettings, "medInterval");
		final EReference reference_SimilaritySettings_highInterval = MetamodelUtils.getReference(class_SimilaritySettings, "highInterval");
		final EAttribute attribute_SimilarityInterval_outOfBoundsWeight = MetamodelUtils.getAttribute(class_SimilaritySettings, "outOfBoundsWeight");

		if (portfolioModel != null) {
			final EObjectWrapper parameters = portfolioModel.getRef("parameters");
			if (parameters != null) {
				final EObjectWrapper oldSimilaritySettings = parameters.getRef("similaritySettings");
				if (oldSimilaritySettings != null) {
					List<EObjectWrapper> intervalsList = oldSimilaritySettings.getRefAsList("intervals");
					if (intervalsList != null) {
						EObject similaritySettings = createSimilaritySettings(loader);
						// may be empty
						if (intervalsList.size() != 0) {
							for (EObjectWrapper interval : intervalsList) {
								Integer threshold = interval.getAttrib("threshold");
								Integer weight = interval.getAttrib("weight");
								if (threshold != null) {
									switch ((int) threshold) {
									case 0:
										similaritySettings.eSet(reference_SimilaritySettings_lowInterval, createSimilarityInterval(loader, 8, weight));
										break;
									case 8:
										similaritySettings.eSet(reference_SimilaritySettings_medInterval, createSimilarityInterval(loader, 16, weight));
										break;
									case 16:
										similaritySettings.eSet(reference_SimilaritySettings_highInterval, createSimilarityInterval(loader, 30, weight));
										break;
									case 30:
										similaritySettings.eSet(attribute_SimilarityInterval_outOfBoundsWeight, weight);
										break;
									default:
										break;
									}
								}
							}
						} else {
							similaritySettings.eSet(reference_SimilaritySettings_lowInterval, createSimilarityInterval(loader, 8, 0));
							similaritySettings.eSet(reference_SimilaritySettings_medInterval, createSimilarityInterval(loader, 16, 0));
							similaritySettings.eSet(reference_SimilaritySettings_highInterval, createSimilarityInterval(loader, 30, 0));
							similaritySettings.eSet(attribute_SimilarityInterval_outOfBoundsWeight, 0);
						}

						parameters.setRef("similaritySettings", similaritySettings);
						// parameters.unsetFeature("similaritySettings");
					} else {
						parameters.unsetFeature("similaritySettings");
					}
				}
			}
		}
	}

	private EObject createSimilarityInterval(MetamodelLoader modelLoader, int threshold, int weight) {
		final EPackage package_ParametersModel = modelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ParametersModel);

		final EClass class_SimilarityInterval = MetamodelUtils.getEClass(package_ParametersModel, "SimilarityInterval");

		final EAttribute attribute_SimilarityInterval_threshold = MetamodelUtils.getAttribute(class_SimilarityInterval, "threshold");
		final EAttribute attribute_SimilarityInterval_weight = MetamodelUtils.getAttribute(class_SimilarityInterval, "weight");

		EObject newSimilarityInterval = package_ParametersModel.getEFactoryInstance().create(class_SimilarityInterval);

		newSimilarityInterval.eSet(attribute_SimilarityInterval_threshold, threshold);
		newSimilarityInterval.eSet(attribute_SimilarityInterval_weight, weight);

		return newSimilarityInterval;

	}

	private EObject createSimilaritySettings(MetamodelLoader modelLoader) {
		final EPackage package_ParametersModel = modelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ParametersModel);

		final EClass class_SimilaritySettings = MetamodelUtils.getEClass(package_ParametersModel, "SimilaritySettings");

		EObject newSimilaritySettings = package_ParametersModel.getEFactoryInstance().create(class_SimilaritySettings);

		return newSimilaritySettings;
	}

}
