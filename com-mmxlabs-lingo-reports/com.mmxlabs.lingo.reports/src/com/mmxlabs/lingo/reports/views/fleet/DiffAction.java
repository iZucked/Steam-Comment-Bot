/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.lingo.reports.components.ColumnHandler;


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