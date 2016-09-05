/**
 */
package com.mmxlabs.models.lng.adp.presentation;

import com.mmxlabs.models.lng.cargo.provider.CargoEditPlugin;

import com.mmxlabs.models.lng.commercial.provider.CommercialEditPlugin;

import com.mmxlabs.models.lng.fleet.provider.FleetEditPlugin;

import com.mmxlabs.models.lng.port.provider.PortEditPlugin;

import com.mmxlabs.models.lng.pricing.provider.PricingEditPlugin;

import com.mmxlabs.models.lng.spotmarkets.provider.SpotMarketsEditPlugin;

import com.mmxlabs.models.lng.types.provider.LNGTypesEditPlugin;

import com.mmxlabs.models.mmxcore.provider.MmxcoreEditPlugin;

import org.eclipse.emf.common.EMFPlugin;

import org.eclipse.emf.common.ui.EclipseUIPlugin;

import org.eclipse.emf.common.util.ResourceLocator;

/**
 * This is the central singleton for the ADP editor plugin.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public final class ADPEditorPlugin extends EMFPlugin {
	/**
	 * Keep track of the singleton.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final ADPEditorPlugin INSTANCE = new ADPEditorPlugin();
	
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
	public ADPEditorPlugin() {
		super
			(new ResourceLocator [] {
				CargoEditPlugin.INSTANCE,
				CommercialEditPlugin.INSTANCE,
				FleetEditPlugin.INSTANCE,
				LNGTypesEditPlugin.INSTANCE,
				MmxcoreEditPlugin.INSTANCE,
				PortEditPlugin.INSTANCE,
				PricingEditPlugin.INSTANCE,
				SpotMarketsEditPlugin.INSTANCE,
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
	public static class Implementation extends EclipseUIPlugin {
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
