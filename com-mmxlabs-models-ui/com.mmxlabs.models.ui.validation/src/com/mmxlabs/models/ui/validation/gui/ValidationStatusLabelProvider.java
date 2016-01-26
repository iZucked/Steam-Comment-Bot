/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation.gui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.ui.validation.internal.Activator;

/**
 * Simple label provider for {@link IStatus} objects showing the message and an icon based on severity.
 * 
 * @author Simon Goodall
 * 
 */
public class ValidationStatusLabelProvider implements ILabelProvider {

	private final Image imgError;
	private final Image imgWarn;
	private final Image imgInfo;

	public ValidationStatusLabelProvider() {

		imgError = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/error.gif").createImage();
		imgWarn = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/warning.gif").createImage();
		imgInfo = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/information.gif").createImage();
	}

	@Override
	public void dispose() {
		imgError.dispose();
		imgWarn.dispose();
		imgInfo.dispose();
	}

	@Override
	public Image getImage(final Object element) {
		if (element instanceof IStatus) {
			final int severity = ((IStatus) element).getSeverity();

			if (severity == IStatus.ERROR) {
				return imgError;
			} else if (severity == IStatus.WARNING) {
				return imgWarn;
			} else if (severity == IStatus.INFO) {
				return imgInfo;
			}
		}
		return null;
	}

	@Override
	public String getText(final Object element) {
		if (element instanceof IStatus) {
			return ((IStatus) element).getMessage();
		}
		return null;
	}

	@Override
	public void addListener(final ILabelProviderListener listener) {

	}

	@Override
	public boolean isLabelProperty(final Object element, final String property) {
		return false;
	}

	@Override
	public void removeListener(final ILabelProviderListener listener) {

	}
}
