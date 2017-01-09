/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;


import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;

import com.mmxlabs.models.ui.editors.util.ControlUtils;

/**
 */
public class CheckedControl<T extends Control> extends Composite {
	private final Button check;
	private T control;
	public CheckedControl(Composite parent, int style) {
		super(parent, style);
		check = new Button(this, SWT.CHECK);
		final GridLayout l = new GridLayout(2, false);
		check.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true));
		l.marginBottom = l.marginTop = l.marginLeft = l.marginRight = l.marginWidth = l.marginHeight = 0;
		l.verticalSpacing = 0;
		l.horizontalSpacing = 1;
		setLayout(l);
		check.addSelectionListener(new SelectionAdapter() {
			{
				final SelectionAdapter self = this;
				check.addDisposeListener(new DisposeListener() {
					
					@Override
					public void widgetDisposed(DisposeEvent e) {
						check.removeSelectionListener(self);
					}
				});
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				updateEnablement();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				updateEnablement();
			}
			
		});
	}
	
	private void updateEnablement() {
		ControlUtils.setControlEnabled(control, check.getSelection());
	}
	
	public void setValueControl(final T control) {
		assert Arrays.asList(getChildren()).contains(control);
		this.control = control;
		control.setLayoutData(new GridData(GridData.FILL_BOTH));
		for (final Listener fl : this.getListeners(SWT.FocusIn | SWT.FocusOut)) {
			if (fl instanceof FocusListener) control.addFocusListener((FocusListener) fl);
		}
	}
	
	public T getValueControl() {
		return control;
	}

	@Override
	public boolean isFocusControl() {
		return control.isDisposed() ? false : (super.isFocusControl() || check.isFocusControl() || control.isFocusControl());
	}
	
	public boolean isCheckFocusControl() {
		return check.isFocusControl();
	}
	
	public boolean isControlFocusControl() {
		return control.isFocusControl();
	}
	
	@Override
	public boolean setFocus() {
		if (check.getSelection()) {
			return control.setFocus();
		} else {
			return check.setFocus();
		}
	}

	public boolean isChecked() {
		return check.getSelection();
	}
	
	public void setChecked(final boolean checked) {
		check.setSelection(checked);
		updateEnablement();
	}

	@Override
	public void addFocusListener(final FocusListener listener) {
		super.addFocusListener(listener);
		check.addFocusListener(listener);
		if (control != null) control.addFocusListener(listener);
	}
}
