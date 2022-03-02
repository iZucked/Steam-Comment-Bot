/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.time.Instant;
import java.util.UUID;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.hub.services.users.UsernameProvider;
import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV104 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 103;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 104;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		final EObjectWrapper modelRoot = modelRecord.getModelRoot();
		final EObjectWrapper referenceModel = modelRoot.getRef("referenceModel");
		final EObjectWrapper pricingModel = referenceModel.getRef("pricingModel");
		final EObjectWrapper fleetModel = referenceModel.getRef("fleetModel");
		final EObjectWrapper portModel = referenceModel.getRef("portModel");

		migrate(modelRecord, pricingModel, "marketCurveDataVersion", "marketCurvesVersionRecord");
		migrate(modelRecord, pricingModel, "settledPricesDataVersion", "settledPricesVersionRecord");

		migrate(modelRecord, portModel, "portDataVersion", "portVersionRecord");
		migrate(modelRecord, portModel, "portGroupDataVersion", "portGroupVersionRecord");
		migrate(modelRecord, portModel, "distanceDataVersion", "distanceVersionRecord");

		migrate(modelRecord, fleetModel, "fleetDataVersion", "fleetVersionRecord");
		migrate(modelRecord, fleetModel, "vesselGroupDataVersion", "vesselGroupVersionRecord");
		migrate(modelRecord, fleetModel, "bunkerFuelsDataVersion", "bunkerFuelsVersionRecord");
	}

	private void migrate(@NonNull final MigrationModelRecord modelRecord, final EObjectWrapper owner, final String oldFeature, final String newFeature) {

		final EPackage pkgMMXCorePackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_MMXCore);
		final EClass classVersionRecord = MetamodelUtils.getEClass(pkgMMXCorePackage, "VersionRecord");

		final EObjectWrapper record = (EObjectWrapper) pkgMMXCorePackage.getEFactoryInstance().create(classVersionRecord);
		record.setAttrib("createdAt", Instant.now());
		record.setAttrib("createdBy", UsernameProvider.INSTANCE.getUserID());

		if (owner.isSetFeature(oldFeature)) {
			final String version = owner.getAttrib(oldFeature);
			record.setAttrib("version", version);
			owner.unsetFeature(oldFeature);
		} else {
			record.setAttrib("version", UUID.randomUUID().toString());
		}

		owner.setRef(newFeature, record);
	}
}
