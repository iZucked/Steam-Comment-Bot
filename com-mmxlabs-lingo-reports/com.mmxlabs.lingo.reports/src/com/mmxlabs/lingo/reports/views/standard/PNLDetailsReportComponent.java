/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterLengthEvent;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupedCharterLengthEvent;
import com.mmxlabs.models.lng.schedule.GroupedCharterOutEvent;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.ui.properties.views.DetailPropertiesViewComponent;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.ViewerHelper;

public class PNLDetailsReportComponent extends DetailPropertiesViewComponent {

	@PostConstruct
	public void addHelpContext() {

		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Reports_PNLDetails");
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
				} else if (a instanceof EndEvent) {
					adaptedObjects.add(a);
				} else if (a instanceof Cargo) {
					findSelectionElement((Cargo) a, adaptedObjects);
				} else if (a instanceof Slot) {
					findSelectionElement((Slot) a, adaptedObjects);
				} else if (a instanceof VesselEvent) {
					findSelectionElement((VesselEvent) a, adaptedObjects);
				} else if (a instanceof GeneratedCharterOut) {
					adaptedObjects.add(a);
				} else if (a instanceof CharterLengthEvent) {
					adaptedObjects.add(a);
				} else if (a instanceof GroupedCharterLengthEvent) {
					GroupedCharterLengthEvent groupedCharterLengthEvent = (GroupedCharterLengthEvent) a;
					adaptedObjects.addAll(groupedCharterLengthEvent.getEvents());
				} else if (a instanceof GroupedCharterOutEvent) {
					GroupedCharterOutEvent groupedCharterOutEvent = (GroupedCharterOutEvent) a;
					adaptedObjects.addAll(groupedCharterOutEvent.getEvents());
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
				final LNGScenarioModel scenarioModel = ScenarioModelUtil.findScenarioModel(cargoModel);
				if (scenarioModel != null) {
					final ScheduleModel scheduleModel = scenarioModel.getScheduleModel();
					if (scheduleModel != null) {
						final Schedule schedule = scheduleModel.getSchedule();
						if (scheduleModel.getSchedule() != null) {
							for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
								if (ScheduleModelUtils.matchingSlots(cargo, cargoAllocation)) {
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
				final LNGScenarioModel scenarioModel = ScenarioModelUtil.findScenarioModel(cargoModel);
				if (scenarioModel != null) {
					final ScheduleModel scheduleModel = scenarioModel.getScheduleModel();
					if (scheduleModel != null) {
						final Schedule schedule = scheduleModel.getSchedule();
						if (scheduleModel.getSchedule() != null) {
							if (slot.getCargo() != null) {
								final Cargo cargo = slot.getCargo();
								for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
									if (ScheduleModelUtils.matchingSlots(cargo, cargoAllocation)) {
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
				final LNGScenarioModel scenarioModel = ScenarioModelUtil.findScenarioModel(cargoModel);
				if (scenarioModel != null) {
					final ScheduleModel scheduleModel = scenarioModel.getScheduleModel();
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

	public void rebuild(final ISelectedDataProvider selectedDataProvider, boolean block) {

		if (pinSelection) {
			return;
		}

		final ISelection selection = SelectionHelper.adaptSelection(selectedDataProvider == null ? Collections.emptyList() : selectedDataProvider.getSelectedObjects());
		removeAdapters();

		// This is very slow with many selected items. Run async to avoid blocking other actions.
		ViewerHelper.runIfViewerValid(viewer, block, v -> {
			removeAdapters();
			final Collection<?> adaptSelection = adaptSelection(selection);
			viewer.setInput(adaptSelection);
			hookAdapters(adaptSelection);
		});
	}
}
