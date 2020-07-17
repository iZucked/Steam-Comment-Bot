/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV129 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 128;
	}
	@Override
	public int getScenarioDestinationVersion() {
		return 129;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		
		final EObjectWrapper modelRoot = modelRecord.getModelRoot();

		final EObjectWrapper portModel = modelRoot.getRef("referenceModel").getRef("portModel");

		final List<EObjectWrapper> routes = portModel.getRefAsList("routes");
		// Clear virtual port
		if (routes != null) {
			routes.forEach(route -> route.unsetFeature("virtualPort"));
		}

		// Clear upstream id data model
		final List<EObjectWrapper> ports = portModel.getRefAsList("ports");
		if (ports != null) {
			final List<EObjectWrapper> virtualPorts = new LinkedList<>();
			for (final EObjectWrapper port : ports) {
				final EObjectWrapper location = port.getRef("location");
				if (location != null) {
					final String mmxid = location.getAttrib("mmxId");
					if ("L_V_Panam".equals(mmxid) || "L_V_SuezC".equals(mmxid)) {
						// Virtual port - remove it.
						virtualPorts.add(port);
					} else {
						// Clear upstream id data model
						location.unsetFeature("otherIdentifiers");
					}
				}
			}
		}
		
		final EPackage adpModelPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ADPModel);
		final EClass preDefinedDistributionModel = MetamodelUtils.getEClass(adpModelPackage, "PreDefinedDistributionModel");
		
		final EObjectWrapper adpModel = modelRoot.getRef("adpModel");
		if (adpModel == null)
			return;
		
		updateContractProfiles(preDefinedDistributionModel, adpModel.getRefAsList("purchaseContractProfiles"));
		updateContractProfiles(preDefinedDistributionModel, adpModel.getRefAsList("salesContractProfiles"));
		
		adpModel.eAllContents().forEachRemaining(obj -> {
			if (preDefinedDistributionModel.isInstance(obj)) {
				((EObjectWrapper) obj).unsetFeature("windowSize");
				((EObjectWrapper) obj).unsetFeature("windowSizeUnits");
			}
		});
	}

	private void updateContractProfiles(final EClass preDefinedDistributionModel, final List<EObjectWrapper> contractProfiles) {
		if (contractProfiles != null && !contractProfiles.isEmpty()) {
			contractProfiles.forEach(contractProfile -> {

				final List<EObjectWrapper> subProfiles = contractProfile.getRefAsList("subProfiles");
				if (subProfiles != null && !subProfiles.isEmpty()) {
					//grab distribution model and check that it's an instance of predefined distribution model
					subProfiles.forEach(subProfile ->{
						final EObjectWrapper distributionModel = subProfile.getRef("distributionModel");
						if (preDefinedDistributionModel.isInstance(distributionModel)) {
							subProfile.setAttrib("windowSize", distributionModel.getAttrib("windowSize"));
							subProfile.setAttrib("windowSizeUnits", distributionModel.getAttrib("windowSizeUnits"));

							distributionModel.unsetFeature("windowSize");
							distributionModel.unsetFeature("windowSizeUnits");
						}
					});
				}

			});
		}
	}
} 