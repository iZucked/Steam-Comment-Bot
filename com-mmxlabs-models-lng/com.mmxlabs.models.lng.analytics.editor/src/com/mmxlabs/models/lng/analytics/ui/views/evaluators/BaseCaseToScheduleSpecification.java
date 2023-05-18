/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.Months;
import com.mmxlabs.common.util.TriConsumer;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BaseCaseRowOptions;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.FullVesselCharterOption;
import com.mmxlabs.models.lng.analytics.OpenBuy;
import com.mmxlabs.models.lng.analytics.OpenSell;
import com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.VesselEventOption;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ShippingType;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.SlotMode;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.NonShippedCargoSpecification;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.cargo.ScheduleSpecificationEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SlotSpecification;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.VesselEventSpecification;
import com.mmxlabs.models.lng.cargo.VesselScheduleSpecification;
import com.mmxlabs.models.lng.cargo.VoyageSpecification;
import com.mmxlabs.models.lng.cargo.util.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.util.CollectedAssignment;
import com.mmxlabs.models.lng.cargo.util.scheduling.FakeCargo;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class BaseCaseToScheduleSpecification {

	private final LNGScenarioModel scenarioModel;
	private final IMapperClass mapper;
	private final IScenarioDataProvider scenarioDataProvider;
	private final Set<String> usedIDs;

	public BaseCaseToScheduleSpecification(final IScenarioDataProvider scenarioDataProvider, final IMapperClass mapper) {
		this.scenarioDataProvider = scenarioDataProvider;
		this.scenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
		this.mapper = mapper;
		this.usedIDs = getUsedSlotIDs(scenarioModel);
	}

	public ScheduleSpecification generate(final BaseCase baseCase, boolean useExistingBaseCase, final boolean includePartialRowShipping) {

		final Map<EObject, BaseCaseRow> elementToRowMap = new HashMap<>();

		final Map<Slot<?>, AssignableElement> slotToCargoMap = new HashMap<>();
		final Collection<LoadSlot> unusedLoads = new LinkedHashSet<>();
		final Collection<DischargeSlot> unusedDischarges = new LinkedHashSet<>();
		final Collection<VesselEvent> unusedVesselEvents = new LinkedHashSet<>();

		final Map<AssignableElement, @Nullable CollectedAssignment> cargoToCollectedAssignmentMap = new HashMap<>();
		BiConsumer<AssignableElement, ShippingOption> setShipping;

		final ModelDistanceProvider modelDistanceProvider = scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);
		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);

		final Map<VesselCharter, CollectedAssignment> vaMap = new HashMap<>();
		final Map<Pair<CharterInMarket, Integer>, CollectedAssignment> mktMap = new HashMap<>();
		final List<CollectedAssignment> assignments;
		if (useExistingBaseCase) {

			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioDataProvider);

			for (final LoadSlot s : cargoModel.getLoadSlots()) {
				unusedLoads.add(s);
				final Cargo c = s.getCargo();
				if (c != null) {
					slotToCargoMap.put(s, c);
				}
			}
			for (final DischargeSlot s : cargoModel.getDischargeSlots()) {
				unusedDischarges.add(s);
				final Cargo c = s.getCargo();
				if (c != null) {
					slotToCargoMap.put(s, c);
				}
			}
			for (final VesselEvent s : cargoModel.getVesselEvents()) {
				unusedVesselEvents.add(s);
			}

			final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(scenarioDataProvider);

			// Mapping of cargo to vessel assignment (or null if non-shipped)
			assignments = AssignmentEditorHelper.collectAssignments(cargoModel, portModel, spotMarketsModel, modelDistanceProvider, null, mapper.getExtraDataProvider());

			for (final CollectedAssignment seq : assignments) {
				if (seq.getVesselCharter() != null) {
					vaMap.put(seq.getVesselCharter(), seq);
				} else {
					mktMap.put(new Pair<>(seq.getCharterInMarket(), seq.getSpotIndex()), seq);
				}
				for (final AssignableElement assignedObject : seq.getAssignedObjects()) {
					if (assignedObject instanceof final Cargo cargo) {
						cargoToCollectedAssignmentMap.put(cargo, seq);
					} else if (assignedObject instanceof final VesselEvent vesselEvent) {
						cargoToCollectedAssignmentMap.put(vesselEvent, seq);
					}
				}
			}

			// Add mapping for non-shipped cargoes
			for (final Cargo cargo : cargoModel.getCargoes()) {
				if (cargo.getCargoType() != CargoType.FLEET) {
					cargoToCollectedAssignmentMap.put(cargo, null);
				}
			}

		} else {
			assignments = new LinkedList<>();
		}
		setShipping = (assignableElement, shipping) -> getShippingOption(modelDistanceProvider, portModel, vaMap, mktMap, cargoToCollectedAssignmentMap, assignableElement, shipping);

		// Function to clean up an existing allocation
		final Consumer<Slot<?>> removeExistingCargoAssignment = slot -> {
			if (slotToCargoMap.containsKey(slot)) {
				final Cargo cargo = (Cargo) slotToCargoMap.get(slot);
				cargo.getSortedSlots().forEach(slotToCargoMap::remove);
				final CollectedAssignment collectedAssignment = cargoToCollectedAssignmentMap.get(cargo);
				if (collectedAssignment != null) {
					collectedAssignment.getAssignedObjects().remove(cargo);
				}
				cargoToCollectedAssignmentMap.remove(cargo);
			}
		};
		final Consumer<VesselEvent> removeExistingVesselEventAssignment = event -> {
			final CollectedAssignment collectedAssignment = cargoToCollectedAssignmentMap.get(event);
			if (collectedAssignment != null) {
				collectedAssignment.getAssignedObjects().remove(event);
				cargoToCollectedAssignmentMap.remove(event);
			}
		};

		for (final BaseCaseRow row : baseCase.getBaseCase()) {
			BaseCaseRow firstRow = null;

			ShippingType shippingType = null;
			final List<BuyOption> buys = new LinkedList<>();
			final List<SellOption> sells = new LinkedList<>();
			VesselEventOption vesselEventOption = null;

			// Gather data
			{
				if (firstRow == null) {
					firstRow = row;
				}
				if (row.getVesselEventOption() != null) {
					assert row.getBuyOption() == null;
					assert row.getSellOption() == null;
					vesselEventOption = row.getVesselEventOption();
				} else {
					if (row.getBuyOption() != null && !(row.getBuyOption() instanceof OpenBuy)) {
						buys.add(row.getBuyOption());
					}
					if (row.getSellOption() != null && !(row.getSellOption() instanceof OpenSell)) {
						sells.add(row.getSellOption());
					}
				}
			}
			assert firstRow != null;

			// Determine shipping type
			if (vesselEventOption != null) {
				assert buys.isEmpty();
				assert sells.isEmpty();
				shippingType = ShippingType.Shipped;
			} else if ((buys.isEmpty() && sells.size() == 1) || (sells.isEmpty() && buys.size() == 1)) {
				shippingType = ShippingType.Open;
			} else {
				for (final var buy : buys) {
					if (AnalyticsBuilder.getBuyShippingType(buy) == ShippingType.NonShipped) {
						shippingType = ShippingType.NonShipped;
						break;
					}
				}
				if (shippingType == null) {
					for (final var sell : sells) {
						if (AnalyticsBuilder.getSellShippingType(sell) == ShippingType.NonShipped) {
							shippingType = ShippingType.NonShipped;
							break;
						}
					}
				}
				if (shippingType == null) {
					if (includePartialRowShipping || (!buys.isEmpty() && !sells.isEmpty())) {
						shippingType = ShippingType.Shipped;
					}
				}
			}
			if (shippingType == ShippingType.Shipped || shippingType == ShippingType.NonShipped) {
				AssignableElement assignableElement;
				if (vesselEventOption != null) {
					assert shippingType == ShippingType.Shipped;
					final VesselEvent event = getOrCreate(row.getVesselEventOption());
					assignableElement = event;
					elementToRowMap.put(event, row);
					removeExistingVesselEventAssignment.accept(event);
				} else {
					final List<Object> options = new LinkedList<>();
					options.addAll(buys);
					options.addAll(sells);
					boolean usedADate = false;

					final List<Slot<?>> slots = new LinkedList<>();
					for (final var option : options) {
						BaseCaseRow optionRow = null;
						{
							if (row.getBuyOption() == option || row.getSellOption() == option) {
								optionRow = row;
							}
						}
						assert optionRow != null;
						// For shipped, we want to cover the next/previous month as voyages can take time
						int shift = shippingType == ShippingType.Shipped ? 1 : 0;
						if (option instanceof final BuyOption buy) {
							LoadSlot loadSlot;
							if (buyNeedsDate(buy)) {
								assert !usedADate;
								usedADate = true;
								final var dischargeSlot = getOrCreate(sells.get(0));
								loadSlot = getOrCreate(buy, dischargeSlot.getWindowStart(), shift);
							} else {
								loadSlot = getOrCreate(buy);
							}
							elementToRowMap.put(loadSlot, optionRow);
							removeExistingCargoAssignment.accept(loadSlot);
							slots.add(loadSlot);
						} else if (option instanceof final SellOption sell) {
							DischargeSlot dischargeSlot;
							if (sellNeedsDate(sell)) {
								assert !usedADate;
								usedADate = true;
								final var loadSlot = getOrCreate(buys.get(buys.size() - 1));
								dischargeSlot = getOrCreate(sell, loadSlot.getWindowStart(), shift);
							} else {
								dischargeSlot = getOrCreate(sell);
							}
							elementToRowMap.put(dischargeSlot, optionRow);
							removeExistingCargoAssignment.accept(dischargeSlot);
							slots.add(dischargeSlot);
						}

					}
					if (buys.size() >= 1 && sells.size() >= 1) {
						assert slots.size() >= 2;
						final FakeCargo c = new FakeCargo(slots);
						slots.forEach(s -> slotToCargoMap.put(s, c));
						assignableElement = c;
					} else {
						assignableElement = null;
					}
				}
				if (assignableElement != null || includePartialRowShipping) {
					final ShippingOption shipping = firstRow.getShipping();
					if (shipping != null) {
						setShipping.accept(assignableElement, shipping);
					} else {
						assert shippingType == ShippingType.NonShipped;
						cargoToCollectedAssignmentMap.put(assignableElement, null);
					}
				}
			} else if (shippingType == ShippingType.Open || shippingType == ShippingType.None) {
				for (final var buy : buys) {
					final LoadSlot slot = getOrCreate(buy);
					if (slot != null) {
						unusedLoads.add(slot);
						removeExistingCargoAssignment.accept(slot);
					}
				}
				for (final var sell : sells) {
					final DischargeSlot slot = getOrCreate(sell);
					if (slot != null) {
						unusedDischarges.add(slot);
						removeExistingCargoAssignment.accept(slot);
					}
				}
				if (includePartialRowShipping) {
					final ShippingOption shipping = firstRow.getShipping();
					if (shipping != null) {
						setShipping.accept(null, shipping);
					}
				}
			}
		}

		final ScheduleSpecification scheduleSpecification = CargoFactory.eINSTANCE.createScheduleSpecification();
		final Set<Slot<?>> seenSlots = new HashSet<>();
		final Set<AssignableElement> seenCargoes = new HashSet<>();
		final Set<VesselEvent> seenEvents = new HashSet<>();

		final BiConsumer<CollectedAssignment, VesselScheduleSpecification> action = (seq, vesselScheduleSpecification) -> {
			seq.resort(portModel, modelDistanceProvider, null);

			final Map<EObject, VoyageSpecification> voyageSpecs = new HashMap<>();
			final Function<EObject, VoyageSpecification> builder = k -> {
				final VoyageSpecification spec = CargoFactory.eINSTANCE.createVoyageSpecification();
				vesselScheduleSpecification.getEvents().add(spec);
				return spec;
			};

			final TriConsumer<EObject, BaseCaseRowOptions, ScheduleSpecificationEvent> applyOptions = (target, options, scheduleSpec) -> {
				if (options != null) {
					if (target instanceof final LoadSlot ls) {
						if (options.isSetLadenRoute()) {
							voyageSpecs.computeIfAbsent(target, builder).setRouteOption(options.getLadenRoute());
						}
						if (options.isSetLadenFuelChoice()) {
							voyageSpecs.computeIfAbsent(target, builder).setFuelChoice(options.getLadenFuelChoice());
						}
						if (options.getLoadDate() != null) {
							((SlotSpecification) scheduleSpec).setArrivalDate(options.getLoadDate());
							ZonedDateTime zdt;
							if (ls.getPort() != null) {
								zdt = options.getLoadDate().atZone(ls.getPort().getZoneId());
							} else {
								zdt = options.getLoadDate().atZone(ZoneId.of("Etc/UTC"));
							}
							mapper.addExtraDate(zdt);
						}

					} else if (target instanceof final DischargeSlot ds) {
						if (options.isSetBallastRoute()) {
							voyageSpecs.computeIfAbsent(target, builder).setRouteOption(options.getBallastRoute());
						}
						if (options.isSetBallastFuelChoice()) {
							voyageSpecs.computeIfAbsent(target, builder).setFuelChoice(options.getBallastFuelChoice());
						}
						if (options.getDischargeDate() != null && scheduleSpec instanceof SlotSpecification) {
							((SlotSpecification) scheduleSpec).setArrivalDate(options.getDischargeDate());
							ZonedDateTime zdt;
							if (ds.getPort() != null) {
								zdt = options.getDischargeDate().atZone(ds.getPort().getZoneId());
							} else {
								zdt = options.getDischargeDate().atZone(ZoneId.of("Etc/UTC"));
							}
							mapper.addExtraDate(zdt);
						}
					} else if (target instanceof final VesselEvent ve) {
						if (options.isSetLadenRoute()) {
							voyageSpecs.computeIfAbsent(target, builder).setRouteOption(options.getLadenRoute());
						}
						if (options.isSetLadenFuelChoice()) {
							voyageSpecs.computeIfAbsent(target, builder).setFuelChoice(options.getLadenFuelChoice());
						}
						if (options.getLoadDate() != null) {
							((VesselEventSpecification) scheduleSpec).setArrivalDate(options.getLoadDate());
							ZonedDateTime zdt;
							if (ve.getPort() != null) {
								zdt = options.getLoadDate().atZone(ve.getPort().getZoneId());
							} else {
								zdt = options.getLoadDate().atZone(ZoneId.of("Etc/UTC"));
							}
							mapper.addExtraDate(zdt);
						}
					}
				}
			};

			for (final AssignableElement assignedObject : seq.getAssignedObjects()) {
				if (assignedObject instanceof final Cargo cargo) {
					assert cargo.getCargoType() == CargoType.FLEET;

					for (final Slot<?> slot : cargo.getSortedSlots()) {
						assert slot != null;
						final SlotSpecification eventSpecification = CargoFactory.eINSTANCE.createSlotSpecification();
						eventSpecification.setSlot(slot);
						vesselScheduleSpecification.getEvents().add(eventSpecification);
						seenSlots.add(slot);

						if (elementToRowMap.containsKey(slot)) {
							final BaseCaseRow row = elementToRowMap.get(slot);
							applyOptions.accept(slot, row.getOptions(), eventSpecification);
						}

					}
					seenCargoes.add(cargo);

				} else if (assignedObject instanceof final FakeCargo cargo) {

					for (final Slot<?> slot : cargo.getSlots()) {
						assert slot != null;
						final SlotSpecification eventSpecification = CargoFactory.eINSTANCE.createSlotSpecification();
						eventSpecification.setSlot(slot);
						vesselScheduleSpecification.getEvents().add(eventSpecification);
						seenSlots.add(slot);

						if (elementToRowMap.containsKey(slot)) {
							final BaseCaseRow row = elementToRowMap.get(slot);
							applyOptions.accept(slot, row.getOptions(), eventSpecification);
						}
					}

					seenCargoes.add(cargo);

				} else if (assignedObject instanceof final VesselEvent vesselEvent) {
					final VesselEventSpecification eventSpecification = CargoFactory.eINSTANCE.createVesselEventSpecification();
					eventSpecification.setVesselEvent(vesselEvent);
					vesselScheduleSpecification.getEvents().add(eventSpecification);

					seenEvents.add(vesselEvent);

					if (elementToRowMap.containsKey(vesselEvent)) {
						final BaseCaseRow row = elementToRowMap.get(vesselEvent);
						applyOptions.accept(vesselEvent, row.getOptions(), eventSpecification);
					}
				} else {
					assert false;
				}
			}
		};

		final Collection<CollectedAssignment> mergedAssignments = new LinkedHashSet<>(assignments);
		mergedAssignments.addAll(vaMap.values());
		mergedAssignments.addAll(mktMap.values());
		for (final CollectedAssignment seq : mergedAssignments) {
			final VesselScheduleSpecification vesselScheduleSpecification = CargoFactory.eINSTANCE.createVesselScheduleSpecification();

			final VesselCharter eVesselCharter = seq.getVesselCharter();
			if (eVesselCharter != null) {
				vesselScheduleSpecification.setVesselAllocation(eVesselCharter);
			} else {
				vesselScheduleSpecification.setVesselAllocation(seq.getCharterInMarket());
				vesselScheduleSpecification.setSpotIndex(seq.getSpotIndex());

			}
			scheduleSpecification.getVesselScheduleSpecifications().add(vesselScheduleSpecification);

			action.accept(seq, vesselScheduleSpecification);

		}

		final Set<AssignableElement> elements = new LinkedHashSet<>();
		elements.addAll(cargoToCollectedAssignmentMap.keySet());
		elements.removeAll(seenCargoes);
		elements.removeAll(seenEvents);

		final TriConsumer<EObject, BaseCaseRowOptions, ScheduleSpecificationEvent> applyOptions = (target, options, slotSpec) -> {
			if (options != null) {
				if (target instanceof LoadSlot ls) {
					if (options.getLoadDate() != null) {
						((SlotSpecification) slotSpec).setArrivalDate(options.getLoadDate());
						ZonedDateTime zdt;
						if (ls.getPort() != null) {
							zdt = options.getLoadDate().atZone(ls.getPort().getZoneId());
						} else {
							zdt = options.getLoadDate().atZone(ZoneId.of("Etc/UTC"));
						}
						mapper.addExtraDate(zdt);
					}

				}
				if (target instanceof DischargeSlot ds) {
					if (options.getDischargeDate() != null) {
						((SlotSpecification) slotSpec).setArrivalDate(options.getDischargeDate());
						ZonedDateTime zdt;
						if (ds.getPort() != null) {
							zdt = options.getDischargeDate().atZone(ds.getPort().getZoneId());
						} else {
							zdt = options.getDischargeDate().atZone(ZoneId.of("Etc/UTC"));
						}
						mapper.addExtraDate(zdt);
					}
				}
			}
		};

		for (final AssignableElement assignedObject : elements) {
			if (seenCargoes.contains(assignedObject)) {
				continue;
			}
			if (assignedObject instanceof final Cargo cargo) {
				// Re-added the non-shipped code path to fix issue respecting locked cargoes
				// when running optioniser in sandbox

				if (cargo.getCargoType() == CargoType.FOB || cargo.getCargoType() == CargoType.DES) {
					final NonShippedCargoSpecification nonShipedCargoSpecification = CargoFactory.eINSTANCE.createNonShippedCargoSpecification();
					for (final Slot<?> slot : cargo.getSortedSlots()) {
						assert !seenSlots.contains(slot);
						final SlotSpecification slotSpec = CargoFactory.eINSTANCE.createSlotSpecification();
						slotSpec.setSlot(slot);
						final BaseCaseRow row = elementToRowMap.get(slot);
						if (row != null) {
							final BaseCaseRowOptions rowOptions = row.getOptions();
							applyOptions.accept(slot, rowOptions, slotSpec);
						}

						nonShipedCargoSpecification.getSlotSpecifications().add(slotSpec);
						seenSlots.add(slot);
					}
					seenCargoes.add(cargo);
					scheduleSpecification.getNonShippedCargoSpecifications().add(nonShipedCargoSpecification);
				} else {
					// Fleet cargo with no vessel allocation....
					// two open positions - covered below
					for (final Slot<?> slot : cargo.getSlots()) {
						if (slot instanceof LoadSlot ls) {
							unusedLoads.add(ls);
						} else if (slot instanceof DischargeSlot ds) {
							unusedDischarges.add(ds);
						} else {
							assert false;
						}
					}
				}
			} else if (assignedObject instanceof FakeCargo cargo) {
				final NonShippedCargoSpecification nonShipedCargoSpecification = CargoFactory.eINSTANCE.createNonShippedCargoSpecification();

				boolean nonShipped = false;

				for (final Slot<?> slot : cargo.getSlots()) {
					assert slot != null;
					final SlotSpecification slotSpec = CargoFactory.eINSTANCE.createSlotSpecification();
					slotSpec.setSlot(slot);
					nonShipedCargoSpecification.getSlotSpecifications().add(slotSpec);
					seenSlots.add(slot);

					if (slot instanceof LoadSlot ls) {
						nonShipped |= ls.isDESPurchase();
					}
					if (slot instanceof DischargeSlot ds) {
						nonShipped |= ds.isFOBSale();
					}

					final BaseCaseRow row = elementToRowMap.get(slot);
					if (row != null) {
						final BaseCaseRowOptions rowOptions = row.getOptions();
						applyOptions.accept(slot, rowOptions, slotSpec);
					}

				}
				if (!nonShipped) {
					// Sanity check!
					final CollectedAssignment collectedAssignment = cargoToCollectedAssignmentMap.get(cargo);
					throw new IllegalStateException("Shipped cargo in non-shipped code path");
				}

				seenCargoes.add(cargo);
				scheduleSpecification.getNonShippedCargoSpecifications().add(nonShipedCargoSpecification);
				// Create non-shipped spec.

			}

		}

		for (final LoadSlot loadSlot : unusedLoads) {
			if (seenSlots.contains(loadSlot)) {
				continue;
			}
			if (loadSlot instanceof SpotSlot) {
				continue;
			}
			final SlotSpecification eventSpecification = CargoFactory.eINSTANCE.createSlotSpecification();
			eventSpecification.setSlot(loadSlot);
			scheduleSpecification.getOpenEvents().add(eventSpecification);
		}

		for (final DischargeSlot dischargeSlot : unusedDischarges) {
			if (seenSlots.contains(dischargeSlot)) {
				continue;
			}
			if (dischargeSlot instanceof SpotSlot) {
				continue;
			}
			final SlotSpecification eventSpecification = CargoFactory.eINSTANCE.createSlotSpecification();
			eventSpecification.setSlot(dischargeSlot);
			scheduleSpecification.getOpenEvents().add(eventSpecification);
		}

		for (final VesselEvent vesselEvent : unusedVesselEvents) {
			if (seenEvents.contains(vesselEvent)) {
				continue;
			}
			final VesselEventSpecification eventSpecification = CargoFactory.eINSTANCE.createVesselEventSpecification();
			eventSpecification.setVesselEvent(vesselEvent);
			scheduleSpecification.getOpenEvents().add(eventSpecification);
			seenEvents.add(vesselEvent);
		}

		// What is missing?
		// Non-shipped cargoes
		// Keep open!
		// Freeze to vessel/group
		// Freeze cargo paring
		// Freeze discharge to next load

		// Assert section
		{
			// Assert all availabilities are used
			// Assert all slots are used
			// Assert all events are used
			// Assert all cargoes are used.
		}

		return scheduleSpecification;
	}

	private void getShippingOption(final ModelDistanceProvider modelDistanceProvider, final PortModel portModel, final Map<VesselCharter, CollectedAssignment> vaMap,
			final Map<Pair<CharterInMarket, Integer>, CollectedAssignment> mktMap, final Map<AssignableElement, @Nullable CollectedAssignment> cargoToCollectedAssignmentMap,
			@Nullable final AssignableElement assignableElement, final ShippingOption shipping) {

		final Function<Pair<CharterInMarket, Integer>, CollectedAssignment> mktMapGenerator = k -> new CollectedAssignment(new LinkedList<>(), k.getFirst(), k.getSecond(), portModel,
				modelDistanceProvider, null);

		final Function<VesselCharter, CollectedAssignment> charterGenerator = k -> new CollectedAssignment(new LinkedList<>(), k, portModel, modelDistanceProvider, null);

		if (shipping instanceof final RoundTripShippingOption roundTripShippingOption) {
			CharterInMarket newMarket = mapper.get(roundTripShippingOption);
			if (newMarket == null) {
				newMarket = AnalyticsBuilder.makeRoundTripOption(roundTripShippingOption);
				mapper.addMapping(roundTripShippingOption, newMarket);
			}

			final Pair<CharterInMarket, Integer> key = new Pair<>(newMarket, -1);
			final CollectedAssignment ca = mktMap.computeIfAbsent(key, mktMapGenerator);
			if (assignableElement != null) {
				ca.getAssignedObjects().add(assignableElement);
				cargoToCollectedAssignmentMap.put(assignableElement, ca);
			}

		} else if (shipping instanceof final ExistingCharterMarketOption option) {
			final CollectedAssignment ca = mktMap.computeIfAbsent(new Pair<>(option.getCharterInMarket(), option.getSpotIndex()), mktMapGenerator);
			if (assignableElement != null) {
				ca.getAssignedObjects().add(assignableElement);
				cargoToCollectedAssignmentMap.put(assignableElement, ca);
			}
		} else if (shipping instanceof final OptionalSimpleVesselCharterOption optionalAvailabilityShippingOption) {
			VesselCharter vesselCharter = mapper.get(optionalAvailabilityShippingOption);
			if (vesselCharter == null) {
				vesselCharter = AnalyticsBuilder.makeOptionalSimpleCharter(optionalAvailabilityShippingOption);
				mapper.addMapping(optionalAvailabilityShippingOption, vesselCharter);

				final CollectedAssignment ca =  vaMap.computeIfAbsent(vesselCharter, charterGenerator);
				if (assignableElement != null) {
					ca.getAssignedObjects().add(assignableElement);
					cargoToCollectedAssignmentMap.put(assignableElement, ca);
				}
			} else {
				final CollectedAssignment ca = vaMap.computeIfAbsent(vesselCharter, charterGenerator);
				if (assignableElement != null) {
					ca.getAssignedObjects().add(assignableElement);
					cargoToCollectedAssignmentMap.put(assignableElement, ca);
				}
			}

		} else if (shipping instanceof final SimpleVesselCharterOption fleetShippingOption) {
			VesselCharter vesselCharter = mapper.get(fleetShippingOption);
			if (vesselCharter == null) {
				vesselCharter = AnalyticsBuilder.makeSimpleCharter(fleetShippingOption);
				mapper.addMapping(fleetShippingOption, vesselCharter);

				final CollectedAssignment ca = vaMap.computeIfAbsent(vesselCharter, charterGenerator);

				if (assignableElement != null) {
					ca.getAssignedObjects().add(assignableElement);

					cargoToCollectedAssignmentMap.put(assignableElement, ca);
				}
			} else {
				final CollectedAssignment ca = vaMap.computeIfAbsent(vesselCharter, charterGenerator);
				if (assignableElement != null) {

					ca.getAssignedObjects().add(assignableElement);
					cargoToCollectedAssignmentMap.put(assignableElement, ca);
				}
			}

		} else if (shipping instanceof final ExistingVesselCharterOption fleetShippingOption) {
			final VesselCharter vesselCharter = fleetShippingOption.getVesselCharter();
			mapper.addMapping(fleetShippingOption, vesselCharter);
			final CollectedAssignment ca = vaMap.computeIfAbsent(vesselCharter, charterGenerator);
			if (assignableElement != null) {

				ca.getAssignedObjects().add(assignableElement);
				cargoToCollectedAssignmentMap.put(assignableElement, ca);
			}
		} else if (shipping instanceof final FullVesselCharterOption fleetShippingOption) {
			final VesselCharter vesselCharter = fleetShippingOption.getVesselCharter();
			mapper.addMapping(fleetShippingOption, vesselCharter);

			final CollectedAssignment ca = vaMap.computeIfAbsent(vesselCharter, charterGenerator);
			if (assignableElement != null) {
				ca.getAssignedObjects().add(assignableElement);
				cargoToCollectedAssignmentMap.put(assignableElement, ca);
			}
		} else {
			if (shipping == null && assignableElement instanceof CharterOutEvent) {
				// Open charter out event.
			} else {
				assert false;
			}
		}
	}

	private LoadSlot getOrCreate(final BuyOption buyOption, final LocalDate windowStart, final int shift) {
		// Do not modify original slots
		assert !(buyOption instanceof BuyReference);
		final LoadSlot slot = getOrCreate(buyOption);
		final LocalDate monthAlignedStart = windowStart.withDayOfMonth(1).minusMonths(shift);

		if (slot.getWindowStart() == null) {
			slot.setWindowStart(monthAlignedStart);
			slot.setWindowSize(1 + shift);
			slot.setWindowSizeUnits(TimePeriod.MONTHS);
		} else {
			LocalDate originalEnd = slot.getWindowStart().plusMonths(slot.getWindowSize());
			final int originalDiff = Months.between(slot.getWindowStart(), originalEnd);
			// Sanity check calcs
			assert originalDiff == slot.getWindowSize();

			if (slot.getWindowStart().isAfter(monthAlignedStart)) {
				slot.setWindowStart(monthAlignedStart);
				// slot.setWindowSize(1 + shift);
				slot.setWindowSizeUnits(TimePeriod.MONTHS);
			} else if (monthAlignedStart.plusMonths(1 + shift).isAfter(originalEnd)) {
				originalEnd = monthAlignedStart.plusMonths(1 + shift);
			}
			int diff = Months.between(slot.getWindowStart(), originalEnd);
			if (shift + 1 > diff) {
				diff = shift + 1;
			}
			slot.setWindowSize(diff);
		}
		if (mapper.isCreateBEOptions()) {

			mapper.getBreakEven(buyOption).setWindowStart(slot.getWindowStart());
			mapper.getBreakEven(buyOption).setWindowSize(slot.getWindowSize());
			mapper.getBreakEven(buyOption).setWindowSizeUnits(slot.getWindowSizeUnits());
			mapper.getChangable(buyOption).setWindowStart(slot.getWindowStart());
			mapper.getChangable(buyOption).setWindowSize(slot.getWindowSize());
			mapper.getChangable(buyOption).setWindowSizeUnits(slot.getWindowSizeUnits());
		}
		return slot;
	}

	private DischargeSlot getOrCreate(final SellOption sellOption, final LocalDate windowStart, final int shift) {

		// Do not modify original slots
		assert !(sellOption instanceof SellReference);
		final DischargeSlot slot = getOrCreate(sellOption);

		if (slot.getWindowStart() == null) {
			slot.setWindowStart(windowStart.withDayOfMonth(1));
			slot.setWindowSize(1 + shift);
			slot.setWindowSizeUnits(TimePeriod.MONTHS);
		} else {
			LocalDate originalEnd = slot.getWindowStart().plusMonths(slot.getWindowSize());
			final int originalDiff = Months.between(slot.getWindowStart(), originalEnd);
			// Sanity check calcs
			assert originalDiff == slot.getWindowSize();

			if (slot.getWindowStart().isAfter(windowStart)) {
				slot.setWindowStart(windowStart.withDayOfMonth(1));
				// slot.setWindowSize(1 + shift);
				slot.setWindowSizeUnits(TimePeriod.MONTHS);
			} else if (windowStart.withDayOfMonth(1).plusMonths(1 + shift).isAfter(originalEnd)) {
				originalEnd = windowStart.withDayOfMonth(1).plusMonths(1 + shift);
			}
			int diff = Months.between(slot.getWindowStart(), originalEnd);
			if (shift + 1 > diff) {
				diff = shift + 1;
			}
			slot.setWindowSize(diff);
		}

		if (mapper.isCreateBEOptions()) {
			mapper.getBreakEven(sellOption).setWindowStart(slot.getWindowStart());
			mapper.getBreakEven(sellOption).setWindowSize(slot.getWindowSize());
			mapper.getBreakEven(sellOption).setWindowSizeUnits(slot.getWindowSizeUnits());
			mapper.getChangable(sellOption).setWindowStart(slot.getWindowStart());
			mapper.getChangable(sellOption).setWindowSize(slot.getWindowSize());
			mapper.getChangable(sellOption).setWindowSizeUnits(slot.getWindowSizeUnits());
		}
		return slot;

	}

	private VesselEvent getOrCreate(final VesselEventOption vesselEventOption) {
		final VesselEvent original = mapper.getOriginal(vesselEventOption);
		if (original != null) {
			return original;
		}
		final VesselEvent newVesselEvent = AnalyticsBuilder.makeVesselEvent(vesselEventOption, scenarioModel);

		mapper.addMapping(vesselEventOption, newVesselEvent);

		return newVesselEvent;
	}

	private DischargeSlot getOrCreate(final SellOption sellOption) {
		if (sellOption == null) {
			return null;
		}

		final DischargeSlot original = mapper.getOriginal(sellOption);
		if (original != null) {
			return original;
		}
		this.usedIDs.addAll(mapper.getExtraDataProvider().extraDischarges.stream().map(DischargeSlot::getName).collect(Collectors.toSet()));
		this.usedIDs.addAll(mapper.getExtraDataProvider().extraLoads.stream().map(LoadSlot::getName).collect(Collectors.toSet()));
		final DischargeSlot dischargeSlot_original = AnalyticsBuilder.makeDischargeSlot(sellOption, scenarioModel, SlotMode.ORIGINAL_SLOT, this.usedIDs);

		if (mapper.isCreateBEOptions()) {
			final DischargeSlot dischargeSlot_breakEven = AnalyticsBuilder.makeDischargeSlot(sellOption, scenarioModel, SlotMode.BREAK_EVEN_VARIANT, this.usedIDs);
			final DischargeSlot dischargeSlot_changeable = AnalyticsBuilder.makeDischargeSlot(sellOption, scenarioModel, SlotMode.CHANGE_PRICE_VARIANT, this.usedIDs);
			mapper.addMapping(sellOption, dischargeSlot_original, dischargeSlot_breakEven, dischargeSlot_changeable);
		} else {
			mapper.addMapping(sellOption, dischargeSlot_original, null, null);
		}

		return dischargeSlot_original;
	}

	private LoadSlot getOrCreate(final BuyOption buyOption) {

		if (buyOption == null) {
			return null;
		}

		final LoadSlot original = mapper.getOriginal(buyOption);
		if (original != null) {
			return original;
		}
		this.usedIDs.addAll(mapper.getExtraDataProvider().extraDischarges.stream().map(DischargeSlot::getName).collect(Collectors.toSet()));
		this.usedIDs.addAll(mapper.getExtraDataProvider().extraLoads.stream().map(LoadSlot::getName).collect(Collectors.toSet()));
		final LoadSlot loadSlot_original = AnalyticsBuilder.makeLoadSlot(buyOption, scenarioModel, SlotMode.ORIGINAL_SLOT, this.usedIDs);
		if (mapper.isCreateBEOptions()) {
			final LoadSlot loadSlot_breakEven = AnalyticsBuilder.makeLoadSlot(buyOption, scenarioModel, SlotMode.BREAK_EVEN_VARIANT, this.usedIDs);
			final LoadSlot loadSlot_changeable = AnalyticsBuilder.makeLoadSlot(buyOption, scenarioModel, SlotMode.CHANGE_PRICE_VARIANT, this.usedIDs);
			mapper.addMapping(buyOption, loadSlot_original, loadSlot_breakEven, loadSlot_changeable);
		} else {
			mapper.addMapping(buyOption, loadSlot_original, null, null);
		}
		return loadSlot_original;
	}

	private boolean sellNeedsDate(final SellOption sellOption) {
		if (sellOption instanceof final SellMarket sellMarket) {
			return !sellMarket.isSetMonth() || sellMarket.getMonth() == null;
		}
		if (sellOption instanceof SellReference) {
			return false;
		}
		if (sellOption instanceof SellOpportunity) {
			final SellOpportunity sellOpportunity = (SellOpportunity) sellOption;
			return sellOpportunity.getDate() == null;
		}
		throw new IllegalArgumentException();
	}

	private boolean buyNeedsDate(final BuyOption buyOption) {
		if (buyOption instanceof final BuyMarket buyMarket) {
			return !buyMarket.isSetMonth() || buyMarket.getMonth() == null;
		}
		if (buyOption instanceof BuyReference) {
			return false;
		}
		if (buyOption instanceof BuyOpportunity) {
			final BuyOpportunity buyOpportunity = (BuyOpportunity) buyOption;
			return buyOpportunity.getDate() == null;
		}
		throw new IllegalArgumentException();
	}

	private static Set<String> getUsedSlotIDs(final LNGScenarioModel lngScenarioModel) {
		final Set<String> usedIDs = new HashSet<>();
		usedIDs.addAll(lngScenarioModel.getCargoModel().getLoadSlots().stream().map(LoadSlot::getName).collect(Collectors.toSet()));
		usedIDs.addAll(lngScenarioModel.getCargoModel().getDischargeSlots().stream().map(DischargeSlot::getName).collect(Collectors.toSet()));
		return usedIDs;
	}

}
