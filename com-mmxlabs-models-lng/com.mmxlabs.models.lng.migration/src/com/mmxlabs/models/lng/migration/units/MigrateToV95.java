/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;

import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV95 extends AbstractMigrationUnit {

	private static final String ATTRIB_PORT = "port";
	private static final String ATTRIB_VIRTUAL_PORT = "virtualPort";
	private static final String ATTRIB_DISTANCE = "distance";

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 94;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 95;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {

		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();

		final EObjectWrapper referenceModel = scenarioModel.getRef("referenceModel");
		final EObjectWrapper portModel = referenceModel.getRef("portModel");

		// Ensure Suez and Panama ports exist.
		EObjectWrapper panamaNorth = null;
		EObjectWrapper panamaSouth = null;
		EObjectWrapper panamaVirtual = null;
		EObjectWrapper suezNorth = null;
		EObjectWrapper suezSouth = null;
		EObjectWrapper suezVirtual = null;
		final List<EObjectWrapper> ports = portModel.getRefAsList("ports");
		for (final EObjectWrapper port : ports) {
			final String mmxId = port.getRef("location").getAttrib("mmxId");
			if(mmxId != null) {
				switch (mmxId) {
				case "L_CP_PortS":
					suezNorth = port;
					break;
				case "L_CP_Suez":
					suezSouth = port;
					break;
				case "L_V_SuezC":
					suezVirtual = port;
					break;
				case "L_CP_Colon":
					panamaNorth = port;
					break;
				case "L_CP_Balbo":
					panamaSouth = port;
					break;
				case "L_V_Panam":
					panamaVirtual = port;
					break;
				}
			}
		}

		final EPackage pkg_PortPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PortModel);
		final EEnum eenum_RouteOption = MetamodelUtils.getEEnum(pkg_PortPackage, "RouteOption");
		final EEnumLiteral eenum_RouteOption_SUEZ = MetamodelUtils.getEEnum_Literal(eenum_RouteOption, "SUEZ");
		final EEnumLiteral eenum_RouteOption_PANAMA = MetamodelUtils.getEEnum_Literal(eenum_RouteOption, "PANAMA");

		// Ensure entry points and distances exists

		final List<EObjectWrapper> routes = portModel.getRefAsList("routes");
		for (final EObjectWrapper route : routes) {
			final Object routeOption = route.getAttrib("routeOption");
			final EObjectWrapper northEntrance = route.getRef("northEntrance");
			final EObjectWrapper southEntrance = route.getRef("southEntrance");

			if (routeOption == eenum_RouteOption_SUEZ) {
				if (route.getAttribAsDouble(ATTRIB_DISTANCE) == 0.0) {
					route.setAttrib(ATTRIB_DISTANCE, 102.279);
				}
				if (northEntrance != null && northEntrance.getRef(ATTRIB_PORT) == null) {
					northEntrance.setRef(ATTRIB_PORT, suezNorth);
				}
				if (southEntrance != null && southEntrance.getRef(ATTRIB_PORT) == null) {
					southEntrance.setRef(ATTRIB_PORT, suezSouth);
				}
				if (route.getRef(ATTRIB_VIRTUAL_PORT) == null) {
					route.setRef(ATTRIB_VIRTUAL_PORT, suezVirtual);
				}
			} else if (routeOption == eenum_RouteOption_PANAMA) {
				if (route.getAttribAsDouble(ATTRIB_DISTANCE) == 0.0) {
					route.setAttrib(ATTRIB_DISTANCE, 39.97);
				}
				if (northEntrance != null && northEntrance.getRef(ATTRIB_PORT) == null) {
					northEntrance.setRef(ATTRIB_PORT, panamaNorth);
				}
				if (southEntrance != null && southEntrance.getRef(ATTRIB_PORT) == null) {
					southEntrance.setRef(ATTRIB_PORT, panamaSouth);
				}
				if (route.getRef(ATTRIB_VIRTUAL_PORT) == null) {
					route.setRef(ATTRIB_VIRTUAL_PORT, panamaVirtual);
				}
			}
		}
	}
}
