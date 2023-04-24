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
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterLengthEvent;
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
		if (selection instanceof final IStructuredSelection iss) {

			final Iterator<?> itr = iss.iterator();
			final Set<Object> adaptedObjects = new HashSet<>();
			while (itr.hasNext()) {
				final Object adaptedObject = itr.next();

				// map to events
				if (adaptedObject instanceof CargoAllocation || adaptedObject instanceof OpenSlotAllocation || adaptedObject instanceof VesselEventVisit //
						|| adaptedObject instanceof StartEvent || adaptedObject instanceof EndEvent || adaptedObject instanceof GeneratedCharterOut //
						|| adaptedObject instanceof GeneratedCharterLengthEvent) {
					adaptedObjects.add(adaptedObject);
				} else if (adaptedObject instanceof final SlotAllocation slotAllocation) {
					adaptedObjects.add(slotAllocation.getCargoAllocation());
				} else if (adaptedObject instanceof SlotVisit slotVisit) {
					final SlotAllocation slotAllocation = slotVisit.getSlotAllocation();
					if (slotAllocation.getCargoAllocation() != null) {
						adaptedObjects.add(slotAllocation.getCargoAllocation());
					} else if (slotAllocation.getMarketAllocation() != null) {
						adaptedObjects.add(slotAllocation.getMarketAllocation());
					}
				} else if (adaptedObject instanceof Cargo || adaptedObject instanceof Slot //
						|| adaptedObject instanceof VesselEvent || adaptedObject instanceof VesselCharter) {
					findSelectionElement((EObject) adaptedObject, adaptedObjects);
				} else if (adaptedObject instanceof GroupedCharterLengthEvent groupedCharterLengthEvent) {
					adaptedObjects.addAll(groupedCharterLengthEvent.getEvents());
				} else if (adaptedObject instanceof GroupedCharterOutEvent groupedCharterOutEvent) {
					adaptedObjects.addAll(groupedCharterOutEvent.getEvents());
				}
			}
			return adaptedObjects;
		}

		return Collections.emptySet();
	}

	private void findSelectionElement(final EObject eObj, final Collection<Object> adaptedObject) {
		if (eObj instanceof final Cargo cargo) {
			final Schedule schedule = getScheduleFromEContainer(cargo.eContainer());
			if (schedule != null) {
				for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
					if (ScheduleModelUtils.matchingSlots(cargo, cargoAllocation)) {
						adaptedObject.add(cargoAllocation);
						return;
					}
				}
			}

		} else if (eObj instanceof final Slot slot) {
			final Schedule schedule = getScheduleFromEContainer(slot.eContainer());
			if (schedule != null) {
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
		} else if (eObj instanceof final VesselEvent vesselEvent) {
			final Schedule schedule = getScheduleFromEContainer(vesselEvent.eContainer());
			if (schedule != null) {
				for (final Sequence sequence : schedule.getSequences()) {
					for (final Event event : sequence.getEvents()) {
						if (event instanceof final VesselEventVisit vesselEventVisit && vesselEvent.equals(vesselEventVisit.getVesselEvent())) {
							adaptedObject.add(vesselEventVisit);
							return;
						}
					}
				}
			}
		} else if (eObj instanceof final VesselCharter vesselCharter) {
			final Schedule schedule = getScheduleFromEContainer(vesselCharter.eContainer());
			if (schedule != null) {
				for (final Sequence seq : schedule.getSequences()) {
					if (!seq.getEvents().isEmpty() && seq.getVesselCharter() == vesselCharter) {
						if (seq.getEvents().get(0) instanceof final StartEvent se){
							adaptedObject.add(se);
						}
						if (seq.getEvents().get(seq.getEvents().size() - 1) instanceof final EndEvent ee) {
							adaptedObject.add(ee);
						}
					}
				}
			}
		}
	}
	
	private Schedule getScheduleFromEContainer(final EObject eContainer) {
		if (eContainer instanceof final CargoModel cargoModel) {
			final LNGScenarioModel scenarioModel = ScenarioModelUtil.findScenarioModel(cargoModel);
			if (scenarioModel != null) {
				final ScheduleModel scheduleModel = scenarioModel.getScheduleModel();
				if (scheduleModel != null) {
					return scheduleModel.getSchedule();
				}
			}
		}
		return null;
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
