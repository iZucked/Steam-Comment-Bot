package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.ui.properties.views.DetailPropertiesView;

public class PNLDetailsReport extends DetailPropertiesView {

	public PNLDetailsReport() {
		super("pnl", "com.mmxlabs.shiplingo.platform.reports.views.PNLDetailsReport", false);
	}

	@Override
	protected Collection<?> adaptSelection(final ISelection selection) {
		if (selection instanceof IStructuredSelection) {

			final Iterator<Object> itr = ((IStructuredSelection) selection).iterator();
			final Set<Object> adaptedObjects = new HashSet<>();
			while (itr.hasNext()) {
				final Object a = itr.next();

				// map to events
				if (a instanceof CargoAllocation) {
					adaptedObjects.add(a);
				} else if (a instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) a;
					final SlotAllocation slotAllocation = slotVisit.getSlotAllocation();
					if (slotAllocation.getCargoAllocation() != null) {
						adaptedObjects.add(slotAllocation.getCargoAllocation());
					} else if (slotAllocation.getMarketAllocation() != null) {
						adaptedObjects.add(slotAllocation.getMarketAllocation());
					}
				} else if (a instanceof VesselEventVisit) {
					adaptedObjects.add(a);
				} else if (a instanceof StartEvent) {
					adaptedObjects.add(a);
				} else if (a instanceof Cargo) {
					findSelectionElement((Cargo) a, adaptedObjects);
				} else if (a instanceof Slot) {
					findSelectionElement((Slot) a, adaptedObjects);
				} else if (a instanceof VesselEvent) {
					findSelectionElement((VesselEvent) a, adaptedObjects);
				}
			}
			return adaptedObjects;
		}

		return Collections.emptySet();
	}

	private void findSelectionElement(final EObject a, final Collection<Object> adaptedObject) {
		if (a instanceof Cargo) {
			final Cargo cargo = (Cargo) a;
			final EObject eContainer = cargo.eContainer();
			if (eContainer instanceof CargoModel) {
				final CargoModel cargoModel = (CargoModel) eContainer;
				final EObject eContainer2 = cargoModel.eContainer();
				if (eContainer2 instanceof LNGPortfolioModel) {
					final LNGPortfolioModel lngPortfolioModel = (LNGPortfolioModel) eContainer2;
					final ScheduleModel scheduleModel = lngPortfolioModel.getScheduleModel();
					if (scheduleModel != null) {
						final Schedule schedule = scheduleModel.getSchedule();
						if (scheduleModel.getSchedule() != null) {
							for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
								if (cargo.equals(cargoAllocation.getInputCargo())) {
									adaptedObject.add(cargoAllocation);
									return;
								}
							}
						}
					}
				}
			}

		} else if (a instanceof Slot) {
			final Slot slot = (Slot) a;
			final EObject eContainer = slot.eContainer();
			if (eContainer instanceof CargoModel) {
				final CargoModel cargoModel = (CargoModel) eContainer;
				final EObject eContainer2 = cargoModel.eContainer();
				if (eContainer2 instanceof LNGPortfolioModel) {
					final LNGPortfolioModel lngPortfolioModel = (LNGPortfolioModel) eContainer2;
					final ScheduleModel scheduleModel = lngPortfolioModel.getScheduleModel();
					if (scheduleModel != null) {
						final Schedule schedule = scheduleModel.getSchedule();
						if (scheduleModel.getSchedule() != null) {
							if (slot.getCargo() != null) {
								final Cargo cargo = slot.getCargo();
								for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
									if (cargo.equals(cargoAllocation.getInputCargo())) {
										adaptedObject.add(cargoAllocation);
										return;
									}
								}
							} else {
								for (final MarketAllocation marketAllocation : schedule.getMarketAllocations()) {
									if (slot.equals(marketAllocation.getSlot())) {
										adaptedObject.add(marketAllocation);
										return;
									}
								}
							}
						}
					}
				}
			}
		} else if (a instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) a;
			final EObject eContainer = vesselEvent.eContainer();
			if (eContainer instanceof ScenarioFleetModel) {
				final ScenarioFleetModel scenarioFleetModel = (ScenarioFleetModel) eContainer;
				final EObject eContainer2 = scenarioFleetModel.eContainer();
				if (eContainer2 instanceof LNGPortfolioModel) {
					final LNGPortfolioModel lngPortfolioModel = (LNGPortfolioModel) eContainer2;
					final ScheduleModel scheduleModel = lngPortfolioModel.getScheduleModel();
					if (scheduleModel != null) {
						final Schedule schedule = scheduleModel.getSchedule();
						if (scheduleModel.getSchedule() != null) {

							for (final Sequence sequence : schedule.getSequences()) {
								for (final Event event : sequence.getEvents()) {
									if (event instanceof VesselEventVisit) {
										final VesselEventVisit vesselEventVisit = (VesselEventVisit) event;
										if (vesselEvent.equals(vesselEventVisit.getVesselEvent())) {
											adaptedObject.add(vesselEventVisit);
											return;
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
