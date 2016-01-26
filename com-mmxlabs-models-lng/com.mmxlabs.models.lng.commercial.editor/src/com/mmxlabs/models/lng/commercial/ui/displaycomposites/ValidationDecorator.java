/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.displaycomposites;

import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.models.ui.validation.IDetailConstraintStatus;

public abstract class ValidationDecorator {

	/**
	 * {@link ControlDecoration} used to show validation messages.
	 */
	protected ControlDecoration validationDecoration;

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

	protected int severity = IStatus.INFO;
	
	public ValidationDecorator(Control control, int position) {
		validationDecoration = new ControlDecoration(control, position);
		validationDecoration.hide();

		// These should be the defaults...
		validationDecoration.setShowHover(true);
		validationDecoration.setShowOnlyOnFocus(false);
	}
	
	public void processValidation(final IStatus status) {
		if (status.isOK()) {
			// No problems, so hide decoration
			validationDecoration.hide();
		} else {
			// Default severity
			int severity = IStatus.INFO;

			// Builder used to accumulate messages
			final StringBuilder sb = new StringBuilder();
			
			boolean join = false;
			for (final IStatus element: ValidationStatusUtils.getStatuses(status)) {
				if (checkStatus(element)) {
					if (join) {
						sb.append("\n");
					}
					
					sb.append(element.getMessage());
					// Is severity worse, then note it
					if (element.getSeverity() > severity) {
						severity = element.getSeverity();
					}
		
					join = true;
				}				
			}
			
			if (sb.toString().isEmpty()) {
				// No problems, so hide decoration
				validationDecoration.hide();
				return;
			}

			// Update description text
			validationDecoration.setDescriptionText(sb.toString());

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

	protected boolean checkStatus(IStatus status) {
		if (status instanceof IDetailConstraintStatus) {
			final IDetailConstraintStatus element = (IDetailConstraintStatus) status;

			final Collection<EObject> objects = element.getObjects();
			if (objects.contains(getTarget())) {
				if (element.getFeaturesForEObject(getTarget()).contains(getFeature())) {
					return true;
				}
			}
		}
		return false;
	}

	
	abstract protected  EObject getFeature();

	abstract protected EObject getTarget();
}
