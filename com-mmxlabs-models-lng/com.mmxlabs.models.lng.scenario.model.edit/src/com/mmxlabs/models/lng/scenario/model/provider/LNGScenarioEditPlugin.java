/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.scenario.model.provider;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.ResourceLocator;

import com.mmxlabs.models.lng.actuals.provider.ActualsEditPlugin;
import com.mmxlabs.models.lng.adp.provider.ADPEditPlugin;
import com.mmxlabs.models.lng.analytics.provider.AnalyticsEditPlugin;
import com.mmxlabs.models.lng.cargo.provider.CargoEditPlugin;
import com.mmxlabs.models.lng.commercial.provider.CommercialEditPlugin;
import com.mmxlabs.models.lng.fleet.provider.FleetEditPlugin;
import com.mmxlabs.models.lng.nominations.provider.NominationsEditPlugin;
import com.mmxlabs.models.lng.parameters.provider.ParametersEditPlugin;
import com.mmxlabs.models.lng.port.provider.PortEditPlugin;
import com.mmxlabs.models.lng.pricing.provider.PricingEditPlugin;
import com.mmxlabs.models.lng.schedule.provider.ScheduleEditPlugin;
import com.mmxlabs.models.lng.spotmarkets.provider.SpotMarketsEditPlugin;
import com.mmxlabs.models.lng.transfers.provider.TransfersEditPlugin;
import com.mmxlabs.models.lng.types.provider.LNGTypesEditPlugin;
import com.mmxlabs.models.mmxcore.provider.MmxcoreEditPlugin;

/**
 * This is the central singleton for the LNGScenario edit plugin.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public final class LNGScenarioEditPlugin extends EMFPlugin {
	/**
	 * Keep track of the singleton.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final LNGScenarioEditPlugin INSTANCE = new LNGScenarioEditPlugin();

	/**
	 * Keep track of the singleton.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static Implementation plugin;

	/**
	 * Create the instance.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LNGScenarioEditPlugin() {
		super
		  (new ResourceLocator [] {
		     ActualsEditPlugin.INSTANCE,
		     ADPEditPlugin.INSTANCE,
		     AnalyticsEditPlugin.INSTANCE,
		     CargoEditPlugin.INSTANCE,
		     CommercialEditPlugin.INSTANCE,
		     FleetEditPlugin.INSTANCE,
		     LNGTypesEditPlugin.INSTANCE,
		     MmxcoreEditPlugin.INSTANCE,
		     NominationsEditPlugin.INSTANCE,
		     ParametersEditPlugin.INSTANCE,
		     PortEditPlugin.INSTANCE,
		     PricingEditPlugin.INSTANCE,
		     ScheduleEditPlugin.INSTANCE,
		     SpotMarketsEditPlugin.INSTANCE,
		     TransfersEditPlugin.INSTANCE,
		   });
	}

	/**
	 * Returns the singleton instance of the Eclipse plugin.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the singleton instance.
	 * @generated
	 */
	@Override
	public ResourceLocator getPluginResourceLocator() {
		return plugin;
	}

	/**
	 * Returns the singleton instance of the Eclipse plugin.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the singleton instance.
	 * @generated
	 */
	public static Implementation getPlugin() {
		return plugin;
	}

	/**
	 * The actual implementation of the Eclipse <b>Plugin</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static class Implementation extends EclipsePlugin {
		/**
		 * Creates an instance.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public Implementation() {
			super();

			// Remember the static instance.
			//
			plugin = this;
		}
	}

}
