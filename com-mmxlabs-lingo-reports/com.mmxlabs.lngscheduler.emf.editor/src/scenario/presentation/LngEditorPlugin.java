/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;

/**
 * This is the central singleton for the Lng editor plugin.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public final class LngEditorPlugin extends EMFPlugin {
	
	public static final boolean DEBUG_UI_ENABLED;

	private static final String DEBUG_PROPERTY_KEY = "com.mmxlabs.debug_ui";
	
	private ServiceTracker<IEclipseJobManager, IEclipseJobManager> jobManagerServiceTracker;

	
	static {
		DEBUG_UI_ENABLED = System.getProperty(DEBUG_PROPERTY_KEY).equalsIgnoreCase("true");
	}
	/**
	 * Keep track of the singleton.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final LngEditorPlugin INSTANCE = new LngEditorPlugin();
	
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
	public LngEditorPlugin() {
		super
			(new ResourceLocator [] {
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
		private ServiceTracker<IEclipseJobManager, IEclipseJobManager> jobManagerServiceTracker;
		
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

		@Override
		public void start(BundleContext context) throws Exception {
			super.start(context);
			jobManagerServiceTracker = new ServiceTracker<IEclipseJobManager, IEclipseJobManager>(context,
					IEclipseJobManager.class.getName(), null);
			jobManagerServiceTracker.open();
		}

		@Override
		public void stop(BundleContext context) throws Exception {
			jobManagerServiceTracker.close();
			jobManagerServiceTracker = null;
			super.stop(context);
		}
		
		public IEclipseJobManager getJobManager() {
			return jobManagerServiceTracker.getService();
		}
	}

}
