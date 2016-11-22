package com.mmxlabs.scheduler.optimiser.lso.guided.moves;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;

public class CompoundMove implements IMove {

	private final @NonNull List<IMove> moves;

	private final @NonNull Set<IResource> affectedResources = new LinkedHashSet<>();

	public CompoundMove(final @NonNull List<IMove> moves) {
		this.moves = new ArrayList<>(moves);
		for (final IMove move : moves) {
			affectedResources.addAll(move.getAffectedResources());
		}
	}

	@Override
	public boolean validate(final ISequences sequences) {
		return true;
	}

	@Override
	public void apply(final IModifiableSequences sequences) {

		for (final IMove move : moves) {
			move.apply(sequences);
		}
	}

	@Override
	public Collection<IResource> getAffectedResources() {
		return affectedResources;
	}
}
