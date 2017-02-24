package com.mmxlabs.scheduler.optimiser.lso.guided;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
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

	private final GuidedMoveTypes moveType;

	private @Nullable List<ISequenceElement> potentialElements = null;

	private @Nullable ISelectedElementFilter selectedElementFilter;

	public @Nullable ISelectedElementFilter getSelectedElementFilter() {
		return selectedElementFilter;
	}

	public void setSelectedElementFilter(final ISelectedElementFilter selectedElementFilter) {
		this.selectedElementFilter = selectedElementFilter;
	}

	public GuidedMoveHandlerWrapper(final GuidedMoveTypes moveType, final IGuidedMoveHandler handler) {
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

	@Inject
	public void initPossibleTargets(final IOptimisationData optimisationData) {
		potentialElements = new LinkedList<>();
		@NonNull
		final Map<ISequenceElement, @NonNull Collection<@NonNull GuidedMoveTypes>> moveTypes = moveTypeHelper.getMoveTypes(optimisationData);
		for (final ISequenceElement e : optimisationData.getSequenceElements()) {
			@NonNull
			final Collection<@NonNull GuidedMoveTypes> collection = moveTypes.get(e);
			if (collection != null && collection.contains(moveType)) {
				potentialElements.add(e);
			}
		}
	}

	@Override
	public @Nullable IMove generateMove(@NonNull final ISequences rawSequences, @NonNull final ILookupManager lookupManager, @NonNull final Random random) {

		final List<ISequenceElement> possibleElements = findPossibleElements(lookupManager, rawSequences);

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
	 * Find all compatible elements for the {@link GuidedMoveTypes}. This should probably be generated once per accepted solution and passed in rather than recomputing each time. Note: Some of this
	 * may be pre-computable, and further filtered at each accepted solution. E.g. some move types depend on the currently assigned resource.
	 * 
	 * @param rawSequences
	 * @return
	 */
	private List<ISequenceElement> findPossibleElements(final ILookupManager lookupManager, final ISequences rawSequences) {

		final List<ISequenceElement> possibleElements = new LinkedList<>();
		if (potentialElements != null) {
			for (final ISequenceElement element : potentialElements) {
				@Nullable
				final IResource currentResource = lookupManager.lookup(element).getFirst();
				final Collection<GuidedMoveTypes> moveTypes = moveTypeHelper.getMoveTypes(currentResource, element);
				if (moveTypes.contains(moveType)) {
					if (selectedElementFilter == null || selectedElementFilter.canSelect(element, currentResource)) {
						possibleElements.add(element);
					}
				}
			}
		} else {

			for (final IResource resource : rawSequences.getResources()) {
				final ISequence sequence = rawSequences.getSequence(resource);
				for (final ISequenceElement element : sequence) {
					final Collection<GuidedMoveTypes> moveTypes = moveTypeHelper.getMoveTypes(resource, element);
					if (moveTypes.contains(moveType)) {
						if (selectedElementFilter == null || selectedElementFilter.canSelect(element, resource)) {
							possibleElements.add(element);
						}
					}
				}
			}
			for (final ISequenceElement element : rawSequences.getUnusedElements()) {
				final Collection<GuidedMoveTypes> moveTypes = moveTypeHelper.getMoveTypes(null, element);
				if (moveTypes.contains(moveType)) {
					possibleElements.add(element);
				}
			}
		}
		return possibleElements;
	}
}
