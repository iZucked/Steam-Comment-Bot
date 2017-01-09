/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV53 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 52;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 53;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {
		// Part 1 - just here to get rid of obsolete model fields

		// Fixup migration -> We may have excess, fixed panama costs imported, remove them.

		final EPackage portPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PortModel);
		final EEnum enum_RouteOption = MetamodelUtils.getEEnum(portPackage, "RouteOption");

		final EEnumLiteral enum_RouteOption_Panama = MetamodelUtils.getEEnum_Literal(enum_RouteOption, "PANAMA");

		final EObjectWrapper referenceModel = model.getRef("referenceModel");
		if (referenceModel == null) {
			return;
		}

		final EObjectWrapper costModel = referenceModel.getRef("costModel");
		if (costModel == null) {
			return;
		}

		final List<EObjectWrapper> routeCosts = costModel.getRefAsList("routeCosts");
		if (routeCosts != null) {
			final List<EObjectWrapper> toRemove = new LinkedList<>();
			for (final EObjectWrapper routeCost : routeCosts) {
				final EObjectWrapper route = routeCost.getRef("route");
				if (enum_RouteOption_Panama == route.getAttrib("routeOption")) {
					toRemove.add(routeCost);
				}
			}

			routeCosts.removeAll(toRemove);
		}
	}
}
