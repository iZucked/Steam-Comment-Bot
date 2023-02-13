/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.models.migration.utils.MetamodelLoader;

/**
 * Utility class to load metamodels for each Models LNG Version
 * 
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author Simon Goodall
 * 
 */
public class MetamodelVersionsUtil {

	public static MetamodelLoader createV0Loader(final Map<URI, PackageData> extraPackages) {

		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v1.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes.ecore-v0", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v0.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v0.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v0.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v0.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v0.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v0.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v0.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.migration/model/assignment-v0.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AssignmentModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v0.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v0.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v0.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);

		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV1Loader(final Map<URI, PackageData> extraPackages) {

		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v1.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v1.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v1.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v1.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v1.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v1.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v1.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v1.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v1.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.migration/model/assignment-v1.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AssignmentModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v1.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v1.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v1.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);

		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV1_V2_IntermediateLoader(final Map<URI, PackageData> extraPackages) {

		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v1.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v1.inter.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v1.inter.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.migration/model/assignment-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AssignmentModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	/**
	 */
	public static MetamodelLoader createV2Loader(final Map<URI, PackageData> extraPackages) {
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v1.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.migration/model/assignment-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AssignmentModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	/**
	 */
	public static MetamodelLoader createV2_V3Loader(final Map<URI, PackageData> extraPackages) {
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v1.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v2.inter.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.migration/model/assignment-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AssignmentModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);

		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());
		return loader;
	}

	/**
	 * @param extraPackages
	 * @return
	 */
	public static MetamodelLoader createV3Loader(final Map<URI, PackageData> extraPackages) {
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v1.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.migration/model/assignment-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AssignmentModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	/**
	 */
	public static MetamodelLoader createCurrentLoader(final Map<URI, PackageData> extraPackages) {

		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.datetime/model/datetime-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_DateTime);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.migration/model/assignment.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AssignmentModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.actuals/model/actuals.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ActualsModel);

		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());
		return loader;
	}

	public static MetamodelLoader createVNLoaderTemplate0to5(final int n, final Map<URI, PackageData> extraPackages) {
		assert n >= 0 && n < 6;
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v1.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.migration/model/assignment-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_AssignmentModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.types/model/lngtypes-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.port/model/port-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.pricing/model/pricing-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.fleet/model/fleet-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.commercial/model/commercial-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v%d.ecore", n), true),
				ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.cargo/model/cargo-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.schedule/model/schedule-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.analytics/model/analytics-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.parameters/model/parameters-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.scenario.model/model/scenario-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);

		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());
		return loader;
	}

	public static MetamodelLoader createVNLoaderTemplate6to21(final int n, final Map<URI, PackageData> extraPackages) {
		assert n >= 6 && n < 22;
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);

		if (n == 6) {
			loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.migration/model/assignment-v%d.ecore", n), true),
					ModelsLNGMigrationConstants.PKG_DATA_AssignmentModel);
		}
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.types/model/lngtypes-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.port/model/port-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.pricing/model/pricing-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.fleet/model/fleet-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.commercial/model/commercial-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v%d.ecore", n), true),
				ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.cargo/model/cargo-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);

		if (n == 8) {
			loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v8-inter.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		} else if (n == 9) {
			loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v9-inter.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		} else {
			loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.schedule/model/schedule-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);

		}
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.analytics/model/analytics-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.parameters/model/parameters-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.scenario.model/model/scenario-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);

		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());
		return loader;
	}

	public static MetamodelLoader createVNLoaderTemplate22to63(final int n, final Map<URI, PackageData> extraPackages) {
		assert n >= 22 && n < 64;
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.datetime/model/datetime-v1.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_DateTime);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);

		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.types/model/lngtypes-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.port/model/port-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.pricing/model/pricing-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.fleet/model/fleet-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.commercial/model/commercial-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v%d.ecore", n), true),
				ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.cargo/model/cargo-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.schedule/model/schedule-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.analytics/model/analytics-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.parameters/model/parameters-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.scenario.model/model/scenario-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.actuals/model/actuals-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ActualsModel);

		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());
		return loader;
	}

	public static MetamodelLoader createVNLoaderTemplate64Onwards(final int n, final Map<URI, PackageData> extraPackages) {

		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.datetime/model/datetime-v1.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_DateTime);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);

		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.types/model/lngtypes-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.port/model/port-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.pricing/model/pricing-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.fleet/model/fleet-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.commercial/model/commercial-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v%d.ecore", n), true),
				ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.cargo/model/cargo-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.schedule/model/schedule-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.analytics/model/analytics-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.parameters/model/parameters-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.scenario.model/model/scenario-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.actuals/model/actuals-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ActualsModel);

		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());
		return loader;
	}

	public static MetamodelLoader createVNLoaderTemplate74Onwards(final int n, final Map<URI, PackageData> extraPackages) {

		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.datetime/model/datetime-v2.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_DateTime);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);

		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.types/model/lngtypes-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.port/model/port-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.pricing/model/pricing-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.fleet/model/fleet-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.commercial/model/commercial-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v%d.ecore", n), true),
				ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.cargo/model/cargo-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.schedule/model/schedule-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.analytics/model/analytics-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.parameters/model/parameters-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.scenario.model/model/scenario-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.actuals/model/actuals-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ActualsModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.adp/model/adp-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ADPModel);

		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());
		return loader;
	}

	public static MetamodelLoader createVNLoaderTemplate109Onwards(final int n, final Map<URI, PackageData> extraPackages) {

		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.datetime/model/datetime-v2.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_DateTime);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);

		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.types/model/lngtypes-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.port/model/port-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.pricing/model/pricing-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.fleet/model/fleet-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.commercial/model/commercial-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v%d.ecore", n), true),
				ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.cargo/model/cargo-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.schedule/model/schedule-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.analytics/model/analytics-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.parameters/model/parameters-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.scenario.model/model/scenario-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.actuals/model/actuals-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ActualsModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.adp/model/adp-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ADPModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.nominations/model/nominations-v%d.ecore", n), true),
				ModelsLNGMigrationConstants.PKG_DATA_NominationsModel);

		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());
		return loader;
	}
	
	public static MetamodelLoader createVNLoaderTemplate164Onwards(final int n, final Map<URI, PackageData> extraPackages) {
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.datetime/model/datetime-v2.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_DateTime);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v3.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);

		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.types/model/lngtypes-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.port/model/port-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.pricing/model/pricing-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.fleet/model/fleet-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.commercial/model/commercial-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v%d.ecore", n), true),
				ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.cargo/model/cargo-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.schedule/model/schedule-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.analytics/model/analytics-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.parameters/model/parameters-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.scenario.model/model/scenario-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.actuals/model/actuals-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ActualsModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.adp/model/adp-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_ADPModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.nominations/model/nominations-v%d.ecore", n), true),
				ModelsLNGMigrationConstants.PKG_DATA_NominationsModel);
		loader.loadEPackage(URI.createPlatformPluginURI(String.format("/com.mmxlabs.models.lng.transfers/model/transfers-v%d.ecore", n), true), ModelsLNGMigrationConstants.PKG_DATA_TransfersModel);
		
		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());
		return loader;
	}

	public static MetamodelLoader createVNLoader(final int version, final Map<URI, PackageData> extraPackages) {
		if (version < 6) {
			return MetamodelVersionsUtil.createVNLoaderTemplate0to5(version, extraPackages);
		} else if (version < 22) {
			return MetamodelVersionsUtil.createVNLoaderTemplate6to21(version, extraPackages);
		} else if (version < 64) {
			return MetamodelVersionsUtil.createVNLoaderTemplate22to63(version, extraPackages);
		} else if (version < 74) {
			return createVNLoaderTemplate64Onwards(version, extraPackages);
		} else if (version < 109) {
			return createVNLoaderTemplate74Onwards(version, extraPackages);
		} else if (version < 164){
			return createVNLoaderTemplate109Onwards(version, extraPackages);
		} else {
			return createVNLoaderTemplate164Onwards(version, extraPackages);
		}
	}
}
