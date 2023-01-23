/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.time.YearMonth;
import java.util.List;

import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;
import com.mmxlabs.models.migration.MigrationModelRecord;

public class MigrateToV77 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 76;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 77;
	}

	@Override
	protected void doMigration(@NonNull MigrationModelRecord modelRecord) {

		final MetamodelLoader loader = modelRecord.getMetamodelLoader();
		final EObjectWrapper model = modelRecord.getModelRoot();

		final EObjectWrapper obj = model.getRef("userSettings");
		if (obj != null) {
			final YearMonth old = (YearMonth) obj.getAttrib("periodStart");
			if (old != null) {
				obj.setAttrib("periodStartDate", old.atDay(1));
			}
			obj.unsetFeature("periodStart");
		}

		// check structure
		final EObjectWrapper cargoModel = model.getRef("cargoModel");
		if (cargoModel != null) {
			final EObjectWrapper canalBookings = cargoModel.getRef("canalBookings");
			if (canalBookings != null) {
				final int flex = canalBookings.getAttrib("flexibleBookingAmount");
				canalBookings.unsetFeature("flexibleBookingAmount");

				canalBookings.setAttrib("flexibleBookingAmountNorthbound", flex);
				canalBookings.setAttrib("flexibleBookingAmountSouthbound", flex);
			}
		}

		final EPackage portPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PortModel);
		final EEnum enum_RouteOption = MetamodelUtils.getEEnum(portPackage, "RouteOption");
		final EEnumLiteral enum_RouteOption_SUEZ = MetamodelUtils.getEEnum_Literal(enum_RouteOption, "SUEZ");
		final EEnumLiteral enum_RouteOption_PANAMA = MetamodelUtils.getEEnum_Literal(enum_RouteOption, "PANAMA");

		final EObjectWrapper referenceModel = model.getRef("referenceModel");
		final EObjectWrapper portModel = referenceModel.getRef("portModel");
		if (portModel != null) {
			final List<EObjectWrapper> routes = portModel.getRefAsList("routes");
			if (routes != null) {
				for (final EObjectWrapper route : routes) {
					final EObjectWrapper entryA = route.getRef("entryA");
					final EObjectWrapper entryB = route.getRef("entryB");
					route.unsetFeature("entryA");
					route.unsetFeature("entryB");

					EObjectWrapper northEntrance = null;
					EObjectWrapper southEntrance = null;

					if (route.getAttrib("routeOption") == enum_RouteOption_SUEZ) {
						if (isSuezNorth(entryA)) {
							northEntrance = entryA;
						} else if (isSuezSouth(entryA)) {
							southEntrance = entryA;
						}

						if (isSuezNorth(entryB)) {
							northEntrance = entryB;
						} else if (isSuezSouth(entryB)) {
							southEntrance = entryB;
						}
					} else if (route.getAttrib("routeOption") == enum_RouteOption_PANAMA) {
						if (isPanamaNorth(entryA)) {
							northEntrance = entryA;
						} else if (isPanamaSouth(entryA)) {
							southEntrance = entryA;
						}

						if (isPanamaNorth(entryB)) {
							northEntrance = entryB;
						} else if (isPanamaSouth(entryB)) {
							southEntrance = entryB;
						}
					} else {
						continue;
					}
					route.setRef("northEntrance", northEntrance);
					route.setRef("southEntrance", southEntrance);

					assert entryA == null || (entryA == northEntrance || entryA == southEntrance);
					assert entryB == null || (entryB == northEntrance || entryB == southEntrance);
				}
			}
		}

	}

	private boolean isSuezNorth(final EObjectWrapper entryPoint) {
		if (entryPoint != null) {
			final String name = entryPoint.getAttrib("name");
			if (name != null) {
				if (name.toLowerCase().endsWith("north")) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isSuezSouth(final EObjectWrapper entryPoint) {
		if (entryPoint != null) {
			final String name = entryPoint.getAttrib("name");
			if (name != null) {
				if (name.toLowerCase().endsWith("south")) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isPanamaNorth(final EObjectWrapper entryPoint) {
		if (entryPoint != null) {

			EObjectWrapper port = entryPoint.getRef("port");
			if (port != null) {
				return port.<String> getAttrib("name").toLowerCase().contains("colon");
			}

			final String name = entryPoint.getAttrib("name");
			if (name != null) {
				if (name.toLowerCase().endsWith("north")) {
					return true;
				}
				if (name.toLowerCase().endsWith("northside")) {
					return true;
				}
				if (name.toLowerCase().endsWith("east")) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isPanamaSouth(final EObjectWrapper entryPoint) {
		if (entryPoint != null) {

			EObjectWrapper port = entryPoint.getRef("port");
			if (port != null) {
				return port.<String> getAttrib("name").toLowerCase().contains("balboa");
			}

			final String name = entryPoint.getAttrib("name");
			if (name != null) {
				if (name.toLowerCase().endsWith("south")) {
					return true;
				}
				if (name.toLowerCase().endsWith("southside")) {
					return true;
				}
				if (name.toLowerCase().endsWith("west")) {
					return true;
				}
			}
		}
		return false;
	}
}
