/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;

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
		Analytics, Cargo, Commercial, Fleet, Input, Optimiser, Port, Pricing, Schedule, SpotMarkets
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
		} else if (ModelsLNGMigrationConstants.NSURI_SpotMarketsModel.equals(nsURI)) {
			return ModelsLNGSet_v1.SpotMarkets;
		}
		return null;
	}

	public static MetamodelLoader createV0Loader(final Map<String, URI> extraPackages) {

		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore.ecore", true), ModelsLNGMigrationConstants.NSURI_MMXCore);
		final EPackage typesPackage = loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes.ecore", true), ModelsLNGMigrationConstants.NSURI_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v0.ecore", true), ModelsLNGMigrationConstants.NSURI_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v0.ecore", true), ModelsLNGMigrationConstants.NSURI_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v0.ecore", true), ModelsLNGMigrationConstants.NSURI_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v0.ecore", true), ModelsLNGMigrationConstants.NSURI_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.input/model/input-v0.ecore", true), ModelsLNGMigrationConstants.NSURI_InputModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.optimiser/model/optimiser-v0.ecore", true), ModelsLNGMigrationConstants.NSURI_OptimiserModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v0.ecore", true), ModelsLNGMigrationConstants.NSURI_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v0.ecore", true), ModelsLNGMigrationConstants.NSURI_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v0.ecore", true), ModelsLNGMigrationConstants.NSURI_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/spotmarkets-v0.ecore", true), ModelsLNGMigrationConstants.NSURI_SpotMarketsModel);

		hookSerializableObjectConvertor(typesPackage);

		if (extraPackages != null) {
			for (final Map.Entry<String, URI> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getValue(), e.getKey());
			}
		}

		return loader;
	}

	/**
	 * The LNG Types model serialises objects into a Base 64 encoded byte array. The standard {@link EFactory} implemention cannot handle this and throws an exception. Here we hook into the datatype
	 * "convert" the data into a string. There is (currently) no need to do anything with this data directly.
	 * 
	 * @param typesPackage
	 */
	private static void hookSerializableObjectConvertor(final EPackage typesPackage) {
		final EClassifier eClassifier = typesPackage.getEClassifier("SerializableObject");
		((EDataType.Internal) eClassifier).setConversionDelegate(new EDataType.Internal.ConversionDelegate() {

			@Override
			public Object createFromString(final String literal) {
				return literal;
			}

			@Override
			public String convertToString(final Object value) {
				return value.toString();
			}
		});
	}

	public static MetamodelLoader createV1Loader(final Map<String, URI> extraPackages) {

		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore.ecore", true), ModelsLNGMigrationConstants.NSURI_MMXCore);
		final EPackage typesPackage = loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes.ecore", true), ModelsLNGMigrationConstants.NSURI_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v1.ecore", true), ModelsLNGMigrationConstants.NSURI_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v1.ecore", true), ModelsLNGMigrationConstants.NSURI_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v1.ecore", true), ModelsLNGMigrationConstants.NSURI_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v1.ecore", true), ModelsLNGMigrationConstants.NSURI_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.input/model/input-v1.ecore", true), ModelsLNGMigrationConstants.NSURI_InputModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.optimiser/model/optimiser-v1.ecore", true), ModelsLNGMigrationConstants.NSURI_OptimiserModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v1.ecore", true), ModelsLNGMigrationConstants.NSURI_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v1.ecore", true), ModelsLNGMigrationConstants.NSURI_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v1.ecore", true), ModelsLNGMigrationConstants.NSURI_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v1.ecore", true), ModelsLNGMigrationConstants.NSURI_SpotMarketsModel);

		hookSerializableObjectConvertor(typesPackage);

		if (extraPackages != null) {
			for (final Map.Entry<String, URI> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getValue(), e.getKey());
			}
		}

		return loader;
	}

	public static MetamodelLoader createV2Loader(final Map<String, URI> extraPackages) {

		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore.ecore", true), ModelsLNGMigrationConstants.NSURI_MMXCore);
		final EPackage typesPackage = loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes.ecore", true), ModelsLNGMigrationConstants.NSURI_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics-v2.ecore", true), ModelsLNGMigrationConstants.NSURI_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo-v2.ecore", true), ModelsLNGMigrationConstants.NSURI_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial-v2.ecore", true), ModelsLNGMigrationConstants.NSURI_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet-v2.ecore", true), ModelsLNGMigrationConstants.NSURI_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.input/model/input-v2.ecore", true), ModelsLNGMigrationConstants.NSURI_InputModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.optimiser/model/optimiser-v2.ecore", true), ModelsLNGMigrationConstants.NSURI_OptimiserModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port-v2.ecore", true), ModelsLNGMigrationConstants.NSURI_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing-v2.ecore", true), ModelsLNGMigrationConstants.NSURI_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule-v2.ecore", true), ModelsLNGMigrationConstants.NSURI_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets-v2.ecore", true), ModelsLNGMigrationConstants.NSURI_SpotMarketsModel);

		hookSerializableObjectConvertor(typesPackage);

		if (extraPackages != null) {
			for (final Map.Entry<String, URI> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getValue(), e.getKey());
			}
		}

		return loader;
	}

	/**
	 * @since 4.0
	 */
	public static MetamodelLoader createV3Loader(final Map<String, URI> extraPackages) {

		final MetamodelLoader loader = new MetamodelLoader();

		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.mmxcore/model/mmxcore.ecore", true), ModelsLNGMigrationConstants.NSURI_MMXCore);
		final EPackage typesPackage = loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.types/model/lngtypes.ecore", true), ModelsLNGMigrationConstants.NSURI_LNGTypes);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.analytics/model/analytics.ecore", true), ModelsLNGMigrationConstants.NSURI_AnalyticsModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.cargo/model/cargo.ecore", true), ModelsLNGMigrationConstants.NSURI_CargoModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.commercial/model/commercial.ecore", true), ModelsLNGMigrationConstants.NSURI_CommercialModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.fleet/model/fleet.ecore", true), ModelsLNGMigrationConstants.NSURI_FleetModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.input/model/input.ecore", true), ModelsLNGMigrationConstants.NSURI_InputModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.optimiser/model/optimiser.ecore", true), ModelsLNGMigrationConstants.NSURI_OptimiserModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.port/model/port.ecore", true), ModelsLNGMigrationConstants.NSURI_PortModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.pricing/model/pricing.ecore", true), ModelsLNGMigrationConstants.NSURI_PricingModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.schedule/model/schedule.ecore", true), ModelsLNGMigrationConstants.NSURI_ScheduleModel);
		loader.loadEPackage(URI.createPlatformPluginURI("/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets.ecore", true), ModelsLNGMigrationConstants.NSURI_SpotMarketsModel);

		hookSerializableObjectConvertor(typesPackage);

		if (extraPackages != null) {
			for (final Map.Entry<String, URI> e : extraPackages.entrySet()) {
				loader.loadEPackage(e.getValue(), e.getKey());
			}
		}

		return loader;
	}
}
