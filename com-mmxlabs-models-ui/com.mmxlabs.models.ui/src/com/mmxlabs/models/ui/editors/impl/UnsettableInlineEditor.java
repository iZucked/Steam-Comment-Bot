/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.MMXObject.DelegateInformation;

public abstract class UnsettableInlineEditor extends BasicAttributeInlineEditor {
	private Button setButton;
	private Object lastSetValue;
	private Control inner;
	/**
	 */
	protected FormToolkit toolkit;
	/**
	 */
	protected EMFDataBindingContext dbc;

	public UnsettableInlineEditor(final EStructuralFeature feature) {
		super(feature);
	}

	protected abstract Control createValueControl(Composite parent);

	protected abstract void updateValueDisplay(final Object value);

	protected void updateControl() {

	}

	/**
	 * Subclasses must return a value which should be set when there was no value set, and then the set box was ticked.
	 * 
	 * @return A suitable value
	 */
	protected abstract Object getInitialUnsetValue();

	private void setControlEnabled(final Control c, final boolean enabled) {
		if (c == null)
			return;
		c.setEnabled(enabled);
		if (c instanceof Composite) {
			for (final Control c2 : ((Composite) c).getChildren()) {
				setControlEnabled(c2, enabled);
			}
		}
	}

	/**
	 */
	@Override
	public Control createControl(final Composite parent, EMFDataBindingContext dbc, final FormToolkit toolkit) {

		this.dbc = dbc;
		this.toolkit = toolkit;
		final Control c;
		if (feature.isUnsettable()) {
			// FIXME: too early! Needs to be after/part of display(), once input has been set
			this.lastSetValue = getInitialUnsetValue();
			// final Composite sub = new Composite(parent, SWT.NONE);
			final Composite sub = toolkit.createComposite(parent);
			GridLayout gl = new GridLayout(2, false);
			gl.marginLeft = 0;
			gl.marginBottom = 0;
			gl.marginHeight = 0;
			gl.marginTop = 0;
			gl.marginRight = 0;
			gl.marginWidth = 0;
			sub.setLayout(gl);
			// this.setButton = new Button(sub, SWT.CHECK);
			this.setButton = toolkit.createButton(sub, "", SWT.CHECK);
			this.inner = createValueControl(sub);
			setButton.addSelectionListener(new SelectionAdapter() {
				{
					final SelectionAdapter self = this;
					setButton.addDisposeListener(new DisposeListener() {
						@Override
						public void widgetDisposed(final DisposeEvent e) {
							setButton.removeSelectionListener(self);
						}
					});
				}

				@Override
				public void widgetSelected(final SelectionEvent e) {
					if (setButton.getSelection()) {
						doSetValue(lastSetValue, true);
						setControlEnabled(inner, true);
						currentlySettingValue = true;
						updateValueDisplay(getValue());
						currentlySettingValue = false;
					} else {
						// unset value
						unsetValue();
						setControlEnabled(inner, false);
						if (input instanceof MMXObject) {
							currentlySettingValue = true;
							updateValueDisplay(getValue());
							currentlySettingValue = false;
						}
					}

				}
			});
			setButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

			inner.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			c = sub;
		} else {
			c = createValueControl(parent);
		}
		return super.wrapControl(c);
	}

	protected void unsetValue() {
		super.doSetValue(SetCommand.UNSET_VALUE, true);
	}

	@Override
	protected Object getValue() {
		if (input != null && (!feature.isUnsettable() || input.eIsSet(feature))) {
			return super.getValue();
		} else {
			if (input instanceof MMXObject) {
				return ((MMXObject) input).getUnsetValue(getFeature());
			} else {
				return null;
			}
		}
	}

	@Override
	protected synchronized void doSetValue(final Object value, final boolean forceCommandExecution) {
		if (currentlySettingValue)
			return;
		if (value != null)
			lastSetValue = value; // hold for later checking and unchecking.
		// maybe set button when value is changed
		if (setButton != null && value != null && !setButton.isDisposed()) {
			setButton.setSelection(true);
		}
		if (setButton == null || setButton.getSelection()) {
			super.doSetValue(value, forceCommandExecution);
		}
	}

	protected boolean valueIsSet() {
		if (input == null)
			return false;
		return input.eIsSet(getFeature());
	}

	@Override
	protected void updateDisplay(final Object value) {

		if (inner != null && inner.isDisposed()) {
			return;
		}
		updateControl();
		if (setButton == null || value != null) {
			updateValueDisplay(value);
		}
		if (setButton != null && !setButton.isDisposed()) {
			setButton.setSelection(valueIsSet());
			setButton.setEnabled(isEditorEnabled());
		}

		boolean innerEnabled = !isFeatureReadonly() && isEditorEnabled() && !isEditorLocked() && (setButton == null || setButton.getSelection());
		if (inner != null) {
			setControlEnabled(inner, innerEnabled);
		}
	}

	@Override
	public void setEditorEnabled(boolean enabled) {
		super.setEditorEnabled(enabled);
	}

	@Override
	protected void setControlsEnabled(final boolean enabled) {
		final boolean controlsEnabled = !isFeatureReadonly() && enabled;

		super.setControlsEnabled(controlsEnabled);
		if (setButton != null) {
			setButton.setEnabled(controlsEnabled);
			if (inner != null) {
				inner.setEnabled(setButton.getSelection());
			}
		} else if (inner != null) {
			inner.setEnabled(controlsEnabled);
		}

	}

	@Override
	protected void setControlsVisible(final boolean visible) {

		super.setControlsVisible(visible);
		if (setButton != null) {
			setButton.setVisible(visible);
		}
	}

	@Override
	protected boolean updateOnChangeToFeature(final Object changedFeature) {
		if (input instanceof MMXObject) {
			MMXObject mmxinput = (MMXObject) input;
			DelegateInformation di = mmxinput.getUnsetValueOrDelegate(feature);
			if (di != null && di.delegatesTo(changedFeature)) {
				return true;
			}

		}

		return super.updateOnChangeToFeature(changedFeature);
	}
}
