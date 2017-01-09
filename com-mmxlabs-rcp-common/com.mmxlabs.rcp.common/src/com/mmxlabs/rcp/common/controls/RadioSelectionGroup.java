/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.controls;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/*
 * Is there *really* no standard JFace way to bind a radio button group to a single variable?
 */
public class RadioSelectionGroup extends Composite {
	int selectedIndex = -1;
	final ArrayList<Button> buttons = new ArrayList<Button>();
	final Group group;
	final int[] values;

	public RadioSelectionGroup(final Composite parent, final String title, final int style, final String[] labels, final int[] values) {
		super(parent, style);

		final GridLayout layout = new GridLayout(1, true);
		// layout.marginRight = 0;
		layout.marginWidth = 0;
		setLayout(layout);
		// GridData gd = new GridData();
		// gd.verticalIndent = 0;
		// gd.horizontalIndent = 0;
		// gd.grabExcessHorizontalSpace = true;
		// setLayoutData(gd);

		group = new Group(this, style);
		for (final String label : labels) {
			addButton(label);
		}
		final GridLayout gl = new GridLayout(1, false);
		gl.marginLeft = 0;
		gl.marginRight = 0;
		gl.marginWidth = 0;
		setLayout(gl);
		group.setLayout(new GridLayout(labels.length, false));
		// GridData groupLayoutData = new GridData();
		// group.setLayoutData(groupLayoutData);
		group.setText(title);
		this.values = values;
	}

	public Button addButton(final String text) {
		final int index = buttons.size();

		final Button button = new Button(group, SWT.RADIO);
		buttons.add(button);

		button.setText(text);
		// GridData gd = new GridData();
		// // gd.verticalIndent = 0;
		// // gd.horizontalIndent = 0;
		// // gd.grabExcessHorizontalSpace = true;
		// button.setLayoutData(gd);
		button.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (button.getSelection()) {
					RadioSelectionGroup.this.selectedIndex = index;
				}
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
			}

		});

		return button;
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(final int value) {
		buttons.get(value).setSelection(true);
		selectedIndex = value;
	}

	public int getSelectedValue() {
		return values[getSelectedIndex()];
	}

}