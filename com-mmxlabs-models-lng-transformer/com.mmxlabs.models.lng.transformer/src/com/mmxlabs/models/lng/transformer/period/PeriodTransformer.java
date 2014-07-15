package com.mmxlabs.models.lng.transformer.period;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.inject.Inject;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.editor.utils.CollectedAssignment;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.parameters.OptimisationRange;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker.InclusionType;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker.PeriodRecord;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker.Position;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;

/***
 * TODO
 * 
 * Break up for unit tests!
 * 
 * Update for extension points
 * 
 * Update for multiple availabilities
 * 
 * Update for end conditions
 * 
 * 
 * @author Simon Goodall
 * 
 */
public class PeriodTransformer {

	@Inject
	private InclusionChecker inclusionChecker;

	public LNGScenarioModel transform(final LNGScenarioModel wholeScenario, final OptimiserSettings optimiserSettings, final IScenarioEntityMapping mapping) {
		final PeriodRecord periodRecord = createPeriodRecord(optimiserSettings);
		return transform(wholeScenario, optimiserSettings, periodRecord, mapping);
	}

	public PeriodRecord createPeriodRecord(final OptimiserSettings optimiserSettings) {

		final PeriodRecord periodRecord = new PeriodRecord();

		final OptimisationRange range = optimiserSettings.getRange();
		if (range == null) {
			return periodRecord;
		}

		final Date startDate = range.getOptimiseAfter();
		final Date endDate = range.getOptimiseBefore();

		final int boundaryFlexInMonths = 1;

		periodRecord.lowerBoundary = startDate;
		periodRecord.upperBoundary = endDate;
		// Get dates with flex
		if (startDate != null) {
			final Calendar cal = Calendar.getInstance();
			cal.setTimeZone(TimeZone.getTimeZone("UTC"));
			cal.setTime(startDate);
			cal.add(Calendar.MONTH, -boundaryFlexInMonths);
			periodRecord.lowerCutoff = cal.getTime();
		}

		if (endDate != null) {
			final Calendar cal = Calendar.getInstance();
			cal.setTimeZone(TimeZone.getTimeZone("UTC"));
			cal.setTime(endDate);
			cal.add(Calendar.MONTH, boundaryFlexInMonths);
			periodRecord.upperCutoff = cal.getTime();
		}

		return periodRecord;
	}

	public LNGScenarioModel transform(final LNGScenarioModel wholeScenario, final OptimiserSettings optimiserSettings, final PeriodRecord periodRecord, final IScenarioEntityMapping mapping) {

		// assert - passed validation

		// Take a copy to manipulate.
		final LNGScenarioModel output = copyScenario(wholeScenario, mapping);

		// Evaluate copy!
		final EditingDomain internalDomain = evaluateScenario(optimiserSettings, output);

		final CargoModel cargoModel = output.getPortfolioModel().getCargoModel();
		final FleetModel fleetModel = output.getFleetModel();

		// Generate the schedule map - maps cargoes and events to schedule information for date, port and heel data extraction
		final Map<AssignableElement, PortVisit> startConditionMap = new HashMap<>();
		final Map<AssignableElement, PortVisit> endConditionMap = new HashMap<>();
		generateStartAndEndConditionsMap(output, startConditionMap, endConditionMap);

		// Update vessel availabilities
		updateVesselAvailabilities(periodRecord, cargoModel, fleetModel, startConditionMap, endConditionMap);

		// Filter out slots and cargoes
		filterSlotsAndCargoes(internalDomain, periodRecord, cargoModel, mapping);

		// Filter out vessel events
		filterVesselEvents(internalDomain, periodRecord, cargoModel, mapping);

		// Filter out vessels
		filterVesselAvailabilities(internalDomain, periodRecord, cargoModel, mapping);

		return output;
	}

