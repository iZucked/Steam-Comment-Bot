/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.moves;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuideMoveGeneratorOptions;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.guided.Hints;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.SwapSlotMoveHandler;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * Tests for the {@link GuidedMoveGenerator}
 *
 */
@RunWith(value = ShiroRunner.class)
public class SwapSlotMoveHandlerTests extends AbstractMoveHandlerTest {

	@Test
	@Category({ MicroTest.class })
	public void testSwapCargoLoadWithUnusedLoad() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel1 = fleetModelBuilder.createVessel("My Vessel 1", vesselClass);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("100000") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withOptional(true) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		Slot load1 = cargo1.getSlots().get(0);
		Slot discharge1 = cargo1.getSlots().get(1);

		final LoadSlot load2 = cargoModelBuilder //
				.makeFOBPurchase("L2", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.6) //
				.withOptional(true) //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final SwapSlotMoveHandler handler = injector.getInstance(SwapSlotMoveHandler.class);
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(initialRawSequences);

			final Random random = new Random(0);

			final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(load2, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(portSlot);

			final GuideMoveGeneratorOptions options = GuideMoveGeneratorOptions.createDefault();

			@NonNull
			final Collection<ISequenceElement> forbiddenElements = Collections.emptySet();
			final Function<ISequenceElement, Slot> slotMapper = e -> {
				final IPortSlot ps = portSlotProvider.getPortSlot(e);
				return modelEntityMap.getModelObjectNullChecked(ps, Slot.class);
			};

			@Nullable
			final Pair<IMove, Hints> movePair = handler.handleMove(lookupManager, element, random, options, forbiddenElements);

			Assert.assertNotNull(movePair);

			final ModifiableSequences result = new ModifiableSequences(initialRawSequences);
			movePair.getFirst().apply(result);

			final IVesselAvailability o_vesselAvailability1 = modelEntityMap.getOptimiserObjectNullChecked(vesselAvailability1, IVesselAvailability.class);

			final IResource resource1 = vesselProvider.getResource(o_vesselAvailability1);

			// Check expectations
			Assert.assertEquals(4, result.getSequence(resource1).size());
			Assert.assertEquals(1, result.getUnusedElements().size());

			Assert.assertSame(load1, slotMapper.apply(result.getUnusedElements().get(0)));
			Assert.assertSame(load2, slotMapper.apply(result.getSequence(resource1).get(1)));
			Assert.assertSame(discharge1, slotMapper.apply(result.getSequence(resource1).get(2)));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSwapWithinSequence() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel1 = fleetModelBuilder.createVessel("My Vessel 1", vesselClass);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("100000") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 2, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withWindowSize(3, TimePeriod.MONTHS) //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2017, 3, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2017, 2, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withWindowSize(3, TimePeriod.MONTHS) //
				.build() //
				.withVesselAssignment(vesselAvailability1, 2) //
				.withAssignmentFlags(true, false) //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final SwapSlotMoveHandler handler = injector.getInstance(SwapSlotMoveHandler.class);
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(initialRawSequences);

			final Random random = new Random(0);

			final ISequenceElement load1 = portSlotProvider.getElement(modelEntityMap.getOptimiserObjectNullChecked(cargo1.getSlots().get(0), IPortSlot.class));
			final ISequenceElement discharge1 = portSlotProvider.getElement(modelEntityMap.getOptimiserObjectNullChecked(cargo1.getSlots().get(1), IPortSlot.class));
			final ISequenceElement load2 = portSlotProvider.getElement(modelEntityMap.getOptimiserObjectNullChecked(cargo2.getSlots().get(0), IPortSlot.class));
			final ISequenceElement discharge2 = portSlotProvider.getElement(modelEntityMap.getOptimiserObjectNullChecked(cargo2.getSlots().get(1), IPortSlot.class));

			final GuideMoveGeneratorOptions options = GuideMoveGeneratorOptions.createDefault();

			@Nullable
			final Pair<IMove, Hints> movePair = handler.handleMove(lookupManager, discharge2, random, options, Collections.emptySet());

			Assert.assertNotNull(movePair);

			final ModifiableSequences result = new ModifiableSequences(initialRawSequences);
			movePair.getFirst().apply(result);

			final IVesselAvailability o_vesselAvailability1 = modelEntityMap.getOptimiserObjectNullChecked(vesselAvailability1, IVesselAvailability.class);

			final IResource resource1 = vesselProvider.getResource(o_vesselAvailability1);

			// Check expectations
			Assert.assertEquals(6, result.getSequence(resource1).size());

			Assert.assertSame(load1, result.getSequence(resource1).get(1));
			Assert.assertSame(discharge2, result.getSequence(resource1).get(2));
			Assert.assertSame(load2, result.getSequence(resource1).get(3));
			Assert.assertSame(discharge1, result.getSequence(resource1).get(4));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSwapUnusedLoadWithUnusedLoadFails() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel1 = fleetModelBuilder.createVessel("My Vessel 1", vesselClass);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("100000") //
				.build();

		final LoadSlot load1 = cargoModelBuilder //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.6) //
				.withOptional(true) //
				.build();
		final LoadSlot load2 = cargoModelBuilder //
				.makeFOBPurchase("L2", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.6) //
				.withOptional(true) //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final SwapSlotMoveHandler handler = injector.getInstance(SwapSlotMoveHandler.class);
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(initialRawSequences);

			final Random random = new Random(0);

			final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(load2, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(portSlot);

			final GuideMoveGeneratorOptions options = GuideMoveGeneratorOptions.createDefault();

			@NonNull
			final Collection<ISequenceElement> forbiddenElements = Collections.emptySet();
			final Function<ISequenceElement, Slot> slotMapper = e -> {
				final IPortSlot ps = portSlotProvider.getPortSlot(e);
				return modelEntityMap.getModelObjectNullChecked(ps, Slot.class);
			};

			@Nullable
			final Pair<IMove, Hints> movePair = handler.handleMove(lookupManager, element, random, options, forbiddenElements);

			Assert.assertNull(movePair);

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSwapCargoLoadWithDESPurchaseLoadFails() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel1 = fleetModelBuilder.createVessel("My Vessel 1", vesselClass);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("100000") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withOptional(true) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		Slot load1 = cargo1.getSlots().get(0);
		Slot discharge1 = cargo1.getSlots().get(1);

		final LoadSlot load2 = cargoModelBuilder //
				.makeFOBPurchase("L2", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.6) //
				.withOptional(true) //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final SwapSlotMoveHandler handler = injector.getInstance(SwapSlotMoveHandler.class);
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(initialRawSequences);

			final Random random = new Random(0);

			final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(load2, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(portSlot);

			final GuideMoveGeneratorOptions options = GuideMoveGeneratorOptions.createDefault();

			@NonNull
			final Collection<ISequenceElement> forbiddenElements = Collections.emptySet();
			final Function<ISequenceElement, Slot> slotMapper = e -> {
				final IPortSlot ps = portSlotProvider.getPortSlot(e);
				return modelEntityMap.getModelObjectNullChecked(ps, Slot.class);
			};

			@Nullable
			final Pair<IMove, Hints> movePair = handler.handleMove(lookupManager, element, random, options, forbiddenElements);

			Assert.assertNotNull(movePair);

			final ModifiableSequences result = new ModifiableSequences(initialRawSequences);
			movePair.getFirst().apply(result);

			final IVesselAvailability o_vesselAvailability1 = modelEntityMap.getOptimiserObjectNullChecked(vesselAvailability1, IVesselAvailability.class);

			final IResource resource1 = vesselProvider.getResource(o_vesselAvailability1);

			// Check expectations
			Assert.assertEquals(4, result.getSequence(resource1).size());
			Assert.assertEquals(1, result.getUnusedElements().size());

			Assert.assertSame(load1, slotMapper.apply(result.getUnusedElements().get(0)));
			Assert.assertSame(load2, slotMapper.apply(result.getSequence(resource1).get(1)));
			Assert.assertSame(discharge1, slotMapper.apply(result.getSequence(resource1).get(2)));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSwapCargoDischargeWithUnusedDischarge() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel1 = fleetModelBuilder.createVessel("My Vessel 1", vesselClass);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("100000") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withOptional(true) //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		Slot load1 = cargo1.getSlots().get(0);
		Slot discharge1 = cargo1.getSlots().get(1);

		final DischargeSlot discharge2 = cargoModelBuilder //
				.makeDESSale("D2", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withOptional(true) //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final SwapSlotMoveHandler handler = injector.getInstance(SwapSlotMoveHandler.class);
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(initialRawSequences);

			final Random random = new Random(0);

			final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(discharge2, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(portSlot);

			final GuideMoveGeneratorOptions options = GuideMoveGeneratorOptions.createDefault();

			@NonNull
			final Collection<ISequenceElement> forbiddenElements = Collections.emptySet();
			final Function<ISequenceElement, Slot> slotMapper = e -> {
				final IPortSlot ps = portSlotProvider.getPortSlot(e);
				return modelEntityMap.getModelObjectNullChecked(ps, Slot.class);
			};

			@Nullable
			final Pair<IMove, Hints> movePair = handler.handleMove(lookupManager, element, random, options, forbiddenElements);

			Assert.assertNotNull(movePair);

			final ModifiableSequences result = new ModifiableSequences(initialRawSequences);
			movePair.getFirst().apply(result);

			final IVesselAvailability o_vesselAvailability1 = modelEntityMap.getOptimiserObjectNullChecked(vesselAvailability1, IVesselAvailability.class);

			final IResource resource1 = vesselProvider.getResource(o_vesselAvailability1);

			// Check expectations
			Assert.assertEquals(4, result.getSequence(resource1).size());
			Assert.assertEquals(1, result.getUnusedElements().size());

			Assert.assertSame(discharge1, slotMapper.apply(result.getUnusedElements().get(0)));
			Assert.assertSame(load1, slotMapper.apply(result.getSequence(resource1).get(1)));
			Assert.assertSame(discharge2, slotMapper.apply(result.getSequence(resource1).get(2)));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSwapCargoDischargeWithUsedDischarge() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel1 = fleetModelBuilder.createVessel("My Vessel 1", vesselClass);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("100000") //
				.build();
		final VesselAvailability vesselAvailability2 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("100000") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		Slot load1 = cargo1.getSlots().get(0);
		Slot discharge1 = cargo1.getSlots().get(1);

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability2, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		Slot load2 = cargo2.getSlots().get(0);
		Slot discharge2 = cargo2.getSlots().get(1);

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final SwapSlotMoveHandler handler = injector.getInstance(SwapSlotMoveHandler.class);
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(initialRawSequences);

			final Random random = new Random(0);

			final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(discharge2, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(portSlot);

			final GuideMoveGeneratorOptions options = GuideMoveGeneratorOptions.createDefault();

			@NonNull
			final Collection<ISequenceElement> forbiddenElements = Collections.emptySet();
			final Function<ISequenceElement, Slot> slotMapper = e -> {
				final IPortSlot ps = portSlotProvider.getPortSlot(e);
				return modelEntityMap.getModelObjectNullChecked(ps, Slot.class);
			};

			@Nullable
			final Pair<IMove, Hints> movePair = handler.handleMove(lookupManager, element, random, options, forbiddenElements);

			Assert.assertNotNull(movePair);

			final ModifiableSequences result = new ModifiableSequences(initialRawSequences);
			movePair.getFirst().apply(result);

			final IVesselAvailability o_vesselAvailability1 = modelEntityMap.getOptimiserObjectNullChecked(vesselAvailability1, IVesselAvailability.class);
			final IVesselAvailability o_vesselAvailability2 = modelEntityMap.getOptimiserObjectNullChecked(vesselAvailability2, IVesselAvailability.class);

			final IResource resource1 = vesselProvider.getResource(o_vesselAvailability1);
			final IResource resource2 = vesselProvider.getResource(o_vesselAvailability2);

			// Check expectations
			Assert.assertEquals(4, result.getSequence(resource1).size());
			Assert.assertEquals(4, result.getSequence(resource2).size());
			Assert.assertEquals(0, result.getUnusedElements().size());

			Assert.assertSame(load1, slotMapper.apply(result.getSequence(resource1).get(1)));
			Assert.assertSame(discharge2, slotMapper.apply(result.getSequence(resource1).get(2)));
			Assert.assertSame(load2, slotMapper.apply(result.getSequence(resource2).get(1)));
			Assert.assertSame(discharge1, slotMapper.apply(result.getSequence(resource2).get(2)));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSwapCargoLoadWithUsedLoad() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel1 = fleetModelBuilder.createVessel("My Vessel 1", vesselClass);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("100000") //
				.build();
		final VesselAvailability vesselAvailability2 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("100000") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		Slot load1 = cargo1.getSlots().get(0);
		Slot discharge1 = cargo1.getSlots().get(1);

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability2, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		Slot load2 = cargo2.getSlots().get(0);
		Slot discharge2 = cargo2.getSlots().get(1);

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final SwapSlotMoveHandler handler = injector.getInstance(SwapSlotMoveHandler.class);
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(initialRawSequences);

			final Random random = new Random(0);

			final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(load1, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(portSlot);

			final GuideMoveGeneratorOptions options = GuideMoveGeneratorOptions.createDefault();

			@NonNull
			final Collection<ISequenceElement> forbiddenElements = Collections.emptySet();
			final Function<ISequenceElement, Slot> slotMapper = e -> {
				final IPortSlot ps = portSlotProvider.getPortSlot(e);
				return modelEntityMap.getModelObjectNullChecked(ps, Slot.class);
			};

			@Nullable
			final Pair<IMove, Hints> movePair = handler.handleMove(lookupManager, element, random, options, forbiddenElements);

			Assert.assertNotNull(movePair);

			final ModifiableSequences result = new ModifiableSequences(initialRawSequences);
			movePair.getFirst().apply(result);

			final IVesselAvailability o_vesselAvailability1 = modelEntityMap.getOptimiserObjectNullChecked(vesselAvailability1, IVesselAvailability.class);
			final IVesselAvailability o_vesselAvailability2 = modelEntityMap.getOptimiserObjectNullChecked(vesselAvailability2, IVesselAvailability.class);

			final IResource resource1 = vesselProvider.getResource(o_vesselAvailability1);
			final IResource resource2 = vesselProvider.getResource(o_vesselAvailability2);

			// Check expectations
			Assert.assertEquals(4, result.getSequence(resource1).size());
			Assert.assertEquals(4, result.getSequence(resource2).size());
			Assert.assertEquals(0, result.getUnusedElements().size());

			Assert.assertSame(load2, slotMapper.apply(result.getSequence(resource1).get(1)));
			Assert.assertSame(discharge1, slotMapper.apply(result.getSequence(resource1).get(2)));
			Assert.assertSame(load1, slotMapper.apply(result.getSequence(resource2).get(1)));
			Assert.assertSame(discharge2, slotMapper.apply(result.getSequence(resource2).get(2)));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSwapCargoLoadWithUnusedLoad_WiringLocked() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel1 = fleetModelBuilder.createVessel("My Vessel 1", vesselClass);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("100000") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withOptional(true) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		Slot load1 = cargo1.getSlots().get(0);
		Slot discharge1 = cargo1.getSlots().get(1);

		final LoadSlot load2 = cargoModelBuilder //
				.makeFOBPurchase("L2", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.6) //
				.withOptional(true) //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final SwapSlotMoveHandler handler = injector.getInstance(SwapSlotMoveHandler.class);
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(initialRawSequences);

			final Random random = new Random(0);

			final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(load2, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(portSlot);

			final GuideMoveGeneratorOptions options = GuideMoveGeneratorOptions.createDefault();

			@NonNull
			final Collection<ISequenceElement> forbiddenElements = Collections.emptySet();
			final Function<ISequenceElement, Slot> slotMapper = e -> {
				final IPortSlot ps = portSlotProvider.getPortSlot(e);
				return modelEntityMap.getModelObjectNullChecked(ps, Slot.class);
			};

			@Nullable
			final Pair<IMove, Hints> movePair = handler.handleMove(lookupManager, element, random, options, forbiddenElements);

			Assert.assertNull(movePair);
		});
	}
}