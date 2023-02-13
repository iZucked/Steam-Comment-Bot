/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.moves;

import java.time.LocalDateTime;
import java.util.Random;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.moves.handlers.InsertOptionalElementMoveHandler;
import com.mmxlabs.scheduler.optimiser.moves.handlers.RemoveOptionalElementMoveHandler;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * Tests for the {@link OptionalConstrainedMoveGenerator}
 *
 */
@ExtendWith(ShiroRunner.class)
public class InsertOptionalCharterOutTests extends AbstractMoveHandlerTest {

	/**
	 * Make sure we can insert a optional charter out event onto an empty vessel.
	 * 
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSimpleInsertOptionalCharterOutMove() throws Exception {

		// Construct the vessel
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 3, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 12, 30, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvent
		final CharterOutEvent charterOutEvent = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_solo", LocalDateTime.of(2017, 12, 5, 0, 0, 0), LocalDateTime.of(2017, 12, 15, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS)) //
				.withHireRate(50_000) //
				.withOptional(true) //
				.withDurationInDays(10) //
				.withAllowedVessels(vessel) //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(charterOutEvent, IPortSlot.class);
			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final ISequenceElement element = portSlotProvider.getElement(portSlot);

			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final InsertOptionalElementMoveHandler handler = injector.getInstance(InsertOptionalElementMoveHandler.class);
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(initialRawSequences);

			final Pair<IResource, Integer> elementPair = lookupManager.lookup(element);
			Assertions.assertNotNull(elementPair);

			final Random random = new Random(0);

			final IMove move = handler.generateAddingMove(element, elementPair.getSecond(), lookupManager, random);

			final ModifiableSequences result = new ModifiableSequences(initialRawSequences);
			move.apply(result);

			final IVesselCharter o_vesselCharter1 = modelEntityMap.getOptimiserObjectNullChecked(vesselCharter, IVesselCharter.class);

			final IResource resource1 = vesselProvider.getResource(o_vesselCharter1);

			// Check expectations
			// Empty vessel
			Assertions.assertNotNull(result.getSequence(resource1), "Sequence is null");
			Assertions.assertEquals(5, result.getSequence(resource1).size());
			Assertions.assertEquals(0, result.getUnusedElements().size());

			Assertions.assertEquals("start-charter_out_test_solo", result.getSequence(resource1).get(1).getName());
		});
	}

	/**
	 * Make sure we can insert a optional charter out event onto an empty vessel.
	 * 
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSimpleRemoveOptionalCharterOutMove() throws Exception {

		// Construct the vessel
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 3, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 12, 30, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvent
		final CharterOutEvent charterOutEvent = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_solo", LocalDateTime.of(2017, 12, 5, 0, 0, 0), LocalDateTime.of(2017, 12, 15, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS)) //
				.withHireRate(50_000) //
				.withOptional(true) //
				.withDurationInDays(10) //
				.withVesselAssignment(vesselCharter, 1) //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(charterOutEvent, IPortSlot.class);
			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final ISequenceElement element = portSlotProvider.getElement(portSlot);

			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final RemoveOptionalElementMoveHandler handler = injector.getInstance(RemoveOptionalElementMoveHandler.class);
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(initialRawSequences);

			final Pair<IResource, Integer> elementPair = lookupManager.lookup(element);
			Assertions.assertNotNull(elementPair);

			lookupManager.createLookup(initialRawSequences);

			final Random random = new Random(0);

			final IMove move = handler.generateRemovingMove(element, elementPair, lookupManager, random);

			final ModifiableSequences result = new ModifiableSequences(initialRawSequences);
			move.apply(result);

			final IVesselCharter o_vesselCharter1 = modelEntityMap.getOptimiserObjectNullChecked(vesselCharter, IVesselCharter.class);

			final IResource resource1 = vesselProvider.getResource(o_vesselCharter1);

			// Check expectations
			// 3 event: start, charter out, end
			Assertions.assertNotNull(result.getSequence(0), "Sequence is null");
			Assertions.assertEquals(2, result.getSequence(resource1).size());
			Assertions.assertEquals(3, result.getUnusedElements().size());

			Assertions.assertEquals("start-charter_out_test_solo", result.getUnusedElements().get(0).getName());
		});
	}

	/**
	 * Make sure we can insert a optional charter out event onto an empty vessel.
	 * 
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testInsertOptionalCharterOutWithRelocationMove() throws Exception {

		// Construct the vessel
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 3, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 12, 30, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvent
		final CharterOutEvent charterOutEvent = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_solo", LocalDateTime.of(2017, 12, 5, 0, 0, 0), LocalDateTime.of(2017, 12, 15, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS)) //
				.withHireRate(50_000) //
				.withOptional(true) //
				.withDurationInDays(10) //
				.withAllowedVessels(vessel) //
				.withRelocatePort(portFinder.findPortById(InternalDataConstants.PORT_TOBATA)) //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(charterOutEvent, IPortSlot.class);
			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselEventPortSlot vesselEventPortSlot = (IVesselEventPortSlot) portSlot;
			{
				// Validate event is transformed correctly and linked up
				Assertions.assertEquals(3, vesselEventPortSlot.getEventPortSlots().size());
				Assertions.assertEquals(3, vesselEventPortSlot.getEventSequenceElements().size());
				for (final IPortSlot ps : vesselEventPortSlot.getEventPortSlots()) {
					Assertions.assertEquals(3, ((IVesselEventPortSlot) ps).getEventPortSlots().size());
					Assertions.assertEquals(3, ((IVesselEventPortSlot) ps).getEventSequenceElements().size());

				}
			}

			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final InsertOptionalElementMoveHandler handler = injector.getInstance(InsertOptionalElementMoveHandler.class);
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(initialRawSequences);
			{
				final ISequenceElement element = portSlotProvider.getElement(portSlot);
				final Pair<IResource, Integer> elementPair = lookupManager.lookup(element);
				Assertions.assertNotNull(elementPair);

				final Random random = new Random(0);

				final IMove move = handler.generateAddingMove(element, elementPair.getSecond(), lookupManager, random);

				final ModifiableSequences result = new ModifiableSequences(initialRawSequences);
				move.apply(result);

				final IVesselCharter o_vesselCharter1 = modelEntityMap.getOptimiserObjectNullChecked(vesselCharter, IVesselCharter.class);

				final IResource resource1 = vesselProvider.getResource(o_vesselCharter1);

				// Check expectations
				// 5 events: start, start-charter, redirect-charter out, charter-out, end
				Assertions.assertNotNull(result.getSequence(resource1), "Sequence is null");
				Assertions.assertEquals(5, result.getSequence(resource1).size());
				Assertions.assertEquals(0, result.getUnusedElements().size());

				Assertions.assertEquals("start-charter_out_test_solo", result.getSequence(resource1).get(1).getName());
				Assertions.assertEquals("redirect-charter_out_test_solo", result.getSequence(resource1).get(2).getName());
				Assertions.assertEquals("charter_out_test_solo", result.getSequence(resource1).get(3).getName());
			}

			// Make sure we can insert from *ANY* element in the element sequence
			for (final ISequenceElement element : vesselEventPortSlot.getEventSequenceElements()) {
				final Pair<IResource, Integer> elementPair = lookupManager.lookup(element);
				Assertions.assertNotNull(elementPair);

				final Random random = new Random(0);

				final IMove move = handler.generateAddingMove(element, elementPair.getSecond(), lookupManager, random);
				Assertions.assertNotNull(move);
			}
		});
	}

	/**
	 * Make sure we can insert a optional charter out event onto an empty vessel.
	 * 
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRemoveOptionalCharterOutWithRelocationMove() throws Exception {

		// Construct the vessel
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 3, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 12, 30, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvent
		final CharterOutEvent charterOutEvent = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_solo", LocalDateTime.of(2017, 12, 5, 0, 0, 0), LocalDateTime.of(2017, 12, 15, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS)) //
				.withHireRate(50_000) //
				.withOptional(true) //
				.withDurationInDays(10) //
				.withVesselAssignment(vesselCharter, 1) //
				.withRelocatePort(portFinder.findPortById(InternalDataConstants.PORT_TOBATA)) //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(charterOutEvent, IPortSlot.class);
			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final ISequenceElement element = portSlotProvider.getElement(portSlot);

			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final RemoveOptionalElementMoveHandler handler = injector.getInstance(RemoveOptionalElementMoveHandler.class);
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(initialRawSequences);

			final Pair<IResource, Integer> elementPair = lookupManager.lookup(element);
			Assertions.assertNotNull(elementPair);

			final Random random = new Random(0);

			final IMove move = handler.generateRemovingMove(element, elementPair, lookupManager, random);

			final ModifiableSequences result = new ModifiableSequences(initialRawSequences);
			move.apply(result);

			final IVesselCharter o_vesselCharter1 = modelEntityMap.getOptimiserObjectNullChecked(vesselCharter, IVesselCharter.class);

			final IResource resource1 = vesselProvider.getResource(o_vesselCharter1);

			// Check expectations
			// 5 events: start, start-charter, redirect-charter out, charter-out, end
			Assertions.assertNotNull(result.getSequence(resource1), "Sequence is null");
			Assertions.assertEquals(2, result.getSequence(resource1).size());
			Assertions.assertEquals(3, result.getUnusedElements().size());

			Assertions.assertEquals("start-charter_out_test_solo", result.getUnusedElements().get(0).getName());
			Assertions.assertEquals("redirect-charter_out_test_solo", result.getUnusedElements().get(1).getName());
			Assertions.assertEquals("charter_out_test_solo", result.getUnusedElements().get(2).getName());
		});
	}

	/**
	 * We check if the charter out event is present in the resulting cargoModel export
	 * 
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testExportOptionalCharterOutMove() throws Exception {

		// Construct the vessel
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 3, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 12, 30, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvent
		final CharterOutEvent charterOutEvent = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_solo", LocalDateTime.of(2017, 12, 5, 0, 0, 0), LocalDateTime.of(2017, 12, 15, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS)) //
				.withHireRate(50_000) //
				.withOptional(true) //
				.withDurationInDays(10) //
				.withAllowedVessels(vessel) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Create the sequence to export
			final IModifiableSequences result = SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector());
			SequenceHelper.addSequence(result, scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselCharter, charterOutEvent);

			final Schedule updatedSchedule = scenarioToOptimiserBridge.createOptimiserSchedule(result, null);

			// Create the export command and try to execute it
			final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

			final Command cmd = LNGSchedulerJobUtils.exportSchedule(scenarioToOptimiserBridge.getInjector(), lngScenarioModel, editingDomain, updatedSchedule);

			Assertions.assertTrue(cmd.canExecute());
			cmd.execute();

			// Get the exported CargoModel and check that everything is still there
			final VesselAssignmentType availability = charterOutEvent.getVesselAssignmentType();
			Assertions.assertNotNull(availability);
		});
	}

	/**
	 * We check if the charter out event with relocation is present in the resulting cargoModel export
	 * 
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testExportOptionalCharterOutWithRelocationMove() throws Exception {

		// Construct the vessel
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 3, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 12, 30, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvent
		final CharterOutEvent charterOutEvent = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_solo", LocalDateTime.of(2017, 12, 5, 0, 0, 0), LocalDateTime.of(2017, 12, 15, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS)) //
				.withHireRate(50_000) //
				.withOptional(true) //
				.withDurationInDays(10) //
				.withAllowedVessels(vessel) //
				.withRelocatePort(portFinder.findPortById(InternalDataConstants.PORT_TOBATA)) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Create the sequence to export
			final IModifiableSequences result = SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector());
			SequenceHelper.addSequence(result, scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselCharter, charterOutEvent);

			final Schedule updatedSchedule = scenarioToOptimiserBridge.createOptimiserSchedule(result, null);

			// Create the export command and try to execute it
			final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

			final Command cmd = LNGSchedulerJobUtils.exportSchedule(scenarioToOptimiserBridge.getInjector(), lngScenarioModel, editingDomain, updatedSchedule);

			Assertions.assertTrue(cmd.canExecute());
			cmd.execute();

			// Get the exported CargoModel and check that everything is still there
			final VesselAssignmentType availability = charterOutEvent.getVesselAssignmentType();
			Assertions.assertNotNull(availability);
		});
	}

	/**
	 * We check if the charter out event with relocation is still present in the resulting cargoModel export After having explicitly removed it from the optimiser's schedule
	 * 
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testExportOptionalCharterOutWithRelocationRemoveMove() throws Exception {

		// Construct the vessel
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 3, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 12, 30, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvent
		final CharterOutEvent charterOutEvent = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_solo", LocalDateTime.of(2017, 12, 5, 0, 0, 0), LocalDateTime.of(2017, 12, 15, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS)) //
				.withHireRate(50_000) //
				.withOptional(true) //
				.withDurationInDays(10) //
				.withVesselAssignment(vesselCharter, 1) //
				.withRelocatePort(portFinder.findPortById(InternalDataConstants.PORT_TOBATA)) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Create the sequence to export
			final IModifiableSequences result = SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector());

			SequenceHelper.addToUnused(result, scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterOutEvent);

			final Schedule updatedSchedule = scenarioToOptimiserBridge.createOptimiserSchedule(result, null);

			// Create the export command and try to execute it
			final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

			final Command cmd = LNGSchedulerJobUtils.exportSchedule(scenarioToOptimiserBridge.getInjector(), lngScenarioModel, editingDomain, updatedSchedule);

			Assertions.assertTrue(cmd.canExecute());
			cmd.execute();

			// Get the exported CargoModel and check that the charter out event is now vessel-less
			final VesselAssignmentType availability = charterOutEvent.getVesselAssignmentType();
			Assertions.assertNull(availability);
		});
	}

}