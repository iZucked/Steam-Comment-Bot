package com.mmxlabs.scheduler.optimiser.lso.guided.moves;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;

public class InsertDESPurchaseMove implements IMove {

	public static class Builder {

		private boolean desPurchaseInUse;
		private ISequenceElement desPurchase;
		private IResource desPurchaseResource;

		private ISequenceElement desSale;
		private IResource currentDesSaleResource;

		public static Builder newMove() {
			return new Builder();
		}

		public Builder withUnusedDESPurchase(@NonNull final IResource desPurchaseResource, @NonNull final ISequenceElement element) {
			this.desPurchaseInUse = false;
			this.desPurchaseResource = desPurchaseResource;
			this.desPurchase = element;
			return this;
		}

		public Builder withUsedDESPurchase(@NonNull final IResource desPurchaseResource, @NonNull final ISequenceElement element) {
			this.desPurchaseInUse = true;
			this.desPurchaseResource = desPurchaseResource;
			this.desPurchase = element;
			return this;
		}

		public Builder withUnusedDESSale(@NonNull final ISequenceElement element) {
			this.desSale = element;
			this.currentDesSaleResource = null;
			return this;
		}

		public Builder withUsedDESSale(@NonNull final IResource resource, @NonNull final ISequenceElement element) {
			this.desSale = element;
			this.currentDesSaleResource = resource;
			return this;
		}

		public InsertDESPurchaseMove create() {
			return new InsertDESPurchaseMove(desPurchaseResource, desPurchase, desSale, currentDesSaleResource);
		}
	}

	private final @NonNull List<IResource> affectedResources = new LinkedList<>();
	private final @NonNull IResource desPurchaseResource;
	private final @NonNull ISequenceElement desPurchase;
	private final @Nullable IResource desSaleResource;
	private final @NonNull ISequenceElement desSale;

	public InsertDESPurchaseMove(final @NonNull IResource desPurchaseResource, final @NonNull ISequenceElement desPurchase, final @NonNull ISequenceElement desSale,
			final @Nullable IResource desSaleResource) {
		this.desPurchaseResource = desPurchaseResource;
		this.desPurchase = desPurchase;
		this.desSale = desSale;
		this.desSaleResource = desSaleResource;
		this.affectedResources.add(desPurchaseResource);
		if (desSaleResource != null) {
			this.affectedResources.add(desSaleResource);
		}
	}

	@Override
	public Collection<IResource> getAffectedResources() {
		return affectedResources;
	}

	@Override
	public void apply(final IModifiableSequences sequences) {
		final IModifiableSequence desPurchaseSequence = sequences.getModifiableSequence(desPurchaseResource);
		desPurchaseSequence.insert(1, desSale);
		desPurchaseSequence.insert(1, desPurchase);

		sequences.getModifiableUnusedElements().remove(desPurchase);

		final IResource pDesSaleResource = desSaleResource;
		if (pDesSaleResource == null) {
			sequences.getModifiableUnusedElements().remove(desSale);
		} else {
			final IModifiableSequence desSaleSequence = sequences.getModifiableSequence(pDesSaleResource);
			desSaleSequence.remove(desSale);
		}
	}

	@Override
	public boolean validate(final ISequences sequences) {
		return true;
	}

}
