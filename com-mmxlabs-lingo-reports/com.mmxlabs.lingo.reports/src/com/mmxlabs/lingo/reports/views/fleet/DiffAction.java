/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet;


import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;

import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class DiffAction extends Action {
	private final Viewer viewer;
	private final ConfigurableVesselSummaryReport reportView;
	
	public DiffAction(final Viewer viewer, ConfigurableVesselSummaryReport reportView) {
		super("Show only diff", SWT.TOGGLE);
		this.viewer = viewer;
		this.reportView = reportView;

		setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Delta, IconMode.Enabled));
		setDescription("Toggle diff rows");
		setToolTipText("Toggle diff rows");
		setChecked(reportView.isDiffMode());

	}

	@Override
	public void run() {
		boolean set = reportView.toggleDiffMode();
		setChecked(set);
		
		reportView.triggerSeletedScenariosServiceListener();
		viewer.refresh();
	}
}