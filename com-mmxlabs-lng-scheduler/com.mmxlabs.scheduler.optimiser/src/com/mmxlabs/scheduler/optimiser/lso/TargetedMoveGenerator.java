/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuideMoveGeneratorOptions;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveGenerator.MoveResult;
import com.mmxlabs.scheduler.optimiser.providers.ISpotMarketSlotsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

@NonNullByDefault
public class TargetedMoveGenerator implements IMoveGenerator {

	protected static final Logger LOG = LoggerFactory.getLogger(TargetedMoveGenerator.class);

	@Inject
	private Injector injector;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private ISpotMarketSlotsProvider spotSlotsProvider;

	private GuideMoveGeneratorOptions options = makeDefaultOptions();

	/**
	 * Default target selector - Any unused slot or nominal cargo slot excluding spot market slots.
	 */
	private BiFunction<ISequences, Random, @Nullable ISequenceElement> targetElementSelector = (sequences, rnd) -> {
		final List<ISequenceElement> elements = new LinkedList<>();
		// Find nominal cargoes
		for (final IResource r : sequences.getResources()) {
			final IVesselCharter va = vesselProvider.getVesselCharter(r);
			if (va.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
				final ISequence s = sequences.getSequence(r);
				// 1 -> len -1 to exclude start and end elements.
				if (s.size() > 2) {
					for (int i = 1; i < s.size() - 1; ++i) {
						elements.add(s.get(i));
					}
				}
			}
		}

		// Add all unused elements
		elements.addAll(sequences.getUnusedElements());

		// Remove spot slots
		final Iterator<ISequenceElement> itr = elements.iterator();
		while (itr.hasNext()) {
			final ISequenceElement e = itr.next();
			if (spotSlotsProvider.isSpotMarketSlot(e)) {
				itr.remove();
			}
		}

		// Only shuffle if there are multiple elements to pick
		if (elements.isEmpty()) {
			return null;
		} else if (elements.size() == 1) {
			return elements.get(0);
		} else {
			Collections.shuffle(elements, rnd);
			return elements.get(0);
		}

	};

	@Override
	public @Nullable IMove generateMove(final ISequences rawSequences, final ILookupManager stateManager, final Random random) {

		final ISequenceElement target = targetElementSelector.apply(rawSequences, random);
		if (target == null) {
			return null;
		}

		final GuidedMoveGenerator mg = injector.getInstance(GuidedMoveGenerator.class);

		final MoveResult p = mg.generateMove(rawSequences, random, Collections.singletonList(target), null, options);
		if (p == null) {
			return null;
		}

		return p.getMove();

	}

	private GuideMoveGeneratorOptions makeDefaultOptions() {
		final GuideMoveGeneratorOptions options = GuideMoveGeneratorOptions.createDefault();

		// Set to true to do lateness and violation checks etc before returning a valid move.
		options.setCheckingMove(false);
		options.setExtendSearch(false);
		options.setStrictOptional(true);
		options.setCheckEvaluatedState(false);
		options.setIgnoreUsedElements(false);
		options.setInsertCanRemove(true);
		options.setNum_tries(3);

		return options;
	}

	/**
	 * Override the default element selector function
	 * 
	 * @param targetElementSelector
	 */
	public void setTargetElementSelector(final BiFunction<ISequences, Random, @Nullable ISequenceElement> targetElementSelector) {
		this.targetElementSelector = targetElementSelector;
	}

	public GuideMoveGeneratorOptions getOptions() {
		return options;
	}

	/**
	 * Allow default options to be overridden
	 * 
	 * @param options
	 */
	public void setOptions(GuideMoveGeneratorOptions options) {
		this.options = options;
	}
}
