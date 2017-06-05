/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
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

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.rcp.common.actions.RunnableAction;

public class BuyOptionsContextMenuManager implements MenuDetectListener {

	private final @NonNull GridTreeViewer viewer;
	private final @NonNull IScenarioEditingLocation scenarioEditingLocation;

	private final @NonNull MenuManager mgr;

	private OptionAnalysisModel optionAnalysisModel;

	private Menu menu;

	public BuyOptionsContextMenuManager(@NonNull final GridTreeViewer viewer, @NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final MenuManager mgr) {
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

		final IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		final GridItem[] items = grid.getSelection();
		if (items.length > 0) {
			mgr.add(new RunnableAction("Delete option(s)", () -> {
				final Collection<EObject> c = new LinkedList<>();
				selection.iterator().forEachRemaining(ee -> c.add((EObject) ee));
				final Collection<EObject> linkedResults = ResultsSetDeletionHelper.getRelatedResultSets(c, optionAnalysisModel);
				final CompoundCommand compoundCommand = new CompoundCommand("Delete buy option");
				compoundCommand.append(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), c));
				if (!linkedResults.isEmpty()) {
					compoundCommand.append(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), linkedResults));
				}
				scenarioEditingLocation.getDefaultCommandHandler().handleCommand(compoundCommand, null, null);
			}));
		}

		if (items.length == 1) {

			final Object ed = items[0].getData();
			final BuyOption row = (BuyOption) ed;
			if (row instanceof BuyReference) {
				final BuyReference buyReference = (BuyReference) row;
				final LoadSlot slot = buyReference.getSlot();
				if (slot != null) {
					mgr.add(new RunnableAction("Copy", () -> {
						final BuyOpportunity newBuy = AnalyticsFactory.eINSTANCE.createBuyOpportunity();
						newBuy.setDesPurchase(slot.isDESPurchase());
						newBuy.setPort(slot.getPort());
						newBuy.setDate(slot.getWindowStart());
						if (slot.isSetContract()) {
							newBuy.setContract((PurchaseContract) slot.getContract());
						} else {
							newBuy.setEntity(slot.getSlotOrDelegatedEntity());
							newBuy.setCv(slot.getSlotOrDelegatedCV());
						}
						if (slot.isSetCargoCV()) {
							newBuy.setCv(slot.getCargoCV());
						}
						if (slot.isSetPriceExpression()) {
							newBuy.setPriceExpression(slot.getPriceExpression());
						}
						newBuy.setMiscCosts(slot.getMiscCosts());
						newBuy.setCancellationExpression(slot.getCancellationExpression());

						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(

								AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS, newBuy), optionAnalysisModel,
								AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS);
					}));
				}
			}
			if (row instanceof BuyOpportunity) {
				mgr.add(new RunnableAction("Copy", () -> {
					final BuyOption copy = EcoreUtil.copy(row);
					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
							AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS, copy), optionAnalysisModel,
							AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS);
					DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(copy));
				}));
			}
		}
		menu.setVisible(true);
	}

	public OptionAnalysisModel getOptionAnalysisModel() {
		return optionAnalysisModel;
	}

	public void setOptionAnalysisModel(final OptionAnalysisModel optionAnalysisModel) {
		this.optionAnalysisModel = optionAnalysisModel;
	}

}
