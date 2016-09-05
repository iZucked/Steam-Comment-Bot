/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration;

import com.mmxlabs.models.migration.PackageData;

/**
 */
public final class ModelsLNGMigrationConstants {

	// Context under which migration units
	// DO NOT CHANGE
	public static final String Context = "com.mmxlabs.lingo";

	// The namespace URI's
	public static final String NSURI_DateTime = "http://www.mmxlabs.com/models/datetime/1";

	public static final PackageData PKG_DATA_DateTime = new PackageData(NSURI_DateTime, "platform:/plugin/com.mmxlabs.models.datetime/model/datetime.ecore",
			"../../com.mmxlabs.models.datetime/model/datetime.ecore", "../../../com.mmxlabs.models.datetime/model/datetime.ecore");

	public static final String NSURI_MMXCore = "http://www.mmxlabs.com/models/mmxcore/1/";
	/**
	 */
	public static final PackageData PKG_DATA_MMXCore = new PackageData(NSURI_MMXCore, "platform:/plugin/com.mmxlabs.models.mmxcore/model/mmxcore.ecore",
			"../../com.mmxlabs.models.mmxcore/model/mmxcore.ecore", "../../../com.mmxlabs.models.mmxcore/model/mmxcore.ecore");

	public static final String NSURI_LNGTypes = "http://www.mmxlabs.com/models/lng/types/1/";
	/**
	 */
	public static final PackageData PKG_DATA_LNGTypes = new PackageData(NSURI_LNGTypes, "platform:/plugin/com.mmxlabs.models.lng.types/model/lngtypes.ecore",
			"../../com.mmxlabs.models.lng.types/model/lngtypes.ecore");

	/**
	 */
	public static final String NSURI_AnalyticsModel = "http://www.mmxlabs.com/models/lng/analytics/1/";
	/**
	 */
	public static final PackageData PKG_DATA_AnalyticsModel = new PackageData(NSURI_AnalyticsModel, "platform:/plugin/com.mmxlabs.models.lng.analytics/model/analytics.ecore",
			"../../com.mmxlabs.models.lng.analytics/model/analytics.ecore");

	/**
	 */
	public static final String NSURI_ADPModel = "http://www.mmxlabs.com/models/lng/adp1/";
	/**
	 */
	public static final PackageData PKG_DATA_ADPModel = new PackageData(NSURI_AnalyticsModel, "platform:/plugin/com.mmxlabs.models.lng.adp/model/adp.ecore",
			"../../com.mmxlabs.models.lng.adp/model/adp.ecore");

	/**
	 */
	public static final String NSURI_CargoModel = "http://www.mmxlabs.com/models/lng/cargo/1/";
	/**
	 */
	public static final PackageData PKG_DATA_CargoModel = new PackageData(NSURI_CargoModel, "platform:/plugin/com.mmxlabs.models.lng.cargo/model/cargo.ecore",
			"../../com.mmxlabs.models.lng.cargo/model/cargo.ecore");

	/**
	 */
	public static final String NSURI_CommercialModel = "http://www.mmxlabs.com/models/lng/commercial/1/";
	/**
	 */
	public static final PackageData PKG_DATA_CommercialModel = new PackageData(NSURI_CommercialModel, "platform:/plugin/com.mmxlabs.models.lng.commercial/model/commercial.ecore",
			"../../com.mmxlabs.models.lng.commercial/model/commercial.ecore");

	public static final String NSURI_FleetModel = "http://www.mmxlabs.com/models/lng/fleet/1/";
	/**
	 */
	public static final PackageData PKG_DATA_FleetModel = new PackageData(NSURI_FleetModel, "platform:/plugin/com.mmxlabs.models.lng.fleet/model/fleet.ecore",
			"../../com.mmxlabs.models.lng.fleet/model/fleet.ecore");

	/**
	 */
	public static final String NSURI_AssignmentModel = "http://www.mmxlabs.com/models/lng/assignment/1/";
	/**
	 */
	public static final PackageData PKG_DATA_AssignmentModel = new PackageData(NSURI_AssignmentModel, "platform:/plugin/com.mmxlabs.models.lng.assignment/model/assignment.ecore",
			"platform:/plugin/com.mmxlabs.models.lng.migration/model/assignment.ecore", "../../com.mmxlabs.models.lng.assignment/model/assignment.ecore");

	/**
	 */
	public static final String NSURI_ParametersModel = "http://www.mmxlabs.com/models/lng/parameters/1/";
	/**
	 */
	public static final PackageData PKG_DATA_ParametersModel = new PackageData(NSURI_ParametersModel, "platform:/plugin/com.mmxlabs.models.lng.parameters/model/parameters.ecore",
			"../../com.mmxlabs.models.lng.parameters/model/parameters.ecore");

	public static final String NSURI_PricingModel = "http://www.mmxlabs.com/models/lng/pricing/1/";
	/**
	 */
	public static final PackageData PKG_DATA_PricingModel = new PackageData(NSURI_PricingModel, "platform:/plugin/com.mmxlabs.models.lng.pricing/model/pricing.ecore",
			"../../com.mmxlabs.models.lng.pricing/model/pricing.ecore");

	public static final String NSURI_PortModel = "http://www.mmxlabs.com/models/lng/port/1/";
	/**
	 */
	public static final PackageData PKG_DATA_PortModel = new PackageData(NSURI_PortModel, "platform:/plugin/com.mmxlabs.models.lng.port/model/port.ecore",
			"../../com.mmxlabs.models.lng.port/model/port.ecore");

	public static final String NSURI_ScheduleModel = "http://www.mmxlabs.com/models/lng/schedule/1/";
	/**
	 */
	public static final PackageData PKG_DATA_ScheduleModel = new PackageData(NSURI_ScheduleModel, "platform:/plugin/com.mmxlabs.models.lng.schedule/model/schedule.ecore",
			"../../com.mmxlabs.models.lng.schedule/model/schedule.ecore");

	public static final String NSURI_SpotMarketsModel = "http://www.mmxlabs.com/models/lng/spotmarkets/1/";
	/**
	 */
	public static final PackageData PKG_DATA_SpotMarketsModel = new PackageData(NSURI_SpotMarketsModel, "platform:/plugin/com.mmxlabs.models.lng.spotmarkets/model/spotmarkets.ecore",
			"../../com.mmxlabs.models.lng.spotmarkets/model/spotmarkets.ecore");

	/**
	 */
	public static final String NSURI_ScenarioModel = "http://www.mmxlabs.com/models/lng/scenario/1/";
	/**
	 */
	public static final PackageData PKG_DATA_ScenarioModel = new PackageData(NSURI_ScenarioModel, "platform:/plugin/com.mmxlabs.models.lng.scenario/model/scenario.ecore",
			"../../com.mmxlabs.models.lng.scenario/model/scenario.ecore");

	public static final String NSURI_ActualsModel = "http://www.mmxlabs.com/models/lng/actuals/1/";

	public static final PackageData PKG_DATA_ActualsModel = new PackageData(NSURI_ActualsModel, "platform:/plugin/com.mmxlabs.models.lng.actuals/model/actuals.ecore",
			"../../com.mmxlabs.models.lng.actuals/model/actuals.ecore");

}
