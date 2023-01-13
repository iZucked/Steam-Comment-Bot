/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation.gui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

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

		imgError = CommonImages.getImage(IconPaths.Error, IconMode.Enabled);
		imgWarn = CommonImages.getImage(IconPaths.Warning, IconMode.Enabled);
		imgInfo = CommonImages.getImage(IconPaths.Information, IconMode.Enabled);
	}

	@Override
	public void dispose() {
		
	}
	
	@Override
	public Image getImage(final Object element) {
		if (element instanceof GroupedValidationStatusContentProvider.Node node) {

			int severity = IStatus.OK;
			for (IStatus s : node.status) {
				if (s.getSeverity() > severity) {
					severity = s.getSeverity();
				}
			}

			if (severity == IStatus.ERROR) {
				return imgError;
			} else if (severity == IStatus.WARNING) {
				return imgWarn;
			} else if (severity == IStatus.INFO) {
				return imgInfo;
			}
		}
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
		if (element instanceof GroupedValidationStatusContentProvider.Node) {
			return ((GroupedValidationStatusContentProvider.Node) element).desc;
		}
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
