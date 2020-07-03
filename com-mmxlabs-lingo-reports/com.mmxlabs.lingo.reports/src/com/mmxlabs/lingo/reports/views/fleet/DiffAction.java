/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet;


import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.Viewer;

public class DiffAction extends Action {
	private final Viewer viewer;
	private final ConfigurableFleetReportView reportView;
	
	public DiffAction(final Viewer viewer, ConfigurableFleetReportView reportView) {
		super("Show only diff");
		this.viewer = viewer;
		this.reportView = reportView;

		setText("Δ");
		setDescription("Toggle diff rows");
		setToolTipText("Toggle diff rows");
	}

	@Override
	public void run() {
		reportView.toggleDiffMode();
		reportView.triggerSeletedScenariosServiceListener();
		viewer.refresh();
	}
}