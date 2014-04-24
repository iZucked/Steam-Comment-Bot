/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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

	/**
	 * Enum representing each of the models in Models LNG.
	 * 
	 */
	public enum ModelsLNGSet_v1 {
		/**
		 * @since 4.0
		 */
		Scenario, Analytics, Cargo, Commercial, Fleet,
		/**
		 * @since 4.0
		 */
		Assignment,
		/**
		 * @since 4.0
		 */
		Parameters, Port, Pricing, Schedule, SpotMarkets
	}

	public static ModelsLNGSet_v1 getTypeFromNS(final String nsURI) {
		if (ModelsLNGMigrationConstants.NSURI_ScenarioModel.equals(nsURI)) {
			return ModelsLNGSet_v1.Scenario;
		} else if (ModelsLNGMigrationConstants.NSURI_AnalyticsModel.equals(nsURI)) {
			return ModelsLNGSet_v1.Analytics;
		} else if (ModelsLNGMigrationConstants.NSURI_CargoModel.equals(nsURI)) {
			return ModelsLNGSet_v1.Cargo;
		} else if (ModelsLNGMigrationConstants.NSURI_CommercialModel.equals(nsURI)) {
			return ModelsLNGSet_v1.Commercial;
		} else if (ModelsLNGMigrationConstants.NSURI_FleetModel.equals(nsURI)) {
			return ModelsLNGSet_v1.Fleet;
		} else if (ModelsLNGMigrationConstants.NSURI_AssignmentModel.equals(nsURI)) {
			return ModelsLNGSet_v1.Assignment;
		} else if (ModelsLNGMigrationConstants.NSURI_ParametersModel.equals(nsURI)) {
			return ModelsLNGSet_v1.Parameters;
		} else if (ModelsLNGMigrationConstants.NSURI_PortModel.equals(nsURI)) {
			return ModelsLNGSet_v1.Port;
		} else if (ModelsLNGMigrationConstants.NSURI_PricingModel.equals(nsURI)) {
			return ModelsLNGSet_v1.Pricing;
		} else if (ModelsLNGMigrationConstants.NSURI_ScheduleModel.equals(nsURI)) {
			return ModelsLNGSet_v1.Schedule;
		} else if (ModelsLNGMigrationConstants.NSURI_SpotMarketsModel.equals(nsURI)) {
			return ModelsLNGSet_v1.SpotMarkets;
		}
		return null;
	}

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

	/**
	 * @since 5.0
	 */
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
	 * @since 5.0
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
	 * @since 5.0
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
	 * @since 5.0
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
	 * @since 5.0
	 * @param extraPackages
	 * @return
	 */
	public static MetamodelLoader createV4Loader(final Map<URI, PackageData> extraPackages) {
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v1.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v4.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v4.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v4.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v4.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v4.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v4.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v4.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.migration/model/assignment-v4.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AssignmentModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v4.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v4.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v4.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v4.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	/**
	 * @since 7.0
	 * @param extraPackages
	 * @return
	 */
	public static MetamodelLoader createV5Loader(final Map<URI, PackageData> extraPackages) {
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v1.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v5.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v5.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v5.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v5.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v5.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v5.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v5.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.migration/model/assignment-v5.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AssignmentModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v5.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v5.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v5.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v5.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	/**
	 * @since 8.0
	 */
	public static MetamodelLoader createV6Loader(final Map<URI, PackageData> extraPackages) {
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v6.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v6.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v6.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v6.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v6.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v6.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v6.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.migration/model/assignment-v6.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AssignmentModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v6.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v6.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v6.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v6.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV7Loader(final Map<URI, PackageData> extraPackages) {
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v7.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v7.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v7.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v7.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v7.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v7.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v7.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v7.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v7.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v7.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v7.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV8Loader(final Map<URI, PackageData> extraPackages) {
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v8.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v8.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v8.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v8.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v8.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v8.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v8.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v8-inter.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v8.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v8.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v8.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV9Loader(final Map<URI, PackageData> extraPackages) {
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v9.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v9.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v9.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v9.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v9.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v9.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v9.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v9-inter.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v9.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v9.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v9.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV10Loader(final Map<URI, PackageData> extraPackages) {
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v10.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v10.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v10.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v10.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v10.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v10.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v10.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v10.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v10.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v10.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v10.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV11Loader(final Map<URI, PackageData> extraPackages) {
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v11.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v11.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v11.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v11.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v11.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v11.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v11.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v11.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v11.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v11.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v11.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV12Loader(final Map<URI, PackageData> extraPackages) {
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v12.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v12.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v12.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v12.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v12.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v12.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v12.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v12.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v12.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v12.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v12.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV13Loader(final Map<URI, PackageData> extraPackages) {
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v13.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v13.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v13.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v13.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v13.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v13.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v13.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v13.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v13.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v13.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v13.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV14Loader(final Map<URI, PackageData> extraPackages) {
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v14.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v14.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v14.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v14.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v14.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v14.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v14.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v14.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v14.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v14.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v14.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV15Loader(final Map<URI, PackageData> extraPackages) {
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v15.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v15.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v15.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v15.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v15.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v15.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v15.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v15.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v15.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v15.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v15.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV16Loader(final Map<URI, PackageData> extraPackages) {
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v16.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v16.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v16.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v16.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v16.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v16.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v16.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v16.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v16.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v16.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v16.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}
	
	public static MetamodelLoader createV17Loader(final Map<URI, PackageData> extraPackages) {

		return createCurrentLoader(extraPackages);
	}

	/**
	 * @since 4.0
	 */
	public static MetamodelLoader createCurrentLoader(final Map<URI, PackageData> extraPackages) {

		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
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

		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());
		return loader;
	}
}
