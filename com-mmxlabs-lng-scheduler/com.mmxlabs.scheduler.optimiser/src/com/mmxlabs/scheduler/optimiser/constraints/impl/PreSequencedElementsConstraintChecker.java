package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesAttributesProvider;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.scheduler.optimiser.sequenceproviders.IPreSequencedSegmentProvider;

@NonNullByDefault
public class PreSequencedElementsConstraintChecker implements IConstraintChecker {

	private final String name;

	public PreSequencedElementsConstraintChecker(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(final ISequences fullSequences, final @Nullable Collection<IResource> changedResources, @Nullable final List<String> messages) {

		final ISequencesAttributesProvider providers = fullSequences.getProviders();
		final IPreSequencedSegmentProvider provider = providers.getProvider(IPreSequencedSegmentProvider.class);
		if (provider == null) {
			return true;
		}

		final Collection<IResource> loopResources;
		if (changedResources == null) {
			loopResources = fullSequences.getResources();
		} else {
			loopResources = changedResources;
		}

		for (final IResource resource : loopResources) {
			final ISequence sequence = fullSequences.getSequence(resource);
			ISequenceElement prev = null;
			for (final ISequenceElement e : sequence) {
				if (prev != null) {
					if (!provider.validSequence(prev, e)) {
						return false;
					}
				}
				prev = e;
			}
		}

		return true;
	}
}
