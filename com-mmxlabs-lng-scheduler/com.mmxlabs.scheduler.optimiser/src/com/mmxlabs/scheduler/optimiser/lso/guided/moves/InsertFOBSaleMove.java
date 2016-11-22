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

public class InsertFOBSaleMove implements IMove {

	public static class Builder {

		private boolean fobSaleInUse;
		private ISequenceElement fobSale;
		private IResource fobSaleResource;

		private ISequenceElement fobPurchase;
		private IResource currentFobPurchaseResource;

		public static Builder newMove() {
			return new Builder();
		}

		public Builder withUnusedFOBSale(@NonNull final IResource fobSaleResource, @NonNull final ISequenceElement element) {
			this.fobSaleInUse = false;
			this.fobSaleResource = fobSaleResource;
			this.fobSale = element;
			return this;
		}

		public Builder withUsedFOBSale(@NonNull final IResource fobSaleResource, @NonNull final ISequenceElement element) {
			this.fobSaleInUse = true;
			this.fobSaleResource = fobSaleResource;
			this.fobSale = element;
			return this;
		}

		public Builder withUnusedFOBPurchase(@NonNull final ISequenceElement element) {
			this.fobPurchase = element;
			this.currentFobPurchaseResource = null;
			return this;
		}

		public Builder withUsedFOBPurchase(@NonNull final IResource resource, @NonNull final ISequenceElement element) {
			this.fobPurchase = element;
			this.currentFobPurchaseResource = resource;
			return this;
		}

		public InsertFOBSaleMove create() {
			return new InsertFOBSaleMove(fobSaleResource, fobSale, fobPurchase, currentFobPurchaseResource);
		}
	}

	private final @NonNull List<IResource> affectedResources = new LinkedList<>();
	private final @NonNull IResource fobSaleResource;
	private final @NonNull ISequenceElement fobSale;
	private final @Nullable IResource fobPurchaseResource;
	private final @NonNull ISequenceElement fobPurchase;

	public InsertFOBSaleMove(final @NonNull IResource fobSaleResource, final @NonNull ISequenceElement fobSale, final @NonNull ISequenceElement fobPurchase,
			final @Nullable IResource fobPurchaseResource) {
		this.fobSaleResource = fobSaleResource;
		this.fobSale = fobSale;
		this.fobPurchase = fobPurchase;
		this.fobPurchaseResource = fobPurchaseResource;
		this.affectedResources.add(fobSaleResource);
		if (fobPurchaseResource != null) {
			this.affectedResources.add(fobPurchaseResource);
		}
	}

	@Override
	public Collection<IResource> getAffectedResources() {
		return affectedResources;
	}

	@Override
	public void apply(final IModifiableSequences sequences) {
		final IModifiableSequence fobSaleSequence = sequences.getModifiableSequence(fobSaleResource);
		fobSaleSequence.insert(1, fobSale);
		fobSaleSequence.insert(1, fobPurchase);

		sequences.getModifiableUnusedElements().remove(fobSale);

		final IResource pFobPurchaseResource = fobPurchaseResource;
		if (pFobPurchaseResource == null) {
			sequences.getModifiableUnusedElements().remove(fobPurchase);
		} else {
			final IModifiableSequence fobPurchaseSequence = sequences.getModifiableSequence(pFobPurchaseResource);
			fobPurchaseSequence.remove(fobPurchase);
		}
	}

	@Override
	public boolean validate(final ISequences sequences) {
		return true;
	}

}
