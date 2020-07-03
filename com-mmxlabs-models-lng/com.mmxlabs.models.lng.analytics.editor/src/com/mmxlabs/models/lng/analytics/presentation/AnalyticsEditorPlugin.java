/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.presentation;

import com.mmxlabs.models.lng.analytics.ui.views.SandboxViewCreatorService;
import com.mmxlabs.models.lng.cargo.provider.CargoEditPlugin;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.osgi.framework.BundleContext;

import com.mmxlabs.models.lng.commercial.provider.CommercialEditPlugin;
import com.mmxlabs.models.lng.fleet.provider.FleetEditPlugin;
import com.mmxlabs.models.lng.parameters.provider.ParametersEditPlugin;
import com.mmxlabs.models.lng.port.provider.PortEditPlugin;
import com.mmxlabs.models.lng.pricing.provider.PricingEditPlugin;
import com.mmxlabs.models.lng.schedule.provider.ScheduleEditPlugin;
import com.mmxlabs.models.lng.spotmarkets.provider.SpotMarketsEditPlugin;
import com.mmxlabs.models.lng.types.provider.LNGTypesEditPlugin;
import com.mmxlabs.models.mmxcore.provider.MmxcoreEditPlugin;

/**
 * This is the central singleton for the Analytics editor plugin.
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * @generated
 */
public final class AnalyticsEditorPlugin extends EMFPlugin {
	/**
	 * Keep track of the singleton.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static final AnalyticsEditorPlugin INSTANCE = new AnalyticsEditorPlugin();

	/**
	 * Keep track of the singleton.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private static Implementation plugin;

	/**
	 * Create the instance.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public AnalyticsEditorPlugin() {
		super
			(new ResourceLocator [] {
				CargoEditPlugin.INSTANCE,
				CommercialEditPlugin.INSTANCE,
				FleetEditPlugin.INSTANCE,
				LNGTypesEditPlugin.INSTANCE,
				MmxcoreEditPlugin.INSTANCE,
				ParametersEditPlugin.INSTANCE,
				PortEditPlugin.INSTANCE,
				PricingEditPlugin.INSTANCE,
				ScheduleEditPlugin.INSTANCE,
				SpotMarketsEditPlugin.INSTANCE,
			});
	}

	/**
	 * Returns the singleton instance of the Eclipse plugin.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the singleton instance.
	 * @generated
	 */
	@Override
	public ResourceLocator getPluginResourceLocator() {
		return plugin;
	}

	/**
	 * Returns the singleton instance of the Eclipse plugin.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the singleton instance.
	 * @generated
	 */
	public static Implementation getPlugin() {
		return plugin;
	}

	/**
	 * The actual implementation of the Eclipse <b>Plugin</b>. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NO
	 */
	public static class Implementation extends EclipseUIPlugin {

		private SandboxViewCreatorService sandboxCreatorService = new SandboxViewCreatorService();
		
		/**
		 * Creates an instance.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @generated
		 */
		public Implementation() {
			super();
	
			// Remember the static instance.
			//
			plugin = this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
		 */
		@Override
		public void start(final BundleContext context) throws Exception {
			super.start(context);
			sandboxCreatorService.start();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
		 */
		@Override
		public void stop(final BundleContext context) throws Exception {
			sandboxCreatorService.stop();
			super.stop(context);
		}
	}

}
