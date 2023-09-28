/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BaseCaseRowGroup;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.FullVesselCharterOption;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.OpenBuy;
import com.mmxlabs.models.lng.analytics.OpenSell;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.VesselEventOption;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ShippingType;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

public class BaseCaseDropTargetListener extends AbstractDropTargetListener {

	// DROP_MOVE == Replace element in row or create a new row
	// DROP_COPY == Create a new LDD row

	private final boolean allowLDD = LicenseFeatures.isPermitted(KnownFeatures.FEATURE_COMPLEX_CARGO);

	public BaseCaseDropTargetListener(final @NonNull IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel optionAnalysisModel, @NonNull final GridTreeViewer viewer) {
		super(scenarioEditingLocation, optionAnalysisModel, viewer);
	}

	public BaseCaseDropTargetListener(final @NonNull IScenarioEditingLocation scenarioEditingLocation, @NonNull final GridTreeViewer viewer) {
		super(scenarioEditingLocation, null, viewer);
	}

	@Override
	protected void doDropAccept(final DropTargetEvent event) {

		if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
			final IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().nativeToJava(event.currentDataType);

			if (selection.size() == 1) {
				final Object o = selection.getFirstElement();
				if (o instanceof BuyOption || o instanceof SellOption) {
					if (event.item instanceof final GridItem gridItem) {
						final Object d = gridItem.getData();
						if (d instanceof BaseCaseRow) {
							event.operations = DND.DROP_MOVE;
							if (allowLDD && o instanceof SellOption) {
								event.operations |= DND.DROP_COPY;
							}
							return;
						}
					}
				}
			}
		}
		event.operations = DND.DROP_MOVE;
	}

	@Override
	protected void doDrop(final DropTargetEvent event) {

		if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
			final IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().nativeToJava(event.currentDataType);

			if (selection.size() == 1) {
				final Object o = selection.getFirstElement();

				BaseCaseRow existing = null;
				if (event.item instanceof final GridItem gridItem) {

					final Object d = gridItem.getData();
					if (d instanceof final BaseCaseRow row) {
						existing = row;
					}
				}

				if (o instanceof final VesselEventOption option) {
					if (existing != null) {
						final CompoundCommand cmd = new CompoundCommand();

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
						final BaseCaseRowGroup grp = AnalyticsFactory.eINSTANCE.createBaseCaseRowGroup();
						row.setGroup(grp);
						final CompoundCommand cmd = new CompoundCommand();
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__GROUPS, grp));
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__VESSEL_EVENT_OPTION, option));
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, optionAnalysisModel, null);
					}
				} else if (o instanceof final BuyOption buyOption) {
					if (existing != null) {
						final CompoundCommand cmd = new CompoundCommand();

						if (event.detail == DND.DROP_COPY) {
							final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
							row.setBuyOption(buyOption);
							final BaseCaseRowGroup grp = existing.getGroup();
							cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));
							cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__GROUP, grp));
						} else if (event.detail == DND.DROP_MOVE) {
							cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION, buyOption));
							if (existing.getVesselEventOption() != null) {
								cmd.append(
										SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__VESSEL_EVENT_OPTION, SetCommand.UNSET_VALUE));
							}
						}
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, existing, null);
					} else if (event.detail == DND.DROP_MOVE) {
						final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
						final BaseCaseRowGroup grp = AnalyticsFactory.eINSTANCE.createBaseCaseRowGroup();
						row.setGroup(grp);
						final CompoundCommand cmd = new CompoundCommand();
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__GROUPS, grp));

						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION, buyOption));
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, optionAnalysisModel, null);

					}
				} else if (o instanceof final SellOption sellOption) {
					if (existing != null) {
						final CompoundCommand cmd = new CompoundCommand();

						if (event.detail == DND.DROP_COPY) {
							// LDD
							final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
							row.setSellOption(sellOption);
							final BaseCaseRowGroup grp = existing.getGroup();
							cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));
							cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__GROUP, grp));
						} else if (event.detail == DND.DROP_MOVE) {
							// replace options
							cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION, sellOption));
							if (existing.getVesselEventOption() != null) {
								cmd.append(
										SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__VESSEL_EVENT_OPTION, SetCommand.UNSET_VALUE));
							}
						}
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, existing, null);
					} else if (event.detail == DND.DROP_MOVE) {
						final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
						final BaseCaseRowGroup grp = AnalyticsFactory.eINSTANCE.createBaseCaseRowGroup();
						row.setGroup(grp);
						final CompoundCommand cmd = new CompoundCommand();
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__GROUPS, grp));
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION, sellOption));
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, optionAnalysisModel, null);
					}
				} else if (o instanceof final Vessel vessel) {

					AnalyticsBuilder.applyShipping(scenarioEditingLocation, optionAnalysisModel, existing, vessel);

				} else if (o instanceof ShippingOption option) {
					if (existing != null) {
						ShippingOption opt = null;
						if (AnalyticsBuilder.isNonShipped(existing) == ShippingType.NonShipped) {
							if (o instanceof NominatedShippingOption) {
								opt = (ShippingOption) o;
							}
						} else if (AnalyticsBuilder.isNonShipped(existing) == ShippingType.Shipped) {

							if (o instanceof RoundTripShippingOption || o instanceof SimpleVesselCharterOption || o instanceof ExistingVesselCharterOption || o instanceof FullVesselCharterOption
									|| o instanceof ExistingCharterMarketOption) {
								opt = (ShippingOption) o;
							}
						} else if (existing.getVesselEventOption() instanceof VesselEventOption) {
							opt = (ShippingOption) o;
						} else {
							opt = (ShippingOption) o;
						}
						if (opt != null) {
							scenarioEditingLocation.getDefaultCommandHandler()
									.handleCommand(SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, opt), existing,
											AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
						}
					} else {
						final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
						final CompoundCommand cmd = new CompoundCommand();
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, option));
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, optionAnalysisModel, null);
					}
				} else if (o instanceof final VesselEvent vesselEvent) {
					// Lazily evaluated command to avoid adding shipping multiple times.
					final CompoundCommand c = new CompoundCommand() {
						@Override
						protected boolean prepare() {
							return true;
						}

						@Override
						public void append(final Command command) {
							super.appendAndExecute(command);
						}

						@Override
						public void execute() {
							final VesselEventOption option = AnalyticsBuilder.getOrCreateVesselEventOption(vesselEvent, optionAnalysisModel, scenarioEditingLocation, this);

							final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
							final BaseCaseRowGroup grp = AnalyticsFactory.eINSTANCE.createBaseCaseRowGroup();
							row.setGroup(grp);
							row.setVesselEventOption(option);

							AnalyticsBuilder.applyShipping(scenarioEditingLocation, optionAnalysisModel, row, vesselEvent, optionAnalysisModel.getBaseCase().isKeepExistingScenario(), this);

							appendAndExecute(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));
							appendAndExecute(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__GROUPS, grp));
						}
					};
					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(c, null, null);
				} else if (o instanceof final TradesRow sourceTradesRow) {
					// Lazily evaluated command to avoid adding shipping multiple times.
					final CompoundCommand c = new CompoundCommand() {
						@Override
						protected boolean prepare() {
							return true;
						}

						@Override
						public void append(final Command command) {
							super.appendAndExecute(command);
						}

						@Override
						public void execute() {

							Collection<Cargo> cargoes = new LinkedHashSet<>();
							Collection<LoadSlot> loads = new LinkedHashSet<>();
							Collection<DischargeSlot> discharges = new LinkedHashSet<>();
							final Iterator<?> itr = selection.iterator();

							while (itr.hasNext()) {
								final Object o = itr.next();
								if (o instanceof final TradesRow sourceTradesRow) {
									cargoes.add(sourceTradesRow.getCargo());
									loads.add(sourceTradesRow.getLoadSlot());
									discharges.add(sourceTradesRow.getDischargeSlot());
								}
							}
							loads.remove(null);
							discharges.remove(null);

							loads.forEach(l -> cargoes.add(l.getCargo()));
							discharges.forEach(l -> cargoes.add(l.getCargo()));
							cargoes.remove(null);
							for (var cargo : cargoes) {
								final BaseCaseRowGroup grp = AnalyticsFactory.eINSTANCE.createBaseCaseRowGroup();

								BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
								row.setGroup(grp);

								final CompoundCommand c2 = new CompoundCommand();
								LoadSlot firstLoad = null;
								DischargeSlot firstDischarge = null;
								for (var s : cargo.getSortedSlots()) {
									if (s instanceof LoadSlot loadSlot) {
										if (row.getBuyOption() == null) {
											final BuyOption buyRef = AnalyticsBuilder.getOrCreateBuyOption(loadSlot, optionAnalysisModel, scenarioEditingLocation, c2);
											if (buyRef != null) {
												row.setBuyOption(buyRef);
											}

											if (firstLoad == null) {
												firstLoad = loadSlot;
											}
										} else {
											appendAndExecute(UnexecutableCommand.INSTANCE);
										}
										loads.remove(loadSlot);
									}
									if (s instanceof DischargeSlot dischargeSlot) {
										if (row.getSellOption() != null) {
											appendAndExecute(
													AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));
											row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
											row.setGroup(grp);
										}
										final SellOption sellRef = AnalyticsBuilder.getOrCreateSellOption(dischargeSlot, optionAnalysisModel, scenarioEditingLocation, c2);
										if (sellRef != null) {
											row.setSellOption(sellRef);
										}

										if (firstDischarge == null) {
											firstDischarge = dischargeSlot;
										}
										discharges.remove(dischargeSlot);
									}
								}
								if (!c2.isEmpty()) {
									appendAndExecute(c2);
								}
								appendAndExecute(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));

								appendAndExecute(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__GROUPS, grp));
								AnalyticsBuilder.applyShipping(scenarioEditingLocation, optionAnalysisModel, grp.getRows().get(0), cargo, firstLoad, firstDischarge,
										optionAnalysisModel.getBaseCase().isKeepExistingScenario(), this);

							}
							for (var loadSlot : loads) {
								final BaseCaseRowGroup grp = AnalyticsFactory.eINSTANCE.createBaseCaseRowGroup();

								BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
								row.setGroup(grp);

								final CompoundCommand c2 = new CompoundCommand();
								final BuyOption buyRef = AnalyticsBuilder.getOrCreateBuyOption(loadSlot, optionAnalysisModel, scenarioEditingLocation, c2);
								if (buyRef != null) {
									row.setBuyOption(buyRef);
								}
								if (!c2.isEmpty()) {
									appendAndExecute(c2);
								}
								appendAndExecute(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));

								appendAndExecute(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__GROUPS, grp));
								AnalyticsBuilder.applyShipping(scenarioEditingLocation, optionAnalysisModel, row, null, loadSlot, null, optionAnalysisModel.getBaseCase().isKeepExistingScenario(), this);
							}
							for (var dischargeSlot : discharges) {
								final BaseCaseRowGroup grp = AnalyticsFactory.eINSTANCE.createBaseCaseRowGroup();

								BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
								row.setGroup(grp);

								final CompoundCommand c2 = new CompoundCommand();
								final SellOption sellRef = AnalyticsBuilder.getOrCreateSellOption(dischargeSlot, optionAnalysisModel, scenarioEditingLocation, c2);
								if (sellRef != null) {
									row.setSellOption(sellRef);
								}
								if (!c2.isEmpty()) {
									appendAndExecute(c2);
								}
								appendAndExecute(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));

								appendAndExecute(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__GROUPS, grp));
								AnalyticsBuilder.applyShipping(scenarioEditingLocation, optionAnalysisModel, row, null, null, dischargeSlot, optionAnalysisModel.getBaseCase().isKeepExistingScenario(),
										this);
							}
						}
					};
					// cmd.append(c);

					// if (!cmd.isEmpty()) {
					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(c, null, null);
				}
			} else if (selection.size() > 1) {
 
				// Lazily evaluated command to avoid adding shipping multiple times.
				final CompoundCommand c = new CompoundCommand() {
					@Override
					protected boolean prepare() {
						return true;
					}

					@Override
					public void append(final Command command) {
						super.appendAndExecute(command);
					}

					@Override
					public void execute() {

						Collection<Cargo> cargoes = new LinkedHashSet<>();
						Collection<LoadSlot> loads = new LinkedHashSet<>();
						Collection<DischargeSlot> discharges = new LinkedHashSet<>();
						final Iterator<?> itr = selection.iterator();

						while (itr.hasNext()) {
							final Object o = itr.next();
							if (o instanceof final TradesRow sourceTradesRow) {
								cargoes.add(sourceTradesRow.getCargo());
								loads.add(sourceTradesRow.getLoadSlot());
								discharges.add(sourceTradesRow.getDischargeSlot());
							}
						}
						loads.remove(null);
						discharges.remove(null);

						loads.forEach(l -> cargoes.add(l.getCargo()));
						discharges.forEach(l -> cargoes.add(l.getCargo()));
						cargoes.remove(null);
						for (var cargo : cargoes) {
							final BaseCaseRowGroup grp = AnalyticsFactory.eINSTANCE.createBaseCaseRowGroup();

							BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
							row.setGroup(grp);

							final CompoundCommand c2 = new CompoundCommand();
							LoadSlot firstLoad = null;
							DischargeSlot firstDischarge = null;
							for (var s : cargo.getSortedSlots()) {
								if (s instanceof LoadSlot loadSlot) {
									if (row.getBuyOption() == null) {
										final BuyOption buyRef = AnalyticsBuilder.getOrCreateBuyOption(loadSlot, optionAnalysisModel, scenarioEditingLocation, c2);
										if (buyRef != null) {
											row.setBuyOption(buyRef);
										}

										if (firstLoad == null) {
											firstLoad = loadSlot;
										}
									} else {
										appendAndExecute(UnexecutableCommand.INSTANCE);
									}
									loads.remove(loadSlot);
								}
								if (s instanceof DischargeSlot dischargeSlot) {
									if (row.getSellOption() != null) {
										appendAndExecute(
												AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));
										row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
										row.setGroup(grp);
									}
									final SellOption sellRef = AnalyticsBuilder.getOrCreateSellOption(dischargeSlot, optionAnalysisModel, scenarioEditingLocation, c2);
									if (sellRef != null) {
										row.setSellOption(sellRef);
									}

									if (firstDischarge == null) {
										firstDischarge = dischargeSlot;
									}
									discharges.remove(dischargeSlot);
								}
							}
							if (!c2.isEmpty()) {
								appendAndExecute(c2);
							}
							appendAndExecute(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));

							appendAndExecute(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__GROUPS, grp));
							AnalyticsBuilder.applyShipping(scenarioEditingLocation, optionAnalysisModel, grp.getRows().get(0), cargo, firstLoad, firstDischarge,
									optionAnalysisModel.getBaseCase().isKeepExistingScenario(), this);

						}
						for (var loadSlot : loads) {
							final BaseCaseRowGroup grp = AnalyticsFactory.eINSTANCE.createBaseCaseRowGroup();

							BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
							row.setGroup(grp);

							final CompoundCommand c2 = new CompoundCommand();
							final BuyOption buyRef = AnalyticsBuilder.getOrCreateBuyOption(loadSlot, optionAnalysisModel, scenarioEditingLocation, c2);
							if (buyRef != null) {
								row.setBuyOption(buyRef);
							}
							if (!c2.isEmpty()) {
								appendAndExecute(c2);
							}
							appendAndExecute(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));

							appendAndExecute(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__GROUPS, grp));
							AnalyticsBuilder.applyShipping(scenarioEditingLocation, optionAnalysisModel, row, null, loadSlot, null, optionAnalysisModel.getBaseCase().isKeepExistingScenario(), this);
						}
						for (var dischargeSlot : discharges) {
							final BaseCaseRowGroup grp = AnalyticsFactory.eINSTANCE.createBaseCaseRowGroup();

							BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
							row.setGroup(grp);

							final CompoundCommand c2 = new CompoundCommand();
							final SellOption sellRef = AnalyticsBuilder.getOrCreateSellOption(dischargeSlot, optionAnalysisModel, scenarioEditingLocation, c2);
							if (sellRef != null) {
								row.setSellOption(sellRef);
							}
							if (!c2.isEmpty()) {
								appendAndExecute(c2);
							}
							appendAndExecute(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));

							appendAndExecute(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__GROUPS, grp));
							AnalyticsBuilder.applyShipping(scenarioEditingLocation, optionAnalysisModel, row, null, null, dischargeSlot, optionAnalysisModel.getBaseCase().isKeepExistingScenario(),
									this);
						}
					}
				};
				scenarioEditingLocation.getDefaultCommandHandler().handleCommand(c, null, null);
			}
		}
	}

	@Override
	protected void doDragOver(final DropTargetEvent event) {

		if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
			final IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().nativeToJava(event.currentDataType);
			if (selection.size() == 1) {
				final Object o = selection.getFirstElement();
				if (o instanceof OpenBuy || o instanceof OpenSell) {
					final String type = (o instanceof OpenBuy) ? "open buy" : "open sell";
					if (event.item instanceof final GridItem gridItem) {
						final Object d = gridItem.getData();
						if (d instanceof final BaseCaseRow row) {
							if (o instanceof BuyOption && row.getGroup().getRows().get(0) != row) {
								event.operations = DND.DROP_COPY;
								event.detail = DND.DROP_COPY;
								setDragMessage("Create new row with " + type);
							} else {
								event.operations = DND.DROP_COPY | DND.DROP_MOVE;

								if ((event.detail & event.operations) != event.detail) {
									event.detail = DND.DROP_MOVE;
								}
								if (event.detail == DND.DROP_COPY) {
									setDragMessage("Create new row with " + type);
								} else if (event.detail == DND.DROP_MOVE) {
									setDragMessage("Replace with " + type);

								} else {
									setDragMessage("Add or replace with " + type);
								}
							}
							return;
						}
					}
					// Empty row
					event.operations = DND.DROP_MOVE;
					event.detail = DND.DROP_MOVE;
					setDragMessage("Create new row with " + type);
					return;
				}
				if (o instanceof BuyOption || o instanceof SellOption) {
					final String type = (o instanceof BuyOption) ? "buy position" : "sell position";

					if (event.item instanceof final GridItem gridItem) {
						final Object d = gridItem.getData();
						if (d instanceof final BaseCaseRow row) {
							if (o instanceof BuyOption && row.getGroup().getRows().get(0) != row) {
								event.operations = DND.DROP_NONE;
								event.detail = DND.DROP_NONE;
								setDragMessage("Create new row with " + type);
							} else {
								event.operations = DND.DROP_MOVE;
								boolean canAddDischarge = false;
								if (allowLDD && o instanceof SellOption && (row.getGroup().getRows().size() == 1 || row.getGroup().getRows().size() == 2) && row.getSellOption() != null) {
									// Create LDD cargo if only a LD
									canAddDischarge = true;
									event.operations |= DND.DROP_COPY;
								}
								if ((event.detail & event.operations) != event.detail) {
									event.detail = DND.DROP_MOVE;
								}
								if (event.detail == DND.DROP_MOVE) {
									setDragMessage("Replace with " + type + (canAddDischarge ? "\nHold CTRL to create second discharge row" : ""));
								} else if (event.detail == DND.DROP_COPY) {
									setDragMessage("Create second discharge row");
								} else {
									setDragMessage("Add or replace with " + type);
								}
							}
							return;
						}
					}
					// Empty row
					event.operations = DND.DROP_MOVE;
					event.detail = DND.DROP_MOVE;
					setDragMessage("Create new row with " + type);
					return;
				}
				if (o instanceof VesselEventOption) {
					if (event.item instanceof final GridItem gridItem) {
						final Object d = gridItem.getData();
						if (d instanceof final BaseCaseRow row) {
							event.operations = DND.DROP_MOVE;
							event.detail = DND.DROP_MOVE;
							setDragMessage("Set vessel event");

							return;
						}
					}
					event.operations = DND.DROP_MOVE;
					event.detail = DND.DROP_MOVE;
					setDragMessage("Create new vessel event row");

					return;
				}
				if (o instanceof Vessel) {
					event.operations = DND.DROP_MOVE;
					event.detail = DND.DROP_MOVE;

					return;
				}
				if (o instanceof ShippingOption) {
					if (event.item instanceof final GridItem gridItem) {
						final Object d = gridItem.getData();
						if (d instanceof final BaseCaseRow row) {
							event.operations = DND.DROP_MOVE;
							event.detail = DND.DROP_MOVE;
							setDragMessage("Set shipping option");
							return;
						}
					}
					event.operations = DND.DROP_NONE;
					event.detail = DND.DROP_NONE;
					return;
				}
				if (o instanceof TradesRow) {
					event.operations = DND.DROP_MOVE;
					event.detail = DND.DROP_MOVE;
					setDragMessage("Create new row(s) from trades table");

					return;
				}
			}
		}
		event.operations = DND.DROP_NONE;
	}
}
