/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public final class CargoEditorPlugin extends AbstractUIPlugin {

	public static final String COLOR_CARGO_DES = "cargo.des";
	public static final String COLOR_CARGO_FOB = "cargo.fob";

	public static final String IMAGE_CARGO_LINK = "cargo.link";
	public static final String IMAGE_CARGO_LOCK = "cargo.lock";
	public static final String IMAGE_CARGO_NOTES = "cargo.notes";

	public static final String IMAGE_CARGO_SWAP = "cargo.swap";
	public static final String IMAGE_CARGO_SWAP_DISABLED = "cargo.swap.disabled";

	public static final String IMAGE_CARGO_WIRING = "cargo.wiring";
	public static final String IMAGE_CARGO_WIRING_DISABLED = "cargo.wiring.disabled";

	// The shared instance
	private static CargoEditorPlugin plugin;
	private boolean imageRegistryInited;
	private ColorRegistry colourRegistry;

	@Override
	public void start(final BundleContext context) throws Exception {
		plugin = this;
		super.start(context);
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Get an image from the registry. The first call needs to be from a UI thread.
	 * 
	 */
	public synchronized Image getImage(final String key) {

		// Lazily instantiate the image registry as this needs to be done in a UI
		// thread.
		final ImageRegistry imageRegistry = getImageRegistry();
		if (!imageRegistryInited) {
			initImageRegistry(imageRegistry);
		}
		return imageRegistry.get(key);
	}

	/**
	 * Get an image from the registry. The first call needs to be from a UI thread.
	 * 
	 */
	public synchronized ImageDescriptor getImageDescriptor(final String key) {

		// Lazily instantiate the image registry as this needs to be done in a UI
		// thread.
		final ImageRegistry imageRegistry = getImageRegistry();
		if (!imageRegistryInited) {
			initImageRegistry(imageRegistry);
		}
		return imageRegistry.getDescriptor(key);
	}

	private void initImageRegistry(final ImageRegistry imageRegistry) {
		imageRegistry.put(IMAGE_CARGO_LINK, AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.cargo.editor", "icons/wired.gif"));
		imageRegistry.put(IMAGE_CARGO_LOCK, AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.cargo.editor", "icons/assigned.gif"));
		imageRegistry.put(IMAGE_CARGO_NOTES, AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.cargo.editor", "icons/small_notes.gif"));
		{
			final ImageDescriptor swapImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.cargo.editor", "icons/swap.gif");
			imageRegistry.put(IMAGE_CARGO_SWAP, swapImageDescriptor);
			imageRegistry.put(IMAGE_CARGO_SWAP_DISABLED, ImageDescriptor.createWithFlags(swapImageDescriptor, SWT.IMAGE_DISABLE));
		}

		{
			final ImageDescriptor swapImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.cargo.editor", "icons/rewire_icon.png");
			imageRegistry.put(IMAGE_CARGO_WIRING, swapImageDescriptor);
			imageRegistry.put(IMAGE_CARGO_WIRING_DISABLED, ImageDescriptor.createWithFlags(swapImageDescriptor, SWT.IMAGE_DISABLE));
		}
		imageRegistryInited = true;
	}

	public synchronized Color getColor(final String key) {
		if (colourRegistry == null) {
			colourRegistry = new ColorRegistry();

			colourRegistry.put(COLOR_CARGO_DES, new RGB(150, 210, 230));
			colourRegistry.put(COLOR_CARGO_FOB, new RGB(190, 220, 180));
		}
		return colourRegistry.get(key);
	}

	public static CargoEditorPlugin getPlugin() {
		return plugin;
	}
}
