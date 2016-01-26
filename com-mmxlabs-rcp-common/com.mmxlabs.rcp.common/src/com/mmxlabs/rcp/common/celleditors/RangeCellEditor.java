/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.celleditors;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.mmxlabs.rcp.common.controls.Range;

/**
 * A celleditor which displays a Range widget
 * 
 * @author hinton
 * 
 */
public class RangeCellEditor extends CellEditor {
	private Range control;
	private int lowValue, highValue;

	private Listener listener = null;

	public RangeCellEditor() {
		super();
		setValueValid(true);
	}

	public RangeCellEditor(final Composite parent, final int style) {
		super(parent, style);
		setValueValid(true);
	}

	public RangeCellEditor(final Composite parent) {
		super(parent);
		setValueValid(true);
	}

	private boolean inSetter = false;

	private synchronized Listener getListener() {
		if (listener == null) {
			listener = new Listener() {
				@Override
				public void handleEvent(final Event event) {
					getControlValue();
				}
			};
		}

		return listener;
	}

	private FocusListener focusListener = null;

	private FocusListener getFocusListener() {
		if (focusListener == null) {
			focusListener = new FocusListener() {
				@Override
				public void focusLost(final FocusEvent e) {
					RangeCellEditor.this.focusLost();
				}

				@Override
				public void focusGained(final FocusEvent e) {

				}
			};
		}
		return focusListener;
	}

	@Override
	protected Control createControl(final Composite parent) {
		control = new Range(parent, SWT.NONE);
		control.addListener(SWT.Selection, getListener());
		control.addFocusListener(getFocusListener());
		setControlValue();
		control.pack();
		return control;
	}

	private void setControlValue() {
		if ((control == null) || control.isDisposed()) {
			return;
		}
		inSetter = true;

		control.removeListener(SWT.Selection, getListener());

		control.setUpperValue(highValue);
		control.setLowerValue(lowValue);
		control.addListener(SWT.Selection, getListener());

		inSetter = false;
	}

	private void getControlValue() {
		if (inSetter) {
			// System.err.println("This should not happen ; getting value in setter.");

			// TODO why does this happen?!?!
			return;
		}

		if ((control == null) || control.isDisposed()) {
			return;
		}
		if ((lowValue != control.getLowerValue()) || (highValue != control.getUpperValue())) {
			lowValue = control.getLowerValue();
			highValue = control.getUpperValue();
			markDirty();
		}
	}

	@Override
	protected Object doGetValue() {
		return new Integer[] { lowValue, highValue };
	}

	@Override
	protected void doSetFocus() {
		control.setFocus();
		control.addFocusListener(getFocusListener());
	}

	@Override
	public void deactivate() {
		if ((control != null) && !control.isDisposed()) {
			control.removeFocusListener(getFocusListener());
		}
		super.deactivate();
	}

	@Override
	protected void doSetValue(final Object value) {
		final Integer[] ivalue = (Integer[]) value;

		this.lowValue = ivalue[0];
		this.highValue = ivalue[1];
		setControlValue();
	}

	@Override
	protected boolean dependsOnExternalFocusListener() {
		return false;
	}
}
