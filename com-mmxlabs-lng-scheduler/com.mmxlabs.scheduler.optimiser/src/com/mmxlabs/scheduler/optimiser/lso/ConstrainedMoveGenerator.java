/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.inject.Inject;
import javax.inject.Named;

import com.google.inject.Injector;
import com.mmxlabs.common.Pair;
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

	/**
	 * A reverse lookup table from elements to positions. The {@link Pair} is a item containing {@link Resource} index and position within the {@link ISequence}. There are some special cases here. A
	 * null Resource index means the {@link ISequenceElement} is not part of the main set of sequences. A value of zero or more indicates the position within the {@link ISequences#getUnusedElements()}
	 * list. A negative number means it is not for normal use. Currently -1 means the element is the unused pair of an alternative (@see {@link IAlternativeElementProvider}
	 */
	protected final Map<ISequenceElement, Pair<IResource, Integer>> reverseLookup = new HashMap<>();

	/**
	 * A reference to the current set of sequences, which will be used in generating moves
	 */
	protected ISequences sequences = null;

	protected Random random;

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
		if (random == null) {
			throw new IllegalStateException("Random number generator has not been set");
		}
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

			reverseLookup.put(e1, new Pair<>(null, 0));

			Followers<ISequenceElement> validFollowers = followersAndPreceders.getValidFollowers(e1);
			if (validFollowers.size() > 1) {
				for (ISequenceElement e2 : validFollowers) {
					validBreaks.add(new Pair<ISequenceElement, ISequenceElement>(e1, e2));
				}
			}
		}

		if (false) {
			this.guidedMoveGenerator = injector.getInstance(GuidedMoveGenerator.class);
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
	public IMove generateMove() {
		if (guidedMoveGenerator != null) {
			return guidedMoveGenerator.generateMove();
		}

		if (enableSwapElementsMoveGenerator && ((elementSwapMoveGenerator != null) && (random.nextDouble() < elementSwapMoveFrequency))) {
			return elementSwapMoveGenerator.generateMove();
		} else if ((optionalMoveGenerator != null) && (random.nextDouble() < optionalMoveFrequency)) {
			return optionalMoveGenerator.generateMove();
		} else if ((shuffleMoveGenerator != null) && (random.nextDouble() < shuffleMoveFrequency)) {
			return shuffleMoveGenerator.generateMove();
		} else if ((swapElementsMoveGenerator != null) && (random.nextDouble() < swapMoveFrequency)) {
			return swapElementsMoveGenerator.generateMove();
		} else {
			return sequencesMoveGenerator.generateMove();
		}
	}

	@Override
	public ISequences getSequences() {
		return sequences;
	}

	@Override
	public void setSequences(final ISequences sequences) {
		this.sequences = sequences;

		// build table for elements in conventional sequences
		for (IResource resource : data.getResources()) {
			final ISequence sequence = sequences.getSequence(resource);
			for (int j = 0; j < sequence.size(); j++) {
				final ISequenceElement element = sequence.get(j);
				reverseLookup.get(element).setBoth(resource, j);
				if (alternativeElementProvider.hasAlternativeElement(element)) {
					final ISequenceElement alt = alternativeElementProvider.getAlternativeElement(element);
					// Negative numbers now indicate alternative
					reverseLookup.get(alt).setBoth(null, -1);
				}
			}
		}

		// build table for excluded elements
		int x = 0;
		for (final ISequenceElement element : sequences.getUnusedElements()) {
			reverseLookup.get(element).setBoth(null, x);
			if (alternativeElementProvider.hasAlternativeElement(element)) {
				final ISequenceElement alt = alternativeElementProvider.getAlternativeElement(element);
				reverseLookup.get(alt).setBoth(null, -1);
			}
			x++;
		}

		sequencesMoveGenerator.setSequences(sequences);

		if (guidedMoveGenerator != null) {
			guidedMoveGenerator.setSequences(sequences);
		}
	}

	public Random getRandom() {
		return random;
	}

	public void setRandom(final Random random) {
		this.random = random;
	}

	/**
	 */
	public Map<ISequenceElement, Pair<IResource, Integer>> getReverseLookup() {
		return reverseLookup;
	}
}