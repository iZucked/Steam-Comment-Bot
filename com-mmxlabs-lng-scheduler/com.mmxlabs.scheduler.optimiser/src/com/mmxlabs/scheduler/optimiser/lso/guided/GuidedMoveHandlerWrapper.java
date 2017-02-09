package com.mmxlabs.scheduler.optimiser.lso.guided;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.IGuidedMoveHandler;

/**
 * A wrapper class to be able to use the Guided Move generator handlers as a normal move generator. Once constructed, this class needs to be injected from Guice.
 * 
 * @author Simon Goodall
 *
 */
@NonNullByDefault
public class GuidedMoveHandlerWrapper implements IMoveGenerator {

	@Inject
	private MoveTypeHelper moveTypeHelper;

	private final IGuidedMoveHandler handler;

	private final GuideMoveGeneratorOptions options;

	private final MoveTypes moveType;

	public GuidedMoveHandlerWrapper(final MoveTypes moveType, final IGuidedMoveHandler handler) {
		this.moveType = moveType;
		this.handler = handler;

		// Setup the options needed for the moves. Really only strictOptional and ignoreUsedElements is used by the move handlers
		this.options = new GuideMoveGeneratorOptions();
		// These options could be used by the move handlers
		options.setIgnoreUsedElements(false);
		options.setStrictOptional(true);
		// These options are not expected to be used
		options.setCheckingMove(false);
		options.setExtendSearch(false);
		options.setNum_tries(1);
	}

	@Override
	public @Nullable IMove generateMove(@NonNull final ISequences rawSequences, @NonNull final ILookupManager lookupManager, @NonNull final Random random) {

		final List<ISequenceElement> possibleElements = findPossibleElements(rawSequences);

		if (possibleElements.isEmpty()) {
			return null;
		}

		Collections.shuffle(possibleElements, random);
		for (final ISequenceElement element : possibleElements) {
			assert element != null;
			final Pair<IMove, Hints> p = handler.handleMove(lookupManager, element, random, options, Collections.emptyList());
			if (p != null) {
				return p.getFirst();
			}
		}
		return null;
	}

	/**
	 * Find all compatible elements for the {@link MoveTypes}. This should probably be generated once per accepted solution and passed in rather than recomputing each time. Note: Some of this may be
	 * pre-computable, and further filtered at each accepted solution. E.g. some move types depend on the currently assigned resource.
	 * 
	 * @param rawSequences
	 * @return
	 */
	private List<ISequenceElement> findPossibleElements(final ISequences rawSequences) {
		final List<ISequenceElement> possibleElements = new LinkedList<>();

		for (final IResource resource : rawSequences.getResources()) {
			final ISequence sequence = rawSequences.getSequence(resource);
			for (final ISequenceElement element : sequence) {
				final List<MoveTypes> moveTypes = moveTypeHelper.getMoveTypes(resource, element);
				if (moveTypes.contains(moveType)) {
					possibleElements.add(element);
				}
			}
		}
		for (final ISequenceElement element : rawSequences.getUnusedElements()) {
			final List<MoveTypes> moveTypes = moveTypeHelper.getMoveTypes(null, element);
			if (moveTypes.contains(moveType)) {
				possibleElements.add(element);
			}
		}
		return possibleElements;
	}
}
