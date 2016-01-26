/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.parameters.presentation;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.osgi.framework.BundleContext;

import com.mmxlabs.models.mmxcore.provider.MmxcoreEditPlugin;

/**
 * This is the central singleton for the Parameters editor plugin.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public final class ParametersEditorPlugin extends EMFPlugin {
	/**
	 * Keep track of the singleton.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final ParametersEditorPlugin INSTANCE = new ParametersEditorPlugin();
	
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
	public ParametersEditorPlugin() {
		super
			(new ResourceLocator [] {
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
	 * @generated NOT
	 */
	public static class Implementation extends EclipseUIPlugin {
//		private ServiceTracker<IConstraintCheckerRegistry, IConstraintCheckerRegistry>
//			constraintCheckerRegistryTracker;
//		
//		private ServiceTracker<IFitnessFunctionRegistry, IFitnessFunctionRegistry>
//			fitnessFunctionRegistryTracker;
//	
		
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

//		public IConstraintCheckerRegistry getConstraintCheckerRegistry() {
//			return constraintCheckerRegistryTracker.getService();
//		}
//		
//		public IFitnessFunctionRegistry getFitnessFunctionRegistry() {
//			return fitnessFunctionRegistryTracker.getService();
//		}

		@Override
		public void start(BundleContext context) throws Exception {
			super.start(context);
			
//			constraintCheckerRegistryTracker = new ServiceTracker<IConstraintCheckerRegistry, IConstraintCheckerRegistry>(context, IConstraintCheckerRegistry.class, null);
//			fitnessFunctionRegistryTracker = new ServiceTracker<IFitnessFunctionRegistry, IFitnessFunctionRegistry>(context, IFitnessFunctionRegistry.class, null);
//		
//			constraintCheckerRegistryTracker.open();
//			fitnessFunctionRegistryTracker.open();
		}

		@Override
		public void stop(BundleContext context) throws Exception {
//			constraintCheckerRegistryTracker.close();
//			fitnessFunctionRegistryTracker.close();
			super.stop(context);
		}
		
		
	}

}
