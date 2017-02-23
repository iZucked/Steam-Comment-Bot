/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.moves;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.SwapCargoVesselMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.SwapSlotMoveHandler;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * Tests for the {@link GuidedMoveGenerator}
 *
 */
@RunWith(value = ShiroRunner.class)
public class SwapCargoVesselMoveHandlerTests extends AbstractMoveHandlerTest {

	/**
	 * Make sure we can move between vessels. Second vessel has cheaper hire cost.
	 * 
	 */
	@Test
	@Category({ MicroTest.class })
	public void testSimpleCargoMove() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel1 = fleetModelBuilder.createVessel("My Vessel 1", vesselClass);
		final Vessel vessel2 = fleetModelBuilder.createVessel("My Vessel 2", vesselClass);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("100000") //
				.build();
		final VesselAvailability vesselAvailability2 = cargoModelBuilder.makeVesselAvailability(vessel2, entity) //
				.withCharterRate("10000") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final SwapCargoVesselMoveHandler handler = injector.getInstance(SwapCargoVesselMoveHandler.class);
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(initialRawSequences);

			final Random random = new Random(0);

			final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(cargo1.getSlots().get(0), IPortSlot.class);
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
			Assert.assertEquals(2, result.getSequence(resource1).size());
			Assert.assertEquals(4, result.getSequence(resource2).size());
			Assert.assertEquals(0, result.getUnusedElements().size());

