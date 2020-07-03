/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.actions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.OpenBuy;
import com.mmxlabs.models.lng.analytics.OpenSell;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCase;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.VesselEventOption;
import com.mmxlabs.models.lng.analytics.VesselEventReference;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class UpdateSandboxFromRowsAction extends Action {

	private final Collection<ChangeSetTableRow> selectedRows;
	private final String name;
	private ScenarioResult baseScenarioResult;
	private final OptionAnalysisModel existingModel;

	public UpdateSandboxFromRowsAction(final Collection<ChangeSetTableRow> selectedRows, final String name, ScenarioResult baseScenarioResult, OptionAnalysisModel newModel) {
		super(name);
		this.selectedRows = selectedRows;
		this.existingModel = newModel;
		this.name = name;
		this.baseScenarioResult = baseScenarioResult;
	}

	@Override
	public void run() {
		final ScenarioModelRecord modelRecord = baseScenarioResult.getModelRecord();
		try (ModelReference modelRef = modelRecord.aquireReference("ChangeSetView:ContextMenuManager")) {
			final EditingDomain editingDomain = modelRef.getEditingDomain();

			CompoundCommand cc = new CompoundCommand("Update sandbox");

			final Map<SpotMarket, BuyMarket> buyMarketOptions = new HashMap<>();
			final Map<SpotMarket, SellMarket> sellMarketOptions = new HashMap<>();
			final Map<LoadSlot, BuyOption> buyOptions = new HashMap<>();
			final Map<DischargeSlot, SellOption> sellOptions = new HashMap<>();
			final Map<VesselEvent, VesselEventOption> eventOptions = new HashMap<>();

			final Map<Pair<CharterInMarket, Integer>, ExistingCharterMarketOption> roundTripMap = new HashMap<>();
			final Map<VesselAvailability, ExistingVesselCharterOption> vesselAvailOptionMap = new HashMap<>();

			// Pre-populate
			{
				existingModel.getBuys().stream().forEach(buy -> {
					if (buy instanceof BuyMarket) {
						BuyMarket op = (BuyMarket) buy;
						buyMarketOptions.put(op.getMarket(), op);
					} else if (buy instanceof BuyReference) {
						BuyReference op = (BuyReference) buy;
						buyOptions.put(op.getSlot(), op);
					} else if (buy instanceof OpenBuy) {
						OpenBuy op = (OpenBuy) buy;
						buyOptions.put(null, op);
					}
				});

				existingModel.getSells().stream().forEach(sell -> {
					if (sell instanceof SellMarket) {
						SellMarket op = (SellMarket) sell;
						sellMarketOptions.put(op.getMarket(), op);
					} else if (sell instanceof SellReference) {
						SellReference op = (SellReference) sell;
						sellOptions.put(op.getSlot(), op);
					} else if (sell instanceof OpenSell) {
						OpenSell op = (OpenSell) sell;
						sellOptions.put(null, op);
					}
				});

				existingModel.getVesselEvents().stream().forEach(sell -> {
					if (sell instanceof VesselEventReference) {
						VesselEventReference op = (VesselEventReference) sell;
						eventOptions.put(op.getEvent(), op);

					}
				});

				existingModel.getShippingTemplates().stream().forEach(buy -> {
					if (buy instanceof ExistingCharterMarketOption) {
						ExistingCharterMarketOption op = (ExistingCharterMarketOption) buy;
						roundTripMap.put(Pair.of(op.getCharterInMarket(), op.getSpotIndex()), op);
					} else if (buy instanceof ExistingVesselCharterOption) {
						ExistingVesselCharterOption op = (ExistingVesselCharterOption) buy;
						vesselAvailOptionMap.put(op.getVesselCharter(), op);
					}
				});
			}

			final Function<VesselAvailability, ExistingVesselCharterOption> availOptionComputer = (va) -> {
				final ExistingVesselCharterOption opt = AnalyticsFactory.eINSTANCE.createExistingVesselCharterOption();
				opt.setVesselCharter(va);
				cc.append(AddCommand.create(editingDomain, existingModel, AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt));
				return opt;
			};

			final Function<LoadSlot, BuyOption> buyGetter = slot -> buyOptions.computeIfAbsent(slot, s -> {
				if (s == null) {
					final OpenBuy op = AnalyticsFactory.eINSTANCE.createOpenBuy();
					cc.append(AddCommand.create(editingDomain, existingModel, AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__BUYS, op));
					return op;
				} else {
					if (s instanceof SpotSlot) {
						final SpotSlot spotSlot = (SpotSlot) s;
						return buyMarketOptions.computeIfAbsent(spotSlot.getMarket(), mkt -> {
							final BuyMarket m = AnalyticsFactory.eINSTANCE.createBuyMarket();
							m.setMarket(mkt);
							cc.append(AddCommand.create(editingDomain, existingModel, AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__BUYS, m));
							return m;
						});
					} else {
						final BuyReference ref = AnalyticsFactory.eINSTANCE.createBuyReference();
						ref.setSlot(s);
						cc.append(AddCommand.create(editingDomain, existingModel, AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__BUYS, ref));

						return ref;
					}
				}
			});
			final Function<DischargeSlot, SellOption> sellGetter = slot -> sellOptions.computeIfAbsent(slot, s -> {
				if (s == null) {
					final OpenSell op = AnalyticsFactory.eINSTANCE.createOpenSell();
					cc.append(AddCommand.create(editingDomain, existingModel, AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SELLS, op));
					return op;
				} else {
					if (s instanceof SpotSlot) {
						final SpotSlot spotSlot = (SpotSlot) s;
						return sellMarketOptions.computeIfAbsent(spotSlot.getMarket(), mkt -> {
							final SellMarket m = AnalyticsFactory.eINSTANCE.createSellMarket();
							m.setMarket(mkt);
							cc.append(AddCommand.create(editingDomain, existingModel, AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SELLS, m));
							return m;
						});
					} else {
						final SellReference ref = AnalyticsFactory.eINSTANCE.createSellReference();
						ref.setSlot(s);
						cc.append(AddCommand.create(editingDomain, existingModel, AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SELLS, ref));
						return ref;
					}
				}
			});

			final Function<VesselEvent, VesselEventOption> eventGetter = evt -> eventOptions.computeIfAbsent(evt, s -> {
				final VesselEventReference ref = AnalyticsFactory.eINSTANCE.createVesselEventReference();
				ref.setEvent(s);
				cc.append(AddCommand.create(editingDomain, existingModel, AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS, ref));
				return ref;
			});

			final LNGScenarioModel scenarioModel = baseScenarioResult.getTypedRoot(LNGScenarioModel.class);
			assert scenarioModel != null;

			final BaseCase baseCase = existingModel.getBaseCase();

			for (final ChangeSetTableRow row : selectedRows) {
				final BaseCaseRow bRow = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
				if (row.isWiringChange() || row.isVesselChange()) {
					{
						final ChangeSetRowData lhsData = row.getLhsAfter();
						if (lhsData != null) {
							final SlotAllocation loadAllocation = lhsData.getLoadAllocation();
							if (loadAllocation != null) {
								final LoadSlot slot = (LoadSlot) loadAllocation.getSlot();
								bRow.setBuyOption(buyGetter.apply(slot));
							}
							final Event evt = lhsData.getLhsEvent();
							if (evt instanceof VesselEventVisit) {
								VesselEventVisit vesselEventVisit = (VesselEventVisit) evt;
								bRow.setVesselEventOption(eventGetter.apply(vesselEventVisit.getVesselEvent()));
							}
						}
						final ChangeSetRowData rhsData = row.getLhsBefore();
						if (rhsData != null) {
							final SlotAllocation originalLoadAllocation = rhsData.getLoadAllocation();
							if (originalLoadAllocation != null) {
								final Sequence sequence = originalLoadAllocation.getSlotVisit().getSequence();

								if (sequence != null) {
									if (sequence.getCharterInMarket() != null) {
										final CharterInMarket charterInMarket = sequence.getCharterInMarket();
										final Pair<CharterInMarket, Integer> key = new Pair<>(charterInMarket, sequence.getSpotIndex());
										if (roundTripMap.containsKey(key)) {
											final ExistingCharterMarketOption opt = roundTripMap.get(key);
											bRow.setShipping(opt);
										} else {
											final ExistingCharterMarketOption opt = AnalyticsFactory.eINSTANCE.createExistingCharterMarketOption();
											opt.setCharterInMarket(charterInMarket);
											opt.setSpotIndex(sequence.getSpotIndex());
											cc.append(AddCommand.create(editingDomain, existingModel, AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt));
											bRow.setShipping(opt);
											roundTripMap.put(key, opt);
										}

									} else if (sequence.getVesselAvailability() != null) {
										final VesselAvailability vesselAvailability = sequence.getVesselAvailability();
										final ExistingVesselCharterOption opt = vesselAvailOptionMap.computeIfAbsent(vesselAvailability, availOptionComputer);
										bRow.setShipping(opt);
									}
								}
							}
						}
					}
					final ChangeSetRowData rhsData = row.getLhsBefore();
					if (rhsData != null) {
						final SlotAllocation dischargeAllocation = rhsData.getDischargeAllocation();
						if (dischargeAllocation != null) {
							final DischargeSlot slot = (DischargeSlot) dischargeAllocation.getSlot();
							bRow.setSellOption(sellGetter.apply(slot));
						}
					}
					if (bRow.getBuyOption() instanceof BuyMarket && bRow.getSellOption() == null) {
						// Do not add
					} else if (bRow.getBuyOption() == null && bRow.getSellOption() instanceof SellMarket) {
						// Do not add
					} else if (bRow.getBuyOption() != null || bRow.getSellOption() != null || bRow.getVesselEventOption() != null) {
						boolean foundExisting = false;
						for (BaseCaseRow existing : baseCase.getBaseCase()) {
							if (existing.getBuyOption() == bRow.getBuyOption() //
									&& existing.getSellOption() == bRow.getSellOption() //
									&& existing.getShipping() == bRow.getShipping() //
									&& existing.getVesselEventOption() == bRow.getVesselEventOption() //
							) {
								foundExisting = true;
								break;
							}
						}
						if (!foundExisting) {
							cc.append(AddCommand.create(editingDomain, existingModel.getBaseCase(), AnalyticsPackage.BASE_CASE__BASE_CASE, bRow));
						}
					}
				}
			}
			final PartialCase partialCase = existingModel.getPartialCase();

			for (final ChangeSetTableRow row : selectedRows) {
				final PartialCaseRow pRow = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
				if (row.isWiringChange() || row.isVesselChange()) {
					{
						final ChangeSetRowData lhsData = row.getLhsAfter();
						if (lhsData != null) {

							final Event evt = lhsData.getLhsEvent();
							if (evt instanceof VesselEventVisit) {
								VesselEventVisit vesselEventVisit = (VesselEventVisit) evt;
								pRow.getVesselEventOptions().add(eventGetter.apply(vesselEventVisit.getVesselEvent()));
							}

							final SlotAllocation loadAllocation = lhsData.getLoadAllocation();
							if (loadAllocation != null) {
								final LoadSlot slot = (LoadSlot) loadAllocation.getSlot();
								if (slot != null) {
									pRow.getBuyOptions().add(buyGetter.apply(slot));
								}
							}
						}
					}
					{
						final ChangeSetRowData rhsData = row.getRhsAfter();
						if (rhsData != null) {
							final SlotAllocation dischargeAllocation = rhsData.getDischargeAllocation();
							if (dischargeAllocation != null) {
								final DischargeSlot slot = (DischargeSlot) dischargeAllocation.getSlot();
								pRow.getSellOptions().add(sellGetter.apply(slot));
							}
						}
					}

					{
						final ChangeSetRowData lhsData = row.getLhsAfter();
						if (lhsData != null) {
							final SlotAllocation originalLoadAllocation = lhsData.getLoadAllocation();
							if (originalLoadAllocation != null) {
								final Sequence sequence = originalLoadAllocation.getSlotVisit().getSequence();
								if (sequence != null) {
									if (sequence.getCharterInMarket() != null) {
										final CharterInMarket charterInMarket = sequence.getCharterInMarket();
										final Pair<CharterInMarket, Integer> key = new Pair<>(charterInMarket, sequence.getSpotIndex());
										if (roundTripMap.containsKey(key)) {
											final ExistingCharterMarketOption opt = roundTripMap.get(key);
											pRow.getShipping().add(opt);
										} else {
											final ExistingCharterMarketOption opt = AnalyticsFactory.eINSTANCE.createExistingCharterMarketOption();
											opt.setCharterInMarket(charterInMarket);
											opt.setSpotIndex(sequence.getSpotIndex());
											cc.append(AddCommand.create(editingDomain, existingModel, AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt));
											pRow.getShipping().add(opt);
											roundTripMap.put(key, opt);
										}

									} else if (sequence.getVesselAvailability() != null) {
										final VesselAvailability vesselAvailability = sequence.getVesselAvailability();
										final ExistingVesselCharterOption opt = vesselAvailOptionMap.computeIfAbsent(vesselAvailability, availOptionComputer);
										pRow.getShipping().add(opt);
									}
								}
							}
						}
					}

					if (!pRow.getBuyOptions().isEmpty() || !pRow.getSellOptions().isEmpty() || !pRow.getVesselEventOptions().isEmpty()) {
						boolean foundExisting = false;
						for (PartialCaseRow existing : partialCase.getPartialCase()) {

							if (Objects.equals(existing.getBuyOptions(), pRow.getBuyOptions()) //
									&& Objects.equals(existing.getSellOptions(), pRow.getSellOptions()) //
									&& Objects.equals(existing.getVesselEventOptions(), pRow.getVesselEventOptions()) //
									&& Objects.equals(existing.getShipping(), pRow.getShipping()) //
							) {
								foundExisting = true;
								break;
							}
						}
						if (!foundExisting) {
							cc.append(AddCommand.create(editingDomain, existingModel.getPartialCase(), AnalyticsPackage.PARTIAL_CASE__PARTIAL_CASE, pRow));
						}
					}
				}
			}

			if (!cc.isEmpty()) {
				editingDomain.getCommandStack().execute(cc);
			}

		}
	}
}
