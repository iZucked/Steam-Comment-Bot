/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation.gui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

/**
 * Simple label provider for {@link IStatus} objects showing the message and an icon based on severity.
 * 
 * @author Simon Goodall
 * 
 */
public class ValidationStatusLabelProvider implements ILabelProvider, IColorProvider {

	private final Color errorColour = Display.getDefault().getSystemColor(SWT.COLOR_RED);

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
		if (element instanceof IStatus status) {
			final int severity = status.getSeverity();

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
		if (element instanceof GroupedValidationStatusContentProvider.Node node) {
			return node.desc;
		}
		if (element instanceof IStatus status) {
			return status.getMessage();
		}
		return null;
	}

	@Override
	public Color getForeground(final Object element) {
		if (element instanceof DetailConstraintStatusDecorator dcsd && dcsd.isFlagged()) {
			return errorColour;
		}

		return null;
	}

	@Override
	public Color getBackground(Object element) {
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
