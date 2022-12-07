/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.util.ToBooleanFunction;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.transformer.ui.analytics.marketability.MarketabilityWindowTrimmer;
import com.mmxlabs.models.lng.transformer.ui.analytics.viability.ViabilityWindowTrimmer;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.DisconnectedSegment;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;
import com.mmxlabs.scheduler.optimiser.providers.Followers;
import com.mmxlabs.scheduler.optimiser.scheduling.ICustomTimeWindowTrimmer;

public class InsertCargoSequencesGenerator {

	@Inject
	private @NonNull IMoveHelper helper;

	@Inject
	private @NonNull IFollowersAndPreceders followersAndPreceders;
	
	@Inject
	private @NonNull ViabilityWindowTrimmer trimmer;

	public void generateOptions(final ISequences sequences, final List<ISequenceElement> orderedCargoElements,
			final IResource targetResource, final ViabilityWindowTrimmer trimmer,
			final IPortSlot portSlot, final ToBooleanFunction<ISequences> action) {

		for (final ISequenceElement e : orderedCargoElements) {
			if (!helper.checkResource(e, targetResource)) {
				return;
			}
		}

		// Get list of preceders for first element
		final Followers<ISequenceElement> preceders = followersAndPreceders.getValidPreceders(orderedCargoElements.get(0));

		// Get list of followers for last element.
		final Followers<ISequenceElement> followers = followersAndPreceders.getValidFollowers(orderedCargoElements.get(orderedCargoElements.size() - 1));

		final ISequence seq = sequences.getSequence(targetResource);
		for (int i = 0; i < seq.size() - 1; ++i) {
			final ISequenceElement before = seq.get(i);
			final ISequenceElement after = seq.get(i + 1);
			if (preceders.contains(before) && followers.contains(after)) {
				// Valid insertion point
				final ModifiableSequences newSequences = new ModifiableSequences(sequences);
				final IModifiableSequence modifiableSequence = newSequences.getModifiableSequence(targetResource);
				modifiableSequence.insert(i + 1, new DisconnectedSegment(orderedCargoElements));
				trimmer.setTrim(portSlot, ViabilityWindowTrimmer.Mode.EARLIEST, 0);
				final boolean earliestValid = action.accept(newSequences);
				trimmer.setTrim(portSlot, ViabilityWindowTrimmer.Mode.LATEST, 0);
				final boolean latestValid = action.accept(newSequences);
				if (earliestValid && !latestValid) {
					for (int j = 0; j < 31; j++) {
						trimmer.setTrim(portSlot, ViabilityWindowTrimmer.Mode.SHIFT, j*24);
						final boolean newValid = action.accept(newSequences);
						if (newValid) break;
					}
				}
			}
		}
	}
	
	public void generateOptionsTemp(final ISequences sequences, final List<@NonNull ISequenceElement> orderedCargoElements,
			final @NonNull IResource targetResource,
			final IPortSlot portSlot, final ToBooleanFunction<ISequences> action) {

		for (final ISequenceElement e : orderedCargoElements) {
			if (!helper.checkResource(e, targetResource)) {
				return;
			}
		}
		final ISequence seq = sequences.getSequence(targetResource);
		final ISequenceElement load = orderedCargoElements.get(0);
		final ISequenceElement discharge = orderedCargoElements.get(orderedCargoElements.size() - 1);
		assert discharge != null;
		final ModifiableSequences newSequences = new ModifiableSequences(sequences);
		final IModifiableSequence modifiableSequence = newSequences.getModifiableSequence(targetResource);
		for(int i = 0; i < seq.size()-1; i++) {
			final ISequenceElement elem = seq.get(i);
			if(elem.equals(load)) {
				modifiableSequence.remove(i+1);
				modifiableSequence.insert(i+1, discharge);
				trimmer.setTrim(portSlot, ViabilityWindowTrimmer.Mode.EARLIEST, 0);
				final boolean earliestValid = action.accept(newSequences);
				trimmer.setTrim(portSlot, ViabilityWindowTrimmer.Mode.LATEST, 0);
				final boolean latestValid = action.accept(newSequences);
				if(earliestValid && !latestValid) {
					for (int j = 0; j < 31; j++) {
						trimmer.setTrim(portSlot, ViabilityWindowTrimmer.Mode.SHIFT, j*24);
						final boolean newValid = action.accept(newSequences);
						if (newValid) break;
					}
				}
			}
		}
	}
}
