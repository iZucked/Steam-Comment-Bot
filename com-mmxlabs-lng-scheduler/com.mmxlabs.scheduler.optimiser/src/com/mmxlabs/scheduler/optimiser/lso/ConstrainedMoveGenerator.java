/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Injector;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.Resource;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.providers.Followers;
import com.mmxlabs.scheduler.optimiser.providers.IAlternativeElementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * <p>
 * A move generator which tries to create moves that respect timed constraints, to avoid wasting too many cycles testing clearly impossible moves. Suggested in ticket #9.
 * </p>
 * <ol>
 * The moves generated should try to respect the following aspects, where possible.
 * <li>Time Windows + Voyage Durations</li>
 * <li>Port visit durations</li>
 * <li>Port restrictions</li>
 * <li>Load / Discharge ordering</li>
 * <li>Fixed load/discharge pairs</li>
 * <li>Vessel events</li>
 * <li>Vessel or class assignments</li>
 * </ol>
 * 
 * @author hinton
 * 
 * @param
 */
public class ConstrainedMoveGenerator implements IMoveGenerator {
	/**
	 * The proportion of moves which are related to optional elements
	 * 
	 * TODO make this a parameter.
	 */
	private static final double elementSwapMoveFrequency = 0.10;
	private static final double optionalMoveFrequency = 0.10;
	private static final double shuffleMoveFrequency = 0.20;
	private static final double swapMoveFrequency = 0.01;

	int breakableVertexCount = 0;

	int breakpointCount = 0;
	// private IOptimisationContext context;

	/**
	 * A list containing all the valid edges which could exist in a solution, expressed as pairs whose first element is the start of the edge and second the end.
	 */
	final protected ArrayList<Pair<ISequenceElement, ISequenceElement>> validBreaks = new ArrayList<Pair<ISequenceElement, ISequenceElement>>();

	@Inject
	private LegalSequencingChecker checker;

	private SequencesConstrainedMoveGeneratorUnit sequencesMoveGenerator;
	private OptionalConstrainedMoveGeneratorUnit optionalMoveGenerator;
	private ShuffleElementsMoveGenerator shuffleMoveGenerator;
	private SwapElementsInSequenceMoveGeneratorUnit swapElementsMoveGenerator;
	private ElementSwapMoveGenerator elementSwapMoveGenerator;

	private GuidedMoveGenerator guidedMoveGenerator;

	// TODO: Inject
	private boolean enableSwapElementsMoveGenerator = false;

	@Inject
	private IAlternativeElementProvider alternativeElementProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@com.google.inject.Inject(optional = true)
	private IOptionalElementsProvider optionalElementsProvider;

	@Inject
	private Injector injector;

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private IVirtualVesselSlotProvider virtualVesselSlotProvider;

	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Inject
	private IFollowersAndPreceders followersAndPreceders;

	@Inject
	IOptimisationData data;

	public static final String LSO_MOVES_SCMG = "CMG_LSOMovesSCGM";

	@com.google.inject.Inject(optional = true)
	@Named(LSO_MOVES_SCMG)
	private boolean isLoopingSCMG;

	@Inject
	public void init() {
		// this.checker = injector.getInstance(LegalSequencingChecker.class);
		// LegalSequencingChecker checker2 = new LegalSequencingChecker(context);
		int initialMaxLateness = checker.getMaxLateness();
		checker.disallowLateness();

		// Build of a map of special cargo elements for FOB/DES cargoes.
		final Map<ISequenceElement, IResource> spotElementMap = new HashMap<>();
		for (final IResource resource : data.getResources()) {
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
			if (vesselAvailability != null) {
				if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
					final ISequenceElement startElement = startEndRequirementProvider.getStartElement(resource);
					final ISequenceElement endElement = startEndRequirementProvider.getEndElement(resource);
					final ISequenceElement virtualElement = virtualVesselSlotProvider.getElementForVesselAvailability(vesselAvailability);

					spotElementMap.put(startElement, resource);
					spotElementMap.put(virtualElement, resource);
					spotElementMap.put(endElement, resource);
				}
			}
		}

		// create a massive lookup table, caching all legal sequencing decisions
		// this might be a terrible idea, we could just keep the checker instead
		// also need to fix the resource binding now
		for (final ISequenceElement e1 : data.getSequenceElements()) {
			if (!portTypeProvider.getPortType(e1).equals(PortType.End)) {
				breakableVertexCount++;
			}

			Followers<ISequenceElement> validFollowers = followersAndPreceders.getValidFollowers(e1);
			if (validFollowers.size() > 1) {
				for (ISequenceElement e2 : validFollowers) {
					validBreaks.add(new Pair<ISequenceElement, ISequenceElement>(e1, e2));
				}
			}
		}

		if (isLoopingSCMG) {
			this.sequencesMoveGenerator = new SequencesConstrainedLoopingMoveGeneratorUnit(this);
		} else {
			this.sequencesMoveGenerator = new SequencesConstrainedMoveGeneratorUnit(this);
		}

		injector.injectMembers(sequencesMoveGenerator);

		this.shuffleMoveGenerator = new ShuffleElementsMoveGenerator(this);
		injector.injectMembers(shuffleMoveGenerator);

		this.elementSwapMoveGenerator = new ElementSwapMoveGenerator(this);
		injector.injectMembers(elementSwapMoveGenerator);

		if (optionalElementsProvider != null) {
			if (optionalElementsProvider.getOptionalElements().size() > 0) {
				this.optionalMoveGenerator = new OptionalConstrainedMoveGeneratorUnit(this);
				injector.injectMembers(optionalMoveGenerator);
			} else {
				this.optionalMoveGenerator = null;
			}
		} else {
			this.optionalMoveGenerator = null;
		}
		// Disable within route element swap for round trip cargoes.
		// for (final IResource resource : data.getResources()) {
		// final IVesselAvailability vessel = vesselProvider.getVesselAvailability(resource);
		// if (vessel.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
		// this.swapElementsMoveGenerator = new SwapElementsInSequenceMoveGeneratorUnit(this);
		// injector.injectMembers(swapElementsMoveGenerator);
		// break;
		// }
		// }
		checker.setMaxLateness(initialMaxLateness);

	}

	public ArrayList<Pair<ISequenceElement, ISequenceElement>> getValidBreaks() {

		return validBreaks;
	}

	@Override
	public @Nullable IMove generateMove(@NonNull ISequences rawSequences, @NonNull ILookupManager stateManager, @NonNull Random random) {
		if (enableSwapElementsMoveGenerator && ((elementSwapMoveGenerator != null) && (random.nextDouble() < elementSwapMoveFrequency))) {
			return elementSwapMoveGenerator.generateMove(rawSequences, stateManager, random);
		} else if ((optionalMoveGenerator != null) && (random.nextDouble() < optionalMoveFrequency)) {
			return optionalMoveGenerator.generateMove(rawSequences, stateManager, random);
		} else if ((shuffleMoveGenerator != null) && (random.nextDouble() < shuffleMoveFrequency)) {
			return shuffleMoveGenerator.generateMove(rawSequences, stateManager, random);
		} else if ((swapElementsMoveGenerator != null) && (random.nextDouble() < swapMoveFrequency)) {
			return swapElementsMoveGenerator.generateMove(rawSequences, stateManager, random);
		} else {
			return sequencesMoveGenerator.generateMove(rawSequences, stateManager, random);
		}
	}
}