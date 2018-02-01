/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
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
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
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
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.InsertSegmentMoveHandler;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;

/**
 * Tests for the {@link GuidedMoveGenerator}
 *
 */
@RunWith(value = ShiroRunner.class)
public class InsertCargoVesselMoveHandlerTests extends AbstractMoveHandlerTest {

	/**
	 * Make sure we can insert a cargo onto an empty vessel.
	 * 
	 */
	@Test
	@Category({ MicroTest.class })
	public void testSimpleInsertCargoMove() throws Exception {

		final Vessel vessel1 = fleetModelFinder.findVessel("STEAM-145");

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("100000") //
				.build();

		final LoadSlot load1 = cargoModelBuilder//
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.6) //
				.build();
		//
		final DischargeSlot discharge1 = cargoModelBuilder//
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final InsertSegmentMoveHandler handler = injector.getInstance(InsertSegmentMoveHandler.class);
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

			final IResource resource1 = vesselProvider.getResource(o_vesselAvailability1);

			// Check expectations
			Assert.assertEquals(4, result.getSequence(resource1).size());
			Assert.assertEquals(0, result.getUnusedElements().size());

			Assert.assertSame(load1, slotMapper.apply(result.getSequence(resource1).get(1)));
			Assert.assertSame(discharge1, slotMapper.apply(result.getSequence(resource1).get(2)));
		});
	}

	/**
	 * Insert DES Sale onto DES Purchase slots is supported.
	 * 
	 */
	@Test
	@Category({ MicroTest.class })
	public void testInsertDESPurchaseMove_DischargeSide() throws Exception {

		final Vessel vessel1 = fleetModelFinder.findVessel("STEAM-145");

		final LoadSlot load1 = cargoModelBuilder//
				.makeDESPurchase("L1", false, LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "5", 22.6, null) //
				.build();
		//
		final DischargeSlot discharge1 = cargoModelBuilder//
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final IVirtualVesselSlotProvider virtualVesselSlotProvider = injector.getInstance(IVirtualVesselSlotProvider.class);

			final InsertSegmentMoveHandler handler = injector.getInstance(InsertSegmentMoveHandler.class);
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(initialRawSequences);

			final Random random = new Random(0);

			final IPortSlot load_portSlot = modelEntityMap.getOptimiserObjectNullChecked(load1, IPortSlot.class);
			final ISequenceElement load_element = portSlotProvider.getElement(load_portSlot);
			final IPortSlot discharge_portSlot = modelEntityMap.getOptimiserObjectNullChecked(discharge1, IPortSlot.class);
			final ISequenceElement discharge_element = portSlotProvider.getElement(discharge_portSlot);

			final GuideMoveGeneratorOptions options = GuideMoveGeneratorOptions.createDefault();

			@NonNull
			final Collection<ISequenceElement> forbiddenElements = Collections.emptySet();
			final Function<ISequenceElement, Slot> slotMapper = e -> {
				final IPortSlot ps = portSlotProvider.getPortSlot(e);
				return modelEntityMap.getModelObjectNullChecked(ps, Slot.class);
			};

			@Nullable
			final Pair<IMove, Hints> movePair = handler.handleMove(lookupManager, discharge_element, random, options, forbiddenElements);

			Assert.assertNotNull(movePair);

			final ModifiableSequences result = new ModifiableSequences(initialRawSequences);
			movePair.getFirst().apply(result);

			final IVesselAvailability o_vesselAvailability1 = virtualVesselSlotProvider.getVesselAvailabilityForElement(load_element);

			final IResource resource1 = vesselProvider.getResource(o_vesselAvailability1);

			// Check expectations
			Assert.assertEquals(4, result.getSequence(resource1).size());
			Assert.assertEquals(0, result.getUnusedElements().size());

			Assert.assertSame(load1, slotMapper.apply(result.getSequence(resource1).get(1)));
			Assert.assertSame(discharge1, slotMapper.apply(result.getSequence(resource1).get(2)));
		});
	}

	/**
	 * DES Purchase slots not supported.
	 * 
	 */
	@Test(expected = IllegalArgumentException.class)
	@Category({ MicroTest.class })
	public void testInsertDESPurchaseMove_LoadSide() throws Exception {

		final Vessel vessel1 = fleetModelFinder.findVessel("STEAM-145");

		final LoadSlot load1 = cargoModelBuilder//
				.makeDESPurchase("L1", false, LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "5", 22.6, null) //
				.build();
		//
		final DischargeSlot discharge1 = cargoModelBuilder//
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final InsertSegmentMoveHandler handler = injector.getInstance(InsertSegmentMoveHandler.class);
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

			Assert.fail("Expected exception");
		});
	}

	/**
	 * FOB Purchases onto FOB Sale Purchase slots are supported.
	 * 
	 */
	@Test
	@Category({ MicroTest.class })
	public void testInsertFOBSaleMove_LoadSide() throws Exception {

		final Vessel vessel1 = fleetModelFinder.findVessel("STEAM-145");

		final LoadSlot load1 = cargoModelBuilder//
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 11), portFinder.findPort("Point Fortin"), null, entity, "5", 22.6) //
				.build();
		//
		final DischargeSlot discharge1 = cargoModelBuilder//
				.makeFOBSale("D1", false, LocalDate.of(2015, 12, 11), portFinder.findPort("Point Fortin"), null, entity, "7", null) //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);
			final IVirtualVesselSlotProvider virtualVesselSlotProvider = injector.getInstance(IVirtualVesselSlotProvider.class);

			final InsertSegmentMoveHandler handler = injector.getInstance(InsertSegmentMoveHandler.class);
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(initialRawSequences);

			final Random random = new Random(0);

			final IPortSlot load_portSlot = modelEntityMap.getOptimiserObjectNullChecked(load1, IPortSlot.class);
			final ISequenceElement load_element = portSlotProvider.getElement(load_portSlot);
			final IPortSlot discharge_portSlot = modelEntityMap.getOptimiserObjectNullChecked(discharge1, IPortSlot.class);
			final ISequenceElement discharge_element = portSlotProvider.getElement(discharge_portSlot);

			final GuideMoveGeneratorOptions options = GuideMoveGeneratorOptions.createDefault();

			@NonNull
			final Collection<ISequenceElement> forbiddenElements = Collections.emptySet();
			final Function<ISequenceElement, Slot> slotMapper = e -> {
				final IPortSlot ps = portSlotProvider.getPortSlot(e);
				return modelEntityMap.getModelObjectNullChecked(ps, Slot.class);
			};

			@Nullable
			final Pair<IMove, Hints> movePair = handler.handleMove(lookupManager, load_element, random, options, forbiddenElements);

			Assert.assertNotNull(movePair);

			final ModifiableSequences result = new ModifiableSequences(initialRawSequences);
			movePair.getFirst().apply(result);

			final IVesselAvailability o_vesselAvailability1 = virtualVesselSlotProvider.getVesselAvailabilityForElement(discharge_element);

			final IResource resource1 = vesselProvider.getResource(o_vesselAvailability1);

			// Check expectations
			Assert.assertEquals(4, result.getSequence(resource1).size());
			Assert.assertEquals(0, result.getUnusedElements().size());

			Assert.assertSame(load1, slotMapper.apply(result.getSequence(resource1).get(1)));
			Assert.assertSame(discharge1, slotMapper.apply(result.getSequence(resource1).get(2)));
		});
	}

	/**
	 * FOB Sale Purchase slots not supported.
	 * 
	 */
	@Test(expected = IllegalArgumentException.class)
	@Category({ MicroTest.class })
	public void testInsertFOBSaleMove_DischargeSide() throws Exception {

		final Vessel vessel1 = fleetModelFinder.findVessel("STEAM-145");

		final LoadSlot load1 = cargoModelBuilder//
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 11), portFinder.findPort("Point Fortin"), null, entity, "5", 22.6) //
				.build();
		//
		final DischargeSlot discharge1 = cargoModelBuilder//
				.makeFOBSale("D1", false, LocalDate.of(2015, 12, 11), portFinder.findPort("Point Fortin"), null, entity, "7", null) //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final InsertSegmentMoveHandler handler = injector.getInstance(InsertSegmentMoveHandler.class);
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(initialRawSequences);

			final Random random = new Random(0);

			final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(discharge1, IPortSlot.class);
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

			Assert.fail("Expected exception");
		});
	}

	/**
	 * Make sure we cannot insert the load onto a DES Purchase resource
	 * 
	 */
	@Test
	@Category({ MicroTest.class })
	public void testCannotInsertFleetOntoDESResource() throws Exception {

		final LoadSlot load1 = cargoModelBuilder//
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.6) //
				.build();
		//
		final DischargeSlot discharge1 = cargoModelBuilder//
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build();

		final LoadSlot load2 = cargoModelBuilder//
				.makeDESPurchase("L2", false, LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "5", 22.6, null) //
				.build();
		//

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final InsertSegmentMoveHandler handler = injector.getInstance(InsertSegmentMoveHandler.class);
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

			Assert.assertNull(movePair);

		});
	}

	/**
	 * Make sure we cannot insert the load onto a FOB Sale resource
	 * 
	 */
	@Test
	@Category({ MicroTest.class })
	public void testCannotInsertFleetOntoFOBResource() throws Exception {

		final LoadSlot load1 = cargoModelBuilder//
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.6) //
				.build();
		//
		final DischargeSlot discharge1 = cargoModelBuilder//
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build();

		final DischargeSlot discharge = cargoModelBuilder//
				.makeFOBSale("D2", false, LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", null) //
				.build();
		//

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final InsertSegmentMoveHandler handler = injector.getInstance(InsertSegmentMoveHandler.class);
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(initialRawSequences);

			final Random random = new Random(0);

			// Select the discharge
			final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(discharge1, IPortSlot.class);
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
	public void testInsertCargoMove() throws Exception {

		final Vessel source = fleetModelFinder.findVessel("STEAM-145");
		final Vessel vessel1 = fleetModelBuilder.createVesselFrom("My Vessel 1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("My Vessel 2", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("100000") //
				.build();

		final LoadSlot load1 = cargoModelBuilder //
				.makeFOBPurchase("L1", LocalDate.of(2016, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "5", 22.6) //
				.build();
		final DischargeSlot discharge1 = cargoModelBuilder //
				.makeDESSale("D1", LocalDate.of(2016, 2, 15), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2016, 1, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2016, 1, 15), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) //
				.withAssignmentFlags(false, false) //
				.build();
		final Cargo cargo3 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L3", LocalDate.of(2016, 3, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D3", LocalDate.of(2016, 3, 15), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability1, 2) //
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

			final InsertSegmentMoveHandler handler = injector.getInstance(InsertSegmentMoveHandler.class);
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
			boolean appliedMove = false;
			for (int i = 0; i < 5; ++i) {
				@Nullable
				final Pair<IMove, Hints> movePair = handler.handleMove(lookupManager, element, random, options, forbiddenElements);
				Assert.assertNotNull(movePair);
				
				// Inserting may produce a problem, so retry until we get a valid move as expected below.
				if (!movePair.getSecond().getProblemElements().isEmpty()) {
					continue;
				}

				final ModifiableSequences result = new ModifiableSequences(initialRawSequences);
				movePair.getFirst().apply(result);

				final IVesselAvailability o_vesselAvailability1 = modelEntityMap.getOptimiserObjectNullChecked(vesselAvailability1, IVesselAvailability.class);

				final IResource resource1 = vesselProvider.getResource(o_vesselAvailability1);

				// Check expectations
				Assert.assertEquals(8, result.getSequence(resource1).size());
				Assert.assertEquals(0, result.getUnusedElements().size());

				Assert.assertSame(cargo2.getSlots().get(0), slotMapper.apply(result.getSequence(resource1).get(1)));
				Assert.assertSame(cargo2.getSlots().get(1), slotMapper.apply(result.getSequence(resource1).get(2)));
				Assert.assertSame(load1, slotMapper.apply(result.getSequence(resource1).get(3)));
				Assert.assertSame(discharge1, slotMapper.apply(result.getSequence(resource1).get(4)));
				Assert.assertSame(cargo3.getSlots().get(0), slotMapper.apply(result.getSequence(resource1).get(5)));
				Assert.assertSame(cargo3.getSlots().get(1), slotMapper.apply(result.getSequence(resource1).get(6)));
				appliedMove = true;
			}
			Assert.assertTrue(appliedMove);
		});
	}

	/**
	 * Make sure we can move between vessels in between another cargo. Dates are nicely spaced out.
	 * 
	 */
	@Test
	@Category({ MicroTest.class })
	public void testInsertCargoMove_FailsDueToVesselDate() throws Exception {

		final Vessel source = fleetModelFinder.findVessel("STEAM-145");
		final Vessel vessel1 = fleetModelBuilder.createVesselFrom("My Vessel 1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("My Vessel 2", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());

		// End date cannot accommodate the cargo
		final VesselAvailability vesselAvailability2 = cargoModelBuilder.makeVesselAvailability(vessel2, entity) //
				.withEndWindow(LocalDateTime.of(2016, 2, 15, 0, 0, 0), LocalDateTime.of(2016, 2, 15, 0, 0, 0)) //
				.withCharterRate("10000") //
				.build();

		final LoadSlot load1 = cargoModelBuilder //
				.makeFOBPurchase("L1", LocalDate.of(2016, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "5", 22.6) //
				.build();

		final DischargeSlot discharge1 = cargoModelBuilder //
				.makeDESSale("D1", LocalDate.of(2016, 2, 15), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final InsertSegmentMoveHandler handler = injector.getInstance(InsertSegmentMoveHandler.class);
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

		final Vessel source = fleetModelFinder.findVessel("STEAM-145");

		final Vessel vessel1 = fleetModelBuilder.createVesselFrom("My Vessel 1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("My Vessel 2", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());

		final VesselAvailability vesselAvailability2 = cargoModelBuilder.makeVesselAvailability(vessel2, entity) //
				.withCharterRate("10000") //
				.build();

		final LoadSlot load1 = cargoModelBuilder //
				.makeFOBPurchase("L1", LocalDate.of(2016, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "5", 22.6) //
				.build();

		final DischargeSlot discharge1 = cargoModelBuilder //
				.makeDESSale("D1", LocalDate.of(2016, 2, 15), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				// Only allow vessel 1
				.withAllowedVessels(vessel1) //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

			final InsertSegmentMoveHandler handler = injector.getInstance(InsertSegmentMoveHandler.class);
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

			Assert.assertNull(movePair);
		});
	}
}