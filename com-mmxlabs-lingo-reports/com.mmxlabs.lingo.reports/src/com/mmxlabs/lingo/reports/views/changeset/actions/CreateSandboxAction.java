/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.actions;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.ExistingVesselAvailability;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCase;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class CreateSandboxAction extends Action {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExportChangeAction.class);
	private final ChangeSetTableGroup changeSetTableGroup;
	private String name;

	public CreateSandboxAction(final ChangeSetTableGroup changeSetTableGroup, String name) {
		super("Create sandbox");
		this.changeSetTableGroup = changeSetTableGroup;
		this.name = name;
	}

	@Override
	public void run() {

		final OptionAnalysisModel newModel = AnalyticsFactory.eINSTANCE.createOptionAnalysisModel();
		if (name != null && !name.isEmpty()) {
			newModel.setName(name);
		} else {
			newModel.setName("Insertion sandbox");
		}

		final Map<LoadSlot, BuyOption> buyOptions = new HashMap<>();
		final Map<DischargeSlot, SellOption> sellOptions = new HashMap<>();
		final Map<Pair<CharterInMarket, Integer>, ExistingCharterMarketOption> roundTripMap = new HashMap<>();
		final Map<VesselAvailability, ExistingVesselAvailability> vesselAvailOptionMap = new HashMap<>();
		final Function<VesselAvailability, ExistingVesselAvailability> availOptionComputer = (va) -> {
			final ExistingVesselAvailability opt = AnalyticsFactory.eINSTANCE.createExistingVesselAvailability();
			opt.setVesselAvailability(va);
			newModel.getShippingTemplates().add(opt);
			return opt;
		};
		final ScenarioResult baseScenarioResult = changeSetTableGroup.getBaseScenario();
		final LNGScenarioModel scenarioModel = baseScenarioResult.getTypedRoot(LNGScenarioModel.class);
		assert scenarioModel != null;

		final BaseCase baseCase = AnalyticsFactory.eINSTANCE.createBaseCase();
		baseCase.setKeepExistingScenario(true);
		baseCase.setProfitAndLoss(ScheduleModelKPIUtils.getScheduleProfitAndLoss(ScenarioModelUtil.getScheduleModel(scenarioModel).getSchedule()));

		for (final ChangeSetTableRow row : changeSetTableGroup.getRows()) {
			final BaseCaseRow bRow = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
			if (row.isWiringChange() || row.isVesselChange()) {
				{
					final ChangeSetRowData lhsData = row.getLhsAfter();
					if (lhsData != null) {
						final SlotAllocation loadAllocation = lhsData.getLoadAllocation();
						if (loadAllocation != null) {
							final LoadSlot slot = (LoadSlot) loadAllocation.getSlot();
							if (slot != null) {
								if (slot instanceof SpotSlot) {
									final SpotSlot spotSlot = (SpotSlot) slot;
									final BuyMarket ref = AnalyticsFactory.eINSTANCE.createBuyMarket();
									if (slot.isDESPurchase()) {
										ref.setMarket(spotSlot.getMarket());
									} else {
										ref.setMarket(spotSlot.getMarket());
									}
									bRow.setBuyOption(ref);

									newModel.getBuys().add(ref);
									buyOptions.put(slot, ref);
								} else {

									final BuyReference ref = AnalyticsFactory.eINSTANCE.createBuyReference();
									ref.setSlot(slot);
									bRow.setBuyOption(ref);

									newModel.getBuys().add(ref);
									buyOptions.put(slot, ref);
								}
							}
						}
					}
					final ChangeSetRowData rhsData = row.getLhsBefore();
					if (rhsData != null) {
						{
							final SlotAllocation originalLoadAllocation = rhsData.getLoadAllocation();
							if (originalLoadAllocation != null) {
								final Sequence sequence = originalLoadAllocation.getSlotVisit().getSequence();
								if (sequence != null) {
									if (sequence.getCharterInMarket() != null) {
										final CharterInMarket charterInMarket = sequence.getCharterInMarket();
										final Pair<CharterInMarket, Integer> key = new Pair<CharterInMarket, Integer>(charterInMarket, sequence.getSpotIndex());
										if (roundTripMap.containsKey(key)) {
											final ExistingCharterMarketOption opt = roundTripMap.get(key);
											bRow.setShipping(opt);
										} else {
											final ExistingCharterMarketOption opt = AnalyticsFactory.eINSTANCE.createExistingCharterMarketOption();
											opt.setCharterInMarket(charterInMarket);
											opt.setSpotIndex(sequence.getSpotIndex());
											newModel.getShippingTemplates().add(opt);
											bRow.setShipping(opt);
											roundTripMap.put(key, opt);
										}

									} else if (sequence.getVesselAvailability() != null) {
										final VesselAvailability vesselAvailability = sequence.getVesselAvailability();
										// final VesselAvailability key = vesselAvailMap.computeIfAbsent(vesselAvailability, availMapperComputer);
										final ExistingVesselAvailability opt = vesselAvailOptionMap.computeIfAbsent(vesselAvailability, availOptionComputer);
										bRow.setShipping(opt);
									}
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
						if (slot != null) {
							if (slot instanceof SpotSlot) {
								final SpotSlot spotSlot = (SpotSlot) slot;
								final SellMarket ref = AnalyticsFactory.eINSTANCE.createSellMarket();
								if (slot.isFOBSale()) {
									ref.setMarket(spotSlot.getMarket());
								} else {
									ref.setMarket(spotSlot.getMarket());
								}
								bRow.setSellOption(ref);

								newModel.getSells().add(ref);
								sellOptions.put(slot, ref);
							} else {
								final SellReference ref = AnalyticsFactory.eINSTANCE.createSellReference();
								ref.setSlot(slot);
								bRow.setSellOption(ref);

								newModel.getSells().add(ref);
								sellOptions.put(slot, ref);
							}
						}
					}
				}
				if (bRow.getBuyOption() instanceof BuyMarket && bRow.getSellOption() == null) {
					// Do not add
				} else if (bRow.getBuyOption() == null && bRow.getSellOption() instanceof SellMarket) {
					// Do not add
				} else if (bRow.getBuyOption() != null || bRow.getSellOption() != null) {
					baseCase.getBaseCase().add(bRow);
				}
				// } else if (row.isVesselChange()) {
				// baseCase.getBaseCase().add(bRow);
			}
		}
		newModel.setBaseCase(baseCase);
		newModel.setUseTargetPNL(true);
		final PartialCase partialCase = AnalyticsFactory.eINSTANCE.createPartialCase();
		partialCase.setKeepExistingScenario(true);
		for (final ChangeSetTableRow row : changeSetTableGroup.getRows()) {
			final PartialCaseRow pRow = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
			if (row.isWiringChange() || row.isVesselChange()) {

				{
					final ChangeSetRowData lhsData = row.getLhsAfter();
					if (lhsData != null) {
						final SlotAllocation loadAllocation = lhsData.getLoadAllocation();
						if (loadAllocation != null) {
							final LoadSlot slot = (LoadSlot) loadAllocation.getSlot();
							if (slot != null) {
								if (buyOptions.containsKey(slot)) {
									pRow.getBuyOptions().add(buyOptions.get(slot));
								} else {
									if (slot instanceof SpotSlot) {
										final SpotSlot spotSlot = (SpotSlot) slot;
										final BuyMarket ref = AnalyticsFactory.eINSTANCE.createBuyMarket();
										if (slot.isDESPurchase()) {
											ref.setMarket(spotSlot.getMarket());
										} else {
											ref.setMarket(spotSlot.getMarket());
										}
										pRow.getBuyOptions().add(ref);

										newModel.getBuys().add(ref);
										buyOptions.put(slot, ref);
									} else {
										final BuyReference ref = AnalyticsFactory.eINSTANCE.createBuyReference();
										ref.setSlot(slot);
										pRow.getBuyOptions().add(ref);
										buyOptions.put(slot, ref);
									}
								}
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
							if (slot != null) {
								if (sellOptions.containsKey(slot)) {
									pRow.getSellOptions().add(sellOptions.get(slot));
								} else {
									if (slot instanceof SpotSlot) {
										final SpotSlot spotSlot = (SpotSlot) slot;
										final SellMarket ref = AnalyticsFactory.eINSTANCE.createSellMarket();
										if (slot.isFOBSale()) {
											ref.setMarket(spotSlot.getMarket());
										} else {
											ref.setMarket(spotSlot.getMarket());
										}
										pRow.getSellOptions().add(ref);

										newModel.getSells().add(ref);
										sellOptions.put(slot, ref);
									} else {
										final SellReference ref = AnalyticsFactory.eINSTANCE.createSellReference();
										ref.setSlot(slot);
										pRow.getSellOptions().add(ref);

										newModel.getSells().add(ref);
										sellOptions.put(slot, ref);
									}
								}
							}
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
									final Pair<CharterInMarket, Integer> key = new Pair<CharterInMarket, Integer>(charterInMarket, sequence.getSpotIndex());
									if (roundTripMap.containsKey(key)) {
										final ExistingCharterMarketOption opt = roundTripMap.get(key);
										pRow.getShipping().add(opt);
									} else {
										final ExistingCharterMarketOption opt = AnalyticsFactory.eINSTANCE.createExistingCharterMarketOption();
										opt.setCharterInMarket(charterInMarket);
										opt.setSpotIndex(sequence.getSpotIndex());
										newModel.getShippingTemplates().add(opt);
										pRow.getShipping().add(opt);
										roundTripMap.put(key, opt);
									}

								} else if (sequence.getVesselAvailability() != null) {
									final VesselAvailability vesselAvailability = sequence.getVesselAvailability();
									// final VesselAvailability key = vesselAvailMap.computeIfAbsent(vesselAvailability, availMapperComputer);
									final ExistingVesselAvailability opt = vesselAvailOptionMap.computeIfAbsent(vesselAvailability, availOptionComputer);
									pRow.getShipping().add(opt);
								}
							}
						}
					}
				}

				if (!pRow.getBuyOptions().isEmpty() || !pRow.getSellOptions().isEmpty()) {
					partialCase.getPartialCase().add(pRow);
				}

				// } else if (row.isVesselChange()) {

				// partialCase.getPartialCase().add(pRow);
			}
			newModel.setPartialCase(partialCase);
		}

		final ScenarioModelRecord modelRecord = baseScenarioResult.getModelRecord();
		try (ModelReference ref = modelRecord.aquireReference("ChangeSetView:ContextMenuManager")) {
			final EditingDomain editingDomain = ref.getEditingDomain();
			final CompoundCommand cmd = new CompoundCommand("Create sandbox");
			if (false) {
				cmd.append(AddCommand.create(editingDomain, ((LNGScenarioModel) ref.getInstance()).getAnalyticsModel(), AnalyticsPackage.eINSTANCE.getAnalyticsModel_OptionModels(), newModel));
				editingDomain.getCommandStack().execute(cmd);
			} else {
				// Alternative code path to temporarily disable read-only mode
				List<Runnable> updates = new LinkedList<>();
				boolean readOnly = modelRecord.getScenarioInstance().isReadonly();
				modelRecord.getScenarioInstance().setReadonly(false);
				{
					if (editingDomain instanceof AdapterFactoryEditingDomain) {
						final AdapterFactoryEditingDomain adapterFactoryEditingDomain = (AdapterFactoryEditingDomain) editingDomain;
						Map<Resource, Boolean> resourceToReadOnlyMap = adapterFactoryEditingDomain.getResourceToReadOnlyMap();
						// Init map if needed.
						if (resourceToReadOnlyMap != null) {
							for (final Resource r : editingDomain.getResourceSet().getResources()) {
								Boolean b = resourceToReadOnlyMap.get(r);
								if (b != null && b == true) {
									resourceToReadOnlyMap.put(r, Boolean.FALSE);
									updates.add(() -> resourceToReadOnlyMap.put(r, Boolean.TRUE));
								}

							}
						}
					}
				}
				cmd.append(AddCommand.create(editingDomain, ((LNGScenarioModel) ref.getInstance()).getAnalyticsModel(), AnalyticsPackage.eINSTANCE.getAnalyticsModel_OptionModels(), newModel));
				editingDomain.getCommandStack().execute(cmd);
				try {
					ref.save();
				} catch (IOException e) {

				}
				// Re-eanble read-only
				// modelRecord.getScenarioInstance().setReadonly(readOnly);
				// updates.forEach(r -> r.run());
			}
		}
	}
}
