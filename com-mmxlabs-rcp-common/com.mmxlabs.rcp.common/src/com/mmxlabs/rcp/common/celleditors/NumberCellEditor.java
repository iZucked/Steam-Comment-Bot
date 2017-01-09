/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.celleditors;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * A CellEditor which displays a text editor for numbers
 * 
 * @author Simon Goodall
 * 
 */
public class NumberCellEditor extends CellEditor {

	public interface INumberConvertor {

		Number toNumber(String string);

		String toString(Number number);
	}

	private Text control;

	private final INumberConvertor convertor;

	public NumberCellEditor(final Composite parent, final VerifyListener verifyListener, final ModifyListener modifyListener, final INumberConvertor convertor) {
		super(parent);
		if (verifyListener != null) {
			control.addVerifyListener(verifyListener);
		}
		if (modifyListener != null) {
			control.addModifyListener(modifyListener);
		}
		this.convertor = convertor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createControl(final Composite parent) {
		control = new Text(parent, SWT.NONE);

		control.addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(final TraverseEvent e) {
				if ((e.detail == SWT.TRAVERSE_ESCAPE) || (e.detail == SWT.TRAVERSE_RETURN)) {
					e.doit = false;
				}
			}
		});

		control.addKeyListener(new KeyAdapter() {
			{
				final KeyAdapter ka = this;
				control.addDisposeListener(new DisposeListener() {

					@Override
					public void widgetDisposed(final DisposeEvent e) {
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
		});

		control.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {

			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
				handleDefaultSelection(e);
			}

		});

		return control;
	}

	@Override
	protected void keyReleaseOccured(final KeyEvent keyEvent) {
		if (keyEvent.character == '\r') { // Return key
			return;
		}
		super.keyReleaseOccured(keyEvent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doGetValue()
	 */
	@Override
	protected Object doGetValue() {
		return convertor.toNumber(control.getText());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
	 */
	@Override
	protected void doSetFocus() {
		control.setFocus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doSetValue(java.lang.Object)
	 */
	@Override
	protected void doSetValue(final Object value) {
		assert value instanceof Number;
		final Number number = (Number) value;
		control.setText(convertor.toString(number));
	}

	/**
	 * Handles a default selection event from the text control by applying the editor value and deactivating this cell editor.
	 * 
	 * @param event
	 *            the selection event
	 * 
	 */
	protected void handleDefaultSelection(final SelectionEvent event) {
		// same with enter-key handling code in keyReleaseOccured(e);
		fireApplyEditorValue();
		deactivate();
	}
}
