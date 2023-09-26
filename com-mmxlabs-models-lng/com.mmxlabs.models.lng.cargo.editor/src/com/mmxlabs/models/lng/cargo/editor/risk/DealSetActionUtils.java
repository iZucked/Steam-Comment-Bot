package com.mmxlabs.models.lng.cargo.editor.risk;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jface.action.Action;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DealSet;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.util.DefaultExposuresCustomiser;
import com.mmxlabs.models.lng.cargo.util.IExposuresCustomiser;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.util.ModelMarketCurveProvider;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@NonNullByDefault
public class DealSetActionUtils {
	public static Action createFromPurchaseContractsAction(final IScenarioDataProvider scenarioDataProvider, //
			final EditingDomain editingDomain, final ICommandHandler commandHandler) {
		return new RunnableAction("From purchase contracts", new Runnable() {
			
			@Override
			public void run() {
				final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioDataProvider);
				final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(scenarioDataProvider);
				final CompoundCommand cmd = populateContractGeneratedDealSets(commercialModel.getPurchaseContracts(), cargoModel, editingDomain, "purchase");
				if (!cmd.isEmpty()) {
					commandHandler.handleCommand(cmd,  null, null);
				}
			}
		});
	}
	
	public static Action createFromSalesContractsAction(final IScenarioDataProvider scenarioDataProvider, //
			final EditingDomain editingDomain, final ICommandHandler commandHandler) {
		return new RunnableAction("From sales contracts", new Runnable() {
			
			@Override
			public void run() {
				final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioDataProvider);
				final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(scenarioDataProvider);
				final CompoundCommand cmd = populateContractGeneratedDealSets(commercialModel.getSalesContracts(), cargoModel, editingDomain, "sales");
				if (!cmd.isEmpty()) {
					commandHandler.handleCommand(cmd,  null, null);
				}
			}
		});
	}
	
	private static CompoundCommand populateContractGeneratedDealSets(final List<? extends Contract> contracts, //
			final CargoModel cargoModel, final EditingDomain ed, final String suffix) {
		final CompoundCommand cmd = new CompoundCommand();
		for (final Contract contract : contracts) {
			final List<Slot<?>> usedSlots = cargoModel.getCargoes().stream() //
					.flatMap(cargo -> cargo.getSlots().stream() //
							.filter(slot -> (!(slot instanceof SpotSlot)) && slot.getContract() == contract) //
					).collect(Collectors.toList());
			if (!usedSlots.isEmpty()) {
				final DealSet dealSet = CargoFactory.eINSTANCE.createDealSet();
				dealSet.setName(String.format("%s_%s_set", contract.getName(), suffix));
				cmd.append(AddCommand.create(ed, cargoModel, CargoPackage.Literals.CARGO_MODEL__DEAL_SETS, dealSet));
				for (final Slot<?> slot : usedSlots) {
					cmd.append(AddCommand.create(ed,  dealSet, CargoPackage.Literals.DEAL_SET__SLOTS, slot));
				}
			}
		}
		return cmd;
	}
	
	public static Action createFromIndicesByQuarterAction(final IScenarioDataProvider scenarioDataProvider, //
			final EditingDomain editingDomain, final ICommandHandler commandHandler, final Iterable<IExposuresCustomiser> exposuresCustomisers) {
		return new RunnableAction("By quarter", new Runnable() {
			@Override
			public void run() {
				final ModelMarketCurveProvider mmCurveProvider = getMarketCurveProvider(scenarioDataProvider);
				final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioDataProvider);

				final Map<String, Map<Pair<Year, Integer>, List<Slot<?>>>> indexToQuarterToSlotMap = new HashMap<>();

				final Iterator<IExposuresCustomiser> iterExposuresCustomisers = exposuresCustomisers.iterator();
				final IExposuresCustomiser exposuresCustomiser = iterExposuresCustomisers.hasNext() ? iterExposuresCustomisers.next(): new DefaultExposuresCustomiser();

				cargoModel.getCargoes().stream() //
						.flatMap(cargo -> cargo.getSlots().stream().filter(slot -> (!(slot instanceof SpotSlot))))
						.forEach(slot -> {
							final String priceExpression = exposuresCustomiser.provideExposedPriceExpression(slot);
							for (final AbstractYearMonthCurve curve : mmCurveProvider.getLinkedCurves(priceExpression, PriceIndexType.COMMODITY)) {
								if (curve instanceof final CommodityCurve comCurve) {
									if (comCurve.isSetMarketIndex()) {
										String marketIndexName = comCurve.getMarketIndex().getName();
										final LocalDate slotDate = slot.getWindowStart();
										Pair<Year, Integer> quarter = new Pair<>(Year.from(slotDate), dateToQuarter(slotDate));
										Map<Pair<Year, Integer>, List<Slot<?>>> quarterToSlotMap = indexToQuarterToSlotMap.get(marketIndexName);
										List <Slot<?>> slotList;
										if (quarterToSlotMap == null) {
											quarterToSlotMap = new HashMap<>();
											indexToQuarterToSlotMap.put(marketIndexName, quarterToSlotMap);
											slotList = new LinkedList<>();
											quarterToSlotMap.put(quarter, slotList);
										} else {
											slotList = quarterToSlotMap.get(quarter);
											if (slotList == null) {
												slotList = new LinkedList<>();
												quarterToSlotMap.put(quarter, slotList);
											}
										}
										slotList.add(slot);
									}
								}
							}
						});
				final CompoundCommand cmd = new CompoundCommand();
				final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
				indexToQuarterToSlotMap.forEach((indexName, quarterToSlotMap) -> //
					quarterToSlotMap.forEach((quarter, slots) -> {
						final DealSet dealSet = CargoFactory.eINSTANCE.createDealSet();
						dealSet.setName(String.format("%s_%s_Q%d_index_set", indexName, quarter.getFirst().format(formatter), quarter.getSecond()));
						cmd.append(AddCommand.create(editingDomain, cargoModel, CargoPackage.Literals.CARGO_MODEL__DEAL_SETS, dealSet));
						slots.forEach(slot -> cmd.append(AddCommand.create(editingDomain, dealSet, CargoPackage.Literals.DEAL_SET__SLOTS, slot)));
					})
				);
				if (!cmd.isEmpty()) {
					commandHandler.handleCommand(cmd, null, null);
				}
			}
		});
	}
	
	public static Action createFromIndicesByMonthAction(final IScenarioDataProvider scenarioDataProvider, //
			final EditingDomain editingDomain, final ICommandHandler commandHandler, final Iterable<IExposuresCustomiser> exposuresCustomisers) {
		return new RunnableAction("By month", new Runnable() {
			@Override
			public void run() {
				final ModelMarketCurveProvider mmCurveProvider = getMarketCurveProvider(scenarioDataProvider);
				final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioDataProvider);

				final Map<String, Map<YearMonth, List<Slot<?>>>> indexToDateToSlotMap = new HashMap<>();

				final Iterator<IExposuresCustomiser> iterExposuresCustomisers = exposuresCustomisers.iterator();
				final IExposuresCustomiser exposuresCustomiser = iterExposuresCustomisers.hasNext() ? iterExposuresCustomisers.next(): new DefaultExposuresCustomiser();
				
				cargoModel.getCargoes().stream() //
						.flatMap(cargo -> cargo.getSlots().stream().filter(slot -> (!(slot instanceof SpotSlot))))
						.forEach(slot -> {
							final String priceExpression = exposuresCustomiser.provideExposedPriceExpression(slot);
							for (final AbstractYearMonthCurve curve : mmCurveProvider.getLinkedCurves(priceExpression, PriceIndexType.COMMODITY)) {
								if (curve instanceof final CommodityCurve comCurve) {
									if (comCurve.isSetMarketIndex()) {
										String marketIndexName = comCurve.getMarketIndex().getName();
										final YearMonth ym = YearMonth.from(slot.getWindowStart());
										Map<YearMonth, List<Slot<?>>> dateToSlotMap = indexToDateToSlotMap.get(marketIndexName);
										List <Slot<?>> slotList;
										if (dateToSlotMap == null) {
											dateToSlotMap = new HashMap<>();
											indexToDateToSlotMap.put(marketIndexName, dateToSlotMap);
											slotList = new LinkedList<>();
											dateToSlotMap.put(ym, slotList);
										} else {
											slotList = dateToSlotMap.get(ym);
											if (slotList == null) {
												slotList = new LinkedList<>();
												dateToSlotMap.put(ym, slotList);
											}
										}
										slotList.add(slot);
									}
								}
							}
						});
				final CompoundCommand cmd = new CompoundCommand();
				final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM_yyyy");
				indexToDateToSlotMap.forEach((indexName, dateToSlotMap) -> //
					dateToSlotMap.forEach((ym, slots) -> {
						final DealSet dealSet = CargoFactory.eINSTANCE.createDealSet();
						dealSet.setName(String.format("%s_%s_index_set", indexName, ym.format(formatter)));
						cmd.append(AddCommand.create(editingDomain, cargoModel, CargoPackage.Literals.CARGO_MODEL__DEAL_SETS, dealSet));
						slots.forEach(slot -> cmd.append(AddCommand.create(editingDomain, dealSet, CargoPackage.Literals.DEAL_SET__SLOTS, slot)));
					})
				);
				if (!cmd.isEmpty()) {
					commandHandler.handleCommand(cmd, null, null);
				}
			}
		});
	}
	
	public static Action createFromCurvesByMonthAction(final IScenarioDataProvider scenarioDataProvider, //
			final EditingDomain editingDomain, final ICommandHandler commandHandler, final Iterable<IExposuresCustomiser> exposuresCustomisers) {
		return new RunnableAction("By month", new Runnable() {
			@Override
			public void run() {
				final ModelMarketCurveProvider mmCurveProvider = getMarketCurveProvider(scenarioDataProvider);
				final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioDataProvider);

				final Map<String, Map<YearMonth, List<Slot<?>>>> curveToDateToSlotMap = new HashMap<>();

				final Iterator<IExposuresCustomiser> iterExposuresCustomisers = exposuresCustomisers.iterator();
				final IExposuresCustomiser exposuresCustomiser = iterExposuresCustomisers.hasNext() ? iterExposuresCustomisers.next(): new DefaultExposuresCustomiser();
				
				cargoModel.getCargoes().stream() //
						.flatMap(cargo -> cargo.getSlots().stream().filter(slot -> (!(slot instanceof SpotSlot))))
						.forEach(slot -> {
							final String priceExpression = exposuresCustomiser.provideExposedPriceExpression(slot);
							final Collection<AbstractYearMonthCurve> curves = mmCurveProvider.getLinkedCurves(priceExpression, PriceIndexType.COMMODITY);
							for (final AbstractYearMonthCurve curve : curves) {
								final String curveName = curve.getName();
								if (curveName != null) {
									final YearMonth ym = YearMonth.from(slot.getWindowStart());
									Map<YearMonth, List<Slot<?>>> dateToSlotMap = curveToDateToSlotMap.get(curveName);
									List <Slot<?>> slotList;
									if (dateToSlotMap == null) {
										dateToSlotMap = new HashMap<>();
										curveToDateToSlotMap.put(curveName, dateToSlotMap);
										slotList = new LinkedList<>();
										dateToSlotMap.put(ym, slotList);
									} else {
										slotList = dateToSlotMap.get(ym);
										if (slotList == null) {
											slotList = new LinkedList<>();
											dateToSlotMap.put(ym, slotList);
										}
									}
									slotList.add(slot);
								}
							}
						});
				final CompoundCommand cmd = new CompoundCommand();
				final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM_yyyy");
				curveToDateToSlotMap.forEach((curveName, dateToSlotMap) -> //
					dateToSlotMap.forEach((ym, slots) -> {
						final DealSet dealSet = CargoFactory.eINSTANCE.createDealSet();
						dealSet.setName(String.format("%s_%s_curve_set", curveName, ym.format(formatter)));
						cmd.append(AddCommand.create(editingDomain, cargoModel, CargoPackage.Literals.CARGO_MODEL__DEAL_SETS, dealSet));
						slots.forEach(slot -> cmd.append(AddCommand.create(editingDomain, dealSet, CargoPackage.Literals.DEAL_SET__SLOTS, slot)));
					})
				);
				if (!cmd.isEmpty()) {
					commandHandler.handleCommand(cmd, null, null);
				}
			}
		});
	}

	public static Action createFromCurvesByQuarterAction(final IScenarioDataProvider scenarioDataProvider, //
			final EditingDomain editingDomain, final ICommandHandler commandHandler, final Iterable<IExposuresCustomiser> exposuresCustomisers) {
		return new RunnableAction("By quarter", new Runnable() {
			@Override
			public void run() {
				final ModelMarketCurveProvider mmCurveProvider = getMarketCurveProvider(scenarioDataProvider);
				final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioDataProvider);

				final Map<String, Map<Pair<Year, Integer>, List<Slot<?>>>> curveToQuarterToSlotMap = new HashMap<>();

				final Iterator<IExposuresCustomiser> iterExposuresCustomisers = exposuresCustomisers.iterator();
				final IExposuresCustomiser exposuresCustomiser = iterExposuresCustomisers.hasNext() ? iterExposuresCustomisers.next(): new DefaultExposuresCustomiser();

				cargoModel.getCargoes().stream() //
						.flatMap(cargo -> cargo.getSlots().stream().filter(slot -> (!(slot instanceof SpotSlot))))
						.forEach(slot -> {
							final String priceExpression = exposuresCustomiser.provideExposedPriceExpression(slot);
							final Collection<AbstractYearMonthCurve> curves = mmCurveProvider.getLinkedCurves(priceExpression, PriceIndexType.COMMODITY);
							for (final AbstractYearMonthCurve curve : curves) {
								final String curveName = curve.getName();
								if (curveName != null) {
									final LocalDate slotDate = slot.getWindowStart();
									Pair<Year, Integer> quarter = new Pair<>(Year.from(slotDate), dateToQuarter(slotDate));
									Map<Pair<Year, Integer>, List<Slot<?>>> quarterToSlotMap = curveToQuarterToSlotMap.get(curveName);
									List<Slot<?>> slotList;
									if (quarterToSlotMap == null) {
										quarterToSlotMap = new HashMap<>();
										curveToQuarterToSlotMap.put(curveName, quarterToSlotMap);
										slotList = new LinkedList<>();
										quarterToSlotMap.put(quarter, slotList);
									} else {
										slotList = quarterToSlotMap.get(quarter);
										if (slotList == null) {
											slotList = new LinkedList<>();
											quarterToSlotMap.put(quarter, slotList);
										}
									}
									slotList.add(slot);
								}
							}
						});
				final CompoundCommand cmd = new CompoundCommand();
				final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
				curveToQuarterToSlotMap.forEach((curveName, quarterToSlotMap) -> //
					quarterToSlotMap.forEach((quarter, slots) -> {
						final DealSet dealSet = CargoFactory.eINSTANCE.createDealSet();
						dealSet.setName(String.format("%s_%s_Q%d_curve_set", curveName, quarter.getFirst().format(formatter), quarter.getSecond()));
						cmd.append(AddCommand.create(editingDomain, cargoModel, CargoPackage.Literals.CARGO_MODEL__DEAL_SETS, dealSet));
						slots.forEach(slot -> cmd.append(AddCommand.create(editingDomain, dealSet, CargoPackage.Literals.DEAL_SET__SLOTS, slot)));
					})
				);
				if (!cmd.isEmpty()) {
					commandHandler.handleCommand(cmd, null, null);
				}
			}
		});
	}
	
	public static Action createFromCargoesWithoutPapersAction(final IScenarioDataProvider scenarioDataProvider, //
			final EditingDomain editingDomain, final ICommandHandler commandHandler) {
		return new RunnableAction("Cargoes only", new Runnable() {
			
			@Override
			public void run() {
				final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioDataProvider);
				final Set<Slot<?>> usedSlots = updateSlots(scenarioDataProvider);
				final CompoundCommand cmd = new CompoundCommand();
				for(final Cargo cargo : cargoModel.getCargoes()) {
					if (checkContainment(cargo, usedSlots)) continue;
					final DealSet ds = CargoFactory.eINSTANCE.createDealSet();
					ds.setName(String.format("%s_%s", cargo.getLoadName(), "set"));
					cmd.append(AddCommand.create(editingDomain, cargoModel, CargoPackage.Literals.CARGO_MODEL__DEAL_SETS, ds));
					for (final Slot<?> slot : cargo.getSlots()) {
						if (slot instanceof SpotSlot) continue;
						cmd.append(AddCommand.create(editingDomain, ds, CargoPackage.Literals.DEAL_SET__SLOTS, slot));
					}
				}
				if(!cmd.isEmpty()) {
					commandHandler.handleCommand(cmd, null, null);
				}
			}
		});
	}
	
	public static Action createFromCargoesAndUpdateWithPaperDealCommentsAction(final IScenarioDataProvider scenarioDataProvider, //
			final EditingDomain editingDomain, final ICommandHandler commandHandler) {
		return new RunnableAction("With paper deals", new Runnable() {
			
			@Override
			public void run() {
				final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioDataProvider);
				final Set<Slot<?>> usedSlots = updateSlots(scenarioDataProvider);
				final CompoundCommand cmd = new CompoundCommand();
				for(final Cargo cargo : cargoModel.getCargoes()) {
					if (checkContainment(cargo, usedSlots)) continue;
					final DealSet ds = CargoFactory.eINSTANCE.createDealSet();
					ds.setName(String.format("%s_%s", cargo.getLoadName(), "set"));
					cmd.append(AddCommand.create(editingDomain, cargoModel, CargoPackage.Literals.CARGO_MODEL__DEAL_SETS, ds));
					for (final Slot<?> slot : cargo.getSlots()) {
						if (slot instanceof SpotSlot) continue;
						cmd.append(AddCommand.create(editingDomain, ds, CargoPackage.Literals.DEAL_SET__SLOTS, slot));
						for (final PaperDeal pd : cargoModel.getPaperDeals()) {
							if (slot.getName() != null && pd.getComment() != null && !pd.getComment().isBlank()) {
								if (pd.getComment().equalsIgnoreCase(slot.getName())) {
									cmd.append(AddCommand.create(editingDomain,  ds, CargoPackage.Literals.DEAL_SET__PAPER_DEALS, pd));
								}
							}
						}
					}
				}
				if(!cmd.isEmpty()) {
					commandHandler.handleCommand(cmd, null, null);
				}
			}
		});
	}
	
	private static int dateToQuarter(final LocalDate date) {
		switch (date.getMonth()) {
		case JANUARY,FEBRUARY,MARCH:
			return 1;
		case APRIL,MAY,JUNE:
			return 2;
		case JULY,AUGUST,SEPTEMBER:
			return 3;
		case OCTOBER,NOVEMBER,DECEMBER:
			return 4;
		default:
			throw new IllegalStateException();
		}
	}
	
	
	
	private static @NonNull ModelMarketCurveProvider getMarketCurveProvider(final @NonNull IScenarioDataProvider scenarioDataProvider) {
		if (scenarioDataProvider != null) {
			final ModelMarketCurveProvider provider = scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.MARKET_CURVES, ModelMarketCurveProvider.class);
			if (provider != null) {
				return provider;
			}
		}
		throw new IllegalStateException("Unable to get market curve provider");
	}
	
	private static Set<Slot<?>> updateSlots(final IScenarioDataProvider sdp) {
		final Set<Slot<?>> usedSlots = new HashSet<>();
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sdp);
		for (final DealSet dealSet : cargoModel.getDealSets()) {
			usedSlots.addAll(dealSet.getSlots());
		}
		return usedSlots;
	}
	
	private static boolean checkContainment(final Cargo cargo, final Set<Slot<?>> usedSlots) {
		for (final Slot<?> slot : cargo.getSlots()) {
			if (usedSlots.contains(slot)) return true;
		}
		return false;
	}
}
