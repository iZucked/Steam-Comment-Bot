package com.mmxlabs.models.lng.analytics.ui.views;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.widgets.Menu;

import com.google.common.collect.Sets;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;
public class OptionsTreeViewerContextMenuManager implements MenuDetectListener {

	private final @NonNull OptionModellerView optionModellerView;

	private final @NonNull MenuManager mgr;
	private Menu menu;

	private @NonNull GridTreeViewer viewer;

	public OptionsTreeViewerContextMenuManager(@NonNull final GridTreeViewer viewer, @NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final OptionModellerView optionModellerView, @NonNull final MenuManager mgr) {
		this.optionModellerView = optionModellerView;
		this.mgr = mgr;
		this.viewer = viewer;
	}

	@SuppressWarnings({ "unchecked", "null" })
	@Override
	public void menuDetected(final MenuDetectEvent e) {

		final Grid grid = viewer.getGrid();
		if (menu == null) {
			menu = mgr.createContextMenu(grid);
		}
		mgr.removeAll();

		final IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		final GridItem[] items = grid.getSelection();
		if (items.length > 0) {

			Object selectedElement = selection.getFirstElement();
			final OptionAnalysisModel optionAnalysisModel;
			if (selectedElement instanceof OptionAnalysisModel) {
				optionAnalysisModel = (OptionAnalysisModel) selectedElement;
			} else {
				optionAnalysisModel = null;
			}
			mgr.add(new RunnableAction("Rename", () -> {
				if (optionAnalysisModel != null) {
					String newForkName = ScenarioServiceModelUtils.openNewNameForForkPrompt(optionAnalysisModel.getName(), optionAnalysisModel.getName(), Sets.<String>newHashSet());
					optionAnalysisModel.setName(newForkName);
				}
				
			}));
			


		}
		menu.setVisible(true);
	}

}
