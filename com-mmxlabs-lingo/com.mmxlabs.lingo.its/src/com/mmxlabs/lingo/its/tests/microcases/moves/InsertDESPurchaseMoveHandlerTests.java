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
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
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
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.InsertDESPurchaseMoveHandler;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;

/**
 * Tests for the {@link GuidedMoveGenerator}
 *
 */
@RunWith(value = ShiroRunner.class)
public class InsertDESPurchaseMoveHandlerTests extends AbstractMoveHandlerTest {

	/**
	 * Make sure we can insert a DES purchase
	 * 
	 */
	@Test
	@Category({ MicroTest.class })
	public void testInsertDESPurchaseMove() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel1 = fleetModelBuilder.createVessel("My Vessel 1", vesselClass);

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

			final InsertDESPurchaseMoveHandler handler = injector.getInstance(InsertDESPurchaseMoveHandler.class);
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
	 * Expect this to fail due to vessel restriction
	 * 
	 */
	@Test
	@Category({ MicroTest.class })
	public void testInsertDESPurchaseMove_Fail_VesselRestriction() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel1 = fleetModelBuilder.createVessel("My Vessel 1", vesselClass);
		final Vessel vessel2 = fleetModelBuilder.createVessel("My Vessel 2", vesselClass);

		final LoadSlot load1 = cargoModelBuilder//
				.makeDESPurchase("L1", false, LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "5", 22.6, vessel1) //
				.build();
		//
		final DischargeSlot discharge1 = cargoModelBuilder//
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withAllowedVessels(vessel2) //
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

			final InsertDESPurchaseMoveHandler handler = injector.getInstance(InsertDESPurchaseMoveHandler.class);
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

			Assert.assertNull(movePair);
		});
	}

	/**
	 * Expect this to fail as time windows do not match
	 * 
	 */
	@Test
	@Category({ MicroTest.class })
	public void testInsertDESPurchaseMove_Fail_Timewindows() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel1 = fleetModelBuilder.createVessel("My Vessel 1", vesselClass);

		final LoadSlot load1 = cargoModelBuilder//
				.makeDESPurchase("L1", false, LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "5", 22.6, null) //
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

			final InsertDESPurchaseMoveHandler handler = injector.getInstance(InsertDESPurchaseMoveHandler.class);
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

			Assert.assertNull(movePair);

		});
	}
}