/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.presentation;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.mmxlabs.models.lng.types.provider.LNGTypesEditPlugin;
import com.mmxlabs.models.mmxcore.provider.MmxcoreEditPlugin;

/**
 * This is the central singleton for the Cargo editor plugin.
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * @generated
 */
public final class CargoEditorPlugin extends EMFPlugin {

	/**
	 * @since 2.0
	 */
	public static final String COLOR_CARGO_DES = "cargo.des";
	/**
	 * @since 2.0
	 */
	public static final String COLOR_CARGO_FOB = "cargo.fob";

	/**
	 * @since 2.0
	 */
	public static final String IMAGE_CARGO_LINK = "cargo.link";
	/**
	 * @since 2.0
	 */
	public static final String IMAGE_CARGO_LOCK = "cargo.lock";
	/**
	 * @since 2.0
	 */
	public static final String IMAGE_CARGO_SWAP = "cargo.swap";
	/**
	 * @since 2.0
	 */
	public static final String IMAGE_CARGO_SWAP_DISABLED = "cargo.swap.disabled";

	/**
	 * Keep track of the singleton.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static final CargoEditorPlugin INSTANCE = new CargoEditorPlugin();

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
	public CargoEditorPlugin() {
		super
			(new ResourceLocator [] {
				LNGTypesEditPlugin.INSTANCE,
				MmxcoreEditPlugin.INSTANCE,
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
	 * @generated NOT services
	 */
	public static class Implementation extends EclipseUIPlugin {

		private boolean imageRegistryInited = false;
		private ColorRegistry colorRegistry;

		/**
		 * Creates an instance. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated NOT
		 */
		public Implementation() {
			super();

			// Remember the static instance.
			//
			plugin = this;
		}

		@Override
		public void start(final BundleContext context) throws Exception {
			super.start(context);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
		 */
		/**
		 * @since 2.0
		 */
		@Override
		public void stop(final BundleContext context) throws Exception {

			super.stop(context);
		}

		/**
		 * Get an image from the registry. The first call needs to be from a UI thread.
		 * 
		 * @since 2.0
		 */
		public synchronized Image getImage(final String key) {

			// Lazily instantiate the image registry as this needs to be done in a UI thread.
			final ImageRegistry imageRegistry = getImageRegistry();
			if (!imageRegistryInited) {
				initImageRegistry(imageRegistry);
			}
			return imageRegistry.get(key);
		}

		/**
		 * Get an image from the registry. The first call needs to be from a UI thread.
		 * 
		 * @since 2.0
		 */
		public synchronized ImageDescriptor getImageDescriptor(final String key) {

			// Lazily instantiate the image registry as this needs to be done in a UI thread.
			final ImageRegistry imageRegistry = getImageRegistry();
			if (!imageRegistryInited) {
				initImageRegistry(imageRegistry);
			}
			return imageRegistry.getDescriptor(key);
		}

		private void initImageRegistry(final ImageRegistry imageRegistry) {
			imageRegistry.put(IMAGE_CARGO_LINK, AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.cargo.editor", "icons/wired.gif"));
			imageRegistry.put(IMAGE_CARGO_LOCK, AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.cargo.editor", "icons/assigned.gif"));
			ImageDescriptor swapImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.cargo.editor", "icons/swap.gif");
			imageRegistry.put(IMAGE_CARGO_SWAP, swapImageDescriptor);
			imageRegistry.put(IMAGE_CARGO_SWAP_DISABLED, ImageDescriptor.createWithFlags(swapImageDescriptor, SWT.IMAGE_DISABLE));

			imageRegistryInited = true;
		}

		/**
		 * @since 2.0
		 */
		public synchronized Color getColor(String key) {
			if (colorRegistry == null) {
				colorRegistry = new ColorRegistry();

				colorRegistry.put(COLOR_CARGO_DES, new RGB(150, 210, 230));
				colorRegistry.put(COLOR_CARGO_FOB, new RGB(190, 220, 180));
			}
			return colorRegistry.get(key);
		}
	}

}
