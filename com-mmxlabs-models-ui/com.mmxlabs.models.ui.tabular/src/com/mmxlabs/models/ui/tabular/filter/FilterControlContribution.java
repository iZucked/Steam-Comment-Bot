/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.filter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * A ControlContribution which displays a filter text box.
 * 
 * @author hinton
 * 
 */
public class FilterControlContribution extends ControlContribution {
	private String label = "";
	private Text t;
	private final List<ModifyListener> listeners = Collections.synchronizedList(new LinkedList<ModifyListener>());
	private final List<KeyListener> keyListeners = Collections.synchronizedList(new LinkedList<KeyListener>());

	public FilterControlContribution(final String id, final String label) {
		super(id);
		this.label = label;
	}

	public Text getTextBox() {
		return t;
	}

	public void addModifyListener(final ModifyListener ml) {
		listeners.add(ml);
	}

	public void removeModifyListener(final ModifyListener ml) {
		listeners.remove(ml);
	}

	public void addKeyListener(final KeyListener kl) {
		keyListeners.add(kl);
	}

	public void removeKeyListener(final KeyListener kl) {
		keyListeners.remove(kl);
	}

	@Override
	protected Control createControl(final Composite parent) {
		final Composite row = new Composite(parent, SWT.NONE);
		final GridLayout gl = new GridLayout(2, false);
		gl.marginHeight = 0;
		gl.marginWidth = 0;

		row.setLayout(gl);

		final Label l = new Label(row, SWT.NONE);
		l.setText(label);
		t = new Text(row, SWT.SEARCH);
		l.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, true));
		t.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

		t.setToolTipText("Enter text to filter this grid\n\n" + "Use spaces to separate terms which must all occur\n" + "Use commas to separate terms of which one must occur\n"
				+ "Use columnname:value or cn:value to look for value in the column Column Name\n\n" + "Press F1 for more help on searching, press ESC to clear");

		t.addModifyListener(new ModifyListener() {
			{
				final ModifyListener self = this;
				t.addDisposeListener(new DisposeListener() {
					@Override
					public void widgetDisposed(final DisposeEvent e) {
						t.removeModifyListener(self);
					}
				});
			}

			@Override
			public void modifyText(final ModifyEvent e) {
				for (final ModifyListener ml : listeners) {
					ml.modifyText(e);
				}
			}
		});

		t.addKeyListener(new KeyAdapter() {
			{
				final KeyAdapter self = this;
				t.addDisposeListener(new DisposeListener() {
					@Override
					public void widgetDisposed(final DisposeEvent e) {
						t.removeKeyListener(self);
					}
				});
			}

			@Override
			public void keyReleased(final KeyEvent e) {
				if (e.keyCode == SWT.ESC) {
					t.setText("");
				} else {
					for (final KeyListener kl : keyListeners) {
						kl.keyReleased(e);
					}
				}
			}

			@Override
			public void keyPressed(final KeyEvent e) {
				for (final KeyListener kl : keyListeners) {
					kl.keyPressed(e);
				}
			}
		});

		return row;
	}

	@Override
	protected int computeWidth(final Control control) {
		// TODO this is only ever called once; can you make a contribution which resizes?
		return (super.computeWidth(control) * 2);
	}
}
