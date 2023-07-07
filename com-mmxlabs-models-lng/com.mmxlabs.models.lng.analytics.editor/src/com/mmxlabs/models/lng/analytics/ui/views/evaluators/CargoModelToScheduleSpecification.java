/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.NonShippedCargoSpecification;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.cargo.SlotSpecification;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.VesselEventSpecification;
import com.mmxlabs.models.lng.cargo.VesselScheduleSpecification;
import com.mmxlabs.models.lng.cargo.editor.utils.IAssignableElementDateProviderFactory;
import com.mmxlabs.models.lng.cargo.util.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.util.CollectedAssignment;
import com.mmxlabs.models.lng.cargo.util.IExtraDataProvider;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class CargoModelToScheduleSpecification {

	@Inject(optional = true)
	@Nullable
	private IAssignableElementDateProviderFactory assignableElementComparator;

	@Inject(optional = true)
	@Nullable
	private IExtraDataProvider extraDataProvider;

	public ScheduleSpecification generateScheduleSpecifications(IScenarioDataProvider scenarioDataProvider, final CargoModel cargoModel) {

		LNGScenarioModel scenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
		final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(scenarioModel);
		// final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);

		final ScheduleSpecification scheduleSpecification = CargoFactory.eINSTANCE.createScheduleSpecification();

		// Process initial vessel assignments list
		ModelDistanceProvider modelDistanceProvider = ScenarioModelUtil.getModelDistanceProvider(scenarioDataProvider);
		final List<CollectedAssignment> assignments;
		if (assignableElementComparator != null) {
			assignments = AssignmentEditorHelper.collectAssignments(cargoModel, ScenarioModelUtil.getPortModel(scenarioModel), spotMarketsModel, modelDistanceProvider,
					assignableElementComparator.create(scenarioModel), extraDataProvider);
		} else {
			assignments = AssignmentEditorHelper.collectAssignments(cargoModel, ScenarioModelUtil.getPortModel(scenarioModel), spotMarketsModel, modelDistanceProvider, null, extraDataProvider);
		}

		// Sort assignments based on vessel order in the data model.
		// This will be combined later in the ScheduleSpecificationTransformer which further sorts by charter type and vessel speed
		List<CollectedAssignment> orderedAssignments = new LinkedList<>();
		for (var vessel : ScenarioModelUtil.getFleetModel(scenarioDataProvider).getVessels()) {
			Iterator<CollectedAssignment> itr = assignments.iterator();
			while (itr.hasNext()) {
				var ca = itr.next();
				if (ca.getVesselCharter() != null && ca.getVesselCharter().getVessel() == vessel) {
					orderedAssignments.add(ca);
					itr.remove();
				} else if (ca.getCharterInMarket() != null && ca.getCharterInMarket().getVessel() == vessel) {
					orderedAssignments.add(ca);
					itr.remove();
				}
			}
		}

		orderedAssignments.addAll(assignments);

		for (final CollectedAssignment sequence : orderedAssignments) {
			final VesselScheduleSpecification vesselSpec = CargoFactory.eINSTANCE.createVesselScheduleSpecification();
			scheduleSpecification.getVesselScheduleSpecifications().add(vesselSpec);

			if (sequence.getVesselCharter() != null) {
				vesselSpec.setVesselAllocation(sequence.getVesselCharter());
			} else if (sequence.getCharterInMarket() != null) {
				vesselSpec.setVesselAllocation(sequence.getCharterInMarket());
				vesselSpec.setSpotIndex(sequence.getSpotIndex());
			}
			for (var assignedObject : sequence.getAssignedObjects()) {
				// Handle non-shipped later on
				if (assignedObject instanceof Cargo c && c.getCargoType() != CargoType.FLEET) {
					continue;
				}
				if (assignedObject instanceof Cargo cargo) {
					for (var slot : cargo.getSortedSlots()) {
						final SlotSpecification slotSpec = CargoFactory.eINSTANCE.createSlotSpecification();
						slotSpec.setSlot(slot);
						vesselSpec.getEvents().add(slotSpec);
					}
				} else if (assignedObject instanceof VesselEvent ve) {
					final VesselEventSpecification eventSpec = CargoFactory.eINSTANCE.createVesselEventSpecification();
					eventSpec.setVesselEvent(ve);
					vesselSpec.getEvents().add(eventSpec);
				} else {
					throw new IllegalStateException();
				}
			}
		}

		// TODO: Do we need to include extra data?

		// Non-shipped cargoes
		for (Cargo cargo : cargoModel.getCargoes()) {
			if (cargo.getCargoType() != CargoType.FLEET) {
				final NonShippedCargoSpecification cargoSpec = CargoFactory.eINSTANCE.createNonShippedCargoSpecification();
				for (final var slot : cargo.getSortedSlots()) {
					final SlotSpecification slotSpec = CargoFactory.eINSTANCE.createSlotSpecification();
					slotSpec.setSlot(slot);
					cargoSpec.getSlotSpecifications().add(slotSpec);
				}
				scheduleSpecification.getNonShippedCargoSpecifications().add(cargoSpec);
			}
		}

		// Add on open slots
		for (var slot : cargoModel.getLoadSlots()) {
			if (slot.getCargo() == null) {
				final SlotSpecification spec = CargoFactory.eINSTANCE.createSlotSpecification();
				spec.setSlot(slot);
				scheduleSpecification.getOpenEvents().add(spec);
			}
		}
		for (var slot : cargoModel.getDischargeSlots()) {
			if (slot.getCargo() == null) {
				final SlotSpecification spec = CargoFactory.eINSTANCE.createSlotSpecification();
				spec.setSlot(slot);
				scheduleSpecification.getOpenEvents().add(spec);
			}
		}
		// Add in unused vessel events.
		for (var ve : cargoModel.getVesselEvents()) {
			if (ve.getVesselAssignmentType() == null) {
				final var spec = CargoFactory.eINSTANCE.createVesselEventSpecification();
				spec.setVesselEvent(ve);
				scheduleSpecification.getOpenEvents().add(spec);
			}
		}

		return scheduleSpecification;
	}
}
