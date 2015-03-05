/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.util.TypeLiterals;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.util.CollectedAssignment;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.parameters.OptimisationRange;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotAvailability;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker.InclusionType;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker.PeriodRecord;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker.Position;
import com.mmxlabs.models.lng.transformer.period.extensions.IPeriodTransformerExtension;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
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

	@Inject(optional = true)
	private Iterable<IPeriodTransformerExtension> extensions;
	private Module testingModule;
	
	public PeriodTransformer(Module testingModule) {
		this.testingModule = testingModule;
		injectExtensions();
	}

	public PeriodTransformer() {
		injectExtensions();
	}
	
	private void injectExtensions() {

		Injector injector = null;
		if (Platform.isRunning()) {
			final AbstractModule m = new AbstractModule() {

				@Override
				protected void configure() {
					install(Peaberry.osgiModule(FrameworkUtil.getBundle(PeriodTransformer.class).getBundleContext()));
					bind(TypeLiterals.iterable(IPeriodTransformerExtension.class)).toProvider(Peaberry.service(IPeriodTransformerExtension.class).multiple());
				}
			};
			injector = Guice.createInjector(m);
		}
		if (injector != null) {
			injector.injectMembers(this);
		}
	}

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
		final SpotMarketsModel spotMarketsModel = output.getSpotMarketsModel();

		// Generate the schedule map - maps cargoes and events to schedule information for date, port and heel data extraction
		final Map<AssignableElement, PortVisit> startConditionMap = new HashMap<>();
		final Map<AssignableElement, PortVisit> endConditionMap = new HashMap<>();
		generateStartAndEndConditionsMap(output, startConditionMap, endConditionMap);

		final Map<Slot, SlotAllocation> slotAllocationMap = new HashMap<>();
		generateSlotAllocationMap(output, slotAllocationMap);

		// Update vessel availabilities
		updateVesselAvailabilities(periodRecord, cargoModel, spotMarketsModel, startConditionMap, endConditionMap);

		// List of new vessel availabilities for cargoes outside normal range
		final List<VesselAvailability> newVesselAvailabilities = new LinkedList<>();
		final Set<Slot> seenSlots = new HashSet<>();
		final Set<Slot> slotsToRemove = new HashSet<>();
		final Set<Cargo> cargoesToRemove = new HashSet<>();
		// Filter out slots and cargoes, create new availabilities for special cases.
		findSlotsAndCargoesToRemove(internalDomain, periodRecord, cargoModel, seenSlots, slotsToRemove, cargoesToRemove, slotAllocationMap);
		checkIfRemovedSlotsAreStillNeeded(seenSlots, slotsToRemove, cargoesToRemove, newVesselAvailabilities, startConditionMap, endConditionMap, slotAllocationMap);
		removeExcludedSlotsAndCargoes(internalDomain, mapping, slotsToRemove, cargoesToRemove);

		// TEMP HACK UNTIL MULTIPLE AVAILABILITES PROPERLY IN PLACE AND filterSlotsAndCargoes can properly handle this.
		for (final VesselAvailability newVA : newVesselAvailabilities) {
			for (final VesselAvailability vesselAvailability : wholeScenario.getPortfolioModel().getCargoModel().getVesselAvailabilities()) {
				if (newVA.getVessel() == mapping.getCopyFromOriginal(vesselAvailability.getVessel())) {
					newVA.setEntity(mapping.getCopyFromOriginal(vesselAvailability.getEntity()));
					if (vesselAvailability.isSetTimeCharterRate()) {
						newVA.setTimeCharterRate(vesselAvailability.getTimeCharterRate());
					}
				}
			}
		}

		// Filter out vessel events
		filterVesselEvents(internalDomain, periodRecord, cargoModel, mapping);

		// Filter out vessels
		filterVesselAvailabilities(internalDomain, periodRecord, cargoModel, mapping);

		output.getPortfolioModel().getCargoModel().getVesselAvailabilities().addAll(newVesselAvailabilities);

		trimSpotMarketCurves(internalDomain, periodRecord, output.getSpotMarketsModel(), mapping);

		// Remove schedule model
		output.getPortfolioModel().getScheduleModel().setSchedule(null);

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
				// If in boundary, limit available vessels to assigned vessel
				event.getAllowedVessels().clear();
				VesselAvailability vesselAvailability = ((VesselAvailability) event.getVesselAssignmentType());
				if (vesselAvailability != null) {
					event.getAllowedVessels().add(vesselAvailability.getVessel());
				}
			}
		}
		internalDomain.getCommandStack().execute(DeleteCommand.create(internalDomain, eventsToRemove));

	}

	public void findSlotsAndCargoesToRemove(final EditingDomain internalDomain, final PeriodRecord periodRecord, final CargoModel cargoModel, final Set<Slot> seenSlots,
			final Collection<Slot> slotsToRemove, final Collection<Cargo> cargoesToRemove, final Map<Slot, SlotAllocation> slotAllocationMap) {

		for (final Cargo cargo : cargoModel.getCargoes()) {

			final Pair<InclusionType, Position> inclusionResult = inclusionChecker.getObjectInclusionType(cargo, periodRecord);
			final InclusionType inclusionType = inclusionResult.getFirst();
			final Position pos = inclusionResult.getSecond();

			if (inclusionType == InclusionType.Out) {
				cargoesToRemove.add(cargo);
				slotsToRemove.addAll(cargo.getSlots());
			} else if (inclusionType == InclusionType.Boundary) {
				if (pos == Position.Both) {
					// lock whole cargo
					lockDownCargoDates(slotAllocationMap, cargo);
				} else {
					// lock only one slot
					Pair<Slot, Slot> slots = inclusionChecker.getFirstAndLastSlots(cargo);
					if (pos == Position.After) {
						if (inclusionChecker.getObjectInclusionType(slots.getFirst(), periodRecord).getFirst() == InclusionType.In) {
							lockDownSlotDates(slotAllocationMap, slots.getSecond());
						} else {
							lockDownCargoDates(slotAllocationMap, cargo);
						}
					} else {
						if (inclusionChecker.getObjectInclusionType(slots.getSecond(), periodRecord).getFirst() == InclusionType.In) {
							lockDownSlotDates(slotAllocationMap, slots.getFirst());
						} else {
							lockDownCargoDates(slotAllocationMap, cargo);
						}
					}
				}
			}

			// These slots have been considered
			seenSlots.addAll(cargo.getSlots());
		}

		for (final LoadSlot slot : cargoModel.getLoadSlots()) {
			if (seenSlots.contains(slot)) {
				continue;
			}
			seenSlots.add(slot);
			if (inclusionChecker.getObjectInclusionType(slot, periodRecord).getFirst() == InclusionType.Out) {
				slotsToRemove.add(slot);
			}
		}

		for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
			if (seenSlots.contains(slot)) {
				continue;
			}
			seenSlots.add(slot);
			if (inclusionChecker.getObjectInclusionType(slot, periodRecord).getFirst() == InclusionType.Out) {
				slotsToRemove.add(slot);
			}
		}
	}

	/**
	 * Scan through the slots processed and if still in use check for removed slots and cargoes which are required to e.g. complete P&L evaluation and bring them back in on dedicated round trip cargo
	 * models.
	 * 
	 * @param seenSlots
	 * @param slotsToRemove
	 * @param cargoesToRemove
	 * @param newVesselAvailabilities
	 * @param startConditionMap
	 * @param endConditionMap
	 * @param slotAllocationMap
	 */
	public void checkIfRemovedSlotsAreStillNeeded(final Set<Slot> seenSlots, final Collection<Slot> slotsToRemove, final Collection<Cargo> cargoesToRemove,
			final List<VesselAvailability> newVesselAvailabilities, final Map<AssignableElement, PortVisit> startConditionMap, final Map<AssignableElement, PortVisit> endConditionMap,
			final Map<Slot, SlotAllocation> slotAllocationMap) {

		for (final Slot slot : seenSlots) {
			// Slot has already been removed, ignore it.
			// FIXME: This slot may be a dependency of a slot later in the list and have it's own dependencies which will not be included. We should probably loop over a cloned list which can be added
			// to.
			if (slotsToRemove.contains(slot)) {
				continue;
			}
			final List<Slot> slotDependencies = getExtraDependenciesForSlot(slot);
			for (final Slot dep : slotDependencies) {
				if (slotsToRemove.contains(dep)) {
					slotsToRemove.remove(dep);
					final Cargo depCargo = dep.getCargo();
					if (depCargo != null) {
						// Include all deps too
						slotsToRemove.removeAll(depCargo.getSlots());
						cargoesToRemove.remove(depCargo);
						if (depCargo.getCargoType() == CargoType.FLEET) {
							final VesselAssignmentType vesselAssignmentType = depCargo.getVesselAssignmentType();
							if (vesselAssignmentType instanceof VesselAvailability) {
								VesselAvailability vesselAvailability = (VesselAvailability) vesselAssignmentType;
								final VesselAvailability newVesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
								newVesselAvailability.setStartHeel(FleetFactory.eINSTANCE.createHeelOptions());
								newVesselAvailability.setVessel(vesselAvailability.getVessel());

								// TODO: set charter rate, set entity. Once multiple avail complete, grab from assignment.

								newVesselAvailabilities.add(newVesselAvailability);
								updateVesselAvailabilityConditions(newVesselAvailability, depCargo, startConditionMap, endConditionMap);
							}

						}
						// Remove any vessel & time window flexibility
						lockDownCargoDates(slotAllocationMap, depCargo);
					}
				}
			}
		}
	}

	/**
	 * Remove the surplus slots and cargoes from the period scenario and record the deletion in the mapping object.
	 * 
	 * @param internalDomain
	 * @param mapping
	 * @param slotsToRemove
	 * @param cargoesToRemove
	 */
	public void removeExcludedSlotsAndCargoes(final EditingDomain internalDomain, final IScenarioEntityMapping mapping, final Collection<Slot> slotsToRemove, final Collection<Cargo> cargoesToRemove) {
		// <<<
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

	public void lockDownCargoDates(final Map<Slot, SlotAllocation> slotAllocationMap, final Cargo cargo) {

		for (final Slot slot : cargo.getSlots()) {
			if (slot instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slot;
				if (loadSlot.isDESPurchase()) {
					// Dates defined by other slots
					continue;
				}
			} else if (slot instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) slot;
				if (dischargeSlot.isFOBSale()) {
					// Dates defined by other slots
					continue;
				}
			}
			// if (slot instanceof SpotSlot) {
			// // Problems with spot slots...
			// continue;
			// }
			final SlotAllocation cargoSlotAllocation = slotAllocationMap.get(slot);
			if (cargoSlotAllocation != null) {
				slot.setWindowSize(0);
				final Calendar localStart = (Calendar) cargoSlotAllocation.getLocalStart().clone();
				final int localTime = localStart.get(Calendar.HOUR_OF_DAY);
				localStart.set(Calendar.HOUR_OF_DAY, 0);
				slot.setWindowStart(localStart.getTime());
				slot.setWindowStartTime(localTime);
			}
		}
		cargo.setAllowRewiring(false);
		if (cargo.getCargoType() == CargoType.FLEET) {
			cargo.setLocked(true);
		}
	}

	public void lockDownSlotDates(final Map<Slot, SlotAllocation> slotAllocationMap, final Slot slot) {
		if (slot.getName().equals("N130")) {
			int i = 0;
		}
		if (slot instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) slot;
			if (loadSlot.isDESPurchase()) {
				// Dates defined by other slots
				return;
			}
		} else if (slot instanceof DischargeSlot) {
			final DischargeSlot dischargeSlot = (DischargeSlot) slot;
			if (dischargeSlot.isFOBSale()) {
				// Dates defined by other slots
				return;
			}
		}

		final SlotAllocation cargoSlotAllocation = slotAllocationMap.get(slot);
		if (cargoSlotAllocation != null) {
			slot.setWindowSize(0);
			final Calendar localStart = (Calendar) cargoSlotAllocation.getLocalStart().clone();
			final int localTime = localStart.get(Calendar.HOUR_OF_DAY);
			localStart.set(Calendar.HOUR_OF_DAY, 0);
			slot.setWindowStart(localStart.getTime());
			slot.setWindowStartTime(localTime);
			slot.getAllowedVessels().clear();
			VesselAssignmentType vat = slot.getCargo().getVesselAssignmentType();
			if (vat instanceof VesselAvailability) {
				slot.getAllowedVessels().add(((VesselAvailability) vat).getVessel());
			} else if (vat instanceof CharterInMarket) {
				slot.getAllowedVessels().add(((CharterInMarket) vat).getVesselClass());
			}
		}
		slot.setLocked(true);
	}

	protected List<Slot> getExtraDependenciesForSlot(final Slot slot) {

		if (extensions == null) {
			return Collections.emptyList();
		}
		final List<Slot> extraDependencies = new LinkedList<Slot>();
		for (final IPeriodTransformerExtension ext : extensions) {
			final List<Slot> extraDependenciesForSlot = ext.getExtraDependenciesForSlot(slot);
			if (extraDependenciesForSlot != null) {
				extraDependencies.addAll(extraDependenciesForSlot);
			}
		}

		return extraDependencies;
	}

	public void updateVesselAvailabilities(final PeriodRecord periodRecord, final CargoModel cargoModel, final SpotMarketsModel spotMarketsModel,
			final Map<AssignableElement, PortVisit> startConditionMap, final Map<AssignableElement, PortVisit> endConditionMap) {

		final List<CollectedAssignment> collectedAssignments = AssignmentEditorHelper.collectAssignments(cargoModel, spotMarketsModel);

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

						// skip to next segment - start of next sequence. Otherwise this could e.g. cargo discharge slot.
						// TODO: Note, what about FOB/DES here?
						Event segmentEnd = ScheduleModelUtils.getSegmentEnd(event).getNextEvent();
						while (segmentEnd != null && !(segmentEnd instanceof PortVisit)) {
							segmentEnd = segmentEnd.getNextEvent();
						}

						startConditionMap.put(cargo, (PortVisit) segmentEnd);

					} else if (event instanceof VesselEventVisit) {
						final VesselEventVisit vesselEventVisit = (VesselEventVisit) event;
						// Find sequence start and sequence end and map to event.
						final VesselEvent vesselEvent = vesselEventVisit.getVesselEvent();
						endConditionMap.put(vesselEvent, vesselEventVisit);

						Event segmentEnd = ScheduleModelUtils.getSegmentEnd(event);
						while (segmentEnd != null && !(segmentEnd instanceof PortVisit)) {
							segmentEnd = segmentEnd.getNextEvent();
						}
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

	public void generateSlotAllocationMap(final LNGScenarioModel output, final Map<Slot, SlotAllocation> slotAllocationMap) {
		final ScheduleModel scheduleModel = output.getPortfolioModel().getScheduleModel();
		final Schedule schedule = scheduleModel.getSchedule();
		for (final SlotAllocation slotAllocation : schedule.getSlotAllocations()) {
			slotAllocationMap.put(slotAllocation.getSlot(), slotAllocation);
		}
	}

	public EditingDomain evaluateScenario(final OptimiserSettings optimiserSettings, final LNGScenarioModel output) {

		final LNGTransformer transformer = new LNGTransformer(output, optimiserSettings, testingModule);

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
			if (portVisit instanceof VesselEventVisit && ((VesselEventVisit) portVisit).getVesselEvent() instanceof CharterOutEvent) {
				vesselAvailability.getStartAt().add(((VesselEventVisit) portVisit).getVesselEvent().getPort());
			} else {
				vesselAvailability.getStartAt().add(portVisit.getPort());
			}

			vesselAvailability.setStartAfter(portVisit.getStart());
			vesselAvailability.setStartBy(portVisit.getStart());

			// Check end after bounds. Do they still apply?
			// TODO: Add this to unit tests
			if (vesselAvailability.isSetEndAfter()) {
				if (vesselAvailability.getEndAfter().before(portVisit.getStart())) {
					vesselAvailability.setEndAfter(portVisit.getStart());
				}
			}

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
			if (vesselAvailability.getEndHeel() == null) {
				vesselAvailability.setEndHeel(CargoFactory.eINSTANCE.createEndHeelOptions());
			}

			// Set must arrive cold with target heel volume
			final int heel = portVisit.getHeelAtStart();
			if (heel > 0 || portVisit.getPreviousEvent() instanceof Cooldown) {
				vesselAvailability.getEndHeel().setTargetEndHeel(heel);
			} else {
				vesselAvailability.getEndHeel().unsetTargetEndHeel();
			}
		}
	}

	public InclusionChecker getInclusionChecker() {
		return inclusionChecker;
	}

	public void setInclusionChecker(final InclusionChecker inclusionChecker) {
		this.inclusionChecker = inclusionChecker;
	}

	/**
	 * Given a new vessel availability, update the start and end conditions around the input AssignableElement. This is assumed to be a single element modelled statically outside the main period
	 * scope.
	 * 
	 * @param vesselAvailability
	 * @param assignedObject
	 * @param startConditionMap
	 * @param endConditionMap
	 */

	public void updateVesselAvailabilityConditions(final VesselAvailability vesselAvailability, final AssignableElement assignedObject, final Map<AssignableElement, PortVisit> startConditionMap,
			final Map<AssignableElement, PortVisit> endConditionMap) {

		// TODO: Refactor the other conditions methods to avoid code duplication.
		{
			final PortVisit portVisit = endConditionMap.get(assignedObject);
			vesselAvailability.getStartAt().clear();
			vesselAvailability.getStartAt().add(portVisit.getPort());

			vesselAvailability.setStartAfter(portVisit.getStart());
			vesselAvailability.setStartBy(portVisit.getStart());

			// Check end after bounds. Do they still apply?
			// TODO: Add this to unit tests
			if (vesselAvailability.isSetEndAfter()) {
				if (vesselAvailability.getEndAfter().before(portVisit.getStart())) {
					vesselAvailability.unsetEndAfter();
				}
			}

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
		{
			final PortVisit portVisit = startConditionMap.get(assignedObject);
			vesselAvailability.getEndAt().clear();
			vesselAvailability.getEndAt().add(portVisit.getPort());

			vesselAvailability.setEndAfter(portVisit.getStart());
			vesselAvailability.setEndBy(portVisit.getStart());

			// Set must arrive cold with target heel volume
			final int heel = portVisit.getHeelAtStart();
			if (heel > 0 || portVisit.getPreviousEvent() instanceof Cooldown) {
				if (vesselAvailability.getEndHeel() == null) {
					vesselAvailability.setEndHeel(CargoFactory.eINSTANCE.createEndHeelOptions());
				}
				vesselAvailability.getEndHeel().setTargetEndHeel(heel);
			} else {
				vesselAvailability.getEndHeel().unsetTargetEndHeel();
			}
		}
	}

	public void trimSpotMarketCurves(final EditingDomain internalDomain, final PeriodRecord periodRecord, final SpotMarketsModel spotMarketsModel, final IScenarioEntityMapping mapping) {

		// Not quite ready yet...
		// trimSpotMarketCurves(internalDomain, periodRecord, spotMarketsModel.getDesPurchaseSpotMarket(), mapping);
		// trimSpotMarketCurves(internalDomain, periodRecord, spotMarketsModel.getDesSalesSpotMarket(), mapping);
		// trimSpotMarketCurves(internalDomain, periodRecord, spotMarketsModel.getFobPurchasesSpotMarket(), mapping);
		// trimSpotMarketCurves(internalDomain, periodRecord, spotMarketsModel.getFobSalesSpotMarket(), mapping);
	}

	public void trimSpotMarketCurves(final EditingDomain internalDomain, final PeriodRecord periodRecord, final SpotMarketGroup spotMarketGroup, final IScenarioEntityMapping mapping) {
		for (final SpotMarket spotMarket : spotMarketGroup.getMarkets()) {
			final SpotAvailability availability = spotMarket.getAvailability();

			// If the constant is set, get the value and replace with zero. Later create new curve points with the original constant value.
			int constantValue = 0;
			if (availability.isSetConstant() && availability.getConstant() > 0) {
				constantValue = availability.getConstant();
				availability.unsetConstant();
			}

			// Create lookup of curve dates and remove those outside optimisation range.
			final Set<Date> seenDates = new HashSet<>();
			final DataIndex<Integer> curve = availability.getCurve();
			final List<IndexPoint<Integer>> pointsToRemove = new LinkedList<>();
			for (final IndexPoint<Integer> value : curve.getPoints()) {
				seenDates.add(value.getDate());
				if (value.getDate().before(periodRecord.lowerBoundary)) {
					// remove
					pointsToRemove.add(value);
				}
				if (value.getDate().after(periodRecord.upperBoundary)) {
					// remove
					pointsToRemove.add(value);
				}
			}
			curve.getPoints().removeAll(pointsToRemove);

			// TODO: If bounds are missing, then we need to do something different.
			// Set the constant, and add curve data across known period instead

			// Fill in curve gaps with the original constant value.
			if (constantValue != 0) {
				final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				cal.setTime(periodRecord.lowerBoundary);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				while (cal.getTime().before(periodRecord.upperBoundary)) {
					if (!seenDates.contains(cal.getTime())) {
						final IndexPoint<Integer> newValue = PricingFactory.eINSTANCE.createIndexPoint();
						newValue.setDate(cal.getTime());
						newValue.setValue(constantValue);
						// Add
						curve.getPoints().add(newValue);
					}

				}

			}

		}
	}
}
