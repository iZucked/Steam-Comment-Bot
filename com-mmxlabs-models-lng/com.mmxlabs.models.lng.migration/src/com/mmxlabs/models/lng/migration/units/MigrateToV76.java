/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
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

public class MigrateToV76 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 75;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 76;
	}

	@Override
	protected void doMigration(final MigrationModelRecord modelRecord) {
		final MetamodelLoader loader = modelRecord.getMetamodelLoader();
		final EObjectWrapper model = modelRecord.getModelRoot();

		final EObjectWrapper referenceModel = model.getRef("referenceModel");
		final EObjectWrapper portModel = referenceModel.getRef("portModel");
		if (portModel == null) {
			return;
		}

		EPackage portPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PortModel);
		EEnum enum_RouteOption = MetamodelUtils.getEEnum(portPackage, "RouteOption");
		EEnumLiteral enum_RouteOption_SUEZ = MetamodelUtils.getEEnum_Literal(enum_RouteOption, "SUEZ");
		EEnumLiteral enum_RouteOption_PANAMA = MetamodelUtils.getEEnum_Literal(enum_RouteOption, "PANAMA");

		EClass class_EntryPoint = MetamodelUtils.getEClass(portPackage, "EntryPoint");

		List<EObjectWrapper> routes = portModel.getRefAsList("routes");
		if (routes != null) {
			for (EObjectWrapper route : routes) {

				if (!route.isSetFeature("entryA")) {
					EObjectWrapper entryPoint = (EObjectWrapper) portPackage.getEFactoryInstance().create(class_EntryPoint);
					if (route.getAttrib("routeOption") == enum_RouteOption_SUEZ) {
						entryPoint.setAttrib("name", "Suez North");
						route.setRef("entryA", entryPoint);
					} else if (route.getAttrib("routeOption") == enum_RouteOption_PANAMA) {
						entryPoint.setAttrib("name", "Panama Northside");
						route.setRef("entryA", entryPoint);
					}
				}
				if (!route.isSetFeature("entryB")) {
					EObjectWrapper entryPoint = (EObjectWrapper) portPackage.getEFactoryInstance().create(class_EntryPoint);
					if (route.getAttrib("routeOption") == enum_RouteOption_SUEZ) {
						entryPoint.setAttrib("name", "Suez South");
						route.setRef("entryB", entryPoint);
					} else if (route.getAttrib("routeOption") == enum_RouteOption_PANAMA) {
						entryPoint.setAttrib("name", "Panama Southside");
						route.setRef("entryB", entryPoint);
					}
				}

			}
		}
	}
}
