/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.breakeven;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisModel;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResult;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;

public class MainTableDropTargetListener implements DropTargetListener {

	private final @NonNull IScenarioEditingLocation scenarioEditingLocation;

	private BreakEvenAnalysisModel breakEvenAnalysisModel;

	private @NonNull final GridTreeViewer viewer;

	private final LocalMenuHelper menuHelper;

	private Runnable refreshColumnsCallback;

	public MainTableDropTargetListener(final @NonNull IScenarioEditingLocation scenarioEditingLocation, final BreakEvenAnalysisModel breakEvenAnalysisModel, @NonNull final GridTreeViewer viewer,
			Runnable refreshColumnsCallback) {
		this.scenarioEditingLocation = scenarioEditingLocation;
		this.breakEvenAnalysisModel = breakEvenAnalysisModel;
		this.viewer = viewer;
		this.refreshColumnsCallback = refreshColumnsCallback;
		menuHelper = new LocalMenuHelper(scenarioEditingLocation.getShell());
		viewer.getControl().addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(final DisposeEvent e) {
				menuHelper.dispose();
			}
		});
	}

	public MainTableDropTargetListener(final @NonNull IScenarioEditingLocation scenarioEditingLocation, @NonNull final GridTreeViewer viewer, Runnable refreshColumnsCallback) {
		this(scenarioEditingLocation, null, viewer, refreshColumnsCallback);
	}

	@Override
	public void dropAccept(final DropTargetEvent event) {

	}

	@Override
	public void drop(final DropTargetEvent event) {
		final BreakEvenAnalysisModel breakEvenAnalysisModel = this.breakEvenAnalysisModel;
		if (breakEvenAnalysisModel == null) {
			return;
		}
		if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
			final IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().nativeToJava(event.currentDataType);
			Object o = null;
			Collection<EObject> collection = null;
			if (selection.size() == 1) {
				o = selection.getFirstElement();
				if (o instanceof BreakEvenAnalysisResult) {
					collection = Collections.singletonList((EObject) o);
				}
			}

			if (o != null) {

				BreakEvenAnalysisRow existing = null;

				GridItem gridItem = viewer.getGrid().getItem(viewer.getGrid().toControl(event.x, event.y));
				if (gridItem != null) {
					final Object d = gridItem.getData();
					if (d instanceof BreakEvenAnalysisRow) {
						existing = (BreakEvenAnalysisRow) d;
					}
				}

				if (o instanceof SpotMarket) {
					if (!breakEvenAnalysisModel.getMarkets().contains(o)) {
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(AddCommand.create(scenarioEditingLocation.getEditingDomain(), breakEvenAnalysisModel,
								AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_MODEL__MARKETS, Collections.singleton(o)), breakEvenAnalysisModel,
								AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_MODEL__MARKETS);
						refreshColumnsCallback.run();
					}
				} else if (o instanceof BreakEvenAnalysisResult) {
					BreakEvenAnalysisResult breakEvenAnalysisResult = (BreakEvenAnalysisResult) o;
					if (existing != null) {
						CompoundCommand cmd = new CompoundCommand();
						boolean buyMarketSide = true;
						if (breakEvenAnalysisResult.getTarget() instanceof SpotMarket) {
							SpotMarket spotMarket = (SpotMarket) breakEvenAnalysisResult.getTarget();
							if (spotMarket instanceof FOBPurchasesMarket || spotMarket instanceof DESPurchaseMarket) {
								buyMarketSide = true;
							} else {
								buyMarketSide = false;
							}

						}
						EStructuralFeature feature = buyMarketSide ? AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__LHS_BASED_ON : AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__RHS_BASED_ON;
						for (BreakEvenAnalysisRow row : breakEvenAnalysisModel.getRows()) {
							if (row == existing) {
								cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, feature, breakEvenAnalysisResult));
							} else {
								cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, feature, SetCommand.UNSET_VALUE));
							}
						}
						if (!cmd.isEmpty()) {
							scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, breakEvenAnalysisModel, null);
							// viewer.refresh();
						}
					}
				}
				if (o instanceof BuyOption) {
					final BuyOption buyOption = (BuyOption) o;
					if (existing != null && existing.getSellOption() == null) {
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__BUY_OPTION, o), existing,
								AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__BUY_OPTION);
					} else {
						final BreakEvenAnalysisRow row = AnalyticsFactory.eINSTANCE.createBreakEvenAnalysisRow();
						final CompoundCommand cmd = new CompoundCommand();
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), breakEvenAnalysisModel, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_MODEL__ROWS, row));
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__BUY_OPTION, o));
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, breakEvenAnalysisModel, null);

					}
				} else if (o instanceof SellOption) {
					final SellOption sellOption = (SellOption) o;
					if (existing != null && existing.getBuyOption() == null) {
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__SELL_OPTION, o), existing,
								AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__SELL_OPTION);
					} else {
						final BreakEvenAnalysisRow row = AnalyticsFactory.eINSTANCE.createBreakEvenAnalysisRow();
						final CompoundCommand cmd = new CompoundCommand();
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), breakEvenAnalysisModel, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_MODEL__ROWS, row));
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__SELL_OPTION, o));
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, breakEvenAnalysisModel, null);
					}
				}
				// else if (o instanceof PartialCaseRow) {
				// if (o == existing) {
				// event.operations = DND.DROP_LINK;
				// if (existing.getBuyOptions().isEmpty() && existing.getSellOptions().size() == 1) {
				//
				// final SellOption option = existing.getSellOptions().get(0);
				// final Port port = AnalyticsBuilder.getPort(option);
				// final LocalDate date = AnalyticsBuilder.getDate(option);
				// final boolean isShipped = AnalyticsBuilder.isShipped(option);
				//
				// final BuyOpportunity row = AnalyticsFactory.eINSTANCE.createBuyOpportunity();
				// row.setDesPurchase(isShipped);
				// row.setDate(date);
				// row.setPort(port);
				// row.setPriceExpression("?");
				//
				// AnalyticsBuilder.setDefaultEntity(scenarioEditingLocation, row);
				//
				// scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
				// AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS, row), optionAnalysisModel,
				// AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS);
				//
				// scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
				// AddCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS, Collections.singletonList(row)),
				// existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS);
				//
				// DetailCompositeDialogUtil.editSingleObject(scenarioEditingLocation, row);
				// } else if (existing.getBuyOptions().size() == 1 && existing.getSellOptions().isEmpty()) {
				//
				// final BuyOption option = existing.getBuyOptions().get(0);
				// final Port port = AnalyticsBuilder.getPort(option);
				// final LocalDate date = AnalyticsBuilder.getDate(option);
				// final boolean isShipped = AnalyticsBuilder.isShipped(option);
				//
				// final SellOpportunity row = AnalyticsFactory.eINSTANCE.createSellOpportunity();
				// row.setFobSale(isShipped);
				// row.setDate(date);
				// row.setPort(port);
				// row.setPriceExpression("?");
				//
				// AnalyticsBuilder.setDefaultEntity(scenarioEditingLocation, row);
				//
				// scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
				// AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SELLS, row),
				// optionAnalysisModel, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SELLS);
				//
				// scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
				// AddCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS, Collections.singletonList(row)),
				// existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS);
				//
				// DetailCompositeDialogUtil.editSingleObject(scenarioEditingLocation, row);
				// }
				// }
				// } else if (o instanceof Vessel) {
				// final Vessel vessel = (Vessel) o;
				//
				// if (existing != null) {
				// AnalyticsBuilder.applyShipping(scenarioEditingLocation, optionAnalysisModel, existing, vessel, menuHelper);
				//
				// // final ShippingType shippingType = AnalyticsBuilder.isNonShipped(existing);
				// // if (shippingType == ShippingType.NonShipped) {
				// // NominatedShippingOption opt = AnalyticsBuilder.getOrCreatNominatedShippingOption(optionAnalysisModel, vessel);
				// // if (opt.eContainer() == null) {
				// // scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
				// // AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt),
				// // optionAnalysisModel, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES);
				// // }
				// // scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
				// // SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING, opt), existing,
				// // AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
				// // } else if (shippingType == ShippingType.Shipped) {
				// // // final PartialCaseRow pExisting = existing;
				// // // menuHelper.clearActions();
				// // // menuHelper.addAction(new RunnableAction("Create RT", () -> {
				// // // final RoundTripShippingOption opt = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();
				// // // opt.setVesselClass(vessel.getVesselClass());
				// // // scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
				// // // SetCommand.create(scenarioEditingLocation.getEditingDomain(), pExisting, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING, opt), pExisting,
				// // // AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
				// // // DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(opt));
				// // // }));
				// // // menuHelper.addAction(new RunnableAction("Create fleet", () -> {
				// // // final FleetShippingOption opt = AnalyticsFactory.eINSTANCE.createFleetShippingOption();
				// // // AnalyticsBuilder.setDefaultEntity(scenarioEditingLocation, opt);
				// // // opt.setVessel(vessel);
				// // // scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
				// // // SetCommand.create(scenarioEditingLocation.getEditingDomain(), pExisting, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING, opt), pExisting,
				// // // AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
				// // // DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(opt));
				// // // }));
				// // //
				// // // menuHelper.open();
				// // }
				// }
				// } else if (o instanceof VesselClass) {
				// final VesselClass vesselClass = (VesselClass) o;
				// AnalyticsBuilder.applyShipping(scenarioEditingLocation, optionAnalysisModel, existing, vesselClass, menuHelper);
				//
				// // final PartialCaseRow pExisting = existing;
				// // final RoundTripShippingOption opt = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();
				// // opt.setVesselClass((VesselClass) o);
				// // if (pExisting != null) {
				// // final ShippingType shippingType = AnalyticsBuilder.isNonShipped(pExisting);
				// // if (shippingType == ShippingType.Shipped) {
				// // scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
				// // SetCommand.create(scenarioEditingLocation.getEditingDomain(), pExisting, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING, opt), pExisting,
				// // AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
				// // DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(opt));
				// // }
				// // }
				// }
				else if (o instanceof ShippingOption) {
					if (existing != null) {
						ShippingOption opt = null;
						if (o instanceof RoundTripShippingOption || o instanceof SimpleVesselCharterOption) {
							opt = (ShippingOption) o;
						}
						// Ignore for non-shipped rows
						if (existing.getBuyOption() != null) {
							if (!AnalyticsBuilder.isShipped(existing.getBuyOption())) {
								opt = null;
							}
						} else if (existing.getSellOption() != null) {
							if (!AnalyticsBuilder.isShipped(existing.getSellOption())) {
								opt = null;
							}
						}
						// if (AnalyticsBuilder.isNonShipped(existing) == ShippingType.NonShipped) {
						// if (o instanceof NominatedShippingOption) {
						// opt = (ShippingOption) o;
						// }
						// } else if (AnalyticsBuilder.isNonShipped(existing) == ShippingType.Shipped) {
						// }
						if (opt != null) {
							scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
									SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__SHIPPING, opt), existing,
									AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__SHIPPING);
							// }
						}
					}
				}

				// else if (o instanceof CargoModelRowTransformer.RowData) {
				// final CargoModelRowTransformer.RowData rowData = (CargoModelRowTransformer.RowData) o;
				// final CompoundCommand cmd = new CompoundCommand();
				//
				// final LoadSlot loadSlot = rowData.getLoadSlot();
				// final BuyOption buyRef = AnalyticsBuilder.getOrCreateBuyOption(loadSlot, optionAnalysisModel, scenarioEditingLocation, cmd);
				//
				// final DischargeSlot dischargeSlot = rowData.getDischargeSlot();
				// final SellOption sellRef = AnalyticsBuilder.getOrCreateSellOption(dischargeSlot, optionAnalysisModel, scenarioEditingLocation, cmd);
				//
				// final PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
				// if (buyRef != null) {
				// row.getBuyOptions().add(buyRef);
				// }
				// if (sellRef != null) {
				// row.getSellOptions().add(sellRef);
				// }
				//
				// AnalyticsBuilder.applyShipping(scenarioEditingLocation, optionAnalysisModel, row, rowData.getCargo(), loadSlot, dischargeSlot, cmd);
				//
				// cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, row));
				//
				// scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, null, null);
				//
				// return;
				// }
			}

		}

	}

	@Override
	public void dragOver(final DropTargetEvent event) {
		if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
			final IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().nativeToJava(event.currentDataType);
			if (selection.size() > 0) {
				final Object o = selection.getFirstElement();
				if (o instanceof BuyOption || o instanceof SellOption) {
					event.operations = DND.DROP_MOVE;
					return;
				}
				if (o instanceof Vessel) {
					event.operations = DND.DROP_MOVE;
					return;
				}
				if (o instanceof BreakEvenAnalysisResult) {
					event.operations = DND.DROP_LINK;
					return;
				}
				if (o instanceof ShippingOption) {
					event.operations = DND.DROP_MOVE;
					return;
				}
				if (o instanceof CargoModelRowTransformer.RowData) {
					event.operations = DND.DROP_MOVE;
					return;
				}
			}
		}
		event.operations = DND.DROP_NONE;
	}

	@Override
	public void dragOperationChanged(final DropTargetEvent event) {

	}

	@Override
	public void dragLeave(final DropTargetEvent event) {

	}

	@Override
	public void dragEnter(final DropTargetEvent event) {

	}

	public BreakEvenAnalysisModel getBreakEvenAnalysisModel() {
		return breakEvenAnalysisModel;
	}

	public void setBreakEvenAnalysisModel(final BreakEvenAnalysisModel breakEvenAnalysisModel) {
		this.breakEvenAnalysisModel = breakEvenAnalysisModel;
	}

}
