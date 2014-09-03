/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;

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
	private static final double optionalMoveFrequency = 0.10;
	private static final double shuffleMoveFrequency = 0.20;
	private static final double swapMoveFrequency = 0.01;

	/**
	 * A structure caching the output of the {@link LegalSequencingChecker}. If an element x is in the set mapped to by key y, x can legally follow y under some circumstance
	 */
	protected final Map<ISequenceElement, Followers<ISequenceElement>> validFollowers = new HashMap<ISequenceElement, Followers<ISequenceElement>>();
	protected final Map<ISequenceElement, Followers<ISequenceElement>> validPreceeders = new HashMap<ISequenceElement, Followers<ISequenceElement>>();

	/**
	 * A reverse lookup table from elements to positions. The {@link Pair} is a item containing {@link Resource} index and position within the {@link ISequence}. There are some special cases here. A
	 * null Resource index means the {@link ISequenceElement} is not part of the main set of sequences. A value of zero or more indicates the position within the {@link ISequences#getUnusedElements()}
	 * list. A negative number means it is not for normal use. Currently -1 means the element is the unused pair of an alternative (@see {@link IAlternativeElementProvider}
	 */
	protected final Map<ISequenceElement, Pair<Integer, Integer>> reverseLookup = new HashMap<ISequenceElement, Pair<Integer, Integer>>();

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

	final protected class Followers<Q> implements Iterable<Q> {
		/**
		 * @param followers
		 */
		public Followers(final Collection<Q> followers) {
			backingList.addAll(followers);
			containsSet.addAll(followers);
		}

		private final List<Q> backingList = new ArrayList<Q>();
		private final Set<Q> containsSet = new HashSet<Q>();

		/**
		 * @return
		 */
		public int size() {
			return backingList.size();
		}

		/**
		 * @param nextInt
		 * @return
		 */
		public Q get(final int nextInt) {
			return backingList.get(nextInt);
		}

		/**
		 * @param firstElementInSegment
		 * @return
		 */
		public boolean contains(final Q firstElementInSegment) {
			return containsSet.contains(firstElementInSegment);
		}

		@Override
		public Iterator<Q> iterator() {
			return backingList.iterator();
		}
	}

	@Inject
	private LegalSequencingChecker checker;

	private SequencesConstrainedMoveGeneratorUnit sequencesMoveGenerator;
	private OptionalConstrainedMoveGeneratorUnit optionalMoveGenerator;
	private ShuffleElementsMoveGenerator shuffleMoveGenerator;
	private SwapElementsInSequenceMoveGeneratorUnit swapElementsMoveGenerator;

	protected final IOptimisationContext context;

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

	public ConstrainedMoveGenerator(final IOptimisationContext context) {
		this.context = context;
	}

	@Inject
	public void init() {
		if (random == null) {
			throw new IllegalStateException("Random number generator has not been set");
		}
		// this.checker = injector.getInstance(LegalSequencingChecker.class);
		// LegalSequencingChecker checker2 = new LegalSequencingChecker(context);
		checker.disallowLateness();
		final IOptimisationData data = context.getOptimisationData();

		// Build of a map of special cargo elements for FOB/DES cargoes.
		final Map<ISequenceElement, IResource> spotElementMap = new HashMap<>();
		for (final IResource resource : data.getResources()) {
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
			if (vesselAvailability != null) {
				if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
					final ISequenceElement startElement = startEndRequirementProvider.getStartElement(resource);
					@SuppressWarnings("unused")
					final ISequenceElement endElement = startEndRequirementProvider.getEndElement(resource);
					final ISequenceElement virtualElement = virtualVesselSlotProvider.getElementForVesselAvailability(vesselAvailability);

					spotElementMap.put(startElement, resource);
					spotElementMap.put(virtualElement, resource);
					spotElementMap.put(endElement, resource);
			}
		}

		// create a massive lookup table, caching all legal sequencing decisions
		// this might be a terrible idea, we could just keep the checker instead
		// also need to fix the resource binding now
		for (final ISequenceElement e1 : data.getSequenceElements()) {
			if (!portTypeProvider.getPortType(e1).equals(PortType.End)) {
				breakableVertexCount++;
			}

			reverseLookup.put(e1, new Pair<Integer, Integer>(0, 0));

			final LinkedHashSet<ISequenceElement> followers = new LinkedHashSet<ISequenceElement>();
			final LinkedHashSet<ISequenceElement> preceeders = new LinkedHashSet<ISequenceElement>();

			for (final ISequenceElement e2 : data.getSequenceElements()) {
				if (e1 == e2) {
					continue;
				}

				// Check for special cargo elements. A special element can only go on one resource and all three elements in the set must be on the same one.
				if (spotElementMap.containsKey(e1) && spotElementMap.containsKey(e2)) {
					final IResource r1 = spotElementMap.get(e1);
					final IResource r2 = spotElementMap.get(e2);
					if (r1 != r2) {
						continue;
					}
				}

				// This code segment yields a large speed up, but breaks ITS

				// If any of e1 or e2 is a special spot element then there is only one resource it is permitted to go on. Ignore the rest for sequencing checks.
				IResource spotResource = null;
				if (spotElementMap.containsKey(e1)) {
					spotResource = spotElementMap.get(e1);
				} else if (spotElementMap.containsKey(e2)) {
					spotResource = spotElementMap.get(e2);
				}

				final boolean allowForwardSequence = spotResource == null ? checker.allowSequence(e1, e2) : checker.allowSequence(e1, e2, spotResource);
				if (allowForwardSequence) {

					if (followers.size() == 1) {
						validBreaks.add(new Pair<ISequenceElement, ISequenceElement>(e1, followers.iterator().next()));
					}
					followers.add(e2);
					if (followers.size() > 1) {
						validBreaks.add(new Pair<ISequenceElement, ISequenceElement>(e1, e2));
					}
				}

				final boolean allowReverseSequence = spotResource == null ? checker.allowSequence(e2, e1) : checker.allowSequence(e2, e1, spotResource);
				if (allowReverseSequence) {
					preceeders.add(e2);
				}
			}

			validFollowers.put(e1, new Followers<ISequenceElement>(followers));
			validPreceeders.put(e1, new Followers<ISequenceElement>(preceeders));
		}

		this.sequencesMoveGenerator = new SequencesConstrainedMoveGeneratorUnit(this);
		injector.injectMembers(sequencesMoveGenerator);

		this.shuffleMoveGenerator = new ShuffleElementsMoveGenerator(this);
		injector.injectMembers(shuffleMoveGenerator);

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
		for (final IResource resource : data.getResources()) {
			final IVesselAvailability vessel = vesselProvider.getVesselAvailability(resource);
			if (vessel.getVesselInstanceType() == VesselInstanceType.CARGO_SHORTS) {
				this.swapElementsMoveGenerator = new SwapElementsInSequenceMoveGeneratorUnit(this);
				injector.injectMembers(swapElementsMoveGenerator);
				break;
			}
		}

	}

	@Override
	public IMove generateMove() {
		if ((optionalMoveGenerator != null) && (random.nextDouble() < optionalMoveFrequency)) {
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
		for (int i = 0; i < sequences.size(); i++) {
			final ISequence sequence = sequences.getSequence(i);
			for (int j = 0; j < sequence.size(); j++) {
				final ISequenceElement element = sequence.get(j);
				reverseLookup.get(element).setBoth(i, j);
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
	}

	public Random getRandom() {
		return random;
	}

	public void setRandom(final Random random) {
		this.random = random;
	}

	/**
	 */
	public Map<ISequenceElement, Followers<ISequenceElement>> getValidFollowers() {
		return validFollowers;
	}

	/**
	 */
	public Map<ISequenceElement, Followers<ISequenceElement>> getValidPreceeders() {
		return validPreceeders;
	}

	/**
	 */
	public Map<ISequenceElement, Pair<Integer, Integer>> getReverseLookup() {
		return reverseLookup;
	}
}