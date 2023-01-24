/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.NonShippedCargoSpecification;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SlotSpecification;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.VesselEventSpecification;
import com.mmxlabs.models.lng.cargo.VesselScheduleSpecification;
import com.mmxlabs.models.lng.cargo.editor.utils.IAssignableElementDateProviderFactory;
import com.mmxlabs.models.lng.cargo.util.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.util.CollectedAssignment;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;

public class DefaultScheduleSpecificationBuilder {

	public ScheduleSpecification buildScheduleSpecification(final IScenarioDataProvider scenarioDataProvider,
			@Nullable final IAssignableElementDateProviderFactory assignableElementDateProviderFactory) {

		ModelDistanceProvider modelDistanceProvider = scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);

		final List<CollectedAssignment> assignments;
		CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioDataProvider);
		PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);
		SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(scenarioDataProvider);
		if (assignableElementDateProviderFactory != null) {
			assignments = AssignmentEditorHelper.collectAssignments(cargoModel, portModel, spotMarketsModel, modelDistanceProvider,
					assignableElementDateProviderFactory.create(scenarioDataProvider.getTypedScenario(LNGScenarioModel.class)));
		} else {
			assignments = AssignmentEditorHelper.collectAssignments(cargoModel, portModel, spotMarketsModel, modelDistanceProvider);
		}

		final Set<Slot> seenSlots = new HashSet<>();
		final Set<Cargo> seenCargoes = new HashSet<>();
		final Set<VesselEvent> seenEvents = new HashSet<>();

		final ScheduleSpecification scheduleSpecification = CargoFactory.eINSTANCE.createScheduleSpecification();

		for (final CollectedAssignment seq : assignments) {
			final VesselScheduleSpecification vesselScheduleSpecification = CargoFactory.eINSTANCE.createVesselScheduleSpecification();
			final IVesselCharter vesselCharter = null;
			if (seq.getVesselCharter() != null) {
				final VesselCharter eVesselCharter = seq.getVesselCharter();
				assert eVesselCharter != null;
				vesselScheduleSpecification.setVesselAllocation(eVesselCharter);
			} else if (seq.getCharterInMarket() != null) {
				vesselScheduleSpecification.setVesselAllocation(seq.getCharterInMarket());
				vesselScheduleSpecification.setSpotIndex(seq.getSpotIndex());
			} else {
				assert false;
			}

			scheduleSpecification.getVesselScheduleSpecifications().add(vesselScheduleSpecification);

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
		}

		for (final Cargo cargo : cargoModel.getCargoes()) {
			if (seenCargoes.contains(cargo)) {
				continue;
			}
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
			}
			seenCargoes.add(cargo);
		}

		for (final LoadSlot loadSlot : cargoModel.getLoadSlots()) {
			if (seenSlots.contains(loadSlot)) {
				continue;
			}
			final SlotSpecification eventSpecification = CargoFactory.eINSTANCE.createSlotSpecification();
			eventSpecification.setSlot(loadSlot);
			scheduleSpecification.getOpenEvents().add(eventSpecification);
			seenSlots.add(loadSlot);
		}
		for (final DischargeSlot dischargeSlot : cargoModel.getDischargeSlots()) {
			if (seenSlots.contains(dischargeSlot)) {
				continue;
			}
			final SlotSpecification eventSpecification = CargoFactory.eINSTANCE.createSlotSpecification();
			eventSpecification.setSlot(dischargeSlot);
			scheduleSpecification.getOpenEvents().add(eventSpecification);
			seenSlots.add(dischargeSlot);
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
