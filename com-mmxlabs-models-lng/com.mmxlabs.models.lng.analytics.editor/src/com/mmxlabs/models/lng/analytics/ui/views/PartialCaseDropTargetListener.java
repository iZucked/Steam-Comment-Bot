/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
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

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.VesselEventOption;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;

public class PartialCaseDropTargetListener implements DropTargetListener {

	private final @NonNull IScenarioEditingLocation scenarioEditingLocation;

	private OptionAnalysisModel optionAnalysisModel;

	private @NonNull final GridTreeViewer viewer;

	private final LocalMenuHelper menuHelper;

	public PartialCaseDropTargetListener(final @NonNull IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel optionAnalysisModel, @NonNull final GridTreeViewer viewer) {
		this.scenarioEditingLocation = scenarioEditingLocation;
		this.optionAnalysisModel = optionAnalysisModel;
		this.viewer = viewer;
		menuHelper = new LocalMenuHelper(scenarioEditingLocation.getShell());
		viewer.getControl().addDisposeListener(e -> menuHelper.dispose());
	}

	public PartialCaseDropTargetListener(final @NonNull IScenarioEditingLocation scenarioEditingLocation, @NonNull final GridTreeViewer viewer) {
		this(scenarioEditingLocation, null, viewer);
	}

	@Override
	public void dropAccept(final DropTargetEvent event) {
		if (scenarioEditingLocation.isLocked()) {
			event.detail = DND.DROP_NONE;
			return;
		}
	}

