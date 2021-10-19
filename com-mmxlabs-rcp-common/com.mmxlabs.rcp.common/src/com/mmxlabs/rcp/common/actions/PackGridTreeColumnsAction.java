/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.GridColumn;

import com.mmxlabs.rcp.common.CommonImages;
import com.mmxlabs.rcp.common.CommonImages.IconMode;
import com.mmxlabs.rcp.common.CommonImages.IconPaths;
import com.mmxlabs.rcp.common.internal.Activator;

/**
 */
public class PackGridTreeColumnsAction extends Action {

	private final GridTreeViewer viewer;

	public PackGridTreeColumnsAction(final GridTreeViewer viewer) {
		super("Pack Columns");
		setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Pack, IconMode.Enabled));
		this.viewer = viewer;
	}

	@Override
	public void run() {

		if (!viewer.getControl().isDisposed()) {
			final GridColumn[] columns = viewer.getGrid().getColumns();
			for (final GridColumn c : columns) {
				if (c.getResizeable()) {
					c.pack();
					c.setWidth(c.getWidth() + 8);
				}
			}
		}
	}
}