	public void filterVesselAvailabilities(final EditingDomain internalDomain, final PeriodRecord periodRecord, final CargoModel cargoModel, final IScenarioEntityMapping mapping) {
		final Set<VesselAvailability> vesselsToRemove = new HashSet<>();

		for (final VesselAvailability vesselAvailability : cargoModel.getVesselAvailabilities()) {
			if (inclusionChecker.getObjectInclusionType(vesselAvailability, periodRecord).getFirst() == InclusionType.Out) {
				vesselsToRemove.add(vesselAvailability);
				mapping.registerRemovedOriginal(mapping.getOriginalFromCopy(vesselAvailability));

			}
		}
		internalDomain.getCommandStack().execute(DeleteCommand.create(internalDomain, vesselsToRemove));

	}

	public void filterVesselEvents(final EditingDomain internalDomain, final PeriodRecord periodRecord, final CargoModel cargoModel, final IScenarioEntityMapping mapping) {
		final Set<VesselEvent> eventsToRemove = new HashSet<>();

		for (final VesselEvent event : cargoModel.getVesselEvents()) {
			if (inclusionChecker.getObjectInclusionType(event, periodRecord).getFirst() == InclusionType.Out) {
				eventsToRemove.add(event);
				mapping.registerRemovedOriginal(mapping.getOriginalFromCopy(event));
			}
			if (event instanceof CharterOutEvent) {
				// TODO: If in boundary, limit available vessels to assigned vessel
			}
		}
		internalDomain.getCommandStack().execute(DeleteCommand.create(internalDomain, eventsToRemove));

	}

	public void filterSlotsAndCargoes(final EditingDomain internalDomain, final PeriodRecord periodRecord, final CargoModel cargoModel, final IScenarioEntityMapping mapping) {
		final Set<Slot> seenSlots = new HashSet<Slot>();

		final Set<Cargo> cargoesToRemove = new HashSet<Cargo>();
		final Set<Slot> slotsToRemove = new HashSet<>();

		for (final Cargo cargo : cargoModel.getCargoes()) {

			final InclusionType inclusionType = inclusionChecker.getObjectInclusionType(cargo, periodRecord).getFirst();

			if (inclusionType == InclusionType.Out) {
				cargoesToRemove.add(cargo);
				slotsToRemove.addAll(cargo.getSlots());
			} else if (inclusionType == InclusionType.Boundary) {
				cargo.setAllowRewiring(false);
				if (cargo.getCargoType() == CargoType.FLEET) {
					cargo.setLocked(true);
				}
			}

			// These slots have been considered
			seenSlots.addAll(cargo.getSlots());
		}

		for (final LoadSlot slot : cargoModel.getLoadSlots()) {
			if (seenSlots.contains(slot)) {
				continue;
			}
			if (inclusionChecker.getObjectInclusionType(slot, periodRecord).getFirst() == InclusionType.Out) {
				slotsToRemove.add(slot);
			}
		}

		for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
			if (seenSlots.contains(slot)) {
				continue;
			}
			if (inclusionChecker.getObjectInclusionType(slot, periodRecord).getFirst() == InclusionType.Out) {
				slotsToRemove.add(slot);
			}
		}

