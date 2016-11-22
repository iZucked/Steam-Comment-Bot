package com.mmxlabs.scheduler.optimiser.lso.guided;

import java.util.Collection;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.ImplementedBy;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;

@ImplementedBy(GuidedMoveHelperImpl.class)
public interface IGuidedMoveHelper {

	IResource getDESPurchaseResource(@NonNull final ISequenceElement desPurchase);

	IResource getFOBSaleResource(@NonNull final ISequenceElement fobSale);

	boolean isLockedToVessel(@NonNull final ISequenceElement element);

	boolean isLoadSlot(@NonNull final ISequenceElement element);

	boolean isDischargeSlot(@NonNull final ISequenceElement element);

	boolean isFOBPurchase(@NonNull final ISequenceElement element);

	boolean isFOBSale(@NonNull final ISequenceElement element);

	boolean isDESPurchase(@NonNull final ISequenceElement element);

	boolean isDESSale(@NonNull final ISequenceElement element);

	boolean isVesselEvent(@NonNull final ISequenceElement element);

	boolean isDryDockEvent(@NonNull final ISequenceElement element);

	boolean isMaintenanceEvent(@NonNull final ISequenceElement element);

	boolean isSimpleCharterOutEvent(@NonNull final ISequenceElement element);

	boolean isRelocatedCharterOutEvent(@NonNull final ISequenceElement element);

	boolean checkPermittedResource(final ISequenceElement sequenceElement, final IResource resource);

	Random getSharedRandom();

	boolean isOptional(@NonNull final ISequenceElement element);

	boolean doesMovePassConstraints(final ISequences rawSequences);

	@NonNull
	Collection<IResource> getAllVesselResources();

	boolean isStrictOptional();

	boolean isStartOrEndSlot(@NonNull ISequenceElement element);
}
