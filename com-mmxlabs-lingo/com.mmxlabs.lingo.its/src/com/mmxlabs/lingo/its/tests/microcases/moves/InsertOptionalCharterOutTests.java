/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.moves;

import java.time.LocalDateTime;
import java.util.Random;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
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
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.moves.handlers.InsertOptionalElementMoveHandler;
import com.mmxlabs.scheduler.optimiser.moves.handlers.RemoveOptionalElementMoveHandler;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * Tests for the {@link OptionalConstrainedMoveGenerator}
 *
 */
@RunWith(value = ShiroRunner.class)
public class InsertOptionalCharterOutTests extends AbstractMoveHandlerTest {

	/**
	 * Make sure we can insert a optional charter out event onto an empty vessel.
	 * 
	 */
	@Test
	@Category({ MicroTest.class })
	public void testSimpleInsertOptionalCharterOutMove() throws Exception {

		// Construct the vessel
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 3, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 12, 30, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvent
		final CharterOutEvent charterOutEvent = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_solo", LocalDateTime.of(2017, 12, 5, 0, 0, 0), LocalDateTime.of(2017, 12, 15, 0, 0, 0), portFinder.findPort("Sabine Pass LNG")) //
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
			Assert.assertNotNull(elementPair);

			final Random random = new Random(0);

			final IMove move = handler.generateAddingMove(element, elementPair.getSecond(), lookupManager, random);

			final ModifiableSequences result = new ModifiableSequences(initialRawSequences);
			move.apply(result);

			final IVesselAvailability o_vesselAvailability1 = modelEntityMap.getOptimiserObjectNullChecked(vesselAvailability, IVesselAvailability.class);

			final IResource resource1 = vesselProvider.getResource(o_vesselAvailability1);

			// Check expectations
			// Empty vessel
			Assert.assertNotNull("Sequence is null", result.getSequence(resource1));
			Assert.assertEquals(3, result.getSequence(resource1).size());
			Assert.assertEquals(0, result.getUnusedElements().size());

			Assert.assertEquals("charter_out_test_solo", result.getSequence(resource1).get(1).getName());
		});
	}

	/**
	 * Make sure we can insert a optional charter out event onto an empty vessel.
	 * 
	 */
	@Test
	@Category({ MicroTest.class })
	public void testSimpleRemoveOptionalCharterOutMove() throws Exception {

		// Construct the vessel
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 3, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 12, 30, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvent
		final CharterOutEvent charterOutEvent = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_solo", LocalDateTime.of(2017, 12, 5, 0, 0, 0), LocalDateTime.of(2017, 12, 15, 0, 0, 0), portFinder.findPort("Sabine Pass LNG")) //
				.withHireRate(50_000) //
				.withOptional(true) //
				.withDurationInDays(10) //
				.withVesselAssignment(vesselAvailability, 1) //
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
			Assert.assertNotNull(elementPair);

			lookupManager.createLookup(initialRawSequences);

			final Random random = new Random(0);

			final IMove move = handler.generateRemovingMove(element, elementPair, lookupManager, random);

			final ModifiableSequences result = new ModifiableSequences(initialRawSequences);
			move.apply(result);

			final IVesselAvailability o_vesselAvailability1 = modelEntityMap.getOptimiserObjectNullChecked(vesselAvailability, IVesselAvailability.class);

			final IResource resource1 = vesselProvider.getResource(o_vesselAvailability1);

			// Check expectations
			// 3 event: start, charter out, end
			Assert.assertNotNull("Sequence is null", result.getSequence(0));
			Assert.assertEquals(2, result.getSequence(resource1).size());
			Assert.assertEquals(1, result.getUnusedElements().size());

			Assert.assertEquals("charter_out_test_solo", result.getUnusedElements().get(0).getName());
		});
	}

	/**
	 * Make sure we can insert a optional charter out event onto an empty vessel.
	 * 
	 */
	@Test
	@Category({ MicroTest.class })
	public void testInsertOptionalCharterOutWithRelocationMove() throws Exception {

		// Construct the vessel
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 3, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 12, 30, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvent
		final CharterOutEvent charterOutEvent = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_solo", LocalDateTime.of(2017, 12, 5, 0, 0, 0), LocalDateTime.of(2017, 12, 15, 0, 0, 0), portFinder.findPort("Sabine Pass LNG")) //
				.withHireRate(50_000) //
				.withOptional(true) //
				.withDurationInDays(10) //
				.withAllowedVessels(vessel) //
				.withRelocatePort(portFinder.findPort("Tobata")) //
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
				Assert.assertEquals(3, vesselEventPortSlot.getEventPortSlots().size());
				Assert.assertEquals(3, vesselEventPortSlot.getEventSequenceElements().size());
				for (final IPortSlot ps : vesselEventPortSlot.getEventPortSlots()) {
					Assert.assertEquals(3, ((IVesselEventPortSlot) ps).getEventPortSlots().size());
					Assert.assertEquals(3, ((IVesselEventPortSlot) ps).getEventSequenceElements().size());

				}
			}

			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final InsertOptionalElementMoveHandler handler = injector.getInstance(InsertOptionalElementMoveHandler.class);
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(initialRawSequences);
			{
				final ISequenceElement element = portSlotProvider.getElement(portSlot);
				final Pair<IResource, Integer> elementPair = lookupManager.lookup(element);
				Assert.assertNotNull(elementPair);

				final Random random = new Random(0);

				final IMove move = handler.generateAddingMove(element, elementPair.getSecond(), lookupManager, random);

				final ModifiableSequences result = new ModifiableSequences(initialRawSequences);
				move.apply(result);

				final IVesselAvailability o_vesselAvailability1 = modelEntityMap.getOptimiserObjectNullChecked(vesselAvailability, IVesselAvailability.class);

				final IResource resource1 = vesselProvider.getResource(o_vesselAvailability1);

				// Check expectations
				// 5 events: start, start-charter, redirect-charter out, charter-out, end
				Assert.assertNotNull("Sequence is null", result.getSequence(resource1));
				Assert.assertEquals(5, result.getSequence(resource1).size());
				Assert.assertEquals(0, result.getUnusedElements().size());

				Assert.assertEquals("start-charter_out_test_solo", result.getSequence(resource1).get(1).getName());
				Assert.assertEquals("redirect-charter_out_test_solo", result.getSequence(resource1).get(2).getName());
				Assert.assertEquals("charter_out_test_solo", result.getSequence(resource1).get(3).getName());
			}

			// Make sure we can insert from *ANY* element in the element sequence
			for (final ISequenceElement element : vesselEventPortSlot.getEventSequenceElements()) {
				final Pair<IResource, Integer> elementPair = lookupManager.lookup(element);
				Assert.assertNotNull(elementPair);

				final Random random = new Random(0);

				final IMove move = handler.generateAddingMove(element, elementPair.getSecond(), lookupManager, random);
				Assert.assertNotNull(move);
			}
		});
	}

	/**
	 * Make sure we can insert a optional charter out event onto an empty vessel.
	 * 
	 */
	@Test
	@Category({ MicroTest.class })
	public void testRemoveOptionalCharterOutWithRelocationMove() throws Exception {

		// Construct the vessel
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 3, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 12, 30, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvent
		final CharterOutEvent charterOutEvent = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_solo", LocalDateTime.of(2017, 12, 5, 0, 0, 0), LocalDateTime.of(2017, 12, 15, 0, 0, 0), portFinder.findPort("Sabine Pass LNG")) //
				.withHireRate(50_000) //
				.withOptional(true) //
				.withDurationInDays(10) //
				.withVesselAssignment(vesselAvailability, 1) //
				.withRelocatePort(portFinder.findPort("Tobata")) //
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
			Assert.assertNotNull(elementPair);

			final Random random = new Random(0);

			final IMove move = handler.generateRemovingMove(element, elementPair, lookupManager, random);

			final ModifiableSequences result = new ModifiableSequences(initialRawSequences);
			move.apply(result);

			final IVesselAvailability o_vesselAvailability1 = modelEntityMap.getOptimiserObjectNullChecked(vesselAvailability, IVesselAvailability.class);

			final IResource resource1 = vesselProvider.getResource(o_vesselAvailability1);

			// Check expectations
			// 5 events: start, start-charter, redirect-charter out, charter-out, end
			Assert.assertNotNull("Sequence is null", result.getSequence(resource1));
			Assert.assertEquals(2, result.getSequence(resource1).size());
			Assert.assertEquals(3, result.getUnusedElements().size());

			Assert.assertEquals("start-charter_out_test_solo", result.getUnusedElements().get(0).getName());
			Assert.assertEquals("redirect-charter_out_test_solo", result.getUnusedElements().get(1).getName());
			Assert.assertEquals("charter_out_test_solo", result.getUnusedElements().get(2).getName());
		});
	}

	/**
	 * We check if the charter out event is present in the resulting cargoModel export
	 * 
	 */
	@Test
	@Category({ MicroTest.class })
	public void testExportOptionalCharterOutMove() throws Exception {

		// Construct the vessel
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 3, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 12, 30, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvent
		final CharterOutEvent charterOutEvent = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_solo", LocalDateTime.of(2017, 12, 5, 0, 0, 0), LocalDateTime.of(2017, 12, 15, 0, 0, 0), portFinder.findPort("Sabine Pass LNG")) //
				.withHireRate(50_000) //
				.withOptional(true) //
				.withDurationInDays(10) //
				.withAllowedVessels(vessel) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Create the sequence to export
			final IModifiableSequences result = SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector());
			SequenceHelper.addSequence(result, scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability, charterOutEvent);

			final Schedule updatedSchedule = scenarioToOptimiserBridge.createOptimiserSchedule(result, null);

			// Create the export command and try to execute it
			final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

			final Command cmd = LNGSchedulerJobUtils.exportSchedule(scenarioToOptimiserBridge.getInjector(), lngScenarioModel, editingDomain, updatedSchedule);

			Assert.assertTrue(cmd.canExecute());
			cmd.execute();

			// Get the exported CargoModel and check that everything is still there
			final VesselAssignmentType availability = charterOutEvent.getVesselAssignmentType();
			Assert.assertNotNull(availability);
		});
	}

	/**
	 * We check if the charter out event with relocation is present in the resulting cargoModel export
	 * 
	 */
	@Test
	@Category({ MicroTest.class })
	public void testExportOptionalCharterOutWithRelocationMove() throws Exception {

		// Construct the vessel
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 3, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 12, 30, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvent
		final CharterOutEvent charterOutEvent = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_solo", LocalDateTime.of(2017, 12, 5, 0, 0, 0), LocalDateTime.of(2017, 12, 15, 0, 0, 0), portFinder.findPort("Sabine Pass LNG")) //
				.withHireRate(50_000) //
				.withOptional(true) //
				.withDurationInDays(10) //
				.withAllowedVessels(vessel) //
				.withRelocatePort(portFinder.findPort("Tobata")) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Create the sequence to export
			final IModifiableSequences result = SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector());
			SequenceHelper.addSequence(result, scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability, charterOutEvent);

			final Schedule updatedSchedule = scenarioToOptimiserBridge.createOptimiserSchedule(result, null);

			// Create the export command and try to execute it
			final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

			final Command cmd = LNGSchedulerJobUtils.exportSchedule(scenarioToOptimiserBridge.getInjector(), lngScenarioModel, editingDomain, updatedSchedule);

			Assert.assertTrue(cmd.canExecute());
			cmd.execute();

			// Get the exported CargoModel and check that everything is still there
			final VesselAssignmentType availability = charterOutEvent.getVesselAssignmentType();
			Assert.assertNotNull(availability);
		});
	}

	/**
	 * We check if the charter out event with relocation is still present in the resulting cargoModel export After having explicitly removed it from the optimiser's schedule
	 * 
	 */
	@Test
	@Category({ MicroTest.class })
	public void testExportOptionalCharterOutWithRelocationRemoveMove() throws Exception {

		// Construct the vessel
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 3, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 12, 30, 0, 0, 0)) //
				.build();

		// Construct the charterOutEvent
		final CharterOutEvent charterOutEvent = cargoModelBuilder //
				.makeCharterOutEvent("charter_out_test_solo", LocalDateTime.of(2017, 12, 5, 0, 0, 0), LocalDateTime.of(2017, 12, 15, 0, 0, 0), portFinder.findPort("Sabine Pass LNG")) //
				.withHireRate(50_000) //
				.withOptional(true) //
				.withDurationInDays(10) //
				.withVesselAssignment(vesselAvailability, 1) //
				.withRelocatePort(portFinder.findPort("Tobata")) //
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

			Assert.assertTrue(cmd.canExecute());
			cmd.execute();

			// Get the exported CargoModel and check that the charter out event is now vessel-less
			final VesselAssignmentType availability = charterOutEvent.getVesselAssignmentType();
			Assert.assertNull(availability);
		});
	}

}