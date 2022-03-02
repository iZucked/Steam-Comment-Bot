/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.sandbox.components;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.models.lng.analytics.AbstractAnalysisModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.ResultsSetDeletionHelper;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.common.ecore.EMFCopier;

public class ShippingOptionsContextMenuManager implements MenuDetectListener {

	private final @NonNull GridTreeViewer viewer;
	private final @NonNull IScenarioEditingLocation scenarioEditingLocation;

	private final @NonNull MenuManager mgr;

	private AbstractAnalysisModel abstractAnalysisModel;

	private Menu menu;

	public ShippingOptionsContextMenuManager(@NonNull final GridTreeViewer viewer, @NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final MenuManager mgr) {
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
			for (IContributionItem item : items) {
				item.dispose();
			}
		}
		
		if (scenarioEditingLocation.isLocked()) {
			return;
		}
		
		final IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		final GridItem[] items = grid.getSelection();
		if (items.length > 0) {
			mgr.add(new RunnableAction("Delete", () -> {
				final Collection<EObject> c = new LinkedHashSet<>();
				selection.iterator().forEachRemaining(ee -> c.add((EObject) ee));
				final CompoundCommand compoundCommand = new CompoundCommand("Delete shipping option");
				if (abstractAnalysisModel instanceof OptionAnalysisModel) {
					final Collection<EObject> linkedResults = ResultsSetDeletionHelper.getRelatedResultSets(c, (OptionAnalysisModel) abstractAnalysisModel);
					c.addAll(linkedResults);
				}
				if (!c.isEmpty()) {
					compoundCommand.append(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), c));
					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(compoundCommand, null, null);
				}
			}));
		}

		if (items.length == 1) {

			final Object ed = items[0].getData();
			final ShippingOption row = (ShippingOption) ed;

			if (row instanceof SellOpportunity) {
				mgr.add(new RunnableAction("Copy", () -> {
					final ShippingOption copy = EMFCopier.copy(row);
					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
							AddCommand.create(scenarioEditingLocation.getEditingDomain(), abstractAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES, copy),
							abstractAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES);
					DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(copy));
				}));
			}
		}
		menu.setVisible(true);
	}

	public AbstractAnalysisModel getModel() {
		return abstractAnalysisModel;
	}

	public void setModel(final AbstractAnalysisModel optionAnalysisModel) {
		this.abstractAnalysisModel = optionAnalysisModel;
	}

}
