/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;
import com.mmxlabs.models.ui.properties.views.DetailPropertiesView;

public class PNLDetailsReport extends DetailPropertiesView {

	public PNLDetailsReport() {
		super("pnl", "com.mmxlabs.shiplingo.platform.reports.views.PNLDetailsReport", false);
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		// Expand four levels by default
		viewer.setAutoExpandLevel(4);
	}

	@Override
	protected Collection<?> adaptSelection(final ISelection selection) {
		if (selection instanceof IStructuredSelection) {

			final Iterator<?> itr = ((IStructuredSelection) selection).iterator();
			final Set<Object> adaptedObjects = new HashSet<>();
			while (itr.hasNext()) {
				final Object a = itr.next();

				// map to events
				if (a instanceof CargoAllocation) {
					adaptedObjects.add(a);
				} else if (a instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) a;
					adaptedObjects.add(slotAllocation.getCargoAllocation());
				} else if (a instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) a;
					final SlotAllocation slotAllocation = slotVisit.getSlotAllocation();
					if (slotAllocation.getCargoAllocation() != null) {
						adaptedObjects.add(slotAllocation.getCargoAllocation());
					} else if (slotAllocation.getMarketAllocation() != null) {
						adaptedObjects.add(slotAllocation.getMarketAllocation());
					}
				} else if (a instanceof OpenSlotAllocation) {
					adaptedObjects.add(a);
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
								for (final OpenSlotAllocation openSlotAllocation : schedule.getOpenSlotAllocations()) {
									if (slot.equals(openSlotAllocation.getSlot())) {
										adaptedObject.add(openSlotAllocation);
										return;
									}
								}
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
			if (eContainer instanceof CargoModel) {
				final CargoModel cargoModel = (CargoModel) eContainer;
				final EObject eContainer2 = cargoModel.eContainer();
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

	private final Set<ScheduleModel> hookedSchedules = new HashSet<>();

	private final MMXContentAdapter contentAdapter = new MMXContentAdapter() {

		@Override
		public void reallyNotifyChanged(final Notification notification) {
			if (notification.getEventType() != Notification.REMOVING_ADAPTER) {
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						PNLDetailsReport.this.refresh();
					}
				});
			}
		}

		@Override
		protected void missedNotifications(final List<Notification> missed) {
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					PNLDetailsReport.this.refresh();
				}
			});

		}

	};

	@Override
	protected void hookAdapters(final Collection<?> adaptSelection) {
		final Set<ScheduleModel> schedules = new HashSet<>();
		for (final Object object : adaptSelection) {
			if (object instanceof EObject) {
				EObject eObject = (EObject) object;
				while (eObject != null && !(eObject instanceof MMXRootObject)) {
					eObject = eObject.eContainer();
				}

				if (eObject instanceof LNGScenarioModel) {
					final LNGScenarioModel scenarioModel = (LNGScenarioModel) eObject;
					final LNGPortfolioModel portfolioModel = scenarioModel.getPortfolioModel();
					if (portfolioModel != null) {
						final ScheduleModel scheduleModel = portfolioModel.getScheduleModel();
						if (scheduleModel != null) {
							schedules.add(scheduleModel);
						}
					}

				}
			}
		}
		for (final ScheduleModel scheduleModel : schedules) {
			scheduleModel.eAdapters().add(contentAdapter);
			hookedSchedules.add(scheduleModel);
		}
	}

	@Override
	protected void removeAdapters() {
		for (final ScheduleModel scheduleModel : hookedSchedules) {
			scheduleModel.eAdapters().remove(contentAdapter);
		}
	}
}
