/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.detailview;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
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

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

public abstract class UnsettableInlineEditor extends BasicAttributeInlineEditor {
	private Button setButton;
	private Object lastSetValue;
	public UnsettableInlineEditor(EMFPath path, EStructuralFeature feature,
			EditingDomain editingDomain) {
		super(path, feature, editingDomain);
	}

	protected abstract Control createValueControl(Composite parent);
	protected abstract void updateValueDisplay(final Object value);
	protected void updateControl() {
		
	}
	
	@Override
	public Control createControl(Composite parent) {
		if (feature.isUnsettable()) {
			final Composite sub = new Composite(parent, SWT.NONE);
			sub.setLayout(new GridLayout(2, false));
			final Button setButton = new Button(sub, SWT.CHECK);
			this.setButton = setButton;
			setButton.addSelectionListener(
					new SelectionAdapter() {
						{
							final SelectionAdapter self = this;
							setButton.addDisposeListener(new DisposeListener() {
								@Override
								public void widgetDisposed(final DisposeEvent e) {
									setButton.removeSelectionListener(self);
								}});
						}

						@Override
						public void widgetSelected(final SelectionEvent e) {
							if (setButton.getSelection()) {
								doSetValue(lastSetValue); 
							} else {
								// unset value
								unsetValue();
							}
						}
					}
					);
			setButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
			final Control inner = createValueControl(sub);
			inner.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			return sub;
		} else {
			return createValueControl(parent);
		}
	}

	protected void unsetValue() {
		super.doSetValue(SetCommand.UNSET_VALUE); 
	}

	@Override
	protected Object getValue() {
		if (input.eIsSet(feature)) {
			return super.getValue();
		} else {
			return null;
		}
	}

	@Override
	protected synchronized void doSetValue(final Object value) {
		lastSetValue = value; // hold for later checking and unchecking.
		// maybe set button when value is changed
		if (setButton != null && value != null) {
			setButton.setSelection(true);
		}
		if (setButton == null || setButton.getSelection()) {
			super.doSetValue(value);
		}
	}

	@Override
	protected void updateDisplay(Object value) {
		updateControl();
		if (setButton == null || value != null) {
			updateValueDisplay(value);
		}
		if (setButton != null) setButton.setSelection(value != null);
	}
}
