/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.actuals.ui.editorpart;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;

import com.mmxlabs.models.lng.actuals.ActualsFactory;
import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.DischargeActuals;
import com.mmxlabs.models.lng.actuals.LoadActuals;
import com.mmxlabs.models.lng.actuals.ReturnActuals;
import com.mmxlabs.models.lng.actuals.SlotActuals;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.trades.ITradesTableContextMenuExtension;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.scenario.service.model.ScenarioLock;

public class ActualsTradesContextMenu implements ITradesTableContextMenuExtension {

	@Override
	public void contributeToMenu(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final Slot slot, @NonNull final MenuManager menuManager) {

		final Cargo cargo = slot.getCargo();
		if (cargo != null) {

			boolean actualisePossible = false;

			final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
			if (rootObject instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				final LNGPortfolioModel portfolioModel = lngScenarioModel.getPortfolioModel();
				if (portfolioModel != null) {

					final ActualsModel actualsModel = portfolioModel.getActualsModel();

					if (actualsModel == null) {
						actualisePossible = true;
					} else {

						for (final CargoActuals cargoActuals : actualsModel.getCargoActuals()) {
							if (cargoActuals.getCargo() == cargo) {
								// Existing slot!
								actualisePossible = false;
								return;
							}
						}
						actualisePossible = true;
					}
				}
			}

			if (actualisePossible) {
				menuManager.add(new ActualiseCargoAction(scenarioEditingLocation, cargo));
			}

		}
	}

	private static class ActualiseCargoAction extends Action {
		private final IScenarioEditingLocation scenarioEditingLocation;
		private final Cargo cargo;

		public ActualiseCargoAction(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final Cargo cargo) {
			super("Actualise Cargo");
			this.scenarioEditingLocation = scenarioEditingLocation;
			this.cargo = cargo;
		}

