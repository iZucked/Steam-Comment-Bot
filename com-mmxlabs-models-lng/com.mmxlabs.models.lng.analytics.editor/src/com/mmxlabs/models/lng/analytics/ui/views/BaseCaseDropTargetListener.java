/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Iterator;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
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
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.FullVesselCharterOption;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.VesselEventOption;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ShippingType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;

public class BaseCaseDropTargetListener implements DropTargetListener {

	private final @NonNull IScenarioEditingLocation scenarioEditingLocation;

	private OptionAnalysisModel optionAnalysisModel;

	private final @NonNull GridTreeViewer viewer;

	private final LocalMenuHelper menuHelper;

	public BaseCaseDropTargetListener(final @NonNull IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel optionAnalysisModel, @NonNull final GridTreeViewer viewer) {
		this.scenarioEditingLocation = scenarioEditingLocation;
		this.optionAnalysisModel = optionAnalysisModel;
		this.viewer = viewer;
		menuHelper = new LocalMenuHelper(scenarioEditingLocation.getShell());
		viewer.getControl().addDisposeListener(e -> menuHelper.dispose());
	}

	public BaseCaseDropTargetListener(final @NonNull IScenarioEditingLocation scenarioEditingLocation, @NonNull final GridTreeViewer viewer) {
		this(scenarioEditingLocation, null, viewer);
	}

