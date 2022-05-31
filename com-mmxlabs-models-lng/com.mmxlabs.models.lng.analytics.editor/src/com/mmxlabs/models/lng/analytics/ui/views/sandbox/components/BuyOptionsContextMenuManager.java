/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.sandbox.components;

import java.time.YearMonth;
import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.models.lng.analytics.AbstractAnalysisModel;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.VolumeMode;
import com.mmxlabs.models.lng.analytics.ui.views.ResultsSetDeletionHelper;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.common.ecore.EMFCopier;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class BuyOptionsContextMenuManager implements MenuDetectListener {

	private final @NonNull GridTreeViewer viewer;
	private final @NonNull IScenarioEditingLocation scenarioEditingLocation;

	private final @NonNull MenuManager mgr;

	private AbstractAnalysisModel abstractAnalysisModel;

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
			final IContributionItem[] items = mgr.getItems();
			mgr.removeAll();
			for (final IContributionItem mItem : items) {
				mItem.dispose();
			}
		}
		if (scenarioEditingLocation.isLocked()) {
			return;
		}
		final IStructuredSelection selection = viewer.getStructuredSelection();
		final GridItem[] items = grid.getSelection();

		if (items.length == 1) {

			final Object ed = items[0].getData();
			final BuyOption row = (BuyOption) ed;
			if (row instanceof final BuyReference buyReference) {
				final LoadSlot slot = buyReference.getSlot();
				if (slot != null) {
					mgr.add(new RunnableAction("Copy", CommonImages.getImageDescriptor(IconPaths.Copy, IconMode.Enabled), () -> {
						final BuyOpportunity newBuy = AnalyticsFactory.eINSTANCE.createBuyOpportunity();
						newBuy.setDesPurchase(slot.isDESPurchase());
						newBuy.setPort(slot.getPort());
						newBuy.setDate(slot.getWindowStart());
						newBuy.setSpecifyWindow(true);
						newBuy.setWindowSize(slot.getSchedulingTimeWindow().getSize());
						newBuy.setWindowSizeUnits(slot.getSchedulingTimeWindow().getSizeUnits());
						if (slot.isSetContract()) {
							newBuy.setContract(slot.getContract());
						} else {
							newBuy.setEntity(slot.getSlotOrDelegateEntity());
							newBuy.setCv(slot.getSlotOrDelegateCV());
						}
						if (slot.isSetCargoCV()) {
							newBuy.setCv(slot.getCargoCV());
						}
						if (slot.isSetPriceExpression()) {
							newBuy.setPriceExpression(slot.getPriceExpression());
						}
						newBuy.setVolumeMode(VolumeMode.RANGE);
						newBuy.setVolumeUnits(slot.getSlotOrDelegateVolumeLimitsUnit());
						newBuy.setMinVolume(slot.getSlotOrDelegateMinQuantity());
						newBuy.setMaxVolume(slot.getSlotOrDelegateMaxQuantity());

						newBuy.setMiscCosts(slot.getMiscCosts());
						newBuy.setCancellationExpression(slot.getCancellationExpression());

						final Command c = AddCommand.create(scenarioEditingLocation.getEditingDomain(), abstractAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__BUYS, newBuy);
						DetailCompositeDialogUtil.editNewObjectWithUndoOnCancel(scenarioEditingLocation, newBuy, c);
					}));
				}
			}
			if (row instanceof final BuyMarket buyMarket) {
				final SpotMarket spotMarket = buyMarket.getMarket();
				if (spotMarket != null) {
					mgr.add(new RunnableAction("Copy", CommonImages.getImageDescriptor(IconPaths.Copy, IconMode.Enabled), () -> {
						final BuyOpportunity newBuy = AnalyticsFactory.eINSTANCE.createBuyOpportunity();
						if (spotMarket instanceof final DESPurchaseMarket dpMarket) {
							newBuy.setDesPurchase(true);
							final var ports = SetUtils.getObjects(dpMarket.getDestinationPorts());
							// Make sure we have a list of valid ports
							ports.removeIf(p -> !p.getCapabilities().contains(PortCapability.DISCHARGE));
							if (ports.size() == 1) {
								newBuy.setPort(ports.iterator().next());
							}
							newBuy.setEntity(dpMarket.getEntity());
							newBuy.setCv(dpMarket.getCv());
							if (dpMarket.getPriceInfo() instanceof final ExpressionPriceParameters pp) {
								newBuy.setPriceExpression(pp.getPriceExpression());
							} else if (dpMarket.getPriceInfo() instanceof final DateShiftExpressionPriceParameters pp) {
								newBuy.setPriceExpression(pp.getPriceExpression());
							}
						} else if (spotMarket instanceof final FOBPurchasesMarket fpMarket) {
							newBuy.setDesPurchase(false);
							newBuy.setPort(fpMarket.getNotionalPort());
							newBuy.setEntity(fpMarket.getEntity());
							newBuy.setCv(fpMarket.getCv());
							if (fpMarket.getPriceInfo() instanceof final ExpressionPriceParameters pp) {
								newBuy.setPriceExpression(pp.getPriceExpression());
							} else if (fpMarket.getPriceInfo() instanceof final DateShiftExpressionPriceParameters pp) {
								newBuy.setPriceExpression(pp.getPriceExpression());
							}
						}
						if (buyMarket.isSetMonth()) {
							final YearMonth ym = buyMarket.getMonth();
							if (ym != null) {
								newBuy.setSpecifyWindow(true);
								newBuy.setDate(ym.atDay(1));
								newBuy.setWindowSize(1);
								newBuy.setWindowSizeUnits(TimePeriod.MONTHS);
							}
						}

						newBuy.setVolumeMode(VolumeMode.RANGE);
						newBuy.setVolumeUnits(buyMarket.getMarket().getVolumeLimitsUnit());
						newBuy.setMinVolume(buyMarket.getMarket().getMinQuantity());
						newBuy.setMaxVolume(buyMarket.getMarket().getMaxQuantity());

						final Command c = AddCommand.create(scenarioEditingLocation.getEditingDomain(), abstractAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__BUYS, newBuy);
						DetailCompositeDialogUtil.editNewObjectWithUndoOnCancel(scenarioEditingLocation, newBuy, c);
					}));
				}
			}
			if (row instanceof BuyOpportunity) {
				mgr.add(new RunnableAction("Copy", CommonImages.getImageDescriptor(IconPaths.Copy, IconMode.Enabled), () -> {
					final BuyOption copy = EMFCopier.copy(row);

					final Command c = AddCommand.create(scenarioEditingLocation.getEditingDomain(), abstractAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__BUYS, copy);
					DetailCompositeDialogUtil.editNewObjectWithUndoOnCancel(scenarioEditingLocation, copy, c);

				}));
			}
		}

		if (items.length > 0) {
			mgr.add(new RunnableAction("Delete", CommonImages.getImageDescriptor(IconPaths.Delete, IconMode.Enabled), () -> {
				final Collection<EObject> c = new LinkedList<>();
				selection.iterator().forEachRemaining(ee -> c.add((EObject) ee));
				final CompoundCommand compoundCommand = new CompoundCommand("Delete buy option");
				compoundCommand.append(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), c));
				if (abstractAnalysisModel instanceof final OptionAnalysisModel sandbox) {
					final Collection<EObject> linkedResults = ResultsSetDeletionHelper.getRelatedResultSets(c, sandbox);
					if (!linkedResults.isEmpty()) {
						compoundCommand.append(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), linkedResults));
					}
				}
				scenarioEditingLocation.getDefaultCommandHandler().handleCommand(compoundCommand, null, null);
			}));
		}

		menu.setVisible(true);
	}

	public AbstractAnalysisModel getModel() {
		return abstractAnalysisModel;
	}

	public void setModel(final AbstractAnalysisModel abstractAnalysisModel) {
		this.abstractAnalysisModel = abstractAnalysisModel;
	}

}
