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
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;

/**
 * 
 * A blank editor implementation to avoid layout issues.
 * 
 */
public class PlaceholderInlineEditor implements IInlineEditor {

	/**
	 * Object being edited
	 */
	protected EObject input;
	protected Collection<EObject> ranges;

	protected final EStructuralFeature feature;
	protected boolean currentlySettingValue = false;

	protected ICommandHandler commandHandler;

	protected Label label;

	private boolean editorEnabled = true;
	private boolean editorLocked = false;

	protected MMXRootObject rootObject;

	public PlaceholderInlineEditor(final EStructuralFeature feature) {
		this.feature = feature;
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject rootObject, final EObject input, final Collection<EObject> range) {

	}

	@Override
	public void processValidation(final IStatus status) {

	}

	protected Object getValue() {

		return null;
	}

	public Control wrapControl(final Control c) {
		return c;
	}

	@Override
	public EStructuralFeature getFeature() {
		return feature;
	}

	@Override
	public void setCommandHandler(final ICommandHandler handler) {
		this.commandHandler = handler;
	}

	@Override
	public void setLabel(final Label label) {
		this.label = label;
	}

	@Override
	public Label getLabel() {
		return label;
	}

	@Override
	public void setEditorEnabled(final boolean enabled) {
		this.editorEnabled = enabled;
	}

	@Override
	public boolean isEditorEnabled() {
		return editorEnabled;
	}

	@Override
	public void setEditorLocked(final boolean locked) {
		this.editorLocked = locked;
	}

	@Override
	public boolean isEditorLocked() {
		return editorLocked;
	}

	@Override
	public void setEditorVisible(final boolean visible) {
		this.editorEnabled = visible;
	}

	@Override
	public boolean isEditorVisible() {
		return true;
	}

	@Override
	public EObject getEditorTarget() {
		return input;
	}

	@Override
	public void addNotificationChangedListener(final IInlineEditorExternalNotificationListener listener) {
	}

	@Override
	public void removeNotificationChangedListener(final IInlineEditorExternalNotificationListener listener) {
	}

	@Override
	public boolean hasLabel() {
		return true;
	}

	@Override
	public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {
		// Create a composite that wants to be 1x1 px so that is doesn't take too much
		// room in the form
		return new Composite(parent, SWT.None) {

			@Override
			protected void checkSubclass() {
				// Override to allow the subclass
			}

			@Override
			public Point computeSize(final int wHint, final int hHint, final boolean changed) {
				// Try to force a small size
				return super.computeSize(1, 1, changed);
			}
		};
	}
}
