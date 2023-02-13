/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuideMoveGeneratorOptions;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.guided.Hints;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.SwapCargoVesselMoveHandler;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * Tests for the {@link GuidedMoveGenerator}
 *
 */
@ExtendWith(ShiroRunner.class)
public class SwapCargoVesselMoveHandlerTests extends AbstractMoveHandlerTest {

	/**
	 * Make sure we can move between vessels. Second vessel has cheaper hire cost.
	 * 
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSimpleCargoMove() {

		final Vessel source = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel1 = fleetModelBuilder.createVesselFrom("My Vessel 1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("My Vessel 2", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());

		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel1, entity) //
				.withCharterRate("100000") //
				.build();
		final VesselCharter vesselCharter2 = cargoModelBuilder.makeVesselCharter(vessel2, entity) //
				.withCharterRate("10000") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselCharter1, 1) //
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

			Assertions.assertNotNull(movePair);

			final ModifiableSequences result = new ModifiableSequences(initialRawSequences);
			movePair.getFirst().apply(result);

			final IVesselCharter o_vesselCharter1 = modelEntityMap.getOptimiserObjectNullChecked(vesselCharter1, IVesselCharter.class);
			final IVesselCharter o_vesselCharter2 = modelEntityMap.getOptimiserObjectNullChecked(vesselCharter2, IVesselCharter.class);

			final IResource resource1 = vesselProvider.getResource(o_vesselCharter1);
			final IResource resource2 = vesselProvider.getResource(o_vesselCharter2);

			// Check expectations
			Assertions.assertEquals(2, result.getSequence(resource1).size());
			Assertions.assertEquals(4, result.getSequence(resource2).size());
			Assertions.assertEquals(0, result.getUnusedElements().size());

			Assertions.assertSame(cargo1.getSlots().get(0), slotMapper.apply(result.getSequence(resource2).get(1)));
			Assertions.assertSame(cargo1.getSlots().get(1), slotMapper.apply(result.getSequence(resource2).get(2)));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testNominalCargoMove_ThereAndBackAgain() {

		final Vessel source = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel1 = fleetModelBuilder.createVesselFrom("My Vessel 1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("My Vessel 2", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());

		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel1, entity) //
				.withCharterRate("100000") //
				.build();

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", source, entity, "50000", 0);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		// Prompt is before this data.
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2014, 10, 1), LocalDate.of(2014, 12, 5));

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

			// Move 1. nominal to fleet
			{
				lookupManager.createLookup(initialRawSequences);
				@Nullable
				final Pair<IMove, Hints> movePair = handler.handleMove(lookupManager, element, random, options, forbiddenElements);
				Assertions.assertNotNull(movePair);

				final ModifiableSequences result = new ModifiableSequences(initialRawSequences);
				movePair.getFirst().apply(result);

				final IVesselCharter o_vesselCharter1 = modelEntityMap.getOptimiserObjectNullChecked(vesselCharter1, IVesselCharter.class);

				Assertions.assertEquals(2, result.getResources().size());
				final IResource resource1 = vesselProvider.getResource(o_vesselCharter1);
				Assertions.assertSame(resource1, result.getResources().get(0));

				// This should be our nominal market
				final IResource resource2 = result.getResources().get(1);

				// Check expectations
				Assertions.assertEquals(4, result.getSequence(resource1).size());
				Assertions.assertEquals(2, result.getSequence(resource2).size());
				Assertions.assertEquals(0, result.getUnusedElements().size());

				Assertions.assertSame(cargo1.getSlots().get(0), slotMapper.apply(result.getSequence(resource1).get(1)));
				Assertions.assertSame(cargo1.getSlots().get(1), slotMapper.apply(result.getSequence(resource1).get(2)));

				// Prep for move 2
				lookupManager.createLookup(result);

			}
			// Move 2. fleet to nominal
			{
				final Pair<IMove, Hints> movePair = handler.handleMove(lookupManager, element, random, options, forbiddenElements);
				Assertions.assertNotNull(movePair);

				final ModifiableSequences result = new ModifiableSequences(lookupManager.getRawSequences());
				movePair.getFirst().apply(result);

				final IVesselCharter o_vesselCharter1 = modelEntityMap.getOptimiserObjectNullChecked(vesselCharter1, IVesselCharter.class);

				Assertions.assertEquals(2, result.getResources().size());
				final IResource resource1 = vesselProvider.getResource(o_vesselCharter1);
				Assertions.assertSame(resource1, result.getResources().get(0));

				// This should be our nominal market
				final IResource resource2 = result.getResources().get(1);

				// Check expectations
				Assertions.assertEquals(2, result.getSequence(resource1).size());
				Assertions.assertEquals(4, result.getSequence(resource2).size());
				Assertions.assertEquals(0, result.getUnusedElements().size());

				Assertions.assertSame(cargo1.getSlots().get(0), slotMapper.apply(result.getSequence(resource2).get(1)));
				Assertions.assertSame(cargo1.getSlots().get(1), slotMapper.apply(result.getSequence(resource2).get(2)));
			}
		});
	}

	/**
	 * Make sure we cannot move DES Purchase to a vessel
	 * 
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCannotMoveDESPurchase() {

		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel1, entity) //
				.withCharterRate("10000") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "5", 22.8, null) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
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

			Assertions.assertNull(movePair);

		});
	}

	/**
	 * Make sure we cannot move DES Purchase to a vessel
	 * 
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCannotMoveFOBSale() {
		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel1, entity) //
				.withCharterRate("10000") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeFOBSale("D1", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7", null) //
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

			Assertions.assertNull(movePair);

		});
	}

	/**
	 * Make sure we can move between vessels in between another cargo. Dates are nicely spaced out.
	 * 
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testInsertCargoMove() {

		final Vessel source = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final Vessel vessel1 = fleetModelBuilder.createVesselFrom("My Vessel 1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("My Vessel 2", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());

		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel1, entity) //
				.withCharterRate("100000") //
				.build();
		final VesselCharter vesselCharter2 = cargoModelBuilder.makeVesselCharter(vessel2, entity) //
				.withCharterRate("10000") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2016, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2016, 2, 15), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselCharter1, 1) //
				.withAssignmentFlags(false, false) //
				.build();
		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2016, 1, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2016, 1, 15), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselCharter2, 1) //
				.withAssignmentFlags(false, false) //
				.build();
		final Cargo cargo3 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L3", LocalDate.of(2016, 3, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D3", LocalDate.of(2016, 3, 15), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselCharter2, 2) //
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

			Assertions.assertNotNull(movePair);

			final ModifiableSequences result = new ModifiableSequences(initialRawSequences);
			movePair.getFirst().apply(result);

			final IVesselCharter o_vesselCharter1 = modelEntityMap.getOptimiserObjectNullChecked(vesselCharter1, IVesselCharter.class);
			final IVesselCharter o_vesselCharter2 = modelEntityMap.getOptimiserObjectNullChecked(vesselCharter2, IVesselCharter.class);

			final IResource resource1 = vesselProvider.getResource(o_vesselCharter1);
			final IResource resource2 = vesselProvider.getResource(o_vesselCharter2);

			// Check expectations
			Assertions.assertEquals(2, result.getSequence(resource1).size());
			Assertions.assertEquals(8, result.getSequence(resource2).size());
			Assertions.assertEquals(0, result.getUnusedElements().size());

			Assertions.assertSame(cargo2.getSlots().get(0), slotMapper.apply(result.getSequence(resource2).get(1)));
			Assertions.assertSame(cargo2.getSlots().get(1), slotMapper.apply(result.getSequence(resource2).get(2)));
			Assertions.assertSame(cargo1.getSlots().get(0), slotMapper.apply(result.getSequence(resource2).get(3)));
			Assertions.assertSame(cargo1.getSlots().get(1), slotMapper.apply(result.getSequence(resource2).get(4)));
			Assertions.assertSame(cargo3.getSlots().get(0), slotMapper.apply(result.getSequence(resource2).get(5)));
			Assertions.assertSame(cargo3.getSlots().get(1), slotMapper.apply(result.getSequence(resource2).get(6)));
		});
	}

	/**
	 * Make sure we can move between vessels in between another cargo. Dates are nicely spaced out.
	 * 
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testInsertCargoMove_FailsDueToVesselDate() {

		final Vessel source = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel1 = fleetModelBuilder.createVesselFrom("My Vessel 1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("My Vessel 2", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());

		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel1, entity) //
				.withCharterRate("100000") //
				.build();

		// End date cannot accommodate the cargo
		final VesselCharter vesselCharter2 = cargoModelBuilder.makeVesselCharter(vessel2, entity) //
				.withEndWindow(LocalDateTime.of(2016, 2, 15, 0, 0, 0), LocalDateTime.of(2016, 2, 15, 0, 0, 0)) //
				.withCharterRate("10000") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2016, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2016, 2, 15), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselCharter1, 1) //
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

			Assertions.assertNull(movePair);
		});
	}

	/**
	 * Make sure we can move between vessels in between another cargo. Dates are nicely spaced out.
	 * 
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testInsertCargoMove_FailsDueToSlotVesselRestriction() {

		final Vessel source = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel1 = fleetModelBuilder.createVesselFrom("My Vessel 1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("My Vessel 2", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());

		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel1, entity) //
				.withCharterRate("100000") //
				.build();

		final VesselCharter vesselCharter2 = cargoModelBuilder.makeVesselCharter(vessel2, entity) //
				.withCharterRate("10000") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2016, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2016, 2, 15), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				// Only allow vessel 1
				.withRestrictedVessels(vessel1, true) //
				.build() //
				.withVesselAssignment(vesselCharter1, 1) //
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

			Assertions.assertNull(movePair);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSwapWithinSequence() {

		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel1, entity) //
				.withCharterRate("100000") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withWindowSize(3, TimePeriod.MONTHS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withWindowSize(3, TimePeriod.MONTHS) //
				.build() //
				.withVesselAssignment(vesselCharter1, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2017, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withWindowSize(3, TimePeriod.MONTHS) //
				.build() //
				.makeDESSale("D2", LocalDate.of(2017, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withWindowSize(3, TimePeriod.MONTHS) //
				.build() //
				.withVesselAssignment(vesselCharter1, 2) //
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

			Assertions.assertNotNull(movePair);

			final ModifiableSequences result = new ModifiableSequences(initialRawSequences);
			movePair.getFirst().apply(result);

			final IVesselCharter o_vesselCharter1 = modelEntityMap.getOptimiserObjectNullChecked(vesselCharter1, IVesselCharter.class);

			final IResource resource1 = vesselProvider.getResource(o_vesselCharter1);

			// Check expectations
			Assertions.assertEquals(6, result.getSequence(resource1).size());

			Assertions.assertSame(load2, result.getSequence(resource1).get(1));
			Assertions.assertSame(discharge2, result.getSequence(resource1).get(2));
			Assertions.assertSame(load1, result.getSequence(resource1).get(3));
			Assertions.assertSame(discharge1, result.getSequence(resource1).get(4));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSwapEventWithinSequence() {

		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel1, entity) //
				.withCharterRate("100000") //
				.build();

		final CharterOutEvent event = cargoModelBuilder
				.makeCharterOutEvent("CO1", LocalDateTime.of(2017, 2, 1, 0, 0, 0), LocalDateTime.of(2017, 5, 1, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withRelocatePort(portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT)) //
				.withVesselAssignment(vesselCharter1, 1) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2017, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withWindowSize(3, TimePeriod.MONTHS) //
				.build() //
				.makeDESSale("D2", LocalDate.of(2017, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withWindowSize(3, TimePeriod.MONTHS) //
				.build() //
				.withVesselAssignment(vesselCharter1, 2) //
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

			final ISequenceElement event1 = portSlotProvider.getElement(modelEntityMap.getOptimiserObjectNullChecked(event, IPortSlot.class));
			final ISequenceElement load2 = portSlotProvider.getElement(modelEntityMap.getOptimiserObjectNullChecked(cargo2.getSlots().get(0), IPortSlot.class));
			final ISequenceElement discharge2 = portSlotProvider.getElement(modelEntityMap.getOptimiserObjectNullChecked(cargo2.getSlots().get(1), IPortSlot.class));

			final GuideMoveGeneratorOptions options = GuideMoveGeneratorOptions.createDefault();

			@Nullable
			final Pair<IMove, Hints> movePair = handler.handleMove(lookupManager, event1, random, options, Collections.emptySet());

			Assertions.assertNotNull(movePair);

			final ModifiableSequences result = new ModifiableSequences(initialRawSequences);
			movePair.getFirst().apply(result);

			final IVesselCharter o_vesselCharter1 = modelEntityMap.getOptimiserObjectNullChecked(vesselCharter1, IVesselCharter.class);

			final IResource resource1 = vesselProvider.getResource(o_vesselCharter1);

			// Check expectations (2 start/end, 2 cargo, 3 redirected charter out event)
			Assertions.assertEquals(7, result.getSequence(resource1).size());

			Assertions.assertSame(load2, result.getSequence(resource1).get(1));
			Assertions.assertSame(discharge2, result.getSequence(resource1).get(2));
			Assertions.assertTrue(result.getSequence(resource1).get(3).getName().contains("CO1"));
			Assertions.assertTrue(result.getSequence(resource1).get(4).getName().contains("CO1"));
			Assertions.assertTrue(result.getSequence(resource1).get(5).getName().contains("CO1"));
		});
	}
}