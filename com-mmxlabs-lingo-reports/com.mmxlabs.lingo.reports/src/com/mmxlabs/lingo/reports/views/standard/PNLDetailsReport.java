/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import org.eclipse.jface.action.Action;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.models.ui.properties.views.DetailPropertiesView;

public class PNLDetailsReport extends DetailPropertiesView<PNLDetailsReportComponent> {

	private int expandLevel = 4;

	public PNLDetailsReport() {
		super("pnl", "com.mmxlabs.shiplingo.platform.reports.views.PNLDetailsReport", false);
	}

	@Override
	protected Class<PNLDetailsReportComponent> getComponentClass() {
		return PNLDetailsReportComponent.class;
	}

	@Override
	public void createPartControl(final Composite parent) {
		super.createPartControl(parent);
		// Expand four levels by default
		expandLevel = 4;
		GridTreeViewer viewer = component.getViewer();
		viewer.setAutoExpandLevel(expandLevel);

		final Action collapseOneLevel = new Action("Collapse All") {
			@Override
			public void run() {
				viewer.collapseAll();
				expandLevel = 1;
			}
		};
		final Action expandOneLevel = new Action("Expand one Level") {
			@Override
			public void run() {
				viewer.expandToLevel(++expandLevel);
			}
		};
		collapseOneLevel.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/collapseall.gif"));
		expandOneLevel.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/expandall.gif"));

		getViewSite().getActionBars().getToolBarManager().add(collapseOneLevel);
		getViewSite().getActionBars().getToolBarManager().add(expandOneLevel);
		getViewSite().getActionBars().updateActionBars();

		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Reports_PNLDetails");
	}

}
