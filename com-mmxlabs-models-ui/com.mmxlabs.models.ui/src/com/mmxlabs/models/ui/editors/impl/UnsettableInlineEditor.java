/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

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
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.mmxcore.MMXObject;

public abstract class UnsettableInlineEditor extends BasicAttributeInlineEditor {
	private Button setButton;
	private Object lastSetValue;
	private Control inner;

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

	@Override
	public Control createControl(final Composite parent) {

		final Control c;
		if (feature.isUnsettable()) {
			this.lastSetValue = getInitialUnsetValue();
			final Composite sub = new Composite(parent, SWT.NONE);
			sub.setLayout(new GridLayout(2, false));
			final Button setButton = new Button(sub, SWT.CHECK);
			this.setButton = setButton;
			final Control inner = createValueControl(sub);
			this.inner = inner;
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
						updateValueDisplay(lastSetValue);
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
		if (!feature.isUnsettable() || input.eIsSet(feature)) {
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
	protected synchronized void doSetValue(final Object value, boolean forceCommandExecution) {
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
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				if (inner != null && inner.isDisposed()) {
					return;
				}
				updateControl();
				if (setButton == null || value != null) {
					updateValueDisplay(value);
				}
				if (setButton != null && !setButton.isDisposed()) {
					setButton.setSelection(valueIsSet());
				}
				setControlEnabled(inner, isEnabled() && (setButton == null || setButton.getSelection()));

			}
		});
	}

	@Override
	public void setEnabled(final boolean enabled) {
		if (setButton != null) {
			setButton.setEnabled(enabled);
		}

		super.setEnabled(enabled);
	}
}