		// Delete slots and cargoes outside of range.
		for (final Slot slot : slotsToRemove) {
			mapping.registerRemovedOriginal(mapping.getOriginalFromCopy(slot));
			if (slot instanceof LoadSlot) {
				// cargoModel.getLoadSlots().remove(slot);
			} else if (slot instanceof DischargeSlot) {
				// cargoModel.getDischargeSlots().remove(slot);
			} else {
				throw new IllegalStateException("Unknown slot type");
			}
			internalDomain.getCommandStack().execute(DeleteCommand.create(internalDomain, slot));
		}
		for (final Cargo cargo : cargoesToRemove) {
			// cargoModel.getCargoes().remove(cargo);
			mapping.registerRemovedOriginal(mapping.getOriginalFromCopy(cargo));
			internalDomain.getCommandStack().execute(DeleteCommand.create(internalDomain, cargo));
		}
	}

	public void updateVesselAvailabilities(final PeriodRecord periodRecord, final CargoModel cargoModel, final FleetModel fleetModel, final Map<AssignableElement, PortVisit> startConditionMap,
			final Map<AssignableElement, PortVisit> endConditionMap) {

		final List<CollectedAssignment> collectedAssignments = AssignmentEditorHelper.collectAssignments(cargoModel, fleetModel);

		updateVesselAvailabilities(periodRecord, collectedAssignments, startConditionMap, endConditionMap);
	}

	public void updateVesselAvailabilities(final PeriodRecord periodRecord, final List<CollectedAssignment> collectedAssignments, final Map<AssignableElement, PortVisit> startConditionMap,
			final Map<AssignableElement, PortVisit> endConditionMap) {

		// Here we loop through all the collected assignments, trimming the vessel availability to anything outside of the date range.
		// This can handle out-of-order assignments by checking to see whether or not a cargo has already been trimmed out of the date range before updating
		for (final CollectedAssignment collectedAssignment : collectedAssignments) {
			if (collectedAssignment.isSetSpotIndex()) {
				continue;
			}

			final List<AssignableElement> assignedObjects = collectedAssignment.getAssignedObjects();
			final VesselAvailability vesselAvailability = collectedAssignment.getVesselAvailability();
			if (vesselAvailability != null) {
				for (final AssignableElement assignedObject : assignedObjects) {
					final Pair<InclusionType, Position> result = inclusionChecker.getObjectInclusionType(assignedObject, periodRecord);
					if (result.getFirst() == InclusionType.Out) {
						final Position position = result.getSecond();

						if (position == Position.Before) {
							// Update availability start heel
							updateStartConditions(vesselAvailability, assignedObject, startConditionMap);
						} else if (position == Position.After) {
							// Update availability end heel
							updateEndConditions(vesselAvailability, assignedObject, endConditionMap);
						}

					}
				}
			}
		}
	}

	public void generateStartAndEndConditionsMap(final LNGScenarioModel output, final Map<AssignableElement, PortVisit> startConditionMap, final Map<AssignableElement, PortVisit> endConditionMap) {
		final ScheduleModel scheduleModel = output.getPortfolioModel().getScheduleModel();
		final Schedule schedule = scheduleModel.getSchedule();
		for (final Sequence sequence : schedule.getSequences()) {
			for (final Event event : sequence.getEvents()) {
				final Event segmentStart = ScheduleModelUtils.getSegmentStart(event);
				if (event == segmentStart) {
					if (event instanceof SlotVisit) {
						final SlotVisit slotVisit = (SlotVisit) event;
						// Get the cargo for the set of visits., set start/end heel links
						final Cargo cargo = slotVisit.getSlotAllocation().getCargoAllocation().getInputCargo();
						endConditionMap.put(cargo, slotVisit);

						Event segmentEnd = ScheduleModelUtils.getSegmentEnd(event);
						int nextCounter = 0;
						while (segmentEnd != null && !(segmentEnd instanceof PortVisit)) {
							segmentEnd = segmentEnd.getNextEvent();
							++nextCounter;
						}

						// Only expect getNextEvent to be called once
						assert nextCounter < 2;
						startConditionMap.put(cargo, (PortVisit) segmentEnd);

					} else if (event instanceof VesselEventVisit) {
						final VesselEventVisit vesselEventVisit = (VesselEventVisit) event;
						// Find sequence start and sequence end and map to event.
						final VesselEvent vesselEvent = vesselEventVisit.getVesselEvent();
						endConditionMap.put(vesselEvent, vesselEventVisit);

						Event segmentEnd = ScheduleModelUtils.getSegmentEnd(event);
						int nextCounter = 0;
						while (segmentEnd != null && !(segmentEnd instanceof PortVisit)) {
							segmentEnd = segmentEnd.getNextEvent();
							++nextCounter;
						}
						// Only expect getNextEvent to be called once
						assert nextCounter < 2;
						startConditionMap.put(vesselEvent, (PortVisit) segmentEnd);

					}
				}
			}
		}
	}

	public LNGScenarioModel copyScenario(final LNGScenarioModel wholeScenario, final IScenarioEntityMapping mapping) {
		final Copier copier = new Copier();
		final LNGScenarioModel output = (LNGScenarioModel) copier.copy(wholeScenario);
		copier.copyReferences();

		mapping.createMappings(copier);
		return output;
	}

	public EditingDomain evaluateScenario(final OptimiserSettings optimiserSettings, final LNGScenarioModel output) {

		final LNGTransformer transformer = new LNGTransformer(output, optimiserSettings);

		final ModelEntityMap modelEntityMap = transformer.getModelEntityMap();
		final IAnnotatedSolution startSolution = LNGSchedulerJobUtils.evaluateCurrentState(transformer);

		// Construct internal command stack to generate correct output schedule
		final BasicCommandStack commandStack = new BasicCommandStack();
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		final EditingDomain ed = new AdapterFactoryEditingDomain(adapterFactory, commandStack);

		// Delete commands need a resource set on the editing domain
		final Resource r = new XMIResourceImpl();
		r.getContents().add(output);
		ed.getResourceSet().getResources().add(r);

		LNGSchedulerJobUtils.exportSolution(transformer.getInjector(), output, transformer.getOptimiserSettings(), ed, modelEntityMap, startSolution, 0);

		return ed;
	}

	/**
	 * Given a vessel availability, update the starting conditions around the input AssignableElement which is assumed to be outside of the current optimisation period scope. We want to set the
	 * starting point to be equal to the end conditions of the AssignableElement sequence.
	 * 
	 * @param vesselAvailability
	 * @param assignedObject
	 * @param startConditionMap
	 */
	public void updateStartConditions(final VesselAvailability vesselAvailability, final AssignableElement assignedObject, final Map<AssignableElement, PortVisit> startConditionMap) {
		final PortVisit portVisit = startConditionMap.get(assignedObject);
		if (inclusionChecker.getObjectInVesselAvailabilityRange(portVisit, vesselAvailability) == InclusionType.In) {
			vesselAvailability.getStartAt().clear();
			vesselAvailability.getStartAt().add(portVisit.getPort());

			vesselAvailability.setStartAfter(portVisit.getStart());
			vesselAvailability.setStartBy(portVisit.getStart());

			// TODO: Set CV, price
			final int heelAtStart = portVisit.getHeelAtStart();
			if (heelAtStart == 0) {
				vesselAvailability.getStartHeel().unsetVolumeAvailable();
				vesselAvailability.getStartHeel().setCvValue(0.0);
				vesselAvailability.getStartHeel().setPricePerMMBTU(0.0);
			} else {
				vesselAvailability.getStartHeel().setVolumeAvailable(heelAtStart);
				vesselAvailability.getStartHeel().setCvValue(22.8);
				vesselAvailability.getStartHeel().setPricePerMMBTU(0.01);
			}
		}
	}

	public void updateEndConditions(final VesselAvailability vesselAvailability, final AssignableElement assignedObject, final Map<AssignableElement, PortVisit> endConditionMap) {
		final PortVisit portVisit = endConditionMap.get(assignedObject);
		if (inclusionChecker.getObjectInVesselAvailabilityRange(portVisit, vesselAvailability) == InclusionType.In) {
			vesselAvailability.getEndAt().clear();
			vesselAvailability.getEndAt().add(portVisit.getPort());

			vesselAvailability.setEndAfter(portVisit.getStart());
			vesselAvailability.setEndBy(portVisit.getStart());

			// TODO: Set must arrive cold with target heel volume
			// vesselAvailability.getEndHeel().setVolumeAvailable(portVisit.getHeelAtStart());
		}
	}

	public InclusionChecker getInclusionChecker() {
		return inclusionChecker;
	}

	public void setInclusionChecker(final InclusionChecker inclusionChecker) {
		this.inclusionChecker = inclusionChecker;
	}
}
