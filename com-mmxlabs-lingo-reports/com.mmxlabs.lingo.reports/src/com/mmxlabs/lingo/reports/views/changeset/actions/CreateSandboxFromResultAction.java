/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.actions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.VesselEventOption;
import com.mmxlabs.models.lng.analytics.VesselEventReference;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class CreateSandboxFromResultAction extends Action {

	private final Collection<ChangeSetTableRow> selectedRows;
	private final String name;
	private final ScenarioResult baseScenarioResult;

	public CreateSandboxFromResultAction(final Collection<ChangeSetTableRow> selectedRows, final String name, final ScenarioResult baseScenarioResult) {

		super("Create sandbox");
		this.selectedRows = selectedRows;
		this.name = name;
		this.baseScenarioResult = baseScenarioResult;
	}

	@Override
	public void run() {

		final OptionAnalysisModel newModel = AnalyticsFactory.eINSTANCE.createOptionAnalysisModel();
		if (name != null && !name.isEmpty()) {
			newModel.setName("Sandbox: " + name);
		} else {
			newModel.setName("Sandbox from solution");
		}

		final Map<SpotMarket, BuyMarket> buyMarketOptions = new HashMap<>();
		final Map<SpotMarket, SellMarket> sellMarketOptions = new HashMap<>();
		final Map<LoadSlot, BuyOption> buyOptions = new HashMap<>();
		final Map<DischargeSlot, SellOption> sellOptions = new HashMap<>();
		final Map<VesselEvent, VesselEventOption> eventOptions = new HashMap<>();
		final Map<Pair<CharterInMarket, Integer>, ShippingOption> roundTripMap = new HashMap<>();
		final Map<VesselAvailability, ExistingVesselCharterOption> vesselAvailOptionMap = new HashMap<>();

		final Function<VesselAvailability, ExistingVesselCharterOption> availOptionComputer = (va) -> {
			final ExistingVesselCharterOption opt = AnalyticsFactory.eINSTANCE.createExistingVesselCharterOption();
			opt.setVesselCharter(va);
			newModel.getShippingTemplates().add(opt);
			return opt;
		};

		final Function<LoadSlot, BuyOption> buyGetter = slot -> buyOptions.computeIfAbsent(slot, s -> {
			if (s == null) {
				final OpenBuy op = AnalyticsFactory.eINSTANCE.createOpenBuy();
				newModel.getBuys().add(op);
				return op;
			} else {
				if (s instanceof SpotSlot) {
					final SpotSlot spotSlot = (SpotSlot) s;
					return buyMarketOptions.computeIfAbsent(spotSlot.getMarket(), mkt -> {
						final BuyMarket m = AnalyticsFactory.eINSTANCE.createBuyMarket();
						m.setMarket(mkt);
						newModel.getBuys().add(m);

						return m;
					});
				} else {
					final BuyReference ref = AnalyticsFactory.eINSTANCE.createBuyReference();
					ref.setSlot(s);
					newModel.getBuys().add(ref);
					return ref;
				}
			}
		});
		final Function<DischargeSlot, SellOption> sellGetter = slot -> sellOptions.computeIfAbsent(slot, s -> {
			if (s == null) {
				final OpenSell op = AnalyticsFactory.eINSTANCE.createOpenSell();
				newModel.getSells().add(op);
				return op;
			} else {
				if (s instanceof SpotSlot) {
					final SpotSlot spotSlot = (SpotSlot) s;
					return sellMarketOptions.computeIfAbsent(spotSlot.getMarket(), mkt -> {
						final SellMarket m = AnalyticsFactory.eINSTANCE.createSellMarket();
						m.setMarket(mkt);
						newModel.getSells().add(m);

						return m;
					});
				} else {
					final SellReference ref = AnalyticsFactory.eINSTANCE.createSellReference();
					ref.setSlot(s);
					newModel.getSells().add(ref);
					return ref;
				}
			}
		});

		final Function<VesselEvent, VesselEventOption> eventGetter = evt -> eventOptions.computeIfAbsent(evt, s -> {
			final VesselEventReference ref = AnalyticsFactory.eINSTANCE.createVesselEventReference();
			ref.setEvent(s);
			newModel.getVesselEvents().add(ref);
			return ref;
		});

		Function<Sequence, Optional<ShippingOption>> shippingOptionFunc = sequence -> {
			if (sequence != null) {
				if (sequence.getCharterInMarket() != null) {
					final CharterInMarket charterInMarket = sequence.getCharterInMarket();
					final Pair<CharterInMarket, Integer> key = new Pair<>(charterInMarket, sequence.getSpotIndex());
					if (roundTripMap.containsKey(key)) {
						return Optional.of(roundTripMap.get(key));
					} else {
						if (!(charterInMarket.eContainer() instanceof SpotMarketsModel)) {
							if (sequence.getSpotIndex() < 0) {
								final RoundTripShippingOption option = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();
								option.setVessel(charterInMarket.getVessel());
								option.setEntity(charterInMarket.getEntity());
								option.setHireCost(charterInMarket.getCharterInRate());
								newModel.getShippingTemplates().add(option);
								roundTripMap.put(key, option);
								return Optional.of(option);
							} else {
								// We do not expect a charter in market outside the spot markets model - unless it is roundtrip.
								throw new IllegalStateException();
							}
						} else {
							final ExistingCharterMarketOption opt = AnalyticsFactory.eINSTANCE.createExistingCharterMarketOption();
							opt.setCharterInMarket(charterInMarket);
							opt.setSpotIndex(sequence.getSpotIndex());
							newModel.getShippingTemplates().add(opt);
							roundTripMap.put(key, opt);
							return Optional.of(opt);
						}
					}
				} else if (sequence.getVesselAvailability() != null) {
					final VesselAvailability vesselAvailability = sequence.getVesselAvailability();
					final ExistingVesselCharterOption opt = vesselAvailOptionMap.computeIfAbsent(vesselAvailability, availOptionComputer);
					return Optional.of(opt);
				}
			}
			return Optional.empty();
		};

		final LNGScenarioModel scenarioModel = baseScenarioResult.getTypedRoot(LNGScenarioModel.class);
		assert scenarioModel != null;

		final BaseCase baseCase = AnalyticsFactory.eINSTANCE.createBaseCase();
		baseCase.setKeepExistingScenario(true);
		baseCase.setProfitAndLoss(ScheduleModelKPIUtils.getScheduleProfitAndLoss(ScenarioModelUtil.getScheduleModel(scenarioModel).getSchedule()));

		// Stage 1, generate the before case (i.e. the pin)
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
							final VesselEventVisit vesselEventVisit = (VesselEventVisit) evt;
							bRow.setVesselEventOption(eventGetter.apply(vesselEventVisit.getVesselEvent()));
						}
					}
					final ChangeSetRowData rhsData = row.getLhsBefore();
					if (rhsData != null) {
						final SlotAllocation originalLoadAllocation = rhsData.getLoadAllocation();
						if (originalLoadAllocation != null) {
							final Sequence sequence = originalLoadAllocation.getSlotVisit().getSequence();
							shippingOptionFunc.apply(sequence).ifPresent(bRow::setShipping);
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
					baseCase.getBaseCase().add(bRow);
				}
			}
		}
		newModel.setBaseCase(baseCase);
		newModel.setUseTargetPNL(true);
		final PartialCase partialCase = AnalyticsFactory.eINSTANCE.createPartialCase();
		partialCase.setKeepExistingScenario(true);

		// Stage 2, generate the after case (i.e. the diff/other)
		// This code is pretty similar to stage 1.
		for (final ChangeSetTableRow row : selectedRows) {
			final PartialCaseRow pRow = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
			if (row.isWiringChange() || row.isVesselChange()) {
				{
					final ChangeSetRowData lhsData = row.getLhsAfter();
					if (lhsData != null) {

						final Event evt = lhsData.getLhsEvent();
						if (evt instanceof VesselEventVisit) {
							final VesselEventVisit vesselEventVisit = (VesselEventVisit) evt;
							pRow.getVesselEventOptions().add(eventGetter.apply(vesselEventVisit.getVesselEvent()));
						}

						final SlotAllocation loadAllocation = lhsData.getLoadAllocation();
						if (loadAllocation != null) {
							final LoadSlot slot = (LoadSlot) loadAllocation.getSlot();
							// TODO: Not sure why we have a null check here, it may be a bug
							// as this will avoid creating an OpenBuy
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
							shippingOptionFunc.apply(sequence).ifPresent(pRow.getShipping()::add);
						}
					}
				}

				if (!pRow.getBuyOptions().isEmpty() || !pRow.getSellOptions().isEmpty() || !pRow.getVesselEventOptions().isEmpty()) {
					partialCase.getPartialCase().add(pRow);
				}
			}
			newModel.setPartialCase(partialCase);
		}

		final ScenarioModelRecord modelRecord = baseScenarioResult.getModelRecord();
		try (ModelReference ref = modelRecord.aquireReference("ChangeSetView:ContextMenuManager")) {
			final EditingDomain editingDomain = ref.getEditingDomain();
			final CompoundCommand cmd = new CompoundCommand("Create sandbox");
			cmd.append(AddCommand.create(editingDomain, ((LNGScenarioModel) ref.getInstance()).getAnalyticsModel(), AnalyticsPackage.eINSTANCE.getAnalyticsModel_OptionModels(), newModel));
			editingDomain.getCommandStack().execute(cmd);

		}
	}
}
