/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;

public class DetailToolbarManager {

	private final ToolBarManager toolbarManager;

	public ToolBarManager getToolbarManager() {
		return toolbarManager;
	}

	private final ControlListener controlListener;
	private final Composite parent;

	public DetailToolbarManager(final Composite control, int position) {

		this.parent = control;
		toolbarManager = new ToolBarManager(SWT.NO_BACKGROUND | position);

		final ToolBar removeToolbar = toolbarManager.createControl(parent);

		// This code places the remove button in the top right of the display composite on top of the Group border.
		final GridData layoutData = new GridData();
		// This tells the grid layout to ignore this control
		layoutData.exclude = true;
		removeToolbar.setLayoutData(layoutData);
		// Make sure we are above the Group
		removeToolbar.moveAbove(control);
		for (Control c : control.getChildren()) {
			removeToolbar.moveAbove(c);
		}

		controlListener = new ControlListener() {

			@Override
			public void controlResized(final ControlEvent e) {
				final Point p = parent.getLocation();
				final Rectangle b = parent.getBounds();
				removeToolbar.setLocation(0 + b.width - removeToolbar.getBounds().width, 0);
			}

			@Override
			public void controlMoved(final ControlEvent e) {
				final Point p = parent.getLocation();
				final Rectangle b = parent.getBounds();
				removeToolbar.setLocation(0 + b.width - removeToolbar.getBounds().width, 0);

			}
		};
		parent.addControlListener(controlListener);
	}

	public void dispose() {
		parent.removeControlListener(controlListener);

	}
}
