/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.celleditors;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;

/**
 * A CellEditor which displays a spinner for numbers
 * 
 * @author hinton
 *
 */
public class SpinnerCellEditor extends CellEditor {
	private Spinner control;
	public SpinnerCellEditor(final Composite parent) {
		super(parent);
	}

	public void setDigits(int digits) {
		control.setDigits(digits);
	}
	
	public int getDigits() {
		return control.getDigits();
	}
	
	private int numberToSelection(final Number number) {
		final int d = getDigits();
		
		if (d == 0) {
			return number.intValue();
		} else {
			return ((int) (number.floatValue() * Math.pow(10, d)));
		}
	}
	
	/**
	 * Set the maximum legal value for this control.
	 * 
	 * If you're going to call {@link #setDigits(int)}, you should do it before you do this. 
	 * @param max
	 */
	public void setMaximumValue(final Number max) {
		control.setMaximum(numberToSelection(max));
	}

	/**
	 * Set the minimum legal value for this control.
	 * 
	 * If you're going to call {@link #setDigits(int)}, you should do it before you do this. 
	 * @param max
	 */
	public void setMinimumValue(final Number min) {
		control.setMinimum(numberToSelection(min));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.CellEditor#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createControl(final Composite parent) {
		control = new Spinner(parent, SWT.NONE);
		return control;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.CellEditor#doGetValue()
	 */
	@Override
	protected Object doGetValue() {
		if (getDigits() == 0) {
			return (Integer) control.getSelection();
		} else {
			return (Float) ((float)
					(control.getSelection() * Math.pow(10, -getDigits())));
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
	 */
	@Override
	protected void doSetFocus() {
		control.setFocus();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.CellEditor#doSetValue(java.lang.Object)
	 */
	@Override
	protected void doSetValue(final Object value) {
		assert value instanceof Number;
		final Number number = (Number) value;
		
		control.setSelection(numberToSelection(number));
	}
}
