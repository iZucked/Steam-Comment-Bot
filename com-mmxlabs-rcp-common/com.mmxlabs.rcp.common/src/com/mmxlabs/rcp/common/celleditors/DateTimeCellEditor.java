/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.celleditors;

import java.util.Calendar;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.mmxlabs.rcp.common.controls.DateAndComboTime;

/**
 * A CellEditor which displays a date & time picker control.
 * 
 * TODO trigger focus listeners to make changes happen
 * 
 * @author hinton
 * 
 */
public class DateTimeCellEditor extends CellEditor {
	private DateAndComboTime popup;

	public DateTimeCellEditor() {
		super();
	}

	public DateTimeCellEditor(final Composite parent, final int style) {
		super(parent, style);
	}

	public DateTimeCellEditor(final Composite parent) {
		super(parent);
	}

	public void setLegalRange(final Calendar minimumValue, final Calendar maximumValue) {

	}

	@Override
	protected Control createControl(final Composite parent) {
		popup = new DateAndComboTime(parent, SWT.DROP_DOWN, false, 0);
		popup.addListener(SWT.Selection, getSelectionListener());
		popup.addKeyListener(getKeyListener());
		popup.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(final DisposeEvent e) {
				popup.removeListener(SWT.Selection, getSelectionListener());
				popup.removeKeyListener(getKeyListener());
				// popup.removeFocusListener(getFocusListener());
			}
		});
		setValueValid(true);
		return popup;
	}

	private Listener selectionListener = null;

	private Listener getSelectionListener() {
		if (selectionListener == null) {
			selectionListener = new Listener() {
				@Override
				public void handleEvent(final Event event) {
					markDirty();
				}
			};
		}
		return selectionListener;
	}

	private KeyListener keyListener = null;

	private KeyListener getKeyListener() {
		if (keyListener == null) {
			keyListener = new KeyAdapter() {
				@Override
				public void keyPressed(final KeyEvent e) {
					keyReleaseOccured(e);
				}
			};
		}
		return keyListener;
	}

	@Override
	protected void doSetFocus() {
		popup.setFocus();
	}

	@Override
	protected Object doGetValue() {
		return popup.getValue();
	}

	@Override
	protected void doSetValue(final Object value) {
		popup.setValue((Calendar) value);
	}

	@Override
	protected boolean dependsOnExternalFocusListener() {
		return false;
	}
}
