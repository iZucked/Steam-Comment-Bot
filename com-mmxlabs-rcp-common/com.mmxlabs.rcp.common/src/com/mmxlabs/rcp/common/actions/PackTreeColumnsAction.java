/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeColumn;

import com.mmxlabs.rcp.common.CommonImages;
import com.mmxlabs.rcp.common.CommonImages.IconMode;
import com.mmxlabs.rcp.common.CommonImages.IconPaths;
import com.mmxlabs.rcp.common.internal.Activator;

public class PackTreeColumnsAction extends Action {

	private final TreeViewer viewer;

	public PackTreeColumnsAction(final TreeViewer viewer) {
		super("Pack Columns");
		setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Pack, IconMode.Enabled));
		this.viewer = viewer;
	}

	@Override
	public void run() {

		if (!viewer.getControl().isDisposed()) {
			final TreeColumn[] columns = viewer.getTree().getColumns();
			for (final TreeColumn c : columns) {
				c.pack();
			}
		}
	}
}
