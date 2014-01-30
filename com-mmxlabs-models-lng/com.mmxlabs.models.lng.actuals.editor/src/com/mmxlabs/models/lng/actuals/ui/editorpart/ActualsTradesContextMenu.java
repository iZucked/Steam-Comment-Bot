package com.mmxlabs.models.lng.actuals.ui.editorpart;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;

import com.mmxlabs.models.lng.actuals.ActualsFactory;
import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.SlotActuals;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.trades.ITradesTableContextMenuExtension;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
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

					ActualsModel actualsModel = null;
					final CargoModel cargoModel = portfolioModel.getCargoModel();
					if (cargoModel != null) {
						for (final EObject ext : cargoModel.getExtensions()) {
							if (ext instanceof ActualsModel) {
								actualsModel = (ActualsModel) ext;
								break;
							}
						}
					}

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

		@Override
		public void run() {

			// Step 1: Find or create Actuals model if needed.
			final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
			if (rootObject instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				final LNGPortfolioModel portfolioModel = lngScenarioModel.getPortfolioModel();
				if (portfolioModel != null) {

					final CompoundCommand cmd = new CompoundCommand();
					ActualsModel actualsModel = null;
					final CargoModel cargoModel = portfolioModel.getCargoModel();
					if (cargoModel != null) {
						for (final EObject ext : cargoModel.getExtensions()) {
							if (ext instanceof ActualsModel) {
								actualsModel = (ActualsModel) ext;
								break;
							}
						}
						if (actualsModel == null) {
							actualsModel = ActualsFactory.eINSTANCE.createActualsModel();
							cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), cargoModel, MMXCorePackage.Literals.MMX_OBJECT__EXTENSIONS, actualsModel));
						}
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

					for (final Slot slot : cargo.getSlots()) {

						SlotActuals slotActuals = null;
						if (slot instanceof LoadSlot) {
							final LoadSlot loadSlot = (LoadSlot) slot;
							slotActuals = ActualsFactory.eINSTANCE.createLoadActuals();
							if (loadSlot.isDESPurchase()) {
								if (slot.getAssignment() instanceof Vessel) {
									cargoActuals.setVessel((Vessel) slot.getAssignment());
								}
							}
							slotActuals.setCV(((LoadSlot) slot).getCargoCV());
						} else if (slot instanceof DischargeSlot) {
							final DischargeSlot dischargeSlot = (DischargeSlot) slot;
							slotActuals = ActualsFactory.eINSTANCE.createDischargeActuals();
							if (dischargeSlot.isFOBSale()) {
								if (slot.getAssignment() instanceof Vessel) {
									cargoActuals.setVessel((Vessel) slot.getAssignment());
								}
							}
						}
						if (slotActuals != null) {
							slotActuals.setSlot(slot);
							slotActuals.setNotes(slot.getNotes());
							Contract c = slot.getContract();
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

									for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
										final SlotActuals slotActuals = slotActualMap.get(slotAllocation.getSlot());
										slotActuals.setOperationsStart(slotAllocation.getLocalStart().getTime());
										slotActuals.setOperationsEnd(slotAllocation.getLocalEnd().getTime());
										slotActuals.setTitleTransferPoint(slotAllocation.getPort());
										slotActuals.setVolumeInM3(slotAllocation.getVolumeTransferred());
										slotActuals.setPriceDOL(slotAllocation.getPrice());
										slotActuals.setPortCharges(slotAllocation.getSlotVisit().getPortCost());

										// set a base fuel price.
										for (FuelQuantity fq : slotAllocation.getSlotVisit().getFuels()) {
											if (fq.getFuel() == Fuel.BASE_FUEL) {
												if (fq.getAmounts().size() > 0) {
													cargoActuals.setBaseFuelPrice(fq.getAmounts().get(0).getUnitPrice());
												}
											}
										}
									}
								}
							}
						}
					}

					cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), actualsModel, ActualsPackage.Literals.ACTUALS_MODEL__CARGO_ACTUALS, cargoActuals));

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