			Assert.assertSame(cargo1.getSlots().get(0), slotMapper.apply(result.getSequence(resource2).get(1)));
			Assert.assertSame(cargo1.getSlots().get(1), slotMapper.apply(result.getSequence(resource2).get(2)));
		});
	}

	/**
	 * Make sure we cannot move DES Purchase to a vessel
	 * 
	 */
	@Test
	@Category({ MicroTest.class })
	public void testCannotMoveDESPurchase() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel1 = fleetModelBuilder.createVessel("My Vessel 1", vesselClass);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("10000") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", false, LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "5", null) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final SwapCargoVesselMoveHandler handler = injector.getInstance(SwapCargoVesselMoveHandler.class);
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(initialRawSequences);

			final Random random = new Random(0);

			final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(cargo1.getSlots().get(0), IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(portSlot);

			final GuideMoveGeneratorOptions options = GuideMoveGeneratorOptions.createDefault();

			@NonNull
			final Collection<ISequenceElement> forbiddenElements = Collections.emptySet();

			@Nullable
			final Pair<IMove, Hints> movePair = handler.handleMove(lookupManager, element, random, options, forbiddenElements);

			Assert.assertNull(movePair);

		});
	}

	/**
	 * Make sure we cannot move DES Purchase to a vessel
	 * 
	 */
	@Test
	@Category({ MicroTest.class })
	public void testCannotMoveFOBSale() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel1 = fleetModelBuilder.createVessel("My Vessel 1", vesselClass);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("10000") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 11), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeFOBSale("D1", false, LocalDate.of(2015, 12, 11), portFinder.findPort("Point Fortin"), null, entity, "7", null) //
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final SwapCargoVesselMoveHandler handler = injector.getInstance(SwapCargoVesselMoveHandler.class);
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(initialRawSequences);

			final Random random = new Random(0);

			final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(cargo1.getSlots().get(0), IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(portSlot);

			final GuideMoveGeneratorOptions options = GuideMoveGeneratorOptions.createDefault();

			@NonNull
			final Collection<ISequenceElement> forbiddenElements = Collections.emptySet();

			@Nullable
			final Pair<IMove, Hints> movePair = handler.handleMove(lookupManager, element, random, options, forbiddenElements);

			Assert.assertNull(movePair);

		});
	}

	/**
	 * Make sure we can move between vessels in between another cargo. Dates are nicely spaced out.
	 * 
	 */
	@Test
	@Category({ MicroTest.class })
	public void testInsertCargoMove() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel1 = fleetModelBuilder.createVessel("My Vessel 1", vesselClass);
		final Vessel vessel2 = fleetModelBuilder.createVessel("My Vessel 2", vesselClass);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("100000") //
				.build();
		final VesselAvailability vesselAvailability2 = cargoModelBuilder.makeVesselAvailability(vessel2, entity) //
				.withCharterRate("10000") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2016, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2016, 2, 15), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) //
				.withAssignmentFlags(false, false) //
				.build();
		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2016, 1, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2016, 1, 15), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability2, 1) //
				.withAssignmentFlags(false, false) //
				.build();
		final Cargo cargo3 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L3", LocalDate.of(2016, 3, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D3", LocalDate.of(2016, 3, 15), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability2, 2) //
				.withAssignmentFlags(false, false) //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final SwapCargoVesselMoveHandler handler = injector.getInstance(SwapCargoVesselMoveHandler.class);
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(initialRawSequences);

			final Random random = new Random(0);

			final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(cargo1.getSlots().get(0), IPortSlot.class);
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
			Assert.assertEquals(2, result.getSequence(resource1).size());
			Assert.assertEquals(8, result.getSequence(resource2).size());
			Assert.assertEquals(0, result.getUnusedElements().size());

			Assert.assertSame(cargo2.getSlots().get(0), slotMapper.apply(result.getSequence(resource2).get(1)));
			Assert.assertSame(cargo2.getSlots().get(1), slotMapper.apply(result.getSequence(resource2).get(2)));
			Assert.assertSame(cargo1.getSlots().get(0), slotMapper.apply(result.getSequence(resource2).get(3)));
			Assert.assertSame(cargo1.getSlots().get(1), slotMapper.apply(result.getSequence(resource2).get(4)));
			Assert.assertSame(cargo3.getSlots().get(0), slotMapper.apply(result.getSequence(resource2).get(5)));
			Assert.assertSame(cargo3.getSlots().get(1), slotMapper.apply(result.getSequence(resource2).get(6)));
		});
	}

	/**
	 * Make sure we can move between vessels in between another cargo. Dates are nicely spaced out.
	 * 
	 */
	@Test
	@Category({ MicroTest.class })
	public void testInsertCargoMove_FailsDueToVesselDate() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel1 = fleetModelBuilder.createVessel("My Vessel 1", vesselClass);
		final Vessel vessel2 = fleetModelBuilder.createVessel("My Vessel 2", vesselClass);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("100000") //
				.build();

		// End date cannot accommodate the cargo
		final VesselAvailability vesselAvailability2 = cargoModelBuilder.makeVesselAvailability(vessel2, entity) //
				.withEndWindow(LocalDateTime.of(2016, 2, 15, 0, 0, 0), LocalDateTime.of(2016, 2, 15, 0, 0, 0)) //
				.withCharterRate("10000") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2016, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2016, 2, 15), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final SwapCargoVesselMoveHandler handler = injector.getInstance(SwapCargoVesselMoveHandler.class);
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(initialRawSequences);

			final Random random = new Random(0);

			final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(cargo1.getSlots().get(0), IPortSlot.class);
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

	/**
	 * Make sure we can move between vessels in between another cargo. Dates are nicely spaced out.
	 * 
	 */
	@Test
	@Category({ MicroTest.class })
	public void testInsertCargoMove_FailsDueToSlotVesselRestriction() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel1 = fleetModelBuilder.createVessel("My Vessel 1", vesselClass);
		final Vessel vessel2 = fleetModelBuilder.createVessel("My Vessel 2", vesselClass);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("100000") //
				.build();

		final VesselAvailability vesselAvailability2 = cargoModelBuilder.makeVesselAvailability(vessel2, entity) //
				.withCharterRate("10000") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2016, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2016, 2, 15), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				// Only allow vessel 1
				.withAllowedVessels(vessel1) //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final SwapCargoVesselMoveHandler handler = injector.getInstance(SwapCargoVesselMoveHandler.class);
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(initialRawSequences);

			final Random random = new Random(0);

			final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(cargo1.getSlots().get(0), IPortSlot.class);
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
	public void testSwapWithinSequence() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel1 = fleetModelBuilder.createVessel("My Vessel 1", vesselClass);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("100000") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withWindowSize(3, TimePeriod.MONTHS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 2, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withWindowSize(3, TimePeriod.MONTHS) //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2017, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withWindowSize(3, TimePeriod.MONTHS) //
				.build() //
				.makeDESSale("D2", LocalDate.of(2017, 2, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
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

			final SwapCargoVesselMoveHandler handler = injector.getInstance(SwapCargoVesselMoveHandler.class);
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

			Assert.assertSame(load2, result.getSequence(resource1).get(1));
			Assert.assertSame(discharge2, result.getSequence(resource1).get(2));
			Assert.assertSame(load1, result.getSequence(resource1).get(3));
			Assert.assertSame(discharge1, result.getSequence(resource1).get(4));
		});
	}
}