/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
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
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.PartialCaseRowGroup;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.VesselEventOption;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;

public class PartialCaseDropTargetListener extends AbstractDropTargetListener {

	// DROP_MOVE == Replace element in row or create a new row
	// DROP_COPY == Create a new LDD row
	// DROP_LINK == Add to existing row (create multiple choice)

	private final boolean allowLDD = LicenseFeatures.isPermitted(KnownFeatures.FEATURE_COMPLEX_CARGO);

	public PartialCaseDropTargetListener(final @NonNull IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel optionAnalysisModel, @NonNull final GridTreeViewer viewer) {
		super(scenarioEditingLocation, optionAnalysisModel, viewer);
	}

	public PartialCaseDropTargetListener(final @NonNull IScenarioEditingLocation scenarioEditingLocation, @NonNull final GridTreeViewer viewer) {
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
						if (d instanceof PartialCaseRow) {
							event.operations = DND.DROP_LINK | DND.DROP_MOVE;
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

			Object o = null; // Target option. We assume selection came from the same source and all elements will be of the same super-type.
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
			// Mutable distinct copy
			if (collection != null) {
				collection = new LinkedList<>(new LinkedHashSet<>(collection));
			}
			if (o != null) {

				PartialCaseRow existing = null;
				if (event.item instanceof final GridItem gridItem) {
					final Object d = gridItem.getData();
					if (d instanceof final PartialCaseRow pcr) {
						existing = pcr;
					}
				}

				if (o instanceof final VesselEventOption option) {
					if (existing != null) {
						final CompoundCommand cmd = new CompoundCommand();
						if (event.detail == DND.DROP_MOVE) {
							final List<EObject> toRemove = new LinkedList<>(existing.getVesselEventOptions());
							toRemove.removeAll(collection);
							if (!toRemove.isEmpty()) {
								cmd.append(RemoveCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__VESSEL_EVENT_OPTIONS, toRemove));
							}
							collection.removeAll(existing.getVesselEventOptions());
							if (!collection.isEmpty()) {
								cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__VESSEL_EVENT_OPTIONS, collection));
							}
						} else if (event.detail == DND.DROP_LINK) {
							collection.removeAll(existing.getVesselEventOptions());
							if (!collection.isEmpty()) {
								cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__VESSEL_EVENT_OPTIONS, collection));
							}
						}
						if (!cmd.isEmpty()) {
							scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, existing, null);
						}
					} else {
						final PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
						final PartialCaseRowGroup grp = AnalyticsFactory.eINSTANCE.createPartialCaseRowGroup();
						row.setGroup(grp);
						final CompoundCommand cmd = new CompoundCommand();
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, row));
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__GROUPS, grp));
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__VESSEL_EVENT_OPTIONS, collection));
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, optionAnalysisModel, null);
					}
				} else if (o instanceof final BuyOption buyOption) {
					if (existing != null) {

						final CompoundCommand cmd = new CompoundCommand();

						if (event.detail == DND.DROP_COPY) {
							final PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
							row.getBuyOptions().addAll((Collection<? extends BuyOption>) collection);
							final PartialCaseRowGroup grp = existing.getGroup();
							cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, row));
							cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__GROUP, grp));
						} else if (event.detail == DND.DROP_MOVE) {
							final List<EObject> toRemove = new LinkedList<>(existing.getBuyOptions());
							toRemove.removeAll(collection);
							if (!toRemove.isEmpty()) {
								cmd.append(RemoveCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS, toRemove));
							}
							collection.removeAll(existing.getBuyOptions());
							if (!collection.isEmpty()) {
								cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS, collection));
							}
						} else if (event.detail == DND.DROP_LINK) {
							collection.removeAll(existing.getBuyOptions());
							if (!collection.isEmpty()) {
								cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS, collection));
							}

						}
						if (!cmd.isEmpty()) {
							scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, existing, null);
						}
					} else if (event.detail == DND.DROP_MOVE) {
						// Create a new row

						final PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
						final PartialCaseRowGroup grp = AnalyticsFactory.eINSTANCE.createPartialCaseRowGroup();
						row.setGroup(grp);
						final CompoundCommand cmd = new CompoundCommand();
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, row));
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__GROUPS, grp));
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS, collection));
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, optionAnalysisModel, null);

					}
				} else if (o instanceof final SellOption sellOption) {
					if (existing != null) {

						final CompoundCommand cmd = new CompoundCommand();

						if (event.detail == DND.DROP_COPY) {
							// LDD case
							final PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
							row.getSellOptions().addAll((Collection<? extends SellOption>) collection);
							final PartialCaseRowGroup grp = existing.getGroup();
							cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, row));
							cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__GROUP, grp));
						} else if (event.detail == DND.DROP_MOVE) {
							// replace options
							final List<EObject> toRemove = new LinkedList<>(existing.getSellOptions());
							toRemove.removeAll(collection);
							if (!toRemove.isEmpty()) {
								cmd.append(RemoveCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS, toRemove));
							}
							collection.removeAll(existing.getSellOptions());
							if (!collection.isEmpty()) {
								cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS, collection));
							}
						} else if (event.detail == DND.DROP_LINK) {
							// Add to options
							collection.removeAll(existing.getSellOptions());
							if (!collection.isEmpty()) {
								cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS, collection));
							}
						}
						if (!cmd.isEmpty()) {
							scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, existing, null);
						}
					} else if (event.detail == DND.DROP_MOVE) {
						final PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
						final PartialCaseRowGroup grp = AnalyticsFactory.eINSTANCE.createPartialCaseRowGroup();
						row.setGroup(grp);
						final CompoundCommand cmd = new CompoundCommand();
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, row));
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__GROUPS, grp));
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

							scenarioEditingLocation.getDefaultCommandHandler()
									.handleCommand(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__BUYS, row),
											optionAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__BUYS);

							scenarioEditingLocation.getDefaultCommandHandler()
									.handleCommand(AddCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS,
											Collections.singletonList(row)), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS);

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

							scenarioEditingLocation.getDefaultCommandHandler()
									.handleCommand(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SELLS, row),
											optionAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SELLS);

							scenarioEditingLocation.getDefaultCommandHandler()
									.handleCommand(AddCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS,
											Collections.singletonList(row)), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS);

							DetailCompositeDialogUtil.editSingleObject(scenarioEditingLocation, row);
						}
					}
				} else if (o instanceof final Vessel vessel) {
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
						scenarioEditingLocation.getDefaultCommandHandler()
								.handleCommand(SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING, collection), existing,
										AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
						// }
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

							final PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
							final PartialCaseRowGroup grp = AnalyticsFactory.eINSTANCE.createPartialCaseRowGroup();
							row.setGroup(grp);
							row.getVesselEventOptions().add(option);

							AnalyticsBuilder.applyShipping(scenarioEditingLocation, optionAnalysisModel, row, vesselEvent, optionAnalysisModel.getBaseCase().isKeepExistingScenario(), this);

							appendAndExecute(
									AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, row));
							appendAndExecute(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__GROUPS, grp));
						}
					};
					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(c, null, null);
				} else if (o instanceof final CargoModelRowTransformer.RowData sourceRowData) {

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
								if (o instanceof final CargoModelRowTransformer.RowData sourceRowData) {
									cargoes.add(sourceRowData.getCargo());
									loads.add(sourceRowData.getLoadSlot());
									discharges.add(sourceRowData.getDischargeSlot());
								}
							}
							loads.remove(null);
							discharges.remove(null);

							loads.forEach(l -> cargoes.add(l.getCargo()));
							discharges.forEach(l -> cargoes.add(l.getCargo()));
							cargoes.remove(null);
							for (var cargo : cargoes) {
								final PartialCaseRowGroup grp = AnalyticsFactory.eINSTANCE.createPartialCaseRowGroup();

								PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
								row.setGroup(grp);

								final CompoundCommand c2 = new CompoundCommand();
								LoadSlot firstLoad = null;
								DischargeSlot firstDischarge = null;
								for (var s : cargo.getSortedSlots()) {
									if (s instanceof LoadSlot loadSlot) {
										if (row.getBuyOptions().isEmpty()) {
											final BuyOption buyRef = AnalyticsBuilder.getOrCreateBuyOption(loadSlot, optionAnalysisModel, scenarioEditingLocation, c2);
											if (buyRef != null) {
												row.getBuyOptions().add(buyRef);
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
										if (!row.getSellOptions().isEmpty()) {
											appendAndExecute(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(),
													AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, row));
											row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
											row.setGroup(grp);
										}
										final SellOption sellRef = AnalyticsBuilder.getOrCreateSellOption(dischargeSlot, optionAnalysisModel, scenarioEditingLocation, c2);
										if (sellRef != null) {
											row.getSellOptions().add(sellRef);
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
								appendAndExecute(
										AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, row));

								appendAndExecute(
										AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__GROUPS, grp));
								AnalyticsBuilder.applyShipping(scenarioEditingLocation, optionAnalysisModel, grp.getRows().get(0), cargo, firstLoad, firstDischarge,
										optionAnalysisModel.getBaseCase().isKeepExistingScenario(), this);

							}
							for (var loadSlot : loads) {
								final PartialCaseRowGroup grp = AnalyticsFactory.eINSTANCE.createPartialCaseRowGroup();

								final PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
								row.setGroup(grp);

								final CompoundCommand c2 = new CompoundCommand();
								final BuyOption buyRef = AnalyticsBuilder.getOrCreateBuyOption(loadSlot, optionAnalysisModel, scenarioEditingLocation, c2);
								if (buyRef != null) {
									row.getBuyOptions().add(buyRef);
								}
								if (!c2.isEmpty()) {
									appendAndExecute(c2);
								}
								appendAndExecute(
										AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, row));

								appendAndExecute(
										AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__GROUPS, grp));
								AnalyticsBuilder.applyShipping(scenarioEditingLocation, optionAnalysisModel, row, null, loadSlot, null, optionAnalysisModel.getBaseCase().isKeepExistingScenario(),
										this);
							}
							for (var dischargeSlot : discharges) {
								final PartialCaseRowGroup grp = AnalyticsFactory.eINSTANCE.createPartialCaseRowGroup();

								final PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
								row.setGroup(grp);

								final CompoundCommand c2 = new CompoundCommand();
								final SellOption sellRef = AnalyticsBuilder.getOrCreateSellOption(dischargeSlot, optionAnalysisModel, scenarioEditingLocation, c2);
								if (sellRef != null) {
									row.getSellOptions().add(sellRef);
								}
								if (!c2.isEmpty()) {
									appendAndExecute(c2);
								}
								appendAndExecute(
										AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, row));

								appendAndExecute(
										AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__GROUPS, grp));
								AnalyticsBuilder.applyShipping(scenarioEditingLocation, optionAnalysisModel, row, null, null, dischargeSlot, optionAnalysisModel.getBaseCase().isKeepExistingScenario(),
										this);
							}
						}
					};
					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(c, null, null);
				}
			}
		}

	}

	@Override
	protected void doDragOver(final DropTargetEvent event) {
		if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
			final IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().nativeToJava(event.currentDataType);
			if (selection.size() > 0) {
				final Object o = selection.getFirstElement();
				if (o instanceof BuyOption || o instanceof SellOption) {
					final String type = (o instanceof BuyOption) ? "buy position" : "sell position";
					if (event.item instanceof final GridItem gridItem) {
						final Object d = gridItem.getData();
						if (d instanceof final PartialCaseRow row) {

							if (o instanceof BuyOption && row.getGroup().getRows().get(0) != row) {
								event.operations = DND.DROP_NONE;
								event.detail = DND.DROP_NONE;
								setDragMessage("Create new row with " + type);
							} else {
								event.operations = DND.DROP_MOVE | DND.DROP_LINK;
								boolean canAddDischarge = false;
								if (allowLDD && o instanceof SellOption && row.getGroup().getRows().size() == 1 && !row.getSellOptions().isEmpty()) {
									// Create LDD cargo if only a LD
									canAddDischarge = true;
									event.operations |= DND.DROP_COPY;
								}
								if ((event.detail & event.operations) != event.detail) {
									event.detail = DND.DROP_MOVE;
								}
								if (event.detail == DND.DROP_MOVE) {
									setDragMessage("Replace with " + type + "\nHold ALT to add options" + (canAddDischarge ? "\nHold CTRL to create second discharge row" : ""));
								} else if (event.detail == DND.DROP_LINK) {
									setDragMessage("Add another " + type + " to the options");
								} else if (event.detail == DND.DROP_COPY) {
									setDragMessage("Create second discharge row");
								} else {
									setDragMessage("Add or replace with " + type);
								}
							}
							return;
						}
					}
					event.operations = DND.DROP_MOVE;
					event.detail = DND.DROP_MOVE;
					setDragMessage("Create new row with " + type);
					return;
				}
				if (o instanceof VesselEventOption) {
					final String type = "vessel event";

					if (event.item instanceof final GridItem gridItem) {
						final Object d = gridItem.getData();
						if (d instanceof final PartialCaseRow row) {

							if (row.getGroup().getRows().size() == 1 || row.getGroup().getRows().get(0) == row) {
								event.operations = DND.DROP_MOVE | DND.DROP_LINK;
							} else {
								event.operations = DND.DROP_NONE;
								event.detail = DND.DROP_NONE;
								setDragMessage(null);

								return;
							}
							if (event.detail == DND.DROP_NONE) {
								event.detail = DND.DROP_MOVE;
							}

							if (event.detail == DND.DROP_MOVE) {
								setDragMessage("Replace with " + type + "\nHold ALT to add options");
							} else if (event.detail == DND.DROP_LINK) {
								setDragMessage("Add another " + type + " to the options");
							}
							return;
						}
					}
					event.operations = DND.DROP_MOVE;
					event.detail = DND.DROP_MOVE;
					setDragMessage("Create new vessel event row");

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
					event.operations = DND.DROP_MOVE | DND.DROP_LINK;
					return;
				}
				if (o instanceof CargoModelRowTransformer.RowData) {
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