	@Override
	public void dropAccept(final DropTargetEvent event) {
		if (scenarioEditingLocation.isLocked()) {
			event.detail = DND.DROP_NONE;
			return;
		}
		event.operations = DND.DROP_LINK;
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

			if (selection.size() == 1) {
				final Object o = selection.getFirstElement();

				BaseCaseRow existing = null;
				if (event.item instanceof GridItem) {
					final GridItem gridItem = (GridItem) event.item;

					final Object d = gridItem.getData();
					if (d instanceof BaseCaseRow) {
						existing = (BaseCaseRow) d;
					}
				}

				if (o instanceof VesselEventOption) {
					final VesselEventOption option = (VesselEventOption) o;
					if (existing != null) {
						CompoundCommand cmd = new CompoundCommand();

						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__VESSEL_EVENT_OPTION, option));
						if (existing.getBuyOption() != null) {
							cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION, SetCommand.UNSET_VALUE));
						}
						if (existing.getSellOption() != null) {
							cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION, SetCommand.UNSET_VALUE));
						}
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, existing, null);
					} else {
						final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
						final CompoundCommand cmd = new CompoundCommand();
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__VESSEL_EVENT_OPTION, option));
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, optionAnalysisModel, null);
					}
				} else if (o instanceof BuyOption) {
					final BuyOption buyOption = (BuyOption) o;
					if (existing != null) {

						CompoundCommand cmd = new CompoundCommand();
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION, buyOption));
						if (existing.getVesselEventOption() != null) {
							cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__VESSEL_EVENT_OPTION, SetCommand.UNSET_VALUE));
						}
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, existing, null);
					} else {
						final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
						final CompoundCommand cmd = new CompoundCommand();
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION, buyOption));
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, optionAnalysisModel, null);

					}
				} else if (o instanceof SellOption) {
					final SellOption sellOption = (SellOption) o;
					if (existing != null) {
						CompoundCommand cmd = new CompoundCommand();
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION, sellOption));
						if (existing.getVesselEventOption() != null) {
							cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__VESSEL_EVENT_OPTION, SetCommand.UNSET_VALUE));
						}
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, existing, null);
					} else {
						final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
						final CompoundCommand cmd = new CompoundCommand();
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION, sellOption));
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, optionAnalysisModel, null);
					}
				} else if (o instanceof Vessel) {
					final Vessel vessel = (Vessel) o;

					AnalyticsBuilder.applyShipping(scenarioEditingLocation, optionAnalysisModel, existing, vessel);

				} else if (o instanceof ShippingOption) {
					if (existing != null) {
						ShippingOption opt = null;
						if (AnalyticsBuilder.isNonShipped(existing) == ShippingType.NonShipped) {
							if (o instanceof NominatedShippingOption) {
								opt = (ShippingOption) o;
							}
						} else if (AnalyticsBuilder.isNonShipped(existing) == ShippingType.Shipped) {

							if (o instanceof RoundTripShippingOption || o instanceof SimpleVesselCharterOption
									|| o instanceof ExistingVesselCharterOption || o instanceof FullVesselCharterOption
									|| o instanceof ExistingCharterMarketOption) {
								opt = (ShippingOption) o;
							}
						}
						if (opt != null) {
							scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
									SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, opt), existing,
									AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
						}
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

							final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
							row.setVesselEventOption(option);

							AnalyticsBuilder.applyShipping(scenarioEditingLocation, optionAnalysisModel, row, vesselEvent, optionAnalysisModel.getBaseCase().isKeepExistingScenario(), this);

							appendAndExecute(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));
						}
					};
					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(c, null, null);
				} else if (o instanceof CargoModelRowTransformer.RowData) {
					final CargoModelRowTransformer.RowData rowData = (CargoModelRowTransformer.RowData) o;
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
							final LoadSlot loadSlot = rowData.getLoadSlot();
							final BuyOption buyRef = AnalyticsBuilder.getOrCreateBuyOption(loadSlot, optionAnalysisModel, scenarioEditingLocation, this);

							final DischargeSlot dischargeSlot = rowData.getDischargeSlot();
							final SellOption sellRef = AnalyticsBuilder.getOrCreateSellOption(dischargeSlot, optionAnalysisModel, scenarioEditingLocation, this);

							final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
							row.setBuyOption(buyRef);
							row.setSellOption(sellRef);

							AnalyticsBuilder.applyShipping(scenarioEditingLocation, optionAnalysisModel, row, rowData.getCargo(), loadSlot, dischargeSlot,
									optionAnalysisModel.getBaseCase().isKeepExistingScenario(), this);

							appendAndExecute(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));
						}
					};
					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(c, null, null);
				}
			} else if (selection.size() > 1) {

				BaseCaseRow existing = null;
				if (event.item instanceof GridItem) {
					final GridItem gridItem = (GridItem) event.item;

					final Object d = gridItem.getData();
					if (d instanceof BaseCaseRow) {
						existing = (BaseCaseRow) d;
					}
				}

				CompoundCommand cmd = new CompoundCommand();
				Iterator<?> itr = selection.iterator();
				while (itr.hasNext()) {
					Object o = itr.next();
					if (o instanceof CargoModelRowTransformer.RowData) {
						final CargoModelRowTransformer.RowData rowData = (CargoModelRowTransformer.RowData) o;

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
								final LoadSlot loadSlot = rowData.getLoadSlot();
								final BuyOption buyRef = AnalyticsBuilder.getOrCreateBuyOption(loadSlot, optionAnalysisModel, scenarioEditingLocation, this);

								final DischargeSlot dischargeSlot = rowData.getDischargeSlot();
								final SellOption sellRef = AnalyticsBuilder.getOrCreateSellOption(dischargeSlot, optionAnalysisModel, scenarioEditingLocation, this);

								final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
								row.setBuyOption(buyRef);
								row.setSellOption(sellRef);

								AnalyticsBuilder.applyShipping(scenarioEditingLocation, optionAnalysisModel, row, rowData.getCargo(), loadSlot, dischargeSlot,
										optionAnalysisModel.getBaseCase().isKeepExistingScenario(), this);

								appendAndExecute(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));
							}
						};
						cmd.append(c);

					}
				}
				if (!cmd.isEmpty()) {
					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, null, null);
				}
			}
		}
	}

	@Override
	public void dragOver(final DropTargetEvent event) {
		if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
			final IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().nativeToJava(event.currentDataType);
			if (selection.size() == 1) {
				final Object o = selection.getFirstElement();
				if (o instanceof BuyOption || o instanceof SellOption || o instanceof VesselEventOption) {
					event.detail = DND.DROP_LINK;
					return;
				}
				if (o instanceof Vessel) {
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
