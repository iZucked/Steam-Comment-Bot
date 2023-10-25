/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.handlers.HandlerUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.lng.importers.merge.adpsync.VesselCharterStartState;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.commercial.StartHeelOptions;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.CapabilityGroup;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortCountryGroup;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.json.EMFDeserializationContext;
import com.mmxlabs.rcp.common.json.EMFJacksonModule;
import com.mmxlabs.rcp.common.json.JSONReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.IBaseCaseVersionsProvider;

public class AdpSyncWithBasecaseHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final Exception exceptions[] = new Exception[1];
		BusyIndicator.showWhile(HandlerUtil.getActiveShellChecked(event).getDisplay(), new Runnable() {
			@Override
			public void run() {
				if (HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection() instanceof IStructuredSelection structuredSelection) {
					final List<?> selections = structuredSelection.toList();
					if (selections.size() == 1 && selections.get(0) instanceof @NonNull ScenarioInstance instance) {
						try {
							syncInitialCargoes(instance);
						} catch (final Exception e) {
							exceptions[0] = e;
						}
					}
				}
			}
		});
		if (exceptions[0] != null) {
			throw new ExecutionException("Unable to sync cargoes into scenarios: " + exceptions[0], exceptions[0]);
		}
		return null;
	}

	private static void resetUuids(final @NonNull LNGScenarioModel lngScenarioModel) {
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(lngScenarioModel);
		final Set<UUIDObject> seenObjects = new HashSet<>();
		Stream.of( //
				cargoModel.getLoadSlots().stream(), //
				cargoModel.getDischargeSlots().stream(), //
				cargoModel.getCargoes().stream(), //
				cargoModel.getVesselEvents().stream() //
		).flatMap(Function.identity()) //
				.filter(seenObjects::add) //
				.forEach(uuidObject -> uuidObject.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID()));
	}

	public static void syncInitialCargoes(final @NonNull ScenarioInstance scenarioInstance) {
		ServiceHelper.<IBaseCaseVersionsProvider>withServiceConsumer(IBaseCaseVersionsProvider.class, p -> {
			final ScenarioInstance baseCase = p.getBaseCase();
			if (baseCase != null) {
				final ScenarioModelRecord baseCaseSmr = SSDataManager.Instance.getModelRecordChecked(baseCase);
				final ScenarioModelRecord scenarioSmr = SSDataManager.Instance.getModelRecordChecked(scenarioInstance);

				try (final IScenarioDataProvider baseCaseSdp = baseCaseSmr.aquireScenarioDataProvider("AdpSyncCargoes:1")) {
					final LNGScenarioModel copiedBasecaseScenario = EcoreUtil.copy(ScenarioModelUtil.getScenarioModel(baseCaseSdp));
					resetUuids(copiedBasecaseScenario);
					try (final IScenarioDataProvider scenarioSdp = scenarioSmr.aquireScenarioDataProvider("AdpSyncCargoes:2")) {
						final ScheduleModel baseCaseScheduleModel = ScenarioModelUtil.getScheduleModel(copiedBasecaseScenario);
						final CargoModel baseCaseCargoModel = ScenarioModelUtil.getCargoModel(copiedBasecaseScenario);
						final CargoModel scenarioCargoModel = ScenarioModelUtil.getCargoModel(scenarioSdp);
						final List<Vessel> scenarioVesselCharterVessels = scenarioCargoModel.getVesselCharters().stream() //
								.map(vc -> vc.getVessel()) //
								.distinct()//
								.toList();
						final Set<Vessel> baseCaseVesselCharterVessels = baseCaseCargoModel.getVesselCharters().stream() //
								.map(VesselCharter::getVessel) //
								.collect(Collectors.toSet());
						final Set<String> baseCaseVesselNames = baseCaseVesselCharterVessels.stream().map(Vessel::getName).map(String::toLowerCase).collect(Collectors.toSet());
						final List<Vessel> vesselCharterVesselsToSync = new LinkedList<>();
						for (final Vessel scenarioVessel : scenarioVesselCharterVessels) {
							if (baseCaseVesselNames.contains(scenarioVessel.getName().toLowerCase())) {
								vesselCharterVesselsToSync.add(scenarioVessel);
							}
						}
						final Set<String> vesselNamesToSync = vesselCharterVesselsToSync.stream().map(Vessel::getName).map(String::toLowerCase).collect(Collectors.toSet());
						final Map<Vessel, List<Object>> lastEventsOnVesselCharters = new HashMap<>();
						final Schedule baseCaseSchedule = baseCaseScheduleModel.getSchedule();
						final List<VesselEvent> vesselEventsToAdd = new LinkedList<>();
						final List<Slot<?>> slotsToAdd = new LinkedList<>();
						final List<Cargo> cargoesToAdd = new LinkedList<>();
						final Map<String, VesselCharterStartState> vesselCharterInitialConditions = new HashMap<>();
						if (baseCaseSchedule != null) {
							final List<Sequence> sequences = baseCaseSchedule.getSequences();
							for (final Sequence sequence : sequences) {
								final VesselCharter vc = sequence.getVesselCharter();
								if (vc != null) {
									final String vesselName = vc.getVessel().getName();
									if (vesselNamesToSync.contains(vesselName.toLowerCase())) {
										final List<VesselEventVisit> nonSlotVisitEvents = new LinkedList<>();
										SlotVisit lastSlotVisit = null;
										final Event lastEvent = sequence.getEvents().get(sequence.getEvents().size() - 1);
										Event currentEvent = lastEvent;
										VesselCharterStartState currentStartState = null;
										while (currentEvent != null) {
											if (currentEvent instanceof @NonNull SlotVisit slotVisit) {
												lastSlotVisit = slotVisit;
												final Set<PortVisit> portVisitsToSkip = slotVisit.getSlotAllocation().getCargoAllocation().getSlotAllocations().stream() //
														.map(SlotAllocation::getSlotVisit) //
														.collect(Collectors.toSet());
												final PortVisit previousPv = getPreviousPortVisit(slotVisit, portVisitsToSkip);
												final ZonedDateTime newVcStart = previousPv.getEnd();
												final int eventStartHeel = previousPv.getHeelAtEnd();
												final Port eventStartPort = slotVisit.getSlotAllocation().getCargoAllocation().getSlotAllocations().get(0).getPort();
												final Optional<Port> optScenarioPortEquivalent = ScenarioModelUtil.getPortModel(scenarioSdp).getPorts().stream()
														.filter(port -> port.getLocation().getMmxId().equalsIgnoreCase(eventStartPort.getLocation().getMmxId())).findAny();
												final double heelPrice = previousPv.getHeelCostUnitPrice();
												final double heelCv = eventStartHeel == 0 ? 0 : getCv(previousPv, vc);
												currentStartState = new VesselCharterStartState(vc, newVcStart, eventStartHeel, heelPrice,
														optScenarioPortEquivalent.isPresent() ? optScenarioPortEquivalent.get() : null, heelCv);
												break;
											} else if (currentEvent instanceof @NonNull VesselEventVisit vev) {
												nonSlotVisitEvents.add(vev);
												final PortVisit previousPv = getPreviousPortVisit(vev);
												final ZonedDateTime newVcStart = previousPv.getEnd();
												final int eventStartHeel = previousPv.getHeelAtEnd();
												final Port eventStartPort = currentEvent.getPort();
												final Optional<Port> optScenarioPortEquivalent = ScenarioModelUtil.getPortModel(scenarioSdp).getPorts().stream()
														.filter(port -> port.getLocation().getMmxId().equalsIgnoreCase(eventStartPort.getLocation().getMmxId())).findAny();
												final double heelPrice = previousPv.getHeelCostUnitPrice();
												final double heelCv = eventStartHeel == 0 ? 0 : getCv(previousPv, vc);
												currentStartState = new VesselCharterStartState(vc, newVcStart, eventStartHeel, heelPrice,
														optScenarioPortEquivalent.isPresent() ? optScenarioPortEquivalent.get() : null, heelCv);
											}
											currentEvent = currentEvent.getPreviousEvent();
										}
										if (lastSlotVisit == null && nonSlotVisitEvents.isEmpty()) {
											continue;
										}
										if (lastSlotVisit != null) {
											final Slot<?> slot = lastSlotVisit.getSlotAllocation().getSlot();
											final Cargo cargo = slot.getCargo();
											cargoesToAdd.add(cargo);
											slotsToAdd.addAll(cargo.getSlots());
											if (!nonSlotVisitEvents.isEmpty()) {
												for (final VesselEventVisit vev : nonSlotVisitEvents) {
													vesselEventsToAdd.add(vev.getVesselEvent());
												}
											}
										} else {
											// Trim to only closest event?
											for (final VesselEventVisit vev : nonSlotVisitEvents) {
												vesselEventsToAdd.add(vev.getVesselEvent());
											}
										}
										if (currentStartState != null) {
											vesselCharterInitialConditions.put(vc.getVessel().getName().toLowerCase(), currentStartState);
										}
									}
								}
							}
						}

						final LNGScenarioModel scenarioModel = ScenarioModelUtil.getScenarioModel(scenarioSdp);
						remapPortData(copiedBasecaseScenario, scenarioModel);
						remapCommercialData(copiedBasecaseScenario, scenarioModel);

						final List<Pair<JSONReference, String>> missingReferences = new LinkedList<>();
						final EMFDeserializationContext ctx = new EMFDeserializationContext(BeanDeserializerFactory.instance);
						final ObjectMapper jsonWriter = new ObjectMapper();
						jsonWriter.registerModule(new EMFJacksonModule());

						ctx.setMissingFeatureHandler((ref, lbl) -> {
							System.out.println("Unknown reference " + ref.getName() + " " + ref.getGlobalId() + " " + ref.getClassType());
							missingReferences.add(Pair.of(ref, lbl));
							return false;
						});

						ctx.ignoreFeature(CargoPackage.Literals.SLOT__CARGO);

						final CargoModel cam = ScenarioModelUtil.getCargoModel(scenarioSdp);
						cam.getVesselCharters().forEach(ctx::registerType);

						final PortModel pm = ScenarioModelUtil.getPortModel(scenarioSdp);
						pm.getPorts().forEach(ctx::registerType);
						pm.getPortGroups().forEach(ctx::registerType);
						pm.getPortCountryGroups().forEach(ctx::registerType);
						pm.getSpecialPortGroups().forEach(ctx::registerType);

						final FleetModel fm = ScenarioModelUtil.getFleetModel(scenarioSdp);
						fm.getBaseFuels().forEach(ctx::registerType);
						fm.getVessels().forEach(ctx::registerType);
						fm.getVesselGroups().forEach(ctx::registerType);

						final CommercialModel cm = ScenarioModelUtil.getCommercialModel(scenarioSdp);
						cm.getCharterContracts().forEach(ctx::registerType);
						cm.getPurchaseContracts().forEach(ctx::registerType);
						cm.getSalesContracts().forEach(ctx::registerType);
						cm.getEntities().forEach(ctx::registerType);

						final SpotMarketsModel smm = ScenarioModelUtil.getSpotMarketsModel(scenarioSdp);
						smm.getCharterInMarkets().forEach(ctx::registerType);
						smm.getDesPurchaseSpotMarket().getMarkets().forEach(ctx::registerType);
						smm.getDesSalesSpotMarket().getMarkets().forEach(ctx::registerType);
						smm.getFobPurchasesSpotMarket().getMarkets().forEach(ctx::registerType);
						smm.getFobSalesSpotMarket().getMarkets().forEach(ctx::registerType);

						final ObjectMapper jsonReader = new ObjectMapper(null, null, ctx);
						jsonReader.registerModule(new EMFJacksonModule());
						final CompoundCommand cmd = new CompoundCommand("Merge basecase cargoes into ADP");
						final List<LoadSlot> loadSlotsToAdd = new LinkedList<>();
						final List<DischargeSlot> dischargeSlotsToAdd = new LinkedList<>();
						final List<Cargo> newCargoesToAdd = new LinkedList<>();
						final List<VesselEvent> newVesselEventsToAdd = new LinkedList<>();
						final Set<Cargo> seenCargoes = new HashSet<>();
						for (final Slot<?> slot : slotsToAdd) {
							try {
								final String slotJson = jsonWriter.writerWithDefaultPrettyPrinter().writeValueAsString(slot);
								final EObject eObj = (EObject) jsonReader.readValue(slotJson, slot.eClass().getInstanceClass());
								if (eObj instanceof LoadSlot ls) {
									loadSlotsToAdd.add(ls);
								} else if (eObj instanceof DischargeSlot ds) {
									dischargeSlotsToAdd.add(ds);
								}
							} catch (JsonProcessingException e) {

							}
						}
						for (final Cargo cargo : cargoesToAdd) {
							try {
								final String cargoJson = jsonWriter.writerWithDefaultPrettyPrinter().writeValueAsString(cargo);
								final EObject cargoEObj = (EObject) jsonReader.readValue(cargoJson, cargo.eClass().getInstanceClass());
								if (cargoEObj instanceof Cargo newCargo) {
									newCargoesToAdd.add(newCargo);
									newCargo.setAllowRewiring(false);
									newCargo.setLocked(true);
								}
							} catch (JsonProcessingException e) {

							}
						}
						for (final VesselEvent ve : vesselEventsToAdd) {
							try {
								final String vesselEventJson = jsonWriter.writerWithDefaultPrettyPrinter().writeValueAsString(ve);
								final EObject vesselEventEObj = (EObject) jsonReader.readValue(vesselEventJson, ve.eClass().getInstanceClass());
								if (vesselEventEObj instanceof VesselEvent newVesselEvent) {
									newVesselEventsToAdd.add(newVesselEvent);
								}
							} catch (JsonProcessingException e) {

							}
						}
						ctx.runDeferredActions();
						final EditingDomain ed = scenarioSdp.getEditingDomain();

						for (final Entry<String, VesselCharterStartState> entry : vesselCharterInitialConditions.entrySet()) {
							final Optional<VesselCharter> optVc = scenarioCargoModel.getVesselCharters().stream() //
									.filter(vc -> vc.getVessel().getName().toLowerCase().equals(entry.getKey())) //
									.findFirst();
							if (optVc.isPresent()) {
								final VesselCharter scenarioVc = optVc.get();
								final VesselCharterStartState vcss = entry.getValue();
								final ZonedDateTime newZdtUtc = vcss.getStartTime().withZoneSameInstant(ZoneId.of("UTC"));
								cmd.append(SetCommand.create(ed, scenarioVc, CargoPackage.eINSTANCE.getVesselCharter_StartAfter(), newZdtUtc.toLocalDateTime()));
								cmd.append(SetCommand.create(ed, scenarioVc, CargoPackage.eINSTANCE.getVesselCharter_StartBy(), newZdtUtc.toLocalDateTime()));
								if (vcss.getVesselStartPort() != null) {
									cmd.append(SetCommand.create(ed, scenarioVc, CargoPackage.eINSTANCE.getVesselCharter_StartAt(), vcss.getVesselStartPort()));
								}
								final StartHeelOptions sho = scenarioVc.getStartHeel();
								cmd.append(SetCommand.create(ed, sho, CommercialPackage.eINSTANCE.getStartHeelOptions_MinVolumeAvailable(), (double) vcss.getStartHeel()));
								cmd.append(SetCommand.create(ed, sho, CommercialPackage.eINSTANCE.getStartHeelOptions_MaxVolumeAvailable(), (double) vcss.getStartHeel()));
								cmd.append(SetCommand.create(ed, sho, CommercialPackage.eINSTANCE.getStartHeelOptions_PriceExpression(), Double.toString(vcss.getStartHeelPrice())));
								cmd.append(SetCommand.create(ed, sho, CommercialPackage.eINSTANCE.getStartHeelOptions_CvValue(), vcss.getHeelCv()));
							}
						}
						if (!loadSlotsToAdd.isEmpty()) {
							cmd.append(AddCommand.create(ed, scenarioCargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), loadSlotsToAdd));
						}
						if (!dischargeSlotsToAdd.isEmpty()) {
							cmd.append(AddCommand.create(ed, scenarioCargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), dischargeSlotsToAdd));
						}
						if (!cargoesToAdd.isEmpty()) {
							cmd.append(AddCommand.create(ed, scenarioCargoModel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), newCargoesToAdd));
						}
						if (!newVesselEventsToAdd.isEmpty()) {
							cmd.append(AddCommand.create(ed, scenarioCargoModel, CargoPackage.eINSTANCE.getCargoModel_VesselEvents(), newVesselEventsToAdd));
						}
						if (!cmd.isEmpty()) {
							if (ed instanceof CommandProviderAwareEditingDomain cpaed) {
								cpaed.setAdaptersEnabled(false);
							}
							try {
								ed.getCommandStack().execute(cmd);
							} finally {
								if (ed instanceof CommandProviderAwareEditingDomain cpaed) {
									cpaed.setAdaptersEnabled(true);
								}
							}
						}
					}
				}
			}
		});
	}

	private static PortVisit getPreviousPortVisit(final @NonNull Event event) {
		return getPreviousPortVisit(event, Collections.emptySet());
	}

	private static PortVisit getPreviousPortVisit(final @NonNull Event event, final Set<PortVisit> visitsToSkip) {
		Event currentEvent = event.getPreviousEvent();
		while (currentEvent != null) {
			if (currentEvent instanceof PortVisit pv && !visitsToSkip.contains(pv)) {
				return pv;
			}
			currentEvent = currentEvent.getPreviousEvent();
		}
		return null;
	}

	private static double getCv(final @NonNull PortVisit closestPortVisit, final @NonNull VesselCharter vc) {
		Event currentEvent = closestPortVisit;
		while (currentEvent != null) {
			if (currentEvent instanceof SlotVisit sv) {
				return sv.getSlotAllocation().getCv();
			} else if (currentEvent instanceof VesselEventVisit vev) {
				final VesselEvent ve = vev.getVesselEvent();
				if (ve instanceof DryDockEvent) {
					return 0;
				} else if (ve instanceof CharterOutEvent coe) {
					return coe.getAvailableHeel().getCvValue();
				}
			} else if (currentEvent instanceof StartEvent se) {
				final SlotAllocation sa = se.getSlotAllocation();
				if (sa != null) {
					return se.getSlotAllocation().getCv();
				} else {
					return vc.getStartHeel().getCvValue();
				}
			}
			currentEvent = currentEvent.getPreviousEvent();
		}
		return 0.0;
	}

	private static void updateReferencesViaSet(LNGScenarioModel model, EObject oldObject, EObject newObject) {
		final List<EObject> wrappedOldObject = Collections.singletonList(oldObject);
		final Map<EObject, Collection<EStructuralFeature.Setting>> usagesMap = EcoreUtil.UsageCrossReferencer.findAll(wrappedOldObject, model);
		for (final EObject eObj : wrappedOldObject) {
			final Collection<EStructuralFeature.Setting> usages = usagesMap.get(eObj);
			if (usages != null) {
				for (final EStructuralFeature.Setting setting : usages) {
					if (setting.getEStructuralFeature().isMany()) {
						final Collection<?> collection = (Collection<?>) setting.getEObject().eGet(setting.getEStructuralFeature());
						if (collection.contains(newObject)) {
							collection.remove(eObj);
						} else {
							EcoreUtil.replace(setting, eObj, newObject);
						}
					} else {
						EcoreUtil.replace(setting, eObj, newObject);
					}
				}
			}
		}
	}

	private static void remapPortData(@NonNull LNGScenarioModel baseCaseModel, @NonNull LNGScenarioModel scenarioModel) {
		final PortModel baseCasePortModel = ScenarioModelUtil.getPortModel(baseCaseModel);
		final PortModel scenarioPortModel = ScenarioModelUtil.getPortModel(scenarioModel);
		final Map<String, Port> baseCasePortMap = new HashMap<>();
		baseCasePortModel.getPorts().stream().forEach(p -> baseCasePortMap.put(p.getLocation().getMmxId().toLowerCase(), p));
		for (final Port port : ScenarioModelUtil.getPortModel(scenarioModel).getPorts()) {
			final Port baseCasePort = baseCasePortMap.get(port.getLocation().getMmxId().toLowerCase());
			if (baseCasePort != null) {
				updateReferencesViaSet(baseCaseModel, baseCasePort, port);
			}
		}

		final Map<String, PortGroup> baseCasePortGroupMap = new HashMap<>();
		baseCasePortModel.getPortGroups().forEach(pg -> baseCasePortGroupMap.put(pg.getName().toLowerCase(), pg));
		for (final PortGroup portGroup : scenarioPortModel.getPortGroups()) {
			final PortGroup baseCasePortGroup = baseCasePortGroupMap.get(portGroup.getName().toLowerCase());
			if (baseCasePortGroup != null) {
				updateReferencesViaSet(baseCaseModel, baseCasePortGroup, portGroup);
			}
		}

		final Map<String, PortCountryGroup> baseCasePortCountryGroupMap = new HashMap<>();
		baseCasePortModel.getPortCountryGroups().forEach(pcg -> baseCasePortCountryGroupMap.put(pcg.getName().toLowerCase(), pcg));
		for (final PortCountryGroup pcg : scenarioPortModel.getPortCountryGroups()) {
			final PortCountryGroup baseCasePcg = baseCasePortCountryGroupMap.get(pcg.getName().toLowerCase());
			if (baseCasePcg != null) {
				updateReferencesViaSet(baseCaseModel, baseCasePcg, pcg);
			}
		}

		final Map<String, CapabilityGroup> baseCasePortCapabilityGroupMap = new HashMap<>();
		baseCasePortModel.getSpecialPortGroups().forEach(spg -> baseCasePortCapabilityGroupMap.put(spg.getName().toLowerCase(), spg));
		for (final CapabilityGroup cg : scenarioPortModel.getSpecialPortGroups()) {
			final CapabilityGroup baseCaseCg = baseCasePortCapabilityGroupMap.get(cg.getName().toLowerCase());
			if (baseCaseCg != null) {
				updateReferencesViaSet(baseCaseModel, baseCaseCg, cg);
			}
		}
	}

	private static void remapCommercialData(@NonNull LNGScenarioModel baseCaseModel, @NonNull LNGScenarioModel scenarioModel) {
		final CommercialModel baseCaseCommercialModel = ScenarioModelUtil.getCommercialModel(baseCaseModel);
		final CommercialModel scenarioCommercialModel = ScenarioModelUtil.getCommercialModel(scenarioModel);
		final Map<String, PurchaseContract> baseCasePurchaseContracts = new HashMap<>();
		baseCaseCommercialModel.getPurchaseContracts().forEach(pc -> baseCasePurchaseContracts.put(pc.getName().toLowerCase(), pc));
		for (final PurchaseContract pc : scenarioCommercialModel.getPurchaseContracts()) {
			final PurchaseContract baseCasePc = baseCasePurchaseContracts.get(pc.getName().toLowerCase());
			if (baseCasePc != null) {
				updateReferencesViaSet(baseCaseModel, baseCasePc, pc);
			}
		}

		final Map<String, SalesContract> baseCaseSalesContracts = new HashMap<>();
		baseCaseCommercialModel.getSalesContracts().forEach(sc -> baseCaseSalesContracts.put(sc.getName().toLowerCase(), sc));
		for (final SalesContract sc : scenarioCommercialModel.getSalesContracts()) {
			final SalesContract baseCaseSc = baseCaseSalesContracts.get(sc.getName().toLowerCase());
			if (baseCaseSc != null) {
				updateReferencesViaSet(baseCaseModel, baseCaseSc, sc);
			}
		}

		final Map<String, BaseLegalEntity> baseCaseEntities = new HashMap<>();
		baseCaseCommercialModel.getEntities().forEach(e -> baseCaseEntities.put(e.getName().toLowerCase(), e));
		for (final BaseLegalEntity e : scenarioCommercialModel.getEntities()) {
			final BaseLegalEntity baseCaseE = baseCaseEntities.get(e.getName().toLowerCase());
			if (baseCaseE != null) {
				updateReferencesViaSet(baseCaseModel, baseCaseE, e);
			}
		}
	}
}
