/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation.gui;

import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.ui.validation.internal.Activator;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

/**
 * Simple label provider for {@link IStatus} objects showing the message and an
 * icon based on severity.
 * 
 * @author Simon Goodall
 * 
 */
public class ValidationStatusColumnLabelProvider extends ColumnLabelProvider {

	private final Image imgError;
	private final Image imgWarn;
	private final Image imgInfo;

	public ValidationStatusColumnLabelProvider() {

		imgError = CommonImages.getImageDescriptor(IconPaths.Error, IconMode.Enabled).createImage();
		imgWarn = CommonImages.getImageDescriptor(IconPaths.Warning, IconMode.Enabled).createImage();
		imgInfo = CommonImages.getImageDescriptor(IconPaths.Information, IconMode.Enabled).createImage();
	}

	@Override
	public void dispose() {
		imgError.dispose();
		imgWarn.dispose();
		imgInfo.dispose();
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof GroupedValidationStatusContentProvider.Node) {
			GroupedValidationStatusContentProvider.Node node = (GroupedValidationStatusContentProvider.Node) element;

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
		if (element instanceof Map.Entry) {
			Map.Entry entry = (Map.Entry) element;
			element = entry.getValue();
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
		if (element instanceof Map.Entry) {
			Map.Entry entry = (Map.Entry) element;
			Object key = entry.getKey();
			if (key instanceof ScenarioModelRecord) {
				ScenarioModelRecord modelRecord = (ScenarioModelRecord) key;
				return modelRecord.getName();
			}
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
