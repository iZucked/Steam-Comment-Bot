/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics.spec;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.CargoChange;
import com.mmxlabs.models.lng.analytics.Change;
import com.mmxlabs.models.lng.analytics.ChangeDescription;
import com.mmxlabs.models.lng.analytics.FleetVesselAllocationDescriptor;
import com.mmxlabs.models.lng.analytics.MarketVesselAllocationDescriptor;
import com.mmxlabs.models.lng.analytics.OpenSlotChange;
import com.mmxlabs.models.lng.analytics.PositionDescriptor;
import com.mmxlabs.models.lng.analytics.RealSlotDescriptor;
import com.mmxlabs.models.lng.analytics.SlotDescriptor;
import com.mmxlabs.models.lng.analytics.SlotType;
import com.mmxlabs.models.lng.analytics.SpotMarketSlotDescriptor;
import com.mmxlabs.models.lng.analytics.VesselAllocationDescriptor;
import com.mmxlabs.models.lng.analytics.VesselEventChange;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ExtraDataProvider;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.NonShippedCargoSpecification;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.cargo.ScheduleSpecificationEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SlotSpecification;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.VesselEventSpecification;
import com.mmxlabs.models.lng.cargo.VesselScheduleSpecification;
import com.mmxlabs.models.lng.cargo.editor.utils.IAssignableElementDateProviderFactory;
import com.mmxlabs.models.lng.cargo.util.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.util.CollectedAssignment;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.port.util.PortModelFinder;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotType;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelFinder;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class ChangeModelToScheduleSpecification {

	private final Function<String, EObject> finderFunction;
	private final LNGScenarioModel lngScenarioModel;
	private @Nullable final IAssignableElementDateProviderFactory assignableElementDateProviderFactory;
	private PortModelFinder portFinder;
	private SpotMarketsModelFinder spotMarketsModelFinder;
	private IScenarioDataProvider scenarioDataProvider;
	private ModelDistanceProvider modelDistanceProvider;

	public ChangeModelToScheduleSpecification(final IScenarioDataProvider scenarioDataProvider, @Nullable final IAssignableElementDateProviderFactory assignableElementDateProviderFactory) {

		this.scenarioDataProvider = scenarioDataProvider;
		this.lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
		modelDistanceProvider = scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);
		this.assignableElementDateProviderFactory = assignableElementDateProviderFactory;
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(lngScenarioModel);
		finderFunction = buildFinderFunction(cargoModel);

		portFinder = new PortModelFinder(ScenarioModelUtil.getPortModel(lngScenarioModel));
		spotMarketsModelFinder = new SpotMarketsModelFinder(ScenarioModelUtil.getSpotMarketsModel(lngScenarioModel));
	}

	private Function<String, EObject> buildFinderFunction(final CargoModel cargoModel) {

		final Map<String, Slot> slotMap = new HashMap<>();
		final Map<String, List<Slot>> marketSlotMap = new HashMap<>();
		final Map<String, VesselEvent> eventMap = new HashMap<>();

		for (final LoadSlot slot : cargoModel.getLoadSlots()) {
			if (slot instanceof SpotSlot) {
				final SpotSlot spotSlot = (SpotSlot) slot;
				final SlotType slotType = ScheduleSpecificationHelper.getSlotType(slot);
				final String key = "market-" + slotType + "-" + spotSlot.getMarket().getName() + String.format("%04d-%02d", slot.getWindowStart().getYear(), slot.getWindowStart().getMonthValue());
				marketSlotMap.computeIfAbsent(key, k -> new LinkedList<>()).add(slot);
			} else {
				final SlotType slotType = ScheduleSpecificationHelper.getSlotType(slot);
				final String key = "slot-" + slotType + "-" + slot.getName();
				slotMap.put(key, slot);
			}
		}
		for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
			if (slot instanceof SpotSlot) {
				final SpotSlot spotSlot = (SpotSlot) slot;
				final SlotType slotType = ScheduleSpecificationHelper.getSlotType(slot);
				final String key = "market-" + slotType + "-" + spotSlot.getMarket().getName() + String.format("%04d-%02d", slot.getWindowStart().getYear(), slot.getWindowStart().getMonthValue());
				marketSlotMap.computeIfAbsent(key, k -> new LinkedList<>()).add(slot);
			} else {
				final SlotType slotType = ScheduleSpecificationHelper.getSlotType(slot);
				final String key = "slot-" + slotType + "-" + slot.getName();
				slotMap.put(key, slot);
			}
		}
		for (final VesselEvent vesselEvent : cargoModel.getVesselEvents()) {
			eventMap.put("event-" + vesselEvent.getName(), vesselEvent);
		}

		return (key) -> {
			if (key.startsWith("event-")) {
				return eventMap.get(key);
			} else if (key.startsWith("slot-")) {
				return slotMap.get(key);
			} else if (key.startsWith("market-")) {
				// What to do here? Just ignore for now I think
				// return marketSlotMap.get(key);
			}

			return null;
		};
	}

	public Pair<ScheduleSpecification, ExtraDataProvider> buildScheduleSpecification(final ChangeDescription changeDescription) {

		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(lngScenarioModel);
		final PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);
		final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(lngScenarioModel);

		final List<CollectedAssignment> assignments;
		if (assignableElementDateProviderFactory != null) {
			assignments = AssignmentEditorHelper.collectAssignments(cargoModel, portModel, spotMarketsModel, modelDistanceProvider, assignableElementDateProviderFactory.create(lngScenarioModel));
		} else {
			assignments = AssignmentEditorHelper.collectAssignments(cargoModel, portModel, spotMarketsModel, modelDistanceProvider);
		}

		final ScheduleSpecification scheduleSpecification = CargoFactory.eINSTANCE.createScheduleSpecification();
		final Map<Slot, List<SlotSpecification>> cargoDefinitions = new HashMap<>();
		final Map<VesselEvent, VesselEventSpecification> eventDefinitions = new HashMap<>();
		for (final CollectedAssignment seq : assignments) {

			final VesselScheduleSpecification vesselScheduleSpecification = CargoFactory.eINSTANCE.createVesselScheduleSpecification();
			if (seq.getVesselAvailability() != null) {
				vesselScheduleSpecification.setVesselAllocation(seq.getVesselAvailability());
			} else if (seq.getCharterInMarket() != null) {
				vesselScheduleSpecification.setVesselAllocation(seq.getCharterInMarket());
				vesselScheduleSpecification.setSpotIndex(seq.getSpotIndex());
			} else {
				assert false;
			}

			for (final AssignableElement e : seq.getAssignedObjects()) {
				if (e instanceof Cargo) {
					final Cargo cargo = (Cargo) e;
					final List<SlotSpecification> slotSpecifications = new LinkedList<>();
					for (final Slot slot : cargo.getSortedSlots()) {
						final SlotSpecification slotSpecification = CargoFactory.eINSTANCE.createSlotSpecification();
						slotSpecification.setSlot(slot);
						vesselScheduleSpecification.getEvents().add(slotSpecification);
						slotSpecifications.add(slotSpecification);
						cargoDefinitions.put(slot, slotSpecifications);
					}
				} else if (e instanceof VesselEvent) {
					final VesselEvent vesselEvent = (VesselEvent) e;
					final VesselEventSpecification eventSpecification = CargoFactory.eINSTANCE.createVesselEventSpecification();
					eventSpecification.setVesselEvent(vesselEvent);
					eventDefinitions.put(vesselEvent, eventSpecification);
					vesselScheduleSpecification.getEvents().add(eventSpecification);
				} else {
					assert false;
				}
			}
			scheduleSpecification.getVesselScheduleSpecifications().add(vesselScheduleSpecification);
		}
		for (final Cargo cargo : cargoModel.getCargoes()) {
			if (cargo.getCargoType() != CargoType.FLEET) {
				final NonShippedCargoSpecification nonShippedCargoSpecification = CargoFactory.eINSTANCE.createNonShippedCargoSpecification();
				final List<SlotSpecification> slotSpecifications = new LinkedList<>();
				for (final Slot slot : cargo.getSortedSlots()) {
					final SlotSpecification slotSpecification = CargoFactory.eINSTANCE.createSlotSpecification();
					slotSpecification.setSlot(slot);
					nonShippedCargoSpecification.getSlotSpecifications().add(slotSpecification);
					slotSpecifications.add(slotSpecification);
					cargoDefinitions.put(slot, slotSpecifications);
				}
				scheduleSpecification.getNonShippedCargoSpecifications().add(nonShippedCargoSpecification);
			}
		}

		for (final LoadSlot slot : cargoModel.getLoadSlots()) {
			final SlotSpecification slotSpecification = CargoFactory.eINSTANCE.createSlotSpecification();
			slotSpecification.setSlot(slot);
			if (slot.getCargo() == null) {
				scheduleSpecification.getOpenEvents().add(slotSpecification);
				cargoDefinitions.put(slot, Lists.newArrayList(slotSpecification));
			}
		}
		for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
			final SlotSpecification slotSpecification = CargoFactory.eINSTANCE.createSlotSpecification();
			slotSpecification.setSlot(slot);
			if (slot.getCargo() == null) {
				scheduleSpecification.getOpenEvents().add(slotSpecification);
				cargoDefinitions.put(slot, Lists.newArrayList(slotSpecification));
			}
		}

		final List<LoadSlot> extraLoads = new LinkedList<>();
		final List<DischargeSlot> extraDischarges = new LinkedList<>();

		List<BooleanSupplier> leftOvers = new LinkedList<>();

		for (final Change change : changeDescription.getChanges()) {
			final List<ScheduleSpecificationEvent> newEvents = new LinkedList<>();
			VesselAllocationDescriptor vesselAllocation = null;
			PositionDescriptor position = null;
			if (change instanceof CargoChange) {
				final CargoChange cargoChange = (CargoChange) change;
				vesselAllocation = cargoChange.getVesselAllocation();
				position = cargoChange.getPosition();
				final List<SlotDescriptor> slotDescriptors = cargoChange.getSlotDescriptors();
				for (final SlotDescriptor descriptor : slotDescriptors) {
					boolean newSlot = false;
					Slot slot = null;
					if (descriptor instanceof RealSlotDescriptor) {
						final RealSlotDescriptor d = (RealSlotDescriptor) descriptor;
						final SlotType slotType = d.getSlotType();
						final String key = "slot-" + slotType + "-" + d.getSlotName();
						slot = (Slot) finderFunction.apply(key);
						if (slot == null) {
							throw new RuntimeException("Slot not found: " + d.getSlotName());
						}
					} else if (descriptor instanceof SpotMarketSlotDescriptor) {
						final SpotMarketSlotDescriptor d = (SpotMarketSlotDescriptor) descriptor;
						final SlotType slotType = d.getSlotType();
						final String key = "market-" + slotType + "-" + d.getMarketName() + String.format("%04d-%02d", d.getDate().getYear(), d.getDate().getMonthValue());
						slot = (Slot) finderFunction.apply(key);

						if (slot == null) {
							newSlot = true;
							SpotMarket spotMarket = spotMarketsModelFinder.findSpotMarket(map(slotType), d.getMarketName());
							if (spotMarket == null) {
								throw new RuntimeException("Spot market not found: " + d.getMarketName());
							}

							if (d.getSlotType() == SlotType.DES_PURCHASE || d.getSlotType() == SlotType.FOB_PURCHASE) {
								SpotLoadSlot load = CargoFactory.eINSTANCE.createSpotLoadSlot();
								extraLoads.add(load);
								load.setDESPurchase(d.getSlotType() == SlotType.DES_PURCHASE);
								load.setMarket(spotMarket);
								slot = load;
							} else {
								SpotDischargeSlot discharge = CargoFactory.eINSTANCE.createSpotDischargeSlot();
								extraDischarges.add(discharge);
								discharge.setFOBSale(d.getSlotType() == SlotType.FOB_SALE);
								discharge.setMarket(spotMarket);
								slot = discharge;
							}

							slot.setWindowSize(1);
							slot.setWindowSizeUnits(TimePeriod.MONTHS);
							slot.setWindowStartTime(0);

							// Set port
							slot.setPort(portFinder.findPort(d.getPortName()));
							// Set data
							slot.setWindowStart(d.getDate().atDay(1));
							// Set name -- TODO: Make unique
							final String name = d.getMarketName() + String.format("-%04d-%02d", d.getDate().getYear(), d.getDate().getMonthValue());
							slot.setName(name);
						}
					} else {
						assert false;
					}

					if (!newSlot) {
						final List<SlotSpecification> list = cargoDefinitions.get(slot);
						if (list != null) {
							// Existing cargo
							for (final SlotSpecification spec : list) {
								if (spec.eContainer() instanceof VesselScheduleSpecification) {
									final VesselScheduleSpecification vesselScheduleSpecification = (VesselScheduleSpecification) spec.eContainer();
									vesselScheduleSpecification.getEvents().remove(spec);
								} else if (spec.eContainer() instanceof NonShippedCargoSpecification) {
									final NonShippedCargoSpecification nonShippedCargoSpecification = (NonShippedCargoSpecification) spec.eContainer();
									nonShippedCargoSpecification.getSlotSpecifications().clear();
									scheduleSpecification.getNonShippedCargoSpecifications().remove(nonShippedCargoSpecification);
								} else {
									// already removed
								}
							}
						} else {
							// Look through unused list
							for (final ScheduleSpecificationEvent eventSpecification : scheduleSpecification.getOpenEvents()) {
								if (eventSpecification instanceof SlotSpecification) {
									final SlotSpecification slotSpecification = (SlotSpecification) eventSpecification;
									if (slotSpecification.getSlot() == slot) {
										scheduleSpecification.getOpenEvents().remove(slotSpecification);
										break;
									}
								}
							}
						}
					}
					final SlotSpecification newSlotSpecification = CargoFactory.eINSTANCE.createSlotSpecification();
					newSlotSpecification.setSlot(slot);
					newEvents.add(newSlotSpecification);
				}
				
				if (vesselAllocation == null) {
					// Non-shipped.
					final NonShippedCargoSpecification nonShippedCargoSpecification = CargoFactory.eINSTANCE.createNonShippedCargoSpecification();
					nonShippedCargoSpecification.getSlotSpecifications().addAll(newEvents.stream().map(e -> (SlotSpecification) e).collect(Collectors.toList()));
					scheduleSpecification.getNonShippedCargoSpecifications().add(nonShippedCargoSpecification);
					break;
				}
			} else if (change instanceof VesselEventChange) {
				final VesselEventChange vesselEventChange = (VesselEventChange) change;
				vesselAllocation = vesselEventChange.getVesselAllocation();

				String key = "event-" + vesselEventChange.getVesselEventDescriptor().getEventName();
				VesselEvent vesselEvent = (VesselEvent) finderFunction.apply(key);

				if (vesselEvent == null) {
					throw new RuntimeException("Vessel event not found: " + vesselEventChange.getVesselEventDescriptor().getEventName());
				}

				position = vesselEventChange.getPosition();

				final VesselEventSpecification spec = eventDefinitions.get(vesselEvent);
				if (spec != null) {
					// Existing cargo
					if (spec.eContainer() instanceof VesselScheduleSpecification) {
						final VesselScheduleSpecification vesselScheduleSpecification = (VesselScheduleSpecification) spec.eContainer();
						vesselScheduleSpecification.getEvents().remove(spec);
					} else {
						// already removed
					}
					VesselEventSpecification newSpec = CargoFactory.eINSTANCE.createVesselEventSpecification();
					newSpec.setVesselEvent(vesselEvent);
					newEvents.add(newSpec);
				}
			} else if (change instanceof OpenSlotChange) {
				final OpenSlotChange openSlotChange = (OpenSlotChange) change;
				final SlotDescriptor descriptor = openSlotChange.getSlotDescriptor();
				if (descriptor instanceof RealSlotDescriptor) {
					final RealSlotDescriptor d = (RealSlotDescriptor) descriptor;
					final SlotType slotType = d.getSlotType();
					final String key = "slot-" + slotType + "-" + d.getSlotName();
					final Slot slot = (Slot) finderFunction.apply(key);
					if (slot == null) {
						throw new RuntimeException("Slot not found: " + d.getSlotName());
					}
					final List<SlotSpecification> list = cargoDefinitions.get(slot);
					boolean foundExisting = false;
					if (list != null) {
						// Existing cargo
						for (final SlotSpecification spec : list) {
							if (spec.eContainer() instanceof VesselScheduleSpecification) {
								final VesselScheduleSpecification vesselScheduleSpecification = (VesselScheduleSpecification) spec.eContainer();
								vesselScheduleSpecification.getEvents().remove(spec);
							} else if (spec.eContainer() instanceof NonShippedCargoSpecification) {
								final NonShippedCargoSpecification nonShippedCargoSpecification = (NonShippedCargoSpecification) spec.eContainer();
								nonShippedCargoSpecification.getSlotSpecifications().clear();
								scheduleSpecification.getNonShippedCargoSpecifications().remove(nonShippedCargoSpecification);
							} else {
								// already removed
							}
						}
					} else {
						// Look through unused list
						for (final ScheduleSpecificationEvent eventSpecification : scheduleSpecification.getOpenEvents()) {
							if (eventSpecification instanceof SlotSpecification) {
								final SlotSpecification slotSpecification = (SlotSpecification) eventSpecification;
								if (slotSpecification.getSlot() == slot) {
									// Matched
									foundExisting = true;
									break;
								}
							}
						}
					}
					if (!foundExisting) {
						final SlotSpecification newSlotSpecification = CargoFactory.eINSTANCE.createSlotSpecification();
						newSlotSpecification.setSlot(slot);
						scheduleSpecification.getOpenEvents().add(newSlotSpecification);
					}
				} else {
					assert false;
				}

				break;
			}
			if (!(change instanceof OpenSlotChange)) {
				VesselScheduleSpecification targetVessel = null;
				if (vesselAllocation instanceof FleetVesselAllocationDescriptor) {
					final FleetVesselAllocationDescriptor fleetVesselAllocationDescriptor = (FleetVesselAllocationDescriptor) vesselAllocation;
					final String vesselName = fleetVesselAllocationDescriptor.getName();
					assert vesselName != null;
					for (final VesselScheduleSpecification vesselScheduleSpecification : scheduleSpecification.getVesselScheduleSpecifications()) {
						if (vesselScheduleSpecification.getVesselAllocation() instanceof VesselAvailability) {
							final VesselAvailability vesselAvailability = (VesselAvailability) vesselScheduleSpecification.getVesselAllocation();
							final Vessel vessel = vesselAvailability.getVessel();
							if (vessel != null && vesselName.equals(vessel.getName())) {
								if (vesselAvailability.getCharterNumber() == fleetVesselAllocationDescriptor.getCharterIndex()) {
									targetVessel = vesselScheduleSpecification;
									break;
								}
							}
						}
					}
					if (targetVessel == null) {
						throw new RuntimeException("Vessel availability not found for " + vesselName + " + (" + fleetVesselAllocationDescriptor.getCharterIndex() + ")");
					}
					assert targetVessel != null;
					// TODO: Find correct vessel specification and insert
				} else if (vesselAllocation instanceof MarketVesselAllocationDescriptor) {
					final MarketVesselAllocationDescriptor marketVesselAllocationDescriptor = (MarketVesselAllocationDescriptor) vesselAllocation;
					String marketName = marketVesselAllocationDescriptor.getMarketName();
					// TODO: Find or create correct vessel specification and insert
					for (final VesselScheduleSpecification vesselScheduleSpecification : scheduleSpecification.getVesselScheduleSpecifications()) {
						if (vesselScheduleSpecification.getVesselAllocation() instanceof CharterInMarket) {
							CharterInMarket charterInMarket = (CharterInMarket) vesselScheduleSpecification.getVesselAllocation();
							if (marketName != null && marketName.equals(charterInMarket.getName())) {
								if (vesselScheduleSpecification.getSpotIndex() == marketVesselAllocationDescriptor.getSpotIndex()) {
									targetVessel = vesselScheduleSpecification;
									break;
								}
							}
						}
					}

					if (targetVessel == null) {
						throw new RuntimeException("Spot vessel market not found for " + marketName);
					}
				} else {
					assert false;
				}
				final String afterID = position.getAfter();
				final String beforeID = position.getBefore();
				final VesselScheduleSpecification pTargetVessel = targetVessel;
				leftOvers.add(() -> {
					String l = beforeID;
					String m = afterID;
					boolean inserted = false;
					for (int i = 0; i < pTargetVessel.getEvents().size(); ++i) {
						final ScheduleSpecificationEvent event = pTargetVessel.getEvents().get(i);
						final String eventID = getEventID(event);
						if (afterID != null && afterID.equalsIgnoreCase(eventID)) {
							if (i + 1 == pTargetVessel.getEvents().size()) {
								pTargetVessel.getEvents().addAll(newEvents);
							} else {
								pTargetVessel.getEvents().addAll(i + 1, newEvents);
							}
							inserted = true;
							break;
						} else if (beforeID != null && beforeID.equalsIgnoreCase(eventID)) {
							pTargetVessel.getEvents().addAll(i, newEvents);
							inserted = true;
							break;
						}
						if (event instanceof SlotSpecification) {
							SlotSpecification slotSpecification = (SlotSpecification) event;
							if (slotSpecification.getSlot() instanceof SpotSlot) {
								if (afterID != null && afterID.toLowerCase().matches(eventID.toLowerCase() + "-[0-9]")) {
									if (i + 1 == pTargetVessel.getEvents().size()) {
										pTargetVessel.getEvents().addAll(newEvents);
									} else {
										pTargetVessel.getEvents().addAll(i + 1, newEvents);
									}
									inserted = true;
									break;
								} else if (beforeID != null && beforeID.toLowerCase().matches(eventID.toLowerCase() + "-[0-9]")) {
									pTargetVessel.getEvents().addAll(i, newEvents);
									inserted = true;
									break;
								}
							}
							
						}
					}
					if (pTargetVessel.getEvents().isEmpty()) {
						pTargetVessel.getEvents().addAll(newEvents);
						inserted = true;
					}
					return inserted;
				});
			}
		}
		boolean changed = true;
		while (changed && !leftOvers.isEmpty()) {
			changed = false;
			Iterator<BooleanSupplier> itr = leftOvers.iterator();
			while (itr.hasNext()) {
				BooleanSupplier supplier = itr.next();
				if (supplier.getAsBoolean()) {
					itr.remove();
					changed = true;
				}
			}
		}
		
		if (!leftOvers .isEmpty()) {
			// We got here because; it looks like the period transformer has extracted all cargoes off the vessel and put two on.
			// Thus the full sequence has multiple carfoes but the positional descriptor does not know about removed cargoes.
			// Maybe we need to run on FULL sequences not just the period?
			
			
			Iterator<BooleanSupplier> itr = leftOvers.iterator();
			while (itr.hasNext()) {
				BooleanSupplier supplier = itr.next();
				if (supplier.getAsBoolean()) {
					itr.remove();
					changed = true;
				}
			}
		}
		
		assert leftOvers.isEmpty();

		return new Pair<>(scheduleSpecification, new ExtraDataProvider(null, null, null, extraLoads, extraDischarges));
	}

	private @NonNull SpotType map(SlotType slotType) {
		switch (slotType) {
		case DES_PURCHASE:
			return SpotType.DES_PURCHASE;
		case DES_SALE:
			return SpotType.DES_SALE;
		case FOB_PURCHASE:
			return SpotType.FOB_PURCHASE;
		case FOB_SALE:
			return SpotType.FOB_SALE;
		default:
			break;

		}
		throw new IllegalArgumentException();
	}

	private String getEventID(final ScheduleSpecificationEvent event) {
		if (event instanceof SlotSpecification) {
			final SlotSpecification slotSpecification = (SlotSpecification) event;
			final Slot slot = slotSpecification.getSlot();
			if (slot != null) {
				return slot.getName();
			}
		} else if (event instanceof VesselEventSpecification) {
			final VesselEventSpecification vesselEventSpecification = (VesselEventSpecification) event;
			final VesselEvent vesselEvent = vesselEventSpecification.getVesselEvent();
			if (vesselEvent != null) {
				return vesselEvent.getName();
			}
		}
		return null;
	}
}
