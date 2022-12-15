/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 * 
 */
package com.mmxlabs.models.ui.editors.impl;

import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.models.ui.validation.IDetailConstraintStatus;

public class ValidationStatusWrapper {

	public static ValidationStatusWrapper createValidationDecorator(Control control, EObject input, EStructuralFeature feature) {
		ValidationStatusWrapper wrapper = new ValidationStatusWrapper(feature);
		wrapper.wrapControl(control);
		wrapper.setInput(input);
		return wrapper;
	}

	protected EObject input;

	public EObject getInput() {
		return input;
	}

	public void setInput(EObject input) {
		this.input = input;
	}

	protected final EStructuralFeature feature;

	/**
	 * {@link ControlDecoration} used to show validation messages.
	 */
	private ControlDecoration validationDecoration;

	/**
	 * Cached reference to the Information icon
	 */
	protected final FieldDecoration decorationInfo = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION);

	/**
	 * Cached reference to the Warning icon
	 */
	protected final FieldDecoration decorationWarning = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_WARNING);

	/**
	 * Cached reference to the Error icon
	 */
	protected final FieldDecoration decorationError = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);

	private ValidationStatusWrapper(final EStructuralFeature feature) {
		this.feature = feature;
	}

	public void processValidation(final IStatus status) {
		final Control c = validationDecoration.getControl();
		if (c == null || c.isDisposed()) {
			return;
		}

		if (status.isOK()) {
			// No problems, so hide decoration
			if (validationDecoration != null) {
				validationDecoration.hide();
			}
		} else {
			// Default severity
			int severity = IStatus.OK;

			// Builder used to accumulate messages
			final StringBuilder sb = new StringBuilder();

			severity = checkStatus(status, IStatus.OK, sb);

			final String description = sb.toString();
			if (description.isEmpty()) {
				// No problems, so hide decoration
				if (validationDecoration != null) {
					validationDecoration.hide();
				}
				return;
			}

			// Update description text
			if (!description.equals(validationDecoration.getDescriptionText())) {
				validationDecoration.setDescriptionText(description);
			}

			// Update icon
			switch (severity) {
			case IStatus.INFO:
				validationDecoration.setImage(decorationInfo.getImage());
				break;
			case IStatus.WARNING:
				validationDecoration.setImage(decorationWarning.getImage());
				break;
			case IStatus.ERROR:
			default:
				validationDecoration.setImage(decorationError.getImage());
				break;
			}

			// Show the decoration.
			validationDecoration.show();
		}
	}

	/**
	 * Check status message applies to this editor.
	 * 
	 * @param status
	 * @return
	 */
	private int checkStatus(final IStatus status, int currentSeverity, final StringBuilder sb) {
		if (status.isMultiStatus()) {
			final IStatus[] children = status.getChildren();
			for (final IStatus element : children) {
				final int severity = checkStatus(element, currentSeverity, sb);

				// Is severity worse, then note it
				if (severity > currentSeverity) {
					currentSeverity = element.getSeverity();
				}

			}
		}
		if (status instanceof IDetailConstraintStatus) {
			final IDetailConstraintStatus element = (IDetailConstraintStatus) status;

			final Collection<EObject> objects = element.getObjects();
			if (objects.contains(input)) {
				if (element.getFeaturesForEObject(input).contains(feature)) {

					sb.append(element.getMessage());
					sb.append("\n");

					// Is severity worse, then note it
					if (element.getSeverity() > currentSeverity) {
						currentSeverity = element.getSeverity();
					}

					return currentSeverity;
				}
			}
		}

		return currentSeverity;
	}

	public void wrapControl(final Control c) {
		// Create decorator for validation items
		{
			validationDecoration = new ControlDecoration(c, SWT.LEFT | SWT.TOP);
			validationDecoration.hide();

			// These should be the defaults...
			validationDecoration.setShowHover(true);
			validationDecoration.setShowOnlyOnFocus(false);

			// Set a default image
			// commented out, because this takes about 70% of the runtime of displaying the
			// editor
			// validationDecoration.setImage(decorationInfo.getImage());

			// Hide by default
		}
	}

}
