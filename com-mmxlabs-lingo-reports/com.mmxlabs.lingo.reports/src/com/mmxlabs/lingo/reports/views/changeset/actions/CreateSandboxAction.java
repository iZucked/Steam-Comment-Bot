package com.mmxlabs.lingo.reports.views.changeset.actions;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
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
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class CreateSandboxAction extends Action {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExportChangeAction.class);
	private final ChangeSetTableGroup changeSetTableGroup;

	public CreateSandboxAction(final ChangeSetTableGroup changeSetTableGroup) {
		super("Create sandbox");
		this.changeSetTableGroup = changeSetTableGroup;
	}

	public void run() {

		final Map<EObject, EObject> childToBaseMapping = new HashMap<>();

		// TODO: Create sandbox on base case (need mapping to original slots,markets, vessels etc)
		// TODO: Market slots!
		// TODO: Create better shipping bits
		// TODO: Needs better fleet model
		// TODO: Need to fix cargo sort order for sandbox!

		final OptionAnalysisModel newModel = AnalyticsFactory.eINSTANCE.createOptionAnalysisModel();
		newModel.setName("Insertion plan");

		final Map<LoadSlot, BuyOption> buyOptions = new HashMap<>();
		final Map<DischargeSlot, SellOption> sellOptions = new HashMap<>();
		final Map<Pair<CharterInMarket, Integer>, ExistingCharterMarketOption> roundTripMap = new HashMap<>();
		// Map from copy scenario so instance in original! (Thus do search once).
		final Map<VesselAvailability, VesselAvailability> vesselAvailMap = new HashMap<>();
		final Map<VesselAvailability, ExistingVesselAvailability> vesselAvailOptionMap = new HashMap<>();
		final Function<VesselAvailability, ExistingVesselAvailability> availOptionComputer = (va) -> {
			final ExistingVesselAvailability opt = AnalyticsFactory.eINSTANCE.createExistingVesselAvailability();
			opt.setVesselAvailability(va);

			newModel.getShippingTemplates().add(opt);

			return opt;
		};
		final ScenarioResult baseScenarioResult = changeSetTableGroup.getChangeSet().getBaseScenario();
		final LNGScenarioModel scenarioModel = baseScenarioResult.getTypedRoot(LNGScenarioModel.class);
		assert scenarioModel != null;
		final Function<VesselAvailability, VesselAvailability> availMapperComputer = (va) -> {

			@NonNull
			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);
			for (final VesselAvailability this_va : cargoModel.getVesselAvailabilities()) {
				if (!this_va.getVessel().getName().contentEquals(va.getVessel().getName())) {
					continue;
				}
				if (!java.util.Objects.equals(this_va.getCharterNumber(), va.getCharterNumber())) {
					continue;
				}
				// Assume if the above is a match, then this is pretty likely to be a match,
				return this_va;
			}
			return null;
		};

		final BaseCase baseCase = AnalyticsFactory.eINSTANCE.createBaseCase();
		baseCase.setKeepExistingScenario(true);
		baseCase.setProfitAndLoss(ScheduleModelKPIUtils.getScheduleProfitAndLoss(ScenarioModelUtil.getScheduleModel(scenarioModel).getSchedule()));

		final Map<String, LoadSlot> baseLoadSlotMap = ScenarioModelUtil.getCargoModel(scenarioModel) //
				.getLoadSlots().stream() //
				.collect(Collectors.toMap(s -> s.getName(), s -> s));
		final Map<String, DischargeSlot> baseDischargeSlotMap = ScenarioModelUtil.getCargoModel(scenarioModel) //
				.getDischargeSlots().stream() //
				.collect(Collectors.toMap(s -> s.getName(), s -> s));

		final Map<String, SpotMarket> dpMarkets = ScenarioModelUtil.getSpotMarketsModel(scenarioModel) //
				.getDesPurchaseSpotMarket().getMarkets().stream() //
				.collect(Collectors.toMap(s -> s.getName(), s -> s));
		final Map<String, SpotMarket> dsMarkets = ScenarioModelUtil.getSpotMarketsModel(scenarioModel) //
				.getDesSalesSpotMarket().getMarkets().stream() //
				.collect(Collectors.toMap(s -> s.getName(), s -> s));
		final Map<String, SpotMarket> fsMarkets = ScenarioModelUtil.getSpotMarketsModel(scenarioModel) //
				.getFobSalesSpotMarket().getMarkets().stream() //
				.collect(Collectors.toMap(s -> s.getName(), s -> s));
		final Map<String, SpotMarket> fpMarkets = ScenarioModelUtil.getSpotMarketsModel(scenarioModel) //
				.getFobPurchasesSpotMarket().getMarkets().stream() //
				.collect(Collectors.toMap(s -> s.getName(), s -> s));

		final Map<String, CharterInMarket> charterInMarkets = ScenarioModelUtil.getSpotMarketsModel(scenarioModel) //
				.getCharterInMarkets().stream() //
				.collect(Collectors.toMap(s -> s.getName(), s -> s));

		for (final ChangeSetTableRow row : changeSetTableGroup.getRows()) {
			final BaseCaseRow bRow = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
			if (row.isWiringChange()) {
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
										ref.setMarket(dpMarkets.get(spotSlot.getMarket().getName()));
									} else {
										ref.setMarket(fpMarkets.get(spotSlot.getMarket().getName()));
									}
									bRow.setBuyOption(ref);

									newModel.getBuys().add(ref);
									buyOptions.put(slot, ref);
								} else {

									final BuyReference ref = AnalyticsFactory.eINSTANCE.createBuyReference();
									ref.setSlot(baseLoadSlotMap.get(slot.getName()));
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
										final CharterInMarket charterInMarket = charterInMarkets.get(sequence.getCharterInMarket().getName());
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
										final VesselAvailability key = vesselAvailMap.computeIfAbsent(vesselAvailability, availMapperComputer);
										final ExistingVesselAvailability opt = vesselAvailOptionMap.computeIfAbsent(key, availOptionComputer);
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
									ref.setMarket(fsMarkets.get(spotSlot.getMarket().getName()));
								} else {
									ref.setMarket(dsMarkets.get(spotSlot.getMarket().getName()));
								}
								bRow.setSellOption(ref);

								newModel.getSells().add(ref);
								sellOptions.put(slot, ref);
							} else {
								final SellReference ref = AnalyticsFactory.eINSTANCE.createSellReference();
								ref.setSlot(baseDischargeSlotMap.get(slot.getName()));
								bRow.setSellOption(ref);

								newModel.getSells().add(ref);
								sellOptions.put(slot, ref);
							}
						}
					}
				}

				baseCase.getBaseCase().add(bRow);
			} else if (row.isVesselChange()) {
				// baseCase.getBaseCase().add(bRow);
			}
		}
		newModel.setBaseCase(baseCase);
		newModel.setUseTargetPNL(true);
		final PartialCase partialCase = AnalyticsFactory.eINSTANCE.createPartialCase();
		partialCase.setKeepExistingScenario(true);
		for (final ChangeSetTableRow row : changeSetTableGroup.getRows()) {
			final PartialCaseRow pRow = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
			if (row.isWiringChange()) {

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
											ref.setMarket(dpMarkets.get(spotSlot.getMarket().getName()));
										} else {
											ref.setMarket(fpMarkets.get(spotSlot.getMarket().getName()));
										}
										pRow.getBuyOptions().add(ref);

										newModel.getBuys().add(ref);
										buyOptions.put(slot, ref);
									} else {
										final BuyReference ref = AnalyticsFactory.eINSTANCE.createBuyReference();
										ref.setSlot(baseLoadSlotMap.get(slot.getName()));
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
											ref.setMarket(fsMarkets.get(spotSlot.getMarket().getName()));
										} else {
											ref.setMarket(dsMarkets.get(spotSlot.getMarket().getName()));
										}
										pRow.getSellOptions().add(ref);

										newModel.getSells().add(ref);
										sellOptions.put(slot, ref);
									} else {
										final SellReference ref = AnalyticsFactory.eINSTANCE.createSellReference();
										ref.setSlot(baseDischargeSlotMap.get(slot.getName()));
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
									final CharterInMarket charterInMarket = charterInMarkets.get(sequence.getCharterInMarket().getName());
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
									final VesselAvailability key = vesselAvailMap.computeIfAbsent(vesselAvailability, availMapperComputer);
									final ExistingVesselAvailability opt = vesselAvailOptionMap.computeIfAbsent(key, availOptionComputer);
									pRow.getShipping().add(opt);
								}
							}
						}
					}
				}

				if (!pRow.getBuyOptions().isEmpty() || !pRow.getSellOptions().isEmpty()) {
					partialCase.getPartialCase().add(pRow);
				}

			} else if (row.isVesselChange()) {

				// partialCase.getPartialCase().add(pRow);
			}
			newModel.setPartialCase(partialCase);
		}

		final ScenarioInstance scenarioInstance = baseScenarioResult.getScenarioInstance();
		@NonNull
		final
		ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
		try (ModelReference ref = modelRecord.aquireReference("ChangeSetView:ContextMenuManager")) {
			final EditingDomain ed = ref.getEditingDomain();
			final CompoundCommand cmd = new CompoundCommand("Create sandbox");
			cmd.append(AddCommand.create(ed, ((LNGScenarioModel) ref.getInstance()).getAnalyticsModel(), AnalyticsPackage.eINSTANCE.getAnalyticsModel_OptionModels(), newModel));
			ed.getCommandStack().execute(cmd);
		}
	}
}
