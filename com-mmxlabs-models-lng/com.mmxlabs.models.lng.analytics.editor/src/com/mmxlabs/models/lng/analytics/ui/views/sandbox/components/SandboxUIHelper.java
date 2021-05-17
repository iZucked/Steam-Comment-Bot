/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.sandbox.components;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * Helper class for common images colours etc in the Sandbox.
 * 
 * @author Simon Goodall
 *
 */
public class SandboxUIHelper {
	private ResourceManager images = new LocalResourceManager(JFaceResources.getResources());

	// Validation icons and colours
	public final Image imgError;
	public final Image imgWarn;
	public final Image imgInfo;

	public final Color colourError;
	public final Color colourWarn;
	public final Color colourInfo;

	// Shipping type icons
	public final Image imgShippingRoundTrip;
	public final Image imgShippingFleet;
	public final Image imgModel;

	public final Image image_grey_add;

	public final Font normalFont;
	public final Font largeFont;
	public final Font boldFont;

	public final Image imgFOB;
	public final Image imgDES;
	public final Image imgSPOT_FOB;
	public final Image imgSPOT_DES;

	/** Pass in a control - when it is disposed so will this classes UI resources. */

	public SandboxUIHelper(final @NonNull Control owner) {

		images = new LocalResourceManager(JFaceResources.getResources(), owner);

		imgFOB = images.createImage(getImageDescriptor("com.mmxlabs.models.lng.analytics.editor", "/icons/FOB.png"));
		imgDES = images.createImage(getImageDescriptor("com.mmxlabs.models.lng.analytics.editor", "/icons/DES.png"));
		imgSPOT_FOB = images.createImage(getImageDescriptor("com.mmxlabs.models.lng.analytics.editor", "/icons/SPOT_FOB.png"));
		imgSPOT_DES = images.createImage(getImageDescriptor("com.mmxlabs.models.lng.analytics.editor", "/icons/SPOT_DES.png"));

		imgError = images.createImage(getImageDescriptor("com.mmxlabs.models.ui.validation", "/icons/error.gif"));
		imgWarn = images.createImage(getImageDescriptor("com.mmxlabs.models.ui.validation", "/icons/warning.gif"));
		imgInfo = images.createImage(getImageDescriptor("com.mmxlabs.models.ui.validation", "/icons/information.gif"));

		colourError = images.createColor(new RGB(255, 100, 100));
		colourWarn = images.createColor(new RGB(255, 255, 200));
		colourInfo = images.createColor(new RGB(200, 240, 240));

		// Shipping type icons
		imgShippingRoundTrip = images.createImage(getImageDescriptor("com.mmxlabs.models.lng.analytics.editor", "/icons/roundtrip.png"));
		imgShippingFleet = images.createImage(getImageDescriptor("com.mmxlabs.models.lng.analytics.editor", "/icons/fleet.png"));
		imgModel = images.createImage(getImageDescriptor("com.mmxlabs.models.lng.analytics.editor", "/icons/console_view.gif"));

		final Font systemFont = Display.getDefault().getSystemFont();

		this.normalFont = images.createFont(FontDescriptor.createFrom(systemFont));
		this.largeFont = images.createFont(FontDescriptor.createFrom(systemFont).increaseHeight(2));
		this.boldFont = images.createFont(FontDescriptor.createFrom(systemFont).setStyle(SWT.BOLD));

		final ImageDescriptor baseAdd = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD);
		image_grey_add = images.createImage(ImageDescriptor.createWithFlags(baseAdd, SWT.IMAGE_GRAY));
	}

	private ImageDescriptor getImageDescriptor(final String bundleSymbolicName, final String imageFilePath) {
		return ResourceLocator.imageDescriptorFromBundle(bundleSymbolicName, imageFilePath).orElse(null);
	}

	public void dispose() {
		images.dispose();
	}

	/**
	 * Append validation error messages to StringBuilder.
	 * 
	 * @param sb
	 * @param targetElements
	 * @param validationErrors
	 */
	public void extractValidationMessages(final StringBuilder sb, final Set<Object> targetElements, final Map<Object, IStatus> validationErrors) {

		for (final Object target : targetElements) {
			if (validationErrors.containsKey(target)) {
				final IStatus status = validationErrors.get(target);
				if (!status.isOK()) {
					if (sb.length() > 0) {
						sb.append("\n");
					}
					sb.append(status.getMessage());
				}
			}
		}
	}

	public IStatus getWorstStatus(final Set<Object> targetElements, final Map<Object, IStatus> validationErrors) {
		IStatus s = org.eclipse.core.runtime.Status.OK_STATUS;
		for (final Object e : targetElements) {
			if (validationErrors.containsKey(e)) {
				final IStatus status = validationErrors.get(e);
				if (!status.isOK()) {
					if (status.getSeverity() > s.getSeverity()) {
						s = status;
					}
				}
			}
		}
		return s;
	}

	public void updateGridHeaderItem(final ViewerCell cell, final IStatus status) {

		final GridItem item = (GridItem) cell.getItem();
		if (!status.isOK()) {
			if (status.matches(IStatus.ERROR)) {
				item.setHeaderImage(imgError);
			} else if (status.matches(IStatus.WARNING)) {
				item.setHeaderImage(imgWarn);
			} else if (status.matches(IStatus.INFO)) {
				item.setHeaderImage(imgInfo);
			}
		}

	}

	public void updateGridItem(final ViewerCell cell, final IStatus s) {

		final GridItem item = (GridItem) cell.getItem();

		if (!s.isOK()) {
			if (s.matches(IStatus.ERROR)) {
				cell.setBackground(colourError);
				item.setBackground(colourError);
			} else if (s.matches(IStatus.WARNING)) {
				cell.setBackground(colourWarn);
				item.setBackground(colourWarn);
			} else if (s.matches(IStatus.INFO)) {
				cell.setBackground(colourInfo);
				item.setBackground(colourInfo);
			}
		}

	}

	public @Nullable Image getValidationImageForStatus(final IStatus status) {
		if (!status.isOK()) {
			if (status.matches(IStatus.ERROR)) {
				return imgError;
			}
			if (status.matches(IStatus.WARNING)) {
				return imgWarn;
			}
			if (status.matches(IStatus.INFO)) {
				return imgInfo;
			}
		}
		return null;
	}
}
