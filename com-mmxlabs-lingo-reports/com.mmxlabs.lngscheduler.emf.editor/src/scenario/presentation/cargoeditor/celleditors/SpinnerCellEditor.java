/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.celleditors;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
		
		control.addKeyListener(new KeyAdapter() {
			{
				final KeyAdapter ka = this;
				control.addDisposeListener(
						new DisposeListener() {

							@Override
							public void widgetDisposed(DisposeEvent e) {
								control.removeKeyListener(ka);
							}
						});
			}
			
            // hook key pressed - see PR 14201  
            @Override
			public void keyPressed(final KeyEvent e) {
                keyReleaseOccured(e);

                // as a result of processing the above call, clients may have
                // disposed this cell editor
                if ((getControl() == null) || getControl().isDisposed()) {
					return;
				}
            }
		}
		);
		
		
		control.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				handleDefaultSelection(e);
			}
			
		});
		
		return control;
	}

	
	
	@Override
	protected void keyReleaseOccured(KeyEvent keyEvent) {
		if (keyEvent.character == '\r') { // Return key
			return;
		}
		super.keyReleaseOccured(keyEvent);
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
	
	   /**
     * Handles a default selection event from the text control by applying the editor
     * value and deactivating this cell editor.
     * 
     * @param event the selection event
     * 
     * @since 3.0
     */
    protected void handleDefaultSelection(SelectionEvent event) {
        // same with enter-key handling code in keyReleaseOccured(e);
        fireApplyEditorValue();
        deactivate();
    }
}
