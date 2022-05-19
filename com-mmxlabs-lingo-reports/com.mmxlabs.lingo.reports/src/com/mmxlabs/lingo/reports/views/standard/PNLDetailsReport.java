/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.models.ui.properties.views.DetailPropertiesView;
import com.mmxlabs.rcp.common.application.TogglePinSelection;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class PNLDetailsReport extends DetailPropertiesView<PNLDetailsReportComponent> {

	private ISelectedDataProvider selectedDataProvider;

	private ScenarioComparisonService scenarioComparisonService;
	private final ISelectedScenariosServiceListener listener = new ISelectedScenariosServiceListener() {

		@Override
		public void selectedDataProviderChanged(@NonNull final ISelectedDataProvider selectedDataProvider, final boolean block) {
			PNLDetailsReport.this.selectedDataProvider = selectedDataProvider;
			component.rebuild(selectedDataProvider, block);
		}

		@Override
		public void selectedObjectChanged(@Nullable final MPart source, final ISelection selection) {
			component.rebuild(PNLDetailsReport.this.selectedDataProvider, false);
		}
	};

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

		scenarioComparisonService = getSite().getService(ScenarioComparisonService.class);

		super.createPartControl(parent);
		// Expand four levels by default
		expandLevel = 4;
		final GridTreeViewer viewer = component.getViewer();
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

		CommonImages.setImageDescriptors(collapseOneLevel, IconPaths.CollapseAll);
		CommonImages.setImageDescriptors(expandOneLevel, IconPaths.ExpandAll);

		final Action togglePinSelection = new Action("Pin selection", Action.AS_CHECK_BOX) {

			@Override
			public void run() {

				final Boolean newState = (Boolean) ContextInjectionFactory.invoke(component, TogglePinSelection.class, componentContext);
				if (newState != null) {
					setChecked(newState);
				}
			}
		};
		getViewSite().getActionBars().getToolBarManager().add(togglePinSelection);
		CommonImages.setImageDescriptors(togglePinSelection, IconPaths.Pin);

		getViewSite().getActionBars().getToolBarManager().add(collapseOneLevel);
		getViewSite().getActionBars().getToolBarManager().add(expandOneLevel);
		getViewSite().getActionBars().updateActionBars();

		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Reports_PNLDetails");

		// Initial selection
		scenarioComparisonService.addListener(listener);
		scenarioComparisonService.triggerListener(listener, false);
	}

	@Override
	public void dispose() {
		scenarioComparisonService.removeListener(listener);

		super.dispose();
	}
}
