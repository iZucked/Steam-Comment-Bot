/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
		 */
		Scenario, Analytics, Cargo, Commercial, Fleet,
		/**
		 */
		Assignment,
		/**
		 */
		Parameters, Port, Pricing, Schedule, SpotMarkets, Actuals
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
		} else if (ModelsLNGMigrationConstants.NSURI_ActualsModel.equals(nsURI)) {
			return ModelsLNGSet_v1.Actuals;
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
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v17.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v17.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v17.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v17.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v17.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v17.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v17.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v17.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v17.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v17.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v17.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV18Loader(final Map<URI, PackageData> extraPackages) {
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v18.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v18.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v18.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v18.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v18.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v18.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v18.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v18.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v18.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v18.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v18.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV19Loader(final Map<URI, PackageData> extraPackages) {
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v19.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v19.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v18.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v18.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v19.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v19.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v19.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v19.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v19.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v19.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v19.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV20Loader(final Map<URI, PackageData> extraPackages) {
		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v20.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v20.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v20.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v20.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v20.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v20.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v20.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v20.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v20.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v20.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v20.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV21Loader(final Map<URI, PackageData> extraPackages) {

		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v21.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v21.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v21.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v21.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v21.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v21.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v21.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v21.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v21.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v21.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v21.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV22Loader(final Map<URI, PackageData> extraPackages) {

		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v22.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v22.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v22.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v22.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v22.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v22.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v22.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v22.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v22.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v22.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v22.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.actuals/model/actuals-v22.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ActualsModel);

		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV23Loader(final Map<URI, PackageData> extraPackages) {

		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v23.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v23.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v23.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v23.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v23.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v23.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v23.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v23.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v23.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v23.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v23.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.actuals/model/actuals-v23.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ActualsModel);

		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV24Loader(final Map<URI, PackageData> extraPackages) {

		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v24.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v24.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v24.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v24.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v24.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v24.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v24.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v24.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v24.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v24.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v24.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.actuals/model/actuals-v24.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ActualsModel);

		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV25Loader(final Map<URI, PackageData> extraPackages) {

		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v25.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v25.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v25.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v25.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v25.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v25.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v25.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v25.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v25.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v25.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v25.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.actuals/model/actuals-v25.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ActualsModel);

		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV26Loader(final Map<URI, PackageData> extraPackages) {

		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v26.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v26.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v26.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v26.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v26.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v26.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v26.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v26.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v26.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v26.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v26.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.actuals/model/actuals-v26.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ActualsModel);

		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV27Loader(final Map<URI, PackageData> extraPackages) {

		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v27.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v27.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v27.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v27.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v27.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v27.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v27.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v27.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v27.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v27.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v27.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.actuals/model/actuals-v27.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ActualsModel);

		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV28Loader(final Map<URI, PackageData> extraPackages) {

		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v28.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v28.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v28.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v28.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v28.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v28.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v28.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v28.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v28.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v28.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v28.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.actuals/model/actuals-v28.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ActualsModel);

		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV29Loader(final Map<URI, PackageData> extraPackages) {

		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v29.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v29.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v29.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v29.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v29.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v29.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v29.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v29.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v29.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v29.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v29.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.actuals/model/actuals-v29.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ActualsModel);

		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());

		return loader;
	}

	public static MetamodelLoader createV30Loader(final Map<URI, PackageData> extraPackages) {
		
		final MetamodelLoader loader = new MetamodelLoader();
		
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v30.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v30.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v30.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v30.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v30.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v30.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v30.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v30.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v30.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v30.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v30.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.actuals/model/actuals-v30.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ActualsModel);
		
		if (extraPackages != null) {
			for (final Map.Entry<URI, PackageData> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getKey(), e.getValue());
			}
		}
		EcoreUtil.resolveAll(loader.getResourceSet());
		
		return loader;
	}
	
	public static MetamodelLoader createV31Loader(final Map<URI, PackageData> extraPackages) {
		
		final MetamodelLoader loader = new MetamodelLoader();
		
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore-v2.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes-v31.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v31.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v31.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v31.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v31.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v31.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_SpotMarketsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v31.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v31.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v31.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.parameters/model/parameters-v31.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ParametersModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.scenario.model/model/scenario-v31.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ScenarioModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.actuals/model/actuals-v31.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_ActualsModel);
		
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

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.datetime/model/datetime-v1.ecore", true), ModelsLNGMigrationConstants.PKG_DATA_DateTime);
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

	public static MetamodelLoader createVNLoaderTemplate22Onwards(final int n, final Map<URI, PackageData> extraPackages) {

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

	public static MetamodelLoader createVNLoader(final int version, final Map<URI, PackageData> extraPackages) {
		if (version < 6) {
			return MetamodelVersionsUtil.createVNLoaderTemplate0to5(version, extraPackages);
		} else if (version < 22) {
			return MetamodelVersionsUtil.createVNLoaderTemplate6to21(version, extraPackages);
		} else {
			return createVNLoaderTemplate22Onwards(version, extraPackages);
		}
	}
}
