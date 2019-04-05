/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.Months;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
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
import com.mmxlabs.models.lng.analytics.VesselEventOption;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ShippingType;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.SlotMode;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.EVesselTankState;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.NonShippedCargoSpecification;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SlotSpecification;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.VesselEventSpecification;
import com.mmxlabs.models.lng.cargo.VesselScheduleSpecification;
import com.mmxlabs.models.lng.cargo.util.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.util.CollectedAssignment;
import com.mmxlabs.models.lng.cargo.util.scheduling.FakeCargo;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class ExistingBaseCaseToScheduleSpecification {

	private final LNGScenarioModel scenarioModel;
	private final IMapperClass mapper;
	private IScenarioDataProvider scenarioDataProvider;

	public ExistingBaseCaseToScheduleSpecification(IScenarioDataProvider scenarioDataProvider, final IMapperClass mapper) {
		this.scenarioDataProvider = scenarioDataProvider;
		this.scenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
		this.mapper = mapper;
	}

	public ScheduleSpecification generate(final BaseCase baseCase) {

		/////////////////////////////////////////////////// public ScheduleSpecification buildScheduleSpecification(final IScenarioDataProvider scenarioDataProvider,
		// @Nullable final IAssignableElementDateProviderFactory assignableElementDateProviderFactory) {

		ModelDistanceProvider modelDistanceProvider = scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);

		CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioDataProvider);

		Map<Slot<?>, AssignableElement> slotToCargoMap = new HashMap<>();
		Collection<LoadSlot> unusedLoads = new LinkedHashSet<>();
		Collection<DischargeSlot> unusedDischarges = new LinkedHashSet<>();
		for (LoadSlot s : cargoModel.getLoadSlots()) {
			Cargo c = s.getCargo();
			if (c == null) {
				unusedLoads.add(s);
			} else {
				slotToCargoMap.put(s, c);
			}
		}
		for (DischargeSlot s : cargoModel.getDischargeSlots()) {
			Cargo c = s.getCargo();
			if (c == null) {
				unusedDischarges.add(s);
			} else {
				slotToCargoMap.put(s, c);
			}
		}

		PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);
		SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(scenarioDataProvider);

		Map<VesselAvailability, CollectedAssignment> vaMap = new HashMap<>();
		Map<Pair<CharterInMarket, Integer>, CollectedAssignment> mktMap = new HashMap<>();

		Map<AssignableElement, CollectedAssignment> cargoToCollectedAssignmentMap = new HashMap<>();
		final List<CollectedAssignment> assignments = AssignmentEditorHelper.collectAssignments(cargoModel, portModel, spotMarketsModel, modelDistanceProvider);

		for (final CollectedAssignment seq : assignments) {
			if (seq.getVesselAvailability() != null) {
				vaMap.put(seq.getVesselAvailability(), seq);
			} else {
				mktMap.put(new Pair<>(seq.getCharterInMarket(), seq.getSpotIndex()), seq);
			}
			for (final AssignableElement assignedObject : seq.getAssignedObjects()) {
				if (assignedObject instanceof Cargo) {
					final Cargo cargo = (Cargo) assignedObject;
					cargoToCollectedAssignmentMap.put(cargo, seq);
				} else if (assignedObject instanceof VesselEvent) {
					final VesselEvent vesselEvent = (VesselEvent) assignedObject;
					cargoToCollectedAssignmentMap.put(vesselEvent, seq);
				}
			}
		}
		{
			// //
			// final ScheduleSpecification specification = CargoFactory.eINSTANCE.createScheduleSpecification();

			for (final BaseCaseRow row : baseCase.getBaseCase()) {
				if (row.getVesselEventOption() == null
						&& (row.getBuyOption() == null || row.getSellOption() == null || row.getBuyOption() instanceof OpenBuy || row.getSellOption() instanceof OpenSell)) {
					Slot<?> slot = null;
					if (row.getBuyOption() != null) {
						slot = getOrCreate(row.getBuyOption());
					} else if (row.getSellOption() != null) {
						slot = getOrCreate(row.getSellOption());
					}
					if (slot != null) {
						if (slot instanceof LoadSlot) {
							unusedLoads.add((LoadSlot) slot);
						} else if (slot instanceof DischargeSlot) {
							unusedDischarges.add((DischargeSlot) slot);
						}

						if (slotToCargoMap.containsKey(slot)) {
							Cargo cargo = (Cargo) slotToCargoMap.get(slot);
							cargo.getSortedSlots().forEach(slotToCargoMap::remove);
							CollectedAssignment collectedAssignment = cargoToCollectedAssignmentMap.get(cargo);
							if (collectedAssignment != null) {
								collectedAssignment.getAssignedObjects().remove(cargo);
								cargoToCollectedAssignmentMap.remove(cargo);
							}
						}
					}
					continue;
				}

				final ShippingType shippingType = row.getVesselEventOption() != null ? ShippingType.Shipped : AnalyticsBuilder.getShippingType(row.getBuyOption(), row.getSellOption());
				if (shippingType == ShippingType.NonShipped) {
					DischargeSlot dischargeSlot = null;
					LoadSlot loadSlot = null;

					if (buyNeedsDate(row.getBuyOption())) {
						dischargeSlot = getOrCreate(row.getSellOption());
						loadSlot = getOrCreate(row.getBuyOption(), dischargeSlot.getWindowStart(), 0);
					} else if (sellNeedsDate(row.getSellOption())) {
						loadSlot = getOrCreate(row.getBuyOption());
						dischargeSlot = getOrCreate(row.getSellOption(), loadSlot.getWindowStart(), 0);
					} else {
						dischargeSlot = getOrCreate(row.getSellOption());
						loadSlot = getOrCreate(row.getBuyOption());
					}

					unusedLoads.remove(loadSlot);
					unusedDischarges.remove(dischargeSlot);

					if (slotToCargoMap.containsKey(loadSlot)) {
						Cargo cargo = (Cargo) slotToCargoMap.get(loadSlot);
						if (cargo != null && !cargo.getSlots().contains(dischargeSlot)) {
							cargo.getSlots().forEach(slotToCargoMap::remove);
							CollectedAssignment collectedAssignment = cargoToCollectedAssignmentMap.get(cargo);
							if (collectedAssignment != null) {
								collectedAssignment.getAssignedObjects().remove(cargo);
								cargoToCollectedAssignmentMap.remove(cargo);

							}
						} else if (cargo != null) {
							CollectedAssignment collectedAssignment = cargoToCollectedAssignmentMap.get(cargo);
							if (collectedAssignment != null) {
								collectedAssignment.getAssignedObjects().remove(cargo);
								cargoToCollectedAssignmentMap.remove(cargo);

							}
							continue;
						}
					}

					FakeCargo cargo = new FakeCargo(loadSlot, dischargeSlot);
					slotToCargoMap.put(loadSlot, cargo);
					slotToCargoMap.put(dischargeSlot, cargo);
					cargoToCollectedAssignmentMap.put(cargo, null);
				} else if (shippingType == ShippingType.Shipped) {
					AssignableElement assignableElement = null;

					if (row.getVesselEventOption() != null) {
						assignableElement = getOrCreate(row.getVesselEventOption());
						CollectedAssignment collectedAssignment = cargoToCollectedAssignmentMap.get(assignableElement);
						if (collectedAssignment != null) {
							collectedAssignment.getAssignedObjects().remove(assignableElement);
							cargoToCollectedAssignmentMap.remove(assignableElement);
						}
					} else {

						DischargeSlot dischargeSlot = null;
						LoadSlot loadSlot = null;

						if (buyNeedsDate(row.getBuyOption())) {
							dischargeSlot = getOrCreate(row.getSellOption());
							loadSlot = getOrCreate(row.getBuyOption(), dischargeSlot.getWindowStart(), 1);
						} else if (sellNeedsDate(row.getSellOption())) {
							loadSlot = getOrCreate(row.getBuyOption());
							dischargeSlot = getOrCreate(row.getSellOption(), loadSlot.getWindowStart(), 1);
						} else {
							dischargeSlot = getOrCreate(row.getSellOption());
							loadSlot = getOrCreate(row.getBuyOption());
						}

						unusedLoads.remove(loadSlot);
						unusedDischarges.remove(dischargeSlot);

						if (slotToCargoMap.containsKey(loadSlot)) {
							Cargo c = (Cargo) slotToCargoMap.get(loadSlot);
							if (c != null && !c.getSlots().contains(dischargeSlot)) {
								c.getSlots().forEach(slotToCargoMap::remove);
								CollectedAssignment collectedAssignment = cargoToCollectedAssignmentMap.get(c);
								if (collectedAssignment != null) {
									collectedAssignment.getAssignedObjects().remove(c);
									cargoToCollectedAssignmentMap.remove(assignableElement);

								}
							} else if (c != null) {
								assignableElement = c;
								CollectedAssignment collectedAssignment = cargoToCollectedAssignmentMap.get(c);
								if (collectedAssignment != null) {
									collectedAssignment.getAssignedObjects().remove(c);
									cargoToCollectedAssignmentMap.remove(assignableElement);

								}

							}
						}

						if (slotToCargoMap.containsKey(dischargeSlot)) {
							Cargo c = (Cargo) slotToCargoMap.get(dischargeSlot);
							if (c != null && !c.getSlots().contains(loadSlot)) {
								c.getSlots().forEach(slotToCargoMap::remove);
								CollectedAssignment collectedAssignment = cargoToCollectedAssignmentMap.get(c);
								if (collectedAssignment != null) {
									collectedAssignment.getAssignedObjects().remove(c);
									cargoToCollectedAssignmentMap.remove(assignableElement);

								}
							} else if (c != null) {
								// assignableElement = c;
								CollectedAssignment collectedAssignment = cargoToCollectedAssignmentMap.get(c);
								if (collectedAssignment != null) {
									collectedAssignment.getAssignedObjects().remove(c);
									cargoToCollectedAssignmentMap.remove(assignableElement);

								}

							}
						}
						// NO
						if (assignableElement == null) {
							FakeCargo c = new FakeCargo(loadSlot, dischargeSlot);
							slotToCargoMap.put(loadSlot, c);
							slotToCargoMap.put(dischargeSlot, c);
							assignableElement = c;
						}
					}

					assert assignableElement != null;

					final ShippingOption shipping = row.getShipping();
					if (shipping instanceof RoundTripShippingOption) {
						final RoundTripShippingOption roundTripShippingOption = (RoundTripShippingOption) shipping;

						CharterInMarket newMarket = mapper.get(roundTripShippingOption);
						if (newMarket == null) {
							final Vessel vessel = roundTripShippingOption.getVessel();
							final String hireCost = roundTripShippingOption.getHireCost();

							// TODO: Need place to store this in datamodel
							newMarket = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
							newMarket.setCharterInRate(hireCost);
							newMarket.setVessel(vessel);
							newMarket.setNominal(true);

							newMarket.setName(vessel.getName() + " @" + hireCost);

							mapper.addMapping(roundTripShippingOption, newMarket);
						}
						CollectedAssignment ca = new CollectedAssignment(Collections.singletonList(assignableElement), newMarket, -1, portModel, modelDistanceProvider, null);
						mktMap.put(new Pair<>(newMarket, -1), ca);
						cargoToCollectedAssignmentMap.put(assignableElement, ca);

					} else if (shipping instanceof ExistingCharterMarketOption) {
						ExistingCharterMarketOption option = (ExistingCharterMarketOption) shipping;
						CollectedAssignment ca = mktMap.get(new Pair<>(option.getCharterInMarket(), option.getSpotIndex()));
						ca.getAssignedObjects().add(assignableElement);
						cargoToCollectedAssignmentMap.put(assignableElement, ca);

					} else if (shipping instanceof OptionalSimpleVesselCharterOption) {
						final OptionalSimpleVesselCharterOption optionalAvailabilityShippingOption = (OptionalSimpleVesselCharterOption) shipping;
						VesselAvailability vesselAvailability = mapper.get(optionalAvailabilityShippingOption);
						if (vesselAvailability == null) {
							vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
							vesselAvailability.setTimeCharterRate(optionalAvailabilityShippingOption.getHireCost());
							final Vessel vessel = optionalAvailabilityShippingOption.getVessel();
							vesselAvailability.setVessel(vessel);
							vesselAvailability.setEntity(optionalAvailabilityShippingOption.getEntity());

							vesselAvailability.setStartHeel(CargoFactory.eINSTANCE.createStartHeelOptions());
							vesselAvailability.setEndHeel(CargoFactory.eINSTANCE.createEndHeelOptions());
							if (optionalAvailabilityShippingOption.isUseSafetyHeel()) {
								vesselAvailability.getStartHeel().setMaxVolumeAvailable(vessel.getSafetyHeel());
								vesselAvailability.getStartHeel().setCvValue(22.8);
								// vesselAvailability.getStartHeel().setPriceExpression(PerMMBTU(0.1);

								vesselAvailability.getEndHeel().setMinimumEndHeel(vessel.getSafetyHeel());
								vesselAvailability.getEndHeel().setMaximumEndHeel(vessel.getSafetyHeel());
								vesselAvailability.getEndHeel().setTankState(EVesselTankState.MUST_BE_COLD);
							}

							vesselAvailability.setStartAfter(optionalAvailabilityShippingOption.getStart().atStartOfDay());
							vesselAvailability.setStartBy(optionalAvailabilityShippingOption.getEnd().atStartOfDay());
							vesselAvailability.setEndAfter(optionalAvailabilityShippingOption.getEnd().atStartOfDay());
							vesselAvailability.setEndBy(optionalAvailabilityShippingOption.getEnd().atStartOfDay());
							vesselAvailability.setOptional(true);
							vesselAvailability.setFleet(false);
							vesselAvailability.setRepositioningFee(optionalAvailabilityShippingOption.getRepositioningFee());
							if (optionalAvailabilityShippingOption.getStartPort() != null) {
								vesselAvailability.setStartAt(optionalAvailabilityShippingOption.getStartPort());
							}
							if (optionalAvailabilityShippingOption.getEndPort() != null) {
								EList<APortSet<Port>> endAt = vesselAvailability.getEndAt();
								endAt.clear();
								endAt.add(optionalAvailabilityShippingOption.getEndPort());
							}

							mapper.addMapping(optionalAvailabilityShippingOption, vesselAvailability);

							CollectedAssignment ca = new CollectedAssignment(Collections.singletonList(assignableElement), vesselAvailability, portModel, modelDistanceProvider, null);
							vaMap.put(vesselAvailability, ca);
							cargoToCollectedAssignmentMap.put(assignableElement, ca);

						} else {
							CollectedAssignment ca = vaMap.get(vesselAvailability);
							ca.getAssignedObjects().add(assignableElement);
							cargoToCollectedAssignmentMap.put(assignableElement, ca);
						}

					} else if (shipping instanceof SimpleVesselCharterOption) {
						final SimpleVesselCharterOption fleetShippingOption = (SimpleVesselCharterOption) shipping;
						VesselAvailability vesselAvailability = mapper.get(fleetShippingOption);
						if (vesselAvailability == null) {
							vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
							vesselAvailability.setTimeCharterRate(fleetShippingOption.getHireCost());
							final Vessel vessel = fleetShippingOption.getVessel();
							vesselAvailability.setVessel(vessel);
							vesselAvailability.setEntity(fleetShippingOption.getEntity());

							vesselAvailability.setStartHeel(CargoFactory.eINSTANCE.createStartHeelOptions());
							vesselAvailability.setEndHeel(CargoFactory.eINSTANCE.createEndHeelOptions());

							if (fleetShippingOption.isUseSafetyHeel()) {
								vesselAvailability.getStartHeel().setMaxVolumeAvailable(vessel.getSafetyHeel());
								vesselAvailability.getStartHeel().setCvValue(22.8);
								// vesselAvailability.getStartHeel().setPricePerMMBTU(0.1);

								vesselAvailability.getEndHeel().setMinimumEndHeel(vessel.getSafetyHeel());
								vesselAvailability.getEndHeel().setMaximumEndHeel(vessel.getSafetyHeel());
								vesselAvailability.getEndHeel().setTankState(EVesselTankState.MUST_BE_COLD);
							}
							vesselAvailability.setOptional(false);
							vesselAvailability.setFleet(true);

							mapper.addMapping(fleetShippingOption, vesselAvailability);

							CollectedAssignment ca = new CollectedAssignment(Collections.singletonList(assignableElement), vesselAvailability, portModel, modelDistanceProvider, null);
							vaMap.put(vesselAvailability, ca);
							cargoToCollectedAssignmentMap.put(assignableElement, ca);
						} else {
							CollectedAssignment ca = vaMap.get(vesselAvailability);
							ca.getAssignedObjects().add(assignableElement);
							cargoToCollectedAssignmentMap.put(assignableElement, ca);
						}

					} else if (shipping instanceof ExistingVesselCharterOption) {
						final ExistingVesselCharterOption fleetShippingOption = (ExistingVesselCharterOption) shipping;
						VesselAvailability vesselAvailability = fleetShippingOption.getVesselCharter();
						mapper.addMapping(fleetShippingOption, vesselAvailability);
						CollectedAssignment ca = vaMap.get(vesselAvailability);
						ca.getAssignedObjects().add(assignableElement);
						cargoToCollectedAssignmentMap.put(assignableElement, ca);
					} else if (shipping instanceof FullVesselCharterOption) {
						final FullVesselCharterOption fleetShippingOption = (FullVesselCharterOption) shipping;
						VesselAvailability vesselAvailability = fleetShippingOption.getVesselCharter();
						mapper.addMapping(fleetShippingOption, vesselAvailability);

						CollectedAssignment ca = vaMap.get(vesselAvailability);
						if (ca == null) {
							ca = new CollectedAssignment(Collections.singletonList(assignableElement), vesselAvailability, portModel, modelDistanceProvider, null);
							vaMap.put(vesselAvailability, ca);
						} else {
							ca.getAssignedObjects().add(assignableElement);
						}

						cargoToCollectedAssignmentMap.put(assignableElement, ca);

					} else {
						assert false;
					}

				} else {
					// ?
				}

			}
		}

		{
			final ScheduleSpecification scheduleSpecification = CargoFactory.eINSTANCE.createScheduleSpecification();
			final Set<Slot> seenSlots = new HashSet<>();
			final Set<AssignableElement> seenCargoes = new HashSet<>();
			final Set<VesselEvent> seenEvents = new HashSet<>();

			BiConsumer<CollectedAssignment, VesselScheduleSpecification> action = (seq, vesselScheduleSpecification) -> {
				seq.resort(portModel, modelDistanceProvider, null);

				for (final AssignableElement assignedObject : seq.getAssignedObjects()) {
					if (assignedObject instanceof Cargo) {
						final Cargo cargo = (Cargo) assignedObject;
						assert cargo.getCargoType() == CargoType.FLEET;

						for (final Slot slot : cargo.getSortedSlots()) {
							final SlotSpecification eventSpecification = CargoFactory.eINSTANCE.createSlotSpecification();
							eventSpecification.setSlot(slot);
							vesselScheduleSpecification.getEvents().add(eventSpecification);
							seenSlots.add(slot);
						}
						seenCargoes.add(cargo);

					} else if (assignedObject instanceof FakeCargo) {
						final FakeCargo cargo = (FakeCargo) assignedObject;

						for (final Slot slot : cargo.getSlots()) {
							final SlotSpecification eventSpecification = CargoFactory.eINSTANCE.createSlotSpecification();
							eventSpecification.setSlot(slot);
							vesselScheduleSpecification.getEvents().add(eventSpecification);
							seenSlots.add(slot);
						}
						seenCargoes.add(cargo);

					} else if (assignedObject instanceof VesselEvent) {
						final VesselEvent vesselEvent = (VesselEvent) assignedObject;
						final VesselEventSpecification eventSpecification = CargoFactory.eINSTANCE.createVesselEventSpecification();
						eventSpecification.setVesselEvent(vesselEvent);
						vesselScheduleSpecification.getEvents().add(eventSpecification);

						seenEvents.add(vesselEvent);
					} else {
						assert false;
					}
				}
			};

			Collection<CollectedAssignment> mergedAssignments = new LinkedHashSet<>(assignments);
			mergedAssignments.addAll(vaMap.values());
			mergedAssignments.addAll(mktMap.values());
			for (final CollectedAssignment seq : mergedAssignments) {
				final VesselScheduleSpecification vesselScheduleSpecification = CargoFactory.eINSTANCE.createVesselScheduleSpecification();

				final VesselAvailability eVesselAvailability = seq.getVesselAvailability();
				if (eVesselAvailability != null) {
					vesselScheduleSpecification.setVesselAllocation(eVesselAvailability);
				} else {
					vesselScheduleSpecification.setVesselAllocation(seq.getCharterInMarket());
					vesselScheduleSpecification.setSpotIndex(seq.getSpotIndex());

				}
				scheduleSpecification.getVesselScheduleSpecifications().add(vesselScheduleSpecification);

				action.accept(seq, vesselScheduleSpecification);

			}

			Set<AssignableElement> elements = new LinkedHashSet<>();
			elements.addAll(cargoModel.getCargoes());
			elements.addAll(cargoToCollectedAssignmentMap.keySet());
			elements.removeAll(seenCargoes);

			for (AssignableElement assignedObject : elements) {
				if (seenCargoes.contains(assignedObject)) {
					continue;
				}

				if (assignedObject instanceof Cargo) {
					final Cargo cargo = (Cargo) assignedObject;
					if (cargo.getCargoType() == CargoType.FOB || cargo.getCargoType() == CargoType.DES) {
						NonShippedCargoSpecification nonShipedCargoSpecification = CargoFactory.eINSTANCE.createNonShippedCargoSpecification();
						for (final Slot slot : cargo.getSortedSlots()) {
							final SlotSpecification eventSpecification = CargoFactory.eINSTANCE.createSlotSpecification();
							eventSpecification.setSlot(slot);
							nonShipedCargoSpecification.getSlotSpecifications().add(eventSpecification);
							seenSlots.add(slot);
						}
						seenCargoes.add(cargo);
						scheduleSpecification.getNonShippedCargoSpecifications().add(nonShipedCargoSpecification);
						// Create non-shipped spec.
					} else {

						// Fleet cargo with no vessel allocation....
						// two open positions - covered below
						for (Slot slot : cargo.getSlots()) {
							if (slot instanceof LoadSlot) {
								unusedLoads.add((LoadSlot) slot);
							} else {
								unusedDischarges.add((DischargeSlot) slot);
							}
						}
					}
				} else if (assignedObject instanceof FakeCargo) {
					final FakeCargo cargo = (FakeCargo) assignedObject;

					NonShippedCargoSpecification nonShipedCargoSpecification = CargoFactory.eINSTANCE.createNonShippedCargoSpecification();
					for (final Slot slot : cargo.getSlots()) {
						final SlotSpecification eventSpecification = CargoFactory.eINSTANCE.createSlotSpecification();
						eventSpecification.setSlot(slot);
						nonShipedCargoSpecification.getSlotSpecifications().add(eventSpecification);
						seenSlots.add(slot);
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
				final SlotSpecification eventSpecification = CargoFactory.eINSTANCE.createSlotSpecification();
				eventSpecification.setSlot(loadSlot);
				scheduleSpecification.getOpenEvents().add(eventSpecification);
			}
			for (final DischargeSlot dischargeSlot : unusedDischarges) {
				if (seenSlots.contains(dischargeSlot)) {
					continue;
				}
				final SlotSpecification eventSpecification = CargoFactory.eINSTANCE.createSlotSpecification();
				eventSpecification.setSlot(dischargeSlot);
				scheduleSpecification.getOpenEvents().add(eventSpecification);
			}
			for (final VesselEvent vesselEvent : cargoModel.getVesselEvents()) {
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
		final DischargeSlot original = mapper.getOriginal(sellOption);
		if (original != null) {
			return original;
		}
		final DischargeSlot dischargeSlot_original = AnalyticsBuilder.makeDischargeSlot(sellOption, scenarioModel, SlotMode.ORIGINAL_SLOT);
		final DischargeSlot dischargeSlot_breakEven = AnalyticsBuilder.makeDischargeSlot(sellOption, scenarioModel, SlotMode.BREAK_EVEN_VARIANT);
		final DischargeSlot dischargeSlot_changeable = AnalyticsBuilder.makeDischargeSlot(sellOption, scenarioModel, SlotMode.CHANGE_PRICE_VARIANT);

		mapper.addMapping(sellOption, dischargeSlot_original, dischargeSlot_breakEven, dischargeSlot_changeable);

		return dischargeSlot_original;
	}

	private LoadSlot getOrCreate(final BuyOption buyOption) {

		final LoadSlot original = mapper.getOriginal(buyOption);
		if (original != null) {
			return original;
		}
		final LoadSlot loadSlot_original = AnalyticsBuilder.makeLoadSlot(buyOption, scenarioModel, SlotMode.ORIGINAL_SLOT);
		final LoadSlot loadSlot_breakEven = AnalyticsBuilder.makeLoadSlot(buyOption, scenarioModel, SlotMode.BREAK_EVEN_VARIANT);
		final LoadSlot loadSlot_changeable = AnalyticsBuilder.makeLoadSlot(buyOption, scenarioModel, SlotMode.CHANGE_PRICE_VARIANT);
		mapper.addMapping(buyOption, loadSlot_original, loadSlot_breakEven, loadSlot_changeable);
		return loadSlot_original;
	}

	private boolean sellNeedsDate(final SellOption sellOption) {
		if (sellOption instanceof SellMarket) {
			return true;
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
		if (buyOption instanceof BuyMarket) {
			return true;
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

}