	@Override
	public void drop(final DropTargetEvent event) {
		if (scenarioEditingLocation.isLocked()) {
			event.detail = DND.DROP_NONE;
			return;
		}
		final OptionAnalysisModel optionAnalysisModel = this.optionAnalysisModel;
		if (optionAnalysisModel == null) {
			return;
		}
		if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
			final IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().nativeToJava(event.currentDataType);

			event.operations = DND.DROP_LINK;

			Object o = null;
			Collection<EObject> collection = null;
			if (selection.size() > 1) {
				final Object f = selection.getFirstElement();

				if (f instanceof BuyOption || f instanceof SellOption || f instanceof ShippingOption || f instanceof VesselEventOption) {
					collection = new LinkedList<>();
					final Iterator<?> itr = selection.iterator();
					while (itr.hasNext()) {
						final EObject next = (EObject) itr.next();
						if (o == null) {
							o = next;
						}
						collection.add(next);
					}
				}
			} else if (selection.size() == 1) {
				o = selection.getFirstElement();
				collection = Collections.singletonList((EObject) o);
			}

			if (o != null) {

				PartialCaseRow existing = null;
				if (event.item instanceof GridItem) {
					final GridItem gridItem = (GridItem) event.item;
					final Object d = gridItem.getData();
					if (d instanceof PartialCaseRow) {
						existing = (PartialCaseRow) d;
					}
				}

				if (o instanceof VesselEventOption) {
					final VesselEventOption option = (VesselEventOption) o;
					if (existing != null) {
						CompoundCommand cmd = new CompoundCommand();

						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__VESSEL_EVENT_OPTIONS, collection));

						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, existing, null);
					} else {
						final PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
						final CompoundCommand cmd = new CompoundCommand();
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, row));
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__VESSEL_EVENT_OPTIONS, option));
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, optionAnalysisModel, null);
					}
				} else if (o instanceof BuyOption) {
					final BuyOption buyOption = (BuyOption) o;
					if (existing != null) {
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS, collection), existing,
								AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS);
					} else {
						final PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
						final CompoundCommand cmd = new CompoundCommand();
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, row));
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS, collection));
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, optionAnalysisModel, null);

					}
				} else if (o instanceof SellOption) {
					final SellOption sellOption = (SellOption) o;
					if (existing != null) {
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS, collection), existing,
								AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS);
					} else {
						final PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
						final CompoundCommand cmd = new CompoundCommand();
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, row));
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS, collection));
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, optionAnalysisModel, null);
					}
				} else if (o instanceof PartialCaseRow) {
					if (o == existing) {
						event.operations = DND.DROP_LINK;
						if (existing.getBuyOptions().isEmpty() && existing.getSellOptions().size() == 1) {

							final SellOption option = existing.getSellOptions().get(0);
							final Port port = AnalyticsBuilder.getPort(option);
							final LocalDate date = AnalyticsBuilder.getDate(option);
							final boolean isShipped = AnalyticsBuilder.isShipped(option);

							final BuyOpportunity row = AnalyticsFactory.eINSTANCE.createBuyOpportunity();
							row.setDesPurchase(isShipped);
							row.setDate(date);
							row.setPort(port);
							row.setPriceExpression("?");

							AnalyticsBuilder.setDefaultEntity(scenarioEditingLocation, row);

							scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
									AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__BUYS, row),
									optionAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__BUYS);

							scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
									AddCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS, Collections.singletonList(row)),
									existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS);

							DetailCompositeDialogUtil.editSingleObject(scenarioEditingLocation, row);
						} else if (existing.getBuyOptions().size() == 1 && existing.getSellOptions().isEmpty()) {

							final BuyOption option = existing.getBuyOptions().get(0);
							final Port port = AnalyticsBuilder.getPort(option);
							final LocalDate date = AnalyticsBuilder.getDate(option);
							final boolean isShipped = AnalyticsBuilder.isShipped(option);

							final SellOpportunity row = AnalyticsFactory.eINSTANCE.createSellOpportunity();
							row.setFobSale(isShipped);
							row.setDate(date);
							row.setPort(port);
							row.setPriceExpression("?");

							AnalyticsBuilder.setDefaultEntity(scenarioEditingLocation, row);

							scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
									AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SELLS, row),
									optionAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SELLS);

							scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
									AddCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS, Collections.singletonList(row)),
									existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS);

							DetailCompositeDialogUtil.editSingleObject(scenarioEditingLocation, row);
						}
					}
				} else if (o instanceof Vessel) {
					final Vessel vessel = (Vessel) o;
					if (existing != null) {
						AnalyticsBuilder.applyShipping(scenarioEditingLocation, optionAnalysisModel, existing, vessel, menuHelper);
					}
				} else if (o instanceof ShippingOption) {
					if (existing != null) {
						// ShippingOption opt = null;
						// if (AnalyticsBuilder.isNonShipped(existing) == ShippingType.NonShipped) {
						// if (o instanceof NominatedShippingOption) {
						// opt = (ShippingOption) o;
						// }
						// } else if (AnalyticsBuilder.isNonShipped(existing) == ShippingType.Shipped) {
						//
						// if (o instanceof RoundTripShippingOption || o instanceof FleetShippingOption) {
						// opt = (ShippingOption) o;
						// }
						// }
						// if (opt != null) {
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING, collection), existing,
								AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
						// }
					}
				} else if (o instanceof VesselEvent) {
					VesselEvent vesselEvent = (VesselEvent) o;
					// Lazily evaluated command to avoid adding shipping multiple times.
					CompoundCommand c = new CompoundCommand() {
						@Override
						protected boolean prepare() {
							return true;
						}

						@Override
						public void append(Command command) {
							super.appendAndExecute(command);
						}

						@Override
						public void execute() {
							final VesselEventOption option = AnalyticsBuilder.getOrCreateVesselEventOption(vesselEvent, optionAnalysisModel, scenarioEditingLocation, this);

							final PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
							row.getVesselEventOptions().add(option);

							AnalyticsBuilder.applyShipping(scenarioEditingLocation, optionAnalysisModel, row, vesselEvent, optionAnalysisModel.getBaseCase().isKeepExistingScenario(), this);

							appendAndExecute(
									AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, row));
						}
					};
					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(c, null, null);
				} else if (o instanceof CargoModelRowTransformer.RowData) {
					final CargoModelRowTransformer.RowData rowData = (CargoModelRowTransformer.RowData) o;
					final CompoundCommand cmd = new CompoundCommand();

					final LoadSlot loadSlot = rowData.getLoadSlot();
					final BuyOption buyRef = AnalyticsBuilder.getOrCreateBuyOption(loadSlot, optionAnalysisModel, scenarioEditingLocation, cmd);

					final DischargeSlot dischargeSlot = rowData.getDischargeSlot();
					final SellOption sellRef = AnalyticsBuilder.getOrCreateSellOption(dischargeSlot, optionAnalysisModel, scenarioEditingLocation, cmd);

					final PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
					if (buyRef != null) {
						row.getBuyOptions().add(buyRef);
					}
					if (sellRef != null) {
						row.getSellOptions().add(sellRef);
					}

					AnalyticsBuilder.applyShipping(scenarioEditingLocation, optionAnalysisModel, row, rowData.getCargo(), loadSlot, dischargeSlot,
							optionAnalysisModel.getBaseCase().isKeepExistingScenario(), cmd);

					cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, row));

					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, null, null);

					return;
				}
			}
		}

	}

	@Override
	public void dragOver(final DropTargetEvent event) {
		if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
			final IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().nativeToJava(event.currentDataType);
			if (selection.size() > 0) {
				final Object o = selection.getFirstElement();
				if (o instanceof BuyOption || o instanceof SellOption|| o instanceof VesselEventOption) {
					event.detail = DND.DROP_LINK;
					return;
				}
				if (o instanceof Vessel) {
					event.detail = DND.DROP_LINK;
					return;
				}
				if (o instanceof PartialCaseRow) {
					event.detail = DND.DROP_LINK;
					return;
				}
				if (o instanceof ShippingOption) {
					event.detail = DND.DROP_LINK;
					return;
				}
				if (o instanceof CargoModelRowTransformer.RowData) {
					event.detail = DND.DROP_LINK;
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

	public OptionAnalysisModel getOptionAnalysisModel() {
		return optionAnalysisModel;
	}

	public void setOptionAnalysisModel(final OptionAnalysisModel optionAnalysisModel) {
		this.optionAnalysisModel = optionAnalysisModel;
	}

}
