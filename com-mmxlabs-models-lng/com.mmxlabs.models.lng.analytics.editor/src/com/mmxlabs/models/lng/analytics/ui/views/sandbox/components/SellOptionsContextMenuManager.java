/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.sandbox.components;

import java.time.YearMonth;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
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

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.AbstractAnalysisModel;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.VolumeMode;
import com.mmxlabs.models.lng.analytics.ui.views.ResultsSetDeletionHelper;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.ui.editorpart.PortPickerDialog;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
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

public class SellOptionsContextMenuManager implements MenuDetectListener {

	private final @NonNull GridTreeViewer viewer;
	private final @NonNull IScenarioEditingLocation scenarioEditingLocation;

	private final @NonNull MenuManager mgr;

	private AbstractAnalysisModel abstractAnalysisModel;

	private Menu menu;

	public SellOptionsContextMenuManager(@NonNull final GridTreeViewer viewer, @NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final MenuManager mgr) {
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
			final SellOption row = (SellOption) ed;
			if (row instanceof SellReference) {
				final SellReference sellReference = (SellReference) row;
				final DischargeSlot slot = sellReference.getSlot();
				if (slot != null) {
					mgr.add(new RunnableAction("Copy", CommonImages.getImageDescriptor(IconPaths.Copy, IconMode.Enabled), () -> {
						final SellOpportunity newSell = AnalyticsFactory.eINSTANCE.createSellOpportunity();
						newSell.setFobSale(slot.isFOBSale());
						newSell.setPort(slot.getPort());
						newSell.setDate(slot.getWindowStart());
						newSell.setSpecifyWindow(true);
						newSell.setWindowSize(slot.getSchedulingTimeWindow().getSize());
						newSell.setWindowSizeUnits(slot.getSchedulingTimeWindow().getSizeUnits());
						if (slot.isSetContract()) {
							newSell.setContract(slot.getContract());
						} else {
							newSell.setEntity(slot.getSlotOrDelegateEntity());
						}
						if (slot.isSetPriceExpression()) {
							newSell.setPriceExpression(slot.getPriceExpression());
						}

						newSell.setVolumeMode(VolumeMode.RANGE);
						newSell.setVolumeUnits(slot.getSlotOrDelegateVolumeLimitsUnit());
						newSell.setMinVolume(slot.getSlotOrDelegateMinQuantity());
						newSell.setMaxVolume(slot.getSlotOrDelegateMaxQuantity());

						newSell.setMiscCosts(slot.getMiscCosts());
						newSell.setCancellationExpression(slot.getCancellationExpression());

						final Command c = AddCommand.create(scenarioEditingLocation.getEditingDomain(), abstractAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SELLS, newSell);
						DetailCompositeDialogUtil.editNewObjectWithUndoOnCancel(scenarioEditingLocation, newSell, c);
					}));
				}
			}
			if (row instanceof final SellMarket sellMarket) {
				final SpotMarket spotMarket = sellMarket.getMarket();
				if (spotMarket != null) {
					mgr.add(new RunnableAction("Copy", CommonImages.getImageDescriptor(IconPaths.Copy, IconMode.Enabled), () -> {
						final SellOpportunity newSell = AnalyticsFactory.eINSTANCE.createSellOpportunity();
						if (spotMarket instanceof final FOBSalesMarket dpMarket) {
							newSell.setFobSale(true);
							final var ports = SetUtils.getObjects(dpMarket.getOriginPorts());
							// Make sure we have a list of valid ports
							ports.removeIf(p -> !p.getCapabilities().contains(PortCapability.LOAD));
							if (ports.size() == 1) {
								newSell.setPort(ports.iterator().next());

							}

							newSell.setEntity(dpMarket.getEntity());
							if (dpMarket.getPriceInfo() instanceof final ExpressionPriceParameters pp) {
								newSell.setPriceExpression(pp.getPriceExpression());
							} else if (dpMarket.getPriceInfo() instanceof final DateShiftExpressionPriceParameters pp) {
								newSell.setPriceExpression(pp.getPriceExpression());
							}
						} else if (spotMarket instanceof final DESSalesMarket fpMarket) {
							newSell.setFobSale(false);
							newSell.setPort(fpMarket.getNotionalPort());
							newSell.setEntity(fpMarket.getEntity());
							if (fpMarket.getPriceInfo() instanceof final ExpressionPriceParameters pp) {
								newSell.setPriceExpression(pp.getPriceExpression());
							} else if (fpMarket.getPriceInfo() instanceof final DateShiftExpressionPriceParameters pp) {
								newSell.setPriceExpression(pp.getPriceExpression());
							}
						}
						if (sellMarket.isSetMonth()) {
							final YearMonth ym = sellMarket.getMonth();
							if (ym != null) {
								newSell.setSpecifyWindow(true);
								newSell.setDate(ym.atDay(1));
								newSell.setWindowSize(1);
								newSell.setWindowSizeUnits(TimePeriod.MONTHS);
							}
						}

						newSell.setVolumeMode(VolumeMode.RANGE);
						newSell.setVolumeUnits(sellMarket.getMarket().getVolumeLimitsUnit());
						newSell.setMinVolume(sellMarket.getMarket().getMinQuantity());
						newSell.setMaxVolume(sellMarket.getMarket().getMaxQuantity());

						final Command c = AddCommand.create(scenarioEditingLocation.getEditingDomain(), abstractAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SELLS, newSell);
						DetailCompositeDialogUtil.editNewObjectWithUndoOnCancel(scenarioEditingLocation, newSell, c);
					}));
				}
			}
			if (row instanceof SellOpportunity) {
				mgr.add(new RunnableAction("Copy", CommonImages.getImageDescriptor(IconPaths.Copy, IconMode.Enabled), () -> {
					final SellOption copy = EMFCopier.copy(row);
					
					final Command c = AddCommand.create(scenarioEditingLocation.getEditingDomain(), abstractAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SELLS, copy);
					DetailCompositeDialogUtil.editNewObjectWithUndoOnCancel(scenarioEditingLocation, copy, c);
				}));
			}
		}

		if (items.length > 0) {
			mgr.add(new RunnableAction("Delete", CommonImages.getImageDescriptor(IconPaths.Delete, IconMode.Enabled), () -> {
				final Collection<EObject> c = new LinkedList<>();
				selection.iterator().forEachRemaining(ee -> c.add((EObject) ee));

				final CompoundCommand compoundCommand = new CompoundCommand("Delete sell option");
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

	public void setModel(final AbstractAnalysisModel optionAnalysisModel) {
		this.abstractAnalysisModel = optionAnalysisModel;
	}

}
