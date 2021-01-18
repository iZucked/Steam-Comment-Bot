/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.views;

import java.time.LocalDate;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.date.LocalDateTextFormatter;

public class ValidationLabelProvider extends ColumnLabelProvider {

	private final Color errorColour = Display.getDefault().getSystemColor(SWT.COLOR_RED);
	private final Color warningColour = Display.getDefault().getSystemColor(SWT.COLOR_YELLOW);

	private final EStructuralFeature feature;

	private final Map<Object, IStatus> validationErrors;

	public ValidationLabelProvider(final EStructuralFeature feature, final Map<Object, IStatus> validationErrors) {
		this.feature = feature;
		this.validationErrors = validationErrors;
	}

	@Override
	public Color getBackground(final Object element) {
		if (validationErrors.containsKey(element)) {
			final IStatus s = validationErrors.get(element);
			if (s.getSeverity() == IStatus.ERROR) {
				return errorColour;
			} else if (s.getSeverity() == IStatus.WARNING) {
				return warningColour;
			}
		}

		return super.getBackground(element);
	}

	@Override
	public String getText(Object element) {

		if (element instanceof EObject) {
			element = ((EObject) element).eGet(feature);
		}
		if (element instanceof LocalDate) {
			final LocalDateTextFormatter formatter = new LocalDateTextFormatter();
			formatter.setValue(element);
			return formatter.getDisplayString();
		}

		if (element instanceof NamedObject) {
			return ((NamedObject) element).getName();
		} else if (element != null) {
			return element.toString();
		}
		return super.getText(element);
	}

	@Override
	public String getToolTipText(final Object element) {
		if (validationErrors.containsKey(element)) {
			final IStatus s = validationErrors.get(element);
			if (!s.isOK()) {
				return getMessages(s);
			}
		}
		return super.getToolTipText(element);
	}

	/**
	 * Extract message hierarchy and construct the tool tip message.
	 * 
	 * @param status
	 * @return
	 */
	private String getMessages(final IStatus status) {
		if (status.isMultiStatus()) {
			final StringBuilder sb = new StringBuilder();
			for (final IStatus s : status.getChildren()) {
				sb.append(getMessages(s));
				sb.append("\n");
			}
			return sb.toString();
		} else {
			return status.getMessage();
		}
	}
}