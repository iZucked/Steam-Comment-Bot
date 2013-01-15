/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration;

import org.eclipse.emf.common.util.URI;

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
		Analytics, Cargo, Commercial, Fleet, Input, Optimiser, Port, Pricing, Schedule
	}

	public static ModelsLNGSet_v1 getTypeFromNS(final String nsURI) {
		if (ModelsLNGMigrationConstants.NSURI_AnalyticsModel.equals(nsURI)) {
			return ModelsLNGSet_v1.Analytics;
		} else if (ModelsLNGMigrationConstants.NSURI_CargoModel.equals(nsURI)) {
			return ModelsLNGSet_v1.Cargo;
		} else if (ModelsLNGMigrationConstants.NSURI_CommercialModel.equals(nsURI)) {
			return ModelsLNGSet_v1.Commercial;
		} else if (ModelsLNGMigrationConstants.NSURI_FleetModel.equals(nsURI)) {
			return ModelsLNGSet_v1.Fleet;
		} else if (ModelsLNGMigrationConstants.NSURI_InputModel.equals(nsURI)) {
			return ModelsLNGSet_v1.Input;
		} else if (ModelsLNGMigrationConstants.NSURI_OptimiserModel.equals(nsURI)) {
			return ModelsLNGSet_v1.Optimiser;
		} else if (ModelsLNGMigrationConstants.NSURI_PortModel.equals(nsURI)) {
			return ModelsLNGSet_v1.Port;
		} else if (ModelsLNGMigrationConstants.NSURI_PricingModel.equals(nsURI)) {
			return ModelsLNGSet_v1.Pricing;
		} else if (ModelsLNGMigrationConstants.NSURI_ScheduleModel.equals(nsURI)) {
			return ModelsLNGSet_v1.Schedule;
		}
		return null;
	}

	public static MetamodelLoader getV0Loader() {

		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore.ecore", true), ModelsLNGMigrationConstants.NSURI_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes.ecore", true), ModelsLNGMigrationConstants.NSURI_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v0.ecore", true), ModelsLNGMigrationConstants.NSURI_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v0.ecore", true), ModelsLNGMigrationConstants.NSURI_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v0.ecore", true), ModelsLNGMigrationConstants.NSURI_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v0.ecore", true), ModelsLNGMigrationConstants.NSURI_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.input/model/input-v0.ecore", true), ModelsLNGMigrationConstants.NSURI_InputModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.optimiser/model/optimiser-v0.ecore", true), ModelsLNGMigrationConstants.NSURI_OptimiserModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v0.ecore", true), ModelsLNGMigrationConstants.NSURI_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v0.ecore", true), ModelsLNGMigrationConstants.NSURI_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v0.ecore", true), ModelsLNGMigrationConstants.NSURI_ScheduleModel);

		return loader;
	}

	public static MetamodelLoader getV1Loader() {

		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore.ecore", true), ModelsLNGMigrationConstants.NSURI_MMXCore);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes.ecore", true), ModelsLNGMigrationConstants.NSURI_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics.ecore", true), ModelsLNGMigrationConstants.NSURI_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo.ecore", true), ModelsLNGMigrationConstants.NSURI_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial.ecore", true), ModelsLNGMigrationConstants.NSURI_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet.ecore", true), ModelsLNGMigrationConstants.NSURI_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.input/model/input.ecore", true), ModelsLNGMigrationConstants.NSURI_InputModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.optimiser/model/optimiser.ecore", true), ModelsLNGMigrationConstants.NSURI_OptimiserModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port.ecore", true), ModelsLNGMigrationConstants.NSURI_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing.ecore", true), ModelsLNGMigrationConstants.NSURI_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule.ecore", true), ModelsLNGMigrationConstants.NSURI_ScheduleModel);

		return loader;
	}
}
