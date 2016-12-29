package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Collections;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
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
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ui.utils.OptionsModellerUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;

public class OptionsTreeViewerContextMenuManager implements MenuDetectListener {

	private final @NonNull OptionModellerView optionModellerView;

	private final @NonNull MenuManager mgr;
	private Menu menu;

	private @NonNull GridTreeViewer viewer;

	private @NonNull IScenarioEditingLocation scenarioEditingLocation;

	public OptionsTreeViewerContextMenuManager(@NonNull final GridTreeViewer viewer, @NonNull final IScenarioEditingLocation scenarioEditingLocation,
			@NonNull final OptionModellerView optionModellerView, @NonNull final MenuManager mgr) {
		this.scenarioEditingLocation = scenarioEditingLocation;
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
					String newForkName = ScenarioServiceModelUtils.openNewNameForForkPrompt(optionAnalysisModel.getName(), optionAnalysisModel.getName(), Sets.<String> newHashSet());
					optionAnalysisModel.setName(newForkName);
				}

			}));
			if (OptionsModellerUtils.getRootOptionsModel(optionAnalysisModel) != optionAnalysisModel) {
				mgr.add(new RunnableAction("Delete", () -> {
					OptionAnalysisModel container = (OptionAnalysisModel) optionAnalysisModel.eContainer();
					container.getChildren().remove(optionAnalysisModel);
					optionModellerView.setInput(container);
				}));
			}
			if (OptionsModellerUtils.getRootOptionsModel(optionAnalysisModel) != optionAnalysisModel) {
				mgr.add(new RunnableAction("Copy as top level sandbox", () -> {
					OptionAnalysisModel copy = EcoreUtil.copy(optionAnalysisModel);
					copy.getChildren().clear();

					final CompoundCommand cmd = new CompoundCommand("Create sandbox");
					cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), ScenarioModelUtil.getAnalyticsModel((LNGScenarioModel) scenarioEditingLocation.getRootObject()),
							AnalyticsPackage.Literals.ANALYTICS_MODEL__OPTION_MODELS, Collections.singletonList(copy)));
					scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
				}));
			}
		}
		menu.setVisible(true);
	}

}