		private int getFuelUse(final FuelUsage fuelUsage, final Fuel fuel, final FuelUnit fuelUnit) {
			int total = 0;
			for (final FuelQuantity fq : fuelUsage.getFuels()) {
				if (fq.getFuel().equals(fuel)) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit().equals(fuelUnit)) {
							total += amount.getQuantity();
						}
					}
				}
			}
			return total;

		}

		@Override
		public void run() {

			// Step 1: Find or create Actuals model if needed.
			final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
			if (rootObject instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				final LNGPortfolioModel portfolioModel = lngScenarioModel.getPortfolioModel();
				final PortModel portModel = lngScenarioModel.getPortModel();
				if (portfolioModel != null) {

					final CompoundCommand cmd = new CompoundCommand();
					ActualsModel actualsModel = portfolioModel.getActualsModel();
					if (actualsModel == null) {
						actualsModel = ActualsFactory.eINSTANCE.createActualsModel();
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), portfolioModel, LNGScenarioPackage.eINSTANCE.getLNGPortfolioModel_ActualsModel(), actualsModel));
					}

					if (actualsModel == null) {
						// Cannot do anything more...
						return;
					}

					for (final CargoActuals cargoActuals : actualsModel.getCargoActuals()) {
						if (cargoActuals.getCargo() == cargo) {
							// Existing slot!
							return;
						}
					}

					// Create actuals and map data from cargo
					final CargoActuals cargoActuals = ActualsFactory.eINSTANCE.createCargoActuals();
					cargoActuals.setCargo(cargo);
					cargoActuals.setCargoReference(cargo.getName());
					if (cargo.getAssignment() instanceof Vessel) {
						cargoActuals.setVessel((Vessel) cargo.getAssignment());
					}

					final Map<Slot, SlotActuals> slotActualMap = new HashMap<>();

					// True if DES and divertible -- need to look up distances!
					boolean isDivertableDESPurchase = false;

					Port loadPort = null;
					Port dischargePort = null;

					for (final Slot slot : cargo.getSlots()) {

						SlotActuals slotActuals = null;
						if (slot instanceof LoadSlot) {
							final LoadSlot loadSlot = (LoadSlot) slot;
							loadPort = loadSlot.getPort();
							slotActuals = ActualsFactory.eINSTANCE.createLoadActuals();
							if (loadSlot.isDESPurchase()) {
								isDivertableDESPurchase = loadSlot.isDivertible();

								if (slot.getAssignment() instanceof Vessel) {
									cargoActuals.setVessel((Vessel) slot.getAssignment());
								}
							} else {
							}
							slotActuals.setCV(((LoadSlot) slot).getSlotOrDelegatedCV());

							((LoadActuals) slotActuals).setContractType(loadSlot.isDESPurchase() ? "DES" : "FOB");
						} else if (slot instanceof DischargeSlot) {
							final DischargeSlot dischargeSlot = (DischargeSlot) slot;
							dischargePort = dischargeSlot.getPort();
							slotActuals = ActualsFactory.eINSTANCE.createDischargeActuals();
							if (dischargeSlot.isFOBSale()) {
								if (slot.getAssignment() instanceof Vessel) {
									cargoActuals.setVessel((Vessel) slot.getAssignment());
								}
							}

							((DischargeActuals) slotActuals).setDeliveryType(dischargeSlot.isFOBSale() ? "FOB" : "DES");
						}
						if (slotActuals != null) {
							slotActuals.setSlot(slot);
							slotActuals.setNotes(slot.getNotes());
							final Contract c = slot.getContract();
							if (c != null) {
								slotActuals.setCounterparty(c.getName());
							}

							slotActualMap.put(slot, slotActuals);
							cargoActuals.getActuals().add(slotActuals);
						}
					}

					// Lookup CargoAllocation & copy relevant details across
					final ScheduleModel scheduleModel = portfolioModel.getScheduleModel();
					if (scheduleModel != null) {
						final Schedule schedule = scheduleModel.getSchedule();
						if (schedule != null) {
							for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
								if (cargoAllocation.getInputCargo() == cargo) {
									int ladenBaseFuelConsumptionInMT = 0;
									int ballastBaseFuelConsumptionInMT = 0;

									int ballastRouteCosts = 0;
									int ladenRouteCosts = 0;

									int ladenDistance = 0;
									int ballastDistance = 0;
									Route ladenRoute = null;
									Route ballastRoute = null;
									boolean isLaden = false;

									long totalCharterCost = 0;
									int totalDurationInHours = 0;

									for (final Event event : cargoAllocation.getEvents()) {

										totalCharterCost += event.getCharterCost();
										totalDurationInHours += event.getDuration();

										if (event instanceof SlotVisit) {
											final SlotVisit slotVisit = (SlotVisit) event;
											isLaden = slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot;

											// All port fuel use is part of laden allocation
											ladenBaseFuelConsumptionInMT += getFuelUse(slotVisit, Fuel.BASE_FUEL, FuelUnit.MT);
											ladenBaseFuelConsumptionInMT += getFuelUse(slotVisit, Fuel.PILOT_LIGHT, FuelUnit.MT);
										} else if (event instanceof Journey) {
											final Journey journey = (Journey) event;
											if (isLaden) {
												ladenBaseFuelConsumptionInMT += getFuelUse(journey, Fuel.BASE_FUEL, FuelUnit.MT);
												ladenBaseFuelConsumptionInMT += getFuelUse(journey, Fuel.PILOT_LIGHT, FuelUnit.MT);

												ladenDistance += journey.getDistance();
												ladenRouteCosts += journey.getToll();

												ladenRoute = journey.getRoute();
											} else {
												ballastBaseFuelConsumptionInMT += getFuelUse(journey, Fuel.BASE_FUEL, FuelUnit.MT);
												ballastBaseFuelConsumptionInMT += getFuelUse(journey, Fuel.PILOT_LIGHT, FuelUnit.MT);

												ballastDistance += journey.getDistance();
												ballastRouteCosts += journey.getToll();

												ballastRoute = journey.getRoute();
											}

										} else if (event instanceof Idle) {
											final Idle idle = (Idle) event;
											if (isLaden) {
												ladenBaseFuelConsumptionInMT += getFuelUse(idle, Fuel.BASE_FUEL, FuelUnit.MT);
												ladenBaseFuelConsumptionInMT += getFuelUse(idle, Fuel.PILOT_LIGHT, FuelUnit.MT);
											} else {
												ballastBaseFuelConsumptionInMT += getFuelUse(idle, Fuel.BASE_FUEL, FuelUnit.MT);
												ballastBaseFuelConsumptionInMT += getFuelUse(idle, Fuel.PILOT_LIGHT, FuelUnit.MT);
											}

										}
									}

									// Look up distances for divertable des cargoes
									if (isDivertableDESPurchase && portModel != null && loadPort != null && dischargePort != null) {

										// Take direct route
										for (final Route route : portModel.getRoutes()) {
											if (route.isCanal()) {
												continue;
											}
											for (final RouteLine rl : route.getLines()) {
												if (rl.getFrom() == loadPort && rl.getTo() == dischargePort) {
													ladenRoute = route;
													ladenDistance = rl.getDistance();
												}
												if (rl.getTo() == loadPort && rl.getFrom() == dischargePort) {
													ballastRoute = route;
													ballastDistance = rl.getDistance();
												}
											}
										}
									}

									for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
										final SlotActuals slotActuals = slotActualMap.get(slotAllocation.getSlot());
										slotActuals.setOperationsStart(slotAllocation.getLocalStart().getTime());
										slotActuals.setOperationsEnd(slotAllocation.getLocalEnd().getTime());
										slotActuals.setTitleTransferPoint(slotAllocation.getPort());
										slotActuals.setVolumeInM3(slotAllocation.getVolumeTransferred());
										slotActuals.setCV(slotAllocation.getCv());
										slotActuals.setVolumeInMMBtu(slotAllocation.getEnergyTransferred());

										slotActuals.setPriceDOL(slotAllocation.getPrice());
										slotActuals.setPortCharges(slotAllocation.getSlotVisit().getPortCost());

										if (slotActuals instanceof LoadActuals) {

											final LoadActuals loadActuals = (LoadActuals) slotActuals;
											loadActuals.setStartingHeelM3(slotAllocation.getSlotVisit().getHeelAtStart());
											loadActuals.setStartingHeelMMBTu((int) Math.round(slotAllocation.getSlotVisit().getHeelAtStart() * slotAllocation.getCv()));
											slotActuals.setBaseFuelConsumption(ladenBaseFuelConsumptionInMT);
											slotActuals.setRouteCosts(ladenRouteCosts);
											slotActuals.setDistance(ladenDistance);
											slotActuals.setRoute(ladenRoute);

											// Reset in case of multiple loads/discharges!
											ladenBaseFuelConsumptionInMT = 0;
											ladenRouteCosts = 0;
											ladenDistance = 0;
											ladenRoute = null;
										} else if (slotActuals instanceof DischargeActuals) {
											((DischargeActuals) slotActuals).setEndHeelM3(slotAllocation.getSlotVisit().getHeelAtEnd());
											((DischargeActuals) slotActuals).setEndHeelMMBTu((int) Math.round(slotAllocation.getSlotVisit().getHeelAtEnd() * slotAllocation.getCv()));
											slotActuals.setBaseFuelConsumption(ballastBaseFuelConsumptionInMT);
											slotActuals.setRouteCosts(ballastRouteCosts);
											slotActuals.setDistance(ballastDistance);
											slotActuals.setRoute(ballastRoute);
											// Reset in case of multiple loads/discharges!
											ballastBaseFuelConsumptionInMT = 0;
											ballastRouteCosts = 0;
											ballastDistance = 0;
											ballastRoute = null;
										}

										// set a base fuel price.
										for (final FuelQuantity fq : slotAllocation.getSlotVisit().getFuels()) {
											if (fq.getFuel() == Fuel.BASE_FUEL) {
												if (fq.getAmounts().size() > 0) {
													cargoActuals.setBaseFuelPrice(fq.getAmounts().get(0).getUnitPrice());
												}
											}
										}
									}

									// Take average charter cost
									cargoActuals.setCharterRatePerDay((int) Math.round((double) totalCharterCost / ((double) totalDurationInHours / 24.0)));

									final ReturnActuals returnActuals = ActualsFactory.eINSTANCE.createReturnActuals();

									cargoActuals.setReturnActuals(returnActuals);

									final EList<Event> events = cargoAllocation.getEvents();
									final Event lastEvent = events.get(events.size() - 1);
									final Event nextEvent = lastEvent.getNextEvent();
									final Sequence sequence = lastEvent.getSequence();
									if (nextEvent != null && (sequence.getSequenceType() == SequenceType.VESSEL || sequence.getSequenceType() == SequenceType.SPOT_VESSEL)) {
										returnActuals.setEndHeelM3(nextEvent.getHeelAtStart());
										returnActuals.setTitleTransferPoint(nextEvent.getPort());
										returnActuals.setOperationsStart(nextEvent.getLocalStart().getTime());
									} else {
										final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
										cal.clear();
										// In the past to trigger validation error.
										cal.set(Calendar.YEAR, 2010);
										returnActuals.setOperationsStart(cal.getTime());

										if (isDivertableDESPurchase && loadPort != null) {
											returnActuals.setTitleTransferPoint(loadPort);
										}
									}

								}
							}
						}
					}

					cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), actualsModel, ActualsPackage.Literals.ACTUALS_MODEL__CARGO_ACTUALS, cargoActuals));
					// Disallow re-wiring
					cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), cargo, CargoPackage.Literals.CARGO__ALLOW_REWIRING, Boolean.FALSE));
					// Disallow vessel assignment changes
					if (cargo.getCargoType() == CargoType.FLEET) {
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED, Boolean.TRUE));
					}

					if (cmd.canExecute()) {
						scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
					}

					final DetailCompositeDialog dcd = new DetailCompositeDialog(scenarioEditingLocation.getShell(), scenarioEditingLocation.getDefaultCommandHandler());
					final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
					try {
						editorLock.claim();
						scenarioEditingLocation.setDisableUpdates(true);

						dcd.open(scenarioEditingLocation, scenarioEditingLocation.getRootObject(), Collections.<EObject> singletonList(cargoActuals), scenarioEditingLocation.isLocked());
					} finally {
						scenarioEditingLocation.setDisableUpdates(false);
						editorLock.release();
					}

				}
			}
		}
	}
}
