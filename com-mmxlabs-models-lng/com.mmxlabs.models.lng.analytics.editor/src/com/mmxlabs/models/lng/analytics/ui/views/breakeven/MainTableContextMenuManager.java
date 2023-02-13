/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.breakeven;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisModel;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.actions.RunnableAction;

public class MainTableContextMenuManager implements MenuDetectListener {

	private final @NonNull GridTreeViewer viewer;
	private final @NonNull IScenarioEditingLocation scenarioEditingLocation;

	private final @NonNull MenuManager mgr;

	private Menu menu;
	private BreakEvenAnalysisModel optionAnalysisModel;

	public MainTableContextMenuManager(@NonNull final GridTreeViewer viewer, @NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final MenuManager mgr) {
		this.mgr = mgr;
		this.scenarioEditingLocation = scenarioEditingLocation;
		this.viewer = viewer;
	}

	@Override
	public void menuDetected(final MenuDetectEvent e) {

		final Grid grid = viewer.getGrid();
		if (menu == null) {
			menu = mgr.createContextMenu(grid);
		}
		mgr.removeAll();
		{
			IContributionItem[] items = mgr.getItems();
			mgr.removeAll();
			for (IContributionItem mItem : items) {
				mItem.dispose();
			}
		}
		if (scenarioEditingLocation.isLocked()) {
			return;
		}
		final Point mousePoint = grid.toControl(new Point(e.x, e.y));
		final GridColumn column = grid.getColumn(mousePoint);

		final IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		final GridItem[] items = grid.getSelection();
		if (items.length > 0) {
			mgr.add(new RunnableAction("Delete row(s)", () -> {
				final Collection<EObject> c = new LinkedList<>();
				selection.iterator().forEachRemaining(ee -> c.add((EObject) ee));

				scenarioEditingLocation.getDefaultCommandHandler().handleCommand(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), c), null, null);

			}));
		}
		menu.setVisible(true);

	}

	public BreakEvenAnalysisModel getOptionAnalysisModel() {
		return optionAnalysisModel;
	}

	public void setOptionAnalysisModel(final BreakEvenAnalysisModel optionAnalysisModel) {
		this.optionAnalysisModel = optionAnalysisModel;
	}
}
