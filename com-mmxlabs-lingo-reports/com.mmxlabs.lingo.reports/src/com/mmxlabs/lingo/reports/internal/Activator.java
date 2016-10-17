/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.internal;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.mmxlabs.lingo.reports.preferences.PreferenceConstants;
import com.mmxlabs.lingo.reports.views.formatters.Formatters;
import com.mmxlabs.models.lng.cargo.provider.CargoEditPlugin;
import com.mmxlabs.models.lng.commercial.provider.CommercialEditPlugin;
import com.mmxlabs.models.lng.fleet.provider.FleetEditPlugin;
import com.mmxlabs.models.lng.port.provider.PortEditPlugin;
import com.mmxlabs.models.lng.pricing.provider.PricingEditPlugin;
import com.mmxlabs.models.lng.schedule.provider.ScheduleEditPlugin;
import com.mmxlabs.models.lng.spotmarkets.provider.SpotMarketsEditPlugin;
import com.mmxlabs.models.lng.types.provider.LNGTypesEditPlugin;
import com.mmxlabs.models.mmxcore.provider.MmxcoreEditPlugin;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;

/**
 * This is the central singleton for the model edit plugin.
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * @generated
 */
public final class Activator extends EMFPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.lingo.reports"; //$NON-NLS-1$

	/**
	 * Keep track of the singleton.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static final Activator INSTANCE = new Activator();

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
	public Activator() {
		super
		  (new ResourceLocator [] {
		     CargoEditPlugin.INSTANCE,
		     CommercialEditPlugin.INSTANCE,
		     FleetEditPlugin.INSTANCE,
		     LNGTypesEditPlugin.INSTANCE,
		     MmxcoreEditPlugin.INSTANCE,
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
	 * Returns the singleton instance of the Eclipse plugin. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the singleton instance.
	 * @generated NOT
	 */
	public static Implementation getDefault() {
		return plugin;
	}

	/**
	 * The actual implementation of the Eclipse <b>Plugin</b>.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static class Implementation extends EclipsePlugin {

		public static final String IMAGE_PINNED_ROW = "pinned.row";

		private ServiceTracker<IScenarioServiceSelectionProvider, IScenarioServiceSelectionProvider> scenarioServiceSelectionProviderTracker;
		/**
		 * Storage for preferences.
		 */
		private ScopedPreferenceStore preferenceStore;
		private ImageRegistry imageRegistry = null;

		private IPropertyChangeListener propertyChangeListener;

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

		public ImageRegistry getImageRegistry() {
			if (imageRegistry == null) {
				imageRegistry = createImageRegistry();
				initializeImageRegistry(imageRegistry);
			}
			return imageRegistry;
		}

		protected ImageRegistry createImageRegistry() {

			// If we are in the UI Thread use that
			if (Display.getCurrent() != null) {
				return new ImageRegistry(Display.getCurrent());
			}

			if (PlatformUI.isWorkbenchRunning()) {
				return new ImageRegistry(PlatformUI.getWorkbench().getDisplay());
			}

			// Invalid thread access if it is not the UI Thread
			// and the workbench is not created.
			throw new SWTError(SWT.ERROR_THREAD_INVALID_ACCESS);
		}

		protected void initializeImageRegistry(final ImageRegistry reg) {

			final ImageDescriptor importImageDescriptor = getImageDescriptor("icons/full/obj16/PinnedRow.gif");
			getImageRegistry().put(IMAGE_PINNED_ROW, importImageDescriptor);

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext )
		 */
		@Override
		public void stop(final BundleContext context) throws Exception {

			getPreferenceStore().removePropertyChangeListener(propertyChangeListener);
			
			// close the service tracker
			scenarioServiceSelectionProviderTracker.close();
			scenarioServiceSelectionProviderTracker = null;

			if (imageRegistry != null) {
				imageRegistry.dispose();
			}
			imageRegistry = null;

			super.stop(context);
		}

		@Override
		public void start(final BundleContext context) throws Exception {
			scenarioServiceSelectionProviderTracker = new ServiceTracker<IScenarioServiceSelectionProvider, IScenarioServiceSelectionProvider>(context, IScenarioServiceSelectionProvider.class, null);
			scenarioServiceSelectionProviderTracker.open();

			super.start(context);

			propertyChangeListener = new IPropertyChangeListener() {
				@Override
				public void propertyChange(final PropertyChangeEvent event) {
					final String property = event.getProperty();
					if (PreferenceConstants.P_REPORT_DURATION_FORMAT.equals(property)) {
						final String value = getPreferenceStore().getString(property);
						if (value != null) {
							Formatters.DurationMode m = Formatters.DurationMode.valueOf(value);
							if (m != null) {
								Formatters.setDurationMode(m);
							}
						}
					}
				}
			};
			getPreferenceStore().addPropertyChangeListener(propertyChangeListener);
			final String value = getPreferenceStore().getString(PreferenceConstants.P_REPORT_DURATION_FORMAT);
			if (value != null) {
				Formatters.DurationMode m = Formatters.DurationMode.valueOf(value);
				if (m != null) {
					Formatters.setDurationMode(m);
				}
			}
		}

		/**
		 * Returns an image descriptor for the image file at the given plug-in relative path
		 * 
		 * @param path
		 *            the path
		 * @return the image descriptor
		 */
		public static ImageDescriptor getImageDescriptor(final String path) {
			return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path);
		}

		public IScenarioServiceSelectionProvider getScenarioServiceSelectionProvider() {
			return scenarioServiceSelectionProviderTracker.getService();
		}

		/**
		 * Returns the preference store for this UI plug-in. This preference store is used to hold persistent settings for this plug-in in the context of a workbench. Some of these settings will be
		 * user controlled, whereas others may be internal setting that are never exposed to the user.
		 * <p>
		 * If an error occurs reading the preference store, an empty preference store is quietly created, initialized with defaults, and returned.
		 * </p>
		 * <p>
		 * <strong>NOTE:</strong> As of Eclipse 3.1 this method is no longer referring to the core runtime compatibility layer and so plug-ins relying on Plugin#initializeDefaultPreferences will have
		 * to access the compatibility layer themselves.
		 * </p>
		 * 
		 * @return the preference store
		 */
		public IPreferenceStore getPreferenceStore() {
			// Create the preference store lazily.
			if (preferenceStore == null) {
				preferenceStore = new ScopedPreferenceStore(new InstanceScope(), getBundle().getSymbolicName());

			}
			return preferenceStore;
		}
	}

}
