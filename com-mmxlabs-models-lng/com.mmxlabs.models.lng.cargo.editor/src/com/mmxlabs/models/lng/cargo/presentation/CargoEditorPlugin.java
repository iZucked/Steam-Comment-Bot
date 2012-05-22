/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.presentation;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.mmxlabs.models.lng.cargo.ui.commands.DateUpdatingCommandProvider;
import com.mmxlabs.models.lng.cargo.ui.commands.PortUpdatingCommandProvider;
import com.mmxlabs.models.lng.cargo.ui.commands.SlotDeletingCommandProvider;
import com.mmxlabs.models.lng.cargo.ui.commands.SlotNameUpdatingCommandProvider;
import com.mmxlabs.models.lng.types.provider.LNGTypesEditPlugin;
import com.mmxlabs.models.mmxcore.provider.MmxcoreEditPlugin;
import com.mmxlabs.models.ui.commandservice.IModelCommandProvider;

/**
 * This is the central singleton for the Cargo editor plugin.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public final class CargoEditorPlugin extends EMFPlugin {
	/**
	 * Keep track of the singleton.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final CargoEditorPlugin INSTANCE = new CargoEditorPlugin();
	
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
	public CargoEditorPlugin() {
		super
			(new ResourceLocator [] {
				LNGTypesEditPlugin.INSTANCE,
				MmxcoreEditPlugin.INSTANCE,
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
	 * @generated NOT services
	 */
	public static class Implementation extends EclipseUIPlugin {
		private ServiceRegistration<IModelCommandProvider> dateCorrectorRegistration;
		private ServiceRegistration<IModelCommandProvider> portCorrectorRegistration;
		private ServiceRegistration<IModelCommandProvider> slotNameCorrectorRegistration;
		private ServiceRegistration<IModelCommandProvider> slotDeletingRegistration;

		/**
		 * Creates an instance.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated NOT
		 */
		public Implementation() {
			super();
	
			// Remember the static instance.
			//
			plugin = this;
		}

		
		@Override
		public void start(BundleContext context) throws Exception {
			super.start(context);
			dateCorrectorRegistration = context.registerService(IModelCommandProvider.class, new DateUpdatingCommandProvider(), null);
			portCorrectorRegistration = context.registerService(IModelCommandProvider.class, new PortUpdatingCommandProvider(), null);
			slotNameCorrectorRegistration = context.registerService(IModelCommandProvider.class, new SlotNameUpdatingCommandProvider(), null);
			slotDeletingRegistration = context.registerService(IModelCommandProvider.class, new SlotDeletingCommandProvider(), null);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
		 */
		@Override
		public void stop(BundleContext context) throws Exception {
			dateCorrectorRegistration.unregister();
			portCorrectorRegistration.unregister();
			slotNameCorrectorRegistration.unregister();
			slotDeletingRegistration.unregister();
			
			super.stop(context);
		}
	}

}
