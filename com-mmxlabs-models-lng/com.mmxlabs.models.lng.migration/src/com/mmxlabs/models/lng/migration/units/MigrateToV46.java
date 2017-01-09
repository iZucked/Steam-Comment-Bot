/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;

import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV46 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 45;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 46;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {

		final EPackage package_PortModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PortModel);
		final EEnum enum_RouteOption = MetamodelUtils.getEEnum(package_PortModel, "RouteOption");
		final EEnumLiteral enum_literal_RouteOption_DIRECT = MetamodelUtils.getEEnum_Literal(enum_RouteOption, "DIRECT");
		final EEnumLiteral enum_literal_RouteOption_SUEZ = MetamodelUtils.getEEnum_Literal(enum_RouteOption, "SUEZ");

		final EObjectWrapper referenceModel = model.getRef("referenceModel");
		if (referenceModel != null) {
			final EObjectWrapper portModel = referenceModel.getRef("portModel");
			if (portModel != null) {
				final List<EObjectWrapper> routes = portModel.getRefAsList("routes");
				if (routes != null) {
					for (final EObjectWrapper route : routes) {
						final Boolean isCanal = route.getAttribAsBooleanObject("canal");
						if (isCanal) {
							route.setAttrib("routeOption", enum_literal_RouteOption_SUEZ);
						} else {
							route.setAttrib("routeOption", enum_literal_RouteOption_DIRECT);
						}
					}
				}
			}
		}
	}
}
